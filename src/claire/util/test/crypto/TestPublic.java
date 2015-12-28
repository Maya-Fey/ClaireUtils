package claire.util.test.crypto;

import java.io.File;
import java.io.IOException;

import claire.util.crypto.publickey.rsa.RSACrypter;
import claire.util.crypto.publickey.rsa.RSAFastLargeKeyPair;
import claire.util.crypto.publickey.rsa.RSAFastStandardKeyPair;
import claire.util.crypto.publickey.rsa.RSALargeKeyPair;
import claire.util.crypto.publickey.rsa.RSAStandardKeyPair;
import claire.util.crypto.rng.RandUtils;
import claire.util.encoding.Hex;
import claire.util.logging.Log;
import claire.util.standards.crypto.IAsymmetric;
import claire.util.test.Test;

public final class TestPublic {

	public static void testSystems() throws InstantiationException, IOException
	{
		Log.info.println("Testing RSA with RSALargeKeyPair");
		File file = new File("test.key");
		RSALargeKeyPair pair = RSALargeKeyPair.factory.resurrect(file);
		RSACrypter crypt = new RSACrypter(pair);
		testSystem(crypt);
		Log.info.println("Testing RSA with RSAStandardKeyPair");
		file = new File("testrsamicro.key");
		RSAStandardKeyPair pair2 = RSAStandardKeyPair.factory.resurrect(file);
		crypt = new RSACrypter(pair2);
		testSystem(crypt);
		Log.info.println("Testing RSA Fast Large Keypair with 2 Primes");
		file = new File("testcrt1.key");
		RSAFastLargeKeyPair pair3 = RSAFastLargeKeyPair.factory.resurrect(file);
		crypt = new RSACrypter(pair3);
		testSystem(crypt);
		Log.info.println("Testing RSA Fast Large Keypair with 8 Primes");
		file = new File("testcrt2.key");
		RSAFastLargeKeyPair pair4 = RSAFastLargeKeyPair.factory.resurrect(file);
		crypt = new RSACrypter(pair4);
		testSystem(crypt);
		Log.info.println("Testing RSA Fast Standard Keypair with 2 Primes");
		file = new File("testcrt3.key");
		RSAFastStandardKeyPair pair5 = RSAFastStandardKeyPair.factory.resurrect(file);
		crypt = new RSACrypter(pair5);
		testSystem(crypt);
		Log.info.println("Testing RSA Fast Standard Keypair with 8 Primes");
		file = new File("testcrt4.key");
		RSAFastStandardKeyPair pair6 = RSAFastStandardKeyPair.factory.resurrect(file);
		crypt = new RSACrypter(pair6);
		testSystem(crypt);
	}
	
	public static void testSystem(IAsymmetric<?, ?> asy)
	{
		if(!(asy.encrypts() && asy.signs())) {
			Test.reportError("Invalid cryptosystem, supports neither encryption or signing.");
			return;
		}
		if(asy.encrypts())
		{
			Log.info.println("Testing encryption and decryption.");
			byte[] plain = new byte[asy.ciphertextSize() * 3];
			for(int i = 0; i < 3; i++)
				RandUtils.fillArr(plain, i * asy.ciphertextSize(), asy.plaintextSize());
			byte[] cipher = new byte[asy.ciphertextSize() * 3];
			for(int i = 0; i < 3; i++)
				asy.encryptBlock(plain, i * asy.ciphertextSize(), cipher, i * asy.ciphertextSize());
			boolean changed = false;
			for(int i = 0; i < 3; i++)
				for(int j = i * asy.ciphertextSize(); j < (i * asy.ciphertextSize()) + asy.plaintextSize(); j++)
					if(plain[j] != cipher[j]) {
						changed ^= true;
						i = 3;
						break;
					}
			if(!changed) {
				Test.reportError("Encryption had no effect.");
				return;
			}
			byte[] confirm = new byte[cipher.length];
			System.arraycopy(plain, 0, confirm, 0, cipher.length);
			for(int i = 0; i < 3; i++) 
				asy.encryptBlock(plain, i * asy.ciphertextSize());
			for(int i = 0; i < 3; i++)
				for(int j = i * asy.ciphertextSize(); j < (i * asy.ciphertextSize()) + asy.plaintextSize(); j++)
					if(cipher[j] != plain[j]) {
						Test.reportError("Copy encryption is not consistant with in place encryption");
						return;
					}
			System.arraycopy(confirm, 0, plain, 0, cipher.length);
			for(int i = 0; i < 3; i++) {
				asy.decryptBlock(cipher, i * asy.ciphertextSize(), plain, i * asy.ciphertextSize());
				asy.decryptBlock(cipher, i * asy.ciphertextSize());
			}
			for(int i = 0; i < 3; i++)
				for(int j = i * asy.ciphertextSize(); j < (i * asy.ciphertextSize()) + asy.plaintextSize(); j++)
					if(confirm[j] != plain[j]) {
						System.out.println(Hex.toHexString(confirm));
						System.out.println(Hex.toHexString(plain));
						Test.reportError("Encryption followed by decryption did not yield the same result.");
						return;
					}
			for(int i = 0; i < 3; i++)
				for(int j = i * asy.ciphertextSize(); j < (i * asy.ciphertextSize()) + asy.plaintextSize(); j++)
					if(cipher[j] != plain[j]) {
						Test.reportError("Copy decryption is not consistant with in place decryption");
						return;
					}
		}
		if(asy.signs())
		{
			Log.info.println("Testing Signing and verification.");
			byte[] message = new byte[asy.ciphertextSize() * 3];
			for(int i = 0; i < 3; i++)
				RandUtils.fillArr(message, i * asy.ciphertextSize(), asy.plaintextSize());
			byte[] sig1 = new byte[message.length];
			byte[] sig2 = new byte[message.length];
			System.arraycopy(message, 0, sig1, 0, message.length);
			for(int i = 0; i < 3; i++) {
				asy.signBlock(message, i * asy.ciphertextSize(), sig2, i * asy.ciphertextSize());
				asy.signBlock(sig1, i * asy.ciphertextSize());
			}
			for(int i = 0; i < 3; i++)
				for(int j = i * asy.ciphertextSize(); j < (i * asy.ciphertextSize()) + asy.plaintextSize(); j++)
					if(sig2[j] != sig1[j]) {
						Test.reportError("Copy signing is not consistant with in place signing.");
						return;
					}
			for(int i = 0; i < 3; i++)
				if(!asy.verify(sig1, i * asy.ciphertextSize(), message, i * asy.ciphertextSize())) {
					Test.reportError("Valid signature failed to verify.");
					return;
				}
			RandUtils.fillArr(sig2);
			for(int i = 0; i < 3; i++)
				if(asy.verify(sig2, i * asy.ciphertextSize(), message, i * asy.ciphertextSize())) {
					Test.reportError("Invalid signature succeeded in verification.");
					return;
				}
		}
	}
	
}

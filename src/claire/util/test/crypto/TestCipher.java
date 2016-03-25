package claire.util.test.crypto;

import claire.util.crypto.cipher.StateSpaceRequiredException;
import claire.util.crypto.cipher.key.block.KeyBlowfish;
import claire.util.crypto.cipher.primitive.block.AES;
import claire.util.crypto.cipher.primitive.block.Blowfish;
import claire.util.crypto.cipher.primitive.block.CAST5;
import claire.util.crypto.cipher.primitive.block.CAST6;
import claire.util.crypto.cipher.primitive.block.GOST;
import claire.util.crypto.cipher.primitive.block.RC2;
import claire.util.crypto.cipher.primitive.block.RC5_16;
import claire.util.crypto.cipher.primitive.block.RC5_32;
import claire.util.crypto.cipher.primitive.block.RC5_64;
import claire.util.crypto.cipher.primitive.block.RC6;
import claire.util.crypto.cipher.primitive.block.SEED;
import claire.util.crypto.cipher.primitive.block.Skipjack;
import claire.util.crypto.cipher.primitive.block.TEA;
import claire.util.crypto.cipher.primitive.block.XTEA;
import claire.util.crypto.cipher.primitive.block.XXTEA;
import claire.util.crypto.rng.RandUtils;
import claire.util.crypto.rng.primitive.FastXorShift;
import claire.util.crypto.rng.primitive.JRandom;
import claire.util.crypto.rng.primitive.XorShiftNG;
import claire.util.encoding.Hex;
import claire.util.logging.Log;
import claire.util.standards.IUUID;
import claire.util.standards.crypto.ISymmetric;
import claire.util.test.Test;

public class TestCipher {
	
	public static void testEncryptDecrypt(ISymmetric<?> cipher) throws StateSpaceRequiredException
	{
		JRandom rand = new JRandom();
		byte[] bytes = new byte[cipher.plaintextSize() + 4];
		for(int i = 4; i < bytes.length; i++)
			bytes[i] = rand.nextByte();
		byte[] ciphertext = new byte[cipher.plaintextSize() + 4];
		System.arraycopy(bytes, 4, ciphertext, 4, cipher.plaintextSize());
		cipher.encryptBlock(ciphertext, 4);
		
		boolean changed = false; 
		for(int i = 4; i < 4 + bytes.length; i++)
			if(bytes[i] != ciphertext[i]) {
				changed = true;
				break;
			}
		if(!changed) 
			Test.reportError("Error: Encryption didn't do anything");
		cipher.decryptBlock(ciphertext, 4);
		for(int i = 4; i < ciphertext.length; i++)
			if(bytes[i] != ciphertext[i]) {
				Log.info.println(Hex.toHex(bytes));
				Log.info.println(Hex.toHex(ciphertext));
				Test.reportError("Error: Encryption followed by decryption does not yield the original value");
				break;
			}
	}
	
	public static void testMethods(ISymmetric<?> cipher) throws StateSpaceRequiredException
	{
		JRandom rand = new JRandom();
		byte[] bytes = new byte[cipher.plaintextSize()];
		for(int i = 0; i < bytes.length; i++)
			bytes[i] = rand.nextByte();
		byte[] bytes2 = new byte[cipher.plaintextSize() + cipher.plaintextSize()];
		int offset = rand.nextInt(cipher.plaintextSize());
		System.arraycopy(bytes, 0, bytes2, offset, cipher.plaintextSize());
		byte[] cipher0 = new byte[cipher.plaintextSize()];
		System.arraycopy(bytes, 0, cipher0, 0, cipher.plaintextSize());
		byte[] cipher1 = new byte[cipher.plaintextSize()];
		cipher.encryptBlock(bytes2, offset, cipher1, 0);
		cipher.encryptBlock(cipher0);
		for(int i = 0; i < cipher.plaintextSize(); i++)
			if(cipher0[i] != cipher1[i]) {
				Test.reportError("Error: Encryption with offset is inconsistent with encryption without offset");
				break;
			}
		byte[] cipher2 = new byte[cipher.plaintextSize()];
		System.arraycopy(bytes, 0, cipher2, 0, cipher.plaintextSize());
		cipher.decryptBlock(cipher2);
		byte[] cipher3 = new byte[cipher.plaintextSize()];
		cipher.decryptBlock(bytes2, offset, cipher3, 0);
		for(int i = 0; i < cipher.plaintextSize(); i++)
			if(cipher3[i] != cipher2[i]) {
				Test.reportError("Error: Decryption with offset is inconsistent with encryption without offset");
				break;
			}
 	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void testCKeys(ISymmetric<?> cipher)
	{
		int seed = RandUtils.dprng.nextInt();
		XorShiftNG rand1 = new XorShiftNG(seed);
		XorShiftNG rand2 = new XorShiftNG(seed);
		cipher.genKey(rand2);
		if(!((IUUID) cipher.newKey(rand1)).equals(((IUUID) cipher.getKey())))
			Test.reportError("genKey(rand) is inconsistent with newKey(rand)");
	}
	
	public static void testCipher(ISymmetric<?> cipher) throws StateSpaceRequiredException
	{
		cipher.genKey(new JRandom());
		Log.info.println("Testing encryption and decryption...");
		testEncryptDecrypt(cipher);
		Log.info.println("Testing offsetted encryption and decryption...");
		testMethods(cipher);
		Log.info.println("Testing key generation...");
		testCKeys(cipher);
	}
	
	public static void runTests() throws StateSpaceRequiredException
	{
		byte[] blow = new byte[56];
		RandUtils.fillArr(blow, new FastXorShift());
		Log.info.println("Testing RC2...");
		testCipher(new RC2(128));
		Log.info.println("Testing RC5_16...");
		testCipher(new RC5_16(64));
		Log.info.println("Testing RC5_32...");
		testCipher(new RC5_32(64));
		Log.info.println("Testing RC5_64...");
		testCipher(new RC5_64(64));
		Log.info.println("Testing RC6...");
		testCipher(new RC6());
		Log.info.println("Testing Skipjack...");
		testCipher(new Skipjack());
		Log.info.println("Testing TEA...");
		testCipher(new TEA());
		Log.info.println("Testing XTEA...");
		testCipher(new XTEA(48));
		Log.info.println("Testing XXTEA...");
		testCipher(new XXTEA(4));
		Log.info.println("Testing Blowfish...");
		testCipher(new Blowfish(new KeyBlowfish(blow)));
		Log.info.println("Testing CAST5...");
		testCipher(new CAST5());
		Log.info.println("Testing CAST6...");
		testCipher(new CAST6());
		Log.info.println("Testing AES...");
		testCipher(new AES());
		Log.info.println("Testing SEED...");
		testCipher(new SEED());
		Log.info.println("Testing GOST...");
		testCipher(new GOST());
	}

}

package claire.util.crypto.publickey.rsa;

import java.util.Arrays;

import claire.util.math.primitive.UInt;
import claire.util.memory.Bits;
import claire.util.standards.crypto.IAsymmetric;

public class RSACrypter
	   implements IAsymmetric<RSAPrivateKey<?>, RSAPublicKey<?>> {

	private RSAPrivateKey<?> prvk;
	private RSAPublicKey<?> publk;
	
	private final UInt uint;
	private final int[] buffer;
	private byte[] temp;
	
	public RSACrypter(StdRSAKeyPair<?, ?, ?> keys)
	{
		this.prvk = keys.getPrivateKey();
		this.publk = keys.getPublicKey();
		buffer = new int[publk.modLen() << 1];
		uint = new UInt(buffer);
	}
	
	public RSACrypter(RSAPrivateKey<?> priv, RSAPublicKey<?> pub)
	{
		this.prvk = priv;
		this.publk = pub;
		if(pub.getLen() != priv.getLen())
			throw new java.lang.ExceptionInInitializerError("Public key and private key do not have matching lengths");
		buffer = new int[pub.modLen() << 1];
		uint = new UInt(buffer);
	}

	public RSAPrivateKey<?> getKey()
	{
		return this.prvk;
	}

	public void setKey(RSAPrivateKey<?> t)
	{
		this.prvk = t;
	}

	public RSAPublicKey<?> getPublicKey()
	{
		return this.publk;
	}

	public void setPublicKey(RSAPublicKey<?> pub)
	{
		this.publk = pub;
	}
	
	public void wipe()
	{
		Arrays.fill(temp, (byte) 0);
		Arrays.fill(buffer, 0);
		Arrays.fill(uint.getArr(), 0);
	}

	public int messageSize()
	{
		return publk.messageSize();
	}

	public int signatureSize()
	{
		return publk.signatureSize();
	}

	public int plaintextSize()
	{
		return publk.plaintextSize();
	}

	public int ciphertextSize()
	{
		return publk.ciphertextSize();  
	}

	public void reset() { }

	private void fromPlaintext(byte[] bytes, int start)
	{
		Bits.bytesToInts(bytes, start, buffer, 0, publk.getLen());
		Arrays.fill(buffer, publk.getLen(), publk.modLen(), 0);
	}
	
	private void fromCiphertext(byte[] bytes, int start)
	{
		Bits.bytesToInts(bytes, start, buffer, 0, publk.modLen());
	}
	
	private void toPlaintext(byte[] bytes, int start)
	{
		Bits.intsToBytes(buffer, 0, bytes, start, publk.getLen());	
	}
	
	private void toCiphertext(byte[] bytes, int start)
	{
		Bits.intsToBytes(buffer, 0, bytes, start, publk.modLen());
	}
	
	public void encryptBlock(byte[] block, int start)
	{
		fromPlaintext(block, start);	
		publk.crypt(uint);
		toCiphertext(block, start);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		fromPlaintext(block, start0);
		publk.crypt(uint);
		toCiphertext(out, start1);
	}

	public boolean verify(byte[] signature, int start, byte[] message, int start1)
	{
		fromCiphertext(signature, start);
		publk.crypt(uint);
		if(temp == null)
			temp = new byte[4];
		for(int i = 0; i < publk.getLen(); i++) {
			Bits.intToBytes(buffer[i], temp, 0);
			if(message[start1++] != temp[0])
				return false;
			if(message[start1++] != temp[1])
				return false;
			if(message[start1++] != temp[2])
				return false;
			if(message[start1++] != temp[3])
				return false;
		}
		return true;
	}

	public void decryptBlock(byte[] block, int start)
	{
		fromCiphertext(block, start);
		prvk.crypt(uint);
		toPlaintext(block, start);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		fromCiphertext(block, start0);
		prvk.crypt(uint);
		toPlaintext(out, start1);
	}

	public void signBlock(byte[] block, int start)
	{
		fromPlaintext(block, start);
		prvk.crypt(uint);
		toCiphertext(block, start);
	}

	public void signBlock(byte[] block, int start0, byte[] out, int start1)
	{
		fromPlaintext(block, start0);
		prvk.crypt(uint);
		toCiphertext(out, start1);
	}

	public boolean signs()
	{
		return true;
	}

	public boolean encrypts()
	{
		return true;
	}

}

package claire.util.crypto.cipher;

import claire.util.standards.crypto.ICipherMode;
import claire.util.standards.crypto.IDecrypter;
import claire.util.standards.crypto.IEncrypter;
import claire.util.standards.crypto.ISymmetric;
import claire.util.standards.crypto.ISymmetricKey;

public abstract class BasicMode<Key extends ISymmetricKey<Key>, 
								Cipher extends ISymmetric<Key>,
								Encrypter extends IEncrypter,
								Decrypter extends IDecrypter>
	   			implements ICipherMode<Key> {
	
	protected final Cipher cipher;
	protected Encrypter enc;
	protected Decrypter dec;
	
	public BasicMode(Cipher cipher)
	{
		this.cipher = cipher;
	}

	public BasicMode(Cipher cipher, Encrypter enc, Decrypter dec)
	{
		this.cipher = cipher;
		this.enc = enc;
		this.dec = dec;
	}
	
	public Encrypter getEncrypter()
	{
		return enc;
	}

	public Decrypter getDecrypter()
	{
		return dec;
	}
	
	public void encryptBlock(byte[] block, int start)
	{
		enc.encryptBlock(block, start);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		enc.encryptBlock(block, start0, out, start1);
	}

	public void decryptBlock(byte[] block, int start) 
	{
		dec.decryptBlock(block, start);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		dec.decryptBlock(block, start0, out, start1);
	}
	
	public Key getKey()
	{
		return cipher.getKey();
	}

	public void setKey(Key t)
	{
		cipher.setKey(t);
	}

	public void destroyKey()
	{
		cipher.destroyKey();
	}

	public int plaintextSize()
	{
		return cipher.plaintextSize();
	}
	
	public int ciphertextSize()
	{
		return cipher.plaintextSize();
	}

}

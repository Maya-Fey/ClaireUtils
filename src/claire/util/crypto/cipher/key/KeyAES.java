package claire.util.crypto.cipher.key;

import claire.util.standards._NAMESPACE;

public class KeyAES extends ByteKey<KeyAES> {

	public KeyAES(byte[] key, int size) 
	{
		super(KeyAES.class, key, size);
	}
	
	public KeyAES(byte[] key)
	{
		super(KeyAES.class, key);
	}
	
	protected int getLength(byte[] bytes, int size)
	{
		if(size == 16 || 
		   size == 24 ||
		   size == 32)
			return size;
		else
			throw new java.lang.IllegalArgumentException("AES keys are 128, 192, or 256 bits.");
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.AESKEY;
	}

	protected KeyAES construct(byte[] bytes)
	{
		return new KeyAES(bytes);
	}

}

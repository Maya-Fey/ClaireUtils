package claire.util.crypto.cipher.key;

import claire.util.standards._NAMESPACE;

public class KeyRC6 extends ByteKey<KeyRC6> {

	public KeyRC6(byte[] key, int size) 
	{
		super(KeyRC6.class, key, size);
	}
	
	public KeyRC6(byte[] key)
	{
		super(KeyRC6.class, key);
	}
	
	protected int getLength(byte[] bytes, int size)
	{
		if(size == 16 || 
		   size == 24 ||
		   size == 32)
			return size;
		else
			throw new java.lang.IllegalArgumentException("RC6 keys are 128, 192, or 256 bits.");
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYRC6;
	}

	protected KeyRC6 construct(byte[] bytes)
	{
		return new KeyRC6(bytes);
	}

}

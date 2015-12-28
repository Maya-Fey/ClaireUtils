package claire.util.crypto.cipher.key;

import claire.util.standards._NAMESPACE;

public class KeyRC2 extends ByteKey<KeyRC2> {

	public KeyRC2(byte[] key, int size) 
	{
		super(KeyRC2.class, key, size);
	}
	
	public KeyRC2(byte[] key)
	{
		super(KeyRC2.class, key);
	}
	
	protected int getLength(byte[] bytes, int size)
	{
		if(size < 1)
			return 1;
		if(size > 128)
			return 128;
		return size;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYRC2;
	}

	protected KeyRC2 construct(byte[] bytes)
	{
		return new KeyRC2(bytes);
	}

}

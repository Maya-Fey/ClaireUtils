package claire.util.crypto.cipher.key;

import claire.util.standards._NAMESPACE;

public class KeyRC5 extends ByteKey<KeyRC5> {

	public KeyRC5(byte[] key, int size) 
	{
		super(KeyRC5.class, key, size);
	}
	
	public KeyRC5(byte[] key)
	{
		super(KeyRC5.class, key);
	}
	
	protected int getLength(byte[] bytes, int size)
	{
		if(size < 1)
			return 1;
		if(size > 128)
			return 256;
		return size;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYRC5;
	}

	protected KeyRC5 construct(byte[] bytes)
	{
		return new KeyRC5(bytes);
	}

}

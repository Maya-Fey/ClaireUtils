package claire.util.crypto.cipher.key;

import claire.util.standards._NAMESPACE;

public class KeyBlowfish extends ByteKey<KeyBlowfish> {

	public KeyBlowfish(byte[] key, int size) 
	{
		super(KeyBlowfish.class, key, size);
	}
	
	public KeyBlowfish(byte[] key)
	{
		super(KeyBlowfish.class, key);
	}
	
	protected int getLength(byte[] bytes, int size)
	{
		if(size < 4)
			return 4;
		else if(size > 72)
			return 72;
		else
			return size;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYBLOWFISH;
	}

	protected KeyBlowfish construct(byte[] bytes)
	{
		return new KeyBlowfish(bytes);
	}

}

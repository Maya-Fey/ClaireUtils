package claire.util.crypto.cipher.key;

import claire.util.standards._NAMESPACE;

public class KeyTwofish extends ByteKey<KeyTwofish> {

	public KeyTwofish(byte[] key, int size) 
	{
		super(KeyTwofish.class, key, size);
	}
	
	public KeyTwofish(byte[] key)
	{
		super(KeyTwofish.class, key);
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
		return _NAMESPACE.KEYTWOFISH;
	}

	protected KeyTwofish construct(byte[] bytes)
	{
		return new KeyTwofish(bytes);
	}

}

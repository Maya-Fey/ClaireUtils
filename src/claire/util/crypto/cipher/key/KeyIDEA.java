package claire.util.crypto.cipher.key;

import claire.util.standards._NAMESPACE;

public class KeyIDEA 
	   extends ByteKey<KeyIDEA> {

	public KeyIDEA(byte[] key)
	{
		super(KeyIDEA.class, key);
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYIDEA;
	}

	protected int getLength(byte[] bytes, int len)
	{
		return 16;
	}

	protected KeyIDEA construct(byte[] bytes)
	{
		return new KeyIDEA(bytes);
	}

}

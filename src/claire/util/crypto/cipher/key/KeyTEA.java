package claire.util.crypto.cipher.key;

import claire.util.standards._NAMESPACE;

public class KeyTEA 
	   extends ByteKey<KeyTEA> {
	
	public KeyTEA(byte[] key, int size) 
	{
		super(KeyTEA.class, key, size);
	}
	
	public KeyTEA(byte[] key)
	{
		super(KeyTEA.class, key);
	}
	
	protected int getLength(byte[] bytes, int size)
	{
		return 16;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYTEA;
	}

	protected KeyTEA construct(byte[] bytes)
	{
		return new KeyTEA(bytes);
	}

}

package claire.util.crypto.cipher.key;

import claire.util.standards._NAMESPACE;

public class KeyCAST6 extends ByteKey<KeyCAST6> {

	public KeyCAST6(byte[] key, int size) 
	{
		super(KeyCAST6.class, key, size);
	}
	
	public KeyCAST6(byte[] key)
	{
		super(KeyCAST6.class, key);
	}
	
	protected int getLength(byte[] bytes, int size)
	{
		if(size < 16)
			return 16;
		if(size > 32)
			return 32;
		return size;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYCAST6;
	}

	protected KeyCAST6 construct(byte[] bytes)
	{
		return new KeyCAST6(bytes);
	}

}

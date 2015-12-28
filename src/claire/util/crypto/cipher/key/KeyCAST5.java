package claire.util.crypto.cipher.key;

import claire.util.standards._NAMESPACE;

public class KeyCAST5 extends ByteKey<KeyCAST5> {

	public KeyCAST5(byte[] key, int size) 
	{
		super(KeyCAST5.class, key, size);
	}
	
	public KeyCAST5(byte[] key)
	{
		super(KeyCAST5.class, key);
	}
	
	protected int getLength(byte[] bytes, int size)
	{
		if(size < 8)
			return 8;
		if(size > 16)
			return 16;
		return size;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYCAST5;
	}

	protected KeyCAST5 construct(byte[] bytes)
	{
		return new KeyCAST5(bytes);
	}

}

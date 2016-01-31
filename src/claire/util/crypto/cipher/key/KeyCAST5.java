package claire.util.crypto.cipher.key;

import claire.util.io.Factory;
import claire.util.standards._NAMESPACE;

public class KeyCAST5 
	   extends ByteKey<KeyCAST5> {

	public KeyCAST5(byte[] bytes) 
	{
		super(bytes);
	}

	public KeyCAST5 createDeepClone()
	{
		return new KeyCAST5(bytes);
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.KEYCAST5;
	}
	
	public Factory<KeyCAST5> factory()
	{
		return factory;
	}
	
	private static final KeyCAST5Factory factory = new KeyCAST5Factory();
	
	private static final class KeyCAST5Factory extends ByteKeyFactory<KeyCAST5> {

		public KeyCAST5Factory() 
		{
			super(KeyCAST5.class);
		}

		protected KeyCAST5 construct(byte[] key)
		{
			return new KeyCAST5(key);
		}
		
	}
}

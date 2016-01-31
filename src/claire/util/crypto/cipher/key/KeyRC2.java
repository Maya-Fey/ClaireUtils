package claire.util.crypto.cipher.key;

import claire.util.io.Factory;
import claire.util.standards._NAMESPACE;

public class KeyRC2 
	   extends ByteKey<KeyRC2> {

	public KeyRC2(byte[] bytes) 
	{
		super(bytes);
	}

	public KeyRC2 createDeepClone()
	{
		return new KeyRC2(bytes);
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.KEYRC2;
	}
	
	public Factory<KeyRC2> factory()
	{
		return factory;
	}
	
	private static final KeyRC2Factory factory = new KeyRC2Factory();
	
	private static final class KeyRC2Factory extends ByteKeyFactory<KeyRC2> {

		public KeyRC2Factory() 
		{
			super(KeyRC2.class);
		}

		protected KeyRC2 construct(byte[] key)
		{
			return new KeyRC2(key);
		}
		
	}
}

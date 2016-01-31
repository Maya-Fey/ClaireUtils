package claire.util.crypto.cipher.key;

import claire.util.io.Factory;
import claire.util.standards._NAMESPACE;

public class KeyBlowfish 
	   extends ByteKey<KeyBlowfish> {

	public KeyBlowfish(byte[] bytes) 
	{
		super(bytes);
	}

	public KeyBlowfish createDeepClone()
	{
		return new KeyBlowfish(bytes);
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.KEYCAST5;
	}
	
	public Factory<KeyBlowfish> factory()
	{
		return factory;
	}
	
	private static final KeyBlowfishFactory factory = new KeyBlowfishFactory();
	
	private static final class KeyBlowfishFactory extends ByteKeyFactory<KeyBlowfish> {

		public KeyBlowfishFactory() 
		{
			super(KeyBlowfish.class);
		}

		protected KeyBlowfish construct(byte[] key)
		{
			return new KeyBlowfish(key);
		}
		
	}
}

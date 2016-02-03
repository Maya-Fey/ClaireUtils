package claire.util.crypto.cipher.key;

import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;

public class KeyBlowfish 
	   extends ByteKey<KeyBlowfish> {

	public KeyBlowfish(final byte[] bytes) 
	{
		super(bytes);
	}

	public KeyBlowfish createDeepClone()
	{
		return new KeyBlowfish(bytes);
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.KEYBLOWFISH;
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

		protected KeyBlowfish construct(final byte[] key)
		{
			return new KeyBlowfish(key);
		}
		
	}
	
	public static final int test()
	{
		final byte[] ints = new byte[20];
		RandUtils.fillArr(ints);
		KeyBlowfish aes = new KeyBlowfish(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}
	
}

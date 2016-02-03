package claire.util.crypto.cipher.key;

import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;

public class KeyRC2 
	   extends ByteKey<KeyRC2> {

	public KeyRC2(final byte[] bytes) 
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

		protected KeyRC2 construct(final byte[] key)
		{
			return new KeyRC2(key);
		}
		
	}
	
	public static final int test()
	{
		final byte[] ints = new byte[16];
		RandUtils.fillArr(ints);
		KeyRC2 aes = new KeyRC2(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}
	
}

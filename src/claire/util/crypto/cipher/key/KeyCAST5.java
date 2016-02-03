package claire.util.crypto.cipher.key;

import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;

public class KeyCAST5 
	   extends ByteKey<KeyCAST5> {

	public KeyCAST5(final byte[] bytes) 
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

		protected KeyCAST5 construct(final byte[] key)
		{
			return new KeyCAST5(key);
		}
		
	}
	
	public static final int test()
	{
		final byte[] ints = new byte[10];
		RandUtils.fillArr(ints);
		KeyCAST5 aes = new KeyCAST5(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}
	
}

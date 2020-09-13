package claire.util.crypto.cipher.key.block;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.ByteKey;
import claire.util.crypto.cipher.key.ByteKeyFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;

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
	
	public KeyFactory<KeyRC2> factory()
	{
		return factory;
	}
	
	public static final KeyRC2Factory factory = new KeyRC2Factory();
	
	private static final class KeyRC2Factory extends ByteKeyFactory<KeyRC2> {

		public KeyRC2Factory() 
		{
			super(KeyRC2.class);
		}

		protected KeyRC2 construct(final byte[] key)
		{
			return new KeyRC2(key);
		}

		public KeyRC2 random(IRandom<?, ?> rand, CryptoString s) throws InstantiationException
		{
			int len = 64;
			if(s.args() > 0)
				len = s.nextArg().toInt();
			if(len < 1)
				throw new java.lang.InstantiationException("You cannot have a key length of zero or less");
			if((len & 7) != 0)
				throw new java.lang.InstantiationException("Key length must be divisible by 8");
			byte[] bytes = new byte[((len - 1) / 8) + 1];
			rand.readBytes(bytes);
			return new KeyRC2(bytes);
		}

		public int bytesRequired(CryptoString s)
		{
			int len = 64;
			if(s.args() > 0)
				len = s.nextArg().toInt();
			if(len < 1)
				throw new java.lang.IllegalArgumentException("You cannot have a key length of zero or less");
			if((len & 7) != 0)
				throw new java.lang.IllegalArgumentException("Key length must be divisible by 8");
			return ((len - 1) / 8) + 1;
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

package claire.util.crypto.cipher.key.block;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.ByteKey;
import claire.util.crypto.cipher.key.ByteKeyFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.math.MathHelper;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;

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
	
	public KeyFactory<KeyBlowfish> factory()
	{
		return factory;
	}
	
	public static final KeyBlowfishFactory factory = new KeyBlowfishFactory();
	
	private static final class KeyBlowfishFactory extends ByteKeyFactory<KeyBlowfish> {

		public KeyBlowfishFactory() 
		{
			super(KeyBlowfish.class);
		}

		protected KeyBlowfish construct(final byte[] key)
		{
			return new KeyBlowfish(key);
		}

		public KeyBlowfish random(IRandom<?, ?> rand, CryptoString s) throws InstantiationException
		{
			int len = 64;
			if(s.args() > 0)
				len = s.nextArg().toInt();
			if(len < 1)
				throw new java.lang.InstantiationException("You cannot have a key length of zero or less");
			byte[] bytes = new byte[((len - 1) / 8) + 1];
			rand.readBytes(bytes);
			MathHelper.truncate(bytes, len);
			return new KeyBlowfish(bytes);
		}

		public int bytesRequired(CryptoString s)
		{
			int len = 64;
			if(s.args() > 0)
				len = s.nextArg().toInt();
			if(len < 1)
				throw new java.lang.IllegalArgumentException("You cannot have a key length of zero or less");
			return ((len - 1) / 8) + 1;
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

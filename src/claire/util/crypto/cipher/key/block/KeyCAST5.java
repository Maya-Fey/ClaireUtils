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
	
	public KeyFactory<KeyCAST5> factory()
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

		public KeyCAST5 random(IRandom<?, ?> rand, CryptoString s)
		{
			byte[] bytes;
			if(s.args() > 0) {
				int amt = s.nextArg().toInt() / 8;
				if(amt > 12)
					throw new java.lang.IllegalArgumentException("CAST5 only supports keys of up to 128 bits");
				if(amt < 5)
					throw new java.lang.IllegalArgumentException("CAST5 only supports keys of greater than 40 bits");
				bytes = new byte[amt];
			} else
				bytes = new byte[12];
			rand.readBytes(bytes);
			return new KeyCAST5(bytes);
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

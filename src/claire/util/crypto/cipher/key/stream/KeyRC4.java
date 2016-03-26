package claire.util.crypto.cipher.key.stream;

import claire.util.crypto.cipher.key.ByteKey;
import claire.util.crypto.cipher.key.ByteKeyFactory;
import claire.util.io.Factory;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;

public class KeyRC4 
	   extends ByteKey<KeyRC4>{

	public KeyRC4(byte[] key) 
	{
		super(key);
	}
	
	public KeyRC4 createDeepClone()
	{
		return new KeyRC4(ArrayUtil.copy(this.bytes));
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYRC4;
	}
	
	public Factory<KeyRC4> factory()
	{
		return factory;
	}
	
	public static final KeyRC4Factory factory = new KeyRC4Factory();
	
	private static final class KeyRC4Factory extends ByteKeyFactory<KeyRC4>
	{

		public KeyRC4Factory() 
		{
			super(KeyRC4.class);
		}

		protected KeyRC4 construct(byte[] key)
		{
			return new KeyRC4(key);
		}
		
	}	

}

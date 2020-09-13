package claire.util.crypto.cipher.key.block;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.io.IOUtils;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeySimon64 
	   implements IKey<KeySimon64> {
	
	private long[] key;
	
	public KeySimon64(long[] key)
	{
		this.key = key;
	}
	
	public long[] getLongs()
	{
		return key;
	}

	public KeySimon64 createDeepClone() 
	{
		return new KeySimon64(ArrayUtil.copy(key));
	}

	public void export(IOutgoingStream stream) throws IOException 
	{
		stream.writeLongArr(key);
	}

	public void export(byte[] bytes, int offset) 
	{
		IOUtils.writeArr(key, bytes, offset);
	}

	public int exportSize() 
	{
		return 4 + 8 * key.length;
	}

	public int NAMESPACE() 
	{
		return _NAMESPACE.KEYSIMON64;
	}

	public boolean sameAs(KeySimon64 obj) 
	{
		return ArrayUtil.equals(obj.key, key);
	}

	public void erase() 
	{
		Arrays.fill(key, 0);
		key = null;
	}
	
	public static final KeySimon32Factory factory = new KeySimon32Factory();

	public KeyFactory<KeySimon64> factory() 
	{
		return factory;
	}
	
	public static final class KeySimon32Factory 
						 extends KeyFactory<KeySimon64>
	{

		public KeySimon32Factory() 
		{
			super(KeySimon64.class);
		}

		public KeySimon64 random(IRandom<?, ?> rand, CryptoString s) throws InstantiationException
		{
			int len = 2;
			if(s.args() > 0) {
				len = s.nextArg().toInt();
				if(!(len == 128 || (len == 192 || len == 256)))
					throw new java.lang.InstantiationException("Simon 64 only supports key lengths of 128, 192, or 256 bits");
				len /= 64;
			}
			return new KeySimon64(rand.readLongs(len));
		}

		public KeySimon64 resurrect(byte[] data, int start) throws InstantiationException 
		{
			return new KeySimon64(IOUtils.readLongArr(data, start));
		}

		public KeySimon64 resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeySimon64(stream.readLongArr());
		}
		
		public int bytesRequired(CryptoString s)
		{
			int len = 16;
			if(s.args() > 0) {
				len = s.nextArg().toInt();
				if(!(len == 128 || (len == 192 || len == 256)))
					throw new java.lang.IllegalArgumentException("Simon 64 only supports key lengths of 128, 192, or 256 bits");
				len /= 8;
			}
			return len;
		}
		
	}
	
	public static final int test()
	{
		long[] ints = new long[3];
		RandUtils.fillArr(ints);
		KeySimon64 aes = new KeySimon64(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

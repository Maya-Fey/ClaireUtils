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

public class KeyARIA 
	   implements IKey<KeyARIA> 
{

	private long[] key;
	
	public KeyARIA(long[] longs)
	{
		this.key = longs;
	}
	
	public long[] getLongs()
	{
		return key;
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
		return _NAMESPACE.KEYARIA;
	}

	public boolean sameAs(KeyARIA obj) 
	{
		return ArrayUtil.equals(obj.key, this.key);
	}
	
	public KeyARIA createDeepClone() 
	{
		return new KeyARIA(ArrayUtil.copy(this.key));
	}

	public void erase() 
	{
		Arrays.fill(this.key, 0L);
		this.key = null;
	}
	
	public static final KeyARIAFactory factory = new KeyARIAFactory();

	public KeyFactory<KeyARIA> factory() 
	{
		return factory;
	}
	
	public static final class KeyARIAFactory 
						 extends KeyFactory<KeyARIA>
	{

		public KeyARIAFactory() 
		{
			super(KeyARIA.class);
		}

		public KeyARIA random(IRandom<?, ?> rand, CryptoString s) throws InstantiationException 
		{
			int amt = 2;
			if(s.args() > 0) {
				int n = s.nextArg().toInt();
				if(!(n == 128 || (n == 192 || n == 256)))
					throw new java.lang.InstantiationException("ARIA only supports key lengths of 128, 192, and 256 bits");
				amt = n / 64;
			}
			return new KeyARIA(rand.readLongs(amt));
		}

		public KeyARIA resurrect(byte[] data, int start) throws InstantiationException
		{
			return new KeyARIA(IOUtils.readLongArr(data, start));
		}

		public KeyARIA resurrect(IIncomingStream stream) throws InstantiationException, IOException 
		{
			return new KeyARIA(stream.readLongArr());
		}

		public int bytesRequired(CryptoString s)
		{
			int amt = 16;
			if(s.args() > 0) {
				int n = s.nextArg().toInt();
				if(!(n == 128 || (n == 192 || n == 256)))
					throw new java.lang.IllegalArgumentException("ARIA only supports key lengths of 128, 192, and 256 bits");
				amt = n / 8;
			}
			return amt;
		}
		
	}

	public static final int test()
	{
		long[] ints = new long[3];
		RandUtils.fillArr(ints);
		KeyARIA aes = new KeyARIA(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}
	
}

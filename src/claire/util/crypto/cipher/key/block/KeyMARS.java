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

public class KeyMARS 
	   implements IKey<KeyMARS> {
	
	private int[] key;
	
	public KeyMARS(int[] key)
	{
		this.key = key;
	}
	
	public int[] getInts()
	{
		return key;
	}

	public KeyMARS createDeepClone()
	{
		return new KeyMARS(ArrayUtil.copy(key));
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeIntArr(key);
	}

	public void export(byte[] bytes, int offset)
	{
		IOUtils.writeArr(key, bytes, offset);
	}

	public int exportSize()
	{
		return 4 + 4 * key.length;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYMARS;
	}

	public boolean sameAs(KeyMARS obj)
	{
		return ArrayUtil.equals(key, obj.key);
	}

	public void erase()
	{
		Arrays.fill(key, 0);
		key = null;
	}
	
	public static final KeyMARSFactory factory = new KeyMARSFactory();

	public KeyFactory<KeyMARS> factory()
	{
		return factory;
	}
	
	public static final class KeyMARSFactory extends KeyFactory<KeyMARS>
	{

		public KeyMARSFactory()
		{
			super(KeyMARS.class);
		}

		public KeyMARS random(IRandom<?, ?> rand, CryptoString s) throws InstantiationException
		{
			int size = 4;
			if(s.args() > 0) {
				int len = s.nextArg().toInt();
				if((len & 31) != 0) {
					throw new java.lang.InstantiationException("MARS keys must have lengths divisible by 32 bits");
				}
				if(len < 128) {
					throw new java.lang.InstantiationException("MARS has a minimum key size of 128 bits");
				}
				if(len > 448) {
					throw new java.lang.InstantiationException("MARS has a maximum key size of 448 bits");
				}
				size = len / 32;
			}
			return new KeyMARS(rand.readInts(size));
		}
		
		public KeyMARS resurrect(byte[] data, int start) throws InstantiationException
		{
			return new KeyMARS(IOUtils.readIntArr(data, start));
		}

		public KeyMARS resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyMARS(stream.readIntArr());
		}
		
		public int bytesRequired(CryptoString s)
		{
			int size = 16;
			if(s.args() > 0) {
				int len = s.nextArg().toInt();
				if((len & 31) != 0) {
					throw new java.lang.IllegalArgumentException("MARS keys must have lengths divisible by 32 bits");
				}
				if(len < 128) {
					throw new java.lang.IllegalArgumentException("MARS has a minimum key size of 128 bits");
				}
				if(len > 448) {
					throw new java.lang.IllegalArgumentException("MARS has a maximum key size of 448 bits");
				}
				size = len / 8;
			}
			return size;
		}
		
	}
	
	public static final int test()
	{
		int[] ints = new int[7];
		RandUtils.fillArr(ints);
		KeyMARS aes = new KeyMARS(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

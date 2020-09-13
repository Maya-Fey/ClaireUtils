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

public class KeySimon32 
	   implements IKey<KeySimon32> {
	
	private int[] key;
	
	public KeySimon32(int[] key)
	{
		this.key = key;
	}
	
	public int[] getInts()
	{
		return key;
	}

	public KeySimon32 createDeepClone() 
	{
		return new KeySimon32(ArrayUtil.copy(key));
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
		return _NAMESPACE.KEYSIMON32;
	}

	public boolean sameAs(KeySimon32 obj) 
	{
		return ArrayUtil.equals(obj.key, key);
	}

	public void erase() 
	{
		Arrays.fill(key, 0);
		key = null;
	}
	
	public static final KeySimon32Factory factory = new KeySimon32Factory();

	public KeyFactory<KeySimon32> factory() 
	{
		return factory;
	}
	
	public static final class KeySimon32Factory 
						extends KeyFactory<KeySimon32>
	{

		public KeySimon32Factory() 
		{
			super(KeySimon32.class);
		}

		public KeySimon32 random(IRandom<?, ?> rand, CryptoString s) throws InstantiationException
		{
			int len = 2;
			if(s.args() > 0) {
				len = s.nextArg().toInt();
				if(!(len == 64 || (len == 96 || len == 128)))
					throw new java.lang.InstantiationException("Simon 32 only supports key lengths of 64, 96, or 128 bits");
				len /= 32;
			}
			return new KeySimon32(rand.readInts(len));
		}

		public KeySimon32 resurrect(byte[] data, int start) throws InstantiationException 
		{
			return new KeySimon32(IOUtils.readIntArr(data, start));
		}

		public KeySimon32 resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeySimon32(stream.readIntArr());
		}

		public int bytesRequired(CryptoString s)
		{
			int len = 8;
			if(s.args() > 0) {
				len = s.nextArg().toInt();
				if(!(len == 64 || (len == 96 || len == 128)))
					throw new java.lang.IllegalArgumentException("Simon 32 only supports key lengths of 64, 96, or 128 bits");
				len /= 8;
			}
			return len;
		}
		
	}
	
	public static final int test()
	{
		int[] ints = new int[3];
		RandUtils.fillArr(ints);
		KeySimon32 aes = new KeySimon32(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

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

public class KeySpeck32 implements IKey<KeySpeck32> {
	
	private int[] key;
	
	public KeySpeck32(int[] key)
	{
		this.key = key;
	}
	
	public int[] getInts()
	{
		return key;
	}

	public KeySpeck32 createDeepClone() 
	{
		return new KeySpeck32(ArrayUtil.copy(key));
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
		return _NAMESPACE.KEYSPECK32;
	}

	public boolean sameAs(KeySpeck32 obj) 
	{
		return ArrayUtil.equals(obj.key, key);
	}

	public void erase() 
	{
		Arrays.fill(key, 0);
		key = null;
	}
	
	public static final KeySpeck32Factory factory = new KeySpeck32Factory();

	public KeyFactory<KeySpeck32> factory() 
	{
		return factory;
	}
	
	public static final class KeySpeck32Factory 
						 extends KeyFactory<KeySpeck32>
	{

		public KeySpeck32Factory() 
		{
			super(KeySpeck32.class);
		}

		public KeySpeck32 random(IRandom<?, ?> rand, CryptoString s) throws InstantiationException
		{
			int len = 2;
			if(s.args() > 0) {
				len = s.nextArg().toInt();
				if(!(len == 64 || (len == 96 || len == 128)))
					throw new java.lang.InstantiationException("Speck 32 only supports key lengths of 64, 96, or 128 bits");
				len /= 32;
			}
			return new KeySpeck32(rand.readInts(len));
		}

		public KeySpeck32 resurrect(byte[] data, int start) throws InstantiationException 
		{
			return new KeySpeck32(IOUtils.readIntArr(data, start));
		}

		public KeySpeck32 resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeySpeck32(stream.readIntArr());
		}
		
		public int bytesRequired(CryptoString s)
		{
			int len = 8;
			if(s.args() > 0) {
				len = s.nextArg().toInt();
				if(!(len == 64 || (len == 96 || len == 128)))
					throw new java.lang.IllegalArgumentException("Speck 32 only supports key lengths of 64, 96, or 128 bits");
				len /= 8;
			}
			return len;
		}
		
	}
	
	public static final int test()
	{
		int[] ints = new int[3];
		RandUtils.fillArr(ints);
		KeySpeck32 aes = new KeySpeck32(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

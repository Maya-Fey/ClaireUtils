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

public class KeySimon16 
	   implements IKey<KeySimon16> {
	
	private short[] key;
	
	public KeySimon16(short[] key)
	{
		this.key = key;
	}
	
	public short[] getShorts()
	{
		return key;
	}

	public KeySimon16 createDeepClone() 
	{
		return new KeySimon16(ArrayUtil.copy(key));
	}

	public void export(IOutgoingStream stream) throws IOException 
	{
		stream.writeShortArr(key);
	}

	public void export(byte[] bytes, int offset) 
	{
		IOUtils.writeArr(key, bytes, offset);
	}

	public int exportSize() 
	{
		return 4 + 2 * key.length;
	}

	public int NAMESPACE() 
	{
		return _NAMESPACE.KEYSIMON16;
	}

	public boolean sameAs(KeySimon16 obj) 
	{
		return ArrayUtil.equals(obj.key, key);
	}

	public void erase() 
	{
		Arrays.fill(key, (short) 0);
		key = null;
	}
	
	public static final KeySimon32Factory factory = new KeySimon32Factory();

	public KeyFactory<KeySimon16> factory() 
	{
		return factory;
	}
	
	public static final class KeySimon32Factory 
						 extends KeyFactory<KeySimon16>
	{

		public KeySimon32Factory() 
		{
			super(KeySimon16.class);
		}

		public KeySimon16 random(IRandom<?, ?> rand, CryptoString s) throws InstantiationException
		{
			int len = 4;
			if(s.args() > 0) {
				len = s.nextArg().toInt();
				if(!(len == 32 || (len == 48 || len == 64)))
					throw new java.lang.InstantiationException("Simon 16 only supports key lengths of 32, 48, or 64 bits");
				len /= 16;
			}
			return new KeySimon16(rand.readShorts(len));
		}

		public KeySimon16 resurrect(byte[] data, int start) throws InstantiationException 
		{
			return new KeySimon16(IOUtils.readShortArr(data, start));
		}

		public KeySimon16 resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeySimon16(stream.readShortArr());
		}

		public int bytesRequired(CryptoString s)
		{
			int len = 8;
			if(s.args() > 0) {
				len = s.nextArg().toInt();
				if(!(len == 32 || (len == 48 || len == 64)))
					throw new java.lang.IllegalArgumentException("Simon 16 only supports key lengths of 32, 48, or 64 bits");
				len /= 8;
			}
			return len;
		}
		
	}
	
	public static final int test()
	{
		short[] ints = new short[3];
		RandUtils.fillArr(ints);
		KeySimon16 aes = new KeySimon16(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

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

public class KeySpeck16 implements IKey<KeySpeck16> {
	
	private short[] key;
	
	public KeySpeck16(short[] key)
	{
		this.key = key;
	}
	
	public short[] getShorts()
	{
		return key;
	}

	public KeySpeck16 createDeepClone() 
	{
		return new KeySpeck16(ArrayUtil.copy(key));
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
		return _NAMESPACE.KEYSPECK16;
	}

	public boolean sameAs(KeySpeck16 obj) 
	{
		return ArrayUtil.equals(obj.key, key);
	}

	public void erase() 
	{
		Arrays.fill(key, (short) 0);
		key = null;
	}
	
	public static final KeySpeck32Factory factory = new KeySpeck32Factory();

	public KeyFactory<KeySpeck16> factory() 
	{
		return factory;
	}
	
	public static final class KeySpeck32Factory 
						 extends KeyFactory<KeySpeck16>
	{

		public KeySpeck32Factory() 
		{
			super(KeySpeck16.class);
		}

		public KeySpeck16 random(IRandom<?, ?> rand, CryptoString s) throws InstantiationException
		{
			int len = 4;
			if(s.args() > 0) {
				len = s.nextArg().toInt();
				if(!(len == 32 || (len == 48 || len == 64)))
					throw new java.lang.InstantiationException("Speck 16 only supports key lengths of 32, 48, or 64 bits");
				len /= 16;
			}
			return new KeySpeck16(rand.readShorts(len));
		}

		public KeySpeck16 resurrect(byte[] data, int start) throws InstantiationException 
		{
			return new KeySpeck16(IOUtils.readShortArr(data, start));
		}

		public KeySpeck16 resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeySpeck16(stream.readShortArr());
		}
		
		public int bytesRequired(CryptoString s)
		{
			int len = 8;
			if(s.args() > 0) {
				len = s.nextArg().toInt();
				if(!(len == 32 || (len == 48 || len == 64)))
					throw new java.lang.IllegalArgumentException("Speck 16 only supports key lengths of 32, 48, or 64 bits");
				len /= 8;
			}
			return len;
		}
		
	}
	
	public static final int test()
	{
		short[] ints = new short[3];
		RandUtils.fillArr(ints);
		KeySpeck16 aes = new KeySpeck16(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

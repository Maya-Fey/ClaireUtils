package claire.util.crypto.cipher.key.stream;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.io.IOUtils;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeyRC4_DROP 
	   implements IKey<KeyRC4_DROP> {
		   
	private byte[] key;
	private int drop;

	public KeyRC4_DROP(byte[] key, int drop) 
	{
		this.key = key;
		this.drop = drop;
	}
	
	public byte[] getBytes()
	{
		return this.key;
	}
	
	public int getDropAmount()
	{
		return this.drop;
	}
	
	public KeyRC4_DROP createDeepClone()
	{
		return new KeyRC4_DROP(ArrayUtil.copy(this.key), this.drop);
	}
	
	public void erase()
	{
		this.drop = 0;
		Arrays.fill(key, (byte) 0);
		this.key = null;
	}
	
	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeByteArr(key);
		stream.writeInt(drop);
	}

	public void export(byte[] bytes, int offset)
	{
		offset = IOUtils.writeArr(this.key, bytes, offset);
		Bits.intToBytes(drop, bytes, offset);
	}

	public int exportSize()
	{
		return key.length + 8;
	}
	
	public boolean sameAs(KeyRC4_DROP obj)
	{
		return this.drop == obj.drop && ArrayUtil.equals(this.key, obj.key);
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYRC4_DROP;
	}
	
	public KeyFactory<KeyRC4_DROP> factory()
	{
		return factory;
	}
	
	public static final KeyRC4_DROPFactory factory = new KeyRC4_DROPFactory();
	
	private static final class KeyRC4_DROPFactory extends KeyFactory<KeyRC4_DROP>
	{

		public KeyRC4_DROPFactory() 
		{
			super(KeyRC4_DROP.class);
		}

		public KeyRC4_DROP resurrect(byte[] data, int start) throws InstantiationException
		{
			byte[] key = IOUtils.readByteArr(data, start);
			start += 4 + key.length;
			return new KeyRC4_DROP(key, Bits.intFromBytes(data, start));
		}

		public KeyRC4_DROP resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyRC4_DROP(stream.readByteArr(), stream.readInt());
		}

		public KeyRC4_DROP random(IRandom<?, ?> rand, CryptoString s)
		{
			int drop = 600;
			if(s.args() > 0) {
				drop = s.nextArg().toInt() & 0xFFFF; //Common-sense limit
				if(drop < 0)
					throw new java.lang.IllegalArgumentException("You cannot have a negative drop");
			}
			int amt = 255;
			if(s.args() > 1) {
				amt = s.nextArg().toInt();
				if(amt < 1)
					throw new java.lang.IllegalArgumentException("You cannot have a key length less than one");
				if(amt > 255)
					throw new java.lang.IllegalArgumentException("RC4 can accept a maximum of 255 bytes of key information");
			}
			byte[] bytes = rand.readBytes(amt);
			return new KeyRC4_DROP(bytes, drop);
		}

	
	}	
	
	public static final int test()
	{
		final byte[] ints = new byte[167];
		RandUtils.fillArr(ints);
		KeyRC4_DROP aes = new KeyRC4_DROP(ints, 512);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

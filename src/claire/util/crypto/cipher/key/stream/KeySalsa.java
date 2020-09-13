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

public class KeySalsa 
	   implements IKey<KeySalsa> {
		   
	private int[] key;
	private int rounds;

	public KeySalsa(int[] key, int rounds) 
	{
		this.key = key;
		this.rounds = rounds;
	}
	
	public int[] getInts()
	{
		return this.key;
	}
	
	public int getRounds()
	{
		return rounds;
	}
	
	public KeySalsa createDeepClone()
	{
		return new KeySalsa(ArrayUtil.copy(this.key), this.rounds);
	}
	
	public void erase()
	{
		this.rounds = 0;
		Arrays.fill(key, (int) 0);
		this.key = null;
	}
	
	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeIntArr(key);
		stream.writeInt(rounds);
	}

	public void export(byte[] ints, int offset)
	{
		offset = IOUtils.writeArr(this.key, ints, offset);
		Bits.intToBytes(rounds, ints, offset);
	}

	public int exportSize()
	{
		return key.length * 4 + 8;
	}
	
	public boolean sameAs(KeySalsa obj)
	{
		return this.rounds == obj.rounds && ArrayUtil.equals(this.key, obj.key);
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYSALSA;
	}
	
	public KeyFactory<KeySalsa> factory()
	{
		return factory;
	}
	
	public static final KeySalsaFactory factory = new KeySalsaFactory();
	
	private static final class KeySalsaFactory extends KeyFactory<KeySalsa>
	{

		public KeySalsaFactory() 
		{
			super(KeySalsa.class);
		}

		public KeySalsa resurrect(byte[] data, int start) throws InstantiationException
		{
			int[] key = IOUtils.readIntArr(data, start);
			start += 4 + key.length * 4;
			return new KeySalsa(key, Bits.intFromBytes(data, start));
		}

		public KeySalsa resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeySalsa(stream.readIntArr(), stream.readInt());
		}

		public KeySalsa random(IRandom<?, ?> rand, CryptoString s) throws InstantiationException
		{
			int rounds = 20;
			if(s.args() > 0) {
				rounds = s.nextArg().toInt() & 0xFFFF; //Common-sense limit
				if((rounds & 1) == 1)
					throw new java.lang.InstantiationException("Salsa must have an even number of rounds");
				if(rounds < 9)
					throw new java.lang.InstantiationException("Salsa must use a minimum of 8 rounds");
			}
			int len = 8;
			if(s.args() > 1) {
				len = s.nextArg().toInt();
				if(len != 128 && len != 256)
					throw new java.lang.InstantiationException("Salsa only supports key lengths of 128 or 256 bits");
				len /= 32;
			}
			int[] ints = rand.readInts(len);
			return new KeySalsa(ints, rounds);
		}

		public int bytesRequired(CryptoString s)
		{
			s.nextArg();
			int len = 32;
			if(s.args() > 1) {
				len = s.nextArg().toInt();
				if(len != 128 && len != 256)
					throw new java.lang.IllegalArgumentException("Salsa only supports key lengths of 128 or 256 bits");
				len /= 8;
			}
			return len;
		}
	
	}	
	
	public static final int test()
	{
		final int[] ints = new int[16];
		RandUtils.fillArr(ints);
		KeySalsa aes = new KeySalsa(ints, 50);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

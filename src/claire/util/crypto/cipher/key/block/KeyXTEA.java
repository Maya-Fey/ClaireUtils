package claire.util.crypto.cipher.key.block;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeyXTEA 
	   implements IKey<KeyXTEA> {
	
	/*
	 * 16 byte keys
	 */
	
	private int[] ints;
	private int rounds;
	
	public KeyXTEA(final int[] ints, final int rounds)
	{
		this.ints = ints;
		this.rounds = rounds;
	}
	
	public int getRounds()
	{
		return this.rounds;
	}
	
	public int[] getInts()
	{
		return this.ints;
	}
	
	public KeyXTEA createDeepClone()
	{
		return new KeyXTEA(ArrayUtil.copy(ints), rounds);
	}

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeInt(rounds);
		stream.writeInts(ints);
	}

	public void export(final byte[] bytes, int offset)
	{
		Bits.intToBytes(rounds, bytes, offset); offset += 4;
		Bits.intsToBytes(ints, 0, bytes, offset, 4);
	}
	
	public int exportSize()
	{
		return 20;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYXTEA;
	}

	public boolean sameAs(final KeyXTEA obj)
	{
		return ArrayUtil.equals(ints, obj.ints) && rounds == obj.rounds;
	}

	public void erase()
	{
		Arrays.fill(ints, 0);
		this.ints = null;
		this.rounds = 0;
	}
	
	public KeyFactory<KeyXTEA> factory()
	{
		return factory;
	}
	
	public static final KeyTEAFactory factory = new KeyTEAFactory();

	public static final class KeyTEAFactory extends KeyFactory<KeyXTEA> {

		protected KeyTEAFactory() 
		{
			super(KeyXTEA.class);
		}

		public KeyXTEA resurrect(final byte[] data, int start) throws InstantiationException
		{
			int rounds = Bits.intFromBytes(data, start); 
			start += 4;
			int[] ints = new int[4];
			Bits.bytesToInts(data, start, ints, 0, 4);
			return new KeyXTEA(ints, rounds);
		}

		public KeyXTEA resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			final int rounds = stream.readInt();
			return new KeyXTEA(stream.readInts(4), rounds);
		}
		
		public KeyXTEA random(IRandom<?, ?> rand, CryptoString s)
		{
			int rounds = 32;
			if(s.args() > 0) {
				rounds = s.nextArg().toInt();
				if(rounds < 1) 
					throw new java.lang.IllegalArgumentException("XTEA requires at least 1 rounds");
				if(rounds > 8192) 
					throw new java.lang.IllegalArgumentException("Cannot do in excess of 8192 rounds");
			}
			return new KeyXTEA(rand.readInts(4), rounds);
		}

		public int bytesRequired(CryptoString s)
		{
			return 16;
		}
		
	}
	
	public static final int test()
	{
		final int[] ints = new int[4];
		RandUtils.fillArr(ints);
		KeyXTEA aes = new KeyXTEA(ints, 32);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

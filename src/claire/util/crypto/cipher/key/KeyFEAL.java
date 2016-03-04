package claire.util.crypto.cipher.key;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeyFEAL 
	   implements IKey<KeyFEAL> {
	
	private int[] ints;
	private int rounds;
	
	public KeyFEAL(final int[] ints, final int rounds)
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
	
	public KeyFEAL createDeepClone()
	{
		return new KeyFEAL(ArrayUtil.copy(ints), rounds);
	}

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeInt(rounds);
		stream.writeInts(ints);
	}

	public void export(final byte[] bytes, int offset)
	{
		Bits.intToBytes(rounds, bytes, offset); offset += 4;
		Bits.intsToBytes(ints, 0, bytes, offset, 2);
	}
	
	public int exportSize()
	{
		return 12;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYFEAL;
	}

	public boolean sameAs(final KeyFEAL obj)
	{
		return ArrayUtil.equals(ints, obj.ints) && rounds == obj.rounds;
	}

	public void erase()
	{
		Arrays.fill(ints, 0);
		this.ints = null;
		this.rounds = 0;
	}
	
	public Factory<KeyFEAL> factory()
	{
		return factory;
	}
	
	public static final KeyFEALFactory factory = new KeyFEALFactory();

	public static final class KeyFEALFactory extends Factory<KeyFEAL> {

		protected KeyFEALFactory() 
		{
			super(KeyFEAL.class);
		}

		public KeyFEAL resurrect(final byte[] data, int start) throws InstantiationException
		{
			int rounds = Bits.intFromBytes(data, start); 
			start += 4;
			int[] ints = new int[2];
			Bits.bytesToInts(data, start, ints, 0, 2);
			return new KeyFEAL(ints, rounds);
		}

		public KeyFEAL resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			final int rounds = stream.readInt();
			return new KeyFEAL(stream.readInts(2), rounds);
		}
		
	}
	
	public static final int test()
	{
		final int[] ints = new int[2];
		RandUtils.fillArr(ints);
		KeyFEAL aes = new KeyFEAL(ints, 32);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

package claire.util.crypto.rng.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class MersenneSeed 
	   implements IKey<MersenneSeed> {
	
	public int[] M;
	
	public MersenneSeed(int[] M)
	{
		this.M = M;
	}
	
	public MersenneSeed(int[] M, boolean neww)
	{
		if(neww)
			this.M = MersenneTwister.getSeed(M);
		else
			this.M = M;
	}
	
	public MersenneSeed(int seed)
	{
		this.M = MersenneTwister.getSeed(seed & 0xFFFFFFFFL);
	}
	
	public int[] getSeed()
	{
		return M;
	}

	public MersenneSeed createDeepClone()
	{
		return new MersenneSeed(ArrayUtil.copy(M));
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInts(M);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.intsToBytes(M, 0, bytes, offset, 624);
	}

	public int exportSize()
	{
		return 624 * 4;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.MERSENNESEED;
	}

	public boolean sameAs(MersenneSeed obj)
	{
		return ArrayUtil.equals(M, obj.M);
	}

	public void erase()
	{
		Arrays.fill(M, 0);
	}
	
	public Factory<MersenneSeed> factory()
	{
		return factory;
	}

	public static final MersenneSeedFactory factory = new MersenneSeedFactory();
	
	private static final class MersenneSeedFactory extends Factory<MersenneSeed>
	{

		protected MersenneSeedFactory() 
		{
			super(MersenneSeed.class);
		}

		public MersenneSeed resurrect(byte[] data, int start) throws InstantiationException
		{
			return new MersenneSeed(Bits.bytesToInts(data, start, 624));
		}

		public MersenneSeed resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new MersenneSeed(stream.readInts(624));
		}
		
	}
	
	public static final int test()
	{
		final MersenneSeed aes = new MersenneSeed(432432432);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

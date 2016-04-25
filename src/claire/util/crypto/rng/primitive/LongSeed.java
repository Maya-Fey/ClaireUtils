package claire.util.crypto.rng.primitive;

import java.io.IOException;

import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class LongSeed 
	   implements IKey<LongSeed> {

	private long seed;
	
	public LongSeed(long l) 
	{
		this.seed = l;
	}
	
	public long getSeed()
	{
		return seed;
	}

	public LongSeed createDeepClone()
	{
		return new LongSeed(seed);
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeLong(seed);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.longToBytes(seed, bytes, offset);
	}

	public int exportSize()
	{
		return 8;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.LONGSEED;
	}

	public boolean sameAs(LongSeed obj)
	{
		return this.seed == obj.seed;
	}

	public void erase()
	{
		this.seed = 0;
	}
	
	public Factory<LongSeed> factory()
	{
		return factory;
	}
	
	public static final LongSeedFactory factory = new LongSeedFactory();
	
	private static final class LongSeedFactory extends Factory<LongSeed>
	{

		protected LongSeedFactory() 
		{
			super(LongSeed.class);
		}

		public LongSeed resurrect(byte[] data, int start) throws InstantiationException
		{
			return new LongSeed(Bits.longFromBytes(data, start));
		}

		public LongSeed resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new LongSeed(stream.readLong());
		}
		
	}
	
	

}

package claire.util.crypto.rng.primitive;

import java.io.IOException;

import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.io.IOutgoingStream;

public class LongSeed 
	   implements IKey<LongSeed> {

	private long seed;
	
	public LongSeed(long l) 
	{
		this.seed = l;
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
		// TODO Auto-generated method stub
		return null;
	}
	
	

}

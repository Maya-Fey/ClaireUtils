package claire.util.crypto.rng.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.io.IOutgoingStream;

public class MersenneSeed 
	   implements IKey<MersenneSeed> {
	
	public int[] M;
	
	public MersenneSeed(int[] M)
	{
		this.M = M;
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
		// TODO Auto-generated method stub
		return null;
	}

}

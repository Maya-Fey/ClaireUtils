package claire.util.crypto.rng.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.io.IOUtils;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IOutgoingStream;

public class MersenneState 
	   implements IState<MersenneState> {
	
	private int[] M;
	private int pos;
	
	public MersenneState(int[] M, int pos)
	{
		this.M = M;
		this.pos = pos;
	}
	
	public MersenneState(int[] M, boolean neww)
	{
		if(neww)
			this.M = MersenneTwister.getSeed(M);
		else
			this.M = M;
	}
	
	public MersenneState(int seed)
	{
		this.M = MersenneTwister.getSeed(seed & 0xFFFFFFFFL);
	}
	
	public int[] getSeed()
	{
		return M;
	}
	
	public int getPos()
	{
		return pos;
	}

	public MersenneState createDeepClone()
	{
		return new MersenneState(ArrayUtil.copy(M), pos);
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInts(M);
		stream.writeInt(pos);
	}

	public void export(byte[] bytes, int offset)
	{
		offset = IOUtils.writeArr(M, bytes, offset);
		Bits.intToBytes(pos, bytes, offset);
	}

	public int exportSize()
	{
		return 624 * 4 + 4;
	}

	public int stateID()
	{
		return IState.MERSENNESTATE;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.MERSENNESTATE;
	}

	public boolean sameAs(MersenneState obj)
	{
		return ArrayUtil.equals(M, obj.M) && this.pos == obj.pos;
	}

	public void erase()
	{
		Arrays.fill(M, 0);
		pos = 0;
	}
	
	public KeyFactory<MersenneState> factory()
	{
		return null;
	}

}

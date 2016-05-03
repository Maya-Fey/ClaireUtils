package claire.util.crypto.rng.primitive;

import java.io.IOException;

import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IOutgoingStream;

public class LongState 
	   implements IState<LongState> {
	
	private long state;
	
	public LongState(long state)
	{
		this.state = state;
	}
	
	public long getState()
	{
		return state;
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeLong(state);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.longToBytes(state, bytes, offset);
	}

	public int exportSize()
	{
		return 8;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.LONGSTATE;
	}

	public boolean sameAs(LongState obj)
	{
		return obj.state == this.state;
	}

	public void erase()
	{
		this.state = 0;
	}

	public int stateID()
	{
		return IState.LONGSTATE;
	}
	
	public Factory<LongState> factory()
	{
		// TODO Auto-generated method stub
		return null;
	}

}

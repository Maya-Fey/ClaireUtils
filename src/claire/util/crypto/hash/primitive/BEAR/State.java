package claire.util.crypto.hash.primitive.BEAR;

import claire.util.crypto.rng.primitive.MersenneTwister;
import claire.util.memory.Bits.IntBuilder;

final class State {
	
	private final MersenneTwister prng;
	
	private final byte[] IMP;
	private final byte[] MIR;
	
	private final byte[] STATE;
	
	private final int N;
	
	private int pos;
	
	public State(byte[] IMP, byte[] MIR, MersenneTwister p, int N)
	{
		this.prng = p;
		this.IMP = IMP;
		this.MIR = MIR;
		this.N = N;
		STATE = new byte[N];
		pos = -1;
	}
	
	public void next()
	{
		byte[] ARR;
		if(pos > (N >> 1))
			ARR = IMP;
		else
			ARR = MIR;
		IntBuilder build = new IntBuilder();
		int len = ARR.length, pos = 0, prev = 0, stateptr = 0;
		int[] next = new int[624];
		for(int i = 0; i < 624; i++) 
		{	
			pos += ((int) STATE[(stateptr++) % N] & 0xFF);
			build.add(ARR[pos % len]);
			pos += ((int) STATE[(stateptr++) % N] & 0xFF);
			build.add(ARR[pos % len]);
			pos += ((int) STATE[(stateptr++) % N] & 0xFF);
			build.add(ARR[pos % len]);
			pos += ((int) STATE[(stateptr++) % N] & 0xFF);
			build.add(ARR[pos % len]);
			next[i] = build.get() + prev;
			prev = next[i];			
		}
		prng.setSeed(next);
	}
	
	public void update(byte prev)
	{
		STATE[(N - pos) - 1] = prev;
	}
	
	public byte nextByte()
	{
		pos = (pos + 1);
		if(pos == N)
			pos = 0;
		return STATE[pos];
	}

}

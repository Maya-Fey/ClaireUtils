package claire.util.math.counters;

import java.util.Arrays;

import claire.util.memory.Bits;

public class LongCounter {
	
	private final long[] longs;
	
	public LongCounter(int size)
	{
		longs = new long[size];
	}
	
	public void add(long add)
	{
		longs[0] += add;
		int pos = 0;
		while(Bits.u_greaterThan(add, longs[pos++]))
		{
			longs[pos]++;
			add = 1;
		}
	}
	
	public void remove(long rem)
	{
		longs[0] -= rem;
		int pos = 0;
		while(Bits.u_greaterThan(longs[pos++], rem))
		{
			longs[pos]--;
			rem = 1;
		}
	}
	
	public long[] getLongs()
	{
		return this.longs;
	}
	
	public void reset()
	{
		Arrays.fill(longs, 0);
	}

}

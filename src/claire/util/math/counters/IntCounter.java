package claire.util.math.counters;

import java.util.Arrays;

import claire.util.memory.Bits;

public class IntCounter {
	
	private final int[] ints;
	
	public IntCounter(int size)
	{
		ints = new int[size];
	}
	
	public void add(int add)
	{
		ints[0] += add;
		int pos = 0;
		while(Bits.u_greaterThan(add, ints[pos++]))
		{
			ints[pos]++;
			add = 1;
		}
	}
	
	public void remove(int rem)
	{
		ints[0] -= rem;
		int pos = 0;
		while(Bits.u_greaterThan(ints[pos++], rem))
		{
			ints[pos]--;
			rem = 1;
		}
	}
	
	public int[] getInts()
	{
		return this.ints;
	}
	
	public void reset()
	{
		Arrays.fill(ints, 0);
	}

}

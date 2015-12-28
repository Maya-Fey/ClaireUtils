package claire.util.memory.pool;

import java.util.Arrays;

import claire.util.memory.util.Pointer;

public class IntPool {
	
	private final int[][] pool;
	private final boolean[] free;
	
	private int next = 0;
	
	public IntPool(int size, int max)
	{
		pool = new int[max][size];
		free = new boolean[max];
		Arrays.fill(free, true);
	}
	
	public int[] next(Pointer<Integer> pointer)
	{
		try {
		while(!free[next])
			next++;
		} catch (java.lang.ArrayIndexOutOfBoundsException e)
		{
			int i = 0;
			for(; i < free.length; i++)
				if(free[i]) {
					next = i;
					break;
				}
		}
		free[next] = false;
		pointer.set(new Integer(next));
		return pool[next];
	}
	
	public void free(int pos)
	{
		free[pos] = true;
		next = pos;
	}

}

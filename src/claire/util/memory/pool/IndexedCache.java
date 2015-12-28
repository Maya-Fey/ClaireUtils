package claire.util.memory.pool;

import java.util.Arrays;

import claire.util.standards.ICache;

public class IndexedCache<T> implements ICache<T> {
	
	protected final T[] cache;
	protected final int[] index;
	protected final int top;
	
	protected int head = -1;
	
	public IndexedCache(T[] t)
	{
		this.cache = t;
		this.top = t.length;
		this.index = new int[t.length];
		Arrays.fill(index, -1);
	}
	
	public void add(T t, int i)
	{
		head++;
		if(head == top)
			head = 0;
		this.index[head] = i;
		this.cache[head] = t;
	}
	
	public T get(int in)
	{
		for(int i = 0; i < top; i++)
			if(index[i] == in) {
				return cache[i];
			}
		return null;
	}
	

}

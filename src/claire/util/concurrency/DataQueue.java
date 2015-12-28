package claire.util.concurrency;

import java.util.Iterator;

public class DataQueue<T> 
	   implements Iterator<T> {
	
	private final T[] data;
	private final int len;
	
	private int pos = 0;
	private int next = 0;

	public DataQueue(T[] queue)
	{
		this.data = queue;
		len = queue.length;
	}
	
	public void add(T obj)
	{
		if(pos == next)
			throw new java.lang.ArrayIndexOutOfBoundsException("Queue overflow: No space remaining");
		data[pos++] = obj;
		if(pos == len)
			pos = 0;
	}
	
	public boolean hasNext()
	{
		return pos != next;
	}

	public T next()
	{
		try {
			return data[next++];
		} finally {
			if(next == len)
				next = 0;
		}
	}

}

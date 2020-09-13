package claire.util.memory.array;

import java.lang.reflect.Array;

import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IIterable;
import claire.util.standards.IIterator;

public class DynamicIndexedRegistry<T> 
	   extends IndexedRegistry<T> 
	   implements IIterable<T> {
	
	private final int overflowRate;

	public DynamicIndexedRegistry(Class<T> class_, int initSize) {
		super(class_, initSize);
		this.overflowRate = initSize;
	}
	
	public void overflow()
	{
		this.overflow(overflowRate);
		String[] n = ArrayUtil.upsize(index, overflowRate);
		this.index = n;
	}
	
	public void add(T t, String s)
	{
		if(this.current == index.length)
			this.overflow();
		this.index[this.current] = s;
		this.array[this.current++] = t;
	}
	
	public void add(T t)
	{
		if(this.current == index.length)
			this.overflow();
		this.index[this.current] = "NULL";
		this.array[this.current++] = t;
	}
	
	public T[] getFinal()
	{
		@SuppressWarnings("unchecked")
		T[] arr = (T[]) Array.newInstance(this.class_, this.current);
		System.arraycopy(this.array, 0, arr, 0, current);
		return arr;
	}
	
	public IIterator<T> iterator()
	{
		return new DIRator();
	}
	
	private final class DIRator
				  implements IIterator<T>
	{

		private final int max = DynamicIndexedRegistry.this.current;
		
		private int current = 0;

		public boolean hasNext()
		{
			return current < max;
		}

		public T next()
		{
			return DynamicIndexedRegistry.this.array[current++];
		}

		public void skip()
		{
			current++;
		}

		public void skip(int amt)
		{
			current += amt;
		}

	}

}

package claire.util.memory.array;

import java.lang.reflect.Array;
import java.util.Arrays;

import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IIterator;
import claire.util.standards.IPointer;

@SuppressWarnings("unchecked")
public class Memory<Type> {

	protected int rate;
	
	protected boolean[] free;
	protected Type[] array;
	
	protected int first = 0;
	
	public Memory(Class<Type> class_, int initSize) {
		this.array = (Type[]) Array.newInstance(class_, initSize);
		this.free = new boolean[initSize];
		Arrays.fill(free,  true);
		this.rate = initSize;
	}
	
	public Memory(Type[] arr)
	{
		this.array = arr;
		this.rate = arr.length / 2 + 1;
		this.free = new boolean[arr.length];
		Arrays.fill(this.free, false);		
	}
	
	public void setOverflowRate(int rate)
	{
		this.rate = rate;
	}
	
	protected void overflow()
	{
		this.array = ArrayUtil.upsize(array, rate);
		boolean[] n = new boolean[rate];
		Arrays.fill(n, true);
		this.free = ArrayUtil.concat(free, n);
	}
	
	public int preallocate()
	{
		while(first < free.length) 
			if(free[first]) 
				return first;
			else
				first++;
		this.overflow();
		return first;
	}
	
	public int allocate(Type t)
	{
		while(first < free.length) 
			if(free[first]) {
				array[first] = t;
				free[first] = false;
				return first++;
			} else
				first++;
		this.overflow();
		array[first] = t;
		free[first] = false;
		return first;
	}
	
	public void free(int pos)
	{
		first = pos;
		free[pos] = true;
	}
	
	public void overwrite(int pos, Type t)
	{
		array[pos] = t;
	}
	
	public Type get(int i)
	{
		return array[i];
	}
	
	/**
	 * Throws NullPointerException if no such thing is available.
	 */
	public int getNextOccupied(int i)
	{
		while(free[++i]);
		return i;
	}
	
	public Type[] getArr()
	{
		return this.array;
	}
	
	public int size()
	{
		return this.array.length;
	}
	
	public int length()
	{
		int total = 0;
		for(boolean b : free)
			if(b ^ true)
				total++;
		return total;
	}
	
	public void freeAll()
	{
		Arrays.fill(free, true);
	}
	
	public void secureWipe()
	{
		Arrays.fill(free, true);
		Arrays.fill(array, null);
	}
	
	public IPointer<Type> pointer(int index)
	{
		return this.new MPointer(index);
	}
	
	public IIterator<Type> iterator()
	{
		return this.new MIterator();
	}
	
	protected class MIterator implements IIterator<Type> 
	{
		protected int pos = 0;
		
		public boolean hasNext()
		{
			try {
				while(free[pos])
				{
					pos++;
				}
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				return false;
			}
			return true;
		}

		public Type next()
		{
			if(free[pos]) {
				while(free[pos])
				{
					pos++;
				}
			}
			try {
				return array[pos];
			} finally {
				pos++;
			}
		}
		
		public void skip()
		{
			pos++;
		}
		
		public void skip(int amt)
		{
			pos += amt;
		}
		
	}
	
	private final class MPointer implements IPointer<Type>
	{
		private final int index;
		
		public MPointer(int index)
		{
			this.index = index;
		}
		
		public Type get()
		{
			return array[index];
		}

		public void set(Type t)
		{
			throw new java.lang.IllegalAccessError();
		}
		
	}
	
}

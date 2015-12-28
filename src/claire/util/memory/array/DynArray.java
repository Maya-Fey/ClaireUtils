package claire.util.memory.array;

import java.util.Arrays;

public class DynArray<Type> 
	   extends CArray<Type> {

	protected int cur = 0;
	protected int overflowRate = 4;
	
	/**
	 * Adds the add(Type) function over traditional arrays. Note that add(Type) is slower then set(Type, int)
	 * 
	 * @author Claire
	 * @param class_
	 * @param initSize
	 */
	public DynArray(Class<Type> class_, int initSize) {
		super(class_, initSize);
	}
	
	public DynArray(Type[] t) {
		super(t);
	}

	public void add(Type t)
	{
		try {
			this.array[cur] = t;
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			this.overflow(this.overflowRate);
			this.array[cur] = t;
		}
		cur++;
	}
	
	public void setOverflowRate(int i)
	{
		this.overflowRate = i;
	}
	
	public int fill()
	{
		return this.cur;
	}
	
	public void ensureSize(int size)
	{
		this.overflow(size - this.array.length);
	}
	
	public void set(Type[] array)
	{
		final int len1 = this.array.length;
		final int len2 = array.length;
		if(len1 >= array.length) {
			System.arraycopy(array, 0, this.array, 0, len2);
			Arrays.fill(this.array, len2, len1, null);
		} else 
			this.array = array;
		cur = len2;
	}

}

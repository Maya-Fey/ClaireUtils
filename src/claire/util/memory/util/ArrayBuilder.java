package claire.util.memory.util;

import java.lang.reflect.Array;

@SuppressWarnings("unchecked")
public class ArrayBuilder<T> {
	
	private T[] arr;
	private Class<T> class_;
	private int rate;
	int pos = 0;
	
	public ArrayBuilder(Class<T> clasS, int overflow)
	{
		this.class_ = clasS;
		this.arr = (T[]) Array.newInstance(class_, overflow);
		this.rate = overflow;
	}
	
	public void addElement(T t)
	{
		try {
			arr[pos] = t;
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			this.overflow(this.rate);
			arr[pos] = t;
		}
		pos++;
	}
	
	public void overflow(int i)
	{
		this.arr = ArrayUtil.upsize(this.arr, i);
	}
	
	public T[] build()
	{
		if(pos == arr.length)
			return arr;
		T[] n = (T[]) Array.newInstance(class_, pos);
		System.arraycopy(arr, 0, n, 0, pos);
		return n;
	}

}

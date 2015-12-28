package claire.util.memory.array;

import java.lang.reflect.Array;

public class Buffer<T> extends DynArray<T> {
	
	protected final int initSize;

	public Buffer(Class<T> class_, int initSize) {
		super(class_, initSize);
		this.initSize = initSize;
	}
	
	public Buffer(T[] t)
	{
		super(t);
		this.initSize = t.length;
	}
	
	@SuppressWarnings("unchecked")
	public void clear()
	{
		this.array = (T[]) Array.newInstance(class_, initSize);
	}
	
	@SuppressWarnings("unchecked")
	public T[] get()
	{	
		T[] ret = (T[]) Array.newInstance(class_, this.cur);
		System.arraycopy(this.array, 0, ret, 0, this.cur);
		return ret;
	}

}

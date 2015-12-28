package claire.util.memory.pool;

public class StackIndexCache<T> extends IndexedCache<T>{
	
	public StackIndexCache(T[] t) {
		super(t);
	}

	public T get(int in)
	{
		for(int i = 0; i < top; i++)
			if(index[i] == in) {
				add(cache[i], index[i]);
				return cache[i];
			}
		return null;
	}

}

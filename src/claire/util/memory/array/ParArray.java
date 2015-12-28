package claire.util.memory.array;

public class ParArray<T> {
	
	final T[] arr;
	
	final int min;
	
	public ParArray(T[] t, int min)
	{
		this.arr = t;
		this.min = min;
	}
	
	public T get(int i)
	{
		return arr[i + min];
	}
	
	public void set(int i, T t)
	{
		arr[i + min] = t;
	}
	
	public int length()
	{
		return arr.length - min;
	}
	
	public static final class INT {
		
		final int[] arr;
		
		final int min;
		
		public INT(int[] t, int min)
		{
			this.arr = t;
			this.min = min;
		}
		
		public int get(int i)
		{
			try {
				return arr[i + min];
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				return 0;
			}
		}
		
		public void set(int i, int t)
		{
			try {
				arr[i + min] = t;
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				return;
			}
		}
		
		public int length()
		{
			return arr.length - min;
		}
		
	}

}

package claire.util.memory.array;

public class ComboArray<T> {
	
	private final T[] a1;
	private final T[] a2;
	
	private final int a1len;
	
	public ComboArray(T[] a1, T[] a2)
	{
		this.a1 = a1;
		this.a2 = a2;
		this.a1len = a1.length;
	}
	
	public T get(int i)
	{
		if(i >= a1len)
			return a2[i - a1len];
		else 
			return a1[i];
	}
	
	public void set(T t, int i)
	{
		if(i >= a1len)
			a2[i - a1len] = t;
		else 
			a1[i] = t;
	}
	
	public int length()
	{
		return a2.length + a1len;
	}

	public static class Byte {
		
		private final byte[] a1;
		private final byte[] a2;
		
		private final int a1len;
		
		public Byte(byte[] a1, byte[] a2)
		{
			this.a1 = a1;
			this.a2 = a2;
			this.a1len = a1.length;
		}
		
		public byte get(int i)
		{
			if(i >= a1len)
				return a2[i - a1len];
			else 
				return a1[i];
		}
		
		public void set(byte t, int i)
		{
			if(i >= a1len)
				a2[i - a1len] = t;
			else 
				a1[i] = t;
		}
		
		public int length()
		{
			return a2.length + a1len;
		}
		
	}
}

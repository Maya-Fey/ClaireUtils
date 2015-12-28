package claire.util.memory.util;

public class Pair<T, E> {
	
	private T t;
	private E e;
	
	public Pair(T t, E e)
	{
		this.t = t;
		this.e = e;
	}
	
	public void setObject(T t)
	{
		this.t = t;
	}
	
	public void setPair(E e)
	{
		this.e = e;
	}
	
	public T getObject()
	{
		return this.t;
	}
	
	public E getPair()
	{
		return this.e;
	}	
	
}

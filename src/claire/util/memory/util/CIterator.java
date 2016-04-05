package claire.util.memory.util;

import claire.util.standards.IIterator;

public class CIterator<T> 
	   implements IIterator<T>{
	
	T[] elements;
	
	private int counter = 0;
	
	public CIterator(T[] t)
	{
		this.elements = t;
	}
	
	public T next()
	{
		try {
			return elements[counter];
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			return null;
		} finally {
			counter++;
		}
	}
	
	public boolean hasNext()
	{
		return counter >= elements.length;
	}

	public void skip()
	{
		counter++;
	}

	public void skip(int amt)
	{
		counter += amt;
	}

}

package claire.util.memory.util;

/**
 * The Pair class is a class that holds two elements of different types.
 * 
 * @author Jane
 */
@SuppressWarnings("hiding")
public class Pair<Type, Pair> {
	
	private Type t;
	private Pair e;
	
	/**
	 * Creates a pair object using the given objects. This method is safe.
	 */
	public Pair(final Type t, final Pair e)
	{
		this.t = t;
		this.e = e;
	}
	
	/**
	 * Sets the object of the pair. This method is safe. 
	 */
	public void setObject(final Type t)
	{
		this.t = t;
	}
	
	/**
	 * Sets the pair of the pair. This method is safe. 
	 */
	public void setPair(final Pair e)
	{
		this.e = e;
	}
	
	/**
	 * Gets the object of the pair. This method is safe. 
	 */
	public Type getObject()
	{
		return this.t;
	}
	
	/**
	 * Gets the pair of the pair. This method is safe. 
	 */
	public Pair getPair()
	{
		return this.e;
	}	
	
}

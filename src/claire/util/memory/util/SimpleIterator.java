package claire.util.memory.util;

import claire.util.standards.IIterator;

/**
 * This is a basic iterator class that iterates through an array from index
 * zero to its length. 
 * 
 * @author Jane
 */
public class SimpleIterator<Type> 
	   implements IIterator<Type>{
	
	final Type[] elements;
	
	private int counter = 0;
	
	/**
	 * Creates an Iterator. This constructor is safe and cannot fail, however
	 * passing <code>null</code> will make this iterator useless and cannot be
	 * reversed.
	 * <br><br>
	 * The specified array is <b>not</b> copied and thus subsequent modifications 
	 * to the array will affect the output of this iterator.
	 */
	public SimpleIterator(final Type[] t)
	{
		this.elements = t;
	}
	
	/**
	 * Attempts to return the next element in the array. If the iterator position
	 * is at or past the array length a <code>NullPointerException</code> will be thrown.
	 * Use <code>hasNext()</code> to guard against this.
	 */
	public Type next()
	{
		return elements[counter++];
	}
	
	/**
	 * This method returns whether there is another element in the array. 
	 * <br><br>
	 * This method accepts no arguments and is safe.
	 */
	public boolean hasNext()
	{
		return counter < elements.length;
	}

	/**
	 * This method increments the iterator counter and thus skips an element.
	 * <br><br>
	 * This method accepts no arguments and is safe.
	 */
	public void skip()
	{
		counter++;
	}

	/**
	 * This method adds the specified amount to the iterator.
	 * <br><br>
	 * This method is safe
	 * <br><br>
	 * Note: negative values can be passed to rewind the iterator. However, if the 
	 * iterator is rewound past zero (ie, to a negative index), than this object
	 * will become unusable until you skip back to zero.
	 */
	public void skip(final int amt)
	{
		counter += amt;
	}
	
	/**
	 * Returns the current internal array position in use by the iterator. The array
	 * position shall reflect <b>the next element to be read</b>, and not the last
	 * element that was read.
	 * <br><br>
	 * This method accepts no argument and is safe. 
	 */
	public int getPos()
	{
		return counter;
	}
	
	/**
	 * Returns the total number of elements in the iterator.
	 * <br><br>
	 * This method accepts no argument and is safe. 
	 */
	public int getLength()
	{
		return elements.length;
	}
	
	/**
	 * Returns the number of remaining elements that can be read with <code>next()</code>
	 * <br><br>
	 * Note that because the array position can be modified with <code>skip(int)</code> this 
	 * method can return negative values (if the position has been skipped past the array length,
	 * or values greater than the length of the internal array, if the current position is
	 * negative.
	 * <br><br>
	 * This method accepts no argument and is safe. 
	 */
	public int getRemaining()
	{
		return elements.length - counter;
	}
	
	/**
	 * This method returns the array that was used to create the iterator during construction.
	 * The output of this method can never change.
	 * <br><br>
	 * This method accepts no argument and is safe.  
	 */
	public Type[] getArray()
	{
		return elements;
	}

}

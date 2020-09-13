package claire.util.memory.util;

import claire.util.standards.IIterator;

/**
 * This is a basic iterator class that iterates through an array from index
 * zero to its length. It also allows for a specified end length.
 * 
 * @author Jane
 */
public class IJIterator<Type> 
	   implements IIterator<Type>{
	
	
	Type[] elements;
	
	int counter = 0,
		end;
	
	/**
	 * Creates an iterator using the given array. If <code>null</code> is passed a 
	 * <code>NullPointerException</code> will be thrown.
	 * <br><br>
	 * The specified array is <b>not</b> copied and thus subsequent modifications 
	 * to the array will affect the output of this iterator.
	 */
	public IJIterator(final Type[] t)
	{
		this.elements = t;
		end = t.length;
	}
	
	/**
	 * Creates an Iterator. This constructor is safe and cannot fail.
	 * 
	 * Accepts:
	 * <ul>
	 * <li>The Array to be used in the iterator</li>
	 * <li>The index of the first element to be iterated</li>
	 * <li>The number of elements that are desired</li>
	 * </ul>
	 * <br><br>
	 * Errors:
	 * <ul>
	 * <li>Passing <code>null</code> will make this iterator useless and cannot be
	 * reversed.</li>
	 * <li>A negative start will render this object unusable unless manually skipped</li>
	 * <li>A negative len will render this object unusable</li>
	 * <li>A if len + start is greater than the array length, than undefined behavior will occur</li>
	 * </ul>
	 * <br><br>
	 * The specified array is <b>not</b> copied and thus subsequent modifications 
	 * to the array will affect the output of this iterator.
	 */
	public IJIterator(final Type[] t, int start, int len)
	{
		this.elements = t;
		end = (counter = start) + len;
	}
	
	/**
	 * Attempts to return the next element in the array. If the iterator position
	 * is at or past the array length a <code>NullPointerException</code> will be thrown.
	 * Use <code>hasNext()</code> to guard against this.
	 * <br><br>
	 * If the iterator has already traversed the maximum number of elements this method will
	 * silently fail, and return the next element in accordance with the internal array structure
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
		return counter < end;
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
		return end - counter;
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

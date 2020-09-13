package claire.util.memory.array;

import java.lang.reflect.Array;

import claire.util.memory.util.ArrayUtil;
import claire.util.memory.util.SimpleIterator;
import claire.util.standards.IArray;
import claire.util.standards.IIterable;
import claire.util.standards.IIterator;
import claire.util.standards.IReferrable;

/**
 * The IJArray class is a custom array object which can be used in place of a normal array. 
 * It is not recommended to do that as this class will be slower. Only do so if the methods
 * provided by this class are desired or if you are using a superclass such as <code>DynArray</code>
 * 
 * @author Jane
 */
@SuppressWarnings({ "unchecked" })
public class IJArray <Type> 
	   implements IIterable<Type>, 
	   			  IReferrable<IJArray<Type>>, 
	   			  IArray<Type> {
	
	protected Type[] array;
	protected final Class<Type> class_;
	
	/**
	 * Constructs a new array based on existing data.
	 * <br><br>
	 * If <code>null</code> is given for the array, than a <code>NullPointerException</code>
	 * will be thrown.
	 */
	public IJArray(final Type ... initVal) 
	{
		this.class_ = (Class<Type>) initVal.getClass().getComponentType();
		this.array = initVal;
	}
	
	/**
	 * Constructs a new array of <code>Type</code>, with length of <code>initSize</code>.
	 * <br><br>
	 * If class_ is <code>null</code> or <code>Void.TYPE</code> than this method will fail.
	 * <br><br>
	 * If <code>initSize</code> is negative a <code>NegativeArraySizeException</code> will be
	 * thrown.
	 */
	public IJArray(final Class<Type> class_, final int initSize) 
	{
		this.class_ = class_;
		this.array = (Type[]) Array.newInstance(class_, initSize);
	}
	
	/**
	 * Adds the specified array to the internal array. In effect, this is doing:
	 * <code>setArray(concat(getArray(), array))</code>
	 * <br><br>
	 * Method will fail if array is <code>null</code> 
	 */
	public void add(final Type[] array)
	{
		this.array = ArrayUtil.concat(this.array, array);
	}
	
	/**
	 * Sets the internal array structure. Does <b>not</b> copy over any elements to the new
	 * structure. 
	 * <br><br>
	 * This method is safe. 
	 */
	public void set(final Type[] array)
	{
		this.array = array;
	}
	
	/**
	 * Sets a specific index to a specific value. The
	 * specified array index will be set to the specified value, and can be retrieved
	 * again using <code>get()</code> with the same index.
	 * <br><br>
	 *  If the index is out of bounds an <code>ArrayIndexOutOfBounds</code> exception will be thrown. 
	 */
	public void set(final int i, final Type t)
	{
		this.array[i] = t;
	}
	
	/**
	 * This method overflows the array such that the internal array grows by <code>i</code> spaces. 
	 * <br><br>
	 * If <code>i</code> is negative, undefined behavior will result.
	 * <br><br>
	 * If you do not have enough memory to allocate the new array wholesale, than undefined behavior will result
	 * <br><br>
	 * This method will fail if the total resulting size is greater than 2<sup>31</sup> - 1
	 * <br><br>
	 * This method alters the internal array structure and thus the output of <code>getArray()</code>
	 */
	public void overflow(int i)
	{
		this.array = ArrayUtil.upsize(this.array, i);
	}
	
	/**
	 * This method grabs an element from the array at index <code>i</code>. The returned element
	 * can be anything, and is not guaranteed to not be <code>null</code> or of class <code>Type</code>.
	 * <br><br>
	 * Indexes that are greater than or equal to <code>size()</code> will result in an
	 * <code>ArrayIndexOutOfBoundsException</code>. The index cannot be negative.
	 */
	public Type get(int i)
	{
		return this.array[i];
	}
	
	/**
	 * Will return the internal array structure being used in this object. Will always return an
	 * array of <code>Type</code> unless the internal array structure has been manually set to 
	 * <code>null</code>
	 * <br><br>
	 * The returned array is not a duplicate and can be used to manually edit this object. The
	 * returned array may become invalid if <code>overflow()</code> is called or the array is 
	 * manually reset. Superclasses may also manipulate the array in use to achieve effects like
	 * <code>DynArray</code>
	 * <br><br>
	 * This method accepts no arguments and is safe.
	 */
	public final Type[] getArray() 
	{
		return this.array;
	}
	
	
	/**
	 * Returns a <code>Class</code> object representing what objects the array are comprised of.
	 * <br><br>
	 * The returned class cannot change from the creation of this object
	 * <br><br>
	 * This method accepts no arguments and is safe.
	 */
	public final Class<Type> getType()
	{
		return class_;
	}
	
	/**
	 * Returns the size of this array. Note that the size does <i>not</i> represent the number of elements in the set.
	 * <br><br>
	 * Accepts no arguments
	 * <br><br>
	 * This method is safe unless the internal array has been manually set to <code>null</code>, in which case a 
	 * <code>NullPointerException</code> will be thrown.
	 */
	public final int size()
	{
		return array.length;
	}
	
	/**
	 * Swaps two elements in a the list.
	 * <br><br>
	 * Accepts:
	 * <ul>
	 *  <li>An integer, greater than 0, less than the array <code>size</code></li> 
	 *  <li>An integer, greater than 0, less than the array <code>size</code></li> 
	 * </ul>
	 * <br>
	 * Integers outside the stated bounds will almost certainly result in a <code>ArrayIndexOutOfBoundsException</code>
	 */
	public void swap(final int i, final int i2)
	{
		final Type temp = this.get(i);
		this.set(i, this.get(i2));
		this.set(i2, temp);
	}

	/**
	 * Returns a linked iterator for this array. If the array is altered while the iterator is still in use, than it
	 * is not guaranteed that the elements in the iterator will not also be altered. In fact, it is almost certain that
	 * they will be.
	 * <br><br>
	 * Accepts no arguments
	 * <br><br>
	 * This method is generally safe, although undefined behavior may occur if the internal array has been manually set 
	 * to <code>null</code>
	 */
	public SimpleIterator<Type> riterator()
	{
		return new SimpleIterator<Type>(this.array);
	}
	
	/**
	 * Returns a copy iterator that will reflect the state of the array at the time you call this function. That means subsequent
	 * modifications to this array will not affect the output of this iterator, however a new array will have to be created to
	 * allow for this.
	 * <br><br>
	 * Accepts no arguments
	 * <br><br>
	 * This method is generally safe, although undefined behavior may occur if the internal array has been manually set 
	 * to <code>null</code>
	 */
	public SimpleIterator<Type> rcopyIterator()
	{
		return new SimpleIterator<Type>(ArrayUtil.copy(this.array));
	}
	
	/**
	 * Returns a linked iterator for this array. If the array is altered while the iterator is still in use, than it
	 * is not guaranteed that the elements in the iterator will not also be altered. In fact, it is almost certain that
	 * they will be.
	 * <br><br>
	 * Accepts no arguments
	 * <br><br>
	 * This method is generally safe, although undefined behavior may occur if the internal array has been manually set 
	 * to <code>null</code>
	 */
	public IIterator<Type> iterator()
	{
		return this.riterator();
	}
	
	/**
	 * Returns a copy iterator that will reflect the state of the array at the time you call this function. That means subsequent
	 * modifications to this array will not affect the output of this iterator, however a new array will have to be created to
	 * allow for this.
	 * <br><br>
	 * Accepts no arguments
	 * <br><br>
	 * This method is generally safe, although undefined behavior may occur if the internal array has been manually set 
	 * to <code>null</code>
	 */
	public IIterator<Type> copyIterator()
	{
		return this.rcopyIterator();
	}
	
	/**
	 * Returns a sub array containing the elements from <code>start</code> to <code>start + len</code>. 
	 * <br><br>
	 * If any of the specified elements are out of bounds pertaining to the internal array structure than an <code>ArrayIndexOutOfBounds</code> 
	 * exception will be thrown. <code>start</code> cannot be negative, neither can <code>len</code>. A negative length will result in undefined
	 * behavior.
	 * <br><br>
	 * The returned array is not part of the internal array structure and thus will not update when this array is modified.
	 */
	public Type[] slice(int start, int len)
	{
		return ArrayUtil.subArr(array, start, start + len);
	}

}

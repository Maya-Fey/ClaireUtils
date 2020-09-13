package claire.util.memory.array;

import java.lang.reflect.Array;

import claire.util.memory.util.ArrayUtil;
import claire.util.memory.util.IJIterator;

/**
 * This dynamic array class is a descendant of IJArray. 
 * <br>
 * <br>
 * DynArray will automatically resize as items are added. Can be used as an array
 * builder with getFinal()
 * <br><br>
 * @author Jane
 */
public class DynArray<Type> 
	   extends IJArray<Type> {

	protected int cur = 0;
	protected int overflowRate = 4;
	
	/**
	 * Constructs a dynamic array from a class and an initial size
	 * <br><br>
	 * Accepts:
	 * <ul>
	 * 	<li>A class object</li>
	 * 	<li>A initial size</li>
	 * </ul>
	 * If a negative initSize is given then a NegativeArraySizeException will be thrown
	 * <br>
	 * If a <code>class_</code> is <code>null</code> a NullPointerException will be thrown
	 */
	public DynArray(Class<Type> class_, int initSize) 
	{
		super(class_, initSize);
	}
	
	/**
	 * Constructs a dynamic array from an existing array
	 * <br><br>
	 * Accepts:
	 * <ul>
	 * 	<li>An array of the correct type</li>
	 * </ul>
	 * If a <code>arr</code> is <code>null</code> a NullPointerException will be thrown
	 */
	public DynArray(Type[] arr) 
	{
		super(arr);
	}

	/**
	 * Adds an element to the array
	 * <br>
	 * <br>
	 * Accepts:
	 * <ul>
	 * 	<li>An object of the correct type</li>
	 * </ul>
	 * This function is safe
	 */
	public void add(Type t)
	{
		if(cur == this.array.length)
			this.overflow(overflowRate);
		this.array[cur++] = t;
	}
	
	/**
	 * Adds an array of elements. The entire added array will be considered filled.
	 * <br><br>
	 * If the array is null, a <code>NullPointerException</code> will be thrown.
	 * <br><br>
	 * The array size will be padded to make enough space. If the internal array structure is already big
	 * enough, than the internal array size will not be affected.
	 */
	public void add(Type[] t)
	{
		ensureSize(cur + t.length);
		System.arraycopy(t, 0, array, cur, t.length);
		cur += t.length;
	}
	
	/**
	 * Returns the last element added and removes it from the array.
	 * <br><br>
	 * If no elements currently exist in the array than this method will fail. 
	 */
	public Type pop()
	{
		return array[--cur];
	}
	
	/**
	 * Sets the growth rate of the DynArray. Higher rates result in less allocations but more unfilled
	 * space in the array. An overflow rate of 1 will mean the array will never grow additional
	 * unallocated spaces
	 * <br>
	 * <br>
	 * Accepts:
	 * <ul>
	 * 	<li>An integer specifying the new overflow rate</li>
	 * </ul>
	 * If an overflow rate less than one is given, undefined behavior will occur during calls to add()
	 */
	public void setOverflowRate(int i)
	{
		this.overflowRate = i;
	}
	
	/**
	 * Returns the number of filled cells in the array 
	 */
	public int length()
	{
		return this.cur;
	}
	
	/**
	 * Makes the array as long as the given size.
	 * <br>
	 * <br>
	 * Accepts:
	 * <ul>
	 * 	<li>An integer specifying the new size</li>
	 * </ul>
	 * Passing a size below the current size will result in undefined behavior
	 */
	public void ensureSize(int size)
	{
		this.overflow(size - this.array.length);
	}
	
	/**
	 * Sets the internal array to a new one
	 * <br>
	 * <br>
	 * By default, all the elements in the passed array will be considered filled.
	 * <br>
	 * <br>
	 * Accepts:
	 * <ul>
	 * 	<li>An array of the correct type</li>
	 * </ul>
	 * If the array is <code>null</code>, than a NullPointerException will be thrown
	 */
	public void set(Type[] array)
	{
		this.cur = array.length; //Process this instruction first so NullPointers get caught before the array is set
		this.array = array;
	}
	
	/**
	 * Sets the internal array to a new one with a specified length
	 * <br>
	 * <br>
	 * Accepts:
	 * <ul>
	 * 	<li>An array of the correct type</li>
	 * 	<li>An integer specifying the correct start-index for the dynamic array<li>
	 * </ul>
	 * If the array is <code>null</code>, undefined behavior will result with any use of the array
	 * <br>
	 * If <code>length</code> is negative, undefined behavior will result with any use of the array
	 */
	public void set(Type[] array, int length)
	{
		this.cur = length;
		this.array = array;
	}
	
	/**
	 * A copy with no unallocated cells is returned. 
	 */
	public Type[] getFinal()
	{
		@SuppressWarnings("unchecked")
		Type[] narr =  (Type[]) Array.newInstance(class_, cur);
		System.arraycopy(this.array, 0, narr, 0, cur);
		return narr;
	}
	
	/**
	 * A copy with no unallocated cells is returned. If no unallocated
	 * cells exist and <code>att</code> is <b>true</b> than the internal 
	 * array will be returned as opposed to a copy. This behavior may
	 * be desired if this object is being used as a one-time array builder
	 * (To prevent unnecessary memory allocations)
	 * <br>
	 * <br>
	 * Accepts:
	 * <ul>
	 * 	<li>A boolean that allows or denies the return-internal-array feature</li>
	 * </ul>
	 * This function is safe
	 */
	public Type[] getFinal(boolean att)
	{
		if(att && cur == this.array.length)
			return this.array;
		@SuppressWarnings("unchecked")
		Type[] narr =  (Type[]) Array.newInstance(class_, cur);
		System.arraycopy(this.array, 0, narr, 0, cur);
		return narr;
	}
	
	public IJIterator<Type> iterator()
	{
		return new IJIterator<Type>(array, 0, cur);
	}

	public IJIterator<Type> copyIterator()
	{
		return new IJIterator<Type>(ArrayUtil.subArr(array, 0, cur), 0, cur);
	}
}

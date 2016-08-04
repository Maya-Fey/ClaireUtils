package claire.util.memory.array;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * This dynamic array class is a descendant of IJArray. 
 * <br>
 * <br>
 * DynArray will automatically resize as items are added. Can be used as an array
 * builder with getFinal()
 */
public class DynArray<Type> 
	   extends CArray<Type> {

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
	public DynArray(Class<Type> class_, int initSize) {
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
	public DynArray(Type[] arr) {
		super(arr);
	}

	public void add(Type t)
	{
		if(cur == this.array.length)
			this.overflow(overflowRate);
		this.array[cur++] = t;
	}
	
	public void setOverflowRate(int i)
	{
		this.overflowRate = i;
	}
	
	public int fill()
	{
		return this.cur;
	}
	
	public void ensureSize(int size)
	{
		this.overflow(size - this.array.length);
	}
	
	public void set(Type[] array)
	{
		final int len1 = this.array.length;
		final int len2 = array.length;
		if(len1 >= array.length) {
			System.arraycopy(array, 0, this.array, 0, len2);
			Arrays.fill(this.array, len2, len1, null);
		} else 
			this.array = array;
		cur = len2;
	}
	
	public Type[] getFinal()
	{
		@SuppressWarnings("unchecked")
		Type[] narr =  (Type[]) Array.newInstance(class_, cur);
		System.arraycopy(this.array, 0, narr, 0, cur);
		return narr;
	}
	
	public Type[] getFinal(boolean att)
	{
		if(att && cur == this.array.length)
			return this.array;
		@SuppressWarnings("unchecked")
		Type[] narr =  (Type[]) Array.newInstance(class_, cur);
		System.arraycopy(this.array, 0, narr, 0, cur);
		return narr;
	}

}

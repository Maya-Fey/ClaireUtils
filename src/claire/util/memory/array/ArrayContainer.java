package claire.util.memory.array;

import java.util.Arrays;

import claire.util.standards.IArray;
import claire.util.standards.IPointer;
import claire.util.standards.IReferrable;

public class ArrayContainer<Type> 
	   implements IPointer<Type[]>,
	   			  IArray<Type>,
	   			  IReferrable<ArrayContainer<Type>> {
		
	private Type[] arr;
	
	public ArrayContainer() {}
	
	public ArrayContainer(Type[] arr)
	{
		this.arr = arr;
	}

	public Type[] get()
	{
		return this.arr;
	}

	public void set(int pos, Type obj)
	{
		arr[pos] = obj;
	}

	public Type get(int pos)
	{
		return arr[pos];
	}
	
	@SuppressWarnings("unchecked")
	public void set(Type[] t)
	{
		if(arr.length >= t.length) {
			System.arraycopy(t, 0, arr, 0, t.length);
			Arrays.fill(arr, t.length, arr.length, 0);
		} else {
			this.arr = (Type[]) new Object[t.length];
			System.arraycopy(t, 0, arr, 0, t.length);
		}
	}

}

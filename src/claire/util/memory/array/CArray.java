package claire.util.memory.array;

import java.lang.reflect.Array;

import claire.util.memory.util.ArrayUtil;
import claire.util.memory.util.CIterator;
import claire.util.standards.IArray;
import claire.util.standards.IIterable;
import claire.util.standards.IIterator;
import claire.util.standards.IReferrable;

@SuppressWarnings({ "unchecked" })
public class CArray <Type> 
	   implements IIterable<Type>, IReferrable<CArray<Type>>, IArray<Type> {
	
	protected Type[] array;
	protected final Class<Type> class_;
	
	public CArray(Type ... initVal) 
	{
		this.class_ = (Class<Type>) initVal.getClass().getComponentType();
		this.array = initVal;
	}
	
	public CArray(Class<Type> class_, int initSize) {
		this.class_ = class_;
		this.array = (Type[]) Array.newInstance(class_, initSize);
	}
	
	public final void add(Type[] array)
	{
		this.array = ArrayUtil.concat(this.array, array);
	}
	
	public void set(Type[] array)
	{
		this.array = array;
	}
	
	public final void set(int i, Type t)
	{
		this.array[i] = t;
	}
	
	public final void overflow(int i)
	{
		this.array = ArrayUtil.upsize(this.array, i);
	}
	
	public final Type get(int i)
	{
		return this.array[i];
	}
	
	public Type[] getArray() 
	{
		return this.array;
	}
	
	public final Class<Type> getType()
	{
		return class_;
	}
	
	public final int size()
	{
		return array.length;
	}
	
	public final void swap(int i, int i2)
	{
		Type temp = this.get(i);
		this.set(i, this.get(i2));
		this.set(i2, temp);
	}

	public IIterator<Type> iterator()
	{
		return new CIterator<Type>(this.array);
	}

}

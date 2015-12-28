package claire.util.memory.util;

import claire.util.standards.IDeepClonable;
import claire.util.standards.IPointer;
import claire.util.standards.IReferrable;

public class Pointer<T> 
	   implements IDeepClonable<Pointer<T>>, 
	   			  IReferrable<Pointer<T>>,
	   			  IPointer<T> {
	
	protected T obj;
	
	public Pointer() 
	{
		this.obj = null;
	}
	
	public Pointer(T obj) 
	{
		this.obj = obj;
	}
	
	public T get() 
	{
		return this.obj;
	}
	
	public void set(T obj) 
	{
		this.obj = obj;
	}
	
	public boolean equals(Object obj)
	{
		return this.obj.equals(obj);
	}

	public Pointer<T> createDeepClone()
	{
		return new Pointer<T>(obj);
	}

}

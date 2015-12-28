package claire.util.memory.array;

import claire.util.memory.util.ArrayUtil;

public class DynamicRegistry<Type>
	   extends Registry<Type> {
	
	private int overflowRate;

	public DynamicRegistry(Class<Type> class_, int initSize) 
	{
		super(class_, initSize);
		this.overflowRate = initSize;
	}
	
	public DynamicRegistry(Type[] arrayFrom) 
	{
		super(arrayFrom);
		this.overflowRate = arrayFrom.length / 2 + 1;
	}

	public void setOverflowRate(int rate)
	{
		this.overflowRate = rate;
	}
	
	public void add(Type t)
	{
		try {
			super.add(t);
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			this.overflow();
			super.add(t);
		}
	}
	
	private void overflow()
	{
		this.array = ArrayUtil.upsize(array, overflowRate);
	}

}

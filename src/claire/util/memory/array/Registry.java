package claire.util.memory.array;

public class Registry<Type> 
	   extends IJArray<Type> {
	
	protected int current = 0;

	public Registry(Class<Type> class_, int initSize) {
		super(class_, initSize);
	}
	
	public Registry(Type[] arrayFrom) {
		super(arrayFrom);
	}

	public void add(Type t)
	{
		this.set(current, t);
		current++;
	}
	
	public int length()
	{
		return this.current;
	}

}

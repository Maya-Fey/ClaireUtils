package claire.util.memory.array;


public class IndexedRegistry<Type> 
	   extends Registry<Type> {
	
	protected String[] index;

	public IndexedRegistry(Class<Type> class_, int initSize) {
		super(class_, initSize);
		this.index = new String[initSize];
	}
	
	public IndexedRegistry(Type[] val, String[] index)
	{
		super(val);
		this.index = index;
	}
	
	public void add(Type t, String s)
	{
		this.index[this.current] = s;
		super.add(t);
	}
	
	public void add(Type t)
	{
		this.index[this.current] = "NULL";
		super.add(t);
	}
	
	public String getName(Type t)
	{
		for(int i = 0; i < this.current; i++)
			if(this.get(i).equals(t)) return this.index[i];
		return null;
	}
	
	public Type getByName(String s)
	{
		for(int i = 0; i < this.current; i++)
			if(this.index[i].equals(s)) 
				return this.get(i);
		return null;
	}

	public String getName(int i)
	{
		return index[i];
	}
	
	public String[] index()
	{
		return this.index;
	}

}

package claire.util.memory.stack;

public class Stack<Type> {
	
	private final Type[] stack;
	
	private int pointer = -1;
	
	@SuppressWarnings("unchecked")
	public Stack(int size)
	{
		this.stack = (Type[]) new Object[size];
	}
	
	public void push(Type item)
	{
		stack[++pointer] = item;
	}
	
	public void push(Type[] arr, int pos, int len)
	{
		while(len-- > 0)
			stack[++pointer] = arr[pos++];
	}
	
	public void push(Type[] arr, int len)
	{
		int i = 0;
		while(i < len)
			stack[++pointer] = arr[i++];
	}
	
	public void push(Type[] arr)
	{
		int i = 0;
		while(i < arr.length)
			stack[++pointer] = arr[i++];
	}
	
	public void duplicate()
	{
		stack[++pointer] = stack[pointer - 1];
	}
	
	public Type pop()
	{
		return stack[pointer--];
	}
	
	public void pop(Type[] arr, int pos, int len)
	{
		while(len-- > 0)
			arr[pos++] = stack[pointer--];
	}
	
	public void pop(Type[] arr, int len)
	{
		int i = 0;
		while(i < len)
			arr[i++] = stack[pointer--];
	}
	
	public void pop(Type[] arr)
	{
		int i = 0;
		while(i < arr.length)
			arr[i++] = stack[pointer--];
	}
	
	public Type read()
	{
		return stack[pointer];
	}
	
	public Type read(int rel)
	{
		return stack[pointer - rel];
	}
	
	public void read(Type[] arr, int pos, int rel, int len)
	{
		while(len-- > 0) 
			arr[pos++] = stack[pointer - rel++];
	}
	
	public void read(Type[] arr, int rel, int len)
	{
		int i = 0;
		while(len-- > 0) 
			arr[i++] = stack[pointer - rel++];
	}
	
	public void read(Type[] arr, int len)
	{
		int i = 0;
		while(len-- > 0) 
			arr[i] = stack[pointer - i++];
	}
	
	public void read(Type[] arr)
	{
		int i = 0;
		while(i < arr.length) 
			arr[i] = stack[pointer - i++];
	}
	
	public void write(Type t, int rel)
	{
		stack[pointer - rel] = t;
	}
	
	public void write(Type[] arr, int pos, int rel, int len)
	{
		while(len-- > 0) 
			stack[pointer - rel++] = arr[pos++];
	}
	
	public void write(Type[] arr, int rel, int len)
	{
		int i = 0;
		while(i < len) 
			stack[pointer - rel++] = arr[i++];
	}
	
	public void write(Type[] arr, int len)
	{
		int i = 0;
		while(i < len) 
			stack[pointer - i] = arr[i++];
	}
	
	public void write(Type[] arr)
	{
		int i = 0;
		while(i < arr.length) 
			stack[pointer - i] = arr[i++];
	}
	
	public int getPos()
	{
		return this.pointer;
	}

}

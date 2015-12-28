package claire.util.memory.buffer;

import claire.util.memory.util.ArrayUtil;

public class CharBuffer {

	private char[] arr;
	private int ptr;
	
	public CharBuffer(char[] arr)
	{
		this.arr = arr;
	}
	
	public CharBuffer(int size)
	{
		this.arr = new char[size];
	}
	
	public void add(char c)
	{
		if(ptr == arr.length)
			overflow();
		arr[ptr++] = c;
		
	}
	
	public void add(char[] chars, int start, int len)
	{
		while((ptr + len) > arr.length)
			overflow();
		System.arraycopy(chars, start, arr, ptr, len);
		ptr += len;
	}
	
	public int length()
	{
		return ptr;
	}
	
	public char[] get()
	{
		char[] chars = new char[ptr];
		System.arraycopy(arr, 0, chars, 0, ptr);
		ptr = 0;
		return chars;
	}
	
	public void get(char[] chars, int start)
	{
		System.arraycopy(arr, 0, chars, start, ptr);
		ptr = 0;
	}
	
	private void overflow()
	{
		 arr = ArrayUtil.upsize(arr, arr.length / 4 + 1);
	}
}

package claire.util.memory.stack;

import claire.util.memory.Bits;

public class DataStack {
	
	private final byte[] stack;
	private final byte[] temp = new byte[8];
	
	private int pos = -1;

	public DataStack(int size)
	{
		this.stack = new byte[size];
	}
	
	public void push(byte item)
	{
		stack[++pos] = item;
	}
	
	public void push(short s)
	{
		Bits.shortToBytes(s, temp, 0);
		this.push(temp, 0, 2);
	}
	
	public void push(int i)
	{
		Bits.intToBytes(i, temp, 0);
		this.push(temp, 0, 4);
	}
	
	public void push(long l)
	{
		Bits.longToBytes(l, temp, 0);
		this.push(temp, 0, 8);
	}
	
	public void push(short[] arr, int pos, int len)
	{
		while(len -- > 0) {
			Bits.shortToBytes(arr[pos++], temp, 0);
			this.push(temp, 0, 2);
		}		
	}
	
	public void push(short[] arr, int len)
	{
		int i = 0;
		while(i < len) {
			Bits.shortToBytes(arr[i++], temp, 0);
			this.push(temp, 0, 2);
		}		
	}
	
	public void push(short[] arr)
	{
		int i = 0;
		while(i < arr.length) {
			Bits.shortToBytes(arr[i++], temp, 0);
			this.push(temp, 0, 2);
		}		
	}
	
	public void push(int[] arr, int pos, int len)
	{
		while(len -- > 0) {
			Bits.intToBytes(arr[pos++], temp, 0);
			this.push(temp, 0, 4);
		}		
	}
	
	public void push(int[] arr, int len)
	{
		int i = 0;
		while(i < len) {
			Bits.intToBytes(arr[i++], temp, 0);
			this.push(temp, 0, 4);
		}		
	}
	
	public void push(int[] arr)
	{
		int i = 0;
		while(i < arr.length) {
			Bits.intToBytes(arr[i++], temp, 0);
			this.push(temp, 0, 4);
		}		
	}
	
	public void push(long[] arr, int pos, int len)
	{
		while(len -- > 0) {
			Bits.longToBytes(arr[pos++], temp, 0);
			this.push(temp, 0, 8);
		}		
	}
	
	public void push(long[] arr, int len)
	{
		int i = 0;
		while(i < len) {
			Bits.longToBytes(arr[i++], temp, 0);
			this.push(temp, 0, 8);
		}		
	}
	
	public void push(long[] arr)
	{
		int i = 0;
		while(i < arr.length) {
			Bits.longToBytes(arr[i++], temp, 0);
			this.push(temp, 0, 8);
		}		
	}
	
	public void push(byte[] arr, int pos, int len)
	{
		while(len-- > 0)
			stack[++this.pos] = arr[pos++];
	}
	
	public void push(byte[] arr, int len)
	{
		int i = 0;
		while(i < len)
			stack[++pos] = arr[i++];
	}
	
	public void push(byte[] arr)
	{
		int i = 0;
		while(i < arr.length)
			stack[++pos] = arr[i++];
	}
	
	public void duplicate()
	{
		stack[++pos] = stack[pos - 1];
	}
	
	public byte pop()
	{
		return stack[pos--];
	}
	
	public short popShort()
	{
		temp[1] = stack[pos--];
		temp[0] = stack[pos--];
		return Bits.shortFromBytes(temp, 0);
	}
	
	public int popInt()
	{
		temp[3] = stack[pos--];
		temp[2] = stack[pos--];
		temp[1] = stack[pos--];
		temp[0] = stack[pos--];
		return Bits.intFromBytes(temp, 0);
	}
	
	public long popLong()
	{
		temp[7] = stack[pos--];
		temp[6] = stack[pos--];
		temp[5] = stack[pos--];
		temp[4] = stack[pos--];
		temp[3] = stack[pos--];
		temp[2] = stack[pos--];
		temp[1] = stack[pos--];
		temp[0] = stack[pos--];
		return Bits.longFromBytes(temp, 0);
	}
	
	public void pop(byte[] arr, int pos, int len)
	{
		while(len-- > 0)
			arr[pos++] = stack[this.pos--];
	}
	
	public void pop(byte[] arr, int len)
	{
		int i = 0;
		while(i < len)
			arr[i++] = stack[pos--];
	}
	
	public void pop(byte[] arr)
	{
		int i = 0;
		while(i < arr.length)
			arr[i++] = stack[pos--];
	}
	
	public void pop(short[] arr, int pos, int len)
	{
		while(len-- > 0) {
			temp[1] = stack[this.pos--];
			temp[0] = stack[this.pos--];
			arr[pos++] = Bits.shortFromBytes(temp, 0);
		}
	}
	
	public void pop(short[] arr, int len)
	{
		int i = 0;
		while(i < len) {
			temp[1] = stack[this.pos--];
			temp[0] = stack[this.pos--];
			arr[i++] = Bits.shortFromBytes(temp, 0);
		}
	}
	
	public void pop(short[] arr)
	{
		int i = 0;
		while(i < arr.length) {
			temp[1] = stack[this.pos--];
			temp[0] = stack[this.pos--];
			arr[i++] = Bits.shortFromBytes(temp, 0);
		}
	}
	
	public void pop(int[] arr, int pos, int len)
	{
		while(len-- > 0) {
			temp[3] = stack[this.pos--];
			temp[2] = stack[this.pos--];
			temp[1] = stack[this.pos--];
			temp[0] = stack[this.pos--];
			arr[pos++] = Bits.intFromBytes(temp, 0);
		}
	}
	
	public void pop(int[] arr, int len)
	{
		int i = 0;
		while(i < len) {
			temp[3] = stack[pos--];
			temp[2] = stack[pos--];
			temp[1] = stack[pos--];
			temp[0] = stack[pos--];
			arr[i++] = Bits.intFromBytes(temp, 0);
		}
	}
	
	public void pop(int[] arr)
	{
		int i = 0;
		while(i < arr.length) {
			temp[3] = stack[pos--];
			temp[2] = stack[pos--];
			temp[1] = stack[pos--];
			temp[0] = stack[pos--];
			arr[i++] = Bits.intFromBytes(temp, 0);
		}
	}
	
	public void pop(long[] arr, int pos, int len)
	{
		while(len-- > 0) {
			temp[7] = stack[this.pos--];
			temp[6] = stack[this.pos--];
			temp[5] = stack[this.pos--];
			temp[4] = stack[this.pos--];
			temp[3] = stack[this.pos--];
			temp[2] = stack[this.pos--];
			temp[1] = stack[this.pos--];
			temp[0] = stack[this.pos--];
			arr[pos++] = Bits.longFromBytes(temp, 0);
		}
	}
	
	public void pop(long[] arr, int len)
	{
		int i = 0;
		while(i < len) {
			temp[7] = stack[pos--];
			temp[6] = stack[pos--];
			temp[5] = stack[pos--];
			temp[4] = stack[pos--];
			temp[3] = stack[pos--];
			temp[2] = stack[pos--];
			temp[1] = stack[pos--];
			temp[0] = stack[pos--];
			arr[i++] = Bits.longFromBytes(temp, 0);
		}
	}
	
	public void pop(long[] arr)
	{
		int i = 0;
		while(i < arr.length) {
			temp[7] = stack[pos--];
			temp[6] = stack[pos--];
			temp[5] = stack[pos--];
			temp[4] = stack[pos--];
			temp[3] = stack[pos--];
			temp[2] = stack[pos--];
			temp[1] = stack[pos--];
			temp[0] = stack[pos--];
			arr[i++] = Bits.longFromBytes(temp, 0);
		}
	}
	
	public byte read()
	{
		return stack[pos];
	}
	
	public byte read(int rel)
	{
		return stack[pos - rel];
	}
	
	public void read(byte[] arr, int pos, int rel, int len)
	{
		while(len-- > 0) 
			arr[pos++] = stack[this.pos - rel++];
	}
	
	public void read(byte[] arr, int rel, int len)
	{
		int i = 0;
		while(len-- > 0) 
			arr[i++] = stack[pos - rel++];
	}
	
	public void read(byte[] arr, int len)
	{
		int i = 0;
		while(len-- > 0) 
			arr[i] = stack[pos - i++];
	}
	
	public void read(byte[] arr)
	{
		int i = 0;
		while(i < arr.length) 
			arr[i] = stack[pos - i++];
	}
	
	public void write(byte t, int rel)
	{
		stack[pos - rel] = t;
	}
	
	public void write(byte[] arr, int pos, int rel, int len)
	{
		while(len-- > 0) 
			stack[this.pos - rel++] = arr[pos++];
	}
	
	public void write(byte[] arr, int rel, int len)
	{
		int i = 0;
		while(i < len) 
			stack[pos - rel++] = arr[i++];
	}
	
	public void write(byte[] arr, int len)
	{
		int i = 0;
		while(i < len) 
			stack[pos - i] = arr[i++];
	}
	
	public void write(byte[] arr)
	{
		int i = 0;
		while(i < arr.length) 
			stack[pos - i] = arr[i++];
	}
	
	public int getPos()
	{
		return this.pos;
	}
	
}

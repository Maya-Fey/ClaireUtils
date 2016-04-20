package claire.util.memory.buffer;

import claire.util.memory.Bits;
import claire.util.standards.io.IOutgoingStream;

public class DynamicDeaggregator {
	
	private final byte[] buffer;
	
	int last = 0;
	
	public DynamicDeaggregator()
	{
		this.buffer = new byte[8];
	}
	
	public DynamicDeaggregator(byte[] buffer)
	{
		this.buffer = buffer;
	}
	
	public void processChar(char c)
	{
		Bits.charToBytes(c, buffer, last);
		last += 2;
	}
	
	public void processShort(short c)
	{
		Bits.shortToBytes(c, buffer, last);
		last += 2;
	}
	
	public void processInt(int c)
	{
		Bits.intToBytes(c, buffer, last);
		last += 4;
	}
	
	public void processLong(long c)
	{
		Bits.longToBytes(c, buffer, last);
		last += 8;
	}
	
	public void deplete()
	{
		last = 0;
	}
	
	public void use(int amt)
	{
		last -= amt;
		System.arraycopy(buffer, amt, buffer, 0, last - amt);
	}
	
	public int getBytes()
	{
		return last;
	}
	
	public void writeTo(byte[] bytes)
	{
		this.writeTo(bytes, 0);
	}
	
	public void writeTo(byte[] bytes, int off)
	{
		System.arraycopy(buffer, 0, bytes, off, last);
	}
	
	public void writeTo(IOutgoingStream stream) throws Exception
	{
		stream.writeBytes(buffer, 0, last);
		last = 0;
	}
	
	public byte[] getBuffer()
	{
		return this.buffer;
	}

}

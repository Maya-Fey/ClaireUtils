package claire.util.memory.buffer;

import claire.util.memory.Bits;
import claire.util.standards.io.IOutgoingStream;

public class PrimitiveDeaggregator {
	
	private final byte[] buffer;
	
	int last = 0;
	
	public PrimitiveDeaggregator()
	{
		this.buffer = new byte[8];
	}
	
	public PrimitiveDeaggregator(byte[] buffer)
	{
		this.buffer = buffer;
	}
	
	public void processChar(char c)
	{
		Bits.charToBytes(c, buffer, 0);
		last = 2;
	}
	
	public void processShort(short c)
	{
		Bits.shortToBytes(c, buffer, 0);
		last = 2;
	}
	
	public void processInt(int c)
	{
		Bits.intToBytes(c, buffer, 0);
		last = 4;
	}
	
	public void processLong(long c)
	{
		Bits.longToBytes(c, buffer, 0);
		last = 8;
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

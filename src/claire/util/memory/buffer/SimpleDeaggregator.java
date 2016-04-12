package claire.util.memory.buffer;

import claire.util.memory.Bits;

public class SimpleDeaggregator {
	
	private final byte[] buffer;
	
	public SimpleDeaggregator()
	{
		this.buffer = new byte[8];
	}
	
	public SimpleDeaggregator(byte[] buffer)
	{
		this.buffer = buffer;
	}
	
	public void processChar(char c)
	{
		Bits.charToBytes(c, buffer, 0);
	}
	
	public void processShort(short c)
	{
		Bits.shortToBytes(c, buffer, 0);
	}
	
	public void processInt(int c)
	{
		Bits.intToBytes(c, buffer, 0);
	}
	
	public void processLong(long c)
	{
		Bits.longToBytes(c, buffer, 0);
	}
	
	public byte[] getBuffer()
	{
		return this.buffer;
	}

}

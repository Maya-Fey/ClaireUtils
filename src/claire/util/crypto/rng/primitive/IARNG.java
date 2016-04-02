package claire.util.crypto.rng.primitive;

import claire.util.crypto.cipher.primitive.stream.IA;
import claire.util.memory.buffer.PrimitiveAggregator;
import claire.util.standards.IRandom;

public class IARNG implements IRandom {

	private final IA cipher;
	private final PrimitiveAggregator agg = new PrimitiveAggregator();
	private final byte[] buffer = agg.getBuffer();
	
	public IARNG(IA cip)
	{
		this.cipher = cip;
	}
	
	public boolean nextBoolean()
	{
		return (this.nextByte() & 1) == 1;
	}

	public byte nextByte()
	{
		return this.nextByte();
	}
	
	public short nextShort()
	{
		this.reqBytes(2);
		return agg.getShort();
	}

	public int nextInt()
	{
		return cipher.nextInt();
	}

	public long nextLong()
	{
		return ((cipher.nextInt() & 0xFFFFFFFFL) << 32) | cipher.nextInt();
	}
	
	private void reqBytes(int bytes)
	{
		cipher.fill(buffer, 0, bytes);
	}

}

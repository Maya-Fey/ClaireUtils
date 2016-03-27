package claire.util.crypto.rng.primitive;

import claire.util.memory.buffer.PrimitiveAggregator;
import claire.util.standards.IRandom;
import claire.util.standards.crypto.IStreamCipher;

public class StreamCipherRNG implements IRandom {

	private final IStreamCipher<?, ?> cipher;
	private final PrimitiveAggregator agg = new PrimitiveAggregator();
	private final byte[] buffer = agg.getBuffer();
	
	public StreamCipherRNG(IStreamCipher<?, ?> cip)
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
		this.reqBytes(4);
		return agg.getInt();
	}

	public long nextLong()
	{
		this.reqBytes(8);
		return agg.getLong();
	}
	
	private void reqBytes(int bytes)
	{
		cipher.fill(buffer, 0, bytes);
	}

}

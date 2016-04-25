package claire.util.crypto.rng.primitive;

import claire.util.memory.Bits;
import claire.util.standards.crypto.IRandom;

public abstract class XorShiftRNG
	   			implements IRandom<LongSeed> {

	private LongSeed seed;
	
	protected long last;
	
	public XorShiftRNG() 
	{
    	this(new LongSeed((System.currentTimeMillis() ^ (System.nanoTime() << 12))));
	}
	
	public XorShiftRNG(boolean b)
	{
		if(b)
			this.setSeed(new LongSeed((System.currentTimeMillis() ^ (System.nanoTime() << 12))));
		else
			this.last = (System.currentTimeMillis() ^ (System.nanoTime() << 12));
	}
	
	public XorShiftRNG(LongSeed seed)
	{
		this.seed = seed;
		this.last = seed.getSeed();
	}

	public XorShiftRNG(long l)
	{
		this.last = l;
	}
	
	protected abstract void update();
	
	public void setSeed(long seed)
	{
		this.last = seed;
	}

	public boolean readBool()
	{
		update();
		return last < 0;
	}

	public byte readByte()
	{
		update();
		return (byte) last;
	}

	public short readShort()
	{
		update();
		return (short) last;
	}

	public char readChar()
	{
		update();
		return (char) last;
	}

	public int readInt()
	{
		update();
		return (int) last;
	}

	public long readLong()
	{
		update();
		return last;
	}

	public void readBools(boolean[] out, int off, int amt)
	{
		long i;
		int j;
		while(amt > 63)
		{
			update();
			i = last;
			j = 0;
			while(j < 64) 
				out[off++] = (Bits.BIT64_TABLE[j++] & i) != 0; 
		}
		if(amt > 0)
		{
			update();
			i = last;
			j = 0;
			while(amt-- > 0) 
				out[off++] = (Bits.BIT64_TABLE[j++] & i) != 0; 
		}
	}

	public void readNibbles(byte[] out, int off, int amt)
	{
		while(amt > 15)
		{
			update();
			long l = last;
			out[off++] = (byte) (l & 0xF);
			l >>>= 4;
			out[off++] = (byte) (l & 0xF);
			l >>>= 4;
    		out[off++] = (byte) (l & 0xF);
    		l >>>= 4;
    		out[off++] = (byte) (l & 0xF);
			l >>>= 4;
			out[off++] = (byte) (l & 0xF);
			l >>>= 4;
			out[off++] = (byte) (l & 0xF);
			l >>>= 4;
			out[off++] = (byte) (l & 0xF);
			l >>>= 4;
			out[off++] = (byte) (l & 0xF);
			l >>>= 4;
			out[off++] = (byte) (l & 0xF);
			l >>>= 4;
			out[off++] = (byte) (l & 0xF);
			l >>>= 4;
    		out[off++] = (byte) (l & 0xF);
    		l >>>= 4;
    		out[off++] = (byte) (l & 0xF);
			l >>>= 4;
			out[off++] = (byte) (l & 0xF);
			l >>>= 4;
			out[off++] = (byte) (l & 0xF);
			l >>>= 4;
			out[off++] = (byte) (l & 0xF);
			l >>>= 4;
			out[off++] = (byte) (l & 0xF);
			amt -= 16;
		}
		if(amt > 0) {
			update();
			long l = last;
			out[off++] = (byte) (l & 0xF);
			amt--;
			while(amt-- > 0) {
				l >>>= 4;
				out[off++] = (byte) (l & 0xF);
			}
		}
	}

	public void readBytes(byte[] out, int off, int amt)
	{
		while(amt > 7)
		{
			update();
			long l = last;
			out[off++] = (byte) l;
			l >>>= 8;
			out[off++] = (byte) l;
			l >>>= 8;
    		out[off++] = (byte) l;
			l >>>= 8;
			out[off++] = (byte) l;
			l >>>= 8;
			out[off++] = (byte) l;
			l >>>= 8;
    		out[off++] = (byte) l;
			l >>>= 8;
			out[off++] = (byte) l;
			l >>>= 8;
			out[off++] = (byte) l;
			amt -= 8;
		}
		if(amt > 0) {
			update();
			long l = last;
			out[off++] = (byte) l;
			amt--;
			while(amt-- > 0) {
				l >>>= 8;
				out[off++] = (byte) l;
			}
		}
	}

	public void readShorts(short[] shorts, int off, int amt)
	{
		while(amt > 3)
		{
			update();
			long l = last;
			shorts[off++] = (short) l;
			l >>>= 16;
			shorts[off++] = (short) l;
			l >>>= 16;
    		shorts[off++] = (short) l;
			l >>>= 16;
			shorts[off++] = (short) l;
			amt -= 4;
		}
		if(amt > 0) {
			update();
			long l = last;
			shorts[off++] = (short) l;
			amt--;
			while(amt-- > 0) {
				l >>>= 16;
				shorts[off++] = (short) l;
			}
		}
	}

	public void readChars(char[] chars, int off, int amt)
	{
		while(amt > 3)
		{
			update();
			long l = last;
			chars[off++] = (char) l;
			l >>>= 16;
			chars[off++] = (char) l;
			l >>>= 16;
    		chars[off++] = (char) l;
			l >>>= 16;
			chars[off++] = (char) l;
			amt -= 4;
		}
		if(amt > 0) {
			update();
			long l = last;
			chars[off++] = (char) l;
			amt--;
			while(amt-- > 0) {
				l >>>= 16;
				chars[off++] = (char) l;
			}
		}
	}

	public void readInts(int[] ints, int off, int amt)
	{
		while(amt > 1)
		{
			update();
			ints[off++] = (int) last;
			ints[off++] = (int) (last >>> 32);
			amt -= 2;
		}
		if(amt > 0) {
			update();
			ints[off++] = (int) last;
		}
	}

	public void readLongs(long[] longs, int off, int amt)
	{
		while(amt-- > 0) {
			update();
			longs[off++] = last;
		}
	}

	public LongSeed getSeed()
	{
		return seed;
	}

	public void setSeed(LongSeed key)
	{
		this.last = key.getSeed();
	}

	public void reset()
	{
		last = seed.getSeed();
	}

	public void wipe()
	{
		this.last = 0;
	}

}
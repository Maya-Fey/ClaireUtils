package claire.util.crypto.rng.primitive;

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
			this.last = (System.currentTimeMillis() ^ (System.nanoTime() << 12))
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
	
	public int nextInt() {
		update();
    	return (int) this.last;
	}
	
	public long nextLong() {
		update();
    	return this.last;
	}

	public int nextInt(int max) {
		update();
    	int out = (int) this.last % max;     
    	return (out < 0) ? -out : out;
	}
	
	public boolean nextBoolean()
	{
		update();
    	return (this.last & 1) > 0;
	}

	public byte nextByte()
	{
		update();
		return (byte) this.last;
	}

	public short nextShort()
	{
		update();
		return (short) this.last;
	}

	public boolean readBool()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public byte readByte()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public short readShort()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public char readChar()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int readInt()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public long readLong()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public void readBools(boolean[] out, int off, int amt)
	{
		// TODO Auto-generated method stub
		
	}

	public void readNibbles(byte[] out, int off, int amt)
	{
		// TODO Auto-generated method stub
		
	}

	public void readBytes(byte[] out, int off, int bytes)
	{
		// TODO Auto-generated method stub
		
	}

	public void readShorts(short[] shorts, int off, int amt)
	{
		// TODO Auto-generated method stub
		
	}

	public void readChars(char[] chars, int off, int amt)
	{
		// TODO Auto-generated method stub
		
	}

	public void readInts(int[] ints, int off, int amt)
	{
		// TODO Auto-generated method stub
		
	}

	public void readLongs(long[] longs, int off, int amt)
	{
		// TODO Auto-generated method stub
		
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
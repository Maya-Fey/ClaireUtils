package claire.util.crypto.rng.primitive;

import java.util.Random;

import claire.util.standards.crypto.IRandom;

public class JRandom 
	   extends Random 
	   implements IRandom<LongSeed> {
	
	private static final long serialVersionUID = 1L;
	
	private LongSeed seed;

	public JRandom() 
	{
    	super();
	}
	
	public JRandom(boolean b)
	{
		if(b)
			this.setSeed(new LongSeed((System.currentTimeMillis() ^ (System.nanoTime() << 12))));
	}
	
	public JRandom(LongSeed seed)
	{
		super(seed.getSeed());
		this.seed = seed;
	}

	public JRandom(long l)
	{
		super(l);
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
		this.seed = key;
		super.setSeed(key.getSeed());
	}

	public void reset()
	{
		super.setSeed(seed.getSeed());
	}

	public void wipe()
	{
		super.setSeed(0);
	}

}

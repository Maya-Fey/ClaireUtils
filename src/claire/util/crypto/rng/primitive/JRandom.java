package claire.util.crypto.rng.primitive;

import java.util.Random;

import claire.util.standards.crypto.IRandom;
import claire.util.standards.crypto.IState;

public class JRandom 
	   extends Random 
	   implements IRandom<LongSeed, IState<?>> {
	
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
		return this.nextBoolean();
	}

	public byte readByte()
	{
		return (byte) this.nextInt();
	}

	public short readShort()
	{
		return (short) this.nextInt();
	}

	public char readChar()
	{
		return (char) this.nextInt();
	}

	public int readInt()
	{
		return this.nextInt();
	}

	public long readLong()
	{
		return this.nextLong();
	}

	public void readBools(boolean[] out, int off, int amt)
	{
		while(amt-- > 0)
			out[off++] = this.nextBoolean();
	}

	public void readNibbles(byte[] out, int off, int amt)
	{
		int i, j;
		while(amt > 7)
		{
			i = this.nextInt();
			j = 32;
			while(j-- > 0) {
				out[off++] = (byte) (i & 0x0F);
				i >>>= 4;
			}
		}
		if(amt > 0)
		{
			i = this.nextInt();
			while(amt-- > 0) {
				out[off++] = (byte) (i & 0x0F); 
				i >>>= 4;
			}
		}
	}

	public void readBytes(byte[] out, int off, int bytes)
	{
		while(bytes-- > 0)
			out[off++] = (byte) this.nextInt();
	}

	public void readShorts(short[] shorts, int off, int amt)
	{
		while(amt-- > 0)
			shorts[off++] = (short) this.nextInt();
	}

	public void readChars(char[] chars, int off, int amt)
	{
		while(amt-- > 0)
			chars[off++] = (char) this.nextInt();
	}

	public void readInts(int[] ints, int off, int amt)
	{
		while(amt-- > 0)
			ints[off++] = this.nextInt();
	}

	public void readLongs(long[] longs, int off, int amt)
	{
		while(amt-- > 0)
			longs[off++] = this.nextLong();
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

	public IState<?> getState()
	{
		return null;
	}

	public void loadState(IState<?> state) {}
	public void updateState(IState<?> state) {}

}

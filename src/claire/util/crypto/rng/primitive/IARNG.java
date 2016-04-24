package claire.util.crypto.rng.primitive;

import java.util.Arrays;

import claire.util.crypto.cipher.key.stream.KeyIA;
import claire.util.crypto.cipher.primitive.stream.IA;
import claire.util.memory.buffer.PrimitiveAggregator;
import claire.util.standards.crypto.IRandom;

public class IARNG 
	   implements IRandom<KeyIA> {

	private final IA cipher;
	private final PrimitiveAggregator agg = new PrimitiveAggregator();
	private final byte[] buffer = agg.getBuffer();
	
	public IARNG(IA cip)
	{
		this.cipher = cip;
	}
	
	public boolean readBool()
	{
		return cipher.nextInt() < 0;
	}

	public byte readByte()
	{
		return (byte) cipher.nextInt();
	}

	public short readShort()
	{
		return (short) cipher.nextInt();
	}

	public char readChar()
	{
		return (char) cipher.nextInt();
	}

	public int readInt()
	{
		return cipher.nextInt();
	}

	public long readLong()
	{
		return ((cipher.nextInt() & 0xFFFFFFFFL) << 32) | cipher.nextInt();
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
		cipher.fill(out, off, bytes);
	}

	public void readShorts(short[] shorts, int off, int amt)
	{
		while(amt > 1)
		{
			int i = cipher.nextInt();
			shorts[off++] = (short) i;
			shorts[off++] = (short) (i >>> 16);
			amt -= 2;
		}
		if(amt > 0)
			shorts[off++] = (short) cipher.nextInt();
	}

	public void readChars(char[] chars, int off, int amt)
	{
		while(amt > 1)
		{
			int i = cipher.nextInt();
			chars[off++] = (char) i;
			chars[off++] = (char) (i >>> 16);
			amt -= 2;
		}
		if(amt > 0)
			chars[off++] = (char) cipher.nextInt();
	}

	public void readInts(int[] ints, int off, int amt)
	{
		while(amt-- > 0)
			ints[off++] = cipher.nextInt();
	}

	public void readLongs(long[] longs, int off, int amt)
	{
		while(amt-- > 0)
			longs[off++] = ((cipher.nextInt() & 0xFFFFFFFFL) << 32) | cipher.nextInt();
	}

	public KeyIA getSeed()
	{
		return cipher.getKey();
	}

	public void setSeed(KeyIA key)
	{
		cipher.getKey();
	}

	public void reset()
	{
		cipher.reset();
	}

	public void wipe()
	{
		Arrays.fill(buffer, (byte) 0);
		cipher.wipe();
	}

}

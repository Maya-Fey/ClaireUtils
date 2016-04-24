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

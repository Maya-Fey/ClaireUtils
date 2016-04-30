package claire.util.crypto.rng.primitive;

import java.util.Arrays;

import claire.util.memory.buffer.PrimitiveAggregator;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;
import claire.util.standards.crypto.IStreamCipher;

public class StreamCipherRNG<Key extends IKey<Key>> 
	   implements IRandom<Key> {

	private final IStreamCipher<Key, ?> cipher;
	private final PrimitiveAggregator agg = new PrimitiveAggregator();
	private final byte[] buffer = agg.getBuffer();
	
	public StreamCipherRNG(IStreamCipher<Key, ?> cip)
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

	public Key getSeed()
	{
		return cipher.getKey();
	}

	public void setSeed(Key key)
	{
		cipher.setKey(key);
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

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
	
	private void reqBytes(int bytes)
	{
		cipher.fill(buffer, 0, bytes);
	}

	public boolean readBool()
	{
		return (cipher.nextByte() & 1) == 1;
	}

	public byte readByte()
	{
		return this.readByte();
	}
	
	public short readShort()
	{
		this.reqBytes(2);
		return agg.getShort();
	}

	public char readChar()
	{
		this.reqBytes(2);
		return agg.getChar();
	}
	
	public int readInt()
	{
		this.reqBytes(4);
		return agg.getInt();
	}

	public long readLong()
	{
		this.reqBytes(8);
		return agg.getLong();
	}
	
	public void readBools(boolean[] out, int off, int amt)
	{
		while(amt-- > 0)
			out[off++] = cipher.nextByte() < 0;
	}

	public void readNibbles(byte[] out, int off, int amt)
	{
		while(amt > 2)
		{
			byte b = cipher.nextByte();
			out[off++] = (byte) (b & 0x0F);
			out[off++] = (byte) ((b >>> 4) & 0xF);
			amt -= 2;
		}
		if(amt == 1)
			out[off++] = (byte) (cipher.nextByte() & 0x0F);
	}

	public void readBytes(byte[] out, int off, int bytes)
	{
		while(bytes-- > 0)
			out[off++] = cipher.nextByte();
	}

	public void readShorts(short[] shorts, int off, int amt)
	{
		while(amt-- > 0)
			shorts[off++] = this.readShort();
	}

	public void readChars(char[] chars, int off, int amt)
	{
		while(amt-- > 0)
			chars[off++] = this.readChar();
	}

	public void readInts(int[] ints, int off, int amt)
	{
		while(amt-- > 0)
			ints[off++] = this.readInt();
	}

	public void readLongs(long[] longs, int off, int amt)
	{
		while(amt-- > 0)
			longs[off++] = this.readLong();
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

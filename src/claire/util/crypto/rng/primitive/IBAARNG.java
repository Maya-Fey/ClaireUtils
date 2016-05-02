package claire.util.crypto.rng.primitive;

import java.util.Arrays;

import claire.util.crypto.cipher.key.stream.KeyIBAA;
import claire.util.crypto.cipher.primitive.stream.IBAA;
import claire.util.crypto.cipher.primitive.stream.IBAA.StateIBAA;
import claire.util.memory.Bits;
import claire.util.memory.buffer.PrimitiveAggregator;
import claire.util.standards.crypto.IRandom;

public class IBAARNG 
	   implements IRandom<KeyIBAA, StateIBAA> {

	private final IBAA cipher;
	private final PrimitiveAggregator agg = new PrimitiveAggregator();
	private final byte[] buffer = agg.getBuffer();
	
	public IBAARNG(IBAA cip)
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
		int i, j;
		while(amt > 31)
		{
			i = cipher.nextInt();
			j = 0;
			while(j < 32) 
				out[off++] = (Bits.BIT32_TABLE[j++] & i) != 0; 
		}
		if(amt > 0)
		{
			i = cipher.nextInt();
			j = 0;
			while(amt-- > 0) 
				out[off++] = (Bits.BIT32_TABLE[j++] & i) != 0; 
		}
	}

	public void readNibbles(byte[] out, int off, int amt)
	{
		int i, j;
		while(amt > 7)
		{
			i = cipher.nextInt();
			j = 32;
			while(j-- > 0) {
				out[off++] = (byte) (i & 0x0F);
				i >>>= 4;
			}
		}
		if(amt > 0)
		{
			i = cipher.nextInt();
			while(amt-- > 0) {
				out[off++] = (byte) (i & 0x0F); 
				i >>>= 4;
			}
		}
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

	public KeyIBAA getSeed()
	{
		return cipher.getKey();
	}

	public void setSeed(KeyIBAA key)
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

	public StateIBAA getState()
	{
		return cipher.getState();
	}

	public void loadState(StateIBAA state)
	{
		cipher.loadState(state);
	}

	public void updateState(StateIBAA state)
	{
		cipher.updateState(state);
	}

}

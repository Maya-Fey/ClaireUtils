package claire.util.crypto.rng;

import claire.util.standards.crypto.IRandom;
import claire.util.standards.io.IIncomingStream;

public class RandomStreamAdapter implements IIncomingStream {

	private final IRandom<?> rand;
	
	public RandomStreamAdapter(IRandom<?> rand) 
	{
		this.rand = rand;
	}

	public void close() {}

	public boolean readBool()
	{
		return rand.readBool();
	}

	public byte readByte()
	{
		return rand.readByte();
	}

	public short readShort()
	{
		return rand.readShort();
	}

	public char readChar()
	{
		return rand.readChar();
	}

	public int readInt()
	{
		return rand.readInt();
	}

	public long readLong()
	{
		return rand.readLong();
	}

	public void readBools(boolean[] out, int off, int amt)
	{
		rand.readBools(out, off, amt);
	}

	public void readNibbles(byte[] out, int off, int amt)
	{
		rand.readNibbles(out, off, amt);
	}

	public void readBytes(byte[] out, int off, int bytes)
	{
		rand.readBytes(out, off, bytes);
	}

	public void readShorts(short[] shorts, int off, int amt)
	{
		rand.readShorts(shorts, off, amt);
	}

	public void readChars(char[] chars, int off, int amt)
	{
		rand.readChars(chars, off, amt);
	}

	public void readInts(int[] ints, int off, int amt)
	{
		rand.readInts(ints, off, amt);
	}

	public void readLongs(long[] longs, int off, int amt)
	{
		rand.readLongs(longs, off, amt);
	}

	public void rewind(long len) {}
	public void skip(long pos) {}
	public void seek(long pos) {}

	public long available()
	{
		return 0xFFFFFFFF;
	}

}

package claire.util.standards.crypto;

import claire.util.encoding.CString;
import claire.util.standards.IStateMachine;

public interface IRandom<Key extends IKey<?>, State extends IState<State>>
       extends IStateMachine<State> {
	
	/*
	 * Note: The reason I didn't simply extends IIncomingStream is
	 * that IIncomingStream allows the throwing of IOExceptions, which
	 * shouldn't happen in an R(N/B)G. Also implementing Closable is
	 * not required.
	 */
	
	boolean readBool();
	byte readByte();
	short readShort();
	char readChar();
	int readInt();
	long readLong();
	
	void readBools(boolean[] out, int off, int amt);
	void readNibbles(byte[] out, int off, int amt);
	void readBytes(byte[] out, int off, int bytes);
	void readShorts(short[] shorts, int off, int amt);
	void readChars(char[] chars, int off, int amt);
	void readInts(int[] ints, int off, int amt);
	void readLongs(long[] longs, int off, int amt);

	default CString readText(int length) 
	{
		return new CString(this.readChars(length));
	}
	
	default void readBools(boolean[] bools) 
	{
		this.readBools(bools, 0, bools.length);
	}
	
	default void readNibbles(byte[] nibbles) 
	{
		this.readNibbles(nibbles, 0, nibbles.length);
	}
	
	default void readBytes(byte[] bytes) 
	{
		this.readBytes(bytes, 0, bytes.length);
	}
	
	default void readShorts(short[] shorts) 
	{
		this.readShorts(shorts, 0, shorts.length);
	}
	
	default void readChars(char[] chars) 
	{
		this.readChars(chars, 0, chars.length);
	}
	
	default void readInts(int[] ints) 
	{
		this.readInts(ints, 0, ints.length);
	}
	
	default void readLongs(long[] longs) 
	{
		this.readLongs(longs, 0, longs.length);
	}
	
	default boolean[] readBools(int bools) 
	{
		boolean[] data = new boolean[bools];
		this.readBools(data, 0, bools);
		return data;
	}
	
	default byte[] readNibbles(int bytes) 
	{
		byte[] data = new byte[bytes];
		this.readNibbles(data, 0, bytes);
		return data;
	}
	
	default byte[] readBytes(int bytes) 
	{
		byte[] data = new byte[bytes];
		this.readBytes(data, 0, bytes);
		return data;
	}
	
	default short[] readShorts(int shorts) 
	{
		short[] data = new short[shorts];
		this.readShorts(data, 0, shorts);
		return data;
	}
	
	default char[] readChars(int chars) 
	{
		char[] data = new char[chars];
		this.readChars(data, 0, chars);
		return data;
	}
	
	default int[] readInts(int ints) 
	{
		int[] data = new int[ints];
		this.readInts(data, 0, ints);
		return data;
	}
	
	default long[] readLongs(int longs) 
	{
		long[] data = new long[longs];
		this.readLongs(data, 0, longs);
		return data;
	}
	
	Key getSeed();
	
	void setSeed(Key key);
	void reset();
	void wipe();

	default int nextIntFast(int max) 
	{
		return (this.readInt() >>> 1) % max;
	}
	
	default int nextIntGood(int max)
	{
		int bits, val;
	    do {
	    	bits = (this.readInt() >>> 1);
	    	val = bits % max;
        } while(bits - val + (max - 1) < 0);
	    return val;
	}	
	
}

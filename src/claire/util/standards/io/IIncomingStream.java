package claire.util.standards.io;

import java.io.Closeable;
import java.io.IOException;

import claire.util.encoding.CString;
import claire.util.io.Factory;

public interface IIncomingStream 
	   extends Closeable {
	
	byte readByte() throws IOException;
	short readShort() throws IOException;
	char readChar() throws IOException;
	int readInt() throws IOException;
	long readLong() throws IOException;
	
	void readBytes(byte[] out, int off, int bytes) throws IOException;
	void readShorts(short[] shorts, int off, int amt) throws IOException;
	void readChars(char[] chars, int off, int amt) throws IOException;
	void readInts(int[] ints, int off, int amt) throws IOException;
	void readLongs(long[] longs, int off, int amt) throws IOException;
	
	void rewind(long len) throws IOException;
	void skip(long pos) throws IOException;
	void seek(long pos) throws IOException;
	
	long available() throws IOException;
	
	default CString readText(int length) throws IOException
	{
		return new CString(this.readChars(length));
	}

	default <T> T resurrect(Factory<T> obj) throws IOException, InstantiationException
	{
		return obj.resurrect(this);
	}
	
	default void readBytes(byte[] bytes) throws IOException
	{
		this.readBytes(bytes, 0, bytes.length);
	}
	
	default void readShorts(short[] shorts) throws IOException
	{
		this.readShorts(shorts, 0, shorts.length);
	}
	
	default void readChars(char[] chars) throws IOException
	{
		this.readChars(chars, 0, chars.length);
	}
	
	default void readInts(int[] ints) throws IOException
	{
		this.readInts(ints, 0, ints.length);
	}
	
	default void readLongs(long[] longs) throws IOException
	{
		this.readLongs(longs, 0, longs.length);
	}
	
	default byte[] readBytes(int bytes) throws IOException
	{
		byte[] data = new byte[bytes];
		this.readBytes(data, 0, bytes);
		return data;
	}
	
	default short[] readShorts(int shorts) throws IOException
	{
		short[] data = new short[shorts];
		this.readShorts(data, 0, shorts);
		return data;
	}
	
	default char[] readChars(int chars) throws IOException
	{
		char[] data = new char[chars];
		this.readChars(data, 0, chars);
		return data;
	}
	
	default int[] readInts(int ints) throws IOException
	{
		int[] data = new int[ints];
		this.readInts(data, 0, ints);
		return data;
	}
	
	default long[] readLongs(int longs) throws IOException
	{
		long[] data = new long[longs];
		this.readLongs(data, 0, longs);
		return data;
	}
	
	default byte[] readByteArr() throws IOException
	{
		byte[] arr = new byte[this.readInt()];
		this.readBytes(arr);
		return arr;
	}
	
	default short[] readShortArr() throws IOException
	{
		short[] arr = new short[this.readInt()];
		this.readShorts(arr);
		return arr;
	}
	
	default char[] readCharArr() throws IOException
	{
		char[] arr = new char[this.readInt()];
		this.readChars(arr);
		return arr;
	}
	
	default int[] readIntArr() throws IOException
	{
		int[] arr = new int[this.readInt()];
		this.readInts(arr);
		return arr;
	}
	
	default long[] readLongArr() throws IOException
	{
		long[] arr = new long[this.readInt()];
		this.readLongs(arr);
		return arr;
	}

}

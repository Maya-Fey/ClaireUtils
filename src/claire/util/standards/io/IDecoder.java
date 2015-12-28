package claire.util.standards.io;

import java.io.IOException;

public interface IDecoder {
	
	void setToDecode(IIncomingStream is);
	
	char readChar() throws IOException;
	void readChars(char[] chars, int start, int len) throws IOException;
	
	int readChar32() throws IOException;
	void readChars32(int[] chars, int start, int len) throws IOException;
	
	long readChar64() throws IOException;
	void readChars64(long[] chars, int start, int len) throws IOException;
	
	default char[] readChars(int chars) throws IOException
	{
		char[] arr = new char[chars];
		readChars(arr, 0, chars);
		return arr;
	}
	
	default int[] readChars32(int ints) throws IOException
	{
		int[] arr = new int[ints];
		readChars32(arr, 0, ints);
		return arr;
	}
	
	default long[] readChars64(int longs) throws IOException
	{
		long[] arr = new long[longs];
		readChars64(arr, 0, longs);
		return arr;
	}
	
	default void readChars(char[] chars) throws IOException
	{
		readChars(chars, 0, chars.length);
	}
	
	default void readChars32(int[] chars) throws IOException
	{
		readChars32(chars, 0, chars.length);
	}
	
	default void readChars64(long[] chars) throws IOException
	{
		readChars64(chars, 0, chars.length);
	}

}

package claire.util.standards.io;

import java.io.IOException;

public interface IEncoder {
	
	public void setToEncode(IOutgoingStream os);
	
	void writeChar(char c) throws IOException;
	void writeChars(char[] chars, int start, int len) throws IOException;
	
	void writeChar32(int c) throws IOException;
	void writeChars32(int[] chars, int start, int len) throws IOException;
	
	void writeChar64(long c) throws IOException;
	void writeChars64(long[] chars, int start, int len) throws IOException;
	
	default void writeChars(char[] chars) throws IOException
	{
		writeChars(chars, 0, chars.length);
	}
	
	default void writeChars32(int[] chars) throws IOException
	{
		writeChars32(chars, 0, chars.length);
	}
	
	default void writeChars64(long[] chars) throws IOException
	{
		writeChars64(chars, 0, chars.length);
	}

}

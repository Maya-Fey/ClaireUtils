package claire.util.standards.io;

import java.io.IOException;

import claire.util.encoding.CString;

public interface ITextReader {

	void useDecoder(IDecoder decoder);
	void readFrom(IIncomingStream is);
	
	void readLine(char[] chars, int start) throws IOException;
	char[] readLine() throws IOException;
	String readLineString() throws IOException;
	CString readLineCString() throws IOException;
	
	char[][] readLines() throws IOException;
	String[] readLineStrings() throws IOException;
	CString[] readLineCStrings() throws IOException;
	
	void skip() throws IOException;
	void skip(int lines) throws IOException;
	
	boolean hasLines() throws IOException;
	
}

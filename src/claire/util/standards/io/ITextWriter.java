package claire.util.standards.io;

import java.io.IOException;

import claire.util.encoding.CString;

public interface ITextWriter {
	
	void useEncoder(IEncoder enc);
	void setStream(IOutgoingStream os);
	void newline() throws IOException;
	void print(char c) throws IOException;
	void print(char[] chars) throws IOException;
	void print(String s) throws IOException;
	void print(CString s) throws IOException;
	void println(char c) throws IOException;
	void println(char[] chars) throws IOException;
	void println(String s) throws IOException;
	void println(CString s) throws IOException;

}

package claire.util.encoding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import claire.util.io.FileIncomingStream;
import claire.util.memory.array.Buffer;
import claire.util.memory.buffer.CharBuffer;
import claire.util.standards.io.IDecoder;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.ITextReader;

public class TextReader implements ITextReader {
	
	private IDecoder decoder;
	private IIncomingStream is;
	
	private final CharBuffer buf = new CharBuffer(64);
	
	private boolean end = false;

	public TextReader(IDecoder decoder)
	{
		this.decoder = decoder;
	}
	
	public TextReader(IDecoder decoder, IIncomingStream stream)
	{
		this(decoder);
		this.is = stream;
		this.decoder.setToDecode(stream);
	}
	
	public TextReader(IDecoder decoder, File f) throws FileNotFoundException
	{
		this(decoder);
		this.is = new FileIncomingStream(f);
		this.decoder.setToDecode(this.is);
	}
	
	public void useDecoder(IDecoder decoder)
	{
		this.decoder = decoder;
		this.decoder.setToDecode(is);
	}
	
	public void readFrom(IIncomingStream is)
	{
		this.decoder.setToDecode(is);
	}
	
	public void readLine(char[] chars, int start) throws IOException
	{
		this.fillLine();
		buf.get(chars, start);
	}

	public char[] readLine() throws IOException
	{
		this.fillLine();
		return buf.get();
	}

	public String readLineString() throws IOException
	{
		this.fillLine();
		return new String(buf.get());
	}

	public CString readLineCString() throws IOException
	{
		this.fillLine();
		return new CString(buf.get());
	}

	public char[][] readLines() throws IOException
	{
		Buffer<char[]> buffer = new Buffer<char[]>(char[].class, 16);
		while(end == false) 
			buffer.add(this.readLine());
		return buffer.get();
	}

	public String[] readLineStrings() throws IOException
	{
		Buffer<String> buffer = new Buffer<String>(String.class, 16);
		while(end == false) 
			buffer.add(this.readLineString());
		return buffer.get();
	}

	public CString[] readLineCStrings() throws IOException
	{
		Buffer<CString> buffer = new Buffer<CString>(CString.class, 16);
		while(end == false) 
			buffer.add(this.readLineCString());
		return buffer.get();
	}

	public void skip() throws IOException
	{
		while(decoder.readChar() != '\n');
	}

	public void skip(int lines) throws IOException
	{
		while(lines-- > 0)
			skip();
	}
	
	private void fillLine() throws IOException
	{
		char c;
		while(true)
		{
			try {
				c = decoder.readChar();
			} catch(java.io.IOException e) {
				end = true;
				break;
			}
			if(c == '\n')
				break;
			else if(c != '\r')
				buf.add(c);
		}
	}

	public boolean hasLines() throws IOException
	{
		return !end;
	}

}

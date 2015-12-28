package claire.util.encoding;

import java.io.File;
import java.io.IOException;

import claire.util.io.FileOutgoingStream;
import claire.util.standards.io.IEncoder;
import claire.util.standards.io.IOutgoingStream;
import claire.util.standards.io.ITextWriter;

public class TextWriter 
	   implements ITextWriter {
	
	private IEncoder encoder;
	private IOutgoingStream os;
	
	public TextWriter(IEncoder encoder)
	{
		this.encoder = encoder;
	}
	
	public TextWriter(IEncoder encoder, IOutgoingStream stream)
	{
		this(encoder);
		this.setStream(stream);
	}
	
	public TextWriter(IEncoder encoder, File f) throws IOException
	{
		this(encoder);
		this.setStream(new FileOutgoingStream(f));
	}

	public void useEncoder(IEncoder enc)
	{
		this.encoder = enc;
		this.encoder.setToEncode(os);
	}
	
	public void setStream(IOutgoingStream os)
	{
		this.os = os;
		this.encoder.setToEncode(os);
	}

	public void newline() throws IOException
	{
		encoder.writeChars(EncodingUtil.NEWLINE);
	}

	public void print(char c) throws IOException
	{
		encoder.writeChar(c);
	}

	public void print(char[] chars) throws IOException
	{
		encoder.writeChars(chars);
	}

	public void print(String s) throws IOException
	{
		encoder.writeChars(s.toCharArray());
	}

	public void print(CString s) throws IOException
	{
		encoder.writeChars(s.array());
	}

	public void println(char c) throws IOException
	{
		this.print(c);
		this.newline();
	}

	public void println(char[] chars) throws IOException
	{
		this.print(chars);
		this.newline();
	}

	public void println(String s) throws IOException
	{
		this.print(s);
		this.newline();
	}

	public void println(CString s) throws IOException
	{
		this.print(s);
		this.newline();
	}

}

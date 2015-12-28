package claire.util.encoding;

import java.io.File;
import java.io.IOException;

import claire.util.io.FileOutgoingStream;
import claire.util.standards.io.IEncoder;
import claire.util.standards.io.IOutgoingStream;

public class Encoder 
	   implements IEncoder {

	public static final IEncoder getEncoder(String format)
	{
		switch(format)
		{
			case "UTF-8":
				return new UTF8.Encoder();
			case "UTF-16":
				return new UTF16.Encoder();
			case "CTF-S":
				return new CTFS.Encoder();
			case "CTF-L":
				return new CTFL.Encoder();
			default:
				throw new java.lang.NullPointerException("Unsupported format");
		}
	}
	
	private final IEncoder encoder;
	
	public Encoder(String format)
	{
		this.encoder = getEncoder(format);
	}
	
	public Encoder(IOutgoingStream os, String format)
	{
		this(format);
		encoder.setToEncode(os);
	}
	
	public Encoder(File f, String format) throws IOException
	{
		this(new FileOutgoingStream(f), format);
	}

	public void setToEncode(IOutgoingStream os)
	{
		this.encoder.setToEncode(os);
	}

	public void writeChar(char c) throws IOException
	{
		this.encoder.writeChar(c);
	}

	public void writeChars(char[] chars, int start, int len) throws IOException
	{
		this.encoder.writeChars(chars, start, len);
	}

	public void writeChar32(int c) throws IOException
	{
		this.encoder.writeChar32(c);
	}

	public void writeChars32(int[] chars, int start, int len) throws IOException
	{
		this.encoder.writeChars32(chars, start, len);
	}
	
	public void writeChar64(long c) throws IOException
	{
		this.encoder.writeChar64(c);
	}

	public void writeChars64(long[] chars, int start, int len) throws IOException
	{
		this.encoder.writeChars64(chars, start, len);
	}
}

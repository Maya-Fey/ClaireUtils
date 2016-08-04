package claire.util.encoding;

import java.io.File;
import java.io.IOException;

import claire.util.io.FileIncomingStream;
import claire.util.standards.io.IDecoder;
import claire.util.standards.io.IIncomingStream;

public class Decoder 
	   implements IDecoder {

	public static final IDecoder getDecoder(String format)
	{
		switch(format)
		{
			case "UTF-8":
				return new UTF8.Decoder();
			case "UTF-16":
				return new UTF16.Decoder();
			case "CTF-S":
				return new CTFS.Decoder();
			case "CTF-L":
				return new CTFL.Decoder();
			default:
				throw new java.lang.NullPointerException("Unsupported format");
		}
	}
	
	private final IDecoder decoder;
	
	public Decoder(String format)
	{
		this.decoder = getDecoder(format);
	}
	
	public Decoder(IIncomingStream os, String format)
	{
		this(format);
		decoder.setToDecode(os);
	}
	
	public Decoder(File f, String format) throws IOException
	{
		this(new FileIncomingStream(f), format);
	}

	public void setToDecode(IIncomingStream os)
	{
		this.decoder.setToDecode(os);
	}

	public char readChar() throws IOException
	{
		return this.decoder.readChar();
	}

	public void readChars(char[] chars, int start, int len) throws IOException
	{
		this.decoder.readChars(chars, start, len);
	}

	public int readChar32() throws IOException
	{
		return this.decoder.readChar32();
	}

	public void readChars32(int[] chars, int start, int len) throws IOException
	{
		this.decoder.readChars32(chars, start, len);
	}
	
	public long readChar64() throws IOException
	{
		return this.decoder.readChar64();
	}

	public void readChars64(long[] chars, int start, int len) throws IOException
	{
		this.decoder.readChars64(chars, start, len);
	}
}

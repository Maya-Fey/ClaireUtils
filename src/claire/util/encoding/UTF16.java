package claire.util.encoding;

import java.io.IOException;

import claire.util.standards.io.IDecoder;
import claire.util.standards.io.IEncoder;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class UTF16 {
	
	public static void fromUTF64(IOutgoingStream os, long[] utf, int start, int len) throws IOException
	{
		long l;
		while(len > 0)
		{
			l = utf[start++];
			if(l > 0xFFFF)
				os.writeChar((char) 0xFFFD);
			else
				os.writeChar((char) l);
			len--;
		}
	}
	
	public static void fromUTF64(IOutgoingStream os, long[] utf) throws IOException
	{
		fromUTF64(os, utf, 0, utf.length);
	}
	
	public static void fromUTF64(IOutgoingStream os, long l) throws IOException
	{
		if(l > 0xFFFF)
			os.writeChar((char) 0xFFFD);
		else
			os.writeChar((char) l);
	}
	
	public static void fromUTF64(char[] chars, int start0, long[] utf, int start1, int len)
	{
		long l;
		while(len > 0)
		{
			l = utf[start1++];
			if(l > 0xFFFF)
				chars[start0++] = 0xFFFD;
			else
				chars[start0++] = (char) l;
			len--;
		}
	}
	
	public static void fromUTF64(char[] chars, int start0, long[] utf, int start1)
	{
		fromUTF64(chars, start0, utf, start1, utf.length - start1);
	}
	
	public static void fromUTF64(char[] chars, long[] utf)
	{
		fromUTF64(chars, 0, utf, 0, utf.length);
	}
	
	public static char[] fromUTF64(long[] utf, int start, int len)
	{
		char[] chars = new char[len];
		fromUTF64(chars, 0, utf, start, chars.length);
		return chars;
	}
	
	public static char[] fromUTF64(long[] utf)
	{
		char[] chars = new char[utf.length];
		fromUTF64(chars, 0, utf, 0, chars.length);
		return chars;
	}
	
	public static void fromUTF32(IOutgoingStream os, int[] utf, int start, int len) throws IOException
	{
		int l;
		while(len > 0)
		{
			l = utf[start++];
			if(l > 0xFFFF)
				os.writeChar((char) 0xFFFD);
			else
				os.writeChar((char) l);
			len--;
		}
	}
	
	public static void fromUTF32(IOutgoingStream os, int[] utf) throws IOException
	{
		fromUTF32(os, utf, 0, utf.length);
	}
	
	public static void fromUTF32(IOutgoingStream os, int l) throws IOException
	{
		if(l > 0xFFFF)
			os.writeChar((char) 0xFFFD);
		else
			os.writeChar((char) l);
	}
	
	public static void fromUTF32(char[] chars, int start0, int[] utf, int start1, int len)
	{
		int l;
		while(len > 0)
		{
			l = utf[start1++];
			if(l > 0xFFFF)
				chars[start0++] = 0xFFFD;
			else
				chars[start0++] = (char) l;
			len--;
		}
	}
	
	public static void fromUTF32(char[] chars, int start0, int[] utf, int start1)
	{
		fromUTF32(chars, start0, utf, start1, utf.length - start1);
	}
	
	public static void fromUTF32(char[] chars, int[] utf)
	{
		fromUTF32(chars, 0, utf, 0, utf.length);
	}
	
	public static char[] fromUTF32(int[] utf, int start, int len)
	{
		char[] chars = new char[len];
		fromUTF32(chars, 0, utf, start, len);
		return chars;
	}
	
	public static char[] fromUTF32(int[] utf)
	{
		char[] chars = new char[utf.length];
		fromUTF32(chars, 0, utf, 0, chars.length);
		return chars;
	}
	
	public static void fromUTF16(IOutgoingStream os, char[] chars, int start, int len) throws IOException
	{
		os.writeChars(chars, start, len);
	}
	
	public static void fromUTF16(IOutgoingStream os, char[] chars) throws IOException
	{
		os.writeChars(chars);
	}
	
	public static void fromUTF16(IOutgoingStream os, char chars) throws IOException
	{
		os.writeChar(chars);
	}
	
	public static void toUTF16(IIncomingStream is, char[] chars, int start, int len) throws IOException
	{
		is.readChars(chars, start, len);
	}
	
	public static void toUTF16(IIncomingStream is, char[] chars) throws IOException
	{
		is.readChars(chars);
	}
	
	public static char toUTF16(IIncomingStream is) throws IOException
	{
		return is.readChar();
	}
	
	public static void toUTF32(IIncomingStream is, int[] chars, int start, int len) throws IOException
	{
		while(len > 0)
		{
			chars[start++] = is.readChar();
			len--;
		}
	}
	
	public static void toUTF32(IIncomingStream is, int[] chars) throws IOException
	{
		toUTF32(is, chars, 0, chars.length);
	}
	
	public static int toUTF32(IIncomingStream is) throws IOException
	{
		return is.readChar();
	}
	
	public static void toUTF32(char[] utf, int start0, int[] chars, int start1, int len)
	{
		while(len > 0)
		{
			chars[start1++] = utf[start0++];
			len--;
		}
	}
	
	public static void toUTF32(char[] utf, int start0, int[] chars, int start1)
	{
		toUTF32(utf, start0, chars, start1, chars.length - start1);
	}
	
	public static void toUTF32(char[] utf, int[] chars)
	{
		toUTF32(utf, 0, chars, 0, chars.length);
	}
	
	public static int[] toUTF32(char[] utf, int start, int len)
	{
		int[] chars = new int[len];
		toUTF32(utf, 0, chars, start, len);
		return chars;
	}
	
	public static int[] toUTF32(char[] utf)
	{
		int[] chars = new int[utf.length];
		toUTF32(utf, 0, chars, 0, utf.length);
		return chars;
	}
	
	public static void toUTF64(IIncomingStream is, long[] chars, int start, int len) throws IOException
	{
		while(len > 0)
		{
			chars[start++] = is.readChar();
			len--;
		}
	}
	
	public static void toUTF64(IIncomingStream is, long[] chars) throws IOException
	{
		toUTF64(is, chars, 0, chars.length);
	}
	
	public static long toUTF64(IIncomingStream is) throws IOException
	{
		return is.readChar();
	}
	
	public static void toUTF64(char[] utf, int start0, long[] chars, int start1, int len)
	{
		while(len > 0)
		{
			chars[start1++] = utf[start0++];
			len--;
		}
	}
	
	public static void toUTF64(char[] utf, int start0, long[] chars, int start1)
	{
		toUTF64(utf, start0, chars, start1, chars.length - start1);
	}
	
	public static void toUTF64(char[] utf, long[] chars)
	{
		toUTF64(utf, 0, chars, 0, chars.length);
	}
	
	public static long[] toUTF64(char[] utf, int start, int len)
	{
		long[] chars = new long[len];
		toUTF64(utf, 0, chars, start, len);
		return chars;
	}
	
	public static long[] toUTF64(char[] utf)
	{
		long[] chars = new long[utf.length];
		toUTF64(utf, 0, chars, 0, utf.length);
		return chars;
	}

	public static final class Decoder
						implements IDecoder
	{
	private IIncomingStream is;
	
	public Decoder() {}
	public Decoder(IIncomingStream is)
	{
		this.is = is;
	}
	
	public void setToDecode(IIncomingStream is)
	{
		this.is = is;
	}

	public char readChar() throws IOException
	{
		return toUTF16(is);
	}

	public void readChars(char[] chars, int start, int len) throws IOException
	{
		toUTF16(is, chars, start, len);
	}

	public int readChar32() throws IOException
	{
		return toUTF32(is);
	}

	public void readChars32(int[] chars, int start, int len) throws IOException
	{
		toUTF32(is, chars, start, len);
	}

	public long readChar64() throws IOException
	{
		return toUTF64(is);
	}

	public void readChars64(long[] chars, int start, int len) throws IOException
	{
		toUTF64(is, chars, start, len);
	}
	
}

public static final class Encoder 
					implements IEncoder
{
	private IOutgoingStream os;
	
	public Encoder() {}
	public Encoder(IOutgoingStream os)
	{
		this.os = os;
	}
	
	public void setToEncode(IOutgoingStream os)
	{
		this.os = os;
	}

	public void writeChar(char c) throws IOException 
	{
		fromUTF16(os, c);
	}

	public void writeChars(char[] chars, int start, int len) throws IOException
	{
		fromUTF16(os, chars, start, len);
	}

	public void writeChar32(int c) throws IOException
	{
		fromUTF32(os, c);
	}

	public void writeChars32(int[] chars, int start, int len) throws IOException
	{
		fromUTF32(os, chars, start, len);
	}

	public void writeChar64(long c) throws IOException
	{
		fromUTF64(os, c);
	}

	public void writeChars64(long[] chars, int start, int len) throws IOException
	{
		fromUTF64(os, chars, start, len);
	}
	
}

}

package claire.util.encoding;

import java.io.IOException;

import claire.util.standards.io.IDecoder;
import claire.util.standards.io.IEncoder;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public final class UTF8 {
	
	private static final int[] MASKS = new int[]
		{
			0x7F,
			0x7FF,
			0xFFFF,
			0x1FFFFF,
			0x3FFFFFF
		};
	
	private static final byte[] B1Masks = new byte[]
		{
			0x7F,
			0x3F,
			0x1F,
			0x0F,
			0x07,
			0x03
		};
	
	public static void fromUTF32(IOutgoingStream out, int[] utf, int start, int len) throws IOException
	{
		int t, cont, i;
		byte[] temp = new byte[5];
		while(len > 0)
		{
			t = utf[start++];
			if((t & 0x007F) == t)
				out.writeByte((byte) t);
			else {
				cont = 1;
				for(i = 1; i < MASKS.length; i++)
					if((t & MASKS[i]) != t)
						cont++;
				i = cont;
				while(i-- > 0) {					
					temp[i] = (byte) (0x80 | (t & 0x3F));
					t >>>= 6;
				}
				out.writeByte((byte) (~B1Masks[cont] | (t & B1Masks[cont])));
				for(i = 0; i < cont; i++)
					out.writeByte(temp[i]);
			}
			len--;
		}
	}
	
	public static void fromUTF32(IOutgoingStream out, int[] utf) throws IOException
	{
		fromUTF32(out, utf, 0, utf.length);
	}
	
	public static void fromUTF32(IOutgoingStream out, int utf) throws IOException
	{
		int cont, i;
		byte[] temp = new byte[5];
		if((utf & 0x007F) == utf)
			out.writeByte((byte) utf);
		else {
			cont = 1;
			for(i = 1; i < MASKS.length; i++)
				if((utf & MASKS[i]) != utf)
					cont++;
			i = cont;
			while(i-- > 0) {					
				temp[i] = (byte) (0x80 | (utf & 0x3F));
				utf >>>= 6;
			}
			out.writeByte((byte) (~B1Masks[cont] | (utf & B1Masks[cont])));
			for(i = 0; i < cont; i++)
				out.writeByte(temp[i]);
		}
	}
	
	public static void fromUTF32(int[] ints, int start0, byte[] bytes, int start1, int len)
	{
		int t, cont, i;
		byte[] temp = new byte[5];
		while(len > 0)
		{
			t = ints[start0++];
			if((t & 0x007F) == t)
				bytes[start1++] = (byte) t;
			else {
				cont = 1;
				for(i = 1; i < MASKS.length; i++)
					if((t & MASKS[i]) != t)
						cont++;
				i = cont;
				while(i-- > 0) {					
					temp[i] = (byte) (0x80 | (t & 0x3F));
					t >>>= 6;
				}
				bytes[start1++] = (byte) (~B1Masks[cont] | (t & B1Masks[cont]));
				for(i = 0; i < cont; i++)
					bytes[start1++] = temp[i];
			}
			len--;
		}
	}
	
	public static void fromUTF32(int[] ints, int start0, byte[] bytes, int start1)
	{
		fromUTF32(ints, start0, bytes, start1, ints.length - start0);
	}
	
	public static void fromUTF32(int[] ints, byte[] bytes)
	{
		fromUTF32(ints, 0, bytes, 0, ints.length);
	}
	
	public static byte[] fromUTF32(int[] ints, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0) {
			len++;
			int c = ints[start++];
			for(int i = 0; i < MASKS.length; i++)
			if((c & MASKS[i]) != c)
				len++;
		}
		byte[] bytes = new byte[len];
		fromUTF32(ints, 0, bytes, 0, ints.length);
		return bytes;
	}
	
	public static byte[] fromUTF32(int[] ints)
	{
		int len = 0;
		for(int c : ints) {
			len++;
			for(int i = 0; i < MASKS.length; i++)
			if((c & MASKS[i]) != c)
				len++;
		}
		byte[] bytes = new byte[len];
		fromUTF32(ints, 0, bytes, 0, ints.length);
		return bytes;
	}
	
	public static void fromUTF16(IOutgoingStream out, char[] utf, int start, int len) throws IOException
	{
		char t;
		byte t1, t2;
		while(len > 0)
		{
			t = utf[start++];
			if((t & 0x007F) == t)
				out.writeByte((byte) t);
			else {
				if((t & 0x07FF) == t) {
					t1 = (byte) (0x80 | (t & 0x3F));
					t >>>= 6;
					out.writeByte((byte) (0xC0 | (t & 0x1F)));
					out.writeByte(t1);
				} else {
					t2 = (byte) (0x80 | (t & 0x3F));
					t >>>= 6;
					t1 = (byte) (0x80 | (t & 0x3F));
					t >>>= 6;
					out.writeByte((byte) (0xE0 | (t & 0x0F)));
					out.writeByte(t1);
					out.writeByte(t2);
				}
			}
			len--;
		}
	}
	
	public static void fromUTF16(IOutgoingStream out, char[] utf) throws IOException
	{
		fromUTF16(out, utf, 0, utf.length);
	}
	
	public static void fromUTF16(IOutgoingStream out, char utf) throws IOException
	{
		byte t1, t2;
		if((utf & 0x007F) == utf)
			out.writeByte((byte) utf);
		else {
			if((utf & 0x07FF) == utf) {
				t1 = (byte) (0x80 | (utf & 0x3F));
				utf >>>= 6;
				out.writeByte((byte) (0xC0 | (utf & 0x1F)));
				out.writeByte(t1);
			} else {
				t2 = (byte) (0x80 | (utf & 0x3F));
				utf >>>= 6;
				t1 = (byte) (0x80 | (utf & 0x3F));
				utf >>>= 6;
				out.writeByte((byte) (0xE0 | (utf & 0x0F)));
				out.writeByte(t1);
				out.writeByte(t2);
			}
		}
	}
	
	public static void fromUTF16(char[] chars, int start0, byte[] bytes, int start1, int len)
	{
		char t;
		byte t1, t2;
		while(len > 0)
		{
			t = chars[start0++];
			if((t & 0x007F) == t)
				bytes[start1++] = (byte) t;
			else {
				if((t & 0x07FF) == t) {
					t1 = (byte) (0x80 | (t & 0x3F));
					t >>>= 6;
					bytes[start1++] = (byte) (0xC0 | (t & 0x1F));
					bytes[start1++] = t1;
				} else {
					t2 = (byte) (0x80 | (t & 0x3F));
					t >>>= 6;
					t1 = (byte) (0x80 | (t & 0x3F));
					t >>>= 6;
					bytes[start1++] = (byte) (0xE0 | (t & 0x0F));
					bytes[start1++] = t1;
					bytes[start1++] = t2;
				}
			}
			len--;
		}
	}
	
	public static void fromUTF16(char[] chars, int start0, byte[] bytes, int start1)
	{
		fromUTF16(chars, start0, bytes, start1, chars.length - start0);
	}
	
	public static void fromUTF16(char[] chars, byte[] bytes)
	{
		fromUTF16(chars, 0, bytes, 0, chars.length);
	}
	
	public static byte[] fromUTF16(char[] chars, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0) {
			len++;
			char c = chars[start++];
			for(int i = 0; i < MASKS.length; i++)
			if((c & MASKS[i]) != c)
				len++;
		}
		byte[] bytes = new byte[len];
		fromUTF16(chars, 0, bytes, 0, chars.length);
		return bytes;
	}
	
	public static byte[] fromUTF16(char[] chars)
	{
		int len = 0;
		for(char c : chars) {
			len++;
			for(int i = 0; i < MASKS.length; i++)
			if((c & MASKS[i]) != c)
				len++;
		}
		byte[] bytes = new byte[len];
		fromUTF16(chars, 0, bytes, 0, chars.length);
		return bytes;
	}
	
	public static void fromString(String chars, int start0, byte[] bytes, int start1, int len)
	{
		char t;
		byte t1, t2;
		while(len > 0)
		{
			t = chars.charAt(start0++);
			if((t & 0x007F) == t)
				bytes[start1++] = (byte) t;
			else {
				if((t & 0x07FF) == t) {
					t1 = (byte) (0x80 | (t & 0x3F));
					t >>>= 6;
					bytes[start1++] = (byte) (0xC0 | (t & 0x1F));
					bytes[start1++] = t1;
				} else {
					t2 = (byte) (0x80 | (t & 0x3F));
					t >>>= 6;
					t1 = (byte) (0x80 | (t & 0x3F));
					t >>>= 6;
					bytes[start1++] = (byte) (0xE0 | (t & 0x0F));
					bytes[start1++] = t1;
					bytes[start1++] = t2;
				}
			}
			len--;
		}
	}
	
	public static void fromString(String chars, int start0, byte[] bytes, int start1)
	{
		fromString(chars, start0, bytes, start1, chars.length() - start0);
	}
	
	public static void fromString(String chars, byte[] bytes)
	{
		fromString(chars, 0, bytes, 0, chars.length());
	}
	
	
	public static byte toASCII(IIncomingStream is) throws IOException
	{
		byte t = is.readByte();
		while(true)
			if(t >= 0)
				return t;
			else if((t & 0xC0) == 0x80)
				t = is.readByte();
			else {
				t <<= 1;
				while((t <<= 1) < 0)
					is.readByte();
				return '?';
			}
	}
	
	public static void toASCII(IIncomingStream is, byte[] bytes, int start, int len) throws IOException
	{
		byte t = is.readByte();
		while(len > 0)
			if(t >= 0) {
				bytes[start++] =  t;
				len--;
			} else if((t & 0xC0) == 0x80)
				t = is.readByte();
			else {
				t <<= 1;
				while((t <<= 1) < 0)
					is.readByte();
				bytes[start++] = '?';
				len--;
			}
	}
	
	public static void toASCII(IIncomingStream is, byte[] bytes) throws IOException
	{
		toASCII(is, bytes, 0, bytes.length);
	}
	
	public static void toASCII(byte[] utf8, int start0, byte[] ascii, int start1, int len)
	{
		byte t, c;
		while(len > 0)
		{
			t = utf8[start0++];
			c = (byte) (t & 0xC0);
			if(c >= 0)
				ascii[start1++] = t;
			else if(c == 0xC0)
				ascii[start1++] = '?';
			len--;
		}
	}
	
	public static void toASCII(byte[] utf8, int start0, byte[] ascii, int start1)
	{
		toASCII(utf8, start0, ascii, start1, ascii.length - start1);
	}
	
	public static void toASCII(byte[] utf8, byte[] ascii)
	{
		toASCII(utf8, 0, ascii, 0, ascii.length);
	}
	
	public static byte[] toASCII(byte[] utf8, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0) {
			byte b = utf8[start++];
			if((b & 0xC0) != 0x80)
				len++;
		}
		byte[] bytes = new byte[len];
		toASCII(utf8, 0, bytes, 0, len);
		return bytes;
	}
	
	public static byte[] toASCII(byte[] utf8)
	{
		int len = 0;
		for(byte b : utf8)
			if((b & 0xC0) != 0x80)
				len++;
		byte[] bytes = new byte[len];
		toASCII(utf8, 0, bytes, 0, len);
		return bytes;
	}
	
	public static char toUTF16(IIncomingStream is) throws IOException
	{
		byte b = is.readByte();
		if(b >= 0)
			return (char) (b & 0xFF);
		else if((b & 0xC0) == 0xC0) {
			int cont = 1;
			b <<= 2;
			while(b < 0) {
				cont++;
				b <<= 1;
			}
			if(cont > 2) {
				for(; cont > 0; cont--)
					is.readByte();
				return 0xFFFD;
			}
			b >>>= 1 + cont;
			char t = 0x00;
			t |= b;
			for(; cont > 0; cont--) {
				t <<= 6;
				t |= is.readByte() & 0x3F;
			}
			return t;
		} else
			return 0xFFFD;
	}
	
	public static void toUTF16(IIncomingStream is, char[] chars, int start, int len) throws IOException
	{
		byte b;
		char t;
		int cont;
		while(len > 0)
		{
			b = is.readByte();
			if(b >= 0)
				chars[start++] = (char) (b & 0xFF);
			else if((b & 0xC0) == 0xC0) {
				cont = 1;
				b <<= 2;
				while(b < 0) {
					cont++;
					b <<= 1;
				}
				if(cont > 2) {
					chars[start++] = 0xFFFD;
					for(; cont > 0; cont--)
						is.readByte();
					len--;
					continue;
				}
				b >>>= 1 + cont;
				t = 0x00;
				t |= b;
				for(; cont > 0; cont--) {
					t <<= 6;
					t |= is.readByte() & 0x3F;
				}
				chars[start++] = t;
			} else
				chars[start++] = 0xFFFD;
			len--;
		}
	}
	
	public static void toUTF16(IIncomingStream is, char[] chars) throws IOException
	{
		toUTF16(is, chars, 0, chars.length);
	}
	
	public static void toUTF16(byte[] bytes, int start0, char[] chars, int start1, int codepoints)
	{
		int cont; 
		byte comp;
		while(codepoints > 0)
		{
			comp = bytes[start0++];
			if((comp & 0xC0) == 0xC0) {
				cont = 1;
				comp <<= 2;	
				while((comp & 0x80) != 0) {
					cont++;
					comp <<= 1; 	
				}
				if(cont > 2) {
					chars[start1++] = 0xFFFD;
					start0 += cont;
					codepoints--;
					continue;
				}
				comp >>>= cont + 1;
				char t = 0x0000;
				t |= comp;
				for(; cont > 0; cont--) {
					t <<= 6;
					t |= bytes[start0++] & 0x3F;
				}
				chars[start1++] = t;
			} else if(comp >= 0) {
				chars[start1++] = (char) comp;
			} else {
				chars[start1++] = 0xFFFD;
			}
			codepoints--;
		}
	}
	
	public static void toUTF16(byte[] bytes, int start0, char[] chars, int start1)
	{
		toUTF16(bytes, start0, chars, start1, chars.length - start1);
	}
	
	public static void toUTF16(byte[] bytes, char[] chars)
	{
		toUTF16(bytes, 0, chars, 0, chars.length);
	}
	
	public static char[] toUTF16(byte[] bytes, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0) {
			byte b = bytes[start++];
			if((b & 0xC0) != 0x80)
				len++;
		}
		char[] chars = new char[len];
		toUTF16(bytes, 0, chars, 0, len);
		return chars;
	}
	
	public static char[] toUTF16(byte[] bytes)
	{
		int len = 0;
		for(byte b : bytes)
			if((b & 0xC0) != 0x80)
				len++;
		char[] chars = new char[len];
		toUTF16(bytes, 0, chars, 0, len);
		return chars;
	}
	
	public static int toUTF32(IIncomingStream is) throws IOException
	{
		byte b = is.readByte();
		if(b >= 0)
			return (int) (b & 0xFF);
		else if((b & 0xC0) == 0xC0) {
			int cont = 1;
			b <<= 2;
			while(b < 0) {
				cont++;
				b <<= 1;
			}
			b >>>= 1 + cont;
			int t = 0x00;
			t |= b;
			for(; cont > 0; cont--) {
				t <<= 6;
				t |= is.readByte() & 0x3F;
			}
			return t;
		} else
			return 0xFFFD;
	}
	
	public static void toUTF32(IIncomingStream is, int[] chars, int start, int len) throws IOException
	{
		byte b;
		int cont, t;
		while(len > 0)
		{
			b = is.readByte();
			if(b >= 0)
				chars[start++] = (int) (b & 0xFF);
			else if((b & 0xC0) == 0xC0) {
				cont = 1;
				b <<= 2;
				while(b < 0) {
					cont++;
					b <<= 1;
				}
				b >>>= 1 + cont;
				t = 0x00;
				t |= b;
				for(; cont > 0; cont--) {
					t <<= 6;
					t |= is.readByte() & 0x3F;
				}
				chars[start++] = t;
			} else
				chars[start++] = 0xFFFD;
			len--;
		}
	}
	
	public static void toUTF32(IIncomingStream is, int[] chars) throws IOException
	{
		toUTF32(is, chars, 0, chars.length);
	}
	
	public static void toUTF32(byte[] bytes, int start0, int[] ints, int start1, int codepoints)
	{
		int cont;	 
		byte comp;
		while(codepoints > 0)
		{
			comp = bytes[start0++];
			if((comp & 0xC0) == 0xC0) {
				cont = 1;
				comp <<= 2;	
				while((comp & 0x80) != 0 && cont < 5) {
					cont++;
					comp <<= 1; 	
				}
				comp = (byte) ((comp & 0xFF) >>> (cont + 1)); 	
				int t = 0x0000;
				t |= comp;
				for(; cont > 0; cont--) {
					t <<= 6;
					t |= bytes[start0++] & 0x3F;
				}
				ints[start1++] = t;
			} else if(comp >= 0) {
				ints[start1++] = (int) comp;
			} else {
				ints[start1++] = 0xFFFD;
			}
			codepoints--;
		}
	}
	
	public static void toUTF32(byte[] bytes, int start0, int[] ints, int start1)
	{
		toUTF32(bytes, start0, ints, start1, ints.length - start1);
	}
	
	public static void toUTF32(byte[] bytes, int[] ints)
	{
		toUTF32(bytes, 0, ints, 0, ints.length);
	}
	
	public static int[] toUTF32(byte[] bytes, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0) {
			byte b = bytes[start++];
			if((b & 0xC0) != 0x80)
				len++;
		}
		int[] ints = new int[len];
		toUTF32(bytes, 0, ints, 0, len);
		return ints;
	}
		
	public static int[] toUTF32(byte[] bytes)
	{
		int len = 0;
		for(byte b : bytes)
			if((b & 0xC0) != 0x80)
				len++;
		int[] ints = new int[len];
		toUTF32(bytes, 0, ints, 0, len);
		return ints;
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

		public long readChar64()
		{
			throw new java.lang.UnsupportedOperationException();
		}

		public void readChars64(long[] chars, int start, int len)
		{
			throw new java.lang.UnsupportedOperationException();
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

		public void writeChar64(long c)
		{
			throw new java.lang.UnsupportedOperationException();
		}

		public void writeChars64(long[] chars, int start, int len)
		{
			throw new java.lang.UnsupportedOperationException();
		}
		
	}
}

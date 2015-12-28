package claire.util.encoding;

import java.io.IOException;

import claire.util.standards.io.IDecoder;
import claire.util.standards.io.IEncoder;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

/**
 * Claire text format - Large
 * 
 * @author Claire
 */
public final class CTFL {
	
	private static final long[] MASKS = new long[]
		{
			0x7F,
			0xFFF,
			0xFFFFF,
			0xFFFFFFF,
			0xFFFFFFFFFL,
			0xFFFFFFFFFFFL,
			0xFFFFFFFFFFFFFL,
			0xFFFFFFFFFFFFFFFL,
		};
	
	public static void fromUTF16(IOutgoingStream os, char c) throws IOException
	{
		int cont = 0;
		for(int i = 0; i < MASKS.length; i++)
			if((c & MASKS[i]) != c)
				cont++;
			else 
				break;
		if(cont > 0) {
			os.writeByte((byte) (0x80 | (--cont << 4) | (c & 0x000F)));
			c >>>= 4;
			os.writeByte((byte) c);
			while(cont > 0) {
				c >>>= 8;
				os.writeByte((byte) c);
				cont--;
			}
		} else {
			os.writeByte((byte) (c & 0x7F));
			c >>>= 4;
		}
	}
	
	public static void fromUTF16(IOutgoingStream os, char[] utf, int start, int len) throws IOException
	{
		int cont;
		char c;
		while(len > 0)
		{
			c = utf[start++];
			cont = 0;
			for(int i = 0; i < MASKS.length; i++)
				if((c & MASKS[i]) != c)
					cont++;
				else 
					break;
			if(cont > 0) {
				os.writeByte((byte) (0x80 | (--cont << 4) | (c & 0x000F)));
				c >>>= 4;
				os.writeByte((byte) c);
				while(cont > 0) {
					c >>>= 8;
					os.writeByte((byte) c);
					cont--;
				}
			} else {
				os.writeByte((byte) (c & 0x7F));
				c >>>= 4;
			}
			len--;
		}
	}
	
	public static void fromUTF16(IOutgoingStream os, char[] utf) throws IOException
	{
		fromUTF16(os, utf, 0, utf.length);
	}
	
	public static void fromUTF16(char[] utf, int start0, byte[] bytes, int start1, int len)
	{
		char c;
		int cont;
		while(len > 0)
		{
			c = utf[start0++];
			cont = 0;
			for(int i = 0; i < MASKS.length; i++)
				if((c & MASKS[i]) != c)
					cont++;
				else 
					break;
			if(cont > 0) {
				bytes[start1++] = (byte) (0x80 | (--cont << 4) | (c & 0x000F));
				c >>>= 4;
				bytes[start1++] = (byte) c;
				while(cont > 0) {
					c >>>= 8;
					bytes[start1++] = (byte) c;
					cont--;
				}
			} else {
				bytes[start1++] = (byte) (c & 0x7F);
				c >>>= 4;
			}
			len--;
		}
	}
	
	public static void fromUTF16(char[] utf, int start0, byte[] bytes, int start1)
	{
		fromUTF16(utf, start0, bytes, start1, utf.length - start1);
	}
	
	public static void fromUTF16(char[] utf, byte[] bytes)
	{
		fromUTF16(utf, 0, bytes, 0, utf.length);
	}
	
	public static byte[] fromUTF16(char[] utf)
	{
		int len = 0;
		for(char c : utf)
			if(c > 0x7F)
				if(c > 0x0FFF)
					len += 3;
				else
					len += 2;
			else
				len++;
		byte[] bytes = new byte[len];
		fromUTF16(utf, 0, bytes, 0, utf.length);
		return bytes;
	}
	
	public static void fromUTF32(IOutgoingStream os, int c) throws IOException
	{
		int cont = 0;
		for(int i = 0; i < MASKS.length; i++)
			if((c & MASKS[i]) != c)
				cont++;
			else 
				break;
		if(cont > 0) {
			os.writeByte((byte) (0x80 | (--cont << 4) | (c & 0x000F)));
			c >>>= 4;
			os.writeByte((byte) c);
			while(cont > 0) {
				c >>>= 8;
				os.writeByte((byte) c);
				cont--;
			}
		} else {
			os.writeByte((byte) (c & 0x7F));
			c >>>= 4;
		}
	}
	
	public static void fromUTF32(IOutgoingStream os, int[] utf, int start, int len) throws IOException
	{
		int cont;
		int c;
		while(len > 0)
		{
			c = utf[start++];
			cont = 0;
			for(int i = 0; i < MASKS.length; i++)
				if((c & MASKS[i]) != c)
					cont++;
				else 
					break;
			if(cont > 0) {
				os.writeByte((byte) (0x80 | (--cont << 4) | (c & 0x000F)));
				c >>>= 4;
				os.writeByte((byte) c);
				while(cont > 0) {
					c >>>= 8;
					os.writeByte((byte) c);
					cont--;
				}
			} else {
				os.writeByte((byte) (c & 0x7F));
				c >>>= 4;
			}
			len--;
		}
	}
	
	public static void fromUTF32(IOutgoingStream os, int[] utf) throws IOException
	{
		fromUTF32(os, utf, 0, utf.length);
	}
	
	public static void fromUTF32(int[] utf, int start0, byte[] bytes, int start1, int len)
	{
		int c, cont;
		while(len > 0)
		{
			c = utf[start0++];
			cont = 0;
			for(int i = 0; i < MASKS.length; i++)
				if((c & MASKS[i]) != c)
					cont++;
				else 
					break;
			if(cont > 0) {
				bytes[start1++] = (byte) (0x80 | (--cont << 4) | (c & 0x000F));
				c >>>= 4;
				bytes[start1++] = (byte) c;
				while(cont > 0) {
					c >>>= 8;
					bytes[start1++] = (byte) c;
					cont--;
				}
			} else {
				bytes[start1++] = (byte) (c & 0x7F);
				c >>>= 4;
			}
			len--;
		}
	}
	
	public static void fromUTF32(int[] utf, int start0, byte[] bytes, int start1)
	{
		fromUTF32(utf, start0, bytes, start1, utf.length - start1);
	}
	
	public static void fromUTF32(int[] utf, byte[] bytes)
	{
		fromUTF32(utf, 0, bytes, 0, utf.length);
	}
	
	public static byte[] fromUTF32(int[] utf)
	{
		int len = 0;
		for(int c : utf) {
			len++;
			for(int i = 0; i < MASKS.length; i++)
				if((c & MASKS[i]) != c)
					len++;
				else 
					break;
		}
		byte[] bytes = new byte[len];
		fromUTF32(utf, 0, bytes, 0, utf.length);
		return bytes;
	}
	
	public static void fromUTF64(IOutgoingStream os, long c) throws IOException
	{
		int cont = 0;
		for(int i = 0; i < MASKS.length; i++)
			if((c & MASKS[i]) != c)
				cont++;
			else 
				break;
		if(cont > 0) {
			os.writeByte((byte) (0x80 | (--cont << 4) | (c & 0x000F)));
			c >>>= 4;
			os.writeByte((byte) c);
			while(cont > 0) {
				c >>>= 8;
				os.writeByte((byte) c);
				cont--;
			}
		} else {
			os.writeByte((byte) (c & 0x7F));
			c >>>= 4;
		}
	}
	
	public static void fromUTF64(IOutgoingStream os, long[] utf, int start, int len) throws IOException
	{
		int cont;
		long c;
		while(len > 0)
		{
			c = utf[start++];
			cont = 0;
			for(int i = 0; i < MASKS.length; i++)
				if((c & MASKS[i]) != c)
					cont++;
				else 
					break;
			if(cont > 0) {
				os.writeByte((byte) (0x80 | (--cont << 4) | (c & 0x000F)));
				c >>>= 4;
				os.writeByte((byte) c);
				while(cont > 0) {
					c >>>= 8;
					os.writeByte((byte) c);
					cont--;
				}
			} else {
				os.writeByte((byte) (c & 0x7F));
				c >>>= 4;
			}
			len--;
		}
	}
	
	public static void fromUTF64(IOutgoingStream os, long[] utf) throws IOException
	{
		fromUTF64(os, utf, 0, utf.length);
	}
	
	public static void fromUTF64(long[] utf, int start0, byte[] bytes, int start1, int len)
	{
		long c;
		int cont;
		while(len > 0)
		{
			c = utf[start0++];
			cont = 0;
			for(int i = 0; i < MASKS.length; i++)
				if((c & MASKS[i]) != c)
					cont++;
				else 
					break;
			if(cont > 0) {
				bytes[start1++] = (byte) (0x80 | (--cont << 4) | (c & 0x000F));
				c >>>= 4;
				bytes[start1++] = (byte) c;
				while(cont > 0) {
					c >>>= 8;
					bytes[start1++] = (byte) c;
					cont--;
				}
			} else {
				bytes[start1++] = (byte) (c & 0x7F);
				c >>>= 4;
			}
			len--;
		}
	}
	
	public static void fromUTF64(long[] utf, int start0, byte[] bytes, int start1)
	{
		fromUTF64(utf, start0, bytes, start1, utf.length - start1);
	}
	
	public static void fromUTF64(long[] utf, byte[] bytes)
	{
		fromUTF64(utf, 0, bytes, 0, utf.length);
	}
	
	public static byte[] fromUTF64(long[] utf)
	{
		int len = 0;
		for(long c : utf) {
			len++;
			for(int i = 0; i < MASKS.length; i++)
				if((c & MASKS[i]) != c)
					len++;
				else 
					break;
		}
		byte[] bytes = new byte[len];
		fromUTF64(utf, 0, bytes, 0, utf.length);
		return bytes;
	}
	
	public static void toUTF16(IIncomingStream is, char[] chars, int start, int len) throws IOException
	{
		byte b, b1, n;
		char t;
		while(len > 0)
		{
			t = 0;
			b = is.readByte();
			if(b < 0) {
				n = (byte) (b & 0xF0);
				if((n & 0xFF) > 0x90) {
					int to = ((n & 0x70) >>> 4) + 1;
					while(to-- > 0)
						is.readByte();
					chars[start++] = 0xFFFD;
					len--;
					continue;
				} else if((n & 0xFF) == 0x80) {
					t = (char) (is.readByte() & 0xFF);
				} else {
					b1 = is.readByte();
					t = (char) (is.readByte() & 0xFF);
					if((t & 0x0F) != t) {
						chars[start++] = 0xFFFD;
						len--;
						continue;
					}
					t <<= 8;
					t |= (b1 & 0xFF);		
				}
				t <<= 4;
				t |= b & 0x0F;
				chars[start++] = t;
			} else
				chars[start++] = (char) b;
			len--;
		}
	}
	
	public static void toUTF16(IIncomingStream is, char[] chars) throws IOException
	{
		toUTF16(is, chars, 0, chars.length);
	}
	
	public static char toUTF16(IIncomingStream is) throws IOException
	{
		byte b, b1, n;
		char t;
		b = is.readByte();
		if(b < 0) {
			n = (byte) (b & 0xF0);
			if((n & 0xFF) > 0x90) {
				int to = ((n & 0x70) >>> 4) + 1;
				while(to-- > 0)
					is.readByte();
				return 0xFFFD;
			} else if((n & 0xFF) == 0x80) {
				t = (char) (is.readByte() & 0xFF);
			} else {
				b1 = is.readByte();
				t = (char) (is.readByte() & 0xFF);
				if((t & 0x0F) != t) 
					return 0xFFFD;
				t <<= 8;
				t |= (b1 & 0xFF);		
			}
			t <<= 4;
			t |= b & 0x0F;
			return t;
		} else
			return (char) b;
	}
	
	public static void toUTF16(byte[] ctfl, int start0, char[] utf, int start1, int len)
	{
		byte b, b1, n;
		char t;
		while(len > 0)
		{
			b = ctfl[start0++];
			if(b < 0) {
				n = (byte) (b & 0xF0);
				if((n & 0xFF) > 0x90) {
					utf[start1++] = 0xFFFD;
					start0 += ((n & 0x70) >>> 4) + 1;
					len--;
					continue;
				} else if((n & 0xFF) == 0x80) {
					t = (char) (ctfl[start0++] & 0xFF);
				} else {
					b1 = ctfl[start0++];
					t = (char) (ctfl[start0++] & 0xFF);
					if((t & 0x0F) != t) {
						utf[start1++] = 0xFFFD;
						len--;
						continue;
					}
					t <<= 8;
					t |= (b1 & 0xFF);		
				}
				t <<= 4;
				t |= b & 0x0F;
				utf[start1++] = t;
			} else
				utf[start1++] = (char) b;
			len--;
		}
	}
	
	public static void toUTF16(byte[] ctfl, int start0, char[] utf, int start1)
	{
		toUTF16(ctfl, start0, utf, start1, utf.length - start1);
	}
	
	public static void toUTF16(byte[] ctfl, char[] utf)
	{
		toUTF16(ctfl, 0, utf, 0, utf.length);
	}
	
	public static char[] toUTF16(byte[] ctfl)
	{
		int len = 0;
		byte b;
		for(int i = 0; i < ctfl.length;)
		{
			b = ctfl[i++];
			if(b < 0) 
				i += ((b & 0x70) >>> 4) + 1;
			len++;
		}
		char[] chars = new char[len];
		toUTF16(ctfl, 0, chars, 0, len);
		return chars;
	}
	
	public static void toUTF32(IIncomingStream is, int[] ints, int start, int len) throws IOException
	{
		byte[] buf = new byte[8];
		int n;
		byte b;
		while(len > 0)
		{
			b = is.readByte();
			if(b < 0) {
				n = 3 - ((b & 0x70) >>> 4);
				for(int i = 3; i >= n; i--)
					buf[i] = is.readByte();
				if(n < 0) {
					ints[start++] = 0xFFFD;
					len--;
					break;
				}			
				if(n == 0 && (buf[0] & 0x0F) != buf[0]) {
					ints[start++] = 0xFFFD;
					len--;
					continue;
				}
				int l = 0;
				for(int i = n; i < 3; i++) {
					l |= buf[i] & 0xFF;
					l <<= 8;
				}
				l |= buf[3] & 0xFF;
				l <<= 4;
				l |= b & 0x0F;
				ints[start++] = l;
			} else
				ints[start++] = b & 0x7F;
			len--;
		}
	}
	
	public static void toUTF32(IIncomingStream is, int[] ints) throws IOException
	{
		toUTF32(is, ints, 0, ints.length);
	}
	
	public static int toUTF32(IIncomingStream is) throws IOException
	{
		byte b = is.readByte();
		if(b < 0) {
			byte[] buf = new byte[8];
			int n = 3 - ((b & 0x70) >>> 4);
			for(int i = 3; i >= n; i--)
				buf[i] = is.readByte();
			if(n < 0) 
				return 0xFFFD;
			int l = 0;
			if(n == 0 && (buf[0] & 0x0F) != buf[0])
				return 0xFFFD;
			for(int i = n; i < 3; i++) {
				l |= buf[i] & 0xFF;
				l <<= 8;
			}
			l |= buf[7] & 0xFF;
			l <<= 4;
			l |= b & 0x0F;
			return l;
		} else
			return b & 0x7F;
	}
	
	public static void toUTF32(byte[] ctfl, int start0, int[] utf, int start1, int len)
	{
		byte n, b;
		int t, i;
		byte[] buf = new byte[4];
		while(len > 0)
		{
			b = ctfl[start0++];
			if(b < 0) {
				n = (byte) (3 - ((b & 0x70) >>> 4));
				if(n < 0) {
					utf[start1++] = 0xFFFD;
					start0 += ((b & 0x70) >>> 4);
					len--;
					break;
				}
				for(i = 3; i >= n; i--)
					buf[i] = ctfl[start0++];
				if(n == 0 && (buf[0] & 0x0F) != buf[0]) {
					utf[start1++] = 0xFFFD;
					len--;
					continue;
				}
				t = 0;
				for(i = n; i < 3; i++) {
					t |= buf[i] & 0xFF;
					t <<= 8;
				}
				t |= buf[3] & 0xFF;
				t <<= 4;
				t |= b & 0x0F;
				utf[start1++] = t;
			} else
				utf[start1++] = (int) b;
			len--;
		}
	}
	
	public static void toUTF32(byte[] ctfl, int start0, int[] utf, int start1)
	{
		toUTF32(ctfl, start0, utf, start1, utf.length - start1);
	}
	
	public static void toUTF32(byte[] ctfl, int[] utf)
	{
		toUTF32(ctfl, 0, utf, 0, utf.length);
	}
	
	public static int[] toUTF32(byte[] ctfl)
	{
		int len = 0;
		byte b;
		for(int i = 0; i < ctfl.length;)
		{
			b = ctfl[i++];
			if(b < 0) 
				i += ((b & 0x70) >>> 4) + 1;
			len++;
		}
		int[] ints = new int[len];
		toUTF32(ctfl, 0, ints, 0, len);
		return ints;
	}
	
	public static void toUTF64(IIncomingStream is, long[] longs, int start, int len) throws IOException
	{
		byte[] buf = new byte[8];
		int n;
		byte b;
		while(len > 0)
		{
			b = is.readByte();
			if(b < 0) {
				n = 7 - ((b & 0x70) >>> 4);
				for(int i = 7; i >= n; i--)
					buf[i] = is.readByte();
				long l = 0;
				for(int i = n; i < 7; i++) {
					l |= buf[i] & 0xFF;
					l <<= 8;
				}
				l |= buf[7] & 0xFF;
				l <<= 4;
				l |= b & 0x0F;
				longs[start++] = l;
			} else
				longs[start++] = b & 0x7F;
			len--;
		}
	}
	
	public static void toUTF64(IIncomingStream is, long[] longs) throws IOException
	{
		toUTF64(is, longs, 0, longs.length);
	}
	
	public static long toUTF64(IIncomingStream is) throws IOException
	{
		byte b = is.readByte();
		if(b < 0) {
			byte[] buf = new byte[8];
			int n = 7 - ((b & 0x70) >>> 4);
			for(int i = 7; i >= n; i--)
				buf[i] = is.readByte();
			long l = 0;
			for(int i = n; i < 7; i++) {
				l |= buf[i] & 0xFF;
				l <<= 8;
			}
			l |= buf[7] & 0xFF;
			l <<= 4;
			l |= b & 0x0F;
			return l;
		} else
			return b & 0x7F;
	}
	
	public static void toUTF64(byte[] ctfl, int start0, long[] utf, int start1, int len)
	{
		byte n, b;
		int i;
		long l;
		byte[] buf = new byte[8];
		while(len > 0)
		{
			b = ctfl[start0++];
			if(b < 0) {
				n = (byte) (7 - ((b & 0x70) >>> 4));
				for(i = 7; i >= n; i--)
					buf[i] = ctfl[start0++];
				l = 0;
				for(i = n; i < 7; i++) {
					l |= buf[i] & 0xFF;
					l <<= 8;
				}
				l |= buf[7] & 0xFF;
				l <<= 4;
				l |= b & 0x0F;
				utf[start1++] = l;
			} else
				utf[start1++] = (int) b;
			len--;
		}
	}
	
	public static void toUTF64(byte[] ctfl, int start0, long[] utf, int start1)
	{
		toUTF64(ctfl, start0, utf, start1, utf.length - start1);
	}
	
	public static void toUTF64(byte[] ctfl, long[] utf)
	{
		toUTF64(ctfl, 0, utf, 0, utf.length);
	}
	
	public static long[] toUTF64(byte[] ctfl)
	{
		int len = 0;
		byte b;
		for(int i = 0; i < ctfl.length;)
		{
			b = ctfl[i++];
			if(b < 0) 
				i += ((b & 0x70) >>> 4) + 1;
			len++;
		}
		long[] ints = new long[len];
		toUTF64(ctfl, 0, ints, 0, len);
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

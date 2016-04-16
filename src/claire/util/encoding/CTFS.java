package claire.util.encoding;

import java.io.IOException;

import claire.util.standards.io.IDecoder;
import claire.util.standards.io.IEncoder;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

/**
 * Claire text format - Small
 * 
 * @author Claire
 */
public final class CTFS {
	
	private static final long[] MASKS = new long[]
		{
			0x7F,
			0x3FFF,
			0x1FFFFF,
			0xFFFFFFF,
			0x7FFFFFFFFL,
			0x3FFFFFFFFFFL,
			0x1FFFFFFFFFFFFL,
			0xFFFFFFFFFFFFFFL,
			0x7FFFFFFFFFFFFFFFL,
		};
	
	public static void fromUTF16(IOutgoingStream os, char l) throws IOException
	{
		byte b1, b2;
		if(l >= 128)
		{
			if(l > 0x3FFF) {
				b1 = (byte) (l & 0x007F);
				l >>>= 7;
				b2 = (byte) (0x80 | (l & 0x007F));
				l >>>= 7;
				os.writeByte((byte) (0x80 | (l & 0x007F)));
				os.writeByte(b2);
				os.writeByte(b1);
			} else  {
				b1 = (byte) (l & 0x007F);
				l >>>= 7;
				os.writeByte((byte) (0x80 | (l & 0x007F)));
				os.writeByte(b1);
			}	
		}
		else
			os.writeByte((byte) l);
	}
	
	public static void fromUTF16(IOutgoingStream os, char[] chars, int start, int len) throws IOException
	{
		byte b1, b2;
		char l;
		while(len > 0)
		{
			l = chars[start++];
			if(l >= 128)
			{
				if(l > 0x3FFF) {
					b1 = (byte) (l & 0x007F);
					l >>>= 7;
					b2 = (byte) (0x80 | (l & 0x007F));
					l >>>= 7;
					os.writeByte((byte) (0x80 | (l & 0x007F)));
					os.writeByte(b2);
					os.writeByte(b1);
				} else  {
					b1 = (byte) (l & 0x007F);
					l >>>= 7;
					os.writeByte((byte) (0x80 | (l & 0x007F)));
					os.writeByte(b1);
				}	
			}
			else
				os.writeByte((byte) l);
			len--;
		}
	}
	
	public static void fromUTF16(IOutgoingStream os, char[] chars) throws IOException
	{
		fromUTF16(os, chars, 0, chars.length);
	}
	
	public static void fromUTF16(char t, byte[] bytes, int start1)
	{
		byte b1, b2;
		if(t >= 128)
		{
			if(t > 0x3FFF) {
				b1 = (byte) (t & 0x007F);
				t >>>= 7;
				b2 = (byte) (0x80 | (t & 0x007F));
				t >>>= 7;
				bytes[start1++] = (byte) (0x80 | (t & 0x007F));
				bytes[start1++] = b2;
				bytes[start1++] = b1;
			} else  {
				b1 = (byte) (t & 0x007F);
				t >>>= 7;
				bytes[start1++] = (byte) (0x80 | (t & 0x007F));
				bytes[start1++] = b1;
			}	
		} else
			bytes[start1++] = (byte) t;
	}
	
	public static void fromUTF16(char[] utf, int start0, byte[] bytes, int start1, int len)
	{
		byte b1, b2;
		char t;
		while(len > 0)
		{
			t = utf[start0++];
			if(t >= 128)
			{
				if(t > 0x3FFF) {
					b1 = (byte) (t & 0x007F);
					t >>>= 7;
					b2 = (byte) (0x80 | (t & 0x007F));
					t >>>= 7;
					bytes[start1++] = (byte) (0x80 | (t & 0x007F));
					bytes[start1++] = b2;
					bytes[start1++] = b1;
				} else  {
					b1 = (byte) (t & 0x007F);
					t >>>= 7;
					bytes[start1++] = (byte) (0x80 | (t & 0x007F));
					bytes[start1++] = b1;
				}	
			} else
				bytes[start1++] = (byte) t;
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
	
	public static byte[] fromUTF16(char[] utf, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0)
		{
			char c = utf[start++];
			if(c > 0x7F)
				if(c > 0x3FFF)
					len += 3;
				else
					len += 2;
			else
				len++;
		}
		byte[] bytes = new byte[len];
		fromUTF16(utf, 0, bytes, 0, utf.length);
		return bytes;
	}
	
	public static byte[] fromUTF16(char[] utf)
	{
		int len = 0;
		for(char c : utf)
			if(c > 0x7F)
				if(c > 0x3FFF)
					len += 3;
				else
					len += 2;
			else
				len++;
		byte[] bytes = new byte[len];
		fromUTF16(utf, 0, bytes, 0, utf.length);
		return bytes;
	}
	
	public static final int bytesUTF16(char c)
	{
		if(c > 0x7F)
			if(c > 0x3FFF)
				return 3;
			else
				return 2;
		else
			return 1;
	}
	
	public static final int bytesUTF16(char[] utf, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0)
		{
			char c = utf[start++];
			if(c > 0x7F)
				if(c > 0x3FFF)
					len += 3;
				else
					len += 2;
			else
				len++;
		}
		return len;
	}
	
	public static final int bytesUTF16(char[] utf)
	{
		return bytesUTF16(utf, 0, utf.length);
	}
	
	public static void fromString(String utf, int start0, byte[] bytes, int start1, int len)
	{
		byte b1, b2;
		char t;
		while(len > 0)
		{
			t = utf.charAt(start0++);
			if(t >= 128)
			{
				if(t > 0x3FFF) {
					b1 = (byte) (t & 0x007F);
					t >>>= 7;
					b2 = (byte) (0x80 | (t & 0x007F));
					t >>>= 7;
					bytes[start1++] = (byte) (0x80 | (t & 0x007F));
					bytes[start1++] = b2;
					bytes[start1++] = b1;
				} else  {
					b1 = (byte) (t & 0x007F);
					t >>>= 7;
					bytes[start1++] = (byte) (0x80 | (t & 0x007F));
					bytes[start1++] = b1;
				}	
			} else
				bytes[start1++] = (byte) t;
			len--;
		}
	}
	
	public static void fromString(String utf, int start0, byte[] bytes, int start1)
	{
		fromString(utf, start0, bytes, start1, utf.length() - start1);
	}
	
	public static void fromString(String utf, byte[] bytes)
	{
		fromString(utf, 0, bytes, 0, utf.length());
	}
	
	public static void fromUTF32(IOutgoingStream os, int l) throws IOException
	{
		int cont, co;
		byte[] temp = new byte[5];
		if(l >= 128)
		{
			cont = 1;
			for(int i = 1; i < MASKS.length; i++)
				if((l & MASKS[i]) != l)
					cont++;
				else 
					break;
			co = cont;
			temp[co--] = (byte) (l & 0x7F);
			for(; co >= 0;)
			{
				l >>>= 7;
				temp[co--] = (byte) (0x80 | (l & 0x7F));
			}
			for(int i = 0; i <= cont; i++)
				os.writeByte(temp[i]);
		}
		else
			os.writeByte((byte) l);
	}
	
	public static void fromUTF32(IOutgoingStream os, int[] ints, int start, int len) throws IOException
	{
		int l;
		int cont, co;
		byte[] temp = new byte[5];
		while(len > 0)
		{
			l = ints[start++];
			if(l >= 128)
			{
				cont = 1;
				for(int i = 1; i < MASKS.length; i++)
					if((l & MASKS[i]) != l)
						cont++;
					else 
						break;
				co = cont;
				temp[co--] = (byte) (l & 0x7F);
				for(; co >= 0;)
				{
					l >>>= 7;
					temp[co--] = (byte) (0x80 | (l & 0x7F));
				}
				for(int i = 0; i <= cont; i++)
					os.writeByte(temp[i]);
			}
			else
				os.writeByte((byte) l);
			len--;
		}
	}
	
	public static void fromUTF32(IOutgoingStream os, int[] ints) throws IOException
	{
		fromUTF32(os, ints, 0, ints.length);
	}
	
	public static void fromUTF32(int[] utf, int start0, byte[] bytes, int start1, int len)
	{
		int t, cont, co;
		byte[] temp = new byte[5];
		while(len > 0)
		{
			t = utf[start0++];
			if(t >= 128)
			{
				cont = 1;
				for(int i = 1; i < MASKS.length; i++)
					if((t & MASKS[i]) != t)
						cont++;
					else 
						break;
				co = cont;
				temp[co--] = (byte) (t & 0x7F);
				for(; co >= 0;)
				{
					t >>>= 7;
					temp[co--] = (byte) (0x80 | (t & 0x7F));
				}
				for(int i = 0; i <= cont; i++)
					bytes[start1++] = temp[i];
			} else
				bytes[start1++] = (byte) t;
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
	
	public static byte[] fromUTF32(int[] utf, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0)
		{
			int c = utf[start++];
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
	
	public static final int bytesUTF32(int c)
	{
		int len = 1;
		for(int i = 0; i < MASKS.length; i++)
			if((c & MASKS[i]) != c)
				len++;
			else 
				return len;
		return len;
	}
	
	public static final int bytesUTF32(int[] chars, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0)
		{
			int c = chars[start++];
			for(int i = 0; i < MASKS.length; i++)
				if((c & MASKS[i]) != c)
					len++;
				else 
					break;
		}
		return len;
	}
	
	public static final int bytesUTF32(int[] chars)
	{
		return bytesUTF32(chars, 0, chars.length);
	}
	
	public static void fromUTF64(IOutgoingStream os, long l) throws IOException
	{
		int cont, co;
		byte[] temp = new byte[9];
		if(l >= 128)
		{
			cont = 1;
			for(int i = 1; i < MASKS.length; i++)
				if((l & MASKS[i]) != l)
					cont++;
				else 
					break;
			co = cont;
			temp[co--] = (byte) (l & 0x7F);
			for(; co >= 0;)
			{
				l >>>= 7;
				temp[co--] = (byte) (0x80 | (l & 0x7F));
			}
			for(int i = 0; i <= cont; i++)
				os.writeByte(temp[i]);
		}
		else
			os.writeByte((byte) l);
	}
	
	public static void fromUTF64(IOutgoingStream os, long[] longs, int start, int len) throws IOException
	{
		long l;
		int cont, co;
		byte[] temp = new byte[9];
		while(len > 0)
		{
			l = longs[start++];
			if(l >= 128)
			{
				cont = 1;
				for(int i = 1; i < MASKS.length; i++)
					if((l & MASKS[i]) != l)
						cont++;
					else 
						break;
				co = cont;
				temp[co--] = (byte) (l & 0x7F);
				for(; co >= 0;)
				{
					l >>>= 7;
					temp[co--] = (byte) (0x80 | (l & 0x7F));
				}
				for(int i = 0; i <= cont; i++)
					os.writeByte(temp[i]);
			}
			else
				os.writeByte((byte) l);
			len--;
		}
	}
	
	public static void fromUTF64(IOutgoingStream os, long[] longs) throws IOException
	{
		fromUTF64(os, longs, 0, longs.length);
	}
	
	public static void fromUTF64(long[] utf, int start0, byte[] bytes, int start1, int len)
	{
		long t;
		int cont, co;
		byte[] temp = new byte[9];
		while(len > 0)
		{
			t = utf[start0++];
			if(t >= 128)
			{
				cont = 1;
				for(int i = 1; i < MASKS.length; i++)
					if((t & MASKS[i]) != t)
						cont++;
					else 
						break;
				co = cont;
				temp[co--] = (byte) (t & 0x7F);
				for(; co >= 0;)
				{
					t >>>= 7;
					temp[co--] = (byte) (0x80 | (t & 0x7F));
				}
				for(int i = 0; i <= cont; i++)
					bytes[start1++] = temp[i];
			} else
				bytes[start1++] = (byte) t;
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
	
	public static byte[] fromUTF64(long[] utf, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0)
		{
			long c = utf[start++];
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
	
	public static final int bytesUTF64(long c)
	{
		int len = 1;
		for(int i = 0; i < MASKS.length; i++)
			if((c & MASKS[i]) != c)
				len++;
			else 
				return len;
		return len;
	}
	
	public static final int bytesUTF64(long[] chars, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0)
		{
			long c = chars[start++];
			for(int i = 0; i < MASKS.length; i++)
				if((c & MASKS[i]) != c)
					len++;
				else 
					break;
		}
		return len;
	}
	
	public static final int bytesUTF64(long[] chars)
	{
		return bytesUTF64(chars, 0, chars.length);
	}
	
	public static long toUTF64(IIncomingStream is) throws IOException
	{
		long c = 0;
		byte b = is.readByte();
		while(b < 0)
		{
			c |= b & 0x7F;
			c <<= 7;
			b = is.readByte();
		}
		c |= b & 0x7F;
		return c;
	}
	
	public static void toUTF64(IIncomingStream is, long[] longs, int start, int len) throws IOException
	{
		while(len > 0)
		{
			long c = 0;
			byte b = is.readByte();
			while(b < 0)
			{
				c |= b & 0x7F;
				c <<= 7;
				b = is.readByte();
			}
			c |= b & 0x7F;
			longs[start++] = c;
			len--;
		}
	}
	
	public static void toUTF64(IIncomingStream is, long[] longs) throws IOException
	{
		toUTF64(is, longs, 0, longs.length);
	}
	
	public static void toUTF64(byte[] ctfl, int start0, long[] utf, int start1, int len)
	{
		byte b;
		long t;
		while(len > 0)
		{
			b = ctfl[start0++];
			t = 0x00;
			while(b < 0) {
				t |= b & 0x7F;
				t <<= 7;
				b = ctfl[start0++];
			}
			t |= b & 0x7F;
			utf[start1++] = t;
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
	
	public static long[] toUTF64(byte[] ctfl, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0)
			if(ctfl[start++] >= 0)
				len++;
		long[] longs = new long[len];
		toUTF64(ctfl, 0, longs, 0, len);
		return longs;
	}
	
	public static long[] toUTF64(byte[] ctfl)
	{
		int len = 0;
		for(byte b : ctfl)
			if(b >= 0)
				len++;
		long[] longs = new long[len];
		toUTF64(ctfl, 0, longs, 0, len);
		return longs;
	}
	
	public static int toUTF32(IIncomingStream is) throws IOException
	{
		int ctr = 1;
		int c = 0;
		byte b = is.readByte();
		byte bb = b;
		while(b < 0)
		{
			c |= b & 0x7F;
			c <<= 7;
			b = is.readByte();
			ctr++;
		}
		if((ctr == 5 && (bb & 0x8F) != (bb & 0xFF)) || ctr > 5) 
			return 0xFFFD;
		c |= b & 0x7F;
		return c;
	}
	
	public static void toUTF32(IIncomingStream is, int[] ints, int start, int len) throws IOException
	{
		int c;
		byte b, bb;
		int ctr;
		while(len > 0)
		{
			c = 0;
			bb = b = is.readByte();
			ctr = 1;
			while(b < 0)
			{
				c |= b & 0x7F;
				c <<= 7;
				b = is.readByte();
				ctr++;
			}
			if((ctr == 5 && (bb & 0x8F) != (bb & 0xFF)) || ctr > 5) {
				ints[start++] = 0xFFFD;
				len--;
				continue;
			} 
			c |= b & 0x7F;
			ints[start++] = c;
			len--;
		}
	}
	
	public static void toUTF32(IIncomingStream is, int[] ints) throws IOException
	{
		toUTF32(is, ints, 0, ints.length);
	}
	
	public static void toUTF32(byte[] ctfl, int start0, int[] utf, int start1, int len)
	{
		byte b;
		int t;
		int ctr;
		while(len > 0)
		{
			b = ctfl[start0++];
			t = 0x00;
			ctr = 1;
			while(b < 0) {
				t |= b & 0x7F;
				t <<= 7;
				b = ctfl[start0++];
				ctr++;
			}
			if((ctr == 5 && (ctfl[start0 - ctr] & 0x8F) != (ctfl[start0 - ctr] & 0xFF)) || ctr > 5) {
				utf[start1++] = 0xFFFD;
				len--;
				continue;
			} 	
			t |= b & 0x7F;
			utf[start1++] = t;
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
	
	public static int[] toUTF32(byte[] ctfl, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0)
			if(ctfl[start++] >= 0)
				len++;
		int[] ints = new int[len];
		toUTF32(ctfl, 0, ints, 0, len);
		return ints;
	}
	
	public static int[] toUTF32(byte[] ctfl)
	{
		int len = 0;
		for(byte b : ctfl)
			if(b >= 0)
				len++;
		int[] ints = new int[len];
		toUTF32(ctfl, 0, ints, 0, len);
		return ints;
	}
	
	public static char toUTF16(IIncomingStream is) throws IOException
	{
		int ctr = 1;
		char c = 0;
		byte b = is.readByte();
		byte bb = b;
		while(b < 0)
		{
			c |= b & 0x7F;
			c <<= 7;
			b = is.readByte();
			ctr++;
		}
		if((ctr == 3 && (bb & 0x83) != (bb & 0xFF)) || ctr > 3) 
			return 0xFFFD;
		c |= b & 0x7F;
		return c;
	}
	
	public static void toUTF16(IIncomingStream is, char[] chars, int start, int len) throws IOException
	{
		char c;
		byte b, bb;
		int ctr;
		while(len > 0)
		{
			c = 0;
			bb = b = is.readByte();
			ctr = 1;
			while(b < 0)
			{
				c |= b & 0x7F;
				c <<= 7;
				b = is.readByte();
				ctr++;
			}
			if((ctr == 3 && (bb & 0x83) != (bb & 0xFF)) || ctr > 3) {
				chars[start++] = 0xFFFD;
				len--;
				continue;
			} 
			c |= b & 0x7F;
			chars[start++] = c;
			len--;
		}
	}
	
	public static void toUTF16(IIncomingStream is, char[] chars) throws IOException
	{
		toUTF16(is, chars, 0, chars.length);
	}
	
	public static void toUTF16(byte[] ctfl, int start0, char[] utf, int start1, int len)
	{
		byte b;
		char t;
		int ctr;
		while(len > 0)
		{
			b = ctfl[start0++];
			t = 0x00;
			ctr = 1;
			while(b < 0) {
				t |= b & 0x7F;
				t <<= 7;
				b = ctfl[start0++];
				ctr++;
			}
			if((ctr == 3 && (ctfl[start0 - ctr] & 0x83) != (ctfl[start0 - ctr] & 0xFF)) || ctr > 3) {
				utf[start1++] = 0xFFFD;
				len--;
				continue;
			} 				
			t |= b & 0x7F;
			utf[start1++] = t;
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
	
	public static char[] toUTF16(byte[] ctfl, int start, int slen)
	{
		int len = 0;
		while(slen-- > 0)
			if(ctfl[start++] >= 0)
				len++;
		char[] chars = new char[len];
		toUTF16(ctfl, 0, chars, 0, len);
		return chars;
	}
	
	public static char[] toUTF16(byte[] ctfl)
	{
		int len = 0;
		for(byte b : ctfl)
			if(b >= 0)
				len++;
		char[] chars = new char[len];
		toUTF16(ctfl, 0, chars, 0, len);
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

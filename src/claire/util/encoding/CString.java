package claire.util.encoding;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.CRC16;
import claire.util.crypto.hash.primitive.CRC32;
import claire.util.crypto.hash.primitive.CRC8;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayBuilder;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.CObject;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IIterable;
import claire.util.standards.IIterator;
import claire.util.standards.IPersistable;
import claire.util.standards.ISoftID;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IOutgoingStream;

public class CString 
	   implements CObject<CString>, 
	   			  IIterable<Character>,
	   			  ISoftID {
	
	protected char[] chars;
	
	public CString(final String s)
	{
		chars = s.toCharArray();
	}
	
	public CString(char c)
	{
		this.chars = new char[] { c };
	}
	
	public CString(char[] c, int start, int len)
	{
		chars = new char[len];
		System.arraycopy(c, 0, chars, start, len);
	}
	
	public CString(final char ... c)
	{
		this.chars = c;
	}
	
	public CString(byte[] bytes)
	{
		this(bytes, "UTF-16");
	}
	
	public CString(byte[] bytes, String encoding)
	{
		switch(encoding)
		{
			case "CTF-S":
				this.chars = CTFS.toUTF16(bytes);
				break;
			case "CTF-L":
				this.chars = CTFL.toUTF16(bytes);
				break;
			case "UTF-8":
				this.chars = UTF8.toUTF16(bytes);
				break;
			case "UTF-16":
				this.chars = Bits.bytesToChars(bytes);
				break;
			case "ASCII":
				chars = new char[bytes.length];
				for(int i = 0; i < bytes.length; i++)
					chars[i] = (char) bytes[i];
				break;
			default:
				throw new java.lang.NullPointerException("Unsupported Encoding");
		}
	}
	
	public CString(byte i)
	{
		this.chars = Base10.fromByte(i);
	}
	
	public CString(short i)
	{
		this.chars = Base10.fromShort(i);
	}
	
	public CString(int i)
	{
		this.chars = Base10.fromInt(i);
	}
	
	public CString(long i)
	{
		this.chars = Base10.fromLong(i);
	}
	
	public CString(boolean b)
	{
		if(b)
			this.chars = new char[] { 't', 'r', 'u', 'e' };
		else
			this.chars = new char[] { 'f', 'a', 'l', 's', 'e' };
	}
	
	public CString(CString string)
	{
		this.chars = ArrayUtil.copy(string.array());
	}
	
	public CString(boolean[] bools)
	{
		this.chars = new char[bools.length];
		for(int i = 0; i < bools.length; i++)
			if(bools[i])
				chars[i] = '1';
			else
				chars[i] = '0';
	}
	
	public void add(final char c)
	{
		chars = ArrayUtil.upsize(chars, 1);
		chars[chars.length - 1] = c;
	}
	
	public void add(final String s)
	{
		char[] n = s.toCharArray();
		this.concat(n);
	}
	
	public void add(final CString s)
	{
		char[] n = s.toCharArray();
		this.concat(n);
	}
	
	public int length()
	{
		return this.chars.length;
	}
	
	public void concat(final char[] c)
	{
		final char[] chars = this.chars;
		final char[] n = new char[chars.length + c.length];
		System.arraycopy(chars, 0, n, 0, chars.length);
		System.arraycopy(c, 0, n, chars.length, c.length);
		this.chars = n;
	}
	
	public void prepend(final char[] c)
	{
		final char[] chars = this.chars;
		final char[] n = new char[chars.length + c.length];
		System.arraycopy(chars, 0, n, c.length, chars.length);
		System.arraycopy(c, 0, n, 0, c.length);
		this.chars = n;
	}
	
	public char[] toCharArray()
	{
		final char[] chars = this.chars;
		final char[] n = new char[chars.length];
		System.arraycopy(chars, 0, n, 0, chars.length);
		return n;
	}
	
	public char[] array()
	{
		return this.chars;
	}
	
	public char charAt(int index)
	{
		return chars[index];
	}
	
	public String toString()
	{
		return new String(this.chars);
	}
	
	/**
	 * Note: any parts of the string that match the character will be discarded. 
	 * @param c
	 * @return
	 */
	public CString[] split(char c)
	{
		ArrayBuilder<CString> builder = new ArrayBuilder<CString>(CString.class, 4);
		int i, last = 0;
		for(i = 0; i < chars.length; i++)
		{
			if(chars[i] == c) {
				builder.addElement(new CString(ArrayUtil.subArr(chars, last, i)));
				last = i + 1;
			}
		}
		if(last != chars.length)
			builder.addElement(new CString(ArrayUtil.subArr(chars, last, i)));
		return builder.build();
	}
	
	/**
	 * Note: any parts of the string that match the string will be discarded. 
	 * @param c
	 * @return
	 */
	public CString[] split(CString s)
	{
		if(s.length() == 1)
			return this.split(s.array()[0]);
		ArrayBuilder<CString> builder = new ArrayBuilder<CString>(CString.class, 4);
		int i, last = 0;
		int len = s.length();
		for(i = 0; i < chars.length;)
		{
			if(this.substrEquals(s, i, i + len - 1)) {
				builder.addElement(new CString(ArrayUtil.subArr(chars, last, i)));
				last = i + len;
				i += len;
			} else
				i++;
		}
		if(last < chars.length)
			builder.addElement(new CString(ArrayUtil.subArr(chars, last, i)));
		return builder.build();
	}
	
	public CString removeBefore(int pos)
	{
		char[] n = new char[chars.length - pos];
		System.arraycopy(chars, pos, n, 0, n.length);
		return new CString(n);
	}
	
	public CString removeAfter(int pos)
	{
		char[] n = new char[pos];
		System.arraycopy(chars, 0, n, 0, pos);
		return new CString(n);
	}
	
	public CString removeMiddle(int pos, int len)
	{
		char[] n = new char[chars.length - len];
		System.arraycopy(chars, 0, n, 0, pos - 1);
		System.arraycopy(chars, pos + len, n, pos - 1, chars.length - (pos + len));
		return new CString(n);
	}
	
	public CString slice(int pos, int len)
	{
		char[] n = new char[len];
		System.arraycopy(chars, pos, n, 0, len);
		return new CString(n);
	}
	
	public boolean contains(char c)
	{
		for(char ch : chars)
			if(c == ch)
				return true;
		return false;
	}
	
	public boolean contains(CString s)
	{
		char[] chars = s.array();
		if(chars.length > this.chars.length)
			return false;
		for(int i = 0; i <= this.chars.length - chars.length; i++)
		{
			boolean found = true;
			for(int j = 0; j < chars.length; j++)
				if(!(this.chars[i + j] == chars[j]))
				{
					found = false;
					break;
				}
			if(found) 
				return true;
		}
		return false;
	}
	
	public void replaceWith(CString find, CString replace)
	{
		replaceWith(find.chars, replace.chars);
	}
	
	public void replaceWith(char[] farr, char[] rarr)
	{
		//System.out.println("Replacing " + new String(farr) + " with " + new String(rarr) + " in " + this.toString());
		if(farr.length > this.chars.length)
			return;
		int diff = rarr.length - farr.length;
		int p = 0;
		int last = -1;
		for(int i = 0; i < this.length(); i++) {
			if(chars[i] == farr[p])
				p++;
			else {
				p = 0;
				last = i;
			}
			if(p == farr.length)
			{
				char[] newval = new char[chars.length + diff];
				System.arraycopy(chars, 0, newval, 0, last + 1);
				System.arraycopy(rarr, 0, newval, last + 1, rarr.length);
				System.arraycopy(chars, last + 1 + farr.length, newval, last + 1 + rarr.length , chars.length - last - 1 - farr.length);
				chars = newval;				
				p = 0;
				i--;
			}
			
		}
	}
	
	public int next(int i, char c)
	{
		while(++i < chars.length)
			if(chars[i] == c)
				return i;
		return -1;
	}
	
	public void replaceWith(char c, char r)
	{
		for(int i = 0; i < chars.length; i++)
			if(chars[i] == c)
				chars[i] = r;
	}
	
	public int count(char c)
	{
		int counter = 0;
		for(char ch : chars)
			if(ch == c)
				counter++;
		return counter;
	}
	
	public byte toByte()
	{
		return Base10.stringExcepByte(chars);
	}
	
	public short toShort()
	{
		return Base10.stringExcepShort(chars);
	}
	
	public int toInt()
	{
		return Base10.stringExcepInt(chars);
	}
	
	public long toLong()
	{
		return Base10.stringExcepLong(chars);
	}

	public CString createDeepClone()
	{
		return new CString(this.toCharArray());
	}
	
	public CString reverse()
	{
		EncodingUtil.REVERSE(chars);
		return this;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.CSTRING;
	}

	public boolean sameAs(CString s)
	{
		return this.equals(s);
	}
	
	public boolean equals(String s)
	{
		final char[] o = s.toCharArray();
		if(o.length != this.chars.length)
			return false;
		else
			for(int i = 0; i < this.chars.length; i++)
				if(this.chars[i] != o[i])
					return false;
		return true;
	}
	
	public boolean equals(CString s)
	{
		final char[] o = s.array();
		if(o.length != this.chars.length)
			return false;
		else
			for(int i = 0; i < this.chars.length; i++)
				if(this.chars[i] != o[i])
					return false;
		return true;
	}
	
	public boolean substrEquals(CString s, int st, int en)
	{
		final char[] o = s.array();
		int length = en - st + 1;
		if(s.length() != length)
			return false;
		else 
			for(int i = st; i < en; i++)
				if(chars[i] != o[i - st])
					return false;
		return true;
	}
	
	public void erase()
	{
		Arrays.fill(chars, (char) 0);
	}
	
	public void print()
	{
		System.out.println(chars);
	}
	
	public final byte[] toBytes()
	{
		return this.toBytesUTF16();
	}
	
	public byte[] toBytes(String encoding)
	{
		switch(encoding)
		{
			case "CTF-S":
				return this.toBytesCTFS();
			case "CTF-L":
				return this.toBytesCTFL();
			case "ASCII":
				return this.toBytesASCII();
			case "UTF-8":
				return this.toBytesUTF8();
			case "UTF-16":
				return this.toBytesUTF16();
			case "UTF-32":
				return this.toBytesUTF32();
			default:
				throw new java.lang.NullPointerException("Unsupported encoding: " + encoding);
		}
	}
	
	public byte[] toBytesCTFS()
	{
		return CTFS.fromUTF16(chars);
	}
	
	public void toBytesCTFS(byte[] bytes)
	{
		CTFS.fromUTF16(chars, bytes);
	}
	
	public void toBytesCTFS(byte[] bytes, int start)
	{
		CTFS.fromUTF16(chars, 0, bytes, start);
	}
	
	public byte[] toBytesCTFL()
	{
		return CTFL.fromUTF16(chars);
	}
	
	public void toBytesCTFL(byte[] bytes)
	{
		CTFL.fromUTF16(chars, bytes);
	}
	
	public void toBytesCTFL(byte[] bytes, int start)
	{
		CTFL.fromUTF16(chars, 0, bytes, start);
	}
	
	public byte[] toBytesASCII()
	{
		byte[] bytes = new byte[chars.length];
		int len = chars.length,
			    i = 0;
		while(len-- > 0)
			if(chars[i] < 128)
				bytes[i] = (byte) chars[i++];
			else
				bytes[i++] = (byte) '?';
		return bytes;
	}
	
	public void toBytesASCII(byte[] bytes)
	{
		int len = chars.length,
		    i = 0;
		while(len-- > 0)
			if(chars[i] < 128)
				bytes[i] = (byte) chars[i++];
			else
				bytes[i++] = (byte) '?';
	}
	
	public void toBytesASCII(byte[] bytes, int start)
	{
		int len = chars.length,
			    i = 0;
			while(len-- > 0)
				if(chars[i] < 128)
					bytes[start++] = (byte) chars[i++];
				else {
					bytes[start++] = (byte) '?';
					i++;
				}
	}
	
	public byte[] toBytesUTF16()
	{
		return Bits.charsToBytes(chars);
	}
	
	public void toBytesUTF16(byte[] bytes)
	{
		Bits.charsToBytes(chars, bytes);
	}
	
	public void toBytesUTF16(byte[] bytes, int start)
	{
		Bits.charsToBytes(chars, 0, bytes, start);
	}
	
	public byte[] toBytesUTF8()
	{
		return UTF8.fromUTF16(chars);
	}
	
	public void toBytesUTF8(byte[] bytes)
	{
		UTF8.fromUTF16(chars, bytes);
	}
	
	public void toBytesUTF8(byte[] bytes, int start)
	{
		UTF8.fromUTF16(chars, 0, bytes, start);
	}
	
	public byte[] toBytesUTF32()
	{
		byte[] bytes = new byte[chars.length << 1];
		this.toBytesUTF32(bytes);
		return bytes;
	}
	
	public void toBytesUTF32(byte[] bytes)
	{
		for(int i = 0; i < chars.length; i++)
			Bits.intToBytes(chars[i], bytes, i << 2);
	}
	
	public void toBytesUTF32(byte[] bytes, int start)
	{
		for(int i = 0; i < chars.length; i++)
		{
			Bits.intToBytes(chars[i], bytes, start);
			start += 4;
		}
	}
	
	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInt(chars.length);
		stream.writeChars(this.chars);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.intToBytes(chars.length, bytes, offset);
		Bits.charsToBytes(chars, 0, bytes, offset + 4);
	}
	
	public int exportSize()
	{
		return (this.chars.length << 1) + 4;
	}

	public IIterator<Character> iterator()
	{
		return this.new StringIterator();
	}
	
	private final class StringIterator 
				  implements IIterator<Character>
	{
		private int pos = 0;
		
		public boolean hasNext()
		{
			return !(pos == chars.length);
		}

		public Character next()
		{
			try {
				return chars[pos];
			} finally {
				pos++;
			}
		}

		public void skip()
		{
			pos++;
		}

		public void skip(int amt)
		{
			pos += amt;
		}
		
	}
	
	public static final char[] newFrom(char[] src, int frm)
	{
		char[] n = new char[src.length - frm];
		System.arraycopy(src, frm, n, 0, n.length);
		return n;
	}
	
	public static final CString[] arrayFrom(String[] strings)
	{
		CString[] s = new CString[strings.length];
		for(int i = 0; i < strings.length; i++)
			s[i] = new CString(strings[i]);
		return s;
	}
	
	public static final CString[] arrayFrom(char[][] strings)
	{
		CString[] s = new CString[strings.length];
		for(int i = 0; i < strings.length; i++)
			s[i] = new CString(strings[i]);
		return s;
	}
	
	public static final String[] arrayTo(CString[] strings)
	{
		String[] s = new String[strings.length];
		for(int i = 0; i < strings.length; i++)
			s[i] = new String(strings[i].chars);
		return s;
	}

	public static final Factory<CString> factory = new StringFactory();
	
	public Factory<CString> factory()
	{
		return factory;
	}

	public byte getSID8(CRC8 crc)
	{
		crc.persist(this);
		return crc.getCode();
	}

	public short getSID16(CRC16 crc)
	{
		crc.persist(this);
		return crc.getCode();
	}

	public int getSID32(CRC32 crc)
	{
		crc.persist(this);
		return crc.getCode();
	}
	
	public static final int test()
	{
		int e = 0;
		CString test = new CString("dh87adha28hd8a27dh8a2hd8ag2d8ga287dga28gda2thisiskeyboardmashingÓð↓°×ı‗,>XòY");
		e += IPersistable.test(test);
		e += IPersistable.test(new UTF8String(test));
		e += IPersistable.test(new CTFSString(test));
		e += IDeepClonable.test(test);
		e += ISoftID.test(test, test.createDeepClone());
		return e;
	}
	
	public static final StringFactory factoryutf8 = new StringFactory("UTF-8");
	public static final StringFactory factoryctfs = new StringFactory("CTF-S");
	
	public static final class UTF8String extends CString
	{
		public UTF8String(CString s)
		{
			this.chars = s.chars;
		}
		
		public UTF8String(final String s)
		{
			chars = s.toCharArray();
		}
		
		public UTF8String(char c)
		{
			this.chars = new char[] { c };
		}
		
		public UTF8String(char[] c, int start, int len)
		{
			chars = new char[len];
			System.arraycopy(c, 0, chars, start, len);
		}
		
		public UTF8String(final char ... c)
		{
			this.chars = c;
		}
		
		public UTF8String(byte[] bytes)
		{
			this(bytes, "UTF-16");
		}
		
		public UTF8String(byte[] bytes, String encoding)
		{
			switch(encoding)
			{
				case "CTF-S":
					this.chars = CTFS.toUTF16(bytes);
					break;
				case "CTF-L":
					this.chars = CTFL.toUTF16(bytes);
					break;
				case "UTF-8":
					this.chars = UTF8.toUTF16(bytes);
					break;
				case "UTF-16":
					this.chars = Bits.bytesToChars(bytes);
					break;
				case "ASCII":
					chars = new char[bytes.length];
					for(int i = 0; i < bytes.length; i++)
						chars[i] = (char) bytes[i];
					break;
				default:
					throw new java.lang.NullPointerException("Unsupported Encoding");
			}
		}
		
		public UTF8String(byte i)
		{
			this.chars = Base10.fromByte(i);
		}
		
		public UTF8String(short i)
		{
			this.chars = Base10.fromShort(i);
		}
		
		public UTF8String(int i)
		{
			this.chars = Base10.fromInt(i);
		}
		
		public UTF8String(long i)
		{
			this.chars = Base10.fromLong(i);
		}
		
		public UTF8String(boolean b)
		{
			if(b)
				this.chars = new char[] { 't', 'r', 'u', 'e' };
			else
				this.chars = new char[] { 'f', 'a', 'l', 's', 'e' };
		}
		
		public UTF8String(UTF8String string)
		{
			this.chars = ArrayUtil.copy(string.array());
		}
		
		public UTF8String(boolean[] bools)
		{
			this.chars = new char[bools.length];
			for(int i = 0; i < bools.length; i++)
				if(bools[i])
					chars[i] = '1';
				else
					chars[i] = '0';
		}
		
		public void export(IOutgoingStream os) throws IOException
		{
			os.writeInt(chars.length);
			UTF8.fromUTF16(os, chars);
		}
		
		public void export(byte[] bytes, int start)
		{
			Bits.intToBytes(chars.length, bytes, start);
			UTF8.fromUTF16(chars, 0, bytes, start + 4, chars.length);
		}
		
		public int exportSize()
		{
			return UTF8.bytesUTF16(chars) + 4;
		}
		
		public Factory<CString> factory()
		{
			return factoryutf8;
		}
		
	}
	
	public static final class CTFSString extends CString
	{
		public CTFSString(CString s)
		{
			this.chars = s.chars;
		}
		
		public CTFSString(final String s)
		{
			chars = s.toCharArray();
		}
		
		public CTFSString(char c)
		{
			this.chars = new char[] { c };
		}
		
		public CTFSString(char[] c, int start, int len)
		{
			chars = new char[len];
			System.arraycopy(c, 0, chars, start, len);
		}
		
		public CTFSString(final char ... c)
		{
			this.chars = c;
		}
		
		public CTFSString(byte[] bytes)
		{
			this(bytes, "UTF-16");
		}
		
		public CTFSString(byte[] bytes, String encoding)
		{
			switch(encoding)
			{
				case "CTF-S":
					this.chars = CTFS.toUTF16(bytes);
					break;
				case "CTF-L":
					this.chars = CTFL.toUTF16(bytes);
					break;
				case "UTF-8":
					this.chars = CTFS.toUTF16(bytes);
					break;
				case "UTF-16":
					this.chars = Bits.bytesToChars(bytes);
					break;
				case "ASCII":
					chars = new char[bytes.length];
					for(int i = 0; i < bytes.length; i++)
						chars[i] = (char) bytes[i];
					break;
				default:
					throw new java.lang.NullPointerException("Unsupported Encoding");
			}
		}
		
		public CTFSString(byte i)
		{
			this.chars = Base10.fromByte(i);
		}
		
		public CTFSString(short i)
		{
			this.chars = Base10.fromShort(i);
		}
		
		public CTFSString(int i)
		{
			this.chars = Base10.fromInt(i);
		}
		
		public CTFSString(long i)
		{
			this.chars = Base10.fromLong(i);
		}
		
		public CTFSString(boolean b)
		{
			if(b)
				this.chars = new char[] { 't', 'r', 'u', 'e' };
			else
				this.chars = new char[] { 'f', 'a', 'l', 's', 'e' };
		}
		
		public CTFSString(CTFSString string)
		{
			this.chars = ArrayUtil.copy(string.array());
		}
		
		public CTFSString(boolean[] bools)
		{
			this.chars = new char[bools.length];
			for(int i = 0; i < bools.length; i++)
				if(bools[i])
					chars[i] = '1';
				else
					chars[i] = '0';
		}
		
		public void export(IOutgoingStream os) throws IOException
		{
			os.writeInt(chars.length);
			CTFS.fromUTF16(os, chars);
		}
		
		public void export(byte[] bytes, int start)
		{
			Bits.intToBytes(chars.length, bytes, start);
			CTFS.fromUTF16(chars, 0, bytes, start + 4, chars.length);
		}
		
		public int exportSize()
		{
			return CTFS.bytesUTF16(chars) + 4;
		}
		
		public Factory<CString> factory()
		{
			return factoryctfs;
		}
		
	}
	
}

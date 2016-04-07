package claire.util.encoding;

import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IReferrable;
import claire.util.standards.IUUID;
import claire.util.standards._NAMESPACE;

public class PartialString 
	   implements IUUID<PartialString>,
	   			  IDeepClonable<PartialString>,
	   			  IReferrable<PartialString> {

	private int off, len;
	
	private char[] chars;
	
	public PartialString(char[] chars, int off, int len)
	{
		this.len = len;
		this.off = off;
		this.chars = chars;
	}
	
	public void redefine(int off, int len)
	{
		this.off = off;
		this.len = len;
	}
	
	public int getLength()
	{
		return this.len;
	}
	
	public char[] array()
	{
		return chars;
	}
	
	public char[] toCharArray()
	{
		return ArrayUtil.subArr(chars, off, len);
	}
	
	public String toString()
	{
		return new String(chars, len, off);
	}
	
	public void print()
	{
		int i = off;
		int j = len;
		while(j-- > 0)
			System.out.print(chars[i++]);
	}
	
	public byte[] toBytes()
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
		return CTFS.fromUTF16(chars, off, len);
	}
	
	public void toBytesCTFS(byte[] bytes)
	{
		CTFS.fromUTF16(chars, off, bytes, 0, len);
	}
	
	public void toBytesCTFS(byte[] bytes, int start)
	{
		CTFS.fromUTF16(chars, off, bytes, start, len);
	}
	
	public byte[] toBytesCTFL()
	{
		return CTFL.fromUTF16(chars, off, len);
	}
	
	public void toBytesCTFL(byte[] bytes)
	{
		CTFL.fromUTF16(chars, off, bytes, 0, len);
	}
	
	public void toBytesCTFL(byte[] bytes, int start)
	{
		CTFL.fromUTF16(chars, off, bytes, start, len);
	}
	
	public byte[] toBytesASCII()
	{
		byte[] bytes = new byte[len];
		int len = this.len,
			i = off,
			start = 0;
		while(len-- > 0)
			if(chars[i] < 128)
				bytes[start++] = (byte) chars[i++];
			else {
				bytes[start++] = (byte) '?';
				i++;
			}
		return bytes;
	}
	
	public void toBytesASCII(byte[] bytes)
	{
		int len = this.len,
			i = off,
			start = 0;
		while(len-- > 0)
			if(chars[i] < 128)
				bytes[start++] = (byte) chars[i++];
			else {
				bytes[start++] = (byte) '?';
				i++;
			}
	}
	
	public void toBytesASCII(byte[] bytes, int start)
	{
		int len = this.len,
			i = off;
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
		return Bits.charsToBytes(chars, off, len);
	}
	
	public void toBytesUTF16(byte[] bytes)
	{
		Bits.charsToBytes(chars, off, bytes, 0, len);
	}
	
	public void toBytesUTF16(byte[] bytes, int start)
	{
		Bits.charsToBytes(chars, off, bytes, start, len);
	}
	
	public byte[] toBytesUTF8()
	{
		return UTF8.fromUTF16(chars, off, len);
	}
	
	public void toBytesUTF8(byte[] bytes)
	{
		UTF8.fromUTF16(chars, off, bytes, 0, len);
	}
	
	public void toBytesUTF8(byte[] bytes, int start)
	{
		UTF8.fromUTF16(chars, off, bytes, start, len);
	}
	
	public byte[] toBytesUTF32()
	{
		byte[] bytes = new byte[len << 1];
		this.toBytesUTF32(bytes);
		return bytes;
	}
	
	public void toBytesUTF32(byte[] bytes)
	{
		int len = this.len,
			i = off;
		while(len-- > 0) 
			Bits.intToBytes(chars[i], bytes, i++ << 2);
	}
	
	public void toBytesUTF32(byte[] bytes, int start)
	{
		int len = this.len,
			i = off;
		while(len-- > 0) 
		{
			Bits.intToBytes(chars[i++], bytes, start);
			start += 4;
		}
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.PARTIALSTRING;
	}

	public boolean sameAs(PartialString obj)
	{
		return chars == obj.chars && (off == obj.off && len == obj.len); 
	}
	
	public boolean equals(String s)
	{
		if(s.length() == len) {
			int i = 0;
			int j = off;
			int k = len;
			while(k-- > 0)
				if(chars[j++] != s.charAt(i++))
					return false;
		} else
			return false;
		return true;
	}
	
	public boolean equals(char[] arr)
	{
		if(arr.length == len) {
			int i = 0;
			int j = off;
			int k = len;
			while(k-- > 0)
				if(chars[j++] != arr[i++])
					return false;
		} else
			return false;
		return true;
	}
	
	public boolean equals(char[] arr, int start, int length)
	{
		if(length == len) {
			int i = start;
			int j = off;
			int k = len;
			while(k-- > 0)
				if(chars[j++] !=  arr[i++])
					return false;
		} else
			return false;
		return true;
	}

	public PartialString createDeepClone()
	{
		return new PartialString(chars, off, len);
	}
	
}

package claire.util.encoding;

public final class Hex {
	
	public static byte[] fromHex(CString hex)
	{
		byte[] bytes = new byte[hex.length() >> 1];
		fromHex(hex.array(), 0, bytes, 0, bytes.length);
		return bytes;
	}
	
	public static byte[] fromHex(String hex)
	{
		byte[] bytes = new byte[hex.length() >> 1];
		fromHex(hex.toCharArray(), 0, bytes, 0, bytes.length);
		return bytes;
	}
	
	public static byte[] fromHex(char[] hex)
	{
		byte[] bytes = new byte[hex.length >> 1];
		fromHex(hex, 0, bytes, 0, bytes.length);
		return bytes;
	}
	
	public static void fromHex(CString hex, byte[] bytes)
	{
		fromHex(hex.array(), 0, bytes, 0, bytes.length);
	}
	
	public static void fromHex(String hex, byte[] bytes)
	{
		fromHex(hex.toCharArray(), 0, bytes, 0, bytes.length);
	}
	
	public static void fromHex(char[] hex, byte[] bytes)
	{
		fromHex(hex, 0, bytes, 0, bytes.length);
	}
	
	public static void fromHex(CString hex, int start0, byte[] bytes, int start1)
	{
		fromHex(hex.array(), start0, bytes, start1, bytes.length - start1);
	}
	
	public static void fromHex(String hex, int start0, byte[] bytes, int start1)
	{
		fromHex(hex.toCharArray(), start0, bytes, start1, bytes.length - start1);
	}
	
	public static void fromHex(char[] hex, int start0, byte[] bytes, int start1)
	{
		fromHex(hex, start0, bytes, start1, bytes.length - start1);
	}
	
	public static void fromHex(CString hex, int start0, byte[] bytes, int start1, int len)
	{
		fromHex(hex.array(), start0, bytes, start1, len);
	}
	
	public static void fromHex(String hex, int start0, byte[] bytes, int start1, int len)
	{
		fromHex(hex.toCharArray(), start0, bytes, start1, len);
	}
	
	/*
	 * Note: This function is not safe at all. It uses 'cool hacks' to achieve speed. 
	 * Deal with it.
	 * 
	 * For those of you who can't comprehend the coolness of hacks going on here, don't
	 * feed this function non-hex characters. It will not throw errors. You'll just get
	 * bullshit values.
	 */
	public static void fromHex(char[] hex, int start0, byte[] bytes, int start1, int len)
	{
		byte t;
		while(len > 0)
		{
			t = 0x00;
			char c = hex[start0++];
			t |= (c <= '9') ? c - '0' : c - 'W';
			t <<= 4;
			c = hex[start0++];
			t |= (c <= '9') ? c - '0' : c - 'W';
			bytes[start1++] = t;
			len--;
		}
	}
	
	public static CString toHexCString(byte[] bytes)
	{
		return new CString(toHex(bytes));
	}
	
	public static String toHexString(byte[] bytes)
	{
		return new String(toHex(bytes));
	}
	
	public static char[] toHex(byte[] bytes)
	{
		char[] hex = new char[bytes.length << 1];
		toHex(bytes, 0, hex, 0, bytes.length);
		return hex;
	}
	
	public static void toHex(byte[] bytes, char[] hex)
	{
		toHex(bytes, 0, hex, 0, hex.length);
	}
	
	public static void toHex(byte[] bytes, int start0, char[] hex, int start1)
	{
		toHex(bytes, start0, hex, start1, bytes.length - start1);
	}
	
	public static void toHex(byte[] bytes, int start0, char[] hex, int start1, int len)
	{
		byte t;
		while(len > 0)
		{
			t = bytes[start0++];
			hex[start1++] = EncodingUtil.ALPHABET[(t & 0xF0) >>> 4];
			hex[start1++] = EncodingUtil.ALPHABET[ t & 0x0F       ];
			len--;
		}
	}
	
	public static char[] toHex(byte b)
	{
		return new char[] { EncodingUtil.ALPHABET[(b & 0xF0) >>> 4], EncodingUtil.ALPHABET[b & 0x0F] };
	}
	
	public static String toHexStr(byte b)
	{
		return new String(toHex(b));
	}
	
	public static CString toHexCStr(byte b)
	{
		return new CString(toHex(b));
	}

}

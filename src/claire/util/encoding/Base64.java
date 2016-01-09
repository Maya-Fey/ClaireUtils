package claire.util.encoding;

public final class Base64 {
	
	static char[] ALPHABET = new char[] {
		'0', '1', '2', '3', '4', '5', '6', '7', 
		'8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 
		'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 
		'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 
		'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
		'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 
		'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 
		'u', 'v', 'w', 'x', 'y', 'z', '#', '$'
	};

	public static void fromBytes(byte[] b, int start0, char[] chars, int start1, int len)
	{
		int pos = start0;
		
		while(len >= 3)
		{
			int i = (b[pos++] & 0xff) << 16 |
					(b[pos++] & 0xff) <<  8 |
                    (b[pos++] & 0xff);
			chars[start1++] = ALPHABET[(i >> 18) & 0x3f];
			chars[start1++] = ALPHABET[(i >> 12) & 0x3f];
			chars[start1++] = ALPHABET[(i >> 6) & 0x3f];
			chars[start1++] = ALPHABET[i & 0x3f];
			len -= 3;
		}
		if(len != 0) {
			int byte0 = b[pos++] & 0xFF;
			if(len == 1) {
				chars[start1++] = ALPHABET[byte0 >>> 2];
				chars[start1++] = ALPHABET[(byte0 << 4) & 0x3f];
			} else {
				int byte1 = b[pos] & 0xFF;
				chars[start1++] = ALPHABET[byte0 >>> 2];
				chars[start1++] = ALPHABET[(byte0 << 4) & 0x3f | (byte1 >> 4)];
				chars[start1++] = ALPHABET[(byte1 << 2) & 0x3f];
			}
		}
	}
	
	public static char[] fromBytes(byte[] b, int start, int len)
	{
		char[] chars = new char[(len % 3 == 0) ? len * 4 / 3 : (len * 4 / 3) + 1];
		fromBytes(b, start, chars, 0, len);
		return chars;
	}
	
	public static String fromBytesToStr(byte[] b, int start, int len)
	{
		char[] chars = new char[(len % 3 == 0) ? len * 4 / 3 : (len * 4 / 3) + 1];
		fromBytes(b, start, chars, 0, len);
		return new String(chars);
	}
	
	public static CString fromBytesToCStr(byte[] b, int start, int len)
	{
		char[] chars = new char[(len % 3 == 0) ? len * 4 / 3 : (len * 4 / 3) + 1];
		fromBytes(b, start, chars, 0, len);
		return new CString(chars);
	}
	
	public static void toBytes(char[] chars, int start0, byte[] bytes, int start1, int len)
	{
		int tmp = 0, 
			bits = 0;
		byte b,
			 t1,
			 t2;
		char c;
		while(len-- > 0) {
			c = chars[start0++];
			if(c > '`') {
				b = (byte) (c - '=');
			} else if(c > '@') {
				b = (byte) (c - '7');
			} else if(c > '/') {
				b = (byte) (c - '0');
			} else
				b = (byte) (c + 27);
			bits += 6;
			tmp <<= 6;
			tmp |= b;
			if(bits == 24) {
				t2 = (byte) (tmp & 0xFF); tmp >>>= 8;
				t1 = (byte) (tmp & 0xFF); tmp >>>= 8;
				bytes[start1++] = (byte) (tmp & 0xFF); tmp = 0;
				bytes[start1++] = t1;
				bytes[start1++] = t2;
				bits = 0;
			}
		}
		if(bits == 18) {
			tmp >>>= 2;
			t1 = (byte) (tmp & 0xFF); tmp >>>= 8;
			bytes[start1++] = (byte) (tmp & 0xFF); 
			bytes[start1++] = t1;
			return;
		}
		if(bits == 12) {
			tmp >>>= 4;
			bytes[start1++] = (byte) (tmp & 0xFF);
			return;
		}
	}
	
	public static byte[] toBytes(char[] chars, int start, int len)
	{
		int size = (chars.length / 4) * 3 ;
		int rem = chars.length & 3;
		if(rem == 2)
			size++;
		else
			rem += 2;
		byte[] bytes = new byte[size];
		toBytes(chars, start, bytes, 0, len);
		return bytes;
	}
	
	public static void toBytesSafe(char[] chars, int start0, byte[] bytes, int start1, int len)
	{
		int tmp = 0, 
			bits = 0;
		byte b,
			 t1,
			 t2;
		char c;
		while(len-- > 0) {
			c = chars[start0++];
			if(c > '`') {
				if(c > '{')
					throw new java.lang.AssertionError();
				b = (byte) (c - '=');
			} else if(c > '@') {
				if(c > '[')
					throw new java.lang.AssertionError();
				b = (byte) (c - '7');
			} else if(c > '/') {
				if(c > ':')
					throw new java.lang.AssertionError();
				b = (byte) (c - '0');
			} else if(c > '"') {
				if(c > '$')
					throw new java.lang.AssertionError();
				b = (byte) (c + 27);
			} else
				throw new java.lang.AssertionError();
			bits += 6;
			tmp <<= 6;
			tmp |= b;
			if(bits == 24) {
				t2 = (byte) (tmp & 0xFF); tmp >>>= 8;
				t1 = (byte) (tmp & 0xFF); tmp >>>= 8;
				bytes[start1++] = (byte) (tmp & 0xFF); tmp = 0;
				bytes[start1++] = t1;
				bytes[start1++] = t2;
				bits = 0;
			}
		}
		if(bits == 18) {
			tmp >>>= 2;
			t1 = (byte) (tmp & 0xFF); tmp >>>= 8;
			bytes[start1++] = (byte) (tmp & 0xFF); 
			bytes[start1++] = t1;
			return;
		}
		if(bits == 12) {
			tmp >>>= 4;
			bytes[start1++] = (byte) (tmp & 0xFF);
			return;
		}
		if(bits == 6) 
			throw new java.lang.AssertionError();
	}
	
	public static byte[] toBytesSafe(char[] chars, int start, int len)
	{
		int size = (chars.length / 4) * 3 ;
		int rem = chars.length & 3;
		if(rem == 1) {
			throw new java.lang.AssertionError("Invalid Base64 - Stray single char.");
		}else if(rem == 2)
		 	size++;
		else
			rem += 2;
		byte[] bytes = new byte[size];
		toBytesSafe(chars, start, bytes, 0, len);
		return bytes;
	}
	
	public static byte toBase64(char c)
	{
		if(c > '`') {
			return (byte) (c - '=');
		} else if(c > '@') {
			return (byte) (c - '7');
		} else if(c > '/') {
			return (byte) (c - '0');
		} else
			return (byte) (c + 27);
	}
	
	public static byte toBase64Safe(char c)
	{
		if(c > '`') {
			if(c > 'z')
				throw new java.lang.AssertionError();
			return (byte) (c - '=');
		} else if(c > '@') {
			if(c > 'Z')
				throw new java.lang.AssertionError();
			return (byte) (c - '7');
		} else if(c > '/') {
			if(c > '9')
				throw new java.lang.AssertionError();
			return (byte) (c - '0');
		} else if(c > '"') {
			if(c > '$')
				throw new java.lang.AssertionError();
			return (byte) (c + 27);
		} else
			throw new java.lang.AssertionError();
	}
	
	public static boolean isBase64(char[] chars)
	{
		for(char c : chars)
		{
			if(c > '`') 
				if(c > 'z')
					return false;
			else if(c > '@') 
				if(c > 'Z')
					return false;
			else if(c > '/') 
				if(c > '9')
					return false;
			else if(c > '"') 
				if(c > '$')
					return false;
			else
				throw new java.lang.AssertionError();
		}
		return true;
	}
	
}

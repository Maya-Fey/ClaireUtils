package claire.util.encoding;

public class EncodingUtil {
	
	static char[] ALPHABET = new char[] {
		'0', '1', '2', '3', '4', '5', '6', '7', 
		'8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
		'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
		'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
		'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 
		'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 
		'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
		'U', 'V', 'W', 'X', 'Y', 'Z', '-', '$'
	};
	
	public static final char[] NEWLINE = System.lineSeparator().toCharArray();
	
	public static char[] fromLong(long ger)
	{
		if(ger == 0x8000000000000000L)
			return new char[] {'-', '9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '4', '7', '7', '5', '8', '0', '8' };
		char[] chars = new char[20];
		int i = chars.length - 1;
		boolean negative;
		if(ger < 0) 	
			negative = true;
		else {
			ger = -ger;
			negative = false;
		}
		while(ger <= -10)
		{
			chars[i--] = (char) (48 + (-ger % 10));
			ger /= 10;
		}
		chars[i--] = (char) (48 + -ger);
		if(negative)
			chars[i--] = '-';
		return CString.newFrom(chars, ++i);
	}

	public static char[] fromInt(int ger)
	{
		if(ger == 0x80000000)
			return new char[] {'-', '2', '1', '4', '7', '4', '8', '3', '6', '4', '8' };
		char[] chars = new char[11];
		int i = chars.length - 1;
		boolean negative;
		if(ger < 0) 
			negative = true;
		else {
			negative = false;
			ger = -ger;
		}
		while(ger <= -10)
		{
			chars[i--] = (char) (48 + (-ger % 10));
			ger /= 10;
		}
		chars[i--] = (char) (48 + -ger);
		if(negative)
			chars[i--] = '-';
		return CString.newFrom(chars, ++i);
	}

	public static char[] fromShort(short ger)
	{
		if(ger == 0x8000)
			return new char[] {'-', '3', '2', '7', '6', '8' };
		char[] chars = new char[6];
		int i = chars.length - 1;
		boolean negative;
		if(ger < 0) 	
			negative = true;
		else {
			negative = false;
			ger = (short) -ger;
		}
		while(ger <= -10)
		{
			chars[i--] = (char) (48 + (-ger % 10));
			ger /= 10;
		}
		chars[i--] = (char) (48 + -ger);
		if(negative)
			chars[i--] = '-';
		return CString.newFrom(chars, ++i);
	}

	public static char[] fromByte(byte ger)
	{
		if(ger == 0x80)
			return new char[] {'-', '1', '2', '8'};
		char[] chars = new char[4];
		int i = chars.length - 1;
		boolean negative;
		if(ger < 0) 
			negative = true;
		else {
			negative = false;
			ger = (byte) -ger;
		}
		while(ger <= -10)
		{
			chars[i--] = (char) (48 + (-ger % 10));
			ger /= 10;
		}
		chars[i--] = (char) (48 + -ger);
		if(negative)
			chars[i--] = '-';
		return CString.newFrom(chars, ++i);
	}

	public static final byte[] bytesFromString(CString s)
	{
		byte[] bytes = new byte[s.length() >> 1];
		char[] chars = s.toCharArray();
		if((chars.length & 1) > 0)
			throw new java.lang.IllegalArgumentException();
		int byt = 0;
		for(int i = 0; i < chars.length;)
		{
			byte b = 0;
			for(int j = 0; j < 16; j++)
				if(chars[i] == ALPHABET[j])
					break;
				else
					b += 16;
			i++;
			for(int j = 0; j < 16; j++)
				if(chars[i] == ALPHABET[j])
					break;
				else
					b++;
			i++;
			bytes[byt++] = b;
		}
		return bytes;
	}

	/**
	 * @deprecated
	 */
	public static final char[] base64FromBytes(byte[] b, int start, int len)
	{
		int pos = start;
		int chr = 0;
		char[] chars = new char[(len % 3 == 0) ? len * 4 / 3 : (len * 4 / 3) + 1];
		int rem = len;
		while(rem >= 3)
		{
			int i = (b[pos++] & 0xff) << 16 |
					(b[pos++] & 0xff) <<  8 |
                    (b[pos++] & 0xff);
			chars[chr++] = ALPHABET[(i >> 18) & 0x3f];
			chars[chr++] = ALPHABET[(i >> 12) & 0x3f];
			chars[chr++] = ALPHABET[(i >> 6) & 0x3f];
			chars[chr++] = ALPHABET[i & 0x3f];
			rem -= 3;
		}
		if(rem != 0) {
			int byte0 = b[pos++] & 0xFF;
			if(rem == 1) {
				chars[chr++] = ALPHABET[byte0 >>> 2];
				chars[chr++] = ALPHABET[(byte0 << 4) & 0x3f];
			} else {
				int byte1 = b[pos] & 0xFF;
				chars[chr++] = ALPHABET[byte0 >>> 2];
				chars[chr++] = ALPHABET[(byte0 << 4) & 0x3f | (byte1 >> 4)];
				chars[chr++] = ALPHABET[(byte1 << 2) & 0x3f];
			}
		}
		return chars;
	}
	
	/**
	 * @deprecated
	 */
	public static final char[] base64FromBytes(byte[] bytes)
	{
		return base64FromBytes(bytes, 0, bytes.length);
	}
	
	/**
	 * @deprecated
	 */
	public static final CString base64String(byte[] bytes)
	{
		return new CString(base64FromBytes(bytes));
	}
	
	/**
	 * @deprecated
	 */
	public static final CString base64String(byte[] bytes, int start, int len)
	{
		return new CString(base64FromBytes(bytes, start, len));
	}	
	
	public static final CString byteToHexString(byte b)
	{
		return new CString(ALPHABET[(b & 0xFF) >>> 4], ALPHABET[b & 0x0F]);
	}
	
	/**
	 * @deprecated
	 */
	public static final char[] byteToHex(byte b)
	{
		return new char[] { ALPHABET[(b & 0xFF) >>> 4], ALPHABET[b & 0x0F] };
	}
	
	/**
	 * @deprecated
	 */
	public static final char[] hexFromBytes(byte[] b, int start, int len)
	{
		char[] chars = new char[b.length << 1];
		for(int i = start; i < start + len; i++) {
			chars[i << 1] = ALPHABET[(b[i] & 0xFF) >>> 4];
			chars[(i << 1) + 1] = ALPHABET[b[i] & 0x0F];
		}
		return chars;			
	}
	
	/**
	 * @deprecated
	 */
	public static final char[] hexFromBytes(byte[] b)
	{
		return hexFromBytes(b, 0, b.length);		
	}
	
	/**
	 * @deprecated
	 */
	public static final CString hexString(byte[] b)
	{
		return new CString(hexFromBytes(b));
	}
	
	/**
	 * @deprecated
	 */
	public static final CString hexString(byte[] b, int start, int len)
	{
		return new CString(hexFromBytes(b, start, len));
	}
	
	public static final void REVERSE(char[] c)
	{
		for(int i = 0; i < c.length / 2; i++)
		{
			char c1 = c[i];
			c[i] = c[c.length - i - 1];
			c[c.length - i - 1] = c1;
		}
	}
	
	public static final char[] ELIMINATE_PREPENDING_CHARS(char[] in)
	{
		int i = 0;
		char c = (char) 0L;
		for(; i < in.length;)
			if(in[i] != c)
				break;
			else
				i++;
		char[] n = new char[in.length - i];
		System.arraycopy(in, i, n, 0, n.length);
		return n;
	}
	
	private static final CString TRUE = new CString(new char[] {'t', 'r', 'u', 'e'});
	private static final CString FALSE = new CString(new char[] {'f', 'a', 'l', 's', 'e'});
	
	public static final boolean parseBoolean(CString s)
	{
		return s.equals(TRUE);
	}
	
	public static final char[] booleanText(boolean b)
	{
		if(b)
			return TRUE.array();
		else
			return FALSE.array();
	}
	
	public static final CString booleanString(boolean b)
	{
		if(b)
			return TRUE;
		else
			return FALSE;
	}
	
	public static final byte byteFromHex(CString hex)
	{
		char[] chars = hex.array();
		byte byt = 0;
		int i = 0;
		for(;; i++)
			if(ALPHABET[i] == chars[0])
				break;
		byt += i;
		byt <<= 4;
		i = 0;	
		for(;; i++)
			if(ALPHABET[i] == chars[1])
				break;
		return byt += i;
	}
	
	public static final short shortFromHex(CString hex)
	{
		char[] chars = hex.array();
		short shrt = 0;
		int i = 0;
		for(;; i++)
			if(ALPHABET[i] == chars[0])
				break;
		shrt += i;
		shrt <<= 4;
		i = 0;	
		for(;; i++)
			if(ALPHABET[i] == chars[1])
				break;
		shrt += i;
		shrt <<= 4;
		i = 0;	
		for(;; i++)
			if(ALPHABET[i] == chars[2])
				break;
		shrt += i;
		shrt <<= 4;
		i = 0;	
		for(;; i++)
			if(ALPHABET[i] == chars[3])
				break;
		return shrt += i;
	}
	
}

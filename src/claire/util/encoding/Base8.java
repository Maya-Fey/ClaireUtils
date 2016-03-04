package claire.util.encoding;

public final class Base8 {
	
	private static final char[] DIGITS = new char[]
		{
			'0', '1', '2', '3', '4', '5', '6', '7'
		};
	
	public static char[] unsignedString(byte i, final char[] chars)
	{
		if(i == 0)
			return new char[] { '0' };
		int j = 3;
		while(i != 0)
		{
			chars[--j] = DIGITS[i & 0x00000007];
			i = (byte) ((i & 0xFF) >>> 3);	
		}
		final char[] fin = new char[3 - j];
		System.arraycopy(chars, j, fin, 0, fin.length);
		return fin;
	}
	
	public static char[] signedString(byte i, final char[] chars)
	{
		if(i == 0)
			return new char[] { '0' };
		if(i > -1) 
			return unsignedString(i, chars);
		i = (byte) -i;
		int j = 3;
		while(i != 0)
		{
			chars[--j] = DIGITS[i & 0x00000007];
			i = (byte) ((i & 0xFF) >>> 3);	
		}
		final char[] fin = new char[4 - j];
		fin[0] = '-';
		System.arraycopy(chars, j, fin, 1, fin.length - 1);
		return fin;
	}
	
	public static char[] unsignedString(byte i)
	{
		if(i == 0)
			return new char[] { '0' };
		final char[] chars = new char[3];
		int j = 3;
		while(i != 0)
		{
			chars[--j] = DIGITS[i & 0x00000007];
			i = (byte) ((i & 0xFF) >>> 3);	
		}
		final char[] fin = new char[3 - j];
		System.arraycopy(chars, j, fin, 0, fin.length);
		return fin;
	}
	
	public static char[] signedString(byte i)
	{
		if(i == 0)
			return new char[] { '0' };
		if(i > -1) 
			return unsignedString(i);
		final char[] chars = new char[3];
		i = (byte) -i;
		int j = 3;
		while(i != 0)
		{
			chars[--j] = DIGITS[i & 0x00000007];
			i = (byte) ((i & 0xFF) >>> 3);	
		}
		final char[] fin = new char[4 - j];
		fin[0] = '-';
		System.arraycopy(chars, j, fin, 1, fin.length - 1);
		return fin;
	}
	
	public static char[] unsignedString(short i, final char[] chars)
	{
		if(i == 0)
			return new char[] { '0' };
		int j = 6;
		while(i != 0)
		{
			chars[--j] = DIGITS[i & 0x00000007];
			i = (short) ((i & 0xFFFF) >>> 3);	
		}
		final char[] fin = new char[6 - j];
		System.arraycopy(chars, j, fin, 0, fin.length);
		return fin;
	}
	
	public static char[] signedString(short i, final char[] chars)
	{
		if(i == 0)
			return new char[] { '0' };
		if(i > -1) 
			return unsignedString(i, chars);
		i = (short) -i;
		int j = 6;
		while(i != 0)
		{
			chars[--j] = DIGITS[i & 0x00000007];
			i = (short) ((i & 0xFFFF) >>> 3);	
		}
		final char[] fin = new char[7 - j];
		fin[0] = '-';
		System.arraycopy(chars, j, fin, 1, fin.length - 1);
		return fin;
	}
	
	public static char[] unsignedString(short i)
	{
		if(i == 0)
			return new char[] { '0' };
		final char[] chars = new char[6];
		int j = 6;
		while(i != 0)
		{
			chars[--j] = DIGITS[i & 0x00000007];
			i = (short) ((i & 0xFFFF) >>> 3);	
		}
		final char[] fin = new char[6 - j];
		System.arraycopy(chars, j, fin, 0, fin.length);
		return fin;
	}
	
	public static char[] signedString(short i)
	{
		if(i == 0)
			return new char[] { '0' };
		if(i > -1) 
			return unsignedString(i);
		final char[] chars = new char[6];
		i = (short) -i;
		int j = 6;
		while(i != 0)
		{
			chars[--j] = DIGITS[i & 0x00000007];
			i = (short) ((i & 0xFFFF) >>> 3);	
		}
		final char[] fin = new char[7 - j];
		fin[0] = '-';
		System.arraycopy(chars, j, fin, 1, fin.length - 1);
		return fin;
	}
	
	public static char[] unsignedString(int i, final char[] chars)
	{
		if(i == 0)
			return new char[] { '0' };
		int j = 11;
		while(i != 0)
		{
			chars[--j] = DIGITS[i & 0x00000007];
			i >>>= 3;	
		}
		final char[] fin = new char[11 - j];
		System.arraycopy(chars, j, fin, 0, fin.length);
		return fin;
	}
	
	public static char[] signedString(int i, final char[] chars)
	{
		if(i == 0)
			return new char[] { '0' };
		if(i > -1) 
			return unsignedString(i, chars);
		i = -i;
		int j = 11;
		while(i != 0)
		{
			chars[--j] = DIGITS[i & 0x00000007];
			i >>>= 3;	
		}
		final char[] fin = new char[12 - j];
		fin[0] = '-';
		System.arraycopy(chars, j, fin, 1, fin.length - 1);
		return fin;
	}
	
	public static char[] unsignedString(int i)
	{
		if(i == 0)
			return new char[] { '0' };
		final char[] chars = new char[11];
		int j = 11;
		while(i != 0)
		{
			chars[--j] = DIGITS[i & 0x00000007];
			i >>>= 3;	
		}
		final char[] fin = new char[11 - j];
		System.arraycopy(chars, j, fin, 0, fin.length);
		return fin;
	}
	
	public static char[] signedString(int i)
	{
		if(i == 0)
			return new char[] { '0' };
		if(i > -1) 
			return unsignedString(i);
		final char[] chars = new char[11];
		i = -i;
		int j = 11;
		while(i != 0)
		{
			chars[--j] = DIGITS[i & 0x00000007];
			i >>>= 3;	
		}
		final char[] fin = new char[12 - j];
		fin[0] = '-';
		System.arraycopy(chars, j, fin, 1, fin.length - 1);
		return fin;
	}
	
	public static char[] unsignedString(long i, final char[] chars)
	{
		if(i == 0)
			return new char[] { '0' };
		int j = 22;
		while(i != 0)
		{
			chars[--j] = DIGITS[(int) (i & 0x00000007)];
			i >>>= 3;	
		}
		final char[] fin = new char[22 - j];
		System.arraycopy(chars, j, fin, 0, fin.length);
		return fin;
	}
	
	public static char[] signedString(long i, final char[] chars)
	{
		if(i == 0)
			return new char[] { '0' };
		if(i > -1) 
			return unsignedString(i, chars);
		i = -i;
		int j = 22;
		while(i != 0)
		{
			chars[--j] = DIGITS[(int) (i & 0x00000007)];
			i >>>= 3;	
		}
		final char[] fin = new char[23 - j];
		fin[0] = '-';
		System.arraycopy(chars, j, fin, 1, fin.length - 1);
		return fin;
	}
	
	public static char[] unsignedString(long i)
	{
		if(i == 0)
			return new char[] { '0' };
		final char[] chars = new char[22];
		int j = 22;
		while(i != 0)
		{
			chars[--j] = DIGITS[(int) (i & 0x00000007)];
			i >>>= 3;	
		}
		final char[] fin = new char[22 - j];
		System.arraycopy(chars, j, fin, 0, fin.length);
		return fin;
	}
	
	public static char[] signedString(long i)
	{
		if(i == 0)
			return new char[] { '0' };
		if(i > -1) 
			return unsignedString(i);
		final char[] chars = new char[22];
		i = -i;
		int j = 22;
		while(i != 0)
		{
			chars[--j] = DIGITS[(int) (i & 0x00000007)];
			i >>>= 3;	
		}
		final char[] fin = new char[23 - j];
		fin[0] = '-';
		System.arraycopy(chars, j, fin, 1, fin.length - 1);
		return fin;
	}
	
	public static int toUInt(final CString i)
	{
		final char[] chars = i.array();
		int f = 0;
		for(char c : chars) {
			f <<= 3;
			f |= c - '0';
		}
		return f;
	}
	
	public static int toInt(final CString i)
	{
		final char[] chars = i.array();
		int pos, f = 0;
		final boolean positive;
		if(chars[0] == '-') {
			pos = 1;
			positive = false;
		} else {
			pos = 0;
			positive = true;
		}
		for(; pos < chars.length; pos++) {
			f <<= 3;
			f -= chars[pos] - '0';
		}
		if(positive)
			return -f;
		return f;
	}
	
	public static boolean isBase8(final char[] chars)
	{
		if(chars.length == 0)
			return false;
		int i = 0;
		if(chars[i] == '-')
			i++;
		while(i < chars.length)
			if(chars[i] < '0' || chars[i++] > '7')
				return false;
		return true;
	}
	
	public static boolean isBase8(final CString s)
	{
		if(s.length() == 0)
			return false;
		final char[] chars = s.array();
		int i = 0;
		if(chars[i] == '-')
			i++;
		while(i < chars.length)
			if(chars[i] < '0' || chars[i++] > '7')
				return false;
		return true;
	}
	
	public static boolean isBase8(final String s)
	{
		if(s.length() == 0)
			return false;
		int i = 0;
		if(s.charAt(i) == '-')
			i++;
		for(; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c < '0' || c > '7')
				return false;
		}
		return true;
	}
	
	public static boolean isBase8Un(final char[] chars)
	{
		if(chars.length == 0)
			return false;
		for(char c : chars)
			if(c < '0' || c > '7')
				return false;
		return true;
	}
	
	public static boolean isBase8Un(final CString s)
	{
		if(s.length() == 0)
			return false;
		for(char c : s.array())
			if(c < '0' || c > '7')
				return false;
		return true;
	}
	
	public static boolean isBase8Un(final String s)
	{
		if(s.length() == 0)
			return false;
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c < '0' || c > '7')
				return false;
		}
		return true;
	}

}

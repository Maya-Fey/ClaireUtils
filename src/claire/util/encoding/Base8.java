package claire.util.encoding;


public final class Base8 {
	
	private static final char[] DIGITS = new char[]
		{
			'0', '1', '2', '3', '4', '5', '6', '7'
		};
	
	public static CString unsignedString(int i)
	{
		char[] chars = new char[11];
		for(int j = 10; j >= 0; j--) 
		{
			chars[j] = DIGITS[i & 0x00000007];
			i >>>= 3;	
		}
		int start = 0;
		while(chars[start] == '0')
			start++;
		char[] fin = new char[11 - start];
		System.arraycopy(chars, start, fin, 0, fin.length);
		return new CString(fin);
	}
	
	public static CString signedString(int i)
	{
		if((i & 0x80000000) == 0) 
			return unsignedString(i);
		char[] chars = new char[11];
		int first = 1;
		i = -i;
		for(int j = chars.length - 1; j >= 0; j--) 
		{
			chars[j] = DIGITS[i & 0x00000007];
			i >>>= 3;	
		}
		int start = 0;
		while(chars[start] == '0')
			start++;
		char[] fin = new char[12 - start];
		fin[0] = '-';
		System.arraycopy(chars, start, fin, first, fin.length - 1);
		return new CString(fin);
	}
	
	public static int toUInt(CString i)
	{
		char[] chars = i.array();
		int f = 0;
		for(char c : chars) {
			f <<= 3;
			f |= c - '0';
		}
		return f;
	}
	
	public static int toInt(CString i)
	{
		char[] chars = i.array();
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
	
	public static boolean isBase8(CString s)
	{
		for(char c : s.array())
			if(c < '0' || c > '7')
				return false;
		return true;
	}
	
	public static boolean isBase8(String s)
	{
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c < '0' || c > '7')
				return false;
		}
		return true;
	}

}

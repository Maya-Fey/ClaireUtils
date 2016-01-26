package claire.util.encoding;

public final class Base10 {

	public static byte stringToByte(char[] chars)
	{
		byte acc = 0;
		boolean negative = false;
		if(chars.length > 0)
		{
			int pos = 0;
			if(chars[pos] == '-')
			{
				negative = true;
				pos++;
			}
			while(pos < chars.length)
			{
				acc *= 10;
				acc -= (chars[pos] - 48);
				pos++;
			}
		}
		return (byte) (negative ? acc : -acc);
	}
	
	public static short stringToShort(char[] chars)
	{
		short acc = 0;
		boolean negative = false;
		if(chars.length > 0)
		{
			int pos = 0;
			if(chars[pos] == '-')
			{
				negative = true;
				pos++;
			}
			while(pos < chars.length)
			{
				acc *= 10;
				acc -= (chars[pos] - 48);
				pos++;
			}
		}
		return (short) (negative ? acc : -acc);
	}
	
	public static int stringToInt(char[] chars)
	{
		int acc = 0;
		boolean negative = false;
		if(chars.length > 0)
		{
			int pos = 0;
			if(chars[pos] == '-')
			{
				negative = true;
				pos++;
			}
			while(pos < chars.length)
			{
				acc *= 10;
				acc -= (chars[pos] - 48);
				pos++;
			}
		}
		return negative ? acc : -acc;
	}
	
	public static long stringToLong(char[] chars)
	{
		long acc = 0;
		boolean negative = false;
		if(chars.length > 0)
		{
			int pos = 0;
			if(chars[pos] == '-')
			{
				negative = true;
				pos++;
			}
			while(pos < chars.length)
			{
				acc *= 10;
				acc -= (chars[pos] - 48);
				pos++;
			}
		}
		return negative ? acc : -acc;
	}
	
	public static byte stringToByte(String chars)
	{
		byte acc = 0;
		boolean negative = false;
		if(chars.length() > 0)
		{
			int pos = 0;
			if(chars.charAt(pos) == '-')
			{
				negative = true;
				pos++;
			}
			while(pos < chars.length())
			{
				acc *= 10;
				acc -= (chars.charAt(pos) - 48);
				pos++;
			}
		}
		return (byte) ((negative) ? acc : -acc);
	}
	
	public static short stringToShort(String chars)
	{
		short acc = 0;
		boolean negative = false;
		if(chars.length() > 0)
		{
			int pos = 0;
			if(chars.charAt(pos) == '-')
			{
				negative = true;
				pos++;
			}
			while(pos < chars.length())
			{
				acc *= 10;
				acc -= (chars.charAt(pos) - 48);
				pos++;
			}
		}
		return (short) ((negative) ? acc : -acc);
	}
	
	public static int stringToInt(String chars)
	{
		int acc = 0;
		boolean negative = false;
		if(chars.length() > 0)
		{
			int pos = 0;
			if(chars.charAt(pos) == '-')
			{
				negative = true;
				pos++;
			}
			while(pos < chars.length())
			{
				acc *= 10;
				acc -= (chars.charAt(pos) - 48);
				pos++;
			}
		}
		return (negative) ? acc : -acc;
	}
	
	public static long stringToLong(String chars)
	{
		long acc = 0;
		boolean negative = false;
		if(chars.length() > 0)
		{
			int pos = 0;
			if(chars.charAt(pos) == '-')
			{
				negative = true;
				pos++;
			}
			while(pos < chars.length())
			{
				acc *= 10;
				acc -= (chars.charAt(pos) - 48);
				pos++;
			}
		}
		return (negative) ? acc : -acc;
	}
	
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
	
	public static boolean isBase10(CString s)
	{
		if(s.length() == 0)
			return false;
		for(char c : s.array())
			if(c < '0' || c > '9')
				return false;
		return true;
	}
	
	public static boolean isBase10(String s)
	{
		if(s.length() == 0)
			return false;
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c < '0' || c > '9')
				return false;
		}
		return true;
	}

}

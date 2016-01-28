package claire.util.encoding;

import claire.util.memory.util.Pointer;

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
			while(chars[pos] == '0')
				pos++;
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
			while(chars[pos] == '0')
				pos++;
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
			while(chars[pos] == '0')
				pos++;
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
			while(chars[pos] == '0')
				pos++;
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
			while(chars.charAt(pos) == '0')
				pos++;
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
			while(chars.charAt(pos) == '0')
				pos++;
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
			while(chars.charAt(pos) == '0')
				pos++;
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
			while(chars.charAt(pos) == '0')
				pos++;
			while(pos < chars.length())
			{
				acc *= 10;
				acc -= (chars.charAt(pos) - 48);
				pos++;
			}
		}
		return (negative) ? acc : -acc;
	}
	
	/*
	 * 0 = A-OK
	 * 1 = Out of bound characters
	 * 2 = Number too big for int width
	 */
	public static byte stringCheckByte(char[] chars, Pointer<Integer> error)
	{
		if(chars.length > 0)
		{
			int acc = 0;
			boolean negative = false;
			int pos = 0;
			if(chars[pos] == '-')
			{
				negative = true;
				pos++;
			}
			while(chars[pos] == '0')
				pos++;
			while(pos < chars.length)
			{
				final char c = chars[pos];
				if(c < '0' || c > '9') {
					error.set(1);
					return 0;
				}
				acc *= 10;
				acc -= (c - 48);
				if(acc < -128) {
					error.set(2);
					return 0;
				}
				pos++;
			}
			if(acc == -128 && !negative) {
				error.set(2);
				return 0;
			}
			return (byte) (negative ? acc : -acc);
		} else
			return 0;
	}
	
	public static short stringCheckShort(char[] chars, Pointer<Integer> error)
	{
		if(chars.length > 0)
		{
			short acc = 0;
			boolean negative = false;
			int pos = 0;
			if(chars[pos] == '-')
			{
				negative = true;
				pos++;
			}
			while(chars[pos] == '0')
				pos++;
			while(pos < chars.length)
			{
				final char c = chars[pos];
				if(c < '0' || c > '9') {
					error.set(1);
					return 0;
				}
				acc *= 10;
				acc -= (c - 48);
				if(acc >= 0) {
					error.set(2);
					return 0;
				}
				pos++;
			}
			if(acc == -32768 && !negative) {
				error.set(2);
				return 0;
			}
			return (short) (negative ? acc : -acc);
		} else
			return 0;
	}
	
	public static int stringCheckInt(char[] chars, Pointer<Integer> error)
	{
		if(chars.length > 0)
		{
			int acc = 0;
			boolean negative = false;
			int pos = 0;
			if(chars[pos] == '-')
			{
				negative = true;
				pos++;
			}
			while(chars[pos] == '0')
				pos++;
			while(pos < chars.length)
			{
				final char c = chars[pos];
				if(c < '0' || c > '9') {
					error.set(1);
					return 0;
				}
				acc *= 10;
				acc -= (c - 48);
				if(acc >= 0) {
					error.set(2);
					return 0;
				}
				pos++;
			}
			if(acc == 0x80000000 && !negative) {
				error.set(2);
				return 0;
			}
			return negative ? acc : -acc;
		} else
			return 0;
	}
	
	public static long stringCheckLong(char[] chars, Pointer<Integer> error)
	{
		if(chars.length > 0)
		{
			long acc = 0;
			boolean negative = false;
			int pos = 0;
			if(chars[pos] == '-')
			{
				negative = true;
				pos++;
			}
			while(chars[pos] == '0')
				pos++;
			while(pos < chars.length)
			{
				final char c = chars[pos];
				if(c < '0' || c > '9') {
					error.set(1);
					return 0;
				}
				acc *= 10;
				acc -= (c - 48);
				if(acc >= 0) {
					error.set(2);
					return 0;
				}
				pos++;
			}
			if(acc == 0x8000000000000000L && !negative) {
				error.set(2);
				return 0;
			}
			return negative ? acc : -acc;
		} else
			return 0;
	}
	
	public static byte stringCheckByte(String chars, Pointer<Integer> error)
	{
		if(chars.length() > 0)
		{
			int acc = 0;
			boolean negative = false;
			int pos = 0;
			if(chars.charAt(pos) == '-')
			{
				negative = true;
				pos++;
			}
			while(chars.charAt(pos) == '0')
				pos++;
			while(pos < chars.length())
			{
				final char c = chars.charAt(pos);
				if(c < '0' || c > '9') {
					error.set(1);
					return 0;
				}
				acc *= 10;
				acc -= (c - 48);
				if(acc < -128) {
					error.set(2);
					return 0;
				}
				pos++;
			}
			if(acc == -128 && !negative) {
				error.set(2);
				return 0;
			}
			return (byte) (negative ? acc : -acc);
		} else
			return 0;
	}
	
	public static short stringCheckShort(String chars, Pointer<Integer> error)
	{
		if(chars.length() > 0)
		{
			short acc = 0;
			boolean negative = false;
			int pos = 0;
			if(chars.charAt(pos) == '-')
			{
				negative = true;
				pos++;
			}
			while(chars.charAt(pos) == '0')
				pos++;
			while(pos < chars.length())
			{
				final char c = chars.charAt(pos);
				if(c < '0' || c > '9') {
					error.set(1);
					return 0;
				}
				acc *= 10;
				acc -= (c - 48);
				if(acc >= 0) {
					error.set(2);
					return 0;
				}
				pos++;
			}
			if(acc == -32768 && !negative) {
				error.set(2);
				return 0;
			}
			return (short) (negative ? acc : -acc);
		} else
			return 0;
	}
	
	public static int stringCheckInt(String chars, Pointer<Integer> error)
	{
		if(chars.length() > 0)
		{
			int acc = 0;
			boolean negative = false;
			int pos = 0;
			if(chars.charAt(pos) == '-')
			{
				negative = true;
				pos++;
			}
			while(chars.charAt(pos) == '0')
				pos++;
			while(pos < chars.length())
			{
				final char c = chars.charAt(pos);
				if(c < '0' || c > '9') {
					error.set(1);
					return 0;
				}
				acc *= 10;
				acc -= (c - 48);
				if(acc >= 0) {
					error.set(2);
					return 0;
				}
				pos++;
			}
			if(acc == 0x80000000 && !negative) {
				error.set(2);
				return 0;
			}
			return negative ? acc : -acc;
		} else
			return 0;
	}
	
	public static long stringCheckLong(String chars, Pointer<Integer> error)
	{
		if(chars.length() > 0)
		{
			long acc = 0;
			boolean negative = false;
			int pos = 0;
			if(chars.charAt(pos) == '-')
			{
				negative = true;
				pos++;
			}
			while(chars.charAt(pos) == '0')
				pos++;
			while(pos < chars.length())
			{
				final char c = chars.charAt(pos);
				if(c < '0' || c > '9') {
					error.set(1);
					return 0;
				}
				acc *= 10;
				acc -= (c - 48);
				if(acc >= 0) {
					error.set(2);
					return 0;
				}
				pos++;
			}
			if(acc == 0x8000000000000000L && !negative) {
				error.set(2);
				return 0;
			}
			return negative ? acc : -acc;
		} else
			return 0;
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
	
	//TODO: Update all uses of isBase10 + stringTo* to a safe stringTo
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
	
	public static boolean isBase10(char[] chars)
	{
		if(chars.length == 0)
			return false;
		for(char c : chars)
			if(c < '0' || c > '9')
				return false;
		return true;
	}

}

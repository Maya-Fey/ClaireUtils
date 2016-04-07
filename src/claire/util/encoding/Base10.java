package claire.util.encoding;

import claire.util.math.MathHelper;
import claire.util.memory.util.Pointer;

public final class Base10 {
	
	/*
	 * Note: This class has a lot of bloat. Significant bloat comes from
	 * the distinction between String, CString, and char[], which accounts
	 * for a few hundred lines, the next is the distinction between byte,
	 * short, int, and long which could easily halve the size of this class
	 * if removed, and the no-memory version of from*(), and the distinction
	 * between signed and unsigned. It would be wise to use the outline bar 
	 * to traverse this class
	 */

	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 8 bits.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the array contains non-numerals, the behavior of this
	 * method is undefined</li>
	 * <li>If the array represents an integer greater then 127 or
	 * less then -128, then the behavior of this method is undefined</li>
	 * <li>If the string is of a length greater then one and contains nothing
	 * but zeroes, an ArrayIndexOutOfBounds exception will be thrown</li>
	 * </ul>
	 * Returns: a byte with the value that the character string
	 * represented.
	 */
	public static byte stringToByte(final char[] chars)
	{
		return stringToByte(chars, 0, chars.length);
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 8 bits.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * <li>An integer pointing to the position of the first numeral char</li>
	 * <li>An integer showing how many digits the number is</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the array contains non-numerals, the behavior of this
	 * method is undefined</li>
	 * <li>If the array represents an integer greater then 127 or
	 * less then -128, then the behavior of this method is undefined</li>
	 * <li>If the string is of a length greater then one and contains nothing
	 * but zeroes, an ArrayIndexOutOfBounds exception will be thrown</li>
	 * </ul>
	 * Returns: a byte with the value that the character string
	 * represented.
	 */
	public static byte stringToByte(final char[] chars, int start, int len)
	{
		byte acc = 0;
		boolean negative = false;
		if(chars.length > 0)
		{
			char z = chars[start];
			if(z == '-')
			{
				negative = true;
				start++;
				len--;
			} else if(z == '0') {
				if(len == 1) 
					return 0;
				else {
					start++;
					len--;
				}
				while(chars[start] == '0') {
					start++;
					len--;
				}
			}
			while(len-- > 0)
			{
				acc *= 10;
				acc -= (chars[start] - 48);
				start++;
			}
		}
		return (byte) (negative ? acc : -acc);
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 16 bits.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the array contains non-numerals, the behavior of this
	 * method is undefined</li>
	 * <li>If the array represents an integer greater then 32767 or
	 * less then -32768, then the behavior of this method is undefined
	 * </li>
	 * <li>If the string is of a length greater then one and contains nothing
	 * but zeroes, an ArrayIndexOutOfBounds exception will be thrown</li>
	 * </ul>
	 * Returns: a short with the value that the character string
	 * represented.
	 */
	public static short stringToShort(final char[] chars)
	{
		return stringToShort(chars, 0, chars.length);
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 16 bits.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * <li>An integer pointing to the position of the first numeral char</li>
	 * <li>An integer showing how many digits the number is</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the array contains non-numerals, the behavior of this
	 * method is undefined</li>
	 * <li>If the array represents an integer greater then 32767 or
	 * less then -32768, then the behavior of this method is undefined
	 * </li>
	 * <li>If the string is of a length greater then one and contains nothing
	 * but zeroes, an ArrayIndexOutOfBounds exception will be thrown</li>
	 * </ul>
	 * Returns: a short with the value that the character string
	 * represented.
	 */
	public static short stringToShort(final char[] chars, int start, int len)
	{
		short acc = 0;
		boolean negative = false;
		if(chars.length > 0)
		{
			char z = chars[start];
			if(z == '-')
			{
				negative = true;
				start++;
				len--;
			} else if(z == '0') {
				if(len == 1) 
					return 0;
				else {
					start++;
					len--;
				}
				while(chars[start] == '0') {
					start++;
					len--;
				}
			}
			while(len-- > 0)
			{
				acc *= 10;
				acc -= (chars[start] - 48);
				start++;
			}
		}
		return (short) (negative ? acc : -acc);
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 32 bits.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the array contains non-numerals, the behavior of this
	 * method is undefined</li>
	 * <li>If the array represents an integer greater then 2147483647 
	 * or less then -2147483648, then the behavior of this method is 
	 * undefined</li>
	 * <li>If the string is of a length greater then one and contains nothing
	 * but zeroes, an ArrayIndexOutOfBounds exception will be thrown</li>
	 * </ul>
	 * Returns: a int with the value that the character string
	 * represented.
	 */
	public static int stringToInt(final char[] chars)
	{
		return stringToInt(chars, 0, chars.length);
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 32 bits.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * <li>An integer pointing to the position of the first numeral char</li>
	 * <li>An integer showing how many digits the number is</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the array contains non-numerals, the behavior of this
	 * method is undefined</li>
	 * <li>If the array represents an integer greater then 2147483647 
	 * or less then -2147483648, then the behavior of this method is 
	 * undefined</li>
	 * <li>If the string is of a length greater then one and contains nothing
	 * but zeroes, an ArrayIndexOutOfBounds exception will be thrown</li>
	 * </ul>
	 * Returns: a int with the value that the character string
	 * represented.
	 */
	public static int stringToInt(final char[] chars, int start, int len)
	{
		int acc = 0;
		boolean negative = false;
		if(chars.length > 0)
		{
			char z = chars[start];
			if(z == '-')
			{
				negative = true;
				start++;
				len--;
			} else if(z == '0') {
				if(len == 1) 
					return 0;
				else {
					start++;
					len--;
				}
				while(chars[start] == '0') {
					start++;
					len--;
				}
			}
			while(len-- > 0)
			{
				acc *= 10;
				acc -= (chars[start] - 48);
				start++;
			}
		}
		return negative ? acc : -acc;
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 64 bits.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the array contains non-numerals, the behavior of this
	 * method is undefined</li>
	 * <li>If the array represents an integer greater then Long.MAX_VALUE 
	 * or less then Long.MIN_VALUE, then the behavior of this method is 
	 * undefined</li>
	 * <li>If the string is of a length greater then one and contains nothing
	 * but zeroes, an ArrayIndexOutOfBounds exception will be thrown</li>
	 * </ul>
	 * Returns: a long with the value that the character string
	 * represented.
	 */
	public static long stringToLong(final char[] chars)
	{
		return stringToLong(chars, 0, chars.length);
	}

	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 64 bits.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * <li>An integer pointing to the position of the first numeral char</li>
	 * <li>An integer showing how many digits the number is</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the array contains non-numerals, the behavior of this
	 * method is undefined</li>
	 * <li>If the array represents an integer greater then Long.MAX_VALUE 
	 * or less then Long.MIN_VALUE, then the behavior of this method is 
	 * undefined</li>
	 * <li>If the string is of a length greater then one and contains nothing
	 * but zeroes, an ArrayIndexOutOfBounds exception will be thrown</li>
	 * </ul>
	 * Returns: a long with the value that the character string
	 * represented.
	 */
	public static long stringToLong(final char[] chars, int start, int len)
	{
		long acc = 0;
		boolean negative = false;
		if(chars.length > 0)
		{
			char z = chars[start];
			if(z == '-')
			{
				negative = true;
				start++;
				len--;
			} else if(z == '0') {
				if(len == 1) 
					return 0;
				else {
					start++;
					len--;
				}
				while(chars[start] == '0') {
					start++;
					len--;
				}
			}
			while(len-- > 0)
			{
				acc *= 10;
				acc -= (chars[start] - 48);
				start++;
			}
		}
		return negative ? acc : -acc;
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 8 bits.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>A String representing an integer</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the string contains non-numerals, the behavior of this
	 * method is undefined</li>
	 * <li>If the string represents an integer greater then 127 or
	 * less then -128, then the behavior of this method is undefined</li>
	 * <li>If the string is of a length greater then one and contains nothing
	 * but zeroes, an ArrayIndexOutOfBounds exception will be thrown</li>
	 * </ul>
	 * Returns: a byte with the value that the character string
	 * represented.
	 */
	public static byte stringToByte(final String chars)
	{
		byte acc = 0;
		boolean negative = false;
		if(chars.length() > 0)
		{
			int pos = 0;
			char z = chars.charAt(0);
			if(z == '-') {
				negative = true;
				pos++;
			} else if(z == '0') {
				if(chars.length() == 1)
					return 0;
				pos++;
				while(chars.charAt(pos) == '0')
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
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 16 bits.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>A strings representing an integer</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the string contains non-numerals, the behavior of this
	 * method is undefined</li>
	 * <li>If the string represents an integer greater then 32767 or
	 * less then -32768, then the behavior of this method is undefined
	 * </li>
	 * <li>If the string is of a length greater then one and contains nothing
	 * but zeroes, an ArrayIndexOutOfBounds exception will be thrown</li>
	 * </ul>
	 * Returns: a short with the value that the character string
	 * represented.
	 */
	public static short stringToShort(final String chars)
	{
		short acc = 0;
		boolean negative = false;
		if(chars.length() > 0)
		{
			int pos = 0;
			char z = chars.charAt(0);
			if(z == '-') {
				negative = true;
				pos++;
			} else if(z == '0') {
				if(chars.length() == 1)
					return 0;
				pos++;
				while(chars.charAt(pos) == '0')
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
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 32 bits.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>A string representing an integer</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the string contains non-numerals, the behavior of this
	 * method is undefined</li>
	 * <li>If the string represents an integer greater then 2147483647 
	 * or less then -2147483648, then the behavior of this method is 
	 * undefined</li>
	 * <li>If the string is of a length greater then one and contains nothing
	 * but zeroes, an ArrayIndexOutOfBounds exception will be thrown</li>
	 * </ul>
	 * Returns: a int with the value that the character string
	 * represented.
	 */
	public static int stringToInt(final String chars)
	{
		int acc = 0;
		boolean negative = false;
		if(chars.length() > 0)
		{
			int pos = 0;
			char z = chars.charAt(0);
			if(z == '-') {
				negative = true;
				pos++;
			} else if(z == '0') {
				if(chars.length() == 1)
					return 0;
				pos++;
				while(chars.charAt(pos) == '0')
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
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 64 bits.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>A string of chars representing an integer</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the string contains non-numerals, the behavior of this
	 * method is undefined</li>
	 * <li>If the string represents an integer greater then Long.MAX_VALUE 
	 * or less then Long.MIN_VALUE, then the behavior of this method is 
	 * undefined</li>
	 * <li>If the string is of a length greater then one and contains nothing
	 * but zeroes, an ArrayIndexOutOfBounds exception will be thrown</li>
	 * </ul>
	 * Returns: a long with the value that the character string
	 * represented.
	 */
	public static long stringToLong(final String chars)
	{
		long acc = 0;
		boolean negative = false;
		if(chars.length() > 0)
		{
			int pos = 0;
			char z = chars.charAt(0);
			if(z == '-') {
				negative = true;
				pos++;
			} else if(z == '0') {
				if(chars.length() == 1)
					return 0;
				pos++;
				while(chars.charAt(pos) == '0')
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
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 8 bits, adds an error code to a pointer should 
	 * anything go wrong during the conversion.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * <li>An integer pointer for passing error codes with the value of
	 * zero.</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the pointer points too a nonzero error code, and the method
	 * returns no errors, the error code will remain identical.</li>
	 * <li>If the string contains non-numeral chars, then the value
	 * of zero will be returned and the error code will be set too one.</li>
	 * <li>If the string represents and integer that cannot fit in 8 bits,
	 * then zero will be returned and the error code will be set to 
	 * two.</li>
	 * </ul>
	 * Returns: a byte with the value that the character string
	 * represented. If the method returns successfully an error
	 * code of zero will remain in the pointer.
	 */
	public static byte stringCheckByte(final char[] chars, final Pointer<Integer> error)
	{
		return stringCheckByte(chars, 0, chars.length, error);
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 8 bits, adds an error code to a pointer should 
	 * anything go wrong during the conversion.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * <li>An integer pointing to the position of the first numeral char</li>
	 * <li>An integer showing how many digits the number is</li>
	 * <li>An integer pointer for passing error codes with the value of
	 * zero.</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the pointer points too a nonzero error code, and the method
	 * returns no errors, the error code will remain identical.</li>
	 * <li>If the string contains non-numeral chars, then the value
	 * of zero will be returned and the error code will be set too one.</li>
	 * <li>If the string represents and integer that cannot fit in 8 bits,
	 * then zero will be returned and the error code will be set to 
	 * two.</li>
	 * </ul>
	 * Returns: a byte with the value that the character string
	 * represented. If the method returns successfully an error
	 * code of zero will remain in the pointer.
	 */
	public static byte stringCheckByte(final char[] chars, int start, int len, final Pointer<Integer> error)
	{
		if(chars.length > 0)
		{
			int acc = 0;
			boolean negative = false;
			if(chars[start] == '-')
			{
				negative = true;
				start++;
				len--;
			}
			while(len > 0 && chars[start] == '0') {
				start++;
				len--;
			}
			while(len-- > 0)
			{
				final char c = chars[start];
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
				start++;
			}
			if(acc == -128 && !negative) {
				error.set(2);
				return 0;
			}
			return (byte) (negative ? acc : -acc);
		} else
			return 0;
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 16 bits, adds an error code to a pointer should 
	 * anything go wrong during the conversion.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * <li>An integer pointer for passing error codes with the value of
	 * zero.</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the pointer points too a nonzero error code, and the method
	 * returns no errors, the error code will remain identical.</li>
	 * <li>If the string contains non-numeral chars, then the value
	 * of zero will be returned and the error code will be set too one.</li>
	 * <li>If the string represents and integer that cannot fit in 16 bits,
	 * then zero will be returned and the error code will be set to 
	 * two.</li>
	 * </ul>
	 * Returns: a short with the value that the character string
	 * represented. If the method returns successfully an error
	 * code of zero will remain in the pointer.
	 */
	public static short stringCheckShort(final char[] chars, final Pointer<Integer> error)
	{
		return stringCheckShort(chars, 0, chars.length, error);
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 16 bits, adds an error code to a pointer should 
	 * anything go wrong during the conversion.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * <li>An integer pointing to the position of the first numeral char</li>
	 * <li>An integer showing how many digits the number is</li>
	 * <li>An integer pointer for passing error codes with the value of
	 * zero.</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the pointer points too a nonzero error code, and the method
	 * returns no errors, the error code will remain identical.</li>
	 * <li>If the string contains non-numeral chars, then the value
	 * of zero will be returned and the error code will be set too one.</li>
	 * <li>If the string represents and integer that cannot fit in 16 bits,
	 * then zero will be returned and the error code will be set to 
	 * two.</li>
	 * </ul>
	 * Returns: a short with the value that the character string
	 * represented. If the method returns successfully an error
	 * code of zero will remain in the pointer.
	 */
	public static short stringCheckShort(final char[] chars, int start, int len, final Pointer<Integer> error)
	{
		if(chars.length > 0)
		{
			short acc = 0;
			boolean negative = false;
			if(chars[start] == '-')
			{
				negative = true;
				start++;
				len--;
			}
			while(len > 0 && chars[start] == '0') {
				start++;
				len--;
			}
			while(len-- > 0)
			{
				final char c = chars[start];
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
				start++;
			}
			if(acc == -32768 && !negative) {
				error.set(2);
				return 0;
			}
			return (short) (negative ? acc : -acc);
		} else
			return 0;
	}

	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 32 bits, adds an error code to a pointer should 
	 * anything go wrong during the conversion.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * <li>An integer pointer for passing error codes with the value of
	 * zero.</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the pointer points too a nonzero error code, and the method
	 * returns no errors, the error code will remain identical.</li>
	 * <li>If the string contains non-numeral chars, then the value
	 * of zero will be returned and the error code will be set too one.</li>
	 * <li>If the string represents and integer that cannot fit in 32 bits,
	 * then zero will be returned and the error code will be set to 
	 * two.</li>
	 * </ul>
	 * Returns: a int with the value that the character string
	 * represented. If the method returns successfully an error
	 * code of zero will remain in the pointer.
	 */
	public static int stringCheckInt(final char[] chars, final Pointer<Integer> error)
	{
		return stringCheckInt(chars, 0, chars.length, error);
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 32 bits, adds an error code to a pointer should 
	 * anything go wrong during the conversion.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * <li>An integer pointing to the position of the first numeral char</li>
	 * <li>An integer showing how many digits the number is</li>
	 * <li>An integer pointer for passing error codes with the value of
	 * zero.</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the pointer points too a nonzero error code, and the method
	 * returns no errors, the error code will remain identical.</li>
	 * <li>If the string contains non-numeral chars, then the value
	 * of zero will be returned and the error code will be set too one.</li>
	 * <li>If the string represents and integer that cannot fit in 32 bits,
	 * then zero will be returned and the error code will be set to 
	 * two.</li>
	 * </ul>
	 * Returns: a int with the value that the character string
	 * represented. If the method returns successfully an error
	 * code of zero will remain in the pointer.
	 */
	public static int stringCheckInt(final char[] chars, int start, int len, final Pointer<Integer> error)
	{
		if(chars.length > 0)
		{
			int acc = 0;
			boolean negative = false;
			if(chars[start] == '-')
			{
				negative = true;
				start++;
				len--;
			}
			while(len > 0 && chars[start] == '0') {
				start++;
				len--;
			}
			while(len-- > 0)
			{
				final char c = chars[start];
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
				start++;
			}
			if(acc == 0x80000000 && !negative) {
				error.set(2);
				return 0;
			}
			return negative ? acc : -acc;
		} else
			return 0;
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 64 bits, adds an error code to a pointer should 
	 * anything go wrong during the conversion.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * <li>An integer pointer for passing error codes with the value of
	 * zero.</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the pointer points too a nonzero error code, and the method
	 * returns no errors, the error code will remain identical.</li>
	 * <li>If the string contains non-numeral chars, then the value
	 * of zero will be returned and the error code will be set too one.</li>
	 * <li>If the string represents and integer that cannot fit in 64 bits,
	 * then zero will be returned and the error code will be set to 
	 * two.</li>
	 * </ul>
	 * Returns: a long with the value that the character string
	 * represented. If the method returns successfully an error
	 * code of zero will remain in the pointer.
	 */
	public static long stringCheckLong(final char[] chars, final Pointer<Integer> error)
	{
		return stringCheckLong(chars, 0, chars.length, error);
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 64 bits, adds an error code to a pointer should 
	 * anything go wrong during the conversion.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An array of chars representing an integer</li>
	 * <li>An integer pointing to the position of the first numeral char</li>
	 * <li>An integer showing how many digits the number is</li>
	 * <li>An integer pointer for passing error codes with the value of
	 * zero.</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the pointer points too a nonzero error code, and the method
	 * returns no errors, the error code will remain identical.</li>
	 * <li>If the string contains non-numeral chars, then the value
	 * of zero will be returned and the error code will be set too one.</li>
	 * <li>If the string represents and integer that cannot fit in 64 bits,
	 * then zero will be returned and the error code will be set to 
	 * two.</li>
	 * </ul>
	 * Returns: a long with the value that the character string
	 * represented. If the method returns successfully an error
	 * code of zero will remain in the pointer.
	 */
	public static long stringCheckLong(final char[] chars, int start, int len, final Pointer<Integer> error)
	{
		if(chars.length > 0)
		{
			long acc = 0;
			boolean negative = false;
			if(chars[start] == '-')
			{
				negative = true;
				start++;
				len--;
			}
			while(len > 0 && chars[start] == '0') {
				start++;
				len--;
			}
			while(len-- > 0)
			{
				final char c = chars[start];
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
				start++;
			}
			if(acc == 0x8000000000000000L && !negative) {
				error.set(2);
				return 0;
			}
			return negative ? acc : -acc;
		} else
			return 0;
	}
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 8 bits, adds an error code to a pointer should 
	 * anything go wrong during the conversion.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An string representing an integer</li>
	 * <li>An integer pointer for passing error codes with the value of
	 * zero.</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the pointer points too a nonzero error code, and the method
	 * returns no errors, the error code will remain identical.</li>
	 * <li>If the string contains non-numeral chars, then the value
	 * of zero will be returned and the error code will be set too one.</li>
	 * <li>If the string represents and integer that cannot fit in 8 bits,
	 * then zero will be returned and the error code will be set to 
	 * two.</li>
	 * </ul>
	 * Returns: a byte with the value that the character string
	 * represented. If the method returns successfully an error
	 * code of zero will remain in the pointer.
	 */
	public static byte stringCheckByte(final String chars, final Pointer<Integer> error)
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
			while(pos < chars.length() && chars.charAt(pos) == '0')
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
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 16 bits, adds an error code to a pointer should 
	 * anything go wrong during the conversion.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An string representing an integer</li>
	 * <li>An integer pointer for passing error codes with the value of
	 * zero.</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the pointer points too a nonzero error code, and the method
	 * returns no errors, the error code will remain identical.</li>
	 * <li>If the string contains non-numeral chars, then the value
	 * of zero will be returned and the error code will be set too one.</li>
	 * <li>If the string represents and integer that cannot fit in 16 bits,
	 * then zero will be returned and the error code will be set to 
	 * two.</li>
	 * </ul>
	 * Returns: a short with the value that the character string
	 * represented. If the method returns successfully an error
	 * code of zero will remain in the pointer.
	 */
	public static short stringCheckShort(final String chars, final Pointer<Integer> error)
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
			while(pos < chars.length() && chars.charAt(pos) == '0')
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
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 32 bits, adds an error code to a pointer should 
	 * anything go wrong during the conversion.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An string representing an integer</li>
	 * <li>An integer pointer for passing error codes with the value of
	 * zero.</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the pointer points too a nonzero error code, and the method
	 * returns no errors, the error code will remain identical.</li>
	 * <li>If the string contains non-numeral chars, then the value
	 * of zero will be returned and the error code will be set too one.</li>
	 * <li>If the string represents and integer that cannot fit in 32 bits,
	 * then zero will be returned and the error code will be set to 
	 * two.</li>
	 * </ul>
	 * Returns: a int with the value that the character string
	 * represented. If the method returns successfully an error
	 * code of zero will remain in the pointer.
	 */
	public static int stringCheckInt(final String chars,final Pointer<Integer> error)
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
			while(pos < chars.length() && chars.charAt(pos) == '0')
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
	
	/**
	 * Converts a string of human-readable chars into a machine
	 * integer of 64 bits, adds an error code to a pointer should 
	 * anything go wrong during the conversion.
	 * <br><br>
	 * Expects: 
	 * <ul>
	 * <li>An string representing an integer</li>
	 * <li>An integer pointer for passing error codes with the value of
	 * zero.</li>
	 * </ul>
	 * Exceptions:
	 * <ul>
	 * <li>If the pointer points too a nonzero error code, and the method
	 * returns no errors, the error code will remain identical.</li>
	 * <li>If the string contains non-numeral chars, then the value
	 * of zero will be returned and the error code will be set too one.</li>
	 * <li>If the string represents and integer that cannot fit in 64 bits,
	 * then zero will be returned and the error code will be set to 
	 * two.</li>
	 * </ul>
	 * Returns: a long with the value that the character string
	 * represented. If the method returns successfully an error
	 * code of zero will remain in the pointer.
	 */
	public static long stringCheckLong(final String chars, final Pointer<Integer> error)
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
			while(pos < chars.length() && chars.charAt(pos) == '0')
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
	
	/**
	 * Converts a long integer too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A long integer of any value</li>
	 * <li>An array of chars with a minimum length of twenty</li>
	 * </ul>
	 * If the array has a length less then four an ArrayIndexOutOfBounds
	 * exception may occur.
	 * behavior.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromLong(long ger, final char[] chars)
	{
		if(ger == 0x8000000000000000L)
			return new char[] {'-', '9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '4', '7', '7', '5', '8', '0', '8' };
		final boolean negative;
		int i = chars.length - 1;
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

	/**
	 * Converts a integer too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A integer of any value</li>
	 * <li>An array of chars with a minimum length of eleven</li>
	 * </ul>
	 * If the array has a length less then four an ArrayIndexOutOfBounds
	 * exception may occur.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromInt(int ger, final char[] chars)
	{
		if(ger == 0x80000000)
			return new char[] {'-', '2', '1', '4', '7', '4', '8', '3', '6', '4', '8' };
		final boolean negative;
		int i = chars.length - 1;
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

	/**
	 * Converts a short integer too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A short integer of any value</li>
	 * <li>An array of chars with a minimum length of six</li>
	 * </ul>
	 * If the array has a length less then four an ArrayIndexOutOfBounds
	 * exception may occur.
	 * behavior.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromShort(short ger, final char[] chars)
	{
		if(ger == 0x8000)
			return new char[] {'-', '3', '2', '7', '6', '8' };
		final boolean negative;
		int i = chars.length - 1;
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

	/**
	 * Converts a byte too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A byte of any value</li>
	 * <li>An array of chars with a minimum length of four</li>
	 * </ul>
	 * If the array has a length less then four an ArrayIndexOutOfBounds
	 * exception may occur.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromByte(byte ger, final char[] chars)
	{
		if(ger == 0x80)
			return new char[] {'-', '1', '2', '8'};
		final boolean negative;
		int i = chars.length - 1;
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
	
	/**
	 * Converts a long integer too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A long integer of any value</li>
	 * </ul>
	 * This method is safe and will not result in exceptions or undefined
	 * behavior.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromLong(long ger)
	{
		if(ger == 0x8000000000000000L)
			return new char[] {'-', '9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '4', '7', '7', '5', '8', '0', '8' };
		final char[] chars = new char[20];
		final boolean negative;
		int i = chars.length - 1;
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

	/**
	 * Converts a integer too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A integer of any value</li>
	 * </ul>
	 * This method is safe and will not result in exceptions or undefined
	 * behavior.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromInt(int ger)
	{
		if(ger == 0x80000000)
			return new char[] {'-', '2', '1', '4', '7', '4', '8', '3', '6', '4', '8' };
		final char[] chars = new char[11];
		final boolean negative;
		int i = chars.length - 1;
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

	/**
	 * Converts a short integer too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A short integer of any value</li>
	 * </ul>
	 * This method is safe and will not result in exceptions or undefined
	 * behavior.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromShort(short ger)
	{
		if(ger == 0x8000)
			return new char[] {'-', '3', '2', '7', '6', '8' };
		final char[] chars = new char[6];
		final boolean negative;
		int i = chars.length - 1;
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

	/**
	 * Converts a byte too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A byte of any value</li>
	 * </ul>
	 * This method is safe and will not result in exceptions or undefined
	 * behavior.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromByte(byte ger)
	{
		if(ger == 0x80)
			return new char[] {'-', '1', '2', '8'};
		final char[] chars = new char[4];
		final boolean negative;
		int i = chars.length - 1;
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
	
	/**
	 * Converts an unsigned long integer too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A long integer of any value</li>
	 * <li>An array of chars with a minimum length of twenty</li>
	 * </ul>
	 * If the array has a length less then four an ArrayIndexOutOfBounds
	 * exception may occur.
	 * behavior.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromLongUn(long ger, final char[] chars)
	{
		int i = chars.length - 1;
		if(ger < 10) {
			Pointer<Long> p = new Pointer<Long>(0L);
			ger = MathHelper.dividemodC(ger, 10, p);
			chars[i--] = (char) (48 + p.get());
		}
		while(ger > 10)
		{
			chars[i--] = (char) (48 + (ger % 10));
			ger /= 10;
		}
		chars[i] = (char) (48 + ger);
		return CString.newFrom(chars, i);
	}

	/**
	 * Converts an unsigned integer too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A integer of any value</li>
	 * <li>An array of chars with a minimum length of eleven</li>
	 * </ul>
	 * If the array has a length less then four an ArrayIndexOutOfBounds
	 * exception may occur.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromIntUn(int uger, final char[] chars)
	{
		long ger = uger;
		int i = chars.length - 1;
		while(ger >= 10)
		{
			chars[i--] = (char) (48 + (ger % 10));
			ger /= 10;
		}
		chars[i] = (char) (48 + ger);
		return CString.newFrom(chars, i);
	}

	/**
	 * Converts an unsigned short integer too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A short integer of any value</li>
	 * <li>An array of chars with a minimum length of six</li>
	 * </ul>
	 * If the array has a length less then four an ArrayIndexOutOfBounds
	 * exception may occur.
	 * behavior.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromShortUn(short uger, final char[] chars)
	{
		int ger = (short) (uger & 0xFFFF);
		int i = chars.length - 1;
		while(ger >= 10)
		{
			chars[i--] = (char) (48 + (ger % 10));
			ger /= 10;
		}
		chars[i] = (char) (48 + ger);
		return CString.newFrom(chars, i);
	}

	/**
	 * Converts an unsigned byte too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A byte of any value</li>
	 * <li>An array of chars with a minimum length of four</li>
	 * </ul>
	 * If the array has a length less then four an ArrayIndexOutOfBounds
	 * exception may occur.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromByteUn(byte uger, final char[] chars)
	{
		short ger = (short) (uger & 0xFF);
		int i = chars.length - 1;
		while(ger >= 10)
		{
			chars[i--] = (char) (48 + (ger % 10));
			ger /= 10;
		}
		chars[i] = (char) (48 + ger);
		return CString.newFrom(chars, i);
	}
	
	/**
	 * Converts an unsigned long integer too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A long integer of any value</li>
	 * </ul>
	 * This method is safe and will not result in exceptions or undefined
	 * behavior.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromLongUn(long ger)
	{
		final char[] chars = new char[20];
		int i = 19;
		if(ger < 10) {
			Pointer<Long> p = new Pointer<Long>(0L);
			ger = MathHelper.dividemodC(ger, 10, p);
			chars[i--] = (char) (48 + p.get());
		}
		while(ger > 10)
		{
			chars[i--] = (char) (48 + (ger % 10));
			ger /= 10;
		}
		chars[i] = (char) (48 + ger);
		return CString.newFrom(chars, i);
	}

	/**
	 * Converts an unsigned integer too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A integer of any value</li>
	 * </ul>
	 * This method is safe and will not result in exceptions or undefined
	 * behavior.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromIntUn(final int uger)
	{
		long ger = uger;
		final char[] chars = new char[10];
		int i = 9;
		while(ger >= 10)
		{
			chars[i--] = (char) (48 + (ger % 10));
			ger /= 10;
		}
		chars[i] = (char) (48 + ger);
		return CString.newFrom(chars, i);
	}

	/**
	 * Converts an unsigned short integer too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A short integer of any value</li>
	 * </ul>
	 * This method is safe and will not result in exceptions or undefined
	 * behavior.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromShortUn(final short uger)
	{
		int ger = (short) (uger & 0xFFFF);
		final char[] chars = new char[5];
		int i = 4;
		while(ger >= 10)
		{
			chars[i--] = (char) (48 + (ger % 10));
			ger /= 10;
		}
		chars[i] = (char) (48 + ger);
		return CString.newFrom(chars, i);
	}
	
	/**
	 * Converts an unsigned byte too a string of chars representing it.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A byte of any value</li>
	 * </ul>
	 * This method is safe and will not result in exceptions or undefined
	 * behavior.
	 * <br><br>
	 * Returns: a string representing the integer passed.
	 */
	public static char[] fromByteUn(final byte uger)
	{
		short ger = (short) (uger & 0xFF);
		final char[] chars = new char[3];
		int i = 2;
		while(ger >= 10)
		{
			chars[i--] = (char) (48 + (ger % 10));
			ger /= 10;
		}
		chars[i] = (char) (48 + ger);
		return CString.newFrom(chars, i);
	}
	
	/**
	 * Checks if a string represents a number or not.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An array of chars that you want to check</li>
	 * </ul>
	 * This method is safe and will work normally with any
	 * non-null values.
	 * <br><br>
	 * Returns: true if the chars represent a number
	 */
	public static boolean isBase10(final char[] chars)
	{
		if(chars.length == 0)
			return false;
		int i = 0;
		if(chars[i] == '-')
			i++;
		while(i < chars.length)
			if(chars[i] < '0' || chars[i++] > '9')
				return false;
		return true;
	}

	/**
	 * Checks if a string represents a number or not.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An string that you want to check</li>
	 * </ul>
	 * This method is safe and will work normally with any
	 * non-null values.
	 * <br><br>
	 * Returns: true if the chars represent a number
	 */
	public static boolean isBase10(final String s)
	{
		if(s.length() == 0)
			return false;
		for(int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if(c < '0' || c > '9')
				return false;
		}
		return true;
	}
	
	/**
	 * Checks if a string represents a number or not.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An string that you want to check</li>
	 * </ul>
	 * This method is safe and will work normally with any
	 * non-null values.
	 * <br><br>
	 * Returns: true if the chars represent a number
	 */
	public static boolean isBase10(final CString s)
	{
		if(s.length() == 0)
			return false;
		for(final char c : s.array())
			if(c < '0' || c > '9')
				return false;
		return true;
	}
	
	/**
	 * Checks if a string represents a number or not.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An array of chars that you want to check</li>
	 * </ul>
	 * This method is safe and will work normally with any
	 * non-null values.
	 * <br><br>
	 * Returns: true if the chars represent a number
	 */
	public static boolean isBase10Un(final char[] chars)
	{
		if(chars.length == 0)
			return false;
		for(final char c : chars)
			if(c < '0' || c > '9')
				return false;
		return true;
	}

	/**
	 * Checks if a string represents a number or not.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An string that you want to check</li>
	 * </ul>
	 * This method is safe and will work normally with any
	 * non-null values.
	 * <br><br>
	 * Returns: true if the chars represent a number
	 */
	public static boolean isBase10Un(final String s)
	{
		if(s.length() == 0)
			return false;
		int i = 0;
		if(s.charAt(i) == '-')
			i++;
		for(; i < s.length(); i++) {
			final char c = s.charAt(i);
			if(c < '0' || c > '9')
				return false;
		}
		return true;
	}
	
	/**
	 * Checks if a string represents a number or not.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An string that you want to check</li>
	 * </ul>
	 * This method is safe and will work normally with any
	 * non-null values.
	 * <br><br>
	 * Returns: true if the chars represent a number
	 */
	public static boolean isBase10Un(final CString s)
	{
		if(s.length() == 0)
			return false;
		final char[] chars = s.array();
		int i = 0;
		if(chars[i] == '-')
			i++;
		while(i < chars.length)
			if(chars[i] < '0' || chars[i++] > '9')
				return false;
		return true;
	}

}

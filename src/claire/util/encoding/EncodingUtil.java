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
	
	public static int findFirst(char c, char[] chars)
	{
		for(int i = 0; i < chars.length; i++)
			if(c == chars[i])
				return i;
		return -1;
	}
	
	public static int findLast(char c, char[] chars)
	{
		int i = chars.length;
		while(i > 0)
			if(c == chars[--i])
				return i;
		return -1;
	}
	
	public static int findFirst(char c, String chars)
	{
		for(int i = 0; i < chars.length(); i++)
			if(c == chars.charAt(i))
				return i;
		return -1;
	}
	
	public static int findLast(char c, String chars)
	{
		int i = chars.length();
		while(i > 0)
			if(c == chars.charAt(--i))
				return i;
		return -1;
	}
	
	/**
	 * including: if true it removes the target char aswell
	 * after: if true it removes everything after the target char as opposed to before
	 */
	public static char[] removeFirst(char[] chars, char c, boolean after, boolean including)
	{
		int i = 0;
		for(; i < chars.length; i++)
			if(c == chars[i])
				break;		
		final char[] n;
		if(after) {
			if(!including)
				i++;
			n = new char[i];
			System.arraycopy(chars, 0, n, 0, i);
		} else {
			if(including)
				i++;
			n = new char[chars.length - i];
			System.arraycopy(chars, i, n, 0, n.length);
		}
		return n;
	}
	
	/**
	 * including: if true it removes the target char aswell
	 * after: if true it removes everything after the target char as opposed to before
	 */
	public static char[] removeLast(char[] chars, char c, boolean after, boolean including)
	{
		int i = chars.length;
		while(--i >= 0)
			if(c == chars[i])
				break;		
		final char[] n;
		if(after) {
			if(!including)
				i++;
			n = new char[i];
			System.arraycopy(chars, 0, n, 0, i);
		} else {
			if(including)
				i++;
			n = new char[chars.length - i];
			System.arraycopy(chars, i, n, 0, n.length);
		}
		return n;
	}
	
	public static boolean repeatSlow(char[] chars)
	{
		for(int i = 0; i < chars.length; i++)
		{
			final char c = chars[i];
			for(int j = i + 1; j < chars.length; j++)
				if(chars[j] == c)
					return true;
		}
		return false;
	}

	public static boolean repeatSlow(String chars)
	{
		for(int i = 0; i < chars.length(); i++)
		{
			final char c = chars.charAt(i);
			for(int j = i + 1; j < chars.length(); j++)
				if(chars.charAt(j) == c)
					return true;
		}
		return false;
	}
	
	public static boolean contains(char[] c1, char[] c2)
	{
		for(char c3 : c1) 
			for(char c4 : c2)
				if(c3 == c4)
					return true;
		return false;
	}

	public static boolean contains(String s, char[] c2)
	{
		for(int i = 0; i < s.length(); i++) { 
			char c3 = s.charAt(i);
			for(char c4 : c2)
				if(c3 == c4)
					return true;
		}
		return false;
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

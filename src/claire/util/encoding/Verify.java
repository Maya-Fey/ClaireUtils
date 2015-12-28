package claire.util.encoding;

public final class Verify {

	public static boolean containsOnly(char[] text, char[] alphabet)
	{
		for(char c1 : text) {
			boolean vio = true;
			for(char c2 : alphabet)
				if(c1 == c2) {
					vio ^= true;
					break;
				}
			if(vio)
				return false;
		}
		return true;
	}
	
	public static boolean containsOnly(char[] text, int start0, int len0, char[] alphabet, int start1, int len1)
	{
		while(start0 < len0) {
			char c1 = text[start0++];
			boolean vio = true;
			for(int i = start1; i < len1; i++)
				if(c1 == alphabet[i]) {
					vio ^= true;
					break;
				}
			if(vio)
				return false;
		}
		return true;
	}
	
	public static boolean containsOnly(String s, char[] alphabet)
	{
		return containsOnly(s.toCharArray(), alphabet);
	}
	
	public static boolean containsOnly(CString s, char[] alphabet)
	{
		return containsOnly(s.array(), alphabet);
	}
	
	public static boolean range(int test, int min, int max)
	{
		if(test < min)
			return false;
		if(test > max)
			return false;
		return true;
	}
	
	public static boolean isInt(char[] chars, int start0, int len0)
	{
		if(chars[start0] == '-') 
			if(len0 > 1)
				start0++;
			else
				return false;
		else;
		while(start0 < len0)
		{
			char c = chars[start0++];
			if(c < '0')
				return false;
			if(c > '9')
				return false;
		}
		return true;
	}
	
	public static boolean isInt(char[] chars)
	{
		int i = 0;
		if(chars[i] == '-') 
			if(chars.length > 1)
				i++;
			else
				return false;
		else;
		while(i < chars.length)
		{
			char c = chars[i++];
			if(c < '0')
				return false;
			if(c > '9')
				return false;
		}
		return true;
	}
	
	public static boolean isInt(String s)
	{
		return isInt(s.toCharArray());
	}
	
	public static boolean isInt(CString s)
	{
		return isInt(s.array());
	}
	
}

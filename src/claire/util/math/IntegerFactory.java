package claire.util.math;

import claire.util.encoding.CString;
import claire.util.encoding.PartialString;
import claire.util.standards.IInteger;

public abstract class IntegerFactory<Int extends IInteger<?>> {
	
	public abstract Int construct(int len);
	public abstract Int construct(int[] ints);
	public abstract Int construct(char[] chars, int start, int len, int len2);
	
	public Int construct(char[] chars, int len)
	{
		return construct(chars, 0, chars.length, len);
	}
	
	public Int construct(String s, int len)
	{
		return construct(s.toCharArray(), 0, s.length(), len);
	}
	
	public Int construct(CString s, int len)
	{
		return construct(s.array(), 0, s.length(), len);
	}
	
	public Int construct(PartialString s, int len)
	{
		return construct(s.array(), s.getOffset(), s.getLength(), len);
	}

}

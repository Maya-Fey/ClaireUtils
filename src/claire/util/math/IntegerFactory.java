package claire.util.math;

import claire.util.standards.IInteger;

public abstract class IntegerFactory<Int extends IInteger<?>> {
	
	public abstract Int construct(int len);
	public abstract Int construct(int[] ints);
	public abstract Int construct(char[] chars, int start, int len);
	
	public Int construct(char[] chars)
	{
		return construct(chars, 0, chars.length);
	}

}

package claire.util.math.primitive;

import claire.util.encoding.CString;

public class UInt1024 
	   extends UInt {
	
	public UInt1024()
	{
		super(32);
	}
	
	public UInt1024(String s)
	{
		super(s, 32);
	}
	
	public UInt1024(CString s)
	{
		super(s, 32);
	}
	
	public UInt1024(int[] ints)
	{
		super(ints);
	}
	
}
	
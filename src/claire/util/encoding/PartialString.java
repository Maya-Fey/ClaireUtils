package claire.util.encoding;

import claire.util.memory.util.ArrayUtil;
import claire.util.standards.CObject;
import claire.util.standards.IUUID;
import claire.util.standards._NAMESPACE;

public class PartialString 
	   implements IUUID<PartialString> {

	private int off, len;
	
	private char[] chars;
	
	public PartialString(char[] chars, int off, int len)
	{
		this.len = len;
		this.off = off;
		this.chars = chars;
	}
	
	public void redefine(int off, int len)
	{
		this.off = off;
		this.len = len;
	}
	
	public int getLength()
	{
		return this.len;
	}
	
	public char[] array()
	{
		return chars;
	}
	
	public char[] toCharArray()
	{
		return ArrayUtil.subArr(chars, off, len);
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.PARTIALSTRING;
	}

	public boolean sameAs(PartialString obj)
	{
		return chars == obj.chars && (off == obj.off && len == obj.len); 
	}
	
}

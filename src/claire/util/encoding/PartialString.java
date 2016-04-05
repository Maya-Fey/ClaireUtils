package claire.util.encoding;

import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IReferrable;
import claire.util.standards.IUUID;
import claire.util.standards._NAMESPACE;

public class PartialString 
	   implements IUUID<PartialString>,
	   			  IDeepClonable<PartialString>,
	   			  IReferrable<PartialString> {

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
	
	public boolean equals(String s)
	{
		if(s.length() == len) {
			int i = 0;
			int j = off;
			int k = len;
			while(k-- > 0)
				if(chars[j++] != s.charAt(i++))
					return false;
		} else
			return false;
		return true;
	}
	
	public boolean equals(char[] arr)
	{
		if(arr.length == len) {
			int i = 0;
			int j = off;
			int k = len;
			while(k-- > 0)
				if(chars[j++] != arr[i++])
					return false;
		} else
			return false;
		return true;
	}
	
	public boolean equals(char[] arr, int start, int length)
	{
		if(length == len) {
			int i = start;
			int j = off;
			int k = len;
			while(k-- > 0)
				if(chars[j++] !=  arr[i++])
					return false;
		} else
			return false;
		return true;
	}

	public PartialString createDeepClone()
	{
		return new PartialString(chars, off, len);
	}
	
}

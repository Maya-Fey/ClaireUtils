package claire.util.crypto;

import claire.util.encoding.PartialString;

public class CryptoString 
	   extends PartialString {

	private final char sep;
	
	private int a = 0,
			    b;
	
	private final int end = this.getEnd();
	
	public CryptoString(char[] chars, int off, int len, char sep) 
	{
		super(chars, off, len);
		this.sep = sep;
		b = this.next(-1, sep);
	}
	
	public int args()
	{
		return this.count(sep);
	}
	
	public boolean hasNext()
	{
		return a != -1;
	}
	
	public PartialString nextArg()
	{
		try {
			return this.substr(a, b - a);
		} finally {
			if(b == end)
				a = -1;
			else {
				a = b + 1;
				if((b = this.next(a, sep)) == -1) 
					b = end;
			}
		}
	}
	
	public CryptoString nextLevel(char sep)
	{
		try {
			return new CryptoString(this.chars, this.off + a, b - a, sep);
		} finally {
			if(b == end)
				a = -1;
			else {
				a = b + 1;
				if((b = this.next(a, sep)) == -1) 
					b = end;
			}
		}
	}

}

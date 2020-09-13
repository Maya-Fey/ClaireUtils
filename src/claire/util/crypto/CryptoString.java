package claire.util.crypto;

import claire.util.encoding.PartialString;

public class CryptoString 
	   extends PartialString {
	
	private static final char[] levels = new char[] {
		'_', '-', '~', '/', '.'
	};

	private final char sep;
	
	private int next;
	
	private final int end = this.getEnd();
	
	public CryptoString(char[] chars, int off, int len, int level) 
	{
		super(chars, off, len);
		this.sep = levels[level];
		next = this.next(-1, sep);
		if(next == -1)
			next = end;
	}
	
	public int args()
	{
		return this.count(sep) + (this.hasNext() ? 1 : 0);
	}
	
	public boolean hasNext()
	{
		return len > 0;
	}
	
	public PartialString nextArg()
	{
		try {
			return this.substr(0, next - off);
		} finally {
			len -= next - off + 1;
			off = next + 1;
			if((next = this.next(0, sep)) == -1) 
				next = end;
		}
	}
	
	public CryptoString nextLevel()
	{
		char sep = ' ';
		for(int i = 0; i < levels.length; i++)
			if(this.sep == levels[i]) {
				sep = levels[++i];
				break;
			}
		try {
			return new CryptoString(this.chars, off, next - off, sep);
		} finally {
			len -= next - off + 1;
			off = next + 1;
			if((next = this.next(0, sep)) == -1) 
				next = end;
		}
	}

}

package claire.util.crypto.hash.primitive;

import claire.util.encoding.CString;
import claire.util.standards.crypto.IHash;

public class SHA2 
	   implements IHash {
	
	private IHash md;
	
	public SHA2(int length) 
	{
		switch(length)
		{
			case 224:
				this.md = new SHA2_224(); 
				break;
			case 256:
				this.md = new SHA2_256(); 
				break;
			case 384:
				this.md = new SHA2_384(); 
				break;
			case 512:
				this.md = new SHA2_512(); 
				break;
			default:
				throw new java.lang.IllegalArgumentException("SHA-2 only supports hash lengths of 224, 256, 384, or 512 bits");
		}
	}
	
	public SHA2(CString length)
	{
		this(length.toInt());
	}
	
	public SHA2(String length)
	{
		this(Integer.parseInt(length));
	}
	
	public void add(byte[] bytes, int start, int length)
	{
		md.add(bytes, start, length);
	}

	public void finish(byte[] out, int start)
	{
		md.finish(out, start);
	}
	
	public int outputLength()
	{
		return md.outputLength();
	}

}

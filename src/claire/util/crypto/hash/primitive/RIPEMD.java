package claire.util.crypto.hash.primitive;

import claire.util.standards.crypto.IHash;

public class RIPEMD 
	   implements IHash {
	
	private final MerkleHash md;

	public RIPEMD(int length) 
	{
		switch(length)
		{
			case 128:
				this.md = new RIPEMD128(); 
				break;
			case 160:
				this.md = new RIPEMD160(); 
				break;
			case 256:
				this.md = new RIPEMD256(); 
				break;
			case 320:
				this.md = new RIPEMD320(); 
				break;
			default:
				throw new java.lang.IllegalArgumentException("RIPEMD only supports hash lengths of 128, 160, 256, or 320 bits");
		}
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

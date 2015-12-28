package claire.util.crypto.hash.primitive;

import claire.util.standards.crypto.IHash;

public class BLAKE implements IHash {

	private final IHash blake;
	
	public BLAKE(int size) 
	{
		switch(size)
		{
			case 224:
				blake = new BLAKE224();
				break;
			case 256:
				blake = new BLAKE256();
				break;
			case 384:
				blake = new BLAKE384();
				break;
			case 512:
				blake = new BLAKE512();
				break;
			default:
				throw new java.lang.IllegalArgumentException("BLAKE only supports digest sizes of 224, 256, 384, or 512 bits.");
		}
	}

	public void add(byte[] bytes, int start, int length)
	{
		blake.add(bytes, start, length);
	}

	public void finish(byte[] out, int start)
	{
		blake.finish(out, start);
	}

	public int outputLength()
	{
		return blake.outputLength();
	}

}

package claire.util.crypto.hash.primitive;

import claire.util.standards.crypto.IHash;

public class CRC implements IHash {
	
	private final IHash crc;
	
	public CRC(int len)
	{
		switch(len)
		{
			case 8:
				crc = new CRC8();
				break;
			case 16:
				crc = new CRC16();
				break;
			case 32:
				crc = new CRC32();
				break;
			default: 
				throw new java.lang.IllegalArgumentException();
		}
	}

	public void add(byte[] bytes, int start, int length)
	{
		crc.add(bytes, start, length);
	}

	public void finish(byte[] out, int start)
	{
		crc.finish(out, start);
	}

	public int outputLength()
	{
		return crc.outputLength();
	}

}

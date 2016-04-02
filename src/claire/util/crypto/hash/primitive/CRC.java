package claire.util.crypto.hash.primitive;

import claire.util.standards.crypto.IHash;

public class CRC  {
	
	public IHash<?, ?> getCRC(int len)
	{
		switch(len)
		{
			case 8:
				return new CRC8();
			case 16:
				return new CRC16();
			case 32:
				return new CRC32();
			default: 
				throw new java.lang.IllegalArgumentException();
		}
	}

}

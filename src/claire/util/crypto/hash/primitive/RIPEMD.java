package claire.util.crypto.hash.primitive;

import claire.util.standards.crypto.IHash;

public class RIPEMD {

	public IHash<?> getRIPEMD(int length) 
	{
		switch(length)
		{
			case 128:
					return new RIPEMD128(); 
				case 160:
					return new RIPEMD160(); 
				case 256:
					return new RIPEMD256(); 
				case 320:
					return new RIPEMD320(); 
				default:
					throw new java.lang.IllegalArgumentException("RIPEMD only supports hash lengths of 128, 160, 256, or 320 bits");
		}
	}
	
}

package claire.util.crypto.hash.primitive;

import claire.util.crypto.hash.HashFactory;
import claire.util.standards.crypto.IHash;

public class BLAKE {

	public IHash<?, ?> getBlake(int size) 
	{
		switch(size)
		{
			case 224:
				return new BLAKE224();
			case 256:
				return new BLAKE256();
			case 384:
				return new BLAKE384();
			case 512:
				return new BLAKE512();
			default:
				throw new java.lang.IllegalArgumentException("BLAKE only supports digest sizes of 224, 256, 384, or 512 bits.");
		}
	}
	
	public static HashFactory<BLAKECore<?, ?>> factory()
	{
		return factory;
	}
	
	public static final BLAKEFactory factory = new BLAKEFactory();
	
	public static final class BLAKEFactory extends HashFactory<BLAKECore<?, ?>>
	{

		public BLAKECore<?, ?> build(char[] params, char sep)
		{
			switch(new String(params))
			{
				case "224":
					return new BLAKE224();
				case "256":
					return new BLAKE256();
				case "384":
					return new BLAKE384();
				case "512":
					return new BLAKE512();
				default:
					throw new java.lang.NullPointerException();
			}
		}
		
	}

}

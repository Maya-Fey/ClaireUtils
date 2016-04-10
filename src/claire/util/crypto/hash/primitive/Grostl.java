package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.HashFactory;
import claire.util.standards.crypto.IHash;

public class Grostl {

	public IHash<?, ?> getGrostl(int size) 
	{
		switch(size)
		{
			case 224:
				return new Grostl224();
			case 256:
				return new Grostl256();
			case 384:
				return new Grostl384();
			case 512:
				return new Grostl512();
			default:
				throw new java.lang.IllegalArgumentException("Grostl only supports digest sizes of 224, 256, 384, or 512 bits.");
		}
	}
	
	public static HashFactory<GrostlCore<?, ?>> factory()
	{
		return factory;
	}
	
	public static final GrostlFactory factory = new GrostlFactory();
	
	public static final class GrostlFactory extends HashFactory<GrostlCore<?, ?>>
	{

		public GrostlCore<?, ?> build(CryptoString str)
		{
			switch(str.nextArg().toString())
			{
				case "224":
					return new Grostl224();
				case "256":
					return new Grostl256();
				case "384":
					return new Grostl384();
				case "512":
					return new Grostl512();
				default:
					throw new java.lang.NullPointerException();
			}
		}
		
	}

}

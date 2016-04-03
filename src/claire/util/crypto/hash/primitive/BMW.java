package claire.util.crypto.hash.primitive;

import claire.util.crypto.hash.HashFactory;
import claire.util.standards.crypto.IHash;

public final class BMW {
	
	public static IHash<?, ?> getBMW(int length) 
	{
		switch(length)
		{
			case 224:
				return new BMW224(); 
			case 256:
				return new BMW256(); 
			case 384:
				return new BMW384(); 
			case 512:
				return new BMW512(); 
			default:
				throw new java.lang.IllegalArgumentException("BMW only supports hash lengths of 224, 256, 384, or 512 bits");
		}
	}
	
	public static HashFactory<MerkleHash<?, ?>> factory()
	{
		return factory;
	}
	
	public static final BMWFactory factory = new BMWFactory();
	
	public static final class BMWFactory extends HashFactory<MerkleHash<?, ?>>
	{

		public MerkleHash<?, ?> build(String params)
		{
			switch(params)
			{
				case "224":
					return new BMW224();
				case "256":
					return new BMW256();
				case "384":
					return new BMW384();
				case "512":
					return new BMW512();
				default:
					throw new java.lang.NullPointerException();
			}
		}
		
	}

}

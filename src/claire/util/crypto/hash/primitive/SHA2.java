package claire.util.crypto.hash.primitive;

import claire.util.crypto.hash.HashFactory;
import claire.util.standards.crypto.IHash;

public final class SHA2 {
	
	public static IHash<?, ?> getSHA2(int length) 
	{
		switch(length)
		{
			case 224:
				return new SHA2_224(); 
			case 256:
				return new SHA2_256(); 
			case 384:
				return new SHA2_384(); 
			case 512:
				return new SHA2_512(); 
			default:
				throw new java.lang.IllegalArgumentException("SHA-2 only supports hash lengths of 224, 256, 384, or 512 bits");
		}
	}
	
	public static HashFactory<MerkleHash<?, ?>> factory()
	{
		return factory;
	}
	
	public static final SHA2_Factory factory = new SHA2_Factory();
	
	public static final class SHA2_Factory extends HashFactory<MerkleHash<?, ?>>
	{

		public MerkleHash<?, ?> build(char[] params, char sep)
		{
			switch(new String(params))
			{
				case "224":
					return new SHA2_224();
				case "256":
					return new SHA2_256();
				case "384":
					return new SHA2_384();
				case "512":
					return new SHA2_512();
				default:
					throw new java.lang.NullPointerException();
			}
		}
		
	}

}

package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.HashFactory;
import claire.util.standards.crypto.IHash;

public class SHA2Factory 
	   extends HashFactory<IHash<?, ?>> {

	public IHash<?, ?> build(CryptoString str) throws InstantiationException
	{
		if(str.args() > 2) {
			throw new java.lang.InstantiationException("Too many arguments, only output length required");
		} else if(str.args() == 0) {
			return new SHA2_256();
		} else {
			switch(str.nextArg().toString()) {
				case "224":
					return new SHA2_224();
				case "256":
					return new SHA2_256();
				case "384":
					return new SHA2_384();
				case "512":
					return new SHA2_512();
				default:
					throw new java.lang.InstantiationException("Invalid output length; SHA-2 only supports 224, 256, 384, or 512 bit output");
			}
		}
	}
	
	public static final SHA2Factory instance = new SHA2Factory();

}

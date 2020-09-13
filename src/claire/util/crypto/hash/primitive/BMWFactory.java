package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.HashFactory;
import claire.util.standards.crypto.IHash;

public class BMWFactory 
	   extends HashFactory<IHash<?, ?>> {

	public IHash<?, ?> build(CryptoString str) throws InstantiationException
	{
		if(str.args() > 2) {
			throw new java.lang.InstantiationException("Too many arguments, only output length required");
		} else if(str.args() == 0) {
			return new BMW256();
		} else {
			switch(str.nextArg().toString()) {
				case "224":
					return new BMW224();
				case "256":
					return new BMW256();
				case "384":
					return new BMW384();
				case "512":
					return new BMW512();
				default:
					throw new java.lang.InstantiationException("Invalid output length; BMW only supports 224, 256, 384, or 512 bit output");
			}
		}
	}
	
	public static final BMWFactory instance = new BMWFactory();

}

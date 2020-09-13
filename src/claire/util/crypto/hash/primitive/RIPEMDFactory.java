package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.HashFactory;
import claire.util.standards.crypto.IHash;

public class RIPEMDFactory 
	   extends HashFactory<IHash<?, ?>> {

	public IHash<?, ?> build(CryptoString str) throws InstantiationException
	{
		if(str.args() > 2) {
			throw new java.lang.InstantiationException("Too many arguments, only output length required");
		} else if(str.args() == 0) {
			return new RIPEMD160();
		} else {
			switch(str.nextArg().toString()) {
				case "128":
					return new RIPEMD128();
				case "160":
					return new RIPEMD160();
				case "256":
					return new RIPEMD256();
				case "320":
					return new RIPEMD320();
				default:
					throw new java.lang.InstantiationException("Invalid output length; RIPEMD only supports 128, 160, 256, or 320 bit output");
			}
		}
	}
	
	public static final RIPEMDFactory instance = new RIPEMDFactory();

}

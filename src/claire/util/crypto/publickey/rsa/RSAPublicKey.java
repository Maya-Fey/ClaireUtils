package claire.util.crypto.publickey.rsa;

import claire.util.math.primitive.UInt;
import claire.util.standards.IUUID;
import claire.util.standards.crypto.IPublicKey;

public abstract class RSAPublicKey<This extends RSAPublicKey<This>> 
				implements IPublicKey<This>,
						   IUUID<This> {
	
	protected abstract void crypt(UInt msg);
	protected abstract int getLen();
	protected abstract int modLen();

}

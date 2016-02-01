package claire.util.crypto.publickey.rsa;

import claire.util.math.UInt;
import claire.util.standards.IUUID;
import claire.util.standards.crypto.IPrivateKey;

public abstract class RSAPrivateKey<This extends RSAPrivateKey<This>> 
				implements IPrivateKey<This>,
						   IUUID<This> {

	protected abstract void crypt(UInt msg);
	protected abstract int getLen();
	protected abstract int modLen();

}

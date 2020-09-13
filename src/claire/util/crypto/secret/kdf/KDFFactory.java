package claire.util.crypto.secret.kdf;

import claire.util.crypto.CryptoString;

public abstract class KDFFactory<KDF extends IKDF> {
	
	public abstract KDF build(CryptoString str) throws InstantiationException;
	
}

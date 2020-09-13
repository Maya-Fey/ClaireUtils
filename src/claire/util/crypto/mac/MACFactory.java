package claire.util.crypto.mac;

import claire.util.crypto.CryptoString;

public abstract class MACFactory<MAC extends IMAC<?>> {
	
	public abstract MAC construct(CryptoString str);

}

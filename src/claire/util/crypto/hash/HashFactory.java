package claire.util.crypto.hash;

import claire.util.crypto.CryptoString;
import claire.util.standards.crypto.IHash;

@SuppressWarnings("hiding")
public abstract class HashFactory<Hash extends IHash<?, ?>> {
	
	public abstract Hash build(CryptoString str) throws InstantiationException;
	
}

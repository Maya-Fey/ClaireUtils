package claire.util.crypto.hash;

import claire.util.standards.crypto.IHash;

public abstract class HashFactory<Hash extends IHash<Hash, ?>> {
	
	public abstract Hash build(String params);

}

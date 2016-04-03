package claire.util.crypto.hash;

import claire.util.standards.crypto.IHash;

public abstract class HashFactory<Hash extends IHash<?, ?>> {
	
	public abstract Hash build(char[] params, char sep);

}

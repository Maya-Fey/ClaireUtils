package claire.util.standards.crypto;

import claire.util.standards.IRandom;

public interface ISymmetric<Key extends ISymmetricKey<Key>>
	   extends ICrypto<Key>, 
	   		   IEncrypter, 
	   		   IDecrypter {
	
	/** Generates a new key given a randomness source */
	Key newKey(IRandom rand);
	
	/** Generates a new key for this instance */
	void genKey(IRandom rand);

}

package claire.util.standards.crypto;

import claire.util.crypto.KeyFactory;

public interface ICrypto<Type extends IKey<?>> {
		
	/**
	 * Returns the key for this algorithm. For Symmetric algorithms this is the 
	 * key used to encrypt and decrypt. For Asymmetric algorithms this is the
	 * private key
	 * 
	 * @return
	 */
	Type getKey();
	
	/**
	 * Sets the key for this algorithm. For Symmetric algorithms this is the 
	 * key used to encrypt and decrypt. For Asymmetric algorithms this is the
	 * private key
	 */
	void setKey(Type t);	
	
	KeyFactory<Type> keyFactory();

}

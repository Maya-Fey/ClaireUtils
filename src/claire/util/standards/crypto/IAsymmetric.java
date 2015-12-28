package claire.util.standards.crypto;

public interface IAsymmetric<Private extends IPrivateKey<?>, 
							 Public extends IPublicKey<?>> 
	   extends ICrypto<Private>,
			   IEncrypter,
			   IVerifier,
			   IDecrypter,
			   ISigner {
	
	Public getPublicKey();
	
	void setPublicKey(Public pub);
	
	boolean signs();
	boolean encrypts();
	
}

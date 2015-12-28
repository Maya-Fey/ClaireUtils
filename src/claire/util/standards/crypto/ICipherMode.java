package claire.util.standards.crypto;

public interface ICipherMode<Key extends ISymmetricKey<Key>> 
	   extends IEncrypter, 
	   		   IDecrypter,
	   		   ICrypto<Key> {

	IEncrypter getEncrypter();
	IDecrypter getDecrypter();
	
}
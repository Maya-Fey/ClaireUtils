package claire.util.standards.crypto;

public interface ICipherMode<Key extends IKey<Key>> 
	   extends IEncrypter, 
	   		   IDecrypter,
	   		   ICrypto<Key> {

	IEncrypter getEncrypter();
	IDecrypter getDecrypter();
	
}
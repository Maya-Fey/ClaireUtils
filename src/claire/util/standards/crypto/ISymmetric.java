package claire.util.standards.crypto;

public interface ISymmetric<Key extends IKey<Key>>
	   extends ICrypto<Key>, 
	   		   IEncrypter, 
	   		   IDecrypter {
	
}

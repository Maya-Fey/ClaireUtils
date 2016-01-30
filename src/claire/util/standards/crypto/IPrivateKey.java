package claire.util.standards.crypto;

public interface IPrivateKey<Key extends IPrivateKey<Key>> 
	   extends IKey<Key> {
	
	int messageSize();
	int signatureSize();
	int plaintextSize();
	int ciphertextSize();
	
}

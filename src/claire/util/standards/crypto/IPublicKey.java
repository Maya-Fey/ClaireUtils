package claire.util.standards.crypto;

public interface IPublicKey<Key extends IPublicKey<Key>>
	   extends IKey<Key> {

	int messageSize();
	int signatureSize();
	int plaintextSize();
	int ciphertextSize();
	
}

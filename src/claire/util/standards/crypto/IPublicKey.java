package claire.util.standards.crypto;

import claire.util.crypto.cipher.StateSpaceRequiredException;

public interface IPublicKey<Key extends IPublicKey<Key>>
	   extends IKey<Key> {

	void encryptBlock(byte[] block, int start) throws StateSpaceRequiredException;
	void encryptBlock(byte[] block, int start0, byte[] out, int start1) throws StateSpaceRequiredException;
	
	boolean verify(byte[] signature, int start0, byte[] message, int start1) throws StateSpaceRequiredException;

	int messageSize();
	int signatureSize();
	int plaintextSize();
	int ciphertextSize();
	
}

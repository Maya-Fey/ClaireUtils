package claire.util.standards.crypto;

import claire.util.crypto.cipher.StateSpaceRequiredException;

public interface IPrivateKey<Key extends IPrivateKey<Key>> 
	   extends IKey<Key> {
	
	/**
	 * Decrypts a block of ciphertext in the array. Starts at index <b>start</b> and traverses
	 * the array until block length is exhausted.
	 * 
	 * @param block
	 * @throws StateSpaceRequiredException
	 */
	void decryptBlock(byte[] block, int start) throws StateSpaceRequiredException;
	void decryptBlock(byte[] block, int start0, byte[] out, int start1) throws StateSpaceRequiredException;
	
	void signBlock(byte[] block, int start) throws StateSpaceRequiredException;
	void signBlock(byte[] block, int start0, byte[] out, int start1) throws StateSpaceRequiredException;

	int messageSize();
	int signatureSize();
	int plaintextSize();
	int ciphertextSize();
	
}

package claire.util.standards.crypto;

import claire.util.crypto.cipher.StateSpaceRequiredException;

public interface ISymmetricKey<Key extends ISymmetricKey<Key>> 
	   extends IKey<Key> {
	
	void decryptBlock(byte[] block, int start) throws StateSpaceRequiredException;
	void decryptBlock(byte[] block, int start0, byte[] out, int start1) throws StateSpaceRequiredException;

	void encryptBlock(byte[] block, int start) throws StateSpaceRequiredException;
	void encryptBlock(byte[] block, int start0, byte[] out, int start1) throws StateSpaceRequiredException;
	
}

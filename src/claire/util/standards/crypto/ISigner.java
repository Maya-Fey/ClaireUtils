package claire.util.standards.crypto;

public interface ISigner {
	
	void signBlock(byte[] block, int start);
	void signBlock(byte[] block, int start0, byte[] out, int start1);

	int messageSize();
	int signatureSize();
	
}

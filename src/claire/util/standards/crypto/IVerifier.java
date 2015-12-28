package claire.util.standards.crypto;

public interface IVerifier {
	
	boolean verify(byte[] signature, int start0, byte[] message, int start1);

	int messageSize();
	int signatureSize();
	
}

package claire.util.standards.crypto;

public interface ICrypter {
	
	void reset();
	
	int plaintextSize();
	int ciphertextSize();

}

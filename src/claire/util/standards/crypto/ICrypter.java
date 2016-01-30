package claire.util.standards.crypto;

public interface ICrypter {
	
	void reset();
	void wipe();
	
	int plaintextSize();
	int ciphertextSize();

}

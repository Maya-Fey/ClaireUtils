package claire.util.standards.crypto;

public interface IDecrypter 
	   extends ICrypter {
	
	void reset();
	
	void decryptBlock(byte[] block, int start);
	void decryptBlock(byte[] block, int start0, byte[] out, int start1);
	
	default void decryptBlock(byte[] block)
	{
		this.decryptBlock(block, 0);
	}

}

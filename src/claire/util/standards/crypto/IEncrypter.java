package claire.util.standards.crypto;

public interface IEncrypter 
	   extends ICrypter {
	
	void reset();
	
	void encryptBlock(byte[] block, int start);
	void encryptBlock(byte[] block, int start0, byte[] out, int start1);
	
	default void encryptBlock(byte[] block)
	{
		this.encryptBlock(block, 0);
	}
	
}

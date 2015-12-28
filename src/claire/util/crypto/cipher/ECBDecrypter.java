package claire.util.crypto.cipher;

import claire.util.standards.crypto.ISymmetric;

public class ECBDecrypter 
	   extends BasicDecrypter {
	
	public ECBDecrypter(ISymmetric<?> cipher)
	{
		super(cipher);
	}
	
	public void decryptBlock(byte[] block, int start)
	{
		dec.decryptBlock(block, start);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		dec.decryptBlock(block, start0, out, start1);
	}
	
	public void reset() {}
	
}

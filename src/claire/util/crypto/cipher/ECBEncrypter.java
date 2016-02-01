package claire.util.crypto.cipher;

import claire.util.standards.crypto.ISymmetric;

public class ECBEncrypter 
	   extends BasicEncrypter {
	
	public ECBEncrypter(ISymmetric<?> cipher)
	{
		super(cipher);
	}
	
	public void encryptBlock(byte[] block, int start)
	{
		enc.encryptBlock(block, start);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		enc.encryptBlock(block, start0, out, start1);
	}
	
	public void reset() {}

	public void wipe()
	{
		enc = null;
	}
	
}

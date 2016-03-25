package claire.util.crypto.cipher.modes;

import claire.util.memory.util.ArrayUtil;
import claire.util.standards.crypto.IDecrypter;
import claire.util.standards.crypto.ISymmetric;

public class OFBDecrypter 
	   implements IDecrypter {

	private ISymmetric<?> cipher;
	private final byte[] prev; 
	
	public OFBDecrypter(ISymmetric<?> cipher)
	{
		this.cipher = cipher;
		prev = new byte[cipher.plaintextSize()];
	}
	
	public void decryptBlock(byte[] block, int start)
	{
		cipher.encryptBlock(prev);
		for(int i = 0; i < cipher.plaintextSize(); i++)
			block[start++] ^= prev[i];
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		cipher.encryptBlock(prev);
		System.arraycopy(block, start0, out, start1, cipher.plaintextSize());
		for(int i = 0; i < cipher.plaintextSize(); i++)
			out[start1++] ^= prev[i];
	}

	public void setIV(byte[] IV)
	{
		this.setIV(IV, 0);
	}
	
	public void setIV(byte[] IV, int start)
	{
		System.arraycopy(IV, start, prev, 0, cipher.plaintextSize());
	}
	
	public void reset()
	{
		ArrayUtil.empty(prev);
	}
	
	public int plaintextSize()
	{
		return cipher.plaintextSize();
	}
	
	public int ciphertextSize()
	{
		return cipher.ciphertextSize();
	}

	public void wipe()
	{
		cipher = null;
		ArrayUtil.empty(prev);
	}

}

package claire.util.crypto.cipher;

import claire.util.memory.util.ArrayUtil;
import claire.util.standards.crypto.ISymmetric;

public class OFBEncrypter 
	   extends BasicEncrypter {

	private final byte[] prev; 
	
	public OFBEncrypter(ISymmetric<?> cipher)
	{
		super(cipher);
		prev = new byte[cipher.plaintextSize()];
	}
	
	public void encryptBlock(byte[] block, int start)
	{
		enc.encryptBlock(prev);
		for(int i = 0; i < enc.plaintextSize(); i++)
			block[start++] ^= prev[i];
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		enc.encryptBlock(prev);
		System.arraycopy(block, start0, out, start1, enc.plaintextSize());
		for(int i = 0; i < enc.plaintextSize(); i++)
			out[start1++] ^= prev[i];
	}
	
	public void setIV(byte[] IV)
	{
		this.setIV(IV, 0);
	}
	
	public void setIV(byte[] IV, int start)
	{
		System.arraycopy(IV, start, prev, 0, enc.plaintextSize());
	}
	
	public void reset()
	{
		ArrayUtil.empty(prev);
	}

	public void wipe()
	{
		ArrayUtil.empty(prev);
		this.enc = null;
	}

}

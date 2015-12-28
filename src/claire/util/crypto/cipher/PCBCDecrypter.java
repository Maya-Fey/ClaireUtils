package claire.util.crypto.cipher;

import claire.util.memory.util.ArrayUtil;
import claire.util.standards.crypto.ISymmetric;

public class PCBCDecrypter 
	   extends BasicDecrypter {

	private final byte[] prev, temp; 
	
	public PCBCDecrypter(ISymmetric<?> cipher)
	{
		super(cipher);
		prev = new byte[cipher.plaintextSize()];
		temp = new byte[cipher.plaintextSize()];
	}
	
	public void decryptBlock(byte[] block, int start)
	{
		System.arraycopy(block, start, temp, 0, dec.plaintextSize());
		dec.decryptBlock(block, start);
		for(int i = 0; i < dec.plaintextSize(); i++)
			block[start++] ^= prev[i];
		start -= dec.plaintextSize();
		for(int i = 0; i < dec.plaintextSize(); i++)
			prev[i] = (byte) (temp[i] ^ block[start++]);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		dec.decryptBlock(block, start0, out, start1);
		for(int i = 0; i < dec.plaintextSize(); i++)
			out[start1++] ^= prev[i];
		start1 -= dec.plaintextSize();
		for(int i = 0; i < dec.plaintextSize(); i++)
			prev[i] = (byte) (out[start1++] ^ block[start0++]);
	}

	public void setIV(byte[] IV)
	{
		this.setIV(IV, 0);
	}
	
	public void setIV(byte[] IV, int start)
	{
		System.arraycopy(IV, start, prev, 0, dec.plaintextSize());
	}
	
	public void reset()
	{
		ArrayUtil.empty(prev);
	}

}

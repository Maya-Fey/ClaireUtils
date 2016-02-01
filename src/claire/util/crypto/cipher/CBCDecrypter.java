package claire.util.crypto.cipher;

import java.util.Arrays;

import claire.util.standards.crypto.ISymmetric;

public class CBCDecrypter 
	   extends BasicDecrypter {

	private final byte[] prev, temp; 
	
	public CBCDecrypter(ISymmetric<?> cipher)
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
		System.arraycopy(temp, 0, prev, 0, dec.plaintextSize());
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		dec.decryptBlock(block, start0, out, start1);
		for(int i = 0; i < dec.plaintextSize(); i++)
			out[start1++] ^= prev[i];
		System.arraycopy(block, start0, prev, 0, dec.plaintextSize());
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
		Arrays.fill(prev, (byte) 0);
	}
	
	public void wipe()
	{
		Arrays.fill(prev, (byte) 0);
		Arrays.fill(temp, (byte) 0);
		this.dec = null;
	}

}

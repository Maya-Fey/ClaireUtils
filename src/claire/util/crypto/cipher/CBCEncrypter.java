package claire.util.crypto.cipher;

import java.util.Arrays;

import claire.util.standards.crypto.ISymmetric;

public class CBCEncrypter 
	   extends BasicEncrypter {

	private final byte[] prev; 
	
	public CBCEncrypter(ISymmetric<?> cipher)
	{
		super(cipher);
		prev = new byte[cipher.plaintextSize()];
	}
	
	public void encryptBlock(byte[] block, int start) 
	{
		for(int i = 0; i < enc.plaintextSize(); i++)
			block[start++] ^= prev[i];
		start -= enc.plaintextSize();
		enc.encryptBlock(block, start);
		System.arraycopy(block, start, prev, 0, enc.plaintextSize());
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1) 
	{
		for(int i = 0; i < enc.plaintextSize(); i++)
			prev[i] ^= block[start0++];
		enc.encryptBlock(prev, 0, out, start1);
		System.arraycopy(out, start1, prev, 0, enc.plaintextSize());
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
		Arrays.fill(prev, (byte) 0);
	}

}

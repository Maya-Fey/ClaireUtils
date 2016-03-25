package claire.util.crypto.cipher.modes;

import java.util.Arrays;

import claire.util.standards.crypto.ISymmetric;

public class CFBEncrypter 
	   extends BasicEncrypter {

	private final byte[] prev; 
	
	public CFBEncrypter(ISymmetric<?> cipher)
	{
		super(cipher);
		prev = new byte[cipher.plaintextSize()];
	}
	
	public void encryptBlock(byte[] block, int start)
	{
		enc.encryptBlock(prev);
		for(int i = 0; i < enc.plaintextSize(); i++)
			block[start++] ^= prev[i];
		System.arraycopy(block, start - enc.plaintextSize(), prev, 0, enc.plaintextSize());
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		enc.encryptBlock(prev);
		for(int i = 0; i < enc.plaintextSize(); i++)
			prev[i] ^= block[start0++];
		System.arraycopy(prev, 0, out, start1, enc.plaintextSize());
	}
	
	public void setIV(byte[] IV)
	{
		this.setIV(IV, 0);
	}
	
	public void setIV(byte[] IV, int start)
	{
		System.arraycopy(IV, start, prev, 0, enc.plaintextSize());
	}
	
	public void wipe()
	{
		Arrays.fill(prev, (byte) 0);
		this.enc = null;
	}

	public void reset()
	{
		Arrays.fill(prev, (byte) 0); 
	}
}

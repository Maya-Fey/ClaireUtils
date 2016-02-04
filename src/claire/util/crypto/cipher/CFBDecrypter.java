package claire.util.crypto.cipher;

import java.util.Arrays;

import claire.util.standards.crypto.IDecrypter;
import claire.util.standards.crypto.ISymmetric;

public class CFBDecrypter 
	   implements IDecrypter {

	private ISymmetric<?> cipher;
	
	private final byte[] prev, temp; 
	
	public CFBDecrypter(ISymmetric<?> cipher)
	{
		this.cipher = cipher;
		prev = new byte[cipher.plaintextSize()];
		temp = new byte[cipher.plaintextSize()];
	}
	
	public void decryptBlock(byte[] block, int start)
	{
		cipher.encryptBlock(prev, 0, temp, 0);
		System.arraycopy(block, start, prev, 0, cipher.plaintextSize());
		for(int i = 0; i < cipher.plaintextSize(); i++)
			block[start++] ^= temp[i];
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		cipher.encryptBlock(prev);
		System.arraycopy(block, start0, out, start1, cipher.plaintextSize());
		for(int i = 0; i < cipher.plaintextSize(); i++)
			out[start1++] ^= prev[i];
		System.arraycopy(block, start0, prev, 0, cipher.plaintextSize());
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
		Arrays.fill(prev, (byte) 0);
	}
	
	public void wipe()
	{
		Arrays.fill(prev, (byte) 0);
		Arrays.fill(temp, (byte) 0);
		this.cipher = null;
	}

	public int plaintextSize()
	{
		return cipher.plaintextSize();
	}

	public int ciphertextSize()
	{
		return cipher.ciphertextSize();
	}

}

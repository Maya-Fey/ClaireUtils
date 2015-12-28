package claire.util.crypto.cipher;

import claire.util.memory.util.ArrayUtil;
import claire.util.standards.crypto.ISymmetric;

public class PCBCEncrypter 
	   extends BasicEncrypter {

	private final byte[] prev, temp; 
	
	public PCBCEncrypter(ISymmetric<?> cipher)
	{
		super(cipher);
		prev = new byte[cipher.plaintextSize()];
		temp = new byte[cipher.plaintextSize()];
	}
	
	public void encryptBlock(byte[] block, int start)
	{
		System.arraycopy(block, start, temp, 0, enc.plaintextSize());
		for(int i = 0; i < enc.plaintextSize(); i++)
			block[start++] ^= prev[i];
		enc.encryptBlock(block, start -= enc.plaintextSize());
		for(int i = 0; i < enc.plaintextSize(); i++)
			temp[i] ^= block[start++];
		System.arraycopy(temp, 0, prev, 0, enc.plaintextSize());
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		System.arraycopy(block, start0, temp, 0, enc.plaintextSize());
		for(int i = 0; i < enc.plaintextSize(); i++)
			temp[i] ^= prev[i];
		enc.encryptBlock(temp, 0, out, start1);
		for(int i = 0; i < enc.plaintextSize(); i++)
			prev[i] = (byte) (out[start1++] ^ block[start0++]);
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

}

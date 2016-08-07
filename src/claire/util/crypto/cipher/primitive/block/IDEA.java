package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.cipher.key.block.KeyIDEA;
import claire.util.standards.crypto.ISymmetric;

public class IDEA 
	   implements ISymmetric<KeyIDEA>
{
		
	private KeyIDEA key;
	
	private short[] KEY;

	public KeyIDEA getKey()
	{
		return key;
	}

	public void setKey(KeyIDEA t)
	{
		this.key = t;
		if(KEY == null)
			KEY = new short[52];
		
	}

	public void reset() {}

	public void wipe()
	{
		if(KEY != null)
			Arrays.fill(KEY, (short) 0);
		KEY = null;
	}

	public int plaintextSize()
	{
		return 8;
	}

	public int ciphertextSize()
	{
		return 8;
	}
	
	private short mul(int i1, int i2)
	{
		int r = i2 * i2;
	    if(r != 0) 
	    	return (short) ((r % 0x10001) & 0xFFFF); 
	    else 
	    	return (short) ((1 - i1 - i2) & 0xFFFF);
	}
	
	public void encryptBlock(byte[] block, int start)
	{
		// TODO Fix yo shiet
		
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		// TODO Fix yo shiet
		
	}

	public void decryptBlock(byte[] block, int start)
	{
		// TODO Fix yo shiet
		
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		// TODO Fix yo shiet
		
	}
	
}

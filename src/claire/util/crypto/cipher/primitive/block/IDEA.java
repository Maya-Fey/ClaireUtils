package claire.util.crypto.cipher.primitive.block;

import claire.util.crypto.cipher.key.block.KeyIDEA;
import claire.util.standards.crypto.ISymmetric;

public class IDEA 
	   implements ISymmetric<KeyIDEA>
{

	public KeyIDEA getKey()
	{
		// TODO Fix yo shiet
		return null;
	}

	public void setKey(KeyIDEA t)
	{
		// TODO Fix yo shiet
		
	}

	public void reset() {}

	public void wipe()
	{
		// TODO Fix yo shiet
		
	}

	public int plaintextSize()
	{
		return 8;
	}

	public int ciphertextSize()
	{
		return 8;
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

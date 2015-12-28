package claire.util.crypto.cipher.primitive;

import claire.util.crypto.cipher.key.KeyIDEA;
import claire.util.crypto.rng.RandUtils;
import claire.util.standards.IRandom;
import claire.util.standards.crypto.ISymmetric;

public class IDEA 
	   implements ISymmetric<KeyIDEA> {

	private KeyIDEA key;
	
	public KeyIDEA getKey()
	{
		return this.key;
	}

	public void setKey(KeyIDEA t)
	{
		this.key = t;
		// TODO Auto-generated method stub
	}

	public void destroyKey()
	{
		this.key.erase();
	}

	public int plaintextSize()
	{
		return 8;
	}
	
	public int ciphertextSize()
	{
		return 8;
	}

	public void decryptBlock(byte[] block, int start)
	{
		// TODO Auto-generated method stub
		
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		// TODO Auto-generated method stub
		
	}

	public void encryptBlock(byte[] block, int start)
	{
		// TODO Auto-generated method stub
		
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		// TODO Auto-generated method stub
		
	}

	public KeyIDEA newKey(IRandom rand)
	{
		byte[] key = new byte[16];
		RandUtils.fillArr(key, rand);
		return new KeyIDEA(key);
	}

	public void genKey(IRandom rand)
	{
		setKey(newKey(rand));
	}

	public void reset() {}

}

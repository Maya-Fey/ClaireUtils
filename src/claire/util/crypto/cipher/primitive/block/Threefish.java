package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeyThreefish;
import claire.util.standards.crypto.ISymmetric;

public class Threefish 
	   implements ISymmetric<KeyThreefish> {
	
	private static final long DIV3 = 0x1bd11bdaa9fc1a22L; 
	
	private KeyThreefish ref;
	private int size;
	private long[] key;

	public KeyThreefish getKey()
	{
		return ref;
	}

	public void setKey(KeyThreefish t)
	{
		ref = t;
		long[] raw = t.getLongs();
		size = raw.length;
		
		
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
	
	public void decryptBlock(byte[] block, int start)
	{
		// TODO Auto-generated method stub
		
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		// TODO Auto-generated method stub
		
	}

	public void reset() {}

	public void wipe()
	{
		ref = null;
		size = 0;
		Arrays.fill(key, 0L);
		key = null;		
	}

	public int plaintextSize()
	{
		return size * 8;
	}

	public int ciphertextSize()
	{
		return size * 8;
	}

	@Override
	public KeyFactory<KeyThreefish> keyFactory()
	{
		// TODO Auto-generated method stub
		return null;
	}

}

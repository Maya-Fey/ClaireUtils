package claire.util.crypto.cipher.primitive.block;

import claire.util.crypto.cipher.key.block.KeyTEA;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class TEA 
	   implements ISymmetric<KeyTEA> {
	
	protected KeyTEA key;
	protected int[] schedule;
	
	protected static final int DELTA = 0x9e3779b9;
	
	private static final int PRSUM = 0xc6ef3720;
	
	public TEA(KeyTEA key)
	{
		this.setKey(key);
	}

	public KeyTEA getKey()
	{
		return this.key;
	}

	public void setKey(KeyTEA t)
	{
		this.key = t;
		schedule = t.getInts();
	}

	public void wipe()
	{
		key = null;
		schedule = null;
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
		int k1 = schedule[0],
			k2 = schedule[1],
			k3 = schedule[2],
			k4 = schedule[3];
		
		int A = Bits.intFromBytes(block, start + 0),
			B = Bits.intFromBytes(block, start + 4);
			
		int sum = PRSUM;
			
		for(int i = 0; i < 32; i++)
		{
			B -= ((A << 4) + k3) ^ (A + sum) ^ ((A >> 5) + k4);
			A -= ((B << 4) + k1) ^ (B + sum) ^ ((B >> 5) + k2);
		    sum -= DELTA;
		}
			
		Bits.intToBytes(A, block, start + 0);
		Bits.intToBytes(B, block, start + 4);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int k1 = schedule[0],
			k2 = schedule[1],
			k3 = schedule[2],
			k4 = schedule[3];
			
		int A = Bits.intFromBytes(block, start0 + 0),
			B = Bits.intFromBytes(block, start0 + 4);
				
		int sum = PRSUM;
				
		for(int i = 0; i < 32; i++)
		{
			B -= ((A << 4) + k3) ^ (A + sum) ^ ((A >> 5) + k4);
			A -= ((B << 4) + k1) ^ (B + sum) ^ ((B >> 5) + k2);
			sum -= DELTA;
		}
				
		Bits.intToBytes(A, out, start1 + 0);
		Bits.intToBytes(B, out, start1 + 4);
	}

	public void encryptBlock(byte[] block, int start)
	{		
		int k1 = schedule[0],
			k2 = schedule[1],
			k3 = schedule[2],
			k4 = schedule[3];
		
		int A = Bits.intFromBytes(block, start + 0),
			B = Bits.intFromBytes(block, start + 4);
		
		int sum = 0;
		
		for(int i = 0; i < 32; i++)
		{
			sum += DELTA;
			A += ((B << 4) + k1) ^ (B + sum) ^ ((B >> 5) + k2);
	        B += ((A << 4) + k3) ^ (A + sum) ^ ((A >> 5) + k4);
		}
		
		Bits.intToBytes(A, block, start + 0);
		Bits.intToBytes(B, block, start + 4);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int k1 = schedule[0],
			k2 = schedule[1],
			k3 = schedule[2],
			k4 = schedule[3];
			
		int A = Bits.intFromBytes(block, start0 + 0),
			B = Bits.intFromBytes(block, start0 + 4);
				
		int sum = 0;
		
		for(int i = 0; i < 32; i++)
		{
			sum += DELTA;
			A += ((B << 4) + k1) ^ (B + sum) ^ ((B >> 5) + k2);
	        B += ((A << 4) + k3) ^ (A + sum) ^ ((A >> 5) + k4);
		}
				
		Bits.intToBytes(A, out, start1 + 0);
		Bits.intToBytes(B, out, start1 + 4);
	}	

	public void reset() {}
	
	public static final int test()
	{
		final int[] ints1 = new int[4];
		final int[] ints2 = new int[4];
		RandUtils.fillArr(ints1);
		RandUtils.fillArr(ints2);
		KeyTEA a1 = new KeyTEA(ints1);
		KeyTEA a2 = new KeyTEA(ints2);
		TEA aes = new TEA(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}
	
}

package claire.util.crypto.cipher.primitive;

import java.util.Arrays;

import claire.util.crypto.cipher.key.KeyTEA;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IRandom;
import claire.util.standards.crypto.ISymmetric;

public class TEA implements ISymmetric<KeyTEA> {
	
	protected KeyTEA key;
	protected int[] schedule;
	
	protected static final int DELTA = 0x9e3779b9;
	
	private static final int PRSUM = 0xc6ef3720;

	public KeyTEA getKey()
	{
		return this.key;
	}

	public void setKey(KeyTEA t)
	{
		this.key = t;
		schedule = Bits.bytesToInts(key.getBytes());
	}

	public void destroyKey()
	{
		Arrays.fill(schedule, 0);
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

	public KeyTEA newKey(IRandom rand)
	{
		byte[] bytes = new byte[16];
		RandUtils.fillArr(bytes, rand);
		return new KeyTEA(bytes);
	}

	public void genKey(IRandom rand)
	{
		this.setKey(newKey(rand));
	}

	public void reset() {}
	
}

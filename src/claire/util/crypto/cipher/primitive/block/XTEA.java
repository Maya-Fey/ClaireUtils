package claire.util.crypto.cipher.primitive.block;

import claire.util.crypto.cipher.key.block.KeyXTEA;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class XTEA  
	   implements ISymmetric<KeyXTEA> {
	
	protected KeyXTEA key;
	protected int[] schedule;
	private int rounds;
	
	protected static final int DELTA = 0x9e3779b9;
	
	public XTEA(KeyXTEA key)
	{
		this.setKey(key);
	}

	public KeyXTEA getKey()
	{
		return this.key;
	}

	public void setKey(KeyXTEA t)
	{
		this.key = t;
		rounds = t.getRounds();
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
		int A = Bits.intFromBytes(block, start    ),
			B = Bits.intFromBytes(block, start + 4);
			
		int sum = DELTA * rounds;
			
		for(int i = 0; i < rounds; i++)
		{
			B -= (((A << 4) ^ (A >> 5)) + A) ^ (sum + schedule[(sum >> 11) & 3]);
			sum -= DELTA;
			A -= (((B << 4) ^ (B >> 5)) + B) ^ (sum + schedule[sum & 3]);
		}
			
		Bits.intToBytes(A, block, start    );
		Bits.intToBytes(B, block, start + 4);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{	
		int A = Bits.intFromBytes(block, start0 + 0),
			B = Bits.intFromBytes(block, start0 + 4);
				
		int sum = DELTA * rounds;
				
		for(int i = 0; i < rounds; i++)
		{
			B -= (((A << 4) ^ (A >> 5)) + A) ^ (sum + schedule[(sum >> 11) & 3]);
			sum -= DELTA;
			A -= (((B << 4) ^ (B >> 5)) + B) ^ (sum + schedule[sum & 3]);
		}
				
		Bits.intToBytes(A, out, start1 + 0);
		Bits.intToBytes(B, out, start1 + 4);
	}

	public void encryptBlock(byte[] block, int start)
	{			
		int A = Bits.intFromBytes(block, start    ),
			B = Bits.intFromBytes(block, start + 4);
		
		int sum = 0;
		
		for(int i = 0; i < rounds; i++)
		{
			A += (((B << 4) ^ (B >> 5)) + B) ^ (sum + schedule[sum & 3]);
	        sum += DELTA;
	        B += (((A << 4) ^ (A >> 5)) + A) ^ (sum + schedule[(sum >> 11) & 3]);
		}
		
		Bits.intToBytes(A, block, start    );
		Bits.intToBytes(B, block, start + 4);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int A = Bits.intFromBytes(block, start0 + 0),
			B = Bits.intFromBytes(block, start0 + 4);
				
		int sum = 0;
		
		for(int i = 0; i < rounds; i++)
		{
			A += (((B << 4) ^ (B >> 5)) + B) ^ (sum + schedule[sum & 3]);
	        sum += DELTA;
	        B += (((A << 4) ^ (A >> 5)) + A) ^ (sum + schedule[(sum >> 11) & 3]);
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
		KeyXTEA a1 = new KeyXTEA(ints1, 32);
		KeyXTEA a2 = new KeyXTEA(ints2, 48);
		XTEA aes = new XTEA(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

}

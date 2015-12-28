package claire.util.crypto.cipher.primitive;

import claire.util.memory.Bits;

public class XXTEA extends TEA {
	
	private final int words;
	
	private int rounds;

	public XXTEA(int words)
	{
		this.words = words;
		this.addRounds(6);
	}
	
	public int plaintextSize()
	{
		return this.words * 4;
	}
	
	public void addRounds(int rounds)
	{
		this.rounds = rounds + (52 / words);
	}

	public void decryptBlock(byte[] block)
	{
		int[] ints = new int[words];
		Bits.bytesToInts(block, ints);
		
		int A = ints[0], B, sum = DELTA * rounds, j;
		
		while(sum != 0)
		{	
			int e = (sum >> 2) & 3;
			for(j = words - 1; j > 0; j--)
			{
				B = ints[j - 1];
				A = ints[j] -= (((B >> 5 ^ A << 2) + (A >> 3 ^ B << 4)) ^ ((sum ^ A) + (schedule[(j & 3) ^ e] ^ B)));
			}
			B = ints[words - 1];
			A = ints[0] -= (((B >> 5 ^ A << 2) + (A >> 3 ^ B << 4)) ^ ((sum ^ A) + (schedule[(j & 3) ^ e] ^ B)));
			sum -= DELTA;
		}
		
		Bits.intsToBytes(ints, block);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int[] ints = new int[words];
		Bits.bytesToInts(block, start0, ints, 0, words);
		
		int A = ints[0], B, sum = DELTA * rounds, j;
		
		while(sum != 0)
		{	
			int e = (sum >> 2) & 3;
			for(j = words - 1; j > 0; j--)
			{
				B = ints[j - 1];
				A = ints[j] -= (((B >> 5 ^ A << 2) + (A >> 3 ^ B << 4)) ^ ((sum ^ A) + (schedule[(j & 3) ^ e] ^ B)));
			}
			B = ints[words - 1];
			A = ints[0] -= (((B >> 5 ^ A << 2) + (A >> 3 ^ B << 4)) ^ ((sum ^ A) + (schedule[(j & 3) ^ e] ^ B)));
			sum -= DELTA;
		}
		
		Bits.intsToBytes(ints, 0, out, start1, words);
	}

	public void encryptBlock(byte[] block)
	{				
		int[] ints = new int[words];
		Bits.bytesToInts(block, ints);
		
		int A, B = ints[words - 1], sum = 0, j;
		
		for(int i = 0; i < rounds; i++)
		{
			sum += DELTA;
			int e = (sum >> 2) & 3;
			for(j = 0; j < words - 1; j++)
			{
				A = ints[j + 1];
				B = ints[j] += (((B >> 5 ^ A << 2) + (A >> 3 ^ B << 4)) ^ ((sum ^ A) + (schedule[(j & 3) ^ e] ^ B)));
			}
			A = ints[0];
			B= ints[words - 1] += (((B >> 5 ^ A << 2) + (A >> 3 ^ B << 4)) ^ ((sum ^ A) + (schedule[(j & 3) ^ e] ^ B)));
		}
		
		Bits.intsToBytes(ints, block);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int[] ints = new int[words];
		Bits.bytesToInts(block, start0, ints, 0, words);
		
		int A, B = ints[words - 1], sum = 0, j;
		
		for(int i = 0; i < rounds; i++)
		{
			sum += DELTA;
			int e = (sum >> 2) & 3;
			for(j = 0; j < words - 1; j++)
			{
				A = ints[j + 1];
				B = ints[j] += (((B >> 5 ^ A << 2) + (A >> 3 ^ B << 4)) ^ ((sum ^ A) + (schedule[(j & 3) ^ e] ^ B)));
			}
			A = ints[0];
			B= ints[words - 1] += (((B >> 5 ^ A << 2) + (A >> 3 ^ B << 4)) ^ ((sum ^ A) + (schedule[(j & 3) ^ e] ^ B)));
		}
		Bits.intsToBytes(ints, 0, out, start1, words);
	}

}

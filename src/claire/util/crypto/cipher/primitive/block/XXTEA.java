package claire.util.crypto.cipher.primitive.block;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeyXXTEA;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class XXTEA 
	   implements ISymmetric<KeyXXTEA> {
	
	protected static final int DELTA = 0x9e3779b9;
	
	protected KeyXXTEA key;
	protected int[] schedule;
	
	private int words;
	private int rounds;
	
	public XXTEA() {}

	public XXTEA(KeyXXTEA key)
	{
		this.setKey(key);
	}
	
	public KeyXXTEA getKey()
	{
		return this.key;
	}

	public void setKey(KeyXXTEA t)
	{
		this.key = t;
		schedule = t.getInts();
		words = key.getWords();
		rounds = 6 + (52 / words);
	}

	public void wipe()
	{
		key = null;
		schedule = null;
		words = 0;
		rounds = 0;
	}
	
	public int plaintextSize()
	{
		return this.words * 4;
	}

	public int ciphertextSize()
	{
		return this.words * 4;
	}
	
	public void addRounds(int rounds)
	{
		this.rounds = rounds + (52 / words);
	}

	public void decryptBlock(byte[] block, int start)
	{
		int[] ints = new int[words];
		Bits.bytesToInts(block, start, ints, 0, words);
		
		int A = ints[0], 
			B, sum = DELTA * rounds, 
			j;
		
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
		
		Bits.intsToBytes(ints, 0, block, start, words);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int[] ints = new int[words];
		Bits.bytesToInts(block, start0, ints, 0, words);
		
		int A = ints[0], 
			B, 
			sum = DELTA * rounds, j;
		
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

	public void encryptBlock(byte[] block, int start)
	{				
		int[] ints = new int[words];
		Bits.bytesToInts(block, start, ints, 0, words);
		
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
		
		Bits.intsToBytes(ints, 0, block, start, words);
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

	public void reset() {}
	
	public static final int test()
	{
		final int[] ints1 = new int[4];
		final int[] ints2 = new int[4];
		RandUtils.fillArr(ints1);
		RandUtils.fillArr(ints2);
		KeyXXTEA a1 = new KeyXXTEA(ints1, 4);
		KeyXXTEA a2 = new KeyXXTEA(ints2, 6);
		XXTEA aes = new XXTEA(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

	public KeyFactory<KeyXXTEA> keyFactory()
	{
		return KeyXXTEA.factory;
	}
}

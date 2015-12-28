package claire.util.crypto.cipher.primitive;

import claire.util.memory.Bits;

public class XTEA extends TEA {
	
	private final int rounds;

	public XTEA(int rounds) 
	{
		this.rounds = rounds;
	}
	
	public void decryptBlock(byte[] block)
	{
		int A = Bits.intFromBytes(block, 0),
			B = Bits.intFromBytes(block, 4);
			
		int sum = DELTA * rounds;
			
		for(int i = 0; i < rounds; i++)
		{
			B -= (((A << 4) ^ (A >> 5)) + A) ^ (sum + schedule[(sum >> 11) & 3]);
			sum -= DELTA;
			A -= (((B << 4) ^ (B >> 5)) + B) ^ (sum + schedule[sum & 3]);
		}
			
		Bits.intToBytes(A, block, 0);
		Bits.intToBytes(B, block, 4);
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

	public void encryptBlock(byte[] block)
	{			
		int A = Bits.intFromBytes(block, 0),
			B = Bits.intFromBytes(block, 4);
		
		int sum = 0;
		
		for(int i = 0; i < rounds; i++)
		{
			A += (((B << 4) ^ (B >> 5)) + B) ^ (sum + schedule[sum & 3]);
	        sum += DELTA;
	        B += (((A << 4) ^ (A >> 5)) + A) ^ (sum + schedule[(sum >> 11) & 3]);
		}
		
		Bits.intToBytes(A, block, 0);
		Bits.intToBytes(B, block, 4);
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

}

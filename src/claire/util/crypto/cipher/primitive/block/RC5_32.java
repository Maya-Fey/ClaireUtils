package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.cipher.key.block.KeyRC5;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class RC5_32 
	   implements ISymmetric<KeyRC5> {
	
	private static final int P = 0xb7e15163;
	private static final int Q = 0x9e3779b9;
	
	private KeyRC5 key;
	private int[] schedule;
	
	private int rounds;
	
	public RC5_32(KeyRC5 key)
	{
		this.setKey(key);
	}

	public KeyRC5 getKey()
	{
		return this.key;
	}
	
	public void setKey(KeyRC5 t)
	{
		this.key = t;
		this.rounds = key.getRounds();
		int[] ints = Bits.bytesToIntsFull(key.getBytes());
		schedule = new int[(this.rounds + 1) << 1];
		schedule[0] = P;
		for(int i = 1; i < schedule.length; i++)
			schedule[i] = schedule[i - 1] + Q;
		int max;
		if(ints.length > schedule.length)
			max = 3 * ints.length;
		else
			max = 3 * schedule.length;
		int A = 0, B = 0, C = 0, D = 0;
		for(int i = 0; i < max; i++) 
		{
			A = schedule[C] = Bits.rotateLeft(schedule[C] + A + B, 3           );
			B = 	ints[D] = Bits.rotateLeft(	  ints[D] + A + B, (A + B) & 31);
			C = ++C % schedule.length;
			D = ++D % ints.length;	
		}		
	}

	public void wipe()
	{
		Arrays.fill(schedule, 0);
		schedule = null;
		rounds = 0;
		key = null;
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
		int A = Bits.intFromBytes(block, start + 0);
        int B = Bits.intFromBytes(block, start + 4);

        for (int i = this.rounds; i >= 1; i--)
        {
            B = Bits.rotateRight(B - schedule[(i << 1) + 1], A & 31) ^ A;
            A = Bits.rotateRight(A - schedule[i << 1],   B & 31) ^ B;
        }
        
        A -= schedule[0];
        B -= schedule[1];
        
        Bits.intToBytes(A, block, start + 0);
        Bits.intToBytes(B, block, start + 4);
	}
	
	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int A = Bits.intFromBytes(block, start0 + 0);
        int B = Bits.intFromBytes(block, start0 + 4);

        for (int i = this.rounds; i >= 1; i--)
        {
            B = Bits.rotateRight(B - schedule[(i << 1) + 1], A & 31) ^ A;
            A = Bits.rotateRight(A - schedule[i << 1],   B & 31) ^ B;
        }
        
        A -= schedule[0];
        B -= schedule[1];
        
        Bits.intsToBytes(new int[] { A, B }, 0, out, start1);
	}

	public void encryptBlock(byte[] block, int start)
	{
		int A = Bits.intFromBytes(block, start + 0) + schedule[0];
        int B = Bits.intFromBytes(block, start + 4) + schedule[1];

        for (int i = 1; i <= this.rounds; i++)
        {
            A = Bits.rotateLeft(A ^ B, B & 31) + schedule[i << 1];
            B = Bits.rotateLeft(B ^ A, A & 31) + schedule[(i << 1) + 1];
        }
        
        Bits.intToBytes(A, block, start + 0);
        Bits.intToBytes(B, block, start + 4);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int A = Bits.intFromBytes(block, start0	   ) + schedule[0];
        int B = Bits.intFromBytes(block, start0 + 4) + schedule[1];

        for (int i = 1; i <= this.rounds; i++)
        {
            A = Bits.rotateLeft(A ^ B, B & 31) + schedule[i << 1];
            B = Bits.rotateLeft(B ^ A, A & 31) + schedule[(i << 1) + 1];
        }
        
        Bits.intsToBytes(new int[] { A, B }, 0, out, start1);
	}
	
	public void reset() {}

	public static final int test()
	{
		final byte[] bytes1 = new byte[13];
		final byte[] bytes2 = new byte[33];
		RandUtils.fillArr(bytes1);
		RandUtils.fillArr(bytes2);
		KeyRC5 a1 = new KeyRC5(bytes1, 6);
		KeyRC5 a2 = new KeyRC5(bytes2, 12);
		RC5_32 aes = new RC5_32(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}
	
}

package claire.util.crypto.cipher.primitive;

import java.util.Arrays;

import claire.util.crypto.cipher.key.KeyRC5;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IRandom;
import claire.util.standards.crypto.ISymmetric;

public class RC5_64 
	   implements ISymmetric<KeyRC5> {
	
    private static final long  P = 0xb7e151628aed2a6bL;
    private static final long  Q = 0x9e3779b97f4a7c15L;
   
	private KeyRC5 key;
	private long[] schedule;
	
	private final int rounds;
	
	public RC5_64(int rounds)
	{
		this.rounds = rounds;
	}
	
	public RC5_64(KeyRC5 key, int rounds)
	{
		this.key = key;
		this.rounds = rounds;
	}

	public KeyRC5 getKey()
	{
		return this.key;
	}
	
	public void setKey(KeyRC5 t)
	{
		this.key = t;
		long[] ints = Bits.bytesToLongsFull(key.getBytes());
		schedule = new long[(this.rounds + 1) << 1];
		schedule[0] = P;
		for(int i = 1; i < schedule.length; i++)
			schedule[i] = (long) (schedule[i - 1] + Q);
		int max;
		if(ints.length > schedule.length)
			max = 3 * ints.length;
		else
			max = 3 * schedule.length;
		long A = 0, B = 0;
		int   C = 0, D = 0;
		for(int i = 0; i < max; i++) 
		{
			A = schedule[C] = Bits.rotateLeft((long) (schedule[C] + A + B), 3             );
			B = 	ints[D] = Bits.rotateLeft((long) (ints[D] + A + B), (int) (A + B) & 63);
			C = ++C % schedule.length;
			D = ++D % ints.length;	
		}		
	}

	public void destroyKey()
	{
		Arrays.fill(schedule, 0);
	}

	public int plaintextSize()
	{
		return 16;
	}
	
	public int ciphertextSize()
	{
		return 16;
	}
	
	public void decryptBlock(byte[] block, int start)
	{
		long A = Bits.longFromBytes(block, start + 0);
        long B = Bits.longFromBytes(block, start + 8);

        for (int i = this.rounds; i >= 1; i--)
        {
        	B = Bits.rotateRight((long) (B - schedule[(i << 1) + 1]), (int) A & 63);
        	B ^= A;
            A = Bits.rotateRight((long) (A - schedule[i << 1])      , (int) B & 63);
            A ^= B;
        }
        
        A -= schedule[0];
        B -= schedule[1];
        
        Bits.longToBytes(A, block, start + 0);
        Bits.longToBytes(B, block, start + 8);
	}
	
	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		long A = Bits.longFromBytes(block, start0 + 0);
        long B = Bits.longFromBytes(block, start0 + 8);

        for (int i = this.rounds; i >= 1; i--)
        {
        	B = Bits.rotateRight((long) (B - schedule[(i << 1) + 1]), (int) A & 63);
        	B ^= A;
            A = Bits.rotateRight((long) (A - schedule[i << 1])      , (int) B & 63);
            A ^= B;
        }
        
        A -= schedule[0];
        B -= schedule[1];
        
        Bits.longsToBytes(new long[] { A, B }, 0, out, start1);
	}

	public void encryptBlock(byte[] block, int start)
	{
		long A = (long) ((Bits.longFromBytes(block, start + 0) + schedule[0]));
		long B = (long) ((Bits.longFromBytes(block, start + 8) + schedule[1]));

        for (int i = 1; i <= this.rounds; i++)
        {
        	A ^= B;
            A = (long) (Bits.rotateLeft(A, (int) B & 63) + schedule[i << 1]);
            B ^= A;
            B = (long) (Bits.rotateLeft(B, (int) A & 63) + schedule[(i << 1) + 1]);
        }
        
        Bits.longToBytes(A, block, start + 0);
        Bits.longToBytes(B, block, start + 8);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		long A = (long) (Bits.longFromBytes(block, start0 + 0) + schedule[0]);
        long B = (long) (Bits.longFromBytes(block, start0 + 8) + schedule[1]);

        for (int i = 1; i <= this.rounds; i++)
        {
        	A ^= B;
            A = (long) (Bits.rotateLeft(A, (int) B & 63) + schedule[i << 1]);
            B ^= A;
            B = (long) (Bits.rotateLeft(B, (int) A & 63) + schedule[(i << 1) + 1]);
        }
        
        Bits.longsToBytes(new long[] { A, B }, 0, out, start1);
	}

	public KeyRC5 newKey(IRandom rand)
	{
		byte[] arr = new byte[128];
		RandUtils.fillArr(arr, rand);
		return new KeyRC5(arr);
	}

	public void genKey(IRandom rand)
	{
		this.setKey(newKey(rand));
	}
	
	public void reset() {}

}

package claire.util.crypto.cipher.primitive;

import java.util.Arrays;

import claire.util.crypto.cipher.key.KeyRC5;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class RC5_16 
	   implements ISymmetric<KeyRC5> {
	 
	private static final short P = (short) 0xb7e1;
	private static final short Q = (short) 0x9e37;    
   
	private KeyRC5 key;
	private short[] schedule;
	
	private int rounds;
	
	public RC5_16(KeyRC5 key)
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
		this.rounds = t.getRounds();
		short[] ints = Bits.bytesToShortsFull(key.getBytes());
		schedule = new short[(this.rounds + 1) << 1];
		schedule[0] = P;
		for(int i = 1; i < schedule.length; i++)
			schedule[i] = (short) (schedule[i - 1] + Q);
		int max;
		if(ints.length > schedule.length)
			max = 3 * ints.length;
		else
			max = 3 * schedule.length;
		short A = 0, B = 0;
		int   C = 0, D = 0;
		for(int i = 0; i < max; i++) 
		{
			A = schedule[C] = Bits.rotateLeft((short) (schedule[C] + A + B), 3           );
			B = 	ints[D] = Bits.rotateLeft((short) (ints[D] + A + B), (A + B) & 15);
			C = ++C % schedule.length;
			D = ++D % ints.length;	
		}		
	}

	public void wipe()
	{
		Arrays.fill(schedule, (short) 0);
		schedule = null;
		rounds = 0;
		key = null;
	}

	public int plaintextSize()
	{
		return 4;
	}
	
	public int ciphertextSize()
	{
		return 4;
	}
	
	public void decryptBlock(byte[] block, int start)
	{
		short A = Bits.shortFromBytes(block, start + 0);
        short B = Bits.shortFromBytes(block, start + 2);

        for (int i = this.rounds; i >= 1; i--)
        {
        	B = Bits.rotateRight((short) (B - schedule[(i << 1) + 1]), A & 15);
        	B ^= A;
            A = Bits.rotateRight((short) (A - schedule[i << 1])      , B & 15);
            A ^= B;
        }
        
        A -= schedule[0];
        B -= schedule[1];
        
        Bits.shortToBytes(A, block, start + 0);
        Bits.shortToBytes(B, block, start + 2);
	}
	
	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		short A = Bits.shortFromBytes(block, start0 + 0);
        short B = Bits.shortFromBytes(block, start0 + 2);

        for (int i = this.rounds; i >= 1; i--)
        {
        	B = Bits.rotateRight((short) (B - schedule[(i << 1) + 1]), A & 15);
        	B ^= A;
            A = Bits.rotateRight((short) (A - schedule[i << 1])      , B & 15);
            A ^= B;
        }
        
        A -= schedule[0];
        B -= schedule[1];
        
        Bits.shortsToBytes(new short[] { A, B }, 0, out, start1);
	}

	public void encryptBlock(byte[] block, int start)
	{
		short A = (short) ((Bits.shortFromBytes(block, start + 0) + schedule[0]));
		short B = (short) ((Bits.shortFromBytes(block, start + 2) + schedule[1]));

        for (int i = 1; i <= this.rounds; i++)
        {
        	A ^= B;
            A = (short) (Bits.rotateLeft(A, B & 15) + schedule[i << 1]);
            B ^= A;
            B = (short) (Bits.rotateLeft(B, A & 15) + schedule[(i << 1) + 1]);
        }
        
        Bits.shortToBytes(A, block, start + 0);
        Bits.shortToBytes(B, block, start + 2);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		short A = (short) (Bits.shortFromBytes(block, start0 + 0) + schedule[0]);
        short B = (short) (Bits.shortFromBytes(block, start0 + 2) + schedule[1]);

        for (int i = 1; i <= this.rounds; i++)
        {
        	A ^= B;
            A = (short) (Bits.rotateLeft(A, B & 15) + schedule[i << 1]);
            B ^= A;
            B = (short) (Bits.rotateLeft(B, A & 15) + schedule[(i << 1) + 1]);
        }
        
        Bits.shortsToBytes(new short[] { A, B }, 0, out, start1);
	}
	
	public void reset() {}

}

package claire.util.crypto.hash.primitive;

import java.util.Arrays;

import claire.util.encoding.CString;
import claire.util.memory.Bits;

public class SHA3 extends MerkleHash {
	
	private static final String[] LENGTHS = new String[] {
		"224", "256", "384", "512"
	};
	
	private static final CString[] CLENGTHS = CString.arrayFrom(LENGTHS);
	
	private static final int[] SIZES = new int[] {
		144, 136, 104, 72
	};
	
	private static final long[] LC = new long[] {
		0x0000000000000001L, 0x0000000000008082L, 0x800000000000808aL,
		0x8000000080008000L, 0x000000000000808bL, 0x0000000080000001L,
		0x8000000080008081L, 0x8000000000008009L, 0x000000000000008aL,
		0x0000000000000088L, 0x0000000080008009L, 0x000000008000000aL,
		0x000000008000808bL, 0x800000000000008bL, 0x8000000000008089L,
		0x8000000000008003L, 0x8000000000008002L, 0x8000000000000080L,
		0x000000000000800aL, 0x800000008000000aL, 0x8000000080008081L,
		0x8000000000008080L, 0x0000000080000001L, 0x8000000080008008L
	};
	
	private static final int[] IC = new int[] {
		0, 1, 62, 28, 27, 36, 44, 6, 55, 20, 3, 10, 43,
		25, 39, 41, 45, 15, 21, 8, 18, 2, 61, 56, 14
	};
	
	private final long[] S1;
	private final long[] S2;
	private final long[] S3;
	private final long[] S4;
	
	private SHA3(int size)
	{
		super(size, 0);	
		S1 = new long[25];
		S2 = new long[25];
		S3 = new long[5];
		S4 = new long[5];
	}

	public SHA3(CString size) 
	{	
		this(getLength(size));	
	}
	
	public SHA3(String size)
	{
		this(getLength(size));	
	}
	
	/** Convoluted solution to the fact that you must call constructor on the first line */
	private static final int getLength(CString size)
	{
		for(int i = 0; i < 4; i++)
			if(size.equals(CLENGTHS[i]))
				return SIZES[i];
		throw new java.lang.ExceptionInInitializerError("Invalid length");
	}
	
	/** Convoluted solution to the fact that you must call constructor on the first line */
	private static final int getLength(String size)
	{
		for(int i = 0; i < 4; i++)
			if(size.equals(LENGTHS[i]))
				return SIZES[i];
		throw new java.lang.ExceptionInInitializerError("Invalid length");
	}
	
	private static int index(int x)
	{
		return x < 0 ? index(x + 5) : x % 5;
	}

	private static int index(int x, int y)
	{
		return index(x) + 5 * index(y);
	}
	
	private void keccak(int round)
	{
		for(int i = 0; i < 5; i++) 
			S3[i] = S1[index(i, 0)] ^ 
					S1[index(i, 1)] ^ 
					S1[index(i, 2)] ^ 
					S1[index(i, 3)] ^ 
					S1[index(i, 4)];
		
		for(int i = 0; i < 5; i++) {
			S4[i] = S3[index(i - 1)] ^ Bits.rotateLeft(S3[index(i + 1)], 1);
			for(int j = 0; j < 5; j++) 
				S1[index(i, j)] ^= S4[i];
		}
		
		for(int i = 0; i < 5; i++) 
			for (int j = 0; j < 5; j++) {
				int k = index(i, j);
				S2[index(j, i * 2 + 3 * j)] = Bits.rotateLeft(S1[k], IC[k]);
			}
		
		for(int i = 0; i < 5; i++) 
			for(int j = 0; j < 5; j++) {
				int k = index(i, j);
				S1[k] = S2[k] ^ (~S2[index(i + 1, j)] & S2[index(i + 2, j)]);
			}
		S1[0] ^= LC[round];
	}

	public void processNext(byte[] bytes, int offest)
	{
		for(int i = 0; i < this.size; i += 8) 
			S1[i >>> 3] ^= Bits.longFromBytes(bytes, i);
		for(int i = 0; i < 24; i++)
			keccak(i);
	}

	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		byte[] n = new byte[this.size];
		System.arraycopy(remaining, 0, n, 0, pos);
		if((pos + 2) == this.size)
			n[pos + 1] = (byte) 0x81;
		else {
			n[pos++] = (byte) 0x01;
			Arrays.fill(n, pos, n.length - 2, (byte) 0);
			n[n.length - 1] = (byte) 0x80;
		}
		processNext(n, 0);
		Bits.longsToBytes(S1, 0, out, start, (200 - this.size) / 16);
		Arrays.fill(S1, 0);
	}

}

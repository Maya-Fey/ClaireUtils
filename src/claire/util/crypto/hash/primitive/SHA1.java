package claire.util.crypto.hash.primitive;

import java.util.Arrays;

import claire.util.memory.Bits;

public class SHA1 
	   extends MerkleHash {
	
	private static final int A1 = 0x5a827999;
    private static final int A2 = 0x6ed9eba1;
    private static final int A3 = 0x8f1bbcdc;
    private static final int A4 = 0xca62c1d6;
	
	private final int[] STATE = new int[5];
	
	private long total;

	public SHA1() 
	{
		super(64, 20);
		reset();
	}
	
	private void reset()
	{
		STATE[0] = 0x67452301;
		STATE[1] = 0xefcdab89;
		STATE[2] = 0x98badcfe;
		STATE[3] = 0x10325476;
		STATE[4] = 0xc3d2e1f0;
		total = 0;
	}
	
	private static int F1(int u, int v, int w)
	{
	    return ((u & v) | ((~u) & w));
	}

	private static int F2(int u, int v, int w)
	{
	    return (u ^ v ^ w);
	}

	private static int F3(int u, int v, int w)
	{
	    return ((u & v) | (u & w) | (v & w));
	}

	public void processNext(byte[] bytes, int offset)
	{
		total += 64;
		int[] IN = new int[80];
		Bits.BigEndian.bytesToInts(bytes, offset, IN, 0, 16);
		for(int i = 16; i < 80; i++)
		{
			int temp = IN[i - 3] ^ IN[i - 8] ^ IN[i - 14] ^ IN[i - 16];
			IN[i] = Bits.rotateLeft(temp, 1);
		}
		int A = STATE[0],
			B = STATE[1],
			C = STATE[2],
			D = STATE[3],
			E = STATE[4];
		
		int i = 0;
		for(; i < 20;)
		{
			E += Bits.rotateLeft(A, 5) + F1(B, C, D) + IN[i++] + A1;
            B = Bits.rotateRight(B, 2);       
            D += Bits.rotateLeft(E, 5) + F1(A, B, C) + IN[i++] + A1;
            A = Bits.rotateRight(A, 2);       
            C += Bits.rotateLeft(D, 5) + F1(E, A, B) + IN[i++] + A1;
            E = Bits.rotateRight(E, 2);       
            B += Bits.rotateLeft(C, 5) + F1(D, E, A) + IN[i++] + A1;
            D = Bits.rotateRight(D, 2);
            A += Bits.rotateLeft(B, 5) + F1(C, D, E) + IN[i++] + A1;
            C = Bits.rotateRight(C, 2);
		}
		for(; i < 40;)
		{
			E += Bits.rotateLeft(A, 5) + F2(B, C, D) + IN[i++] + A2;
            B = Bits.rotateRight(B, 2);       
            D += Bits.rotateLeft(E, 5) + F2(A, B, C) + IN[i++] + A2;
            A = Bits.rotateRight(A, 2);       
            C += Bits.rotateLeft(D, 5) + F2(E, A, B) + IN[i++] + A2;
            E = Bits.rotateRight(E, 2);       
            B += Bits.rotateLeft(C, 5) + F2(D, E, A) + IN[i++] + A2;
            D = Bits.rotateRight(D, 2);
            A += Bits.rotateLeft(B, 5) + F2(C, D, E) + IN[i++] + A2;
            C = Bits.rotateRight(C, 2);
		}
		for(; i < 60;)
		{
			E += Bits.rotateLeft(A, 5) + F3(B, C, D) + IN[i++] + A3;
            B = Bits.rotateRight(B, 2);       
            D += Bits.rotateLeft(E, 5) + F3(A, B, C) + IN[i++] + A3;
            A = Bits.rotateRight(A, 2);       
            C += Bits.rotateLeft(D, 5) + F3(E, A, B) + IN[i++] + A3;
            E = Bits.rotateRight(E, 2);       
            B += Bits.rotateLeft(C, 5) + F3(D, E, A) + IN[i++] + A3;
            D = Bits.rotateRight(D, 2);
            A += Bits.rotateLeft(B, 5) + F3(C, D, E) + IN[i++] + A3;
            C = Bits.rotateRight(C, 2);
		}
		for(; i < 80;)
		{
			E += Bits.rotateLeft(A, 5) + F2(B, C, D) + IN[i++] + A4;
            B = Bits.rotateRight(B, 2);       
            D += Bits.rotateLeft(E, 5) + F2(A, B, C) + IN[i++] + A4;
            A = Bits.rotateRight(A, 2);       
            C += Bits.rotateLeft(D, 5) + F2(E, A, B) + IN[i++] + A4;
            E = Bits.rotateRight(E, 2);       
            B += Bits.rotateLeft(C, 5) + F2(D, E, A) + IN[i++] + A4;
            D = Bits.rotateRight(D, 2);
            A += Bits.rotateLeft(B, 5) + F2(C, D, E) + IN[i++] + A4;
            C = Bits.rotateRight(C, 2);
		}
		
		STATE[0] += A;
		STATE[1] += B;
		STATE[2] += C;
		STATE[3] += D;
		STATE[4] += E;
	}


	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		byte[] bytes = new byte[64];
		System.arraycopy(remaining, 0, bytes, 0, pos);
		bytes[pos] = (byte) 0x80;
		total += pos;
		if(pos >= 56) {
			processNext(bytes, 0);
			total -= 64;
			Arrays.fill(bytes, (byte) 0);
		}
		Bits.BigEndian.longToBytes(total << 3, bytes, 56);
		processNext(bytes, 0);
		Bits.BigEndian.intsToBytes(STATE, 0, out, start);
		reset();
	}
	
	

}

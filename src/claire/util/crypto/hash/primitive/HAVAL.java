package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.hash.primitive.HAVAL.HAVALState;
import claire.util.crypto.hash.primitive.MerkleHash.MerkleState;
import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class HAVAL 
	   extends MerkleHash<HAVALState, HAVAL> {
	
	private static final int[][] KCUBE = 
	{
		{
			0x452821e6, 0x38d01377, 0xbe5466cf,  0x34e90c6c,
			0xc0ac29b7, 0xc97c50dd, 0x3f84d5b5, 0xb5470917,
			0x9216d5d9, 0x8979fb1b, 0xd1310ba6, 0x98dfb5ac,
			0x2ffd72db, 0xd01adfb7, 0xb8e1afed, 0x6a267e96,
			0xba7c9045, 0xf12c7f99, 0x24a19947, 0xb3916cf7,
			0x0801f2e2, 0x858efc16, 0x636920d8, 0x71574e69,
			0xa458fea3, 0xf4933d7e, 0x0d95748f, 0x728eb658,
			0x718bcd58, 0x82154aee, 0x7b54a41d, 0xc25a59b5
		},
		{
			0x9c30d539, 0x2af26013, 0xc5d1b023, 0x286085f0,
			0xca417918, 0xb8db38ef, 0x8e79dcb0, 0x603a180e,
			0x6c9e0e8b, 0xb01e8a3e, 0xd71577c1, 0xbd314b27,
			0x78af2fda, 0x55605c60, 0xe65525f3, 0xaa55ab94,
			0x57489862, 0x63e81440, 0x55ca396a, 0x2aab10b6,
			0xb4cc5c34, 0x1141e8ce, 0xa15486af, 0x7c72e993,
			0xb3ee1411, 0x636fbc2a, 0x2ba9c55d, 0x741831f6,
			0xce5c3e16, 0x9b87931e, 0xafd6ba33, 0x6c24cf5c
		},
		{
			0x7a325381, 0x28958677, 0x3b8f4898, 0x6b4bb9af,
			0xc4bfe81b, 0x66282193, 0x61d809cc, 0xfb21a991,
			0x487cac60, 0x5dec8032, 0xef845d5d, 0xe98575b1,
			0xdc262302, 0xeb651b88, 0x23893e81, 0xd396acc5,
			0x0f6d6ff3, 0x83f44239, 0x2e0b4482, 0xa4842004,
			0x69c8f04a, 0x9e1f9b5e, 0x21c66842, 0xf6e96c9a,
			0x670c9c61, 0xabd388f0, 0x6a51a0d2, 0xd8542f68,
			0x960fa728, 0xab5133a3, 0x6eef0b6c, 0x137a3be4
		},
		{
			0xba3bf050, 0x7efb2a98, 0xa1f1651d, 0x39af0176,
			0x66ca593e, 0x82430e88, 0x8cee8619, 0x456f9fb4,
			0x7d84a5c3, 0x3b8b5ebe, 0xe06f75d8, 0x85c12073,
			0x401a449f, 0x56c16aa6, 0x4ed3aa62, 0x363f7706,
			0x1bfedf72, 0x429b023d, 0x37d0d724, 0xd00a1248,
			0xdb0fead3, 0x49f1c09b, 0x075372c9, 0x80991b7b,
			0x25d479d8, 0xf6e8def7, 0xe3fe501a, 0xb6794c3b,
			0x976ce0bd, 0x04c006ba, 0xc1a94fb6, 0x409f60c4
		}
	};

	private static final int[][] PCUBE = 
	{
		{
			 5, 14, 26, 18, 11, 28,  7, 16, 
			 0, 23, 20, 22,  1, 10,  4,  8,
			30,  3, 21,  9, 17, 24, 29,  6, 
			19, 12, 15, 13,  2, 25, 31, 27
		},
		{
			19,  9,  4, 20, 28, 17,  8, 22, 
			29, 14, 25, 12, 24, 30, 16, 26,
			31, 15,  7,  3,  1,  0, 18, 27, 
			13,  6, 21, 10, 23, 11,  5,  2
		},
		{
			24,  4,  0, 14,  2,  7, 28, 23, 
			26,  6, 30, 20, 18, 25, 19,  3,
			22, 11, 31, 21,  8, 27, 12,  9, 
			 1, 29,  5, 15, 17, 10, 16, 13
		},
		{
			27,  3, 21, 26, 17, 11, 20, 29, 
			19,  0, 12,  7, 13,  8, 31, 10,
			 5,  9, 14, 30, 18,  6, 28, 24,  
			 2, 23, 16, 22,  4,  1, 25, 15
		}
	};

	private final int out, rounds;
	
	private final int[] IN = new int[32];
	
	protected final int[] STATE = new int[8];
	protected long counter = 0;
	
	public HAVAL(int out, int passes) {
		super(128, evalOut(out) << 2);
		if(passes > 5 || passes < 3)
			throw new java.lang.IllegalArgumentException("HAVAL can only do 3, 4, or 5 passes");
		this.rounds = passes;
		this.out = out;
		reset();
	}
	
	public void reset()
	{
		super.reset();
		STATE[0] = 0x243f6a88;
		STATE[1] = 0x85a308d3;
		STATE[2] = 0x13198a2e;
		STATE[3] = 0x03707344;
		STATE[4] = 0xa4093822;
		STATE[5] = 0x299f31d0;
		STATE[6] = 0x082efa98;
		STATE[7] = 0xec4e6c89;
		counter = 0;
	}
	
	private static int evalOut(int size)
	{
		if(size > 8 || size < 4)
			throw new java.lang.IllegalArgumentException("HAVAL can only output 4-8 32-bit words");
		return size;
	}
	
	private static final int F1(int A, int B, int C, int D, int E, int F, int G)
	{
		return (F & C) ^ (E & B) ^ (D & A) ^ (G & F) ^ G;
	}

	private static final int F2(int A, int B, int C, int D, int E, int F, int G)
	{
		return (E & ((F & ~D) ^ (C & B) ^ A ^ G)) ^ (C & (F ^ B)) ^ ((D & B) ^ G);
	}

	private static final int F3(int A, int B, int C, int D, int E, int F, int G)
	{
		return (D & ((F & E) ^ A ^ G)) ^ (F & C) ^ (E & B) ^ G;
	}

	private static final int F4(int A, int B, int C, int D, int E, int F, int G)
	{
		return (D & ((F & E) ^ (C | A) ^ B)) ^ (C & ((~E & B) ^ F ^ A ^ G)) ^ (E & A) ^ G;
	}

	private static final int F5(int A, int B, int C, int D, int E, int F, int G)
	{
		return (G & ~((F & E & D) ^ B)) ^ (F & C) ^ (E & B) ^ (D & A);
	}
	
	public void processNext(byte[] bytes, int offset)
	{
		processNext(bytes, offset, true);
	}

	public void processNext(byte[] bytes, int offset, boolean count)
	{
		if(count)
			counter += 1024;
		Bits.bytesToInts(bytes, offset, IN, 0, 32);
		int A = STATE[0],
			B = STATE[1],
			C = STATE[2],
			D = STATE[3],
			E = STATE[4],
			F = STATE[5],
			G = STATE[6],
			H = STATE[7];
		
		switch(rounds)
		{
			case 3:
				HAVAL3(IN);
				break;
			case 4:
				HAVAL4(IN);
				break;
			case 5:
				HAVAL5(IN);
				break;
		}
		
		STATE[0] += A;
		STATE[1] += B;
		STATE[2] += C;
		STATE[3] += D;
		STATE[4] += E;
		STATE[5] += F;
		STATE[6] += G;
		STATE[7] += H;
	}
	
	private void HAVAL3(int[] IN)
	{
		int A = STATE[0],
			B = STATE[1],
			C = STATE[2],
			D = STATE[3],
			E = STATE[4],
			F = STATE[5],
			G = STATE[6],
			H = STATE[7];
		int i;
		for(i = 0; i < 32;) 
		{
			H = Bits.rotateLeft(F1(B, A, D, F, G, C, E), 25) + 
				Bits.rotateLeft(H, 21) + IN[i++];
			G = Bits.rotateLeft(F1(A, H, C, E, F, B, D), 25) + 
				Bits.rotateLeft(G, 21) + IN[i++];
			F = Bits.rotateLeft(F1(H, G, B, D, E, A, C), 25) +
				Bits.rotateLeft(F, 21) + IN[i++];
			E = Bits.rotateLeft(F1(G, F, A, C, D, H, B), 25) + 
				Bits.rotateLeft(E, 21) + IN[i++];
			D = Bits.rotateLeft(F1(F, E, H, B, C, G, A), 25) + 
				Bits.rotateLeft(D, 21) + IN[i++];
			C = Bits.rotateLeft(F1(E, D, G, A, B, F, H), 25) + 
				Bits.rotateLeft(C, 21) + IN[i++];
			B = Bits.rotateLeft(F1(D, C, F, H, A, E, G), 25) +
				Bits.rotateLeft(B, 21) + IN[i++];
			A = Bits.rotateLeft(F1(C, B, E, G, H, D, F), 25) +
				Bits.rotateLeft(A, 21) + IN[i++];
		}
		for(i = 0; i < 32;) 
		{
			H = Bits.rotateLeft(F2(E, C, B, A, F, D, G), 25) +
				Bits.rotateLeft(H, 21) + IN[PCUBE[0][i]] + KCUBE[0][i++];
			G = Bits.rotateLeft(F2(D, B, A, H, E, C, F), 25) +
				Bits.rotateLeft(G, 21) + IN[PCUBE[0][i]] + KCUBE[0][i++];
			F = Bits.rotateLeft(F2(C, A, H, G, D, B, E), 25) +
				Bits.rotateLeft(F, 21) + IN[PCUBE[0][i]] + KCUBE[0][i++];
			E = Bits.rotateLeft(F2(B, H, G, F, C, A, D), 25) +
				Bits.rotateLeft(E, 21) + IN[PCUBE[0][i]] + KCUBE[0][i++];
			D = Bits.rotateLeft(F2(A, G, F, E, B, H, C), 25) +
				Bits.rotateLeft(D, 21) + IN[PCUBE[0][i]] + KCUBE[0][i++];
			C = Bits.rotateLeft(F2(H, F, E, D, A, G, B), 25) +
				Bits.rotateLeft(C, 21) + IN[PCUBE[0][i]] + KCUBE[0][i++];
			B = Bits.rotateLeft(F2(G, E, D, C, H, F, A), 25) +
				Bits.rotateLeft(B, 21) + IN[PCUBE[0][i]] + KCUBE[0][i++];
			A = Bits.rotateLeft(F2(F, D, C, B, G, E, H), 25) +
				Bits.rotateLeft(A, 21) + IN[PCUBE[0][i]] + KCUBE[0][i++];
		}
		for (i = 0; i < 32;) 
		{
			H = Bits.rotateLeft(F3(G, B, C, D, E, F, A), 25) +
				Bits.rotateLeft(H, 21) + IN[PCUBE[1][i]] + KCUBE[1][i++];
			G = Bits.rotateLeft(F3(F, A, B, C, D, E, H), 25) +
				Bits.rotateLeft(G, 21) + IN[PCUBE[1][i]] + KCUBE[1][i++];
			F = Bits.rotateLeft(F3(E, H, A, B, C, D, G), 25) +
				Bits.rotateLeft(F, 21) + IN[PCUBE[1][i]] + KCUBE[1][i++];
			E = Bits.rotateLeft(F3(D, G, H, A, B, C, F), 25) +
				Bits.rotateLeft(E, 21) + IN[PCUBE[1][i]] + KCUBE[1][i++];
			D = Bits.rotateLeft(F3(C, F, G, H, A, B, E), 25) +
				Bits.rotateLeft(D, 21) + IN[PCUBE[1][i]] + KCUBE[1][i++];
			C = Bits.rotateLeft(F3(B, E, F, G, H, A, D), 25) +
				Bits.rotateLeft(C, 21) + IN[PCUBE[1][i]] + KCUBE[1][i++];
			B = Bits.rotateLeft(F3(A, D, E, F, G, H, C), 25) +
				Bits.rotateLeft(B, 21) + IN[PCUBE[1][i]] + KCUBE[1][i++];
			A = Bits.rotateLeft(F3(H, C, D, E, F, G, B), 25) +
				Bits.rotateLeft(A, 21) + IN[PCUBE[1][i]] + KCUBE[1][i++];
		}
		STATE[0] = A;
		STATE[1] = B;
		STATE[2] = C;
		STATE[3] = D;
		STATE[4] = E;
		STATE[5] = F;
		STATE[6] = G;
		STATE[7] = H;
	}
	
	private void HAVAL4(int[] IN)
	{
		int A = STATE[0],
			B = STATE[1],
			C = STATE[2],
			D = STATE[3],
			E = STATE[4],
			F = STATE[5],
			G = STATE[6],
			H = STATE[7],
			i;
		for(i = 0; i < 32;) 
		{
			H = Bits.rotateLeft(F1(C, G, B, E, F, D, A), 25)
				+ Bits.rotateLeft(H, 21) + IN[i++];
			G = Bits.rotateLeft(F1(B, F, A, D, E, C, H), 25)
				+ Bits.rotateLeft(G, 21) + IN[i++];
			F = Bits.rotateLeft(F1(A, E, H, C, D, B, G), 25)
				+ Bits.rotateLeft(F, 21) + IN[i++];
			E = Bits.rotateLeft(F1(H, D, G, B, C, A, F), 25)
				+ Bits.rotateLeft(E, 21) + IN[i++];
			D = Bits.rotateLeft(F1(G, C, F, A, B, H, E), 25)
				+ Bits.rotateLeft(D, 21) + IN[i++];
			C = Bits.rotateLeft(F1(F, B, E, H, A, G, D), 25)
				+ Bits.rotateLeft(C, 21) + IN[i++];
			B = Bits.rotateLeft(F1(E, A, D, G, H, F, C), 25)
				+ Bits.rotateLeft(B, 21) + IN[i++];
			A = Bits.rotateLeft(F1(D, H, C, F, G, E, B), 25)
				+ Bits.rotateLeft(A, 21) + IN[i++];
		}
		for(i = 0; i < 32;)
		{
			H = Bits.rotateLeft(F2(D, F, C, A, B, G, E), 25)
				+ Bits.rotateLeft(H, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			G = Bits.rotateLeft(F2(C, E, B, H, A, F, D), 25)
				+ Bits.rotateLeft(G, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			F = Bits.rotateLeft(F2(B, D, A, G, H, E, C), 25)
				+ Bits.rotateLeft(F, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			E = Bits.rotateLeft(F2(A, C, H, F, G, D, B), 25)
				+ Bits.rotateLeft(E, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			D = Bits.rotateLeft(F2(H, B, G, E, F, C, A), 25)
				+ Bits.rotateLeft(D, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			C = Bits.rotateLeft(F2(G, A, F, D, E, B, H), 25)
				+ Bits.rotateLeft(C, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			B = Bits.rotateLeft(F2(F, H, E, C, D, A, G), 25)
				+ Bits.rotateLeft(B, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			A = Bits.rotateLeft(F2(E, G, D, B, C, H, F), 25)
				+ Bits.rotateLeft(A, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
		}
		for(i = 0; i < 32;) 
		{
			H = Bits.rotateLeft(F3(B, E, D, G, A, C, F), 25)
				+ Bits.rotateLeft(H, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			G = Bits.rotateLeft(F3(A, D, C, F, H, B, E), 25)
				+ Bits.rotateLeft(G, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			F = Bits.rotateLeft(F3(H, C, B, E, G, A, D), 25)
				+ Bits.rotateLeft(F, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			E = Bits.rotateLeft(F3(G, B, A, D, F, H, C), 25)
				+ Bits.rotateLeft(E, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			D = Bits.rotateLeft(F3(F, A, H, C, E, G, B), 25)
				+ Bits.rotateLeft(D, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			C = Bits.rotateLeft(F3(E, H, G, B, D, F, A), 25)
				+ Bits.rotateLeft(C, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			B = Bits.rotateLeft(F3(D, G, F, A, C, E, H), 25)
				+ Bits.rotateLeft(B, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			A = Bits.rotateLeft(F3(C, F, E, H, B, D, G), 25)
				+ Bits.rotateLeft(A, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
		}
		for(i = 0; i < 32;) 
		{
			H = Bits.rotateLeft(F4(G, E, A, F, C, B, D), 25)
				+ Bits.rotateLeft(H, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			G = Bits.rotateLeft(F4(F, D, H, E, B, A, C), 25)
				+ Bits.rotateLeft(G, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			F = Bits.rotateLeft(F4(E, C, G, D, A, H, B), 25)
				+ Bits.rotateLeft(F, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			E = Bits.rotateLeft(F4(D, B, F, C, H, G, A), 25)
				+ Bits.rotateLeft(E, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			D = Bits.rotateLeft(F4(C, A, E, B, G, F, H), 25)
				+ Bits.rotateLeft(D, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			C = Bits.rotateLeft(F4(B, H, D, A, F, E, G), 25)
				+ Bits.rotateLeft(C, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			B = Bits.rotateLeft(F4(A, G, C, H, E, D, F), 25)
				+ Bits.rotateLeft(B, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			A = Bits.rotateLeft(F4(H, F, B, G, D, C, E), 25)
				+ Bits.rotateLeft(A, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
		}
		STATE[0] = A;
		STATE[1] = B;
		STATE[2] = C;
		STATE[3] = D;
		STATE[4] = E;
		STATE[5] = F;
		STATE[6] = G;
		STATE[7] = H;
	}
	
	private void HAVAL5(int[] IN)
	{
		int A = STATE[0],
			B = STATE[1],
			C = STATE[2],
			D = STATE[3],
			E = STATE[4],
			F = STATE[5],
			G = STATE[6],
			H = STATE[7],
			i;
		
		for(i = 0; i < 32;) 
		{
			H = Bits.rotateLeft(F1(D, E, B, A, F, C, G), 25)
				+ Bits.rotateLeft(H, 21) + IN[i++];
			G = Bits.rotateLeft(F1(C, D, A, H, E, B, F), 25)
				+ Bits.rotateLeft(G, 21) + IN[i++];
			F = Bits.rotateLeft(F1(B, C, H, G, D, A, E), 25)
				+ Bits.rotateLeft(F, 21) + IN[i++];
			E = Bits.rotateLeft(F1(A, B, G, F, C, H, D), 25)
				+ Bits.rotateLeft(E, 21) + IN[i++];
			D = Bits.rotateLeft(F1(H, A, F, E, B, G, C), 25)
				+ Bits.rotateLeft(D, 21) + IN[i++];
			C = Bits.rotateLeft(F1(G, H, E, D, A, F, B), 25)
				+ Bits.rotateLeft(C, 21) + IN[i++];
			B = Bits.rotateLeft(F1(F, G, D, C, H, E, A), 25)
				+ Bits.rotateLeft(B, 21) + IN[i++];
			A = Bits.rotateLeft(F1(E, F, C, B, G, D, H), 25)
				+ Bits.rotateLeft(A, 21) + IN[i++];
		}
		for(i = 0; i < 32;) 
		{		
			H = Bits.rotateLeft(F2(G, C, B, A, D, E, F), 25)
				+ Bits.rotateLeft(H, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			G = Bits.rotateLeft(F2(F, B, A, H, C, D, E), 25)
				+ Bits.rotateLeft(G, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			F = Bits.rotateLeft(F2(E, A, H, G, B, C, D), 25)
				+ Bits.rotateLeft(F, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			E = Bits.rotateLeft(F2(D, H, G, F, A, B, C), 25)
				+ Bits.rotateLeft(E, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			D = Bits.rotateLeft(F2(C, G, F, E, H, A, B), 25)
				+ Bits.rotateLeft(D, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			C = Bits.rotateLeft(F2(B, F, E, D, G, H, A), 25)
				+ Bits.rotateLeft(C, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			B = Bits.rotateLeft(F2(A, E, D, C, F, G, H), 25)
				+ Bits.rotateLeft(B, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
			A = Bits.rotateLeft(F2(H, D, C, B, E, F, G), 25)
				+ Bits.rotateLeft(A, 21)
				+ IN[PCUBE[0][i]] + KCUBE[0][i++];
		}
		for(i = 0; i < 32;)
		{
			H = Bits.rotateLeft(F3(C, G, A, E, D, B, F), 25)
				+ Bits.rotateLeft(H, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			G = Bits.rotateLeft(F3(B, F, H, D, C, A, E), 25)
				+ Bits.rotateLeft(G, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			F = Bits.rotateLeft(F3(A, E, G, C, B, H, D), 25)
				+ Bits.rotateLeft(F, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			E = Bits.rotateLeft(F3(H, D, F, B, A, G, C), 25)
				+ Bits.rotateLeft(E, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			D = Bits.rotateLeft(F3(G, C, E, A, H, F, B), 25)
				+ Bits.rotateLeft(D, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			C = Bits.rotateLeft(F3(F, B, D, H, G, E, A), 25)
				+ Bits.rotateLeft(C, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			B = Bits.rotateLeft(F3(E, A, C, G, F, D, H), 25)
				+ Bits.rotateLeft(B, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
			A = Bits.rotateLeft(F3(D, H, B, F, E, C, G), 25)
				+ Bits.rotateLeft(A, 21)
				+ IN[PCUBE[1][i]] + KCUBE[1][i++];
		}
		for(i = 0; i < 32;)
		{
			H = Bits.rotateLeft(F4(B, F, D, C, A, E, G), 25)
				+ Bits.rotateLeft(H, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			G = Bits.rotateLeft(F4(A, E, C, B, H, D, F), 25)
				+ Bits.rotateLeft(G, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			F = Bits.rotateLeft(F4(H, D, B, A, G, C, E), 25)
				+ Bits.rotateLeft(F, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			E = Bits.rotateLeft(F4(G, C, A, H, F, B, D), 25)
				+ Bits.rotateLeft(E, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			D = Bits.rotateLeft(F4(F, B, H, G, E, A, C), 25)
				+ Bits.rotateLeft(D, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			C = Bits.rotateLeft(F4(E, A, G, F, D, H, B), 25)
				+ Bits.rotateLeft(C, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			B = Bits.rotateLeft(F4(D, H, F, E, C, G, A), 25)
				+ Bits.rotateLeft(B, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
			A = Bits.rotateLeft(F4(C, G, E, D, B, F, H), 25)
				+ Bits.rotateLeft(A, 21)
				+ IN[PCUBE[2][i]] + KCUBE[2][i++];
		}
		for(i = 0; i < 32;) {
			H = Bits.rotateLeft(F5(C, F, A, G, E, D, B), 25)
				+ Bits.rotateLeft(H, 21)
				+ IN[PCUBE[3][i]] + KCUBE[3][i++];
			G = Bits.rotateLeft(F5(B, E, H, F, D, C, A), 25)
				+ Bits.rotateLeft(G, 21)
				+ IN[PCUBE[3][i]] + KCUBE[3][i++];
			F = Bits.rotateLeft(F5(A, D, G, E, C, B, H), 25)
				+ Bits.rotateLeft(F, 21)
				+ IN[PCUBE[3][i]] + KCUBE[3][i++];
			E = Bits.rotateLeft(F5(H, C, F, D, B, A, G), 25)
				+ Bits.rotateLeft(E, 21)
				+ IN[PCUBE[3][i]] + KCUBE[3][i++];
			D = Bits.rotateLeft(F5(G, B, E, C, A, H, F), 25)
				+ Bits.rotateLeft(D, 21)
				+ IN[PCUBE[3][i]] + KCUBE[3][i++];
			C = Bits.rotateLeft(F5(F, A, D, B, H, G, E), 25)
				+ Bits.rotateLeft(C, 21)
				+ IN[PCUBE[3][i]] + KCUBE[3][i++];
			B = Bits.rotateLeft(F5(E, H, C, A, G, F, D), 25)
				+ Bits.rotateLeft(B, 21)
				+ IN[PCUBE[3][i]] + KCUBE[3][i++];
			A = Bits.rotateLeft(F5(D, G, B, H, F, E, C), 25)
				+ Bits.rotateLeft(A, 21)
				+ IN[PCUBE[3][i]] + KCUBE[3][i++];
		}
		
		STATE[0] = A;
		STATE[1] = B;
		STATE[2] = C;
		STATE[3] = D;
		STATE[4] = E;
		STATE[5] = F;
		STATE[6] = G;
		STATE[7] = H;
	}
	
	private static final int M128(int A, int B, int C, int D, int n)
	{
		int T = (A & 0x000000FF) |
			    (B & 0x0000FF00) |
			    (C & 0x00FF0000) |
				(D & 0xFF000000);
		if(n > 0)
			return Bits.rotateLeft(T, n);
		else
			return T;
	}
	
	private static final int M160_0(int A, int B, int C)
	{
		return Bits.rotateLeft((A & 0x01F80000) |
				 			   (B & 0xFE000000) | 
				 			   (C & 0x0000003F), 13);
	}

	private static final int M160_1(int A, int B, int C)
	{
		return Bits.rotateLeft((A & 0xFE000000) |
							   (B & 0x0000003F) | 
							   (C & 0x00000FC0), 7);
	}

	private static final int M160_2(int A, int B, int C)
	{
		return (A & 0x0000003F) |
			   (B & 0x00000FC0) |
			   (C & 0x0007F000);
	}

	private static final int M160_3(int A, int B, int C)
	{
		return ((A & 0x00000FC0) |
			    (B & 0x0007F000) |
			    (C & 0x01F80000)) >>> 6;
	}

	private static final int M160_4(int A, int B, int C)
	{
		return ((A & 0x0007F000) |
				(B & 0x01F80000) |
				(C & 0xFE000000)) >>> 12;
	}

	private static final int M192_0(int A, int B)
	{
		return Bits.rotateLeft((A & 0xFC000000) | (B & 0x0000001F), 6);
	}

	private static final int M192_1(int A, int B)
	{
		return (A & 0x0000001F) | (B & 0x000003E0);
	}

	private static final int M192_2(int A, int B)
	{
		return ((A & 0x000003E0) | (B & 0x0000FC00)) >>> 5;
	}

	private static final int M192_3(int A, int B)
	{
		return ((A & 0x0000FC00) | (B & 0x001F0000)) >>> 10;
	}

	private static final int M192_4(int A, int B)
	{
		return ((A & 0x001F0000) | (B & 0x03E00000)) >>> 16;
	}

	private static final int M192_5(int A, int B)
	{
		return ((A & 0x03E00000) | (B & 0xFC000000)) >>> 21;
	}
	
	private final void R128(byte[] out, int start)
	{
		Bits.intToBytes(STATE[0] + M128(STATE[7], STATE[4], STATE[5], STATE[6], 24), out, start     );
		Bits.intToBytes(STATE[1] + M128(STATE[6], STATE[7], STATE[4], STATE[5], 16), out, start + 4 );
		Bits.intToBytes(STATE[2] + M128(STATE[5], STATE[6], STATE[7], STATE[4],  8), out, start + 8 );
		Bits.intToBytes(STATE[3] + M128(STATE[4], STATE[5], STATE[6], STATE[7],  0), out, start + 12);
	}
	
	private final void R160(byte[] out, int start)
	{
		Bits.intToBytes(STATE[0] + M160_0(STATE[5], STATE[6], STATE[7]), out, start     );
		Bits.intToBytes(STATE[1] + M160_1(STATE[5], STATE[6], STATE[7]), out, start + 4 );
		Bits.intToBytes(STATE[2] + M160_2(STATE[5], STATE[6], STATE[7]), out, start + 8 );
		Bits.intToBytes(STATE[3] + M160_3(STATE[5], STATE[6], STATE[7]), out, start + 12);
		Bits.intToBytes(STATE[4] + M160_4(STATE[5], STATE[6], STATE[7]), out, start + 16);
	}

	private final void R192(byte[] out, int start)
	{
		Bits.intToBytes(STATE[0] + M192_0(STATE[6], STATE[7]), out, start     );
		Bits.intToBytes(STATE[1] + M192_1(STATE[6], STATE[7]), out, start + 4 );
		Bits.intToBytes(STATE[2] + M192_2(STATE[6], STATE[7]), out, start + 8 );
		Bits.intToBytes(STATE[3] + M192_3(STATE[6], STATE[7]), out, start + 12);
		Bits.intToBytes(STATE[4] + M192_4(STATE[6], STATE[7]), out, start + 16);
		Bits.intToBytes(STATE[5] + M192_5(STATE[6], STATE[7]), out, start + 20);
	}

	private final void R224(byte[] out, int start)
	{
		Bits.intToBytes(STATE[0] + ((STATE[7] >>> 27) & 0x1F), out, start	  );
		Bits.intToBytes(STATE[1] + ((STATE[7] >>> 22) & 0x1F), out, start + 4 );
		Bits.intToBytes(STATE[2] + ((STATE[7] >>> 18) & 0x0F), out, start + 8 );
		Bits.intToBytes(STATE[3] + ((STATE[7] >>> 13) & 0x1F), out, start + 12);
		Bits.intToBytes(STATE[4] + ((STATE[7] >>>  9) & 0x0F), out, start + 16);
		Bits.intToBytes(STATE[5] + ((STATE[7] >>>  4) & 0x1F), out, start + 20);
		Bits.intToBytes(STATE[6] + ((STATE[7]       ) & 0x0F), out, start + 24);
	}

	private final void R256(byte[] out, int start)
	{
		Bits.intToBytes(STATE[0], out, start     );
		Bits.intToBytes(STATE[1], out, start + 4 );
		Bits.intToBytes(STATE[2], out, start + 8 );
		Bits.intToBytes(STATE[3], out, start + 12);
		Bits.intToBytes(STATE[4], out, start + 16);
		Bits.intToBytes(STATE[5], out, start + 20);
		Bits.intToBytes(STATE[6], out, start + 24);
		Bits.intToBytes(STATE[7], out, start + 28);
	}
	
	private final void out(byte[] out, int start)
	{
		switch(this.out)
		{
			case 4:
				R128(out, start);
				break;
			case 5:
				R160(out, start);
				break;
			case 6:
				R192(out, start);
				break;
			case 7:
				R224(out, start);
				break;
			case 8:
				R256(out, start);
				break;
		}
	}

	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		byte[] bytes = new byte[128];
		System.arraycopy(remaining, 0, bytes, 0, pos);
		counter += pos << 3;
		bytes[pos] = 0x01;
		if(pos >= 118)
		{
			processNext(bytes, 0, false);
			Arrays.fill(bytes, (byte) 0x00);
		}
		bytes[118] = (byte) (0x01 | (rounds << 3));
		bytes[119] = (byte) (this.out << 3);
		Bits.longToBytes(counter, bytes, 120);
		processNext(bytes, 0, false);
		out(out, start);
		reset();
	}
	
	public HAVALState getState()
	{
		return new HAVALState(this);
	}

	public void updateState(HAVALState state)
	{
		state.update(this);
	}

	public void loadCustom(HAVALState state)
	{
		System.arraycopy(state.state, 0, STATE, 0, 8);
		counter = state.counter;
	}
	
	public static final HAVALFactory factory = new HAVALFactory();
	public static final HAVALStateFactory sfactory = new HAVALStateFactory();
	
	protected static final class HAVALState extends MerkleState<HAVALState, HAVAL>
	{
		protected int[] state;
		protected long counter;
		
		public HAVALState(byte[] bytes, int start)
		{
			super(bytes, start);
		}
		
		public HAVALState(HAVAL hl)
		{
			super(hl);
		}

		public Factory<HAVALState> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.HAVALSTATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeInts(state);
			os.writeLong(counter);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.intsToBytes(state, 0, bytes, start, 8); start += 32;
			Bits.longToBytes(counter, bytes, start);
		}

		protected void addCustom(IIncomingStream os) throws IOException
		{
			state = os.readInts(8);
			counter = os.readLong();
		}

		protected void addCustom(byte[] bytes, int start)
		{
			state = new int[8];
			Bits.bytesToInts(bytes, start, state, 0, 8); start += 32;
			counter = Bits.longFromBytes(bytes, start);
		}

		protected void addCustom(HAVAL hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			counter = hash.counter;
		}

		protected void updateCustom(HAVAL hash)
		{
			if(state == null)
				state = ArrayUtil.copy(hash.STATE);
			else
				System.arraycopy(hash.STATE, 0, state, 0, 8);
			counter = hash.counter;
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, 0);
			state = null;
			counter = 0;
		}

		protected boolean compareCustom(HAVALState state)
		{
			return counter == state.counter && ArrayUtil.equals(state.state, this.state);
		}

		protected int customSize()
		{
			return 40;
		}
	}
	
	protected static final class HAVALStateFactory extends MerkleStateFactory<HAVALState, HAVAL>
	{
		public HAVALStateFactory()
		{
			super(HAVALState.class, 128);
		}
		
		public HAVALState construct(byte[] bytes, int pos)
		{
			return new HAVALState(bytes, pos);
		}
	}
	
	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		HAVAL blake = new HAVAL(8, 5);
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}

	public HashFactory<HAVAL> factory()
	{
		return factory;
	}
	
	private static final class HAVALFactory extends HashFactory<HAVAL>
	{

		public HAVAL build(CryptoString str)
		{
			return new HAVAL(str.nextArg().toInt() / 32, str.nextArg().toInt());
		}
		
	}
	
}
package claire.util.crypto.hash.primitive;

import java.util.Arrays;

import claire.util.math.counters.LongCounter;
import claire.util.memory.Bits;

abstract class BLAKE_Base_64 
	     extends MerkleHash {
	
	private static final int[] PERMUTE = 
	{
		 0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
		14, 10,  4,  8,  9, 15, 13,  6,  1, 12,  0,  2, 11,  7,  5,  3,
		11,  8, 12,  0,  5,  2, 15, 13, 10, 14,  3,  6,  7,  1,  9,  4,
		 7,  9,  3,  1, 13, 12, 11, 14,  2,  6,  5, 10,  4,  0, 15,  8,
		 9,  0,  5,  7,  2,  4, 10, 15, 14,  1, 11, 12,  6,  8,  3, 13,
		 2, 12,  6, 10,  0, 11,  8,  3,  4, 13,  7,  5, 15, 14,  1,  9,
		12,  5,  1, 15, 14, 13,  4, 10,  0,  7,  6,  3,  9,  2,  8, 11,
		13, 11,  7, 14, 12,  1,  3,  9,  5,  0, 15,  4,  8,  6,  2, 10,
		 6, 15, 14,  9, 11,  3,  0,  8, 12,  2, 13,  7,  1,  4, 10,  5,
		10,  2,  8,  4,  7,  6,  1,  5, 15, 11,  9, 14,  3, 12, 13,  0,
		 0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
		14, 10,  4,  8,  9, 15, 13,  6,  1, 12,  0,  2, 11,  7,  5,  3,
		11,  8, 12,  0,  5,  2, 15, 13, 10, 14,  3,  6,  7,  1,  9,  4,
		 7,  9,  3,  1, 13, 12, 11, 14,  2,  6,  5, 10,  4,  0, 15,  8,
		 9,  0,  5,  7,  2,  4, 10, 15, 14,  1, 11, 12,  6,  8,  3, 13,
		 2, 12,  6, 10,  0, 11,  8,  3,  4, 13,  7,  5, 15, 14,  1,  9
	};

	private static final long[] KEY = 
	{
		0x243f6a8885a308d3L, 0x13198a2e03707344L,
		0xa4093822299f31d0L, 0x082efa98ec4e6c89L,
		0x452821e638d01377L, 0xbe5466cf34e90c6cL,
		0xc0ac29b7c97c50ddL, 0x3f84d5b5b5470917L,
		0x9216d5d98979fb1bL, 0xd1310ba698dfb5acL,
		0x2ffd72dbd01adfb7L, 0xb8e1afed6a267e96L,
		0xba7c9045f12c7f99L, 0x24a19947b3916cf7L,
		0x0801f2e2858efc16L, 0x636920d871574e69L
	};
	
	protected final long[] STATE = new long[8];
	
	private final long[] WORK = new long[16];

	private LongCounter counter = new LongCounter(2);
	private final long[] counters = counter.getLongs();
	
	protected BLAKE_Base_64(int out)
	{
		super(128, out);
		reset();
	}
	
	protected abstract long[] getIV();
	protected abstract void output(byte[] out, int start);
	
	private void reset()
	{
		System.arraycopy(this.getIV(), 0, STATE, 0, 8);
		counter.reset();
	}
	
	public void processNext(byte[] bytes, int offset)
	{
		this.processNext(bytes, offset, true);
	}

	public void processNext(byte[] bytes, int offset, boolean count)
	{
		if(count)
			counter.add(512);
		long A = STATE[0];
		long B = STATE[1];
		long C = STATE[2];
		long D = STATE[3];
		long E = STATE[4];
		long F = STATE[5];
		long G = STATE[6];
		long H = STATE[7];
		long I = 0x243f6a8885a308d3L;
		long J = 0x13198a2e03707344L;
		long K = 0xa4093822299f31d0L;
		long L = 0x082efa98ec4e6c89L;
		long M = counters[0] ^ 0x452821e638d01377L;
		long N = counters[0] ^ 0xbe5466cf34e90c6cL;
		long O = counters[1] ^ 0xc0ac29b7c97c50ddL;
		long P = counters[1] ^ 0x3f84d5b5b5470917L;
		Bits.BigEndian.bytesToLongs(bytes, offset, WORK, 0, 16);
		int pos = 0, T1, T2;
		for(int i = 0; i < 16; i ++) {
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			A += E + (WORK[T1] ^ KEY[T2]);
			M = Bits.rotateRight(M ^ A, 32);
			I += M;
			E = Bits.rotateRight(E ^ I, 25);
			A += E + (WORK[T2] ^ KEY[T1]);
			M = Bits.rotateRight(M ^ A, 16);
			I += M;
			E = Bits.rotateRight(E ^ I, 11);
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			B += F + (WORK[T1] ^ KEY[T2]);
			N = Bits.rotateRight(N ^ B, 32);
			J += N;
			F = Bits.rotateRight(F ^ J, 25);
			B += F + (WORK[T2] ^ KEY[T1]);
			N = Bits.rotateRight(N ^ B, 16);
			J += N;
			F = Bits.rotateRight(F ^ J, 11);
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			C += G + (WORK[T1] ^ KEY[T2]);
			O = Bits.rotateRight(O ^ C, 32);
			K += O;
			G = Bits.rotateRight(G ^ K, 25);
			C += G + (WORK[T2] ^ KEY[T1]);
			O = Bits.rotateRight(O ^ C, 16);
			K += O;
			G = Bits.rotateRight(G ^ K, 11);
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			D += H + (WORK[T1] ^ KEY[T2]);
			P = Bits.rotateRight(P ^ D, 32);
			L += P;
			H = Bits.rotateRight(H ^ L, 25);
			D += H + (WORK[T2] ^ KEY[T1]);
			P = Bits.rotateRight(P ^ D, 16);
			L += P;
			H = Bits.rotateRight(H ^ L, 11);
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			A += F + (WORK[T1] ^ KEY[T2]);
			P = Bits.rotateRight(P ^ A, 32);
			K += P;
			F = Bits.rotateRight(F ^ K, 25);
			A += F + (WORK[T2] ^ KEY[T1]);
			P = Bits.rotateRight(P ^ A, 16);
			K += P;
			F = Bits.rotateRight(F ^ K, 11);
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			B += G + (WORK[T1] ^ KEY[T2]);
			M = Bits.rotateRight(M ^ B, 32);
			L += M;
			G = Bits.rotateRight(G ^ L, 25);
			B += G + (WORK[T2] ^ KEY[T1]);
			M = Bits.rotateRight(M ^ B, 16);
			L += M;
			G = Bits.rotateRight(G ^ L, 11);
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			C += H + (WORK[T1] ^ KEY[T2]);
			N = Bits.rotateRight(N ^ C, 32);
			I += N;
			H = Bits.rotateRight(H ^ I, 25);
			C += H + (WORK[T2] ^ KEY[T1]);
			N = Bits.rotateRight(N ^ C, 16);
			I += N;
			H = Bits.rotateRight(H ^ I, 11);
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			D += E + (WORK[T1] ^ KEY[T2]);
			O = Bits.rotateRight(O ^ D, 32);
			J += O;
			E = Bits.rotateRight(E ^ J, 25);
			D += E + (WORK[T2] ^ KEY[T1]);
			O = Bits.rotateRight(O ^ D, 16);
			J += O;
			E = Bits.rotateRight(E ^ J, 11);
		}
		STATE[0] ^= A ^ I;
		STATE[1] ^= B ^ J;
		STATE[2] ^= C ^ K;
		STATE[3] ^= D ^ L;
		STATE[4] ^= E ^ M;
		STATE[5] ^= F ^ N;
		STATE[6] ^= G ^ O;
		STATE[7] ^= H ^ P;
	}

	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		counter.add(pos << 3);
		Arrays.fill(remaining, pos, 128, (byte) 0);
		long c1 = counters[1],
			 c2 = counters[0];
		remaining[pos] = (byte) 0x80;
		if(pos >= 111)
		{
			processNext(remaining, 0, false);
			Arrays.fill(remaining, (byte) 0);
		}
		if(this.outputLength() == 64)
			remaining[111] = 0x01;
		Bits.BigEndian.longToBytes(c1, remaining, 112);
		Bits.BigEndian.longToBytes(c2, remaining, 120);
		processNext(remaining, 0, false);
		this.output(out, start);
		reset();
	}

}

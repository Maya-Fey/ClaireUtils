package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.BLAKE_Base_32.BLAKE_32State;
import claire.util.io.Factory;
import claire.util.math.counters.IntCounter;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

abstract class BLAKE_Base_32<Hash extends BLAKE_Base_32<Hash>> 
	     extends MerkleHash<BLAKE_32State, Hash> {
	
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
		 7,  9,  3,  1, 13, 12, 11, 14,  2,  6,  5, 10,  4,  0, 15,  8
	};

	private static final int[] KEY = 
	{
		0x243F6A88, 0x85A308D3, 0x13198A2E, 0x03707344,
		0xA4093822, 0x299F31D0, 0x082EFA98, 0xEC4E6C89,
		0x452821E6, 0x38D01377, 0xBE5466CF, 0x34E90C6C,
		0xC0AC29B7, 0xC97C50DD, 0x3F84D5B5, 0xB5470917
	};
	
	protected final int[] STATE = new int[8];
	protected final int[] WORK = new int[16];
	protected final int[] counters = new int[2];
	
	private final IntCounter counter = new IntCounter(counters);
	
	protected BLAKE_Base_32(int out)
	{
		super(64, out);
		reset();
	}
	
	protected abstract int[] getIV();
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
		int A = STATE[0];
		int B = STATE[1];
		int C = STATE[2];
		int D = STATE[3];
		int E = STATE[4];
		int F = STATE[5];
		int G = STATE[6];
		int H = STATE[7];
		int I = 0x243f6a88;
		int J = 0x85a308d3;
		int K = 0x13198a2e;
		int L = 0x03707344;
		int M = counters[0] ^ 0xa4093822;
		int N = counters[0] ^ 0x299f31d0;
		int O = counters[1] ^ 0x082efa98;
		int P = counters[1] ^ 0xec4e6c89;
		Bits.BigEndian.bytesToInts(bytes, offset, WORK, 0, 16);
		int pos = 0, T1, T2;
		for(int i = 0; i < 14; i ++) {
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			A += E + (WORK[T1] ^ KEY[T2]);
			M = Bits.rotateRight(M ^ A, 16);
			I += M;
			E = Bits.rotateRight(E ^ I, 12);
			A += E + (WORK[T2] ^ KEY[T1]);
			M = Bits.rotateRight(M ^ A, 8 );
			I += M;
			E = Bits.rotateRight(E ^ I, 7 );
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			B += F + (WORK[T1] ^ KEY[T2]);
			N = Bits.rotateRight(N ^ B, 16);
			J += N;
			F = Bits.rotateRight(F ^ J, 12);
			B += F + (WORK[T2] ^ KEY[T1]);
			N = Bits.rotateRight(N ^ B, 8 );
			J += N;
			F = Bits.rotateRight(F ^ J, 7 );
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			C += G + (WORK[T1] ^ KEY[T2]);
			O = Bits.rotateRight(O ^ C, 16);
			K += O;
			G = Bits.rotateRight(G ^ K, 12);
			C += G + (WORK[T2] ^ KEY[T1]);
			O = Bits.rotateRight(O ^ C, 8 );
			K += O;
			G = Bits.rotateRight(G ^ K, 7 );
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			D += H + (WORK[T1] ^ KEY[T2]);
			P = Bits.rotateRight(P ^ D, 16);
			L += P;
			H = Bits.rotateRight(H ^ L, 12);
			D += H + (WORK[T2] ^ KEY[T1]);
			P = Bits.rotateRight(P ^ D, 8 );
			L += P;
			H = Bits.rotateRight(H ^ L, 7 );
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			A += F + (WORK[T1] ^ KEY[T2]);
			P = Bits.rotateRight(P ^ A, 16);
			K += P;
			F = Bits.rotateRight(F ^ K, 12);
			A += F + (WORK[T2] ^ KEY[T1]);
			P = Bits.rotateRight(P ^ A, 8 );
			K += P;
			F = Bits.rotateRight(F ^ K, 7 );
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			B += G + (WORK[T1] ^ KEY[T2]);
			M = Bits.rotateRight(M ^ B, 16);
			L += M;
			G = Bits.rotateRight(G ^ L, 12);
			B += G + (WORK[T2] ^ KEY[T1]);
			M = Bits.rotateRight(M ^ B, 8 );
			L += M;
			G = Bits.rotateRight(G ^ L, 7 );
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			C += H + (WORK[T1] ^ KEY[T2]);
			N = Bits.rotateRight(N ^ C, 16);
			I += N;
			H = Bits.rotateRight(H ^ I, 12);
			C += H + (WORK[T2] ^ KEY[T1]);
			N = Bits.rotateRight(N ^ C, 8 );
			I += N;
			H = Bits.rotateRight(H ^ I, 7 );
			T1 = PERMUTE[pos++];
			T2 = PERMUTE[pos++];
			D += E + (WORK[T1] ^ KEY[T2]);
			O = Bits.rotateRight(O ^ D, 16);
			J += O;
			E = Bits.rotateRight(E ^ J, 12);
			D += E + (WORK[T2] ^ KEY[T1]);
			O = Bits.rotateRight(O ^ D, 8 );
			J += O;
			E = Bits.rotateRight(E ^ J, 7 );
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
		Arrays.fill(remaining, pos, 64, (byte) 0);
		int c1 = counters[1],
			c2 = counters[0];
		remaining[pos] = (byte) 0x80;
		if(pos >= 55)
		{
			processNext(remaining, 0, false);
			Arrays.fill(remaining, (byte) 0);
		}
		if(this.outputLength() == 32)
			remaining[55] = 0x01;
		Bits.BigEndian.intToBytes(c1, remaining, 56);
		Bits.BigEndian.intToBytes(c2, remaining, 60);
		processNext(remaining, 0, false);
		this.output(out, start);
		reset();
	}
	
	public BLAKE_32State getState()
	{
		return new BLAKE_32State(this);
	}

	public void updateState(BLAKE_32State state)
	{
		state.update(this);
	}

	public void loadCustom(BLAKE_32State state)
	{
		System.arraycopy(state.counters, 0, this.counters, 0, 2);
		System.arraycopy(state.state, 0, this.STATE, 0, 8);
	}
	
	public static final BLAKE_32StateFactory sfactory = new BLAKE_32StateFactory();
	
	protected static final class BLAKE_32State extends MerkleState<BLAKE_32State, BLAKE_Base_32<? extends BLAKE_Base_32<?>>>
	{

		protected int[] state;
		protected int[] counters;
		
		public BLAKE_32State(BLAKE_Base_32<? extends BLAKE_Base_32<?>> hash) 
		{
			super(hash);
		}
		
		public BLAKE_32State(byte[] bytes, int pos)
		{
			super(bytes, pos);
		}

		public Factory<BLAKE_32State> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.BLAKE32STATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeInts(state);
			os.writeInts(counters);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.intsToBytes(state, 0, bytes, start, 8); start += 32;
			Bits.intsToBytes(counters, 0, bytes, start, 2);
		}

		protected void addCustom(IIncomingStream is) throws IOException
		{
			state = is.readInts(8);
			counters = is.readInts(2);
		}
		
		protected void addCustom(byte[] bytes, int start)
		{
			state = new int[8];
			counters = new int[2];
			Bits.bytesToInts(bytes, start, state, 0, 8); start += 32;
			Bits.bytesToInts(bytes, start, work, 0, 16); start += 64;
			Bits.bytesToInts(bytes, start, counters, 0, 2);
		}

		protected void addCustom(BLAKE_Base_32<? extends BLAKE_Base_32<?>> hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			counters = ArrayUtil.copy(hash.counters);
		}

		protected void updateCustom(BLAKE_Base_32<? extends BLAKE_Base_32<?>> hash)
		{
			System.arraycopy(this.state, 0, hash.STATE, 0, 8);
			System.arraycopy(this.counters, 0, hash.counters, 0, 2);
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, 0);
			Arrays.fill(counters, 0);
			state = null;
			counters = null;
		}

		protected boolean compareCustom(BLAKE_32State state)
		{
			return ArrayUtil.equals(counters, state.counters) && ArrayUtil.equals(this.state, state.state);
		}

		protected int customSize()
		{
			return 40;
		}
		
	}
	
	protected static final class BLAKE_32StateFactory extends MerkleStateFactory<BLAKE_32State, BLAKE_Base_32<? extends BLAKE_Base_32<?>>>
	{

		protected BLAKE_32StateFactory() 
		{
			super(BLAKE_32State.class, 64);
		}

		protected BLAKE_32State construct(byte[] bytes, int pos)
		{
			return new BLAKE_32State(bytes, pos);
		}
		
	}

}

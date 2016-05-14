package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.BLAKE_Base_64.BLAKE_64State;
import claire.util.io.Factory;
import claire.util.math.counters.LongCounter;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

abstract class BLAKE_Base_64<Hash extends BLAKE_Base_64<Hash>>  
	     extends BLAKECore<BLAKE_64State, Hash> {

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
	
	public void reset()
	{
		super.reset();
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
	
	public BLAKE_64State getState()
	{
		return new BLAKE_64State(this);
	}

	public void updateState(BLAKE_64State state)
	{
		state.update(this);
	}

	public void loadCustom(BLAKE_64State state)
	{
		System.arraycopy(state.counters, 0, this.counters, 0, 2);
		System.arraycopy(state.state, 0, this.STATE, 0, 8);
	}
	
	public static final BLAKE_64StateFactory sfactory = new BLAKE_64StateFactory();
	
	protected static final class BLAKE_64State extends MerkleState<BLAKE_64State, BLAKE_Base_64<? extends BLAKE_Base_64<?>>>
	{

		protected long[] state;
		protected long[] counters;
		
		public BLAKE_64State(BLAKE_Base_64<? extends BLAKE_Base_64<?>> hash) 
		{
			super(hash);
		}
		
		public BLAKE_64State(byte[] bytes, int pos)
		{
			super(bytes, pos);
		}

		public Factory<BLAKE_64State> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.BLAKE64STATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeLongs(state);
			os.writeLongs(counters);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.longsToBytes(state, 0, bytes, start, 8); start += 64;
			Bits.longsToBytes(counters, 0, bytes, start, 2);
		}

		protected void addCustom(IIncomingStream is) throws IOException
		{
			state = is.readLongs(8);
			counters = is.readLongs(2);
		}
		
		protected void addCustom(byte[] bytes, int start)
		{
			state = new long[8];
			counters = new long[2];
			Bits.bytesToLongs(bytes, start, state, 0, 8); start += 64;
			Bits.bytesToLongs(bytes, start, counters, 0, 2);
		}

		protected void addCustom(BLAKE_Base_64<? extends BLAKE_Base_64<?>> hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			counters = ArrayUtil.copy(hash.counters);
		}

		protected void updateCustom(BLAKE_Base_64<? extends BLAKE_Base_64<?>> hash)
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

		protected boolean compareCustom(BLAKE_64State state)
		{	
			return ArrayUtil.equals(counters, state.counters) && ArrayUtil.equals(this.state, state.state);
		}

		protected int customSize()
		{
			return 80;
		}

		public int stateID()
		{
			return IState.BLAKE_64;
		}
		
	}
	
	protected static final class BLAKE_64StateFactory extends MerkleStateFactory<BLAKE_64State, BLAKE_Base_64<? extends BLAKE_Base_64<?>>>
	{

		protected BLAKE_64StateFactory() 
		{
			super(BLAKE_64State.class, 128);
		}

		protected BLAKE_64State construct(byte[] bytes, int pos)
		{
			return new BLAKE_64State(bytes, pos);
		}
		
	}

}

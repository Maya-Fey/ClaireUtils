package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.WhirlpoolBase.WhirlpoolState;
import claire.util.io.Factory;
import claire.util.math.counters.LongCounter;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class WhirlpoolBase<Hash extends WhirlpoolBase<Hash>> 
	   extends MerkleHash<WhirlpoolState, Hash> {
	
	private final long[][] SCUBE;
	private final long[] RC;
	private final long[] IN = new long[8];
	
	protected final long[] STATE = new long[8];
	protected final long[] counters = new long[4];
	LongCounter counter = new LongCounter(counters);

	protected WhirlpoolBase(long[][] SCUBE, long[] RC) {
		super(64, 64);
		this.SCUBE = SCUBE;
		this.RC = RC;
	}
	
	public void reset()
	{
		super.reset();
		Arrays.fill(STATE, 0L);
		counter.reset();
	}

	public void processNext(byte[] bytes, int offset)
	{
		processNext(bytes, offset, true);
	}
	
	public void processNext(byte[] bytes, int offset, boolean add)
	{
		if(add)
			counter.add(512);
		Bits.bytesToLongs(bytes, offset, IN, 0, 8);
		long A1 = IN[0],
			 B1 = IN[1],
			 C1 = IN[2],
			 D1 = IN[3],
			 E1 = IN[4],
			 F1 = IN[5],
			 G1 = IN[6],
			 H1 = IN[7];
		
		long A2 = STATE[0], 
			 h1 = STATE[1], 
			 h2 = STATE[2], 
			 h3 = STATE[3],
			 h4 = STATE[4], 
			 h5 = STATE[5], 
			 h6 = STATE[6], 
			 h7 = STATE[7];
		int r;

		A1 ^= A2;
		B1 ^= h1;
		C1 ^= h2;
		D1 ^= h3;
		E1 ^= h4;
		F1 ^= h5;
		G1 ^= h6;
		H1 ^= h7;
		for (r = 0; r < 10; r ++) {
			long A3, B3, C3, D3, E3, F3, G3, H3;

			A3 = SCUBE[0][(int)A2 & 0xFF]
				^ SCUBE[1][(int)(h7 >> 8) & 0xFF]
				^ SCUBE[2][(int)(h6 >> 16) & 0xFF]
				^ SCUBE[3][(int)(h5 >> 24) & 0xFF]
				^ SCUBE[4][(int)(h4 >> 32) & 0xFF]
				^ SCUBE[5][(int)(h3 >> 40) & 0xFF]
				^ SCUBE[6][(int)(h2 >> 48) & 0xFF]
				^ SCUBE[7][(int)(h1 >> 56) & 0xFF]
				^ RC[r];
			B3 = SCUBE[0][(int)h1 & 0xFF]
				^ SCUBE[1][(int)(A2 >> 8) & 0xFF]
				^ SCUBE[2][(int)(h7 >> 16) & 0xFF]
				^ SCUBE[3][(int)(h6 >> 24) & 0xFF]
				^ SCUBE[4][(int)(h5 >> 32) & 0xFF]
				^ SCUBE[5][(int)(h4 >> 40) & 0xFF]
				^ SCUBE[6][(int)(h3 >> 48) & 0xFF]
				^ SCUBE[7][(int)(h2 >> 56) & 0xFF];
			C3 = SCUBE[0][(int)h2 & 0xFF]
				^ SCUBE[1][(int)(h1 >> 8) & 0xFF]
				^ SCUBE[2][(int)(A2 >> 16) & 0xFF]
				^ SCUBE[3][(int)(h7 >> 24) & 0xFF]
				^ SCUBE[4][(int)(h6 >> 32) & 0xFF]
				^ SCUBE[5][(int)(h5 >> 40) & 0xFF]
				^ SCUBE[6][(int)(h4 >> 48) & 0xFF]
				^ SCUBE[7][(int)(h3 >> 56) & 0xFF];
			D3 = SCUBE[0][(int)h3 & 0xFF]
		        ^ SCUBE[1][(int)(h2 >> 8) & 0xFF]
				^ SCUBE[2][(int)(h1 >> 16) & 0xFF]
				^ SCUBE[3][(int)(A2 >> 24) & 0xFF]
				^ SCUBE[4][(int)(h7 >> 32) & 0xFF]
				^ SCUBE[5][(int)(h6 >> 40) & 0xFF]
				^ SCUBE[6][(int)(h5 >> 48) & 0xFF]
				^ SCUBE[7][(int)(h4 >> 56) & 0xFF];
			E3 = SCUBE[0][(int)h4 & 0xFF]
				^ SCUBE[1][(int)(h3 >> 8) & 0xFF]
				^ SCUBE[2][(int)(h2 >> 16) & 0xFF]
				^ SCUBE[3][(int)(h1 >> 24) & 0xFF]
				^ SCUBE[4][(int)(A2 >> 32) & 0xFF]
				^ SCUBE[5][(int)(h7 >> 40) & 0xFF]
				^ SCUBE[6][(int)(h6 >> 48) & 0xFF]
				^ SCUBE[7][(int)(h5 >> 56) & 0xFF];
			F3 = SCUBE[0][(int)h5 & 0xFF]
				^ SCUBE[1][(int)(h4 >> 8) & 0xFF]
				^ SCUBE[2][(int)(h3 >> 16) & 0xFF]
				^ SCUBE[3][(int)(h2 >> 24) & 0xFF]
				^ SCUBE[4][(int)(h1 >> 32) & 0xFF]
				^ SCUBE[5][(int)(A2 >> 40) & 0xFF]
				^ SCUBE[6][(int)(h7 >> 48) & 0xFF]
				^ SCUBE[7][(int)(h6 >> 56) & 0xFF];
			G3 = SCUBE[0][(int)h6 & 0xFF]
				^ SCUBE[1][(int)(h5 >> 8) & 0xFF]
				^ SCUBE[2][(int)(h4 >> 16) & 0xFF]
				^ SCUBE[3][(int)(h3 >> 24) & 0xFF]
				^ SCUBE[4][(int)(h2 >> 32) & 0xFF]
				^ SCUBE[5][(int)(h1 >> 40) & 0xFF]
				^ SCUBE[6][(int)(A2 >> 48) & 0xFF]
				^ SCUBE[7][(int)(h7 >> 56) & 0xFF];
			H3 = SCUBE[0][(int)h7 & 0xFF]
				^ SCUBE[1][(int)(h6 >> 8) & 0xFF]
				^ SCUBE[2][(int)(h5 >> 16) & 0xFF]
				^ SCUBE[3][(int)(h4 >> 24) & 0xFF]
				^ SCUBE[4][(int)(h3 >> 32) & 0xFF]
				^ SCUBE[5][(int)(h2 >> 40) & 0xFF]
				^ SCUBE[6][(int)(h1 >> 48) & 0xFF]
				^ SCUBE[7][(int)(A2 >> 56) & 0xFF];
			A2 = A3;
			h1 = B3;
			h2 = C3;
			h3 = D3;
			h4 = E3;
			h5 = F3;
			h6 = G3;
			h7 = H3;
			A3 = SCUBE[0][(int)A1 & 0xFF]
				^ SCUBE[1][(int)(H1 >> 8) & 0xFF]
				^ SCUBE[2][(int)(G1 >> 16) & 0xFF]
				^ SCUBE[3][(int)(F1 >> 24) & 0xFF]
				^ SCUBE[4][(int)(E1 >> 32) & 0xFF]
				^ SCUBE[5][(int)(D1 >> 40) & 0xFF]
				^ SCUBE[6][(int)(C1 >> 48) & 0xFF]
				^ SCUBE[7][(int)(B1 >> 56) & 0xFF]
				^ A2;
			B3 = SCUBE[0][(int)B1 & 0xFF]
				^ SCUBE[1][(int)(A1 >> 8) & 0xFF]
				^ SCUBE[2][(int)(H1 >> 16) & 0xFF]
				^ SCUBE[3][(int)(G1 >> 24) & 0xFF]
				^ SCUBE[4][(int)(F1 >> 32) & 0xFF]
				^ SCUBE[5][(int)(E1 >> 40) & 0xFF]
				^ SCUBE[6][(int)(D1 >> 48) & 0xFF]
				^ SCUBE[7][(int)(C1 >> 56) & 0xFF]
				^ h1;
			C3 = SCUBE[0][(int)C1 & 0xFF]
				^ SCUBE[1][(int)(B1 >> 8) & 0xFF]
				^ SCUBE[2][(int)(A1 >> 16) & 0xFF]
				^ SCUBE[3][(int)(H1 >> 24) & 0xFF]
				^ SCUBE[4][(int)(G1 >> 32) & 0xFF]
				^ SCUBE[5][(int)(F1 >> 40) & 0xFF]
				^ SCUBE[6][(int)(E1 >> 48) & 0xFF]
				^ SCUBE[7][(int)(D1 >> 56) & 0xFF]
				^ h2;
			D3 = SCUBE[0][(int)D1 & 0xFF]
				^ SCUBE[1][(int)(C1 >> 8) & 0xFF]
				^ SCUBE[2][(int)(B1 >> 16) & 0xFF]
				^ SCUBE[3][(int)(A1 >> 24) & 0xFF]
				^ SCUBE[4][(int)(H1 >> 32) & 0xFF]
				^ SCUBE[5][(int)(G1 >> 40) & 0xFF]
				^ SCUBE[6][(int)(F1 >> 48) & 0xFF]
				^ SCUBE[7][(int)(E1 >> 56) & 0xFF]
				^ h3;
			E3 = SCUBE[0][(int)E1 & 0xFF]
				^ SCUBE[1][(int)(D1 >> 8) & 0xFF]
				^ SCUBE[2][(int)(C1 >> 16) & 0xFF]
				^ SCUBE[3][(int)(B1 >> 24) & 0xFF]
				^ SCUBE[4][(int)(A1 >> 32) & 0xFF]
				^ SCUBE[5][(int)(H1 >> 40) & 0xFF]
				^ SCUBE[6][(int)(G1 >> 48) & 0xFF]
				^ SCUBE[7][(int)(F1 >> 56) & 0xFF]
				^ h4;
			F3 = SCUBE[0][(int)F1 & 0xFF]
				^ SCUBE[1][(int)(E1 >> 8) & 0xFF]
				^ SCUBE[2][(int)(D1 >> 16) & 0xFF]
				^ SCUBE[3][(int)(C1 >> 24) & 0xFF]
				^ SCUBE[4][(int)(B1 >> 32) & 0xFF]
				^ SCUBE[5][(int)(A1 >> 40) & 0xFF]
				^ SCUBE[6][(int)(H1 >> 48) & 0xFF]
				^ SCUBE[7][(int)(G1 >> 56) & 0xFF]
				^ h5;
			G3 = SCUBE[0][(int)G1 & 0xFF]
				^ SCUBE[1][(int)(F1 >> 8) & 0xFF]
				^ SCUBE[2][(int)(E1 >> 16) & 0xFF]
				^ SCUBE[3][(int)(D1 >> 24) & 0xFF]
				^ SCUBE[4][(int)(C1 >> 32) & 0xFF]
				^ SCUBE[5][(int)(B1 >> 40) & 0xFF]
				^ SCUBE[6][(int)(A1 >> 48) & 0xFF]
				^ SCUBE[7][(int)(H1 >> 56) & 0xFF]
				^ h6;
			H3 = SCUBE[0][(int)H1 & 0xFF]
				^ SCUBE[1][(int)(G1 >> 8) & 0xFF]
				^ SCUBE[2][(int)(F1 >> 16) & 0xFF]
				^ SCUBE[3][(int)(E1 >> 24) & 0xFF]
				^ SCUBE[4][(int)(D1 >> 32) & 0xFF]
				^ SCUBE[5][(int)(C1 >> 40) & 0xFF]
				^ SCUBE[6][(int)(B1 >> 48) & 0xFF]
				^ SCUBE[7][(int)(A1 >> 56) & 0xFF]
				^ h7;
			A1 = A3;
			B1 = B3;
			C1 = C3;
			D1 = D3;
			E1 = E3;
			F1 = F3;
			G1 = G3;
			H1 = H3;
		}
		
		STATE[0] ^= A1 ^ IN[0];
		STATE[1] ^= B1 ^ IN[1];
		STATE[2] ^= C1 ^ IN[2];
		STATE[3] ^= D1 ^ IN[3];
		STATE[4] ^= E1 ^ IN[4];
		STATE[5] ^= F1 ^ IN[5];
		STATE[6] ^= G1 ^ IN[6];
		STATE[7] ^= H1 ^ IN[7];
	}

	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		byte[] bytes = new byte[64];
		System.arraycopy(remaining, 0, bytes, 0, pos);
		bytes[pos] = (byte) 0x80;
		counter.add(pos << 3);
		if(pos >= 32) {
			processNext(bytes, 0, false);
			Arrays.fill(bytes, (byte) 0);
		}
		Bits.BigEndian.longToBytes(counters[3], bytes, 32);
		Bits.BigEndian.longToBytes(counters[2], bytes, 40);
		Bits.BigEndian.longToBytes(counters[1], bytes, 48);
		Bits.BigEndian.longToBytes(counters[0], bytes, 56);
		processNext(bytes, 0, false);
		Bits.longsToBytes(STATE, 0, out, start);
		this.reset();
	}
	
	public WhirlpoolState getState()
	{
		return new WhirlpoolState(this);
	}

	public void updateState(WhirlpoolState state)
	{
		state.update(this);
	}

	public void loadCustom(WhirlpoolState state)
	{
		System.arraycopy(state.state, 0, this.STATE, 0, 8);
		System.arraycopy(state.counters, 0, this.counters, 0, 4);
	}
	
	public static final WhirlpoolStateFactory sfactory = new WhirlpoolStateFactory();
	
	protected static final class WhirlpoolState extends MerkleState<WhirlpoolState, WhirlpoolBase<? extends WhirlpoolBase<?>>>
	{
		protected long[] state;
		protected long[] counters;
		
		public WhirlpoolState(byte[] bytes, int pos) 
		{
			super(bytes, pos);
		}
		
		public WhirlpoolState(WhirlpoolBase<?> wp) 
		{
			super(wp);
		}

		public Factory<WhirlpoolState> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.WHIRLPOOLSTATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeLongs(state);
			os.writeLongs(counters);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.longsToBytes(state, 0, bytes, start, 8); start += 64;
			Bits.longsToBytes(counters, 0, bytes, start, 4);
		}

		protected void addCustom(IIncomingStream os) throws IOException
		{
			this.state = os.readLongs(8);
			this.counters = os.readLongs(4);
		}

		protected void addCustom(byte[] bytes, int start)
		{
			state = new long[8];
			counters = new long[4];
			Bits.bytesToLongs(bytes, start, state, 0, 8); start += 64;
			Bits.bytesToLongs(bytes, start, counters, 0, 4);
		}

		protected void addCustom(WhirlpoolBase<? extends WhirlpoolBase<?>> hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			counters = ArrayUtil.copy(hash.counters);
		}

		protected void updateCustom(WhirlpoolBase<? extends WhirlpoolBase<?>> hash)
		{
			if(state == null)
				state = ArrayUtil.copy(hash.STATE);
			else
				System.arraycopy(hash.STATE, 0, state, 0, 8);
			if(counters == null)
				counters = ArrayUtil.copy(hash.counters);
			else
				System.arraycopy(hash.counters, 0, counters, 0, 4);
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, 0);
			Arrays.fill(counters, 0);
			state = counters = null;
		}

		protected boolean compareCustom(WhirlpoolState state)
		{
			return ArrayUtil.equals(state.state, this.state) && ArrayUtil.equals(state.counters, counters);
		}

		protected int customSize()
		{
			return 96;
		}
		
	}
	
	protected static final class WhirlpoolStateFactory extends MerkleStateFactory<WhirlpoolState, WhirlpoolBase<? extends WhirlpoolBase<?>>>
	{

		protected WhirlpoolStateFactory() 
		{
			super(WhirlpoolState.class, 64);
		}
		
		public WhirlpoolState construct(byte[] bytes, int start)
		{
			return new WhirlpoolState(bytes, start);
		}
		
	}
	
}

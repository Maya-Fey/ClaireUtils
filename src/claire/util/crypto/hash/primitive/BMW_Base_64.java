package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.BMW_Base_64.BMW_64State;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

abstract class BMW_Base_64<Hash extends BMW_Base_64<Hash>> 
	  	 extends MerkleHash<BMW_64State, Hash> {
	
	protected final long[] STATE = new long[16];
	
	private final long[] BLOCK = new long[16];
	private final long[] WORK2 = new long[16];
	private final long[] WORK = new long[32];
	
	private long counter;
	
	private static final long[] last = 
	{
		0xaaaaaaaaaaaaaaa0L, 0xaaaaaaaaaaaaaaa1L,
		0xaaaaaaaaaaaaaaa2L, 0xaaaaaaaaaaaaaaa3L,
		0xaaaaaaaaaaaaaaa4L, 0xaaaaaaaaaaaaaaa5L,
		0xaaaaaaaaaaaaaaa6L, 0xaaaaaaaaaaaaaaa7L,
		0xaaaaaaaaaaaaaaa8L, 0xaaaaaaaaaaaaaaa9L,
		0xaaaaaaaaaaaaaaaaL, 0xaaaaaaaaaaaaaaabL,
		0xaaaaaaaaaaaaaaacL, 0xaaaaaaaaaaaaaaadL,
		0xaaaaaaaaaaaaaaaeL, 0xaaaaaaaaaaaaaaafL
	};
	
	private static final long[] KEY = {
		16L * 0x0555555555555555L, 17L * 0x0555555555555555L,
		18L * 0x0555555555555555L, 19L * 0x0555555555555555L,
		20L * 0x0555555555555555L, 21L * 0x0555555555555555L,
		22L * 0x0555555555555555L, 23L * 0x0555555555555555L,
		24L * 0x0555555555555555L, 25L * 0x0555555555555555L,
		26L * 0x0555555555555555L, 27L * 0x0555555555555555L,
		28L * 0x0555555555555555L, 29L * 0x0555555555555555L,
		30L * 0x0555555555555555L, 31L * 0x0555555555555555L
	};

	public BMW_Base_64(int out) 
	{
		super(128, out);
		reset();
	}

	protected abstract long[] getIV();
	protected abstract void output(byte[] out, int start);
	
	private void reset()
	{
		System.arraycopy(this.getIV(), 0, STATE, 0, 16);
		counter = 0;
	}
	
	private void compress(long[] IN)
	{
		WORK2[0] = (IN[5] ^ STATE[5]) - (IN[7] ^ STATE[7]) + (IN[10] ^ STATE[10])
			+ (IN[13] ^ STATE[13]) + (IN[14] ^ STATE[14]);
		WORK2[1] = (IN[6] ^ STATE[6]) - (IN[8] ^ STATE[8]) + (IN[11] ^ STATE[11])
			+ (IN[14] ^ STATE[14]) - (IN[15] ^ STATE[15]);
		WORK2[2] = (IN[0] ^ STATE[0]) + (IN[7] ^ STATE[7]) + (IN[9] ^ STATE[9])
			- (IN[12] ^ STATE[12]) + (IN[15] ^ STATE[15]);
		WORK2[3] = (IN[0] ^ STATE[0]) - (IN[1] ^ STATE[1]) + (IN[8] ^ STATE[8])
			- (IN[10] ^ STATE[10]) + (IN[13] ^ STATE[13]);
		WORK2[4] = (IN[1] ^ STATE[1]) + (IN[2] ^ STATE[2]) + (IN[9] ^ STATE[9])
			- (IN[11] ^ STATE[11]) - (IN[14] ^ STATE[14]);
		WORK2[5] = (IN[3] ^ STATE[3]) - (IN[2] ^ STATE[2]) + (IN[10] ^ STATE[10])
			- (IN[12] ^ STATE[12]) + (IN[15] ^ STATE[15]);
		WORK2[6] = (IN[4] ^ STATE[4]) - (IN[0] ^ STATE[0]) - (IN[3] ^ STATE[3])
			- (IN[11] ^ STATE[11]) + (IN[13] ^ STATE[13]);
		WORK2[7] = (IN[1] ^ STATE[1]) - (IN[4] ^ STATE[4]) - (IN[5] ^ STATE[5])
			- (IN[12] ^ STATE[12]) - (IN[14] ^ STATE[14]);
		WORK2[8] = (IN[2] ^ STATE[2]) - (IN[5] ^ STATE[5]) - (IN[6] ^ STATE[6])
			+ (IN[13] ^ STATE[13]) - (IN[15] ^ STATE[15]);
		WORK2[9] = (IN[0] ^ STATE[0]) - (IN[3] ^ STATE[3]) + (IN[6] ^ STATE[6])
			- (IN[7] ^ STATE[7]) + (IN[14] ^ STATE[14]);
		WORK2[10] = (IN[8] ^ STATE[8]) - (IN[1] ^ STATE[1]) - (IN[4] ^ STATE[4])
			- (IN[7] ^ STATE[7]) + (IN[15] ^ STATE[15]);
		WORK2[11] = (IN[8] ^ STATE[8]) - (IN[0] ^ STATE[0]) - (IN[2] ^ STATE[2])
			- (IN[5] ^ STATE[5]) + (IN[9] ^ STATE[9]);
		WORK2[12] = (IN[1] ^ STATE[1]) + (IN[3] ^ STATE[3]) - (IN[6] ^ STATE[6])
			- (IN[9] ^ STATE[9]) + (IN[10] ^ STATE[10]);
		WORK2[13] = (IN[2] ^ STATE[2]) + (IN[4] ^ STATE[4]) + (IN[7] ^ STATE[7])
			+ (IN[10] ^ STATE[10]) + (IN[11] ^ STATE[11]);
		WORK2[14] = (IN[3] ^ STATE[3]) - (IN[5] ^ STATE[5]) + (IN[8] ^ STATE[8])
			- (IN[11] ^ STATE[11]) - (IN[12] ^ STATE[12]);
		WORK2[15] = (IN[12] ^ STATE[12]) - (IN[4] ^ STATE[4]) - (IN[6] ^ STATE[6])
			- (IN[9] ^ STATE[9]) + (IN[13] ^ STATE[13]);
		for (int i = 0; i < 15; i += 5) {
			WORK[i + 0] = ((WORK2[i + 0] >>> 1) ^ (WORK2[i + 0] << 3)
				^ Bits.rotateLeft(WORK2[i + 0], 4)
				^ Bits.rotateLeft(WORK2[i + 0], 37)) + STATE[i + 1];
			WORK[i + 1] = ((WORK2[i + 1] >>> 1) ^ (WORK2[i + 1] << 2)
				^ Bits.rotateLeft(WORK2[i + 1], 13)
				^ Bits.rotateLeft(WORK2[i + 1], 43)) + STATE[i + 2];
			WORK[i + 2] = ((WORK2[i + 2] >>> 2) ^ (WORK2[i + 2] << 1)
				^ Bits.rotateLeft(WORK2[i + 2], 19)
				^ Bits.rotateLeft(WORK2[i + 2], 53)) + STATE[i + 3];
			WORK[i + 3] = ((WORK2[i + 3] >>> 2) ^ (WORK2[i + 3] << 2)
				^ Bits.rotateLeft(WORK2[i + 3], 28)
				^ Bits.rotateLeft(WORK2[i + 3], 59)) + STATE[i + 4];
			WORK[i + 4] = ((WORK2[i + 4] >>> 1) ^ WORK2[i + 4]) + STATE[i + 5];
		}
		WORK[15] = ((WORK2[15] >>> 1) ^ (WORK2[15] << 3)
			^ Bits.rotateLeft(WORK2[15], 4) ^ Bits.rotateLeft(WORK2[15], 37))
			+ STATE[0];

		for (int i = 16; i < 18; i++) {
			WORK[i] = ((WORK[i - 16] >>> 1) ^ (WORK[i - 16] << 2)
				^ Bits.rotateLeft(WORK[i - 16], 13)
				^ Bits.rotateLeft(WORK[i - 16], 43))
				+ ((WORK[i - 15] >>> 2) ^ (WORK[i - 15] << 1)
				^ Bits.rotateLeft(WORK[i - 15], 19)
				^ Bits.rotateLeft(WORK[i - 15], 53))
				+ ((WORK[i - 14] >>> 2) ^ (WORK[i - 14] << 2)
				^ Bits.rotateLeft(WORK[i - 14], 28)
				^ Bits.rotateLeft(WORK[i - 14], 59))
				+ ((WORK[i - 13] >>> 1) ^ (WORK[i - 13] << 3)
				^ Bits.rotateLeft(WORK[i - 13], 4)
				^ Bits.rotateLeft(WORK[i - 13], 37))
				+ ((WORK[i - 12] >>> 1) ^ (WORK[i - 12] << 2)
				^ Bits.rotateLeft(WORK[i - 12], 13)
				^ Bits.rotateLeft(WORK[i - 12], 43))
				+ ((WORK[i - 11] >>> 2) ^ (WORK[i - 11] << 1)
				^ Bits.rotateLeft(WORK[i - 11], 19)
				^ Bits.rotateLeft(WORK[i - 11], 53))
				+ ((WORK[i - 10] >>> 2) ^ (WORK[i - 10] << 2)
				^ Bits.rotateLeft(WORK[i - 10], 28)
				^ Bits.rotateLeft(WORK[i - 10], 59))
				+ ((WORK[i - 9] >>> 1) ^ (WORK[i - 9] << 3)
				^ Bits.rotateLeft(WORK[i - 9], 4)
				^ Bits.rotateLeft(WORK[i - 9], 37))
				+ ((WORK[i - 8] >>> 1) ^ (WORK[i - 8] << 2)
				^ Bits.rotateLeft(WORK[i - 8], 13)
				^ Bits.rotateLeft(WORK[i - 8], 43))
				+ ((WORK[i - 7] >>> 2) ^ (WORK[i - 7] << 1)
				^ Bits.rotateLeft(WORK[i - 7], 19)
				^ Bits.rotateLeft(WORK[i - 7], 53))
				+ ((WORK[i - 6] >>> 2) ^ (WORK[i - 6] << 2)
				^ Bits.rotateLeft(WORK[i - 6], 28)
				^ Bits.rotateLeft(WORK[i - 6], 59))
				+ ((WORK[i - 5] >>> 1) ^ (WORK[i - 5] << 3)
				^ Bits.rotateLeft(WORK[i - 5], 4)
				^ Bits.rotateLeft(WORK[i - 5], 37))
				+ ((WORK[i - 4] >>> 1) ^ (WORK[i - 4] << 2)
				^ Bits.rotateLeft(WORK[i - 4], 13)
				^ Bits.rotateLeft(WORK[i - 4], 43))
				+ ((WORK[i - 3] >>> 2) ^ (WORK[i - 3] << 1)
				^ Bits.rotateLeft(WORK[i - 3], 19)
				^ Bits.rotateLeft(WORK[i - 3], 53))
				+ ((WORK[i - 2] >>> 2) ^ (WORK[i - 2] << 2)
				^ Bits.rotateLeft(WORK[i - 2], 28)
				^ Bits.rotateLeft(WORK[i - 2], 59))
				+ ((WORK[i - 1] >>> 1) ^ (WORK[i - 1] << 3)
				^ Bits.rotateLeft(WORK[i - 1], 4)
				^ Bits.rotateLeft(WORK[i - 1], 37))
				+ ((Bits.rotateLeft(IN[(i - 16 + 0) & 15],
					((i - 16 + 0) & 15) + 1)
				+ Bits.rotateLeft(IN[(i - 16 + 3) & 15],
					((i - 16 + 3) & 15) + 1)
				- Bits.rotateLeft(IN[(i - 16 + 10) & 15],
					((i - 16 + 10) & 15) + 1)
				+ KEY[i - 16]) ^ STATE[(i - 16 + 7) & 15]);
		}
		for (int i = 18; i < 32; i++) {
			WORK[i] = WORK[i - 16] + Bits.rotateLeft(WORK[i - 15], 5)
				+ WORK[i - 14] + Bits.rotateLeft(WORK[i - 13], 11)
				+ WORK[i - 12] + Bits.rotateLeft(WORK[i - 11], 27)
				+ WORK[i - 10] + Bits.rotateLeft(WORK[i - 9], 32)
				+ WORK[i - 8] + Bits.rotateLeft(WORK[i - 7], 37)
				+ WORK[i - 6] + Bits.rotateLeft(WORK[i - 5], 43)
				+ WORK[i - 4] + Bits.rotateLeft(WORK[i - 3], 53)
				+ ((WORK[i - 2] >>> 1) ^ WORK[i - 2])
				+ ((WORK[i - 1] >>> 2) ^ WORK[i - 1])
				+ ((Bits.rotateLeft(IN[(i - 16 + 0) & 15],
					((i - 16 + 0) & 15) + 1)
				+ Bits.rotateLeft(IN[(i - 16 + 3) & 15],
					((i - 16 + 3) & 15) + 1)
				- Bits.rotateLeft(IN[(i - 16 + 10) & 15],
					((i - 16 + 10) & 15) + 1)
				+ KEY[i - 16]) ^ STATE[(i - 16 + 7) & 15]);
		}

		long xl = WORK[16] ^ WORK[17] ^ WORK[18] ^ WORK[19]
			^ WORK[20] ^ WORK[21] ^ WORK[22] ^ WORK[23];
		long xh = xl ^ WORK[24] ^ WORK[25] ^ WORK[26] ^ WORK[27]
			^ WORK[28] ^ WORK[29] ^ WORK[30] ^ WORK[31];
		STATE[0] = ((xh << 5) ^ (WORK[16] >>> 5) ^ IN[0]) + (xl ^ WORK[24] ^ WORK[0]);
		STATE[1] = ((xh >>> 7) ^ (WORK[17] << 8) ^ IN[1]) + (xl ^ WORK[25] ^ WORK[1]);
		STATE[2] = ((xh >>> 5) ^ (WORK[18] << 5) ^ IN[2]) + (xl ^ WORK[26] ^ WORK[2]);
		STATE[3] = ((xh >>> 1) ^ (WORK[19] << 5) ^ IN[3]) + (xl ^ WORK[27] ^ WORK[3]);
		STATE[4] = ((xh >>> 3) ^ (WORK[20] << 0) ^ IN[4]) + (xl ^ WORK[28] ^ WORK[4]);
		STATE[5] = ((xh << 6) ^ (WORK[21] >>> 6) ^ IN[5]) + (xl ^ WORK[29] ^ WORK[5]);
		STATE[6] = ((xh >>> 4) ^ (WORK[22] << 6) ^ IN[6]) + (xl ^ WORK[30] ^ WORK[6]);
		STATE[7] = ((xh >>> 11) ^ (WORK[23] << 2) ^ IN[7])
			+ (xl ^ WORK[31] ^ WORK[7]);
		STATE[8] = Bits.rotateLeft(STATE[4], 9) + (xh ^ WORK[24] ^ IN[8])
			+ ((xl << 8) ^ WORK[23] ^ WORK[8]);
		STATE[9] = Bits.rotateLeft(STATE[5], 10) + (xh ^ WORK[25] ^ IN[9])
			+ ((xl >>> 6) ^ WORK[16] ^ WORK[9]);
		STATE[10] = Bits.rotateLeft(STATE[6], 11) + (xh ^ WORK[26] ^ IN[10])
			+ ((xl << 6) ^ WORK[17] ^ WORK[10]);
		STATE[11] = Bits.rotateLeft(STATE[7], 12) + (xh ^ WORK[27] ^ IN[11])
			+ ((xl << 4) ^ WORK[18] ^ WORK[11]);
		STATE[12] = Bits.rotateLeft(STATE[0], 13) + (xh ^ WORK[28] ^ IN[12])
			+ ((xl >>> 3) ^ WORK[19] ^ WORK[12]);
		STATE[13] = Bits.rotateLeft(STATE[1], 14) + (xh ^ WORK[29] ^ IN[13])
			+ ((xl >>> 4) ^ WORK[20] ^ WORK[13]);
		STATE[14] = Bits.rotateLeft(STATE[2], 15) + (xh ^ WORK[30] ^ IN[14])
			+ ((xl >>> 7) ^ WORK[21] ^ WORK[14]);
		STATE[15] = Bits.rotateLeft(STATE[3], 16) + (xh ^ WORK[31] ^ IN[15])
			+ ((xl >>> 2) ^ WORK[22] ^ WORK[15]);
	}
	
	public void processNext(byte[] bytes, int offset)
	{
		this.processNext(bytes, offset, true);
	}

	public void processNext(byte[] bytes, int offset, boolean count)
	{
		if(count)
			counter += 1024;
		Bits.bytesToLongs(bytes, offset, BLOCK, 0, 16);
		compress(BLOCK);
	}

	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		counter += pos << 3;
		Arrays.fill(remaining, pos, 128, (byte) 0);
		remaining[pos] = (byte) 0x80;
		if(pos >= 56)
		{
			processNext(remaining, 0);
			Arrays.fill(remaining, (byte) 0);
		}
		Bits.longToBytes(counter, remaining, 120);
		processNext(remaining, 0);
		System.arraycopy(STATE, 0, BLOCK, 0, 16);
		System.arraycopy(last, 0, STATE, 0, 16);
		compress(BLOCK);
		output(out, start);
		reset();
	}
	
	public BMW_64State getState()
	{
		return new BMW_64State(this);
	}

	public void updateState(BMW_64State state)
	{
		state.update(this);
	}

	public void loadCustom(BMW_64State state)
	{
		this.counter = state.counter;
		System.arraycopy(state.state, 0, this.STATE, 0, 8);
	}
	
	public static final BMW_64StateFactory sfactory = new BMW_64StateFactory();
	
	protected static final class BMW_64State extends MerkleState<BMW_64State, BMW_Base_64<? extends BMW_Base_64<?>>>
	{

		protected long[] state;
		protected long counter;
		
		public BMW_64State(BMW_Base_64<? extends BMW_Base_64<?>> hash) 
		{
			super(hash);
		}
		
		public BMW_64State(byte[] bytes, int pos)
		{
			super(bytes, pos);
		}

		public Factory<BMW_64State> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.BMW64STATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeLongs(state);
			os.writeLong(counter);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.longsToBytes(state, 0, bytes, start, 16); start += 128;
			Bits.longToBytes(counter, bytes, start);
		}

		protected void addCustom(IIncomingStream is) throws IOException
		{
			state = is.readLongs(16);
			counter = is.readLong();
		}
		
		protected void addCustom(byte[] bytes, int start)
		{
			state = new long[16];
			Bits.bytesToLongs(bytes, start, state, 0, 16); start += 128;
			counter = Bits.longFromBytes(bytes, start);
		}

		protected void addCustom(BMW_Base_64<? extends BMW_Base_64<?>> hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			counter = hash.counter;
		}

		protected void updateCustom(BMW_Base_64<? extends BMW_Base_64<?>> hash)
		{
			System.arraycopy(this.state, 0, hash.STATE, 0, 16);
			hash.counter = this.counter;
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, 0);
			state = null;
			counter = 0;
		}

		protected boolean compareCustom(BMW_64State state)
		{	
			return this.counter == state.counter && ArrayUtil.equals(this.state, state.state);
		}

		protected int customSize()
		{
			return 136;
		}
		
	}
	
	protected static final class BMW_64StateFactory extends MerkleStateFactory<BMW_64State, BMW_Base_64<? extends BMW_Base_64<?>>>
	{

		protected BMW_64StateFactory() 
		{
			super(BMW_64State.class, 128);
		}

		protected BMW_64State construct(byte[] bytes, int pos)
		{
			return new BMW_64State(bytes, pos);
		}
		
	}

}

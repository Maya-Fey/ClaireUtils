package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.hash.primitive.CubeHash.CubeHashState;
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

public final class CubeHash 
	  	     extends MerkleHash<CubeHashState, CubeHash> {
	
	private final int[] IV;
	
	private int[] SCRATCH;
	
	private final int round,
				      close;
	
	private final boolean sRound,
						  sClose;

	protected final int[] STATE = new int[32];
	
	protected final int block;

	public CubeHash(int init, int block, int out, int rounds, int close) 
	{
		super(block, out);
		this.IV = new int[32];
		if(((init | rounds | close) & 1) == 1)
			this.SCRATCH = new int[8];
		this.block = block;
		this.round = rounds >> 1;
		this.close = close >> 1;
		this.sRound = (rounds & 1) == 1;
		this.sClose = (close & 1) == 1;
		genIV(IV, SCRATCH, out, block, rounds, init);
		System.arraycopy(IV, 0, STATE, 0, 32);
	}
	
	public int hashID()
	{
		return Hash.CUBEHASH;
	}
	
	protected static final void genIV(int[] IV, int[] scratch, int out, int size, int rounds, final int init)
	{
		IV[0] = out;
		IV[1] = size;
		IV[2] = rounds;
		for(int i = 0; i < (init >> 1); i++) 
			doubleRound(IV);
		if((init & 1) == 1) 
			singleRound(IV, scratch);
	}
	
	protected static final void doubleRound(int[] STATE)
	{
		STATE[16] += STATE[0 ];	
		STATE[17] += STATE[1 ];
		STATE[18] += STATE[2 ];
		STATE[19] += STATE[3 ];
		STATE[20] += STATE[4 ];
		STATE[21] += STATE[5 ];
		STATE[22] += STATE[6 ];
		STATE[23] += STATE[7 ];
		STATE[24] += STATE[8 ];
		STATE[25] += STATE[9 ];
		STATE[26] += STATE[10];
		STATE[27] += STATE[11];
		STATE[28] += STATE[12];
		STATE[29] += STATE[13];
		STATE[30] += STATE[14];
		STATE[31] += STATE[15];

		STATE[0 ] = Bits.rotateLeft(STATE[0 ], 7);
		STATE[1 ] = Bits.rotateLeft(STATE[1 ], 7);
		STATE[2 ] = Bits.rotateLeft(STATE[2 ], 7);
		STATE[3 ] = Bits.rotateLeft(STATE[3 ], 7);
		STATE[4 ] = Bits.rotateLeft(STATE[4 ], 7);
		STATE[5 ] = Bits.rotateLeft(STATE[5 ], 7);
		STATE[6 ] = Bits.rotateLeft(STATE[6 ], 7);
		STATE[7 ] = Bits.rotateLeft(STATE[7 ], 7);
		STATE[8 ] = Bits.rotateLeft(STATE[8 ], 7);
		STATE[9 ] = Bits.rotateLeft(STATE[9 ], 7);
		STATE[10] = Bits.rotateLeft(STATE[10], 7);
		STATE[11] = Bits.rotateLeft(STATE[11], 7);
		STATE[12] = Bits.rotateLeft(STATE[12], 7);
		STATE[13] = Bits.rotateLeft(STATE[13], 7);
		STATE[14] = Bits.rotateLeft(STATE[14], 7);
		STATE[15] = Bits.rotateLeft(STATE[15], 7);
		
		STATE[8 ] ^= STATE[16];
		STATE[9 ] ^= STATE[17];
		STATE[10] ^= STATE[18];
		STATE[11] ^= STATE[19];
		STATE[12] ^= STATE[20];
		STATE[13] ^= STATE[21];
		STATE[14] ^= STATE[22];
		STATE[15] ^= STATE[23];
		STATE[0 ] ^= STATE[24];
		STATE[1 ] ^= STATE[25];
		STATE[2 ] ^= STATE[26];
		STATE[3 ] ^= STATE[27];
		STATE[4 ] ^= STATE[28];
		STATE[5 ] ^= STATE[29];
		STATE[6 ] ^= STATE[30];
		STATE[7 ] ^= STATE[31];
		
		STATE[18] += STATE[8 ];		
		STATE[19] += STATE[9 ];		
		STATE[16] += STATE[10];		
		STATE[17] += STATE[11];		
		STATE[22] += STATE[12];		
		STATE[23] += STATE[13];		
		STATE[20] += STATE[14];		
		STATE[21] += STATE[15];
		STATE[26] += STATE[0 ];
		STATE[27] += STATE[1 ];		
		STATE[24] += STATE[2 ];		
		STATE[25] += STATE[3 ];		
		STATE[30] += STATE[4 ];		
		STATE[31] += STATE[5 ];		
		STATE[28] += STATE[6 ];
		STATE[29] += STATE[7 ];
		
		STATE[0 ] = Bits.rotateLeft(STATE[0 ], 11);
		STATE[1 ] = Bits.rotateLeft(STATE[1 ], 11);
		STATE[2 ] = Bits.rotateLeft(STATE[2 ], 11);
		STATE[3 ] = Bits.rotateLeft(STATE[3 ], 11);
		STATE[4 ] = Bits.rotateLeft(STATE[4 ], 11);
		STATE[5 ] = Bits.rotateLeft(STATE[5 ], 11);
		STATE[6 ] = Bits.rotateLeft(STATE[6 ], 11);
		STATE[7 ] = Bits.rotateLeft(STATE[7 ], 11);
		STATE[8 ] = Bits.rotateLeft(STATE[8 ], 11);
		STATE[9 ] = Bits.rotateLeft(STATE[9 ], 11);
		STATE[10] = Bits.rotateLeft(STATE[10], 11);
		STATE[11] = Bits.rotateLeft(STATE[11], 11);
		STATE[12] = Bits.rotateLeft(STATE[12], 11);
		STATE[13] = Bits.rotateLeft(STATE[13], 11);
		STATE[14] = Bits.rotateLeft(STATE[14], 11);
		STATE[15] = Bits.rotateLeft(STATE[15], 11);
		
		STATE[12] ^= STATE[18];
		STATE[13] ^= STATE[19];
		STATE[14] ^= STATE[16];
		STATE[15] ^= STATE[17];
		STATE[8 ] ^= STATE[22];
		STATE[9 ] ^= STATE[23];
		STATE[10] ^= STATE[20];
		STATE[11] ^= STATE[21];
		STATE[4 ] ^= STATE[26];
		STATE[5 ] ^= STATE[27];
		STATE[6 ] ^= STATE[24];
		STATE[7 ] ^= STATE[25];
		STATE[0 ] ^= STATE[30];
		STATE[1 ] ^= STATE[31];
		STATE[2 ] ^= STATE[28];
		STATE[3 ] ^= STATE[29];
		
		STATE[19] += STATE[12];		
		STATE[18] += STATE[13];		
		STATE[17] += STATE[14];		
		STATE[16] += STATE[15];		
		STATE[23] += STATE[8 ];		
		STATE[22] += STATE[9 ];		
		STATE[21] += STATE[10];		
		STATE[20] += STATE[11];		
		STATE[27] += STATE[4 ];
		STATE[26] += STATE[5 ];		
		STATE[25] += STATE[6 ];		
		STATE[24] += STATE[7 ];		
		STATE[31] += STATE[0 ];	
		STATE[30] += STATE[1 ];		
		STATE[29] += STATE[2 ];		
		STATE[28] += STATE[3 ];
		
		STATE[0 ] = Bits.rotateLeft(STATE[0 ], 7);
		STATE[1 ] = Bits.rotateLeft(STATE[1 ], 7);
		STATE[2 ] = Bits.rotateLeft(STATE[2 ], 7);
		STATE[3 ] = Bits.rotateLeft(STATE[3 ], 7);
		STATE[4 ] = Bits.rotateLeft(STATE[4 ], 7);
		STATE[5 ] = Bits.rotateLeft(STATE[5 ], 7);
		STATE[6 ] = Bits.rotateLeft(STATE[6 ], 7);
		STATE[7 ] = Bits.rotateLeft(STATE[7 ], 7);
		STATE[8 ] = Bits.rotateLeft(STATE[8 ], 7);
		STATE[9 ] = Bits.rotateLeft(STATE[9 ], 7);
		STATE[10] = Bits.rotateLeft(STATE[10], 7);
		STATE[11] = Bits.rotateLeft(STATE[11], 7);
		STATE[12] = Bits.rotateLeft(STATE[12], 7);
		STATE[13] = Bits.rotateLeft(STATE[13], 7);
		STATE[14] = Bits.rotateLeft(STATE[14], 7);
		STATE[15] = Bits.rotateLeft(STATE[15], 7);
		
		STATE[4 ] ^= STATE[19];
		STATE[5 ] ^= STATE[18];
		STATE[6 ] ^= STATE[17];
		STATE[7 ] ^= STATE[16];
		STATE[0 ] ^= STATE[23];
		STATE[1 ] ^= STATE[22];
		STATE[2 ] ^= STATE[21];
		STATE[3 ] ^= STATE[20];
		STATE[12] ^= STATE[27];
		STATE[13] ^= STATE[26];
		STATE[14] ^= STATE[25];
		STATE[15] ^= STATE[24];
		STATE[8 ] ^= STATE[31];
		STATE[9 ] ^= STATE[30];
		STATE[10] ^= STATE[29];
		STATE[11] ^= STATE[28];
		STATE[17] += STATE[4 ];
		STATE[16] += STATE[5 ];		
		STATE[19] += STATE[6 ];		
		STATE[18] += STATE[7 ];	
		STATE[21] += STATE[0 ];	
		STATE[20] += STATE[1 ];		
		STATE[23] += STATE[2 ];		
		STATE[22] += STATE[3 ];		
		STATE[25] += STATE[12];		
		STATE[24] += STATE[13];		
		STATE[27] += STATE[14];		
		STATE[26] += STATE[15];		
		STATE[29] += STATE[8 ];		
		STATE[28] += STATE[9 ];		
		STATE[31] += STATE[10];		
		STATE[30] += STATE[11];
		
		STATE[0 ] = Bits.rotateLeft(STATE[0 ], 11);
		STATE[1 ] = Bits.rotateLeft(STATE[1 ], 11);
		STATE[2 ] = Bits.rotateLeft(STATE[2 ], 11);
		STATE[3 ] = Bits.rotateLeft(STATE[3 ], 11);
		STATE[4 ] = Bits.rotateLeft(STATE[4 ], 11);
		STATE[5 ] = Bits.rotateLeft(STATE[5 ], 11);
		STATE[6 ] = Bits.rotateLeft(STATE[6 ], 11);
		STATE[7 ] = Bits.rotateLeft(STATE[7 ], 11);
		STATE[8 ] = Bits.rotateLeft(STATE[8 ], 11);
		STATE[9 ] = Bits.rotateLeft(STATE[9 ], 11);
		STATE[10] = Bits.rotateLeft(STATE[10], 11);
		STATE[11] = Bits.rotateLeft(STATE[11], 11);
		STATE[12] = Bits.rotateLeft(STATE[12], 11);
		STATE[13] = Bits.rotateLeft(STATE[13], 11);
		STATE[14] = Bits.rotateLeft(STATE[14], 11);
		STATE[15] = Bits.rotateLeft(STATE[15], 11);
		
		STATE[0 ] ^= STATE[17];
		STATE[1 ] ^= STATE[16];
		STATE[2 ] ^= STATE[19];
		STATE[3 ] ^= STATE[18];
		STATE[4 ] ^= STATE[21];
		STATE[5 ] ^= STATE[20];
		STATE[6 ] ^= STATE[23];
		STATE[7 ] ^= STATE[22];
		STATE[8 ] ^= STATE[25];
		STATE[9 ] ^= STATE[24];
		STATE[10] ^= STATE[27];
		STATE[11] ^= STATE[26];
		STATE[12] ^= STATE[29];
		STATE[13] ^= STATE[28];
		STATE[14] ^= STATE[31];
		STATE[15] ^= STATE[30];
	}
	
	protected static final void singleRound(int[] STATE, int[] SCRATCH)
	{
		STATE[16] += STATE[0 ];	
		STATE[17] += STATE[1 ];
		STATE[18] += STATE[2 ];
		STATE[19] += STATE[3 ];
		STATE[20] += STATE[4 ];
		STATE[21] += STATE[5 ];
		STATE[22] += STATE[6 ];
		STATE[23] += STATE[7 ];
		STATE[24] += STATE[8 ];
		STATE[25] += STATE[9 ];
		STATE[26] += STATE[10];
		STATE[27] += STATE[11];
		STATE[28] += STATE[12];
		STATE[29] += STATE[13];
		STATE[30] += STATE[14];
		STATE[31] += STATE[15];

		STATE[0 ] = Bits.rotateLeft(STATE[0 ], 7);
		STATE[1 ] = Bits.rotateLeft(STATE[1 ], 7);
		STATE[2 ] = Bits.rotateLeft(STATE[2 ], 7);
		STATE[3 ] = Bits.rotateLeft(STATE[3 ], 7);
		STATE[4 ] = Bits.rotateLeft(STATE[4 ], 7);
		STATE[5 ] = Bits.rotateLeft(STATE[5 ], 7);
		STATE[6 ] = Bits.rotateLeft(STATE[6 ], 7);
		STATE[7 ] = Bits.rotateLeft(STATE[7 ], 7);
		STATE[8 ] = Bits.rotateLeft(STATE[8 ], 7);
		STATE[9 ] = Bits.rotateLeft(STATE[9 ], 7);
		STATE[10] = Bits.rotateLeft(STATE[10], 7);
		STATE[11] = Bits.rotateLeft(STATE[11], 7);
		STATE[12] = Bits.rotateLeft(STATE[12], 7);
		STATE[13] = Bits.rotateLeft(STATE[13], 7);
		STATE[14] = Bits.rotateLeft(STATE[14], 7);
		STATE[15] = Bits.rotateLeft(STATE[15], 7);
		
		System.arraycopy(STATE, 0, SCRATCH, 0, 8);
		System.arraycopy(STATE, 8, STATE, 0, 8);
		System.arraycopy(SCRATCH, 0, STATE, 8, 8);
		
		STATE[0 ] ^= STATE[16];
		STATE[1 ] ^= STATE[17];
		STATE[2 ] ^= STATE[18];
		STATE[3 ] ^= STATE[19];
		STATE[4 ] ^= STATE[20];
		STATE[5 ] ^= STATE[21];
		STATE[6 ] ^= STATE[22];
		STATE[7 ] ^= STATE[23];
		STATE[8 ] ^= STATE[24];
		STATE[9 ] ^= STATE[25];
		STATE[10] ^= STATE[26];
		STATE[11] ^= STATE[27];
		STATE[12] ^= STATE[28];
		STATE[13] ^= STATE[29];
		STATE[14] ^= STATE[30];
		STATE[15] ^= STATE[31];
		
		System.arraycopy(STATE, 16, SCRATCH, 0, 2);
		System.arraycopy(STATE, 18, STATE, 16, 2);
		System.arraycopy(SCRATCH, 0, STATE, 18, 2);
		System.arraycopy(STATE, 20, SCRATCH, 0, 2);
		System.arraycopy(STATE, 22, STATE, 20, 2);
		System.arraycopy(SCRATCH, 0, STATE, 22, 2);		
		System.arraycopy(STATE, 24, SCRATCH, 0, 2);
		System.arraycopy(STATE, 26, STATE, 24, 2);
		System.arraycopy(SCRATCH, 0, STATE, 26, 2);
		System.arraycopy(STATE, 28, SCRATCH, 0, 2);
		System.arraycopy(STATE, 30, STATE, 28, 2);
		System.arraycopy(SCRATCH, 0, STATE, 30, 2);
		
		STATE[16] += STATE[0 ];	
		STATE[17] += STATE[1 ];
		STATE[18] += STATE[2 ];
		STATE[19] += STATE[3 ];
		STATE[20] += STATE[4 ];
		STATE[21] += STATE[5 ];
		STATE[22] += STATE[6 ];
		STATE[23] += STATE[7 ];
		STATE[24] += STATE[8 ];
		STATE[25] += STATE[9 ];
		STATE[26] += STATE[10];
		STATE[27] += STATE[11];
		STATE[28] += STATE[12];
		STATE[29] += STATE[13];
		STATE[30] += STATE[14];
		STATE[31] += STATE[15];
		
		STATE[0 ] = Bits.rotateLeft(STATE[0 ], 11);
		STATE[1 ] = Bits.rotateLeft(STATE[1 ], 11);
		STATE[2 ] = Bits.rotateLeft(STATE[2 ], 11);
		STATE[3 ] = Bits.rotateLeft(STATE[3 ], 11);
		STATE[4 ] = Bits.rotateLeft(STATE[4 ], 11);
		STATE[5 ] = Bits.rotateLeft(STATE[5 ], 11);
		STATE[6 ] = Bits.rotateLeft(STATE[6 ], 11);
		STATE[7 ] = Bits.rotateLeft(STATE[7 ], 11);
		STATE[8 ] = Bits.rotateLeft(STATE[8 ], 11);
		STATE[9 ] = Bits.rotateLeft(STATE[9 ], 11);
		STATE[10] = Bits.rotateLeft(STATE[10], 11);
		STATE[11] = Bits.rotateLeft(STATE[11], 11);
		STATE[12] = Bits.rotateLeft(STATE[12], 11);
		STATE[13] = Bits.rotateLeft(STATE[13], 11);
		STATE[14] = Bits.rotateLeft(STATE[14], 11);
		STATE[15] = Bits.rotateLeft(STATE[15], 11);

		System.arraycopy(STATE, 0, SCRATCH, 0, 4);
		System.arraycopy(STATE, 4, STATE, 0, 4);
		System.arraycopy(SCRATCH, 0, STATE, 4, 4);
		System.arraycopy(STATE, 8, SCRATCH, 0, 4);
		System.arraycopy(STATE, 12, STATE, 8, 4);
		System.arraycopy(SCRATCH, 0, STATE, 12, 4);
		
		STATE[0 ] ^= STATE[16];
		STATE[1 ] ^= STATE[17];
		STATE[2 ] ^= STATE[18];
		STATE[3 ] ^= STATE[19];
		STATE[4 ] ^= STATE[20];
		STATE[5 ] ^= STATE[21];
		STATE[6 ] ^= STATE[22];
		STATE[7 ] ^= STATE[23];
		STATE[8 ] ^= STATE[24];
		STATE[9 ] ^= STATE[25];
		STATE[10] ^= STATE[26];
		STATE[11] ^= STATE[27];
		STATE[12] ^= STATE[28];
		STATE[13] ^= STATE[29];
		STATE[14] ^= STATE[30];
		STATE[15] ^= STATE[31];
		
		int T;
		T = STATE[0 ];
		STATE[0 ] = STATE[1 ];
		STATE[1 ] = T;
		T = STATE[2 ];
		STATE[2 ] = STATE[3 ];
		STATE[3 ] = T;
		T = STATE[4 ];
		STATE[4 ] = STATE[5 ];
		STATE[5 ] = T;
		T = STATE[6 ];
		STATE[6 ] = STATE[7 ];
		STATE[7 ] = T;
		T = STATE[8 ];
		STATE[8 ] = STATE[9 ];
		STATE[9 ] = T;
		T = STATE[10];
		STATE[10] = STATE[11];
		STATE[11] = T;
		T = STATE[12];
		STATE[12] = STATE[13];
		STATE[13] = T;
		T = STATE[14];
		STATE[14] = STATE[15];
		STATE[15] = T;
	}

	public void reset()
	{
		super.reset();
		System.arraycopy(IV, 0, STATE, 0, 32);
	}
	
	public void processNext(byte[] bytes, int offset)
	{
		for(int i = 0; i < this.block; i++)
			STATE[i >> 2] ^= (bytes[offset++] & 0xFF) << Bits.BYTESHIFTS[i & 3];
		for(int i = 0; i < round; i++)
			doubleRound(STATE);
		if(sRound)
			singleRound(STATE, SCRATCH);
	}	
	
	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		remaining[pos++] = (byte) 0x80;
		Arrays.fill(remaining, pos, block, (byte) 0); 
		processNext(remaining, 0);
		STATE[31] ^= 1;
		for(int i = 0; i < close; i++)
			doubleRound(STATE);
		if(sClose)
			singleRound(STATE, SCRATCH);
		Bits.intsToSBytes(STATE, 0, out, start, this.outputLength());
		this.reset();
	}
	
	public CubeHashState getState()
	{
		return new CubeHashState(this);
	}
	
	public void updateState(CubeHashState state)
	{
		state.update(this);
	}

	public void loadCustom(CubeHashState state)
	{
		System.arraycopy(state.state, 0, STATE, 0, 32);
	}
	
	protected static final class CubeHashState extends MerkleState<CubeHashState, CubeHash>
	{
		protected final int block;
		
		protected int[] state;
		
		protected CubeHashStateFactory factory;
		
		public CubeHashState(byte[] bytes, int start, int block)
		{
			super(bytes, start);
			this.block = block;
			this.factory = new CubeHashStateFactory(block);
		}
		
		public CubeHashState(CubeHash ch)
		{
			super(ch);
			this.block = ch.block;
			this.factory = new CubeHashStateFactory(block);
		}
		
		public int stateID()
		{
			return IState.CUBEHASH;
		}

		public Factory<CubeHashState> factory()
		{
			return factory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.CUBEHASHSTATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeInts(state);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.intsToBytes(state, 0, bytes, start, 32);
		}

		protected void addCustom(IIncomingStream os) throws IOException
		{
			state = os.readInts(32);
		}

		protected void addCustom(byte[] bytes, int start)
		{
			state = new int[32];
			Bits.bytesToInts(bytes, start, state, 0, 32);
		}

		protected void addCustom(CubeHash hash)
		{
			state = ArrayUtil.copy(hash.STATE);
		}

		protected void updateCustom(CubeHash hash)
		{
			if(state == null)
				state = ArrayUtil.copy(hash.STATE);
			else
				System.arraycopy(hash.STATE, 0, state, 0, 32);
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, 0);
			state = null;
		}

		protected boolean compareCustom(CubeHashState state)
		{
			return ArrayUtil.equals(state.state, this.state);
		}

		protected int customSize()
		{
			return 128;
		}
	}
	
	protected static final class CubeHashStateFactory extends MerkleStateFactory<CubeHashState, CubeHash>
	{
		public CubeHashStateFactory(int block)
		{
			super(CubeHashState.class, block);
		}
		
		public CubeHashState construct(byte[] bytes, int pos)
		{
			return new CubeHashState(bytes, pos, this.size);
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
		CubeHash blake = new CubeHash(10, 8, 128, 8, 60);
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}

	/*
	 * CUBEHASH-OUTPUT-INITROUNDS-INPERROUND-ROUNDSPERIN-FINROUNDS
	 * e.g CUBEHASH-256-8-8-16-8
	 */
	
	public static final CubeHashFactory factory = new CubeHashFactory();
	
	public HashFactory<CubeHash> factory()
	{
		return factory;
	}
	
	private static final class CubeHashFactory extends HashFactory<CubeHash>
	{

		public CubeHash build(CryptoString str)
		{
			int out = str.nextArg().toInt() / 8;
			int ini = str.nextArg().toInt();
			int inp = str.nextArg().toInt();
			int rou = str.nextArg().toInt();
			int fin = str.nextArg().toInt();
			return new CubeHash(ini, inp, out, rou, fin);
		}
		
	}
	
}

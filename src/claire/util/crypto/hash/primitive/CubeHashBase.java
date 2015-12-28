package claire.util.crypto.hash.primitive;

import java.util.Arrays;

import claire.util.memory.Bits;

abstract class CubeHashBase
	   	 extends MerkleHash {
	
	protected final int[] STATE = new int[32];
	
	private final int block;

	protected CubeHashBase(int block, int out) 
	{
		super(block, out);
		this.block = block;
	}
	
	protected abstract void init();
	protected abstract void process();
	protected abstract void last();
	
	private void reset()
	{
		this.init();
	}

	public void processNext(byte[] bytes, int offset)
	{
		for(int i = 0; i < this.block; i++)
			STATE[i >> 2] ^= (bytes[offset++] & 0xFF) << Bits.BYTESHIFTS[i & 3];
		process();
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
	
	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		remaining[pos++] = (byte) 0x80;
		Arrays.fill(remaining, pos, block, (byte) 0); 
		processNext(remaining, 0);
		STATE[31] ^= 1;
		last();
		Bits.intsToSBytes(STATE, 0, out, start, this.outputLength());
		this.reset();
	}

}
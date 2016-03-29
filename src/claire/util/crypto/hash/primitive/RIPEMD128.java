package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.MerkleHash.MerkleState;
import claire.util.crypto.hash.primitive.RIPEMD128.RIPEMD128State;
import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public final class RIPEMD128 
	  		 extends MerkleHash<RIPEMD128State, RIPEMD128> {
	
	private static final int[] PERMUTE = 
		{
			 0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
			 7,  4, 13,  1, 10,  6, 15,  3, 12,  0,  9,  5,  2, 14, 11,  8,
			 3, 10, 14,  4,  9, 15,  8,  1,  2,  7,  0,  6, 13, 11,  5, 12,
			 1,  9, 11, 10,  0,  8, 12,  4, 13,  3,  7, 15, 14,  5,  6,  2,
			 5, 14,  7,  0,  9,  2, 11,  4, 13,  6, 15,  8,  1, 10,  3, 12,
			 6, 11,  3,  7,  0, 13,  5, 10, 14, 15,  8, 12,  4,  9,  1,  2,
			15,  5,  1,  3,  7, 14,  6,  9, 11,  8, 12,  2, 10,  0,  4, 13,
			 8,  6,  4,  1,  3, 11, 15,  0,  5, 12,  2, 13,  9,  7, 10, 14
		};
	
	private static final int[] ROTATE =
		{
			11, 14, 15, 12,  5,  8,  7,  9, 11, 13, 14, 15,  6,  7,  9,  8,
			 7,  6,  8, 13, 11,  9,  7, 15,  7, 12, 15,  9, 11,  7, 13, 12,
			11, 13,  6,  7, 14,  9, 13, 15, 14,  8, 13,  6,  5, 12,  7,  5,
			11, 12, 14, 15, 14, 15,  9,  8,  9, 14,  5,  6,  8,  6,  5, 12,
			 8,  9,  9, 11, 13, 15, 15,  5,  7,  7,  8, 11, 14, 14, 12,  6,
			 9, 13, 15,  7, 12,  8,  9, 11,  7,  7, 12,  7,  6, 15, 13, 11,
			 9,  7, 15, 11,  8,  6,  6, 14, 12, 13,  5, 14, 13, 13,  7,  5,
			15,  5,  8, 11, 14, 14,  6, 14,  6,  9, 12,  9, 12,  5, 15,  8
		};
	
	private static final int M1 = 0x5a827999;
	private static final int M2 = 0x6ed9eba1;
	private static final int M3 = 0x8f1bbcdc;
	private static final int M4 = 0x6d703ef3;
	private static final int M5 = 0x5c4dd124;
	private static final int M6 = 0x50a28be6;
	
	protected final int[] STATE = new int[4];
	
	protected long length = 0;

	public RIPEMD128() 
	{
		super(64, 16);
		reset();
	}
	
	private void reset()
	{
		length = 0;
		STATE[0] = 0x67452301;
		STATE[1] = 0xefcdab89;
		STATE[2] = 0x98badcfe;
		STATE[3] = 0x10325476;
	}
	
	private static int F1(int A, int B, int C)
	{
		return A ^ B ^ C;
	}

	private static int F2(int A, int B, int C)
	{
		return (A & B) | (~A & C);
	}

	private static int F3(int A, int B, int C)
	{
		return (A | ~B) ^ C;
	}

	private static int F4(int A, int B, int C)
	{
	    return (A & C) | (B & ~C);
	}
	    
	public void processNext(byte[] bytes, int offset)
	{
		length += 64;
		
		int[] IN = new int[16];
		Bits.bytesToInts(bytes, offset, IN, 0, 16);
		
		int A, AA, B, BB, C, CC, D, DD;
		AA = A = STATE[0];
		BB = B = STATE[1];
		CC = C = STATE[2];
		DD = D = STATE[3];
		
		int j = 0;
		while(j < 16)
		{
			A = Bits.rotateLeft(A + F1(B, C, D) + IN[PERMUTE[j]]	 , ROTATE[j++]);
			D = Bits.rotateLeft(D + F1(A, B, C) + IN[PERMUTE[j]]	 , ROTATE[j++]);
			C = Bits.rotateLeft(C + F1(D, A, B) + IN[PERMUTE[j]]	 , ROTATE[j++]);
			B = Bits.rotateLeft(B + F1(C, D, A) + IN[PERMUTE[j]]     , ROTATE[j++]);
		}
		while(j < 32)
		{
			A = Bits.rotateLeft(A + F2(B, C, D) + IN[PERMUTE[j]] + M1, ROTATE[j++]);
			D = Bits.rotateLeft(D + F2(A, B, C) + IN[PERMUTE[j]] + M1, ROTATE[j++]);
			C = Bits.rotateLeft(C + F2(D, A, B) + IN[PERMUTE[j]] + M1, ROTATE[j++]);
			B = Bits.rotateLeft(B + F2(C, D, A) + IN[PERMUTE[j]] + M1, ROTATE[j++]);
		}
		while(j < 48)
		{
			A = Bits.rotateLeft(A + F3(B, C, D) + IN[PERMUTE[j]] + M2, ROTATE[j++]);
			D = Bits.rotateLeft(D + F3(A, B, C) + IN[PERMUTE[j]] + M2, ROTATE[j++]);
			C = Bits.rotateLeft(C + F3(D, A, B) + IN[PERMUTE[j]] + M2, ROTATE[j++]);
			B = Bits.rotateLeft(B + F3(C, D, A) + IN[PERMUTE[j]] + M2, ROTATE[j++]);
		}
		while(j < 64)
		{
			A = Bits.rotateLeft(A + F4(B, C, D) + IN[PERMUTE[j]] + M3, ROTATE[j++]);
			D = Bits.rotateLeft(D + F4(A, B, C) + IN[PERMUTE[j]] + M3, ROTATE[j++]);
			C = Bits.rotateLeft(C + F4(D, A, B) + IN[PERMUTE[j]] + M3, ROTATE[j++]);
			B = Bits.rotateLeft(B + F4(C, D, A) + IN[PERMUTE[j]] + M3, ROTATE[j++]);
		}
		while(j < 80)
		{
			AA = Bits.rotateLeft(AA + F4(BB, CC, DD) + IN[PERMUTE[j]] + M6, ROTATE[j++]);
			DD = Bits.rotateLeft(DD + F4(AA, BB, CC) + IN[PERMUTE[j]] + M6, ROTATE[j++]);
			CC = Bits.rotateLeft(CC + F4(DD, AA, BB) + IN[PERMUTE[j]] + M6, ROTATE[j++]);
			BB = Bits.rotateLeft(BB + F4(CC, DD, AA) + IN[PERMUTE[j]] + M6, ROTATE[j++]);
		}
		while(j < 96)
		{
			AA = Bits.rotateLeft(AA + F3(BB, CC, DD) + IN[PERMUTE[j]] + M5, ROTATE[j++]);
			DD = Bits.rotateLeft(DD + F3(AA, BB, CC) + IN[PERMUTE[j]] + M5, ROTATE[j++]);
			CC = Bits.rotateLeft(CC + F3(DD, AA, BB) + IN[PERMUTE[j]] + M5, ROTATE[j++]);
			BB = Bits.rotateLeft(BB + F3(CC, DD, AA) + IN[PERMUTE[j]] + M5, ROTATE[j++]);
		}
		while(j < 112)
		{
			AA = Bits.rotateLeft(AA + F2(BB, CC, DD) + IN[PERMUTE[j]] + M4, ROTATE[j++]);
			DD = Bits.rotateLeft(DD + F2(AA, BB, CC) + IN[PERMUTE[j]] + M4, ROTATE[j++]);
			CC = Bits.rotateLeft(CC + F2(DD, AA, BB) + IN[PERMUTE[j]] + M4, ROTATE[j++]);
			BB = Bits.rotateLeft(BB + F2(CC, DD, AA) + IN[PERMUTE[j]] + M4, ROTATE[j++]);
		}
		while(j < 128)
		{
			AA = Bits.rotateLeft(AA + F1(BB, CC, DD) + IN[PERMUTE[j]]	  , ROTATE[j++]);
			DD = Bits.rotateLeft(DD + F1(AA, BB, CC) + IN[PERMUTE[j]]	  , ROTATE[j++]);
			CC = Bits.rotateLeft(CC + F1(DD, AA, BB) + IN[PERMUTE[j]]	  , ROTATE[j++]);
			BB = Bits.rotateLeft(BB + F1(CC, DD, AA) + IN[PERMUTE[j]]	  , ROTATE[j++]);
		}
		
		int TEMP = STATE[0];
		STATE[0] = STATE[1] + C + DD;
		STATE[1] = STATE[2] + D + AA;
		STATE[2] = STATE[3] + A + BB;
		STATE[3] =     TEMP + B + CC;
	}

	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		byte[] bytes = new byte[64];
		System.arraycopy(remaining, 0, bytes, 0, pos);
		bytes[pos] = (byte) 0x80;
		length += pos;
		if(pos >= 56) {
			processNext(bytes, 0);
			length -= 64;
			Arrays.fill(bytes, (byte) 0);
		}
		Bits.longToBytes(length << 3, bytes, 56);
		processNext(bytes, 0);
		Bits.intsToBytes(STATE, 0, out, start);
		reset();
	}
	
	public RIPEMD128State getState()
	{
		return new RIPEMD128State(this);
	}

	public void updateState(RIPEMD128State state)
	{
		state.update(this);
	}

	public void loadCustom(RIPEMD128State state)
	{
		System.arraycopy(state.state, 0, STATE, 0, 4);
		length = state.length;
	}
	
	public static final RIPEMD128StateFactory sfactory = new RIPEMD128StateFactory();
	
	protected static final class RIPEMD128State extends MerkleState<RIPEMD128State, RIPEMD128>
	{
		protected int[] state;
		protected long length;
		
		public RIPEMD128State(byte[] bytes, int pos) 
		{
			super(bytes, pos);
		}
		
		public RIPEMD128State(RIPEMD128 rmd) 
		{
			super(rmd);
		}

		public Factory<RIPEMD128State> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.RIPEMD128STATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeInts(state);
			os.writeLong(length);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.intsToBytes(state, 0, bytes, start, 4); start += 16;
			Bits.longToBytes(length, bytes, start);
		}

		protected void addCustom(IIncomingStream os) throws IOException
		{
			state = os.readInts(4);
			length = os.readLong();
		}

		protected void addCustom(byte[] bytes, int start)
		{
			state = new int[4];
			Bits.bytesToInts(bytes, start, state, 0, 4); start += 16;
			length = Bits.longFromBytes(bytes, start);
		}

		protected void addCustom(RIPEMD128 hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			length = hash.length;
		}

		protected void updateCustom(RIPEMD128 hash)
		{
			if(state == null)
				state = ArrayUtil.copy(hash.STATE);
			else
				System.arraycopy(hash.STATE, 0, state, 0, 4);
			length = hash.length;
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, 0);
			state = null;
			length = 0;
		}

		protected boolean compareCustom(RIPEMD128State state)
		{
			return state.length == this.length && ArrayUtil.equals(state.state, this.state);
		}

		protected int customSize()
		{
			return 24;
		}
		
	}
	
	protected static final class RIPEMD128StateFactory extends MerkleStateFactory<RIPEMD128State, RIPEMD128>
	{

		protected RIPEMD128StateFactory() 
		{
			super(RIPEMD128State.class, 64);
		}

		protected RIPEMD128State construct(byte[] bytes, int pos)
		{
			return new RIPEMD128State(bytes, pos);
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
		RIPEMD128 blake = new RIPEMD128();
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		int i = 0;
		i += IPersistable.test(state);
		return i;
	}
	
}

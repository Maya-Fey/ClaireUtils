package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.hash.primitive.MerkleHash.MerkleState;
import claire.util.crypto.hash.primitive.RIPEMD320.RIPEMD320State;
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

public final class RIPEMD320 
	  		 extends MerkleHash<RIPEMD320State, RIPEMD320> {
	
	private static final int[] PERMUTE = 
		{
		     0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
			 5, 14,  7,  0,  9,  2, 11,  4, 13,  6, 15,  8,  1, 10,  3, 12,
			 7,  4, 13,  1, 10,  6, 15,  3, 12,  0,  9,  5,  2, 14, 11,  8,
			 6, 11,  3,  7,  0, 13,  5, 10, 14, 15,  8, 12,  4,  9,  1,  2,
			 3, 10, 14,  4,  9, 15,  8,  1,  2,  7,  0,  6, 13, 11,  5, 12,
			15,  5,  1,  3,  7, 14,  6,  9, 11,  8, 12,  2, 10,  0,  4, 13,
			 1,  9, 11, 10,  0,  8, 12,  4, 13,  3,  7, 15, 14,  5,  6,  2,	 
			 8,  6,  4,  1,  3, 11, 15,  0,  5, 12,  2, 13,  9,  7, 10, 14,
			 4,  0,  5,  9,  7, 12,  2, 10, 14,  1,  3,  8, 11,  6, 15, 13,
			12, 15, 10,  4,  1,  5,  8,  7,  6,  2, 13, 14,  0,  3,  9, 11,
		};
	
	private static final int[] ROTATE =
		{
			11, 14, 15, 12,  5,  8,  7,  9, 11, 13, 14, 15,  6,  7,  9,  8,  
			 8,  9,  9, 11, 13, 15, 15,  5,  7,  7,  8, 11, 14, 14, 12,  6,  
			 7,  6,  8, 13, 11,  9,  7, 15,  7, 12, 15,  9, 11,  7, 13, 12,  
			 9, 13, 15,  7, 12,  8,  9, 11,  7,  7, 12,  7,  6, 15, 13, 11, 
			11, 13,  6,  7, 14,  9, 13, 15, 14,  8, 13,  6,  5, 12,  7,  5,  
			 9,  7, 15, 11,  8,  6,  6, 14, 12, 13,  5, 14, 13, 13,  7,  5, 
			11, 12, 14, 15, 14, 15,  9,  8,  9, 14,  5,  6,  8,  6,  5, 12, 
			15,  5,  8, 11, 14, 14,  6, 14,  6,  9, 12,  9, 12,  5, 15,  8,  
			 9, 15,  5, 11,  6,  8, 13, 12,  5, 12, 13, 14, 11,  8,  5,  6,  
			 8,  5, 12,  9, 12,  5, 14,  6,  8, 13,  6,  5, 15, 13, 11, 11,
		};
	
	private static final int M1 = 0x50a28be6;
	private static final int M2 = 0x5a827999;
	private static final int M3 = 0x5c4dd124;
	private static final int M4 = 0x6ed9eba1;
	private static final int M5 = 0x6d703ef3;
	private static final int M6 = 0x8f1bbcdc;
	private static final int M7 = 0x7a6d76e9;
	private static final int M8 = 0xa953fd4e;
	
	private final int[] STATE = new int[10];
	
	private long length = 0;

	public RIPEMD320() 
	{
		super(64, 40);
		reset();
	}
	
	public int hashID()
	{
		return Hash.RIPEMD320;
	}
	
	public void reset()
	{
		super.reset();
		length = 0;
        STATE[0] = 0x67452301;
        STATE[1] = 0xefcdab89;
        STATE[2] = 0x98badcfe;
        STATE[3] = 0x10325476;
        STATE[4] = 0xc3d2e1f0;
        STATE[5] = 0x76543210; 
        STATE[6] = 0xfedcba98;
        STATE[7] = 0x89abcdef; 
        STATE[8] = 0x01234567; 
        STATE[9] = 0x3c2d1e0f;
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
	
	private static int F5(int A, int B, int C)
	{
	    return A ^ (B | ~C);
	}
	    
	public void processNext(byte[] bytes, int offset)
	{
		length += 64;
		
		int[] IN = new int[16];
		Bits.bytesToInts(bytes, offset, IN, 0, 16);
		
		int A, AA, B, BB, C, CC, D, DD, E, EE, TEMP;
		
		A = STATE[0];
		B = STATE[1];
		C = STATE[2];
		D = STATE[3];
		E = STATE[4];
		AA = STATE[5];
		BB = STATE[6];
		CC = STATE[7];
		DD = STATE[8];
		EE = STATE[9];
		
		int j = 0;
		
		while(j < 15)
		{
			A = Bits.rotateLeft(A + F1(B, C, D) + IN[PERMUTE[j]], ROTATE[j++]) + E; 
				C = Bits.rotateLeft(C, 10);
			E = Bits.rotateLeft(E + F1(A, B, C) + IN[PERMUTE[j]], ROTATE[j++]) + D; 
				B = Bits.rotateLeft(B, 10);
			D = Bits.rotateLeft(D + F1(E, A, B) + IN[PERMUTE[j]], ROTATE[j++]) + C; 
				A = Bits.rotateLeft(A, 10);
			C = Bits.rotateLeft(C + F1(D, E, A) + IN[PERMUTE[j]], ROTATE[j++]) + B; 
				E = Bits.rotateLeft(E, 10);
			B = Bits.rotateLeft(B + F1(C, D, E) + IN[PERMUTE[j]], ROTATE[j++]) + A; 
				D = Bits.rotateLeft(D, 10);
		}
		A = Bits.rotateLeft(A + F1(B, C, D) + IN[PERMUTE[j]], ROTATE[j++]) + E; 
			C = Bits.rotateLeft(C, 10);
			
		while(j < 31)
		{
			AA = Bits.rotateLeft(AA + F5(BB, CC, DD) + IN[PERMUTE[j]] + M1, ROTATE[j++]) + EE; 
				CC = Bits.rotateLeft(CC, 10);
			EE = Bits.rotateLeft(EE + F5(AA, BB, CC) + IN[PERMUTE[j]] + M1, ROTATE[j++]) + DD; 
				BB = Bits.rotateLeft(BB, 10);
			DD = Bits.rotateLeft(DD + F5(EE, AA, BB) + IN[PERMUTE[j]] + M1, ROTATE[j++]) + CC; 
				AA = Bits.rotateLeft(AA, 10);
			CC = Bits.rotateLeft(CC + F5(DD, EE, AA) + IN[PERMUTE[j]] + M1, ROTATE[j++]) + BB; 
				EE = Bits.rotateLeft(EE, 10);
			BB = Bits.rotateLeft(BB + F5(CC, DD, EE) + IN[PERMUTE[j]] + M1, ROTATE[j++]) + AA; 
				DD = Bits.rotateLeft(DD, 10);
		}
		AA = Bits.rotateLeft(AA + F5(BB, CC, DD) + IN[PERMUTE[j]] + M1, ROTATE[j++]) + EE; 
			CC = Bits.rotateLeft(CC, 10);
		
		TEMP = AA; AA = A; A = TEMP;
			
		while(j < 47)
		{
			E = Bits.rotateLeft(E + F2(A, B, C) + IN[PERMUTE[j]] + M2, ROTATE[j++]) + D; 
				B = Bits.rotateLeft(B, 10);
			D = Bits.rotateLeft(D + F2(E, A, B) + IN[PERMUTE[j]] + M2, ROTATE[j++]) + C; 
				A = Bits.rotateLeft(A, 10);
			C = Bits.rotateLeft(C + F2(D, E, A) + IN[PERMUTE[j]] + M2, ROTATE[j++]) + B; 
				E = Bits.rotateLeft(E, 10);
			B = Bits.rotateLeft(B + F2(C, D, E) + IN[PERMUTE[j]] + M2, ROTATE[j++]) + A; 
				D = Bits.rotateLeft(D, 10);
			A = Bits.rotateLeft(A + F2(B, C, D) + IN[PERMUTE[j]] + M2, ROTATE[j++]) + E; 
				C = Bits.rotateLeft(C, 10);
		}
		E = Bits.rotateLeft(E + F2(A, B, C) + IN[PERMUTE[j]] + M2, ROTATE[j++]) + D; 
			B = Bits.rotateLeft(B, 10);		
		
		while(j < 63)
		{
			EE = Bits.rotateLeft(EE + F4(AA, BB, CC) + IN[PERMUTE[j]] + M3, ROTATE[j++]) + DD; 
				BB = Bits.rotateLeft(BB, 10);
			DD = Bits.rotateLeft(DD + F4(EE, AA, BB) + IN[PERMUTE[j]] + M3, ROTATE[j++]) + CC; 
				AA = Bits.rotateLeft(AA, 10);
			CC = Bits.rotateLeft(CC + F4(DD, EE, AA) + IN[PERMUTE[j]] + M3, ROTATE[j++]) + BB; 
				EE = Bits.rotateLeft(EE, 10);
			BB = Bits.rotateLeft(BB + F4(CC, DD, EE) + IN[PERMUTE[j]] + M3, ROTATE[j++]) + AA; 
				DD = Bits.rotateLeft(DD, 10);
			AA = Bits.rotateLeft(AA + F4(BB, CC, DD) + IN[PERMUTE[j]] + M3, ROTATE[j++]) + EE; 
				CC = Bits.rotateLeft(CC, 10);
		}
		EE = Bits.rotateLeft(EE + F4(AA, BB, CC) + IN[PERMUTE[j]] + M3, ROTATE[j++]) + DD; 
			BB = Bits.rotateLeft(BB, 10);
			
		TEMP = BB; BB = B; B = TEMP;
			
		while(j < 79)
		{	
			D = Bits.rotateLeft(D + F3(E, A, B) + IN[PERMUTE[j]] + M4, ROTATE[j++]) + C; 
				A = Bits.rotateLeft(A, 10);
			C = Bits.rotateLeft(C + F3(D, E, A) + IN[PERMUTE[j]] + M4, ROTATE[j++]) + B; 
				E = Bits.rotateLeft(E, 10);
			B = Bits.rotateLeft(B + F3(C, D, E) + IN[PERMUTE[j]] + M4, ROTATE[j++]) + A; 
				D = Bits.rotateLeft(D, 10);
			A = Bits.rotateLeft(A + F3(B, C, D) + IN[PERMUTE[j]] + M4, ROTATE[j++]) + E; 
				C = Bits.rotateLeft(C, 10);
			E = Bits.rotateLeft(E + F3(A, B, C) + IN[PERMUTE[j]] + M4, ROTATE[j++]) + D; 
				B = Bits.rotateLeft(B, 10);
		}
		D = Bits.rotateLeft(D + F3(E, A, B) + IN[PERMUTE[j]] + M4, ROTATE[j++]) + C; 
			A = Bits.rotateLeft(A, 10);
			
		while(j < 95)
		{
			DD = Bits.rotateLeft(DD + F3(EE, AA, BB) + IN[PERMUTE[j]] + M5, ROTATE[j++]) + CC; 
				AA = Bits.rotateLeft(AA, 10);
			CC = Bits.rotateLeft(CC + F3(DD, EE, AA) + IN[PERMUTE[j]] + M5, ROTATE[j++]) + BB; 
				EE = Bits.rotateLeft(EE, 10);
			BB = Bits.rotateLeft(BB + F3(CC, DD, EE) + IN[PERMUTE[j]] + M5, ROTATE[j++]) + AA; 
				DD = Bits.rotateLeft(DD, 10);
			AA = Bits.rotateLeft(AA + F3(BB, CC, DD) + IN[PERMUTE[j]] + M5, ROTATE[j++]) + EE; 
				CC = Bits.rotateLeft(CC, 10);
			EE = Bits.rotateLeft(EE + F3(AA, BB, CC) + IN[PERMUTE[j]] + M5, ROTATE[j++]) + DD; 
				BB = Bits.rotateLeft(BB, 10);
		}
		DD = Bits.rotateLeft(DD + F3(EE, AA, BB) + IN[PERMUTE[j]] + M5, ROTATE[j++]) + CC; 
			AA = Bits.rotateLeft(AA, 10);
			
		TEMP = CC; CC = C; C = TEMP;

		while(j < 111)
		{		
			C = Bits.rotateLeft(C + F4(D, E, A) + IN[PERMUTE[j]] + M6, ROTATE[j++]) + B; 
				E = Bits.rotateLeft(E, 10);
			B = Bits.rotateLeft(B + F4(C, D, E) + IN[PERMUTE[j]] + M6, ROTATE[j++]) + A; 
				D = Bits.rotateLeft(D, 10);
			A = Bits.rotateLeft(A + F4(B, C, D) + IN[PERMUTE[j]] + M6, ROTATE[j++]) + E; 
				C = Bits.rotateLeft(C, 10);
			E = Bits.rotateLeft(E + F4(A, B, C) + IN[PERMUTE[j]] + M6, ROTATE[j++]) + D; 
				B = Bits.rotateLeft(B, 10);
			D = Bits.rotateLeft(D + F4(E, A, B) + IN[PERMUTE[j]] + M6, ROTATE[j++]) + C; 
				A = Bits.rotateLeft(A, 10);
		}
		C = Bits.rotateLeft(C + F4(D, E, A) + IN[PERMUTE[j]] + M6, ROTATE[j++]) + B; 
			E = Bits.rotateLeft(E, 10);
			
		while(j < 127)
		{
			CC = Bits.rotateLeft(CC + F2(DD, EE, AA) + IN[PERMUTE[j]] + M7, ROTATE[j++]) + BB; 
				EE = Bits.rotateLeft(EE, 10);
			BB = Bits.rotateLeft(BB + F2(CC, DD, EE) + IN[PERMUTE[j]] + M7, ROTATE[j++]) + AA; 
				DD = Bits.rotateLeft(DD, 10);
			AA = Bits.rotateLeft(AA + F2(BB, CC, DD) + IN[PERMUTE[j]] + M7, ROTATE[j++]) + EE; 
				CC = Bits.rotateLeft(CC, 10);
			EE = Bits.rotateLeft(EE + F2(AA, BB, CC) + IN[PERMUTE[j]] + M7, ROTATE[j++]) + DD; 
				BB = Bits.rotateLeft(BB, 10);
			DD = Bits.rotateLeft(DD + F2(EE, AA, BB) + IN[PERMUTE[j]] + M7, ROTATE[j++]) + CC; 
				AA = Bits.rotateLeft(AA, 10);
		}
		CC = Bits.rotateLeft(CC + F2(DD, EE, AA) + IN[PERMUTE[j]] + M7, ROTATE[j++]) + BB; 
			EE = Bits.rotateLeft(EE, 10);
			
		TEMP = DD; DD = D; D = TEMP;
		
		while(j < 143)
		{					
			B = Bits.rotateLeft(B + F5(C, D, E) + IN[PERMUTE[j]] + M8, ROTATE[j++]) + A; 
				D = Bits.rotateLeft(D, 10);
			A = Bits.rotateLeft(A + F5(B, C, D) + IN[PERMUTE[j]] + M8, ROTATE[j++]) + E; 
				C = Bits.rotateLeft(C, 10);
			E = Bits.rotateLeft(E + F5(A, B, C) + IN[PERMUTE[j]] + M8, ROTATE[j++]) + D; 
				B = Bits.rotateLeft(B, 10);
			D = Bits.rotateLeft(D + F5(E, A, B) + IN[PERMUTE[j]] + M8, ROTATE[j++]) + C; 
				A = Bits.rotateLeft(A, 10);
			C = Bits.rotateLeft(C + F5(D, E, A) + IN[PERMUTE[j]] + M8, ROTATE[j++]) + B; 
				E = Bits.rotateLeft(E, 10);
		}
		B = Bits.rotateLeft(B + F5(C, D, E) + IN[PERMUTE[j]] + M8, ROTATE[j++]) + A; 
			D = Bits.rotateLeft(D, 10);
			
		while(j < 159)
		{	
			BB = Bits.rotateLeft(BB + F1(CC, DD, EE) + IN[PERMUTE[j]], ROTATE[j++]) + AA; 
				DD = Bits.rotateLeft(DD, 10);
			AA = Bits.rotateLeft(AA + F1(BB, CC, DD) + IN[PERMUTE[j]], ROTATE[j++]) + EE; 
				CC = Bits.rotateLeft(CC, 10);
			EE = Bits.rotateLeft(EE + F1(AA, BB, CC) + IN[PERMUTE[j]], ROTATE[j++]) + DD; 
				BB = Bits.rotateLeft(BB, 10);
			DD = Bits.rotateLeft(DD + F1(EE, AA, BB) + IN[PERMUTE[j]], ROTATE[j++]) + CC; 
				AA = Bits.rotateLeft(AA, 10);
			CC = Bits.rotateLeft(CC + F1(DD, EE, AA) + IN[PERMUTE[j]], ROTATE[j++]) + BB; 
				EE = Bits.rotateLeft(EE, 10);
		}
		BB = Bits.rotateLeft(BB + F1(CC, DD, EE) + IN[PERMUTE[j]], ROTATE[j++]) + AA; 
			DD = Bits.rotateLeft(DD, 10);
		
		STATE[0] += A;
		STATE[1] += B;
		STATE[2] += C;
		STATE[3] += D;
		STATE[4] += EE;
		STATE[5] += AA;
		STATE[6] += BB;
		STATE[7] += CC;
		STATE[8] += DD;
		STATE[9] += E;
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
	
	public RIPEMD320State getState()
	{
		return new RIPEMD320State(this);
	}

	public void updateState(RIPEMD320State state)
	{
		state.update(this);
	}

	public void loadCustom(RIPEMD320State state)
	{
		System.arraycopy(state.state, 0, STATE, 0, 10);
		length = state.length;
	}
	
	public static final RIPEMD320StateFactory sfactory = new RIPEMD320StateFactory();
	
	protected static final class RIPEMD320State extends MerkleState<RIPEMD320State, RIPEMD320>
	{
		protected int[] state;
		protected long length;
		
		public RIPEMD320State(byte[] bytes, int pos) 
		{
			super(bytes, pos);
		}
		
		public RIPEMD320State(RIPEMD320 rmd) 
		{
			super(rmd);
		}

		public Factory<RIPEMD320State> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.RIPEMD320STATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeInts(state);
			os.writeLong(length);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.intsToBytes(state, 0, bytes, start, 10); start += 40;
			Bits.longToBytes(length, bytes, start);
		}

		protected void addCustom(IIncomingStream os) throws IOException
		{
			state = os.readInts(10);
			length = os.readLong();
		}

		protected void addCustom(byte[] bytes, int start)
		{
			state = new int[10];
			Bits.bytesToInts(bytes, start, state, 0, 10); start += 40;
			length = Bits.longFromBytes(bytes, start);
		}

		protected void addCustom(RIPEMD320 hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			length = hash.length;
		}

		protected void updateCustom(RIPEMD320 hash)
		{
			if(state == null)
				state = ArrayUtil.copy(hash.STATE);
			else
				System.arraycopy(hash.STATE, 0, state, 0, 10);
			length = hash.length;
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, 0);
			state = null;
			length = 0;
		}

		protected boolean compareCustom(RIPEMD320State state)
		{
			return state.length == this.length && ArrayUtil.equals(state.state, this.state);
		}

		protected int customSize()
		{
			return 48;
		}
		
	}
	
	protected static final class RIPEMD320StateFactory extends MerkleStateFactory<RIPEMD320State, RIPEMD320>
	{

		protected RIPEMD320StateFactory() 
		{
			super(RIPEMD320State.class, 64);
		}

		protected RIPEMD320State construct(byte[] bytes, int pos)
		{
			return new RIPEMD320State(bytes, pos);
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
		RIPEMD320 blake = new RIPEMD320();
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}
	
	public HashFactory<RIPEMD320> factory()
	{
		return factory;
	}
	
	public static final RIPEMD320Factory factory = new RIPEMD320Factory();
	
	public static final class RIPEMD320Factory extends HashFactory<RIPEMD320>
	{

		public RIPEMD320 build(CryptoString str)
		{
			return new RIPEMD320();
		}
		
	}

}

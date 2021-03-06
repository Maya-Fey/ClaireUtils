package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.MerkleHash.MerkleState;
import claire.util.crypto.hash.primitive.SHA2_Base_32.SHA2_32State;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

abstract class SHA2_Base_32<Hash extends SHA2_Base_32<Hash>> 
	     extends MerkleHash<SHA2_32State, Hash> {
	
	private static final int[] MIX =
		{
			0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 
			0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
			0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 
			0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
			0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 
			0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
			0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 
			0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
			0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 
			0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
			0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 
			0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070, 
			0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 
			0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
			0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 
			0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
		};
	
	protected final int[] STATE = new int[8];
	protected long length;
	
	protected abstract void complete(byte[] bytes, int pos, byte[] out, int start, int max);

	protected SHA2_Base_32(int out) 
	{
		super(64, out);
		reset();
	}

	private static int F1(int A, int B, int C)
	{
	    return ((A & B) ^ ((~A) & C));
	}

	private static int F2(int A, int B, int C)
	{
	    return ((A & B) ^ (A & C) ^ (B & C));
	}
	
	private static int M1(int A)
	{
	    return Bits.rotateRight(A, 2) ^ Bits.rotateRight(A, 13) ^ Bits.rotateLeft(A, 10);
	}

	private static int M2(int A)
	{
	    return Bits.rotateRight(A, 6) ^ Bits.rotateRight(A, 11) ^ Bits.rotateLeft(A, 7);
	}
	
	private static int E1(int A)
	{
		return Bits.rotateRight(A, 7) ^ Bits.rotateLeft(A, 14) ^ (A >>> 3);
	}
	
	private static int E2(int A)
	{
		return Bits.rotateLeft(A, 15) ^ Bits.rotateLeft(A, 13) ^ (A >>> 10);
	}
	
	public void processNext(byte[] bytes, int offset)
	{
		length += 64;
		int[] IN = new int[64];
		Bits.BigEndian.bytesToInts(bytes, offset, IN, 0, 16);
		for(int i = 16; i < 64; i++)
            IN[i] = E2(IN[i - 2]) + IN[i - 7] + E1(IN[i - 15]) + IN[i - 16];
		
		int A = STATE[0],
			B = STATE[1],
			C = STATE[2],
			D = STATE[3],
			E = STATE[4],
			F = STATE[5],
			G = STATE[6],
			H = STATE[7];
		
		int i= 0;
		
		while(i < 64)
		{
			H += M2(E) + F1(E, F, G) + MIX[i] + IN[i++];
            D += H;
            H += M1(A) + F2(A, B, C);
  
            G += M2(D) + F1(D, E, F) + MIX[i] + IN[i++];
            C += G;
            G += M1(H) + F2(H, A, B);
        
            F += M2(C) + F1(C, D, E) + MIX[i] + IN[i++];
            B += F;
            F += M1(G) + F2(G, H, A);
   
            E += M2(B) + F1(B, C, D) + MIX[i] + IN[i++];
            A += E;
            E += M1(F) + F2(F, G, H);
   
            D += M2(A) + F1(A, B, C) + MIX[i] + IN[i++];
            H += D;
            D += M1(E) + F2(E, F, G);
  
            C += M2(H) + F1(H, A, B) + MIX[i] + IN[i++];
            G += C;
            C += M1(D) + F2(D, E, F);
  
            B += M2(G) + F1(G, H, A) + MIX[i] + IN[i++];
            F += B;
            B += M1(C) + F2(C, D, E);
    
            A += M2(F) + F1(F, G, H) + MIX[i] + IN[i++];
            E += A;
            A += M1(B) + F2(B, C, D);
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
	
	public void finalize(byte[] bytes, int pos, byte[] out, int start, int max)
	{
		try {
			complete(bytes, pos, out, start, max);
		} finally {
			reset();
		}
	}
	
	public SHA2_32State getState()
	{
		return new SHA2_32State(this);
	}

	public void updateState(SHA2_32State state)
	{
		state.update(this);
	}

	public void loadCustom(SHA2_32State state)
	{
		this.length = state.length;
		System.arraycopy(state.state, 0, this.STATE, 0, 8);
	}
	
	public static final SHA2_32StateFactory sfactory = new SHA2_32StateFactory();
	
	protected static final class SHA2_32State 
						   extends MerkleState<SHA2_32State, SHA2_Base_32<? extends SHA2_Base_32<?>>>
	{

		protected int[] state;
		protected long length;
		
		public SHA2_32State(SHA2_Base_32<? extends SHA2_Base_32<?>> hash) 
		{
			super(hash);
		}
		
		public SHA2_32State(byte[] bytes, int pos)
		{
			super(bytes, pos);
		}

		public Factory<SHA2_32State> factory()
		{
			return sfactory;
		}

		public int stateID()
		{
			return IState.SHA2_32;
		}
	
		public int NAMESPACE()
		{
			return _NAMESPACE.SHA2_32STATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeInts(state);
			os.writeLong(length);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.intsToBytes(state, 0, bytes, start, 8); start += 32;
			Bits.longToBytes(length, bytes, start);
		}

		protected void addCustom(IIncomingStream is) throws IOException
		{
			state = is.readInts(8);
			length = is.readLong();
		}
		
		protected void addCustom(byte[] bytes, int start)
		{
			state = new int[8];
			Bits.bytesToInts(bytes, start, state, 0, 8); start += 32;
			length = Bits.longFromBytes(bytes, start);
		}

		protected void addCustom(SHA2_Base_32<? extends SHA2_Base_32<?>> hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			this.length = hash.length;
		}

		protected void updateCustom(SHA2_Base_32<? extends SHA2_Base_32<?>> hash)
		{
			System.arraycopy(this.state, 0, hash.STATE, 0, 8);
			hash.length = this.length;
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, 0);
			state = null;
			length = 0;
		}

		protected boolean compareCustom(SHA2_32State state)
		{	
			return this.length == state.length && ArrayUtil.equals(this.state, state.state);
		}

		protected int customSize()
		{
			return 40;
		}
		
	}
	
	protected static final class SHA2_32StateFactory extends MerkleStateFactory<SHA2_32State, SHA2_Base_32<? extends SHA2_Base_32<?>>>
	{

		protected SHA2_32StateFactory() 
		{
			super(SHA2_32State.class, 64);
		}

		protected SHA2_32State construct(byte[] bytes, int pos)
		{
			return new SHA2_32State(bytes, pos);
		}
		
	}
	
}

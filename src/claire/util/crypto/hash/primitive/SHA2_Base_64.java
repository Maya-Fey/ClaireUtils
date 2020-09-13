package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.MerkleHash.MerkleState;
import claire.util.crypto.hash.primitive.SHA2_Base_64.SHA2_64State;
import claire.util.io.Factory;
import claire.util.math.counters.LongCounter;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

abstract class SHA2_Base_64<Hash extends SHA2_Base_64<Hash>> 
		 extends MerkleHash<SHA2_64State, Hash> {
	
	private static final long[] MIX = 
		{
			0x428a2f98d728ae22L, 0x7137449123ef65cdL, 0xb5c0fbcfec4d3b2fL, 0xe9b5dba58189dbbcL,
			0x3956c25bf348b538L, 0x59f111f1b605d019L, 0x923f82a4af194f9bL, 0xab1c5ed5da6d8118L,
			0xd807aa98a3030242L, 0x12835b0145706fbeL, 0x243185be4ee4b28cL, 0x550c7dc3d5ffb4e2L,
			0x72be5d74f27b896fL, 0x80deb1fe3b1696b1L, 0x9bdc06a725c71235L, 0xc19bf174cf692694L,
			0xe49b69c19ef14ad2L, 0xefbe4786384f25e3L, 0x0fc19dc68b8cd5b5L, 0x240ca1cc77ac9c65L,
			0x2de92c6f592b0275L, 0x4a7484aa6ea6e483L, 0x5cb0a9dcbd41fbd4L, 0x76f988da831153b5L,
			0x983e5152ee66dfabL, 0xa831c66d2db43210L, 0xb00327c898fb213fL, 0xbf597fc7beef0ee4L,
			0xc6e00bf33da88fc2L, 0xd5a79147930aa725L, 0x06ca6351e003826fL, 0x142929670a0e6e70L,
			0x27b70a8546d22ffcL, 0x2e1b21385c26c926L, 0x4d2c6dfc5ac42aedL, 0x53380d139d95b3dfL,
			0x650a73548baf63deL, 0x766a0abb3c77b2a8L, 0x81c2c92e47edaee6L, 0x92722c851482353bL,
			0xa2bfe8a14cf10364L, 0xa81a664bbc423001L, 0xc24b8b70d0f89791L, 0xc76c51a30654be30L,
			0xd192e819d6ef5218L, 0xd69906245565a910L, 0xf40e35855771202aL, 0x106aa07032bbd1b8L,
			0x19a4c116b8d2d0c8L, 0x1e376c085141ab53L, 0x2748774cdf8eeb99L, 0x34b0bcb5e19b48a8L,
			0x391c0cb3c5c95a63L, 0x4ed8aa4ae3418acbL, 0x5b9cca4f7763e373L, 0x682e6ff3d6b2b8a3L,
			0x748f82ee5defb2fcL, 0x78a5636f43172f60L, 0x84c87814a1f0ab72L, 0x8cc702081a6439ecL,
			0x90befffa23631e28L, 0xa4506cebde82bde9L, 0xbef9a3f7b2c67915L, 0xc67178f2e372532bL,
			0xca273eceea26619cL, 0xd186b8c721c0c207L, 0xeada7dd6cde0eb1eL, 0xf57d4f7fee6ed178L,
			0x06f067aa72176fbaL, 0x0a637dc5a2c898a6L, 0x113f9804bef90daeL, 0x1b710b35131c471bL,
			0x28db77f523047d84L, 0x32caab7b40c72493L, 0x3c9ebe0a15c9bebcL, 0x431d67c49c100d4cL,
			0x4cc5d4becb3e42b6L, 0x597f299cfc657e2aL, 0x5fcb6fab3ad6faecL, 0x6c44198c4a475817L
		};
	
	protected final long[] counters = new long[2];	
	protected final long[] STATE = new long[8];

	protected final LongCounter counter = new LongCounter(counters);
	
	public SHA2_Base_64(int out) 
	{
		super(128, out);
		reset();
	}
	
	protected abstract void complete(byte[] out, int start, int max);
	
	private static long F1(long A, long B, long C)
	{
		return ((A & B) ^ ((~A) & C));
	}

	private static long F2(long A, long B, long C)
	{
	    return ((A & B) ^ (A & C) ^ (B & C));
	}

	private static long M1(long A)
	{
	    return Bits.rotateRight(A, 28) ^ Bits.rotateLeft(A, 30) ^ Bits.rotateLeft(A, 25);
	}

	private static long M2(long A)
	{
	    return Bits.rotateRight(A, 14) ^ Bits.rotateRight(A, 18) ^ Bits.rotateLeft(A, 23);
	}

	private static long E1(long A)
	{
	    return Bits.rotateRight(A, 1) ^ Bits.rotateRight(A, 8) ^ (A >>> 7);
	}

	private static long E2(long A)
	{
	    return Bits.rotateRight(A, 19) ^ Bits.rotateLeft(A, 3) ^ (A >>> 6);
	}

	public void processNext(byte[] bytes, int offset)
	{
		counter.add(128);
		long[] IN = new long[80];
		Bits.BigEndian.bytesToLongs(bytes, offset, IN, 0, 16);
		for(int i = 16; i < 80; i++)
			IN[i] = E2(IN[i - 2]) + IN[i - 7] + E1(IN[i - 15]) + IN[i - 16];
		
		long A = STATE[0],
			 B = STATE[1],
			 C = STATE[2],
			 D = STATE[3],
			 E = STATE[4],
			 F = STATE[5],
			 G = STATE[6],
			 H = STATE[7];
		
		int i = 0;
		while(i < 80)
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

	public void finalize(byte[] remaining, int pos, byte[] out, int start, int max)
	{
		try {
			byte[] bytes = new byte[128];
			System.arraycopy(remaining, 0, bytes, 0, pos);
			bytes[pos] = (byte) 0x80;
			counter.add(pos);
			if(pos >= 112) {
				processNext(bytes, 0);
				counter.add(-128);
				Arrays.fill(bytes, (byte) 0);
			}
			Bits.BigEndian.longsToBytes(counters, 0, bytes, 112, 2);
			processNext(bytes, 0);
			complete(out, start, max);
		} finally {
			reset();
		}
	}
	
	public SHA2_64State getState()
	{
		return new SHA2_64State(this);
	}

	public void updateState(SHA2_64State state)
	{
		state.update(this);
	}

	public void loadCustom(SHA2_64State state)
	{
		System.arraycopy(state.counters, 0, this.counters, 0, 2);
		System.arraycopy(state.state, 0, this.STATE, 0, 8);
	}
	
	public static final SHA2_64StateFactory sfactory = new SHA2_64StateFactory();
	
	protected static final class SHA2_64State extends MerkleState<SHA2_64State, SHA2_Base_64<? extends SHA2_Base_64<?>>>
	{

		protected long[] state;
		protected long[] counters;
		
		public SHA2_64State(SHA2_Base_64<? extends SHA2_Base_64<?>> hash) 
		{
			super(hash);
		}
		
		public SHA2_64State(byte[] bytes, int pos)
		{
			super(bytes, pos);
		}

		public Factory<SHA2_64State> factory()
		{
			return sfactory;
		}
		
		public int stateID()
		{
			return IState.SHA2_64;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.SHA2_64STATE;
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

		protected void addCustom(SHA2_Base_64<? extends SHA2_Base_64<?>> hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			counters = ArrayUtil.copy(hash.counters);
		}

		protected void updateCustom(SHA2_Base_64<? extends SHA2_Base_64<?>> hash)
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

		protected boolean compareCustom(SHA2_64State state)
		{	
			return ArrayUtil.equals(counters, state.counters) && ArrayUtil.equals(this.state, state.state);
		}

		protected int customSize()
		{
			return 80;
		}
		
	}
	
	protected static final class SHA2_64StateFactory extends MerkleStateFactory<SHA2_64State, SHA2_Base_64<? extends SHA2_Base_64<?>>>
	{

		protected SHA2_64StateFactory() 
		{
			super(SHA2_64State.class, 128);
		}

		protected SHA2_64State construct(byte[] bytes, int pos)
		{
			return new SHA2_64State(bytes, pos);
		}
		
	}

}

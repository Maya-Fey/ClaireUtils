package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.hash.primitive.MD5.MD5State;
import claire.util.crypto.hash.primitive.MerkleHash.MerkleState;
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

public class MD5 
	   extends MerkleHash<MD5State, MD5> {
	
	private static final int[] PBOX = 
		{
			0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee,
			0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501,
			0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be,
			0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821,
			0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa,
			0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8,
			0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed,
			0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a,
			0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c,
			0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70,
			0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05,
			0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665,
			0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039,
			0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1,
			0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1,
			0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391
		};
	
	private static final int[] PERMUTE =
		{
			0, 1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
			1, 6, 11,  0,  5, 10, 15,  4,  9, 14,  3,  8, 13,  2,  7, 12,
			5, 8, 11, 14,  1,  4,  7, 10, 13,  0,  3,  6,  9, 12, 15,  2,
			0, 7, 14,  5, 12,  3, 10,  1,  8, 15,  6, 13,  4, 11,  2,  9
		};
	
	private final int[] STATE = new int[4];
	private long total = 0;

	public MD5() 
	{
		super(64, 16);
		reset();	
	}	
	
	private static int F1(int u, int v, int w)
	{
		return (u & v) | (~u & w);
	}

	private static int F2(int u, int v, int w)
	{
		return (u & w) | (v & ~w);
	}

	private static int F3(int u, int v, int w)    
	{     
		return u ^ v ^ w;
	}

	private static int F4(int u, int v, int w)
	{      
	    return v ^ (u | ~w);
	}
	
	public void processNext(byte[] bytes, int offset)
	{
		total += 64;
		int[] IN = new int[16];
		Bits.bytesToInts(bytes, offset, IN, 0);
		int A = STATE[0], 
			B = STATE[1], 
			C = STATE[2], 
			D = STATE[3];
		
		int i = 0;
		for(; i < 16;) 
		{
			A = Bits.rotateLeft(A + F1(B, C, D) + IN[PERMUTE[i]] + PBOX[i++],  7) + B;
			D = Bits.rotateLeft(D + F1(A, B, C) + IN[PERMUTE[i]] + PBOX[i++], 12) + A;
			C = Bits.rotateLeft(C + F1(D, A, B) + IN[PERMUTE[i]] + PBOX[i++], 17) + D;
			B = Bits.rotateLeft(B + F1(C, D, A) + IN[PERMUTE[i]] + PBOX[i++], 22) + C;
		}
		for(; i < 32;)
		{
			A = Bits.rotateLeft(A + F2(B, C, D) + IN[PERMUTE[i]] + PBOX[i++],  5) + B;
			D = Bits.rotateLeft(D + F2(A, B, C) + IN[PERMUTE[i]] + PBOX[i++],  9) + A;
			C = Bits.rotateLeft(C + F2(D, A, B) + IN[PERMUTE[i]] + PBOX[i++], 14) + D;
			B = Bits.rotateLeft(B + F2(C, D, A) + IN[PERMUTE[i]] + PBOX[i++], 20) + C;
		}
		for(; i < 48;)
		{
			A = Bits.rotateLeft(A + F3(B, C, D) + IN[PERMUTE[i]] + PBOX[i++],  4) + B;
			D = Bits.rotateLeft(D + F3(A, B, C) + IN[PERMUTE[i]] + PBOX[i++], 11) + A;
			C = Bits.rotateLeft(C + F3(D, A, B) + IN[PERMUTE[i]] + PBOX[i++], 16) + D;
			B = Bits.rotateLeft(B + F3(C, D, A) + IN[PERMUTE[i]] + PBOX[i++], 23) + C;
		}
		for(; i < 64;)
		{
			A = Bits.rotateLeft(A + F4(B, C, D) + IN[PERMUTE[i]] + PBOX[i++],  6) + B;
			D = Bits.rotateLeft(D + F4(A, B, C) + IN[PERMUTE[i]] + PBOX[i++], 10) + A;
			C = Bits.rotateLeft(C + F4(D, A, B) + IN[PERMUTE[i]] + PBOX[i++], 15) + D;
			B = Bits.rotateLeft(B + F4(C, D, A) + IN[PERMUTE[i]] + PBOX[i++], 21) + C;
		}
		
		STATE[0] += A;
		STATE[1] += B;
		STATE[2] += C;
		STATE[3] += D;
	}
	
	public void reset()
	{
		super.reset();
		total = 0;
		STATE[0] = 0x67452301;
		STATE[1] = 0xefcdab89;
		STATE[2] = 0x98badcfe;
		STATE[3] = 0x10325476;
	}

	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		byte[] bytes = new byte[64];
		System.arraycopy(remaining, 0, bytes, 0, pos);
		bytes[pos] = (byte) 0x80;
		total += pos;
		if(pos >= 56) {
			processNext(bytes, 0);
			total -= 64;
			Arrays.fill(bytes, (byte) 0);
		}
		Bits.longToBytes(total << 3, bytes, 56);
		processNext(bytes, 0);
		Bits.intsToBytes(STATE, 0, out, start);
		reset();
	}
	
	public MD5State getState()
	{
		return new MD5State(this);
	}

	public void updateState(MD5State state)
	{
		state.update(this);
	}

	public void loadCustom(MD5State state)
	{
		System.arraycopy(state.state, 0, this.STATE, 0, 4);
		this.total = state.total;
	}
	
	public static final MD5StateFactory sfactory = new MD5StateFactory();
	
	protected static final class MD5State extends MerkleState<MD5State, MD5>
	{
		protected int[] state;
		protected long total;
		
		public MD5State(MD5 md4)
		{
			super(md4);
		}
		
		public MD5State(byte[] bytes, int pos)
		{
			super(bytes, pos);
		}

		public Factory<MD5State> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.MD5STATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeInts(state);
			os.writeLong(total);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.intsToBytes(state, 0, bytes, start, 4); start += 16;
			Bits.longToBytes(total, bytes, start);
		}

		protected void addCustom(IIncomingStream os) throws IOException
		{
			state = os.readInts(4);
			total = os.readLong();
		}

		protected void addCustom(byte[] bytes, int start)
		{
			state = new int[4];
			Bits.bytesToInts(bytes, start, state, 0, 4); start += 16;
			total = Bits.longFromBytes(bytes, start);
		}

		protected void addCustom(MD5 hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			total = hash.total;
		}

		protected void updateCustom(MD5 hash)
		{
			if(state == null)
				state = ArrayUtil.copy(hash.STATE);
			else
				System.arraycopy(hash.STATE, 0, state, 0, 4);
			this.total = hash.total;
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, 0);
			state = null;
			total = 0;
		}

		protected boolean compareCustom(MD5State state)
		{
			return ArrayUtil.equals(state.state, this.state) && this.total == state.total;
		}
		
		protected int customSize()
		{
			return 24;
		}
		
	}
	
	protected static final class MD5StateFactory extends MerkleStateFactory<MD5State, MD5>
	{

		protected MD5StateFactory()
		{
			super(MD5State.class, 64);
		}

		protected MD5State construct(byte[] bytes, int pos)
		{
			return new MD5State(bytes, pos);
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
		MD5 blake = new MD5();
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}
	
	public HashFactory<MD5> factory()
	{
		return factory;
	}
	
	public static final MD5Factory factory = new MD5Factory();
	
	public static final class MD5Factory extends HashFactory<MD5>
	{

		public MD5 build(char[] params, char sep)
		{
			return new MD5();
		}
		
	}

}

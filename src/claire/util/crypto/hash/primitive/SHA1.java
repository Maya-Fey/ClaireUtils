package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.hash.primitive.SHA1.SHA1State;
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

public class SHA1 
	   extends MerkleHash<SHA1State, SHA1> {
	
	private static final int A1 = 0x5a827999;
    private static final int A2 = 0x6ed9eba1;
    private static final int A3 = 0x8f1bbcdc;
    private static final int A4 = 0xca62c1d6;
	
	private final int[] STATE = new int[5];
	
	private long total;

	public SHA1() 
	{
		super(64, 20);
		reset();
	}
	
	public int hashID()
	{
		return Hash.SHA1;
	}
	
	public void reset()
	{
		super.reset();
		STATE[0] = 0x67452301;
		STATE[1] = 0xefcdab89;
		STATE[2] = 0x98badcfe;
		STATE[3] = 0x10325476;
		STATE[4] = 0xc3d2e1f0;
		total = 0;
	}
	
	private static int F1(int u, int v, int w)
	{
	    return ((u & v) | ((~u) & w));
	}

	private static int F2(int u, int v, int w)
	{
	    return (u ^ v ^ w);
	}

	private static int F3(int u, int v, int w)
	{
	    return ((u & v) | (u & w) | (v & w));
	}

	public void processNext(byte[] bytes, int offset)
	{
		total += 64;
		int[] IN = new int[80];
		Bits.BigEndian.bytesToInts(bytes, offset, IN, 0, 16);
		for(int i = 16; i < 80; i++)
		{
			int temp = IN[i - 3] ^ IN[i - 8] ^ IN[i - 14] ^ IN[i - 16];
			IN[i] = Bits.rotateLeft(temp, 1);
		}
		int A = STATE[0],
			B = STATE[1],
			C = STATE[2],
			D = STATE[3],
			E = STATE[4];
		
		int i = 0;
		for(; i < 20;)
		{
			E += Bits.rotateLeft(A, 5) + F1(B, C, D) + IN[i++] + A1;
            B = Bits.rotateRight(B, 2);       
            D += Bits.rotateLeft(E, 5) + F1(A, B, C) + IN[i++] + A1;
            A = Bits.rotateRight(A, 2);       
            C += Bits.rotateLeft(D, 5) + F1(E, A, B) + IN[i++] + A1;
            E = Bits.rotateRight(E, 2);       
            B += Bits.rotateLeft(C, 5) + F1(D, E, A) + IN[i++] + A1;
            D = Bits.rotateRight(D, 2);
            A += Bits.rotateLeft(B, 5) + F1(C, D, E) + IN[i++] + A1;
            C = Bits.rotateRight(C, 2);
		}
		for(; i < 40;)
		{
			E += Bits.rotateLeft(A, 5) + F2(B, C, D) + IN[i++] + A2;
            B = Bits.rotateRight(B, 2);       
            D += Bits.rotateLeft(E, 5) + F2(A, B, C) + IN[i++] + A2;
            A = Bits.rotateRight(A, 2);       
            C += Bits.rotateLeft(D, 5) + F2(E, A, B) + IN[i++] + A2;
            E = Bits.rotateRight(E, 2);       
            B += Bits.rotateLeft(C, 5) + F2(D, E, A) + IN[i++] + A2;
            D = Bits.rotateRight(D, 2);
            A += Bits.rotateLeft(B, 5) + F2(C, D, E) + IN[i++] + A2;
            C = Bits.rotateRight(C, 2);
		}
		for(; i < 60;)
		{
			E += Bits.rotateLeft(A, 5) + F3(B, C, D) + IN[i++] + A3;
            B = Bits.rotateRight(B, 2);       
            D += Bits.rotateLeft(E, 5) + F3(A, B, C) + IN[i++] + A3;
            A = Bits.rotateRight(A, 2);       
            C += Bits.rotateLeft(D, 5) + F3(E, A, B) + IN[i++] + A3;
            E = Bits.rotateRight(E, 2);       
            B += Bits.rotateLeft(C, 5) + F3(D, E, A) + IN[i++] + A3;
            D = Bits.rotateRight(D, 2);
            A += Bits.rotateLeft(B, 5) + F3(C, D, E) + IN[i++] + A3;
            C = Bits.rotateRight(C, 2);
		}
		for(; i < 80;)
		{
			E += Bits.rotateLeft(A, 5) + F2(B, C, D) + IN[i++] + A4;
            B = Bits.rotateRight(B, 2);       
            D += Bits.rotateLeft(E, 5) + F2(A, B, C) + IN[i++] + A4;
            A = Bits.rotateRight(A, 2);       
            C += Bits.rotateLeft(D, 5) + F2(E, A, B) + IN[i++] + A4;
            E = Bits.rotateRight(E, 2);       
            B += Bits.rotateLeft(C, 5) + F2(D, E, A) + IN[i++] + A4;
            D = Bits.rotateRight(D, 2);
            A += Bits.rotateLeft(B, 5) + F2(C, D, E) + IN[i++] + A4;
            C = Bits.rotateRight(C, 2);
		}
		
		STATE[0] += A;
		STATE[1] += B;
		STATE[2] += C;
		STATE[3] += D;
		STATE[4] += E;
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
		Bits.BigEndian.longToBytes(total << 3, bytes, 56);
		processNext(bytes, 0);
		Bits.BigEndian.intsToBytes(STATE, 0, out, start);
		reset();
	}
	
	public SHA1State getState()
	{
		return new SHA1State(this);
	}

	public void updateState(SHA1State state)
	{
		state.update(this);
	}

	public void loadCustom(SHA1State state)
	{
		System.arraycopy(state.state, 0, this.STATE, 0, 5);
		this.total = state.total;
	}
	
	public static final SHA1StateFactory sfactory = new SHA1StateFactory();
	
	protected static final class SHA1State extends MerkleState<SHA1State, SHA1>
	{
		protected int[] state;
		protected long total;
		
		public SHA1State(SHA1 md5)
		{
			super(md5);
		}
		
		public SHA1State(byte[] bytes, int pos)
		{
			super(bytes, pos);
		}

		public Factory<SHA1State> factory()
		{
			return sfactory;
		}
		
		public int stateID()
		{
			return IState.SHA1;
		}


		public int NAMESPACE()
		{
			return _NAMESPACE.SHA1STATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeInts(state);
			os.writeLong(total);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.intsToBytes(state, 0, bytes, start, 5); start += 20;
			Bits.longToBytes(total, bytes, start);
		}

		protected void addCustom(IIncomingStream os) throws IOException
		{
			state = os.readInts(5);
			total = os.readLong();
		}

		protected void addCustom(byte[] bytes, int start)
		{
			state = new int[5];
			Bits.bytesToInts(bytes, start, state, 0, 5); start += 20;
			total = Bits.longFromBytes(bytes, start);
		}

		protected void addCustom(SHA1 hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			total = hash.total;
		}

		protected void updateCustom(SHA1 hash)
		{
			if(state == null)
				state = ArrayUtil.copy(hash.STATE);
			else
				System.arraycopy(hash.STATE, 0, state, 0, 5);
			this.total = hash.total;
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, 0);
			state = null;
			total = 0;
		}

		protected boolean compareCustom(SHA1State state)
		{
			return ArrayUtil.equals(state.state, this.state) && this.total == state.total;
		}
		
		protected int customSize()
		{
			return 28;
		}
		
	}
	
	protected static final class SHA1StateFactory extends MerkleStateFactory<SHA1State, SHA1>
	{

		protected SHA1StateFactory()
		{
			super(SHA1State.class, 64);
		}

		protected SHA1State construct(byte[] bytes, int pos)
		{
			return new SHA1State(bytes, pos);
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
		SHA1 blake = new SHA1();
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}
	
	public HashFactory<SHA1> factory()
	{
		return factory;
	}
	
	public static final SHA1Factory factory = new SHA1Factory();
	
	public static final class SHA1Factory extends HashFactory<SHA1>
	{

		public SHA1 build(CryptoString str)
		{
			return new SHA1();
		}
		
	}
	
}

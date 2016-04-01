package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.MD4.MD4State;
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

public class MD4 
	   extends MerkleHash<MD4State, MD4> {
	
	public MD4() {
		super(64, 16);
		reset();
	}

	private static final int[] CONSTANTS = {
		 0x67452301,
	     0xEFCDAB89,
	     0x98BADCFE,
	     0x10325476
	};
	
	protected final int[] STATE = new int[4];
	
	protected long count;

	private final int[] SCRATCHPAD = new int[16];
	
	public void reset()
	{
		super.reset();
		System.arraycopy(CONSTANTS, 0, STATE, 0, 4);
		count = 0;
	}
	
	public void processNext(byte[] bytes, int pos) 
	{
		count++;
		Bits.bytesToInts(bytes, pos, SCRATCHPAD, 0, 16);
		int a = STATE[0],
			b = STATE[1],
			c = STATE[2],
			d = STATE[3];
		
		a = F1(a, b, c, d, SCRATCHPAD[ 0],  3);
		d = F1(d, a, b, c, SCRATCHPAD[ 1],  7);
		c = F1(c, d, a, b, SCRATCHPAD[ 2], 11);
		b = F1(b, c, d, a, SCRATCHPAD[ 3], 19);
		a = F1(a, b, c, d, SCRATCHPAD[ 4],  3);
		d = F1(d, a, b, c, SCRATCHPAD[ 5],  7);
		c = F1(c, d, a, b, SCRATCHPAD[ 6], 11);
		b = F1(b, c, d, a, SCRATCHPAD[ 7], 19);
		a = F1(a, b, c, d, SCRATCHPAD[ 8],  3);
		d = F1(d, a, b, c, SCRATCHPAD[ 9],  7);
		c = F1(c, d, a, b, SCRATCHPAD[10], 11);
		b = F1(b, c, d, a, SCRATCHPAD[11], 19);
		a = F1(a, b, c, d, SCRATCHPAD[12],  3);
		d = F1(d, a, b, c, SCRATCHPAD[13],  7);
		c = F1(c, d, a, b, SCRATCHPAD[14], 11);
		b = F1(b, c, d, a, SCRATCHPAD[15], 19);

		a = F2(a, b, c, d, SCRATCHPAD[ 0] + 0x5a827999,  3);
		d = F2(d, a, b, c, SCRATCHPAD[ 4] + 0x5a827999,  5);
		c = F2(c, d, a, b, SCRATCHPAD[ 8] + 0x5a827999,  9);
		b = F2(b, c, d, a, SCRATCHPAD[12] + 0x5a827999, 13);
		a = F2(a, b, c, d, SCRATCHPAD[ 1] + 0x5a827999,  3);
		d = F2(d, a, b, c, SCRATCHPAD[ 5] + 0x5a827999,  5);
		c = F2(c, d, a, b, SCRATCHPAD[ 9] + 0x5a827999,  9);
		b = F2(b, c, d, a, SCRATCHPAD[13] + 0x5a827999, 13);
		a = F2(a, b, c, d, SCRATCHPAD[ 2] + 0x5a827999,  3);
		d = F2(d, a, b, c, SCRATCHPAD[ 6] + 0x5a827999,  5);
		c = F2(c, d, a, b, SCRATCHPAD[10] + 0x5a827999,  9);
		b = F2(b, c, d, a, SCRATCHPAD[14] + 0x5a827999, 13);
		a = F2(a, b, c, d, SCRATCHPAD[ 3] + 0x5a827999,  3);
		d = F2(d, a, b, c, SCRATCHPAD[ 7] + 0x5a827999,  5);
		c = F2(c, d, a, b, SCRATCHPAD[11] + 0x5a827999,  9);
		b = F2(b, c, d, a, SCRATCHPAD[15] + 0x5a827999, 13);

		a = F3(a, b, c, d, SCRATCHPAD[ 0] + 0x6ed9eba1,  3);
		d = F3(d, a, b, c, SCRATCHPAD[ 8] + 0x6ed9eba1,  9);
		c = F3(c, d, a, b, SCRATCHPAD[ 4] + 0x6ed9eba1, 11);
		b = F3(b, c, d, a, SCRATCHPAD[12] + 0x6ed9eba1, 15);
		a = F3(a, b, c, d, SCRATCHPAD[ 2] + 0x6ed9eba1,  3);
		d = F3(d, a, b, c, SCRATCHPAD[10] + 0x6ed9eba1,  9);
		c = F3(c, d, a, b, SCRATCHPAD[ 6] + 0x6ed9eba1, 11);
		b = F3(b, c, d, a, SCRATCHPAD[14] + 0x6ed9eba1, 15);
		a = F3(a, b, c, d, SCRATCHPAD[ 1] + 0x6ed9eba1,  3);
		d = F3(d, a, b, c, SCRATCHPAD[ 9] + 0x6ed9eba1,  9);
		c = F3(c, d, a, b, SCRATCHPAD[ 5] + 0x6ed9eba1, 11);
		b = F3(b, c, d, a, SCRATCHPAD[13] + 0x6ed9eba1, 15);
		a = F3(a, b, c, d, SCRATCHPAD[ 3] + 0x6ed9eba1,  3);
		d = F3(d, a, b, c, SCRATCHPAD[11] + 0x6ed9eba1,  9);
		c = F3(c, d, a, b, SCRATCHPAD[ 7] + 0x6ed9eba1, 11);
		b = F3(b, c, d, a, SCRATCHPAD[15] + 0x6ed9eba1, 15);
		
		STATE[0] += a;
		STATE[1] += b;
		STATE[2] += c;
		STATE[3] += d;
	}
	
	private static int F1(int a, int b, int c, int d, int x, int s) 
	{
        int t = a + ((b & c) | (~b & d)) + x;
        return t << s | t >>> (32 - s);
    }
	
    private static int F2(int a, int b, int c, int d, int x, int s) 
    {
        int t = a + ((b & (c | d)) | (c & d)) + x;
        return t << s | t >>> (32 - s);
    }
    
    private static int F3(int a, int b, int c, int d, int x, int s) 
    {    	
        int t = a + (b ^ c ^ d) + x;
        return t << s | t >>> (32 - s);
    }

	public void finalize(byte[] remaining, int len, byte[] out, int start)
	{
		byte[] BUFFER = new byte[64];
		System.arraycopy(remaining, 0, BUFFER, 0, len);
		BUFFER[len] = (byte) 0x80;
		long total = count * 64; total += len;
		if(len >= 56) {
			processNext(BUFFER, 0);
			Arrays.fill(BUFFER, (byte) 0);
		}
		for(int i = 0; i < 8; i++)
            BUFFER[56 + i] = (byte)((total * 8) >>> (8 * i));
		processNext(BUFFER, 0);
		count = 0;
		Bits.intsToBytes(STATE, 0, out, start);
		reset();
	}
	
	public MD4State getState()
	{
		return new MD4State(this);
	}

	public void updateState(MD4State state)
	{
		state.update(this);
	}

	public void loadCustom(MD4State state)
	{
		System.arraycopy(state.state, 0, this.STATE, 0, 4);
		this.count = state.count;
	}
	
	public static final MD4StateFactory sfactory = new MD4StateFactory();
	
	protected static final class MD4State extends MerkleState<MD4State, MD4>
	{
		protected int[] state;
		protected long count;
		
		public MD4State(MD4 md4)
		{
			super(md4);
		}
		
		public MD4State(byte[] bytes, int pos)
		{
			super(bytes, pos);
		}

		public Factory<MD4State> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.MD4STATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeInts(state);
			os.writeLong(count);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.intsToBytes(state, 0, bytes, start, 4); start += 16;
			Bits.longToBytes(count, bytes, start);
		}

		protected void addCustom(IIncomingStream os) throws IOException
		{
			state = os.readInts(4);
			count = os.readLong();
		}

		protected void addCustom(byte[] bytes, int start)
		{
			state = new int[4];
			Bits.bytesToInts(bytes, start, state, 0, 4); start += 16;
			count = Bits.longFromBytes(bytes, start);
		}

		protected void addCustom(MD4 hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			count = hash.count;
		}

		protected void updateCustom(MD4 hash)
		{
			if(state == null)
				state = ArrayUtil.copy(hash.STATE);
			else
				System.arraycopy(hash.STATE, 0, state, 0, 4);
			this.count = hash.count;
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, 0);
			state = null;
			count = 0;
		}

		protected boolean compareCustom(MD4State state)
		{
			return ArrayUtil.equals(state.state, this.state) && this.count == state.count;
		}
		
		protected int customSize()
		{
			return 24;
		}
		
	}
	
	protected static final class MD4StateFactory extends MerkleStateFactory<MD4State, MD4>
	{

		protected MD4StateFactory()
		{
			super(MD4State.class, 64);
		}

		protected MD4State construct(byte[] bytes, int pos)
		{
			return new MD4State(bytes, pos);
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
		MD4 blake = new MD4();
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}
	
    
}

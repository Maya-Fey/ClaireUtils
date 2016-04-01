package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.MD2.MD2State;
import claire.util.crypto.hash.primitive.MerkleHash.MerkleState;
import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class MD2 
	   extends MerkleHash<MD2State, MD2> {
	
	public MD2() {
		super(16, 16);
	}

	private static final byte[] CONSTANTS = {
        41,   46,   67,  -55,  -94,  -40,  124,   1,
        61,   54,   84,  -95,  -20,  -16,    6,  19,
        98,  -89,    5,  -13,  -64,  -57,  115, -116,
      -104, -109,   43,  -39,  -68,   76, -126,  -54,
        30, -101,   87,   60,   -3,  -44,  -32,   22,
       103,   66,  111,   24, -118,   23,  -27,   18,
       -66,   78,  -60,  -42,  -38,  -98,  -34,   73,
       -96,   -5,  -11, -114,  -69,   47,  -18,  122,
       -87,  104,  121, -111,   21,  -78,    7,   63,
      -108,  -62,   16, -119,   11,   34,   95,   33,
      -128,  127,   93, -102,   90, -112,   50,   39,
        53,   62,  -52,  -25,  -65,   -9, -105,    3,
        -1,   25,   48,  -77,   72,  -91,  -75,  -47,
       -41,   94, -110,   42,  -84,   86,  -86,  -58,
        79,  -72,   56,  -46, -106,  -92,  125,  -74,
       118,   -4,  107,  -30, -100,  116,    4,  -15,
        69,  -99,  112,   89,  100,  113, -121,   32,
      -122,   91,  -49,  101,  -26,   45,  -88,    2,
        27,   96,   37,  -83,  -82,  -80,  -71,  -10,
        28,   70,   97,  105,   52,   64,  126,   15,
        85,   71,  -93,   35,  -35,   81,  -81,   58,
       -61,   92,   -7,  -50,  -70,  -59,  -22,   38,
        44,   83,   13,  110, -123,   40, -124,    9,
       -45,  -33,  -51,  -12,   65, -127,   77,   82,
       106,  -36,   55,  -56,  108,  -63,  -85,   -6,
        36,  -31,  123,    8,   12,  -67,  -79,   74,
       120, -120, -107, -117,  -29,   99,  -24,  109,
       -23,  -53,  -43,   -2,   59,    0,   29,   57,
       -14,  -17,  -73,   14,  102,   88,  -48,  -28,
       -90,  119,  114,   -8,  -21,  117,   75,   10,
        49,   68,   80,  -76, -113,  -19,   31,   26,
       -37, -103, -115,   51, - 97,   17, -125,   20 
    };
	
	protected final byte[] checksum = new byte[16];
	protected final byte[] state = new byte[48];
	
	public void reset()
	{
		super.reset();
		Arrays.fill(checksum, (byte) 0);
		Arrays.fill(state, (byte) 0);
	}
	
	public void processNext(byte[] bytes, int pos)
	{
		for(int i = 0; i < 16; i++) {
			state[i + 16] = bytes[i + pos];
			state[i + 32] = (byte) (bytes[i + pos] ^ state[i]);
		}
		
		int i = 0; 
		for(int j = 0; j < 18; j++) { 
		   for (int k = 0; k < 48; k++) { 
		      state[k] ^= CONSTANTS[i]; 
		      i = (int) state[k] & 0xFF;
		      
		   }  
		   i = (i+j) & 0xFF; 
		}  
		
		i = checksum[15];
		for(int j = 0; j < 16; j++) {
			checksum[j] ^= CONSTANTS[(bytes[j + pos] ^ i) & (int) 0xFF];
			i = checksum[j];
		}
	}
	
	public void finalize(byte[] remaining, int pos, byte[] out, final int start)
	{
		System.arraycopy(remaining, 0, out, start, pos);
		for(int i = pos + start; i < (16 + start); i++)
			out[i] = (byte) (16 - pos);
		processNext(out, start);
		System.arraycopy(checksum, 0, out, start, 16);
		processNext(out, start);
		System.arraycopy(state, 0, out, start, 16);
		reset();
	}
	
	public MD2State getState()
	{
		return new MD2State(this);
	}

	public void updateState(MD2State state)
	{
		state.update(this);
	}

	public void loadCustom(MD2State state)
	{
		System.arraycopy(state.state, 0, this.state, 0, 48);
		System.arraycopy(state.checksum, 0, this.checksum, 0, 16);
	}
	
	public static final MD2StateFactory sfactory = new MD2StateFactory();
	
	protected static final class MD2State extends MerkleState<MD2State, MD2>
	{
		protected byte[] state;
		protected byte[] checksum;
		
		public MD2State(byte[] bytes, int pos) 
		{
			super(bytes, pos);
		}
		
		public MD2State(MD2 md2)
		{
			super(md2);
		}

		public Factory<MD2State> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.MD2STATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeBytes(state);
			os.writeBytes(checksum);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			System.arraycopy(state, 0, bytes, start, 48); start += 48;
			System.arraycopy(checksum, 0, bytes, start, 16);
		}

		protected void addCustom(IIncomingStream os) throws IOException
		{
			state = os.readBytes(48);
			checksum = os.readBytes(16);
		}

		protected void addCustom(byte[] bytes, int start)
		{
			state = new byte[48];
			checksum = new byte[16];
			System.arraycopy(bytes, start, state, 0, 48); start += 48;
			System.arraycopy(bytes, start, checksum, 0, 16);
		}

		protected void addCustom(MD2 hash)
		{
			state = ArrayUtil.copy(hash.state);
			checksum = ArrayUtil.copy(hash.checksum);
		}

		protected void updateCustom(MD2 hash)
		{
			if(state == null)
				state = ArrayUtil.copy(hash.state);
			else
				System.arraycopy(hash.state, 0, this.state, 0, 48);
			if(checksum == null)
				checksum = ArrayUtil.copy(hash.checksum);
			else
				System.arraycopy(hash.checksum, 0, this.checksum, 0, 16);
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, (byte) 0);
			Arrays.fill(checksum, (byte) 0);
			state = null;
			checksum = null;
		}

		protected boolean compareCustom(MD2State state)
		{
			return ArrayUtil.equals(this.state, state.state) && ArrayUtil.equals(this.checksum, state.checksum);
		}

		protected int customSize()
		{
			return 64;
		}
		
	}
	
	protected static final class MD2StateFactory extends MerkleStateFactory<MD2State, MD2>
	{

		protected MD2StateFactory() 
		{
			super(MD2State.class, 16);
		}

		protected MD2State construct(byte[] bytes, int pos)
		{
			return new MD2State(bytes, pos);
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
		MD2 blake = new MD2();
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
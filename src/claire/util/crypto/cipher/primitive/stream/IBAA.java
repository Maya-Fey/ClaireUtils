package claire.util.crypto.cipher.primitive.stream;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.cipher.key.stream.KeyIBAA;
import claire.util.crypto.cipher.primitive.stream.IA.StateIA;
import claire.util.crypto.cipher.primitive.stream.IA.StateIAFactory;
import claire.util.crypto.cipher.primitive.stream.IBAA.StateIBAA;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.crypto.IStreamCipher;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class IBAA 
	   implements IStreamCipher<KeyIBAA, StateIBAA> {

	private KeyIBAA key;
	private int[] M;
	private int i = 0, 
			    a = 0,
			    b = 0,
				prev = 0,
				rem = 0;
	
	public IBAA(KeyIBAA key) 
	{
		this.setKey(key);
	}
	
	public KeyIBAA getKey()
	{
		return this.key;
	}

	public void setKey(KeyIBAA t)
	{
		this.key = t;
		if(M == null)
			M = ArrayUtil.copy(t.getInts());
		else
			System.arraycopy(t.getInts(), 0, M, 0, 256);
	}

	public void reset()
	{
		i = a = b = prev = rem = 0;
		if(M == null)
			M = ArrayUtil.copy(key.getInts());
		else
			System.arraycopy(key.getInts(), 0, M, 0, 256);
	}

	public void wipe()
	{
		Arrays.fill(M, 0);
		M = null;
		key = null;
		i = a = b = prev = rem = 0;
	}

	public byte nextByte()
	{
		if(rem == 0) 
			genInt();
		final byte b = (byte) prev;
		prev >>>= 8;
		rem--;
		return b;
	}

	public void fill(final byte[] arr, int start, int len)
	{
		while(rem-- > 0) {
			arr[start++] = (byte) prev;
			prev >>>= 8;
			len--;
		}
		while(len > 4) {
			Bits.intToBytes(nextInt(), arr, start);
			start += 4;
			len -= 4;
		}
		if(len > 0) {
			genInt();
			while(len-- > 0) {
				arr[start++] = (byte) prev;
				prev >>>= 8;
				rem--;
			}
		}
	}
	
	/**
	 * Internal function. This generates the next integer and loads it in
	 * memory to produce bytes at a later time.
	 */
	private void genInt()
	{
		int x = M[i];
		a = Bits.rotateLeft(a, 19) + M[(i + 0x80) & 0xFF];
		int y = M[i] = M[x & 0xFF] + a + b;
		prev = b = M[(y >>> 8) & 0xFF] + x;
		i++;
		i &= 255;
		rem = 4;
	}
	
	/**
	 * Internal function. This generates the next integer and returns it.
	 * Does not alter the integer in memory. 
	 */
	private int nextInt()
	{
		int x = M[i];
		a = Bits.rotateLeft(a, 19) + M[(i + 0x80) & 0xFF];
		int y = M[i] = M[x & 0xFF] + a + b;
		b = M[(y >>> 8) & 0xFF] + x;
		i++;
		i &= 255;
		return b;
	}


	public StateIBAA getState()
	{
		return new StateIBAA(this);
	}

	public void loadState(final StateIBAA state)
	{
		this.i = state.i;
		this.a = state.a;
		this.b = state.b;
		this.prev = state.prev;
		this.rem = state.rem;
		System.arraycopy(state.ints, 0, M, 0, 256);
	}

	public void updateState(final StateIBAA state)
	{
		state.update(this);
	}
	
	public static final StateIBAAFactory sfactory = new StateIBAAFactory();

	protected static final class StateIBAA implements IState<StateIBAA> 
	{

		private int[] ints;
		private int i,
					a,
					b,
					prev,
					rem;
		
		public StateIBAA(final int[] ints, final int i, final int a, final int b, final int prev, final int rem)
		{
			this.ints = ints;
			this.i = i;
			this.a = a;
			this.b = b;
			this.prev = prev;
			this.rem = rem;
		}
		
		public StateIBAA(final IBAA ia)
		{
			this.ints = ArrayUtil.copy(ia.M);
			this.i = ia.i;
			this.a = ia.a;
			this.b = ia.b;
			this.prev = ia.prev;
			this.rem = ia.rem;
		}
		
		/**
		 * Updates this state with the given IBAA object.
		 * <br><br>
		 * Expects:
		 * <ul>
		 * 	<li>A functional IBAA object</li>
		 * </ul>
		 * If the IBAA object has been wiped a NullPointerException will be thrown. 
		 */
		public void update(final IBAA ia)
		{
			System.arraycopy(ia.M, 0, ints, 0, 256);
			this.i = ia.i;
			this.a = ia.a;
			this.b = ia.b;
			this.prev = ia.prev;
			this.rem = ia.rem;
		}
		
		public void export(final IOutgoingStream stream) throws IOException
		{
			stream.writeInts(ints);
			stream.writeInt(i);
			stream.writeInt(a);
			stream.writeInt(b);
			stream.writeInt(prev);
			stream.writeInt(rem);
		}

		public void export(final byte[] bytes, int offset)
		{
			Bits.intsToBytes(ints, 0, bytes, offset, 256); offset += 1024;
			Bits.intToBytes(i, bytes, offset); offset += 4;
			Bits.intToBytes(a, bytes, offset); offset += 4;
			Bits.intToBytes(b, bytes, offset); offset += 4;
			Bits.intToBytes(prev, bytes, offset); offset += 4;
			Bits.intToBytes(rem, bytes, offset); 
		}

		public int exportSize()
		{
			return 1044;
		}

		public Factory<StateIBAA> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.STATEIBAA;
		}

		public boolean sameAs(final StateIBAA obj)
		{
			return ((this.i == obj.i && this.b == obj.b) && (this.prev == obj.prev && this.rem == obj.rem)) && (this.a == obj.a && ArrayUtil.equals(this.ints, obj.ints));
		}

		public void erase()
		{
			i = a = b = prev = rem = 0;
			Arrays.fill(ints, 0);
			ints = null;
		}
		
	}
	
	private static final class StateIBAAFactory extends Factory<StateIBAA>
	{
		public StateIBAAFactory()
		{
			super(StateIBAA.class);
		}

		public StateIBAA resurrect(final byte[] data, int start) throws InstantiationException
		{
			final int[] ints = new int[256];
			Bits.bytesToInts(data, start, ints, 0, 256); start += 1024;
			final int i = Bits.intFromBytes(data, start); start += 4;
			final int a = Bits.intFromBytes(data, start); start += 4;
			final int b = Bits.intFromBytes(data, start); start += 4;
			final int prev = Bits.intFromBytes(data, start); start += 4;
			final int rem = Bits.intFromBytes(data, start); 
			return new StateIBAA(ints, i, a, b, prev, rem);
		}

		public StateIBAA resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			return new StateIBAA(stream.readInts(256), stream.readInt(), stream.readInt(), stream.readInt(), stream.readInt(), stream.readInt());
		}
	}

}

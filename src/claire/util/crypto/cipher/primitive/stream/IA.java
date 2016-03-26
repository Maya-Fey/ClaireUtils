package claire.util.crypto.cipher.primitive.stream;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.cipher.key.stream.KeyIA;
import claire.util.crypto.cipher.key.stream.KeyRC4;
import claire.util.crypto.cipher.primitive.stream.IA.StateIA;
import claire.util.crypto.cipher.primitive.stream.RC4.RC4State;
import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.io.IOUtils;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.crypto.IStreamCipher;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class IA implements IStreamCipher<KeyIA, StateIA> {

	private KeyIA key;
	private int[] M;
	private int i = 0,
				b = 0,
				prev = 0,
				rem = 0;
	
	public IA(KeyIA key) 
	{
		this.setKey(key);
	}
	
	public KeyIA getKey()
	{
		return this.key;
	}

	public void setKey(KeyIA t)
	{
		this.key = t;
		if(M == null)
			M = ArrayUtil.copy(t.getInts());
		else
			System.arraycopy(t.getInts(), 0, M, 0, 256);
	}

	public void reset()
	{
		i = b = prev = rem = 0;
		if(M == null)
			M = ArrayUtil.copy(key.getInts());
		else
			System.arraycopy(key.getInts(), 0, M, 0, 256);
	}

	public void wipe()
	{
		i = prev = 0;
		Arrays.fill(M, 0);
		M = null;
	}

	public byte nextByte()
	{
		if(rem == 0) 
			genInt();
		byte b = (byte) prev;
		prev >>>= 8;
		rem--;
		return b;
	}

	public void fill(byte[] arr, int start, int len)
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
	
	private int nextInt()
	{
		int x = M[i], y;
		M[i] = y = M[x & 0xFF] + b;
		x = b = M[(y >>> 8) & 0xFF] + x;
		i++;
		i &= 255;
		return x;
	}
	
	private void genInt()
	{
		int x = M[i], y;
		M[i] = y = M[x & 0xFF] + b;
		prev = b = M[(y >>> 8) & 0xFF] + x;
		i++;
		i &= 255;
		rem = 4;
	}

	public StateIA getState()
	{
		return new StateIA(this);
	}

	public void loadState(StateIA state)
	{
		this.i = state.i;
		this.b = state.b;
		this.prev = state.prev;
		this.rem = state.rem;
		System.arraycopy(state.ints, 0, M, 0, 256);
	}

	public void updateState(StateIA state)
	{
		state.update(this);
	}
	
	public static final StateIAFactory sfactory = new StateIAFactory();

	protected static final class StateIA implements IState<StateIA> 
	{

		private int[] ints;
		private int i,
					b,
					prev,
					rem;
		
		public StateIA(int[] ints, int i, int b, int prev, int rem)
		{
			this.ints = ints;
			this.i = i;
			this.b = b;
			this.prev = prev;
			this.rem = rem;
		}
		
		public StateIA(IA ia)
		{
			this.ints = ArrayUtil.copy(ia.M);
			this.i = ia.i;
			this.b = ia.b;
			this.prev = ia.prev;
			this.rem = ia.rem;
		}
		
		public void update(IA ia)
		{
			System.arraycopy(ia.M, 0, ints, 0, 256);
			this.i = ia.i;
			this.b = ia.b;
			this.prev = ia.prev;
			this.rem = ia.rem;
		}
		
		public void export(IOutgoingStream stream) throws IOException
		{
			stream.writeInts(ints);
			stream.writeInt(i);
			stream.writeInt(b);
			stream.writeInt(prev);
			stream.writeInt(rem);
		}

		public void export(byte[] bytes, int offset)
		{
			Bits.intsToBytes(ints, 0, bytes, offset, 256); offset += 1024;
			Bits.intToBytes(i, bytes, offset); offset += 4;
			Bits.intToBytes(b, bytes, offset); offset += 4;
			Bits.intToBytes(prev, bytes, offset); offset += 4;
			Bits.intToBytes(rem, bytes, offset); 
		}

		public int exportSize()
		{
			return 1040;
		}

		public Factory<StateIA> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.STATEIA;
		}

		public boolean sameAs(StateIA obj)
		{
			return ((this.i == obj.i && this.b == obj.b) && (this.prev == obj.prev && this.rem == obj.rem)) && ArrayUtil.equals(this.ints, obj.ints);
		}

		public void erase()
		{
			i = b = prev = rem = 0;
			Arrays.fill(ints, 0);
			ints = null;
		}
		
	}
	
	private static final class StateIAFactory extends Factory<StateIA>
	{
		public StateIAFactory()
		{
			super(StateIA.class);
		}

		public StateIA resurrect(byte[] data, int start) throws InstantiationException
		{
			int[] ints = new int[256];
			Bits.bytesToInts(data, start, ints, 0, 256); start += 1024;
			int i = Bits.intFromBytes(data, start); start += 4;
			int b = Bits.intFromBytes(data, start); start += 4;
			int prev = Bits.intFromBytes(data, start); start += 4;
			int rem = Bits.intFromBytes(data, start); 
			return new StateIA(ints, i, b, prev, rem);
		}

		public StateIA resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new StateIA(stream.readInts(256), stream.readInt(), stream.readInt(), stream.readInt(), stream.readInt());
		}
	}
	
	public static final int test()
	{
		int[] bytes = new int[256];
		RandUtils.fillArr(bytes);
		IA rc4 = new IA(new KeyIA(bytes));
		int e = 0;
		e += IStreamCipher.testCipher(rc4);
		return e;
	}
	
	public static final int testState()
	{
		int[] bytes = new int[256];
		RandUtils.fillArr(bytes);
		StateIA state = new StateIA(bytes, RandUtils.dprng.nextIntGood(256), RandUtils.dprng.nextInt(), RandUtils.dprng.nextInt(), RandUtils.dprng.nextInt());
		return IPersistable.test(state);
	}
	
	
}

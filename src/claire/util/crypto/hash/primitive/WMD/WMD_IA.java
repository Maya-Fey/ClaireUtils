package claire.util.crypto.hash.primitive.WMD;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class WMD_IA {

	private int[] key;
	private int[] M;
	private int i = 0,
				b = 0,
				prev = 0;
	
	public WMD_IA(final int[] key) 	
	{
		this.setKey(key);
	}

	public void setKey(final int[] t)
	{
		this.key = t;
		if(M == null)
			M = ArrayUtil.copy(t);
		else
			System.arraycopy(t, 0, M, 0, 256);
	}

	public void reset()
	{
		i = b = prev = 0;
		if(M == null)
			M = ArrayUtil.copy(key);
		else
			System.arraycopy(key, 0, M, 0, 256);
	}

	public void wipe()
	{
		i = prev = 0;
		Arrays.fill(M, 0);
		M = null;
	}
	
	public void update(int[] up)
	{
		int i = prev & 0xFF;
		for(int j : up) {
			M[i++] ^= j;
			i &= 255;
		}
	}

	public int nextInt(int next)
	{
		int x = M[i], y;
		M[i] = y = M[x & 0xFF] + b;
		M[y & 0xFF] ^= next;
		x = b = M[(y >>> 8) & 0xFF] + x;
		i++;
		i &= 255;
		return x;
	}

	public StateIA getState()
	{
		return new StateIA(this);
	}

	public void loadState(final StateIA state)
	{
		this.i = state.i;
		this.b = state.b;
		this.prev = state.prev;
		System.arraycopy(state.ints, 0, M, 0, 256);
	}

	public void updateState(final StateIA state)
	{
		state.update(this);
	}
	
	public static final StateIAFactory sfactory = new StateIAFactory();

	protected static final class StateIA implements IState<StateIA> 
	{

		private int[] ints;
		private int i,
					b,
					prev;
		
		public StateIA(final int[] ints, final int i, final int b, final int prev)
		{
			this.ints = ints;
			this.i = i;
			this.b = b;
			this.prev = prev;
		}
		
		public StateIA(final WMD_IA ia)
		{
			this.ints = ArrayUtil.copy(ia.M);
			this.i = ia.i;
			this.b = ia.b;
			this.prev = ia.prev;
		}
		
		public int stateID()
		{
			return IState.WMDIA;
		}
		
		/**
		 * Updates this state with the given IA object.
		 * <br><br>
		 * Expects:
		 * <ul>
		 * 	<li>A functional IA object</li>
		 * </ul>
		 * If the IA object has been wiped a NullPointerException will be thrown. 
		 */
		public void update(final WMD_IA ia)
		{
			System.arraycopy(ia.M, 0, ints, 0, 256);
			this.i = ia.i;
			this.b = ia.b;
			this.prev = ia.prev;
		}
		
		public void export(final IOutgoingStream stream) throws IOException
		{
			stream.writeInts(ints);
			stream.writeInt(i);
			stream.writeInt(b);
			stream.writeInt(prev);
		}

		public void export(final byte[] bytes, int offset)
		{
			Bits.intsToBytes(ints, 0, bytes, offset, 256); offset += 1024;
			Bits.intToBytes(i, bytes, offset); offset += 4;
			Bits.intToBytes(b, bytes, offset); offset += 4;
			Bits.intToBytes(prev, bytes, offset); offset += 4;
		}

		public int exportSize()
		{
			return 1036;
		}

		public Factory<StateIA> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.STATEIA;
		}

		public boolean sameAs(final StateIA obj)
		{
			return (this.i == obj.i && this.b == obj.b) && (this.prev == obj.prev && ArrayUtil.equals(this.ints, obj.ints));
		}

		public void erase()
		{
			i = b = prev = 0;
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

		public StateIA resurrect(final byte[] data, int start) throws InstantiationException
		{
			final int[] ints = new int[256];
			Bits.bytesToInts(data, start, ints, 0, 256); start += 1024;
			final int i = Bits.intFromBytes(data, start); start += 4;
			final int b = Bits.intFromBytes(data, start); start += 4;
			final int prev = Bits.intFromBytes(data, start); 
			return new StateIA(ints, i, b, prev);
		}

		public StateIA resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			return new StateIA(stream.readInts(256), stream.readInt(), stream.readInt(), stream.readInt());
		}
	}
	
	public static final int testState()
	{
		final int[] bytes = new int[256];
		RandUtils.fillArr(bytes);
		final StateIA state = new StateIA(bytes, RandUtils.dprng.nextIntGood(256), RandUtils.dprng.nextInt(), RandUtils.dprng.nextInt());
		return IPersistable.test(state);
	}
	
	
}

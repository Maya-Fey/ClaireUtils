package claire.util.crypto.hash.primitive;

import java.io.IOException;

import claire.util.crypto.hash.primitive.CRC16.CRC16State;
import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public final class CRC16 
	  	     implements IHash<CRC16State> {

	private static final short[] SBOX =
	{
	     	  0, -16191, -15999,    320, -15615,    960,    640, -15807, 
	     -14847,   1728,   1920, -14527,   1280, -14911, -15231,   1088, 
	     -13311,   3264,   3456, -12991,   3840, -12351, -12671,   3648,   
	       2560, -13631, -13439,   2880, -14079,   2496,   2176, -14271, 
	     -10239,   6336,   6528,  -9919,   6912,  -9279,  -9599,   6720,   
	       7680,  -8511,  -8319,   8000,  -8959,   7616,   7296,  -9151,   
	       5120, -11071, -10879,   5440, -10495,   6080,   5760, -10687,
	     -11775,   4800,   4992, -11455,   4352, -11839, -12159,   4160,
	      -4095,  12480,  12672,  -3775,  13056,  -3135,  -3455,  12864, 
	      13824,  -2367,  -2175,  14144,  -2815,  13760,  13440,  -3007, 
	      15360,   -831,   -639,  15680,   -255,  16320,  16000,   -447, 
	      -1535,  15040,  15232,  -1215,  14592,  -1599,  -1919,  14400, 
	      10240,  -5951,  -5759,  10560,  -5375,  11200,  10880,  -5567, 
	      -4607,  11968,  12160,  -4287,  11520,  -4671,  -4991,  11328, 
	      -7167,   9408,   9600,  -6847,   9984,  -6207,  -6527,   9792, 
	       8704,  -7487,  -7295,   9024,  -7935,   8640,   8320,  -8127,
	     -24575,  24768,  24960, -24255,  25344, -23615, -23935,  25152,
	      26112, -22847, -22655,  26432, -23295,  26048,  25728, -23487,  
	      27648, -21311, -21119,  27968, -20735,  28608,  28288, -20927, 
	     -22015,  27328,  27520, -21695,  26880, -22079, -22399,  26688,
	      30720, -18239, -18047,  31040, -17663,  31680,  31360, -17855,
	     -16895,  32448,  32640, -16575,  32000, -16959, -17279,  31808,
	     -19455,  29888,  30080, -19135,  30464, -18495, -18815,  30272, 
	      29184, -19775, -19583,  29504, -20223,  29120,  28800, -20415, 
	      20480, -28479, -28287,  20800, -27903,  21440,  21120, -28095, 
	     -27135,  22208,  22400, -26815,  21760, -27199, -27519,  21568,
	     -25599,  23744,  23936, -25279,  24320, -24639, -24959,  24128,
	      23040, -25919, -25727,  23360, -26367,  22976,  22656, -26559, 
	     -30719,  18624,  18816, -30399,  19200, -29759, -30079,  19008,
	      19968, -28991, -28799,  20288, -29439,  19904,  19584, -29631, 
	      17408, -31551, -31359,  17728, -30975,  18368,  18048, -31167, 
	     -32255,  17088,  17280, -31935,  16640, -32319, -32639,  16448, 
	};
	
	private short STATE = 0;

	public void add(byte[] bytes, int start, int length)
	{
		final int end = length + start;
		while(start < end) 
			STATE = (short) (((STATE & 0xFFFF) >>> 8) ^ SBOX[((STATE & 0xFFFF) ^ bytes[start++]) & 0xFF]);
	}

	public void finish(byte[] out, int start)
	{
		Bits.BigEndian.shortToBytes((short) STATE, out, start);
		STATE = 0;
	}
	
	public void reset()
	{
		STATE = 0;
	}

	public int outputLength()
	{
		return 2;
	}
	
	public CRC16State getState()
	{
		return new CRC16State(this);
	}

	public void loadState(CRC16State state)
	{
		this.STATE = state.state;
	}
	
	public void updateState(CRC16State state)
	{
		state.state = this.STATE;
	}
	
	public static final CRC16StateFactory sfactory = new CRC16StateFactory();
	
	protected static final class CRC16State implements IState<CRC16State>
	{
		
		private short state;

		public CRC16State(CRC16 crc)
		{
			this.state = crc.STATE;
		}
		
		public CRC16State(short state)
		{
			this.state = state;
		}
		
		public void export(IOutgoingStream stream) throws IOException
		{
			stream.writeShort(state);
		}

		public void export(byte[] bytes, int offset)
		{
			Bits.shortToBytes(state, bytes, offset);
		}

		public int exportSize()
		{
			return 2;
		}

		public Factory<CRC16State> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.CRC16STATE;
		}

		public boolean sameAs(CRC16State obj)
		{
			return this.state == obj.state;
		}

		public void erase()
		{
			this.state = 0;
		}
		
	}
	
	protected static final class CRC16StateFactory extends Factory<CRC16State>
	{

		protected CRC16StateFactory() 
		{
			super(CRC16State.class);
		}
		
		public CRC16State resurrect(byte[] data, int start) throws InstantiationException
		{
			return new CRC16State(Bits.shortFromBytes(data, start));
		}

		public CRC16State resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new CRC16State(stream.readShort());
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
		CRC16 blake = new CRC16();
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		int i = 0;
		i += IPersistable.test(state);
		return i;
	}
	
}

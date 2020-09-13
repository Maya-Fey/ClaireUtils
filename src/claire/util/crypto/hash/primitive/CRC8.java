package claire.util.crypto.hash.primitive;

import java.io.IOException;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.hash.primitive.CRC8.CRC8State;
import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public final class CRC8
	   		 implements IHash<CRC8, CRC8State>,
	   		 			IOutgoingStream {
	
	private static final byte[] SBOX =
	{
		    0,    7,   14,    9,   28,   27,   18,   21,  
		   56,   63,   54,   49,   36,   35,   42,   45,
		  112,  119,  126,  121,  108,  107,   98,  101,
		   72,   79,   70,   65,   84,   83,   90,   93,  
		  -32,  -25,  -18,  -23,   -4,   -5,  -14,  -11, 
		  -40,  -33,  -42,  -47,  -60,  -61,  -54,  -51, 
		 -112, -105,  -98, -103, -116, -117, -126, -123, 
		  -88,  -81,  -90,  -95,  -76,  -77,  -70,  -67,  
		  -57,  -64,  -55,  -50,  -37,  -36,  -43,  -46,  
		   -1,   -8,  -15,  -10,  -29,  -28,  -19,  -22,  
		  -73,  -80,  -71,  -66,  -85,  -84,  -91,  -94, 
		 -113, -120, -127, -122, -109, -108,  -99, -102,  
		   39,   32,   41,   46,   59,   60,   53,   50,   
		   31,   24,   17,   22,    3,    4,   13,   10,   
		   87,   80,   89,   94,   75,   76,   69,   66,  
		  111,  104,   97,  102,  115,  116,  125,  122, 
		 -119, -114, -121, -128, -107, -110, -101, -100,  
		  -79,  -74,  -65,  -72,  -83,  -86,  -93,  -92,  
		   -7,   -2,   -9,  -16,  -27,  -30,  -21,  -20,  
		  -63,  -58,  -49,  -56,  -35,  -38,  -45,  -44, 
		  105,  110,  103,   96,  117,  114,  123,  124,  
		   81,   86,   95,   88,   77,   74,   67,   68,  
		   25,   30,   23,   16,    5,    2,   11,   12,  
		   33,   38,   47,   40,   61,   58,   51,   52,  
		   78,   73,   64,   71,   82,   85,   92,   91,  
		  118,  113,  120,  127,  106,  109,  100,   99, 
		   62,   57,   48,   55,   34,   37,   44,   43,  
		    6,    1,    8,   15,   26,   29,   20,   19,  
		  -82,  -87,  -96,  -89,  -78,  -75,  -68,  -69, 
		 -106, -111, -104,  -97, -118, -115, -124, -125, 
		  -34,  -39,  -48,  -41,  -62,  -59,  -52,  -53,  
		  -26,  -31,  -24,  -17,   -6,   -3,  -12,  -13
	};
	
	public int hashID()
	{
		return Hash.CRC8;
	}
	
	private byte STATE = 0x00;

	public void add(byte[] bytes, int start, int length)
	{
		final int end = length + start;
		while(start < end)
			STATE = SBOX[(bytes[start++] ^ STATE) & 0xFF];
	}
	
	public void close() {}

	public void writeBool(boolean data)
	{
		if(data)
			writeByte((byte) 1);
		else
			writeByte((byte) 0);
	}

	public void writeByte(byte data)
	{
		STATE = SBOX[(data ^ STATE) & 0xFF];
	}

	public void writeShort(short data)
	{
		writeByte((byte) data);
		writeByte((byte) (data >>>  8));
	}

	public void writeChar(char data)
	{
		writeByte((byte) data);
		writeByte((byte) (data >>>  8));
	}

	public void writeInt(int data)
	{
		writeByte((byte) data);
		writeByte((byte) (data >>>= 8));
		writeByte((byte) (data >>>= 8));
		writeByte((byte) (data >>>  8));
	}

	public void writeLong(long data)
	{
		writeByte((byte) data);
		writeByte((byte) (data >>>= 8));
		writeByte((byte) (data >>>= 8));
		writeByte((byte) (data >>>= 8));
		writeByte((byte) (data >>>= 8));
		writeByte((byte) (data >>>= 8));
		writeByte((byte) (data >>>= 8));
		writeByte((byte) (data >>>  8));
	}

	public void writeBools(boolean[] bools, int off, int len)
	{
		while(len-- > 0) {
			boolean data = bools[off++];
			if(data)
				writeByte((byte) 1);
			else
				writeByte((byte) 0);
		}
	}

	public void writeNibbles(byte[] nibbles, int off, int len)
	{
		while(len > 1) {
			writeByte((byte) (((nibbles[off++] & 0xF) << 4) |
							   (nibbles[off++] & 0xF)));
			len -= 2;
		}
	}

	public void writeBytes(byte[] bytes, int off, int len)
	{
		add(bytes, off, len);
	}

	public void writeShorts(short[] shorts, int off, int len)
	{
		while(len-- > 0)
		{
			short data = shorts[off++];
			writeByte((byte) data);
			writeByte((byte) (data >>>  8));
		}
	}

	public void writeChars(char[] charss, int off, int len)
	{
		while(len-- > 0)
		{
			char data = charss[off++];
			writeByte((byte) data);
			writeByte((byte) (data >>>  8));
		}
	}

	public void writeInts(int[] ints, int off, int len)
	{
		while(len-- > 0)
		{
			int data = ints[off++];
			writeByte((byte) data);
			writeByte((byte) (data >>>= 8));
			writeByte((byte) (data >>>= 8));
			writeByte((byte) (data >>>  8));
		}
	}

	public void writeLongs(long[] longs, int off, int len)
	{
		while(len-- > 0)
		{
			long data = longs[off++];
			writeByte((byte) data);
			writeByte((byte) (data >>>= 8));
			writeByte((byte) (data >>>= 8));
			writeByte((byte) (data >>>= 8));
			writeByte((byte) (data >>>= 8));
			writeByte((byte) (data >>>= 8));
			writeByte((byte) (data >>>= 8));
			writeByte((byte) (data >>>  8));
		}
	}
	
	public void persist(IPersistable<?> obj)
	{
		try {
			obj.export(this);
		} catch (IOException e) {}
	}

	public void rewind(long pos) {}
	public void skip(long pos) {}
	public void seek(long pos) {}
	public IIncomingStream getIncoming() { return null; }

	public void finish(byte[] out, int start, int max)
	{
		out[start] = STATE;
		STATE = 0x00;
	}
	
	public byte getCode()
	{
		try {
			return STATE;
		} finally {
			STATE = 0;
		}
	}
	
	public void reset()
	{
		STATE = 0x00;
	}

	public int outputLength()
	{
		return 1;
	}
	
	public int desiredInputLength()
	{
		return 1;
	}
	
	public CRC8State getState()
	{
		return new CRC8State(this);
	}

	public void loadState(CRC8State state)
	{
		this.STATE = state.state;
	}
	
	public void updateState(CRC8State state)
	{
		state.state = this.STATE;
	}
	
	public static final CRC8StateFactory sfactory = new CRC8StateFactory();
	
	protected static final class CRC8State implements IState<CRC8State>
	{
		
		private byte state;

		public CRC8State(CRC8 crc)
		{
			this.state = crc.STATE;
		}
		
		public CRC8State(byte state)
		{
			this.state = state;
		}
		
		public int stateID()
		{
			return IState.CRC8;
		}
		
		public void export(IOutgoingStream stream) throws IOException
		{
			stream.writeByte(state);
		}

		public void export(byte[] bytes, int offset)
		{
			bytes[offset] = this.state;
		}

		public int exportSize()
		{
			return 1;
		}

		public Factory<CRC8State> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.CRC8STATE;
		}

		public boolean sameAs(CRC8State obj)
		{
			return this.state == obj.state;
		}

		public void erase()
		{
			this.state = 0;
		}
		
	}
	
	protected static final class CRC8StateFactory extends Factory<CRC8State>
	{

		protected CRC8StateFactory() 
		{
			super(CRC8State.class);
		}
		
		public CRC8State resurrect(byte[] data, int start) throws InstantiationException
		{
			return new CRC8State(data[start]);
		}

		public CRC8State resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new CRC8State(stream.readByte());
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
		CRC8 blake = new CRC8();
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}

	public String genString(char sep)
	{
		return "";
	}
	
	public HashFactory<CRC8> factory()
	{
		return factory;
	}
	
	public static final CRC8Factory factory = new CRC8Factory();
	
	public static final class CRC8Factory extends HashFactory<CRC8>
	{

		public CRC8 build(CryptoString str)
		{
			return new CRC8();
		}

	}
	
}

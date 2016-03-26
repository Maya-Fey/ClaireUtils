package claire.util.crypto.cipher.primitive.stream;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.cipher.key.stream.KeyRC4;
import claire.util.crypto.cipher.primitive.stream.RC4.RC4State;
import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.crypto.IStreamCipher;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class RC4 
	   implements IStreamCipher<KeyRC4, RC4State> {
	
	private KeyRC4 key;
	private byte[] S = new byte[256];
	
	private int i = 0;
	private int j = 0;

	public RC4(KeyRC4 key) 
	{
		this.setKey(key);
	}

	public KeyRC4 getKey()
	{
		return this.key;
	}

	public void setKey(KeyRC4 key)
	{
		this.key = key;
		byte[] bytes = key.getBytes();
		for(int i = 1; i < 256; i++)
			S[i] = (byte) i;
		byte t = 0;
		int j = 0;
		for(int i = 0; i < 256; i++)
		{
			j += S[i];
			j += bytes[i % bytes.length];
			j &= 255;
			t = S[i];
			S[i] = S[j];
			S[j] = t;
		}
	}

	public void reset()
	{
		byte[] bytes = key.getBytes();
		S[0] = 0;
		for(int i = 1; i < 256; i++)
			S[i] = (byte) i;
		byte t = 0;
		int j = 0;
		for(int i = 0; i < 256; i++)
		{
			j += S[i];
			j += bytes[i % bytes.length];
			j &= 255;
			t = S[i];
			S[i] = S[j];
			S[j] = t;
		}
		this.i = this.j = 0;
	}

	public void wipe()
	{
		key = null;
		Arrays.fill(S, (byte) 0);
		i = j = 0;
	}

	public byte nextByte()
	{
		byte t;
		i++;
		i &= 255;
		j += S[i];
		j &= 255;
		t = S[i];
		S[i] = S[j];
		S[j] = t;
		t += S[i];
		return S[t & 0xFF];
	}

	public void fill(byte[] arr, int start, int len)
	{
		byte t;
		while(len-- > 0)
		{
			i++;
			i &= 255;
			j += S[i];
			j &= 255;
			t = S[i];
			S[i] = S[j];
			S[j] = t;
			t += S[i];
			arr[start++] = S[t & 0xFF];
		}
	}
	
	public RC4State getState()
	{
		return new RC4State(this);
	}

	public void loadState(RC4State state)
	{
		this.i = state.i;
		this.j = state.j;
		System.arraycopy(state.S, 0, S, 0, 256);
	}

	public void updateState(RC4State state)
	{
		state.update(this);
	}
	
	public static final RC4StateFactory sfactory = new RC4StateFactory();
	
	protected static final class RC4State implements IState<RC4State>
	{
		private byte[] S;
		private int i;
		private int j;
		
		public RC4State(RC4 rc4)
		{
			this.S = ArrayUtil.copy(rc4.S);	
			this.i = rc4.i;
			this.j = rc4.j;
		}
		
		public RC4State(byte[] S, int i, int j)
		{
			this.S = S;
			this.i = i;
			this.j = j;
		}
		
		public void update(RC4 rc4)
		{
			if(this.S == null)
				this.S = ArrayUtil.copy(rc4.S);	
			else
				System.arraycopy(rc4.S, 0, S, 0, 256);
			this.i = rc4.i;
			this.j = rc4.j;
		}
		
		public void export(IOutgoingStream stream) throws IOException
		{
			stream.writeInt(i);
			stream.writeInt(j);
			stream.writeBytes(S);
		}

		public void export(byte[] bytes, int offset)
		{
			Bits.intToBytes(i, bytes, offset); offset += 4;
			Bits.intToBytes(j, bytes, offset); offset += 4;
			System.arraycopy(S, 0, bytes, offset, 256);
		}

		public int exportSize()
		{
			return 264;
		}

		public Factory<RC4State> factory()
		{
			return sfactory;
		}

		public void erase()
		{
			Arrays.fill(S, (byte) 0);
			S = null;
			i = j = 0;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.RC4STATE;
		}

		public boolean sameAs(RC4State obj)
		{
			return (i == obj.i && j == obj.j) && ArrayUtil.equals(S, obj.S);
		}
		
	}
	
	protected static final class RC4StateFactory extends Factory<RC4State>
	{
		public RC4StateFactory()
		{
			super(RC4State.class);
		}

		public RC4State resurrect(byte[] data, int start) throws InstantiationException
		{
			int i = Bits.intFromBytes(data, start); start += 4;
			int j = Bits.intFromBytes(data, start); start += 4;
			byte[] bytes = new byte[256];
			System.arraycopy(data, start, bytes, 0, 256);
			return new RC4State(bytes, i, j);
		}

		public RC4State resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			int i = stream.readInt();
			int j = stream.readInt();
			return new RC4State(stream.readBytes(256), i, j);
		}
	}
	
	public static final int test()
	{
		byte[] bytes = new byte[256];
		RandUtils.fillArr(bytes);
		RC4 rc4 = new RC4(new KeyRC4(bytes));
		int e = 0;
		e += IStreamCipher.testCipher(rc4);
		return e;
	}
	
}

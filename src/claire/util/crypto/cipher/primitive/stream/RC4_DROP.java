package claire.util.crypto.cipher.primitive.stream;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.cipher.key.stream.KeyRC4_DROP;
import claire.util.crypto.cipher.primitive.stream.RC4_DROP.RC4_DROPState;
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

public class RC4_DROP 
	   implements IStreamCipher<KeyRC4_DROP, RC4_DROPState> {
	
	private KeyRC4_DROP key;
	private byte[] S = new byte[256];
	
	private int i = 0;
	private int j = 0;

	public RC4_DROP(KeyRC4_DROP key) 
	{
		this.setKey(key);
	}

	public KeyRC4_DROP getKey()
	{
		return this.key;
	}

	public void setKey(KeyRC4_DROP key)
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
		final int max = key.getDropAmount();
		for(int i = 0; i < max; i++)
		{
			i++;
			i &= 255;
			j += S[i];
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
		final int max = key.getDropAmount();
		for(int i = 0; i < max; i++)
		{
			i++;
			i &= 255;
			j += S[i];
			j &= 255;
			t = S[i];
			S[i] = S[j];
			S[j] = t;
		}
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
	
	public RC4_DROPState getState()
	{
		return new RC4_DROPState(this);
	}

	public void loadState(RC4_DROPState state)
	{
		this.i = state.i;
		this.j = state.j;
		System.arraycopy(state.S, 0, S, 0, 256);
	}

	public void updateState(RC4_DROPState state)
	{
		state.update(this);
	}
	
	public static final RC4_DROPStateFactory sfactory = new RC4_DROPStateFactory();
	
	protected static final class RC4_DROPState implements IState<RC4_DROPState>
	{
		private byte[] S;
		private int i;
		private int j;
		
		public RC4_DROPState(RC4_DROP rc4)
		{
			this.S = ArrayUtil.copy(rc4.S);	
			this.i = rc4.i;
			this.j = rc4.j;
		}
		
		public RC4_DROPState(byte[] S, int i, int j)
		{
			this.S = S;
			this.i = i;
			this.j = j;
		}
		
		public void update(RC4_DROP rc4)
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

		public Factory<RC4_DROPState> factory()
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
			return _NAMESPACE.RC4_DROPSTATE;
		}

		public boolean sameAs(RC4_DROPState obj)
		{
			return (i == obj.i && j == obj.j) && ArrayUtil.equals(S, obj.S);
		}
		
	}
	
	protected static final class RC4_DROPStateFactory extends Factory<RC4_DROPState>
	{
		public RC4_DROPStateFactory()
		{
			super(RC4_DROPState.class);
		}

		public RC4_DROPState resurrect(byte[] data, int start) throws InstantiationException
		{
			int i = Bits.intFromBytes(data, start); start += 4;
			int j = Bits.intFromBytes(data, start); start += 4;
			byte[] bytes = new byte[256];
			System.arraycopy(data, start, bytes, 0, 256);
			return new RC4_DROPState(bytes, i, j);
		}

		public RC4_DROPState resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			int i = stream.readInt();
			int j = stream.readInt();
			return new RC4_DROPState(stream.readBytes(256), i, j);
		}
	}
	
	public static final int test()
	{
		byte[] bytes = new byte[256];
		RandUtils.fillArr(bytes);
		RC4_DROP rc4 = new RC4_DROP(new KeyRC4_DROP(bytes, 768));
		int e = 0;
		e += IStreamCipher.testCipher(rc4);
		return e;
	}
	
	public static final int testState()
	{
		byte[] bytes = new byte[256];
		RandUtils.fillArr(bytes);
		RC4_DROPState state = new RC4_DROPState(bytes, RandUtils.dprng.nextIntGood(256), RandUtils.dprng.nextIntGood(256));
		return IPersistable.test(state);
	}
	
}

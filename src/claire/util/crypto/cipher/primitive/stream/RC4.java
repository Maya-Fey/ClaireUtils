package claire.util.crypto.cipher.primitive.stream;

import java.util.Arrays;

import claire.util.crypto.cipher.key.stream.KeyRC4;
import claire.util.standards.crypto.IStreamCipher;

public class RC4 
	   implements IStreamCipher<KeyRC4> {
	
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

	public void wipe()
	{
		key = null;
		Arrays.fill(S, (byte) 0);
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

}

package claire.util.crypto.cipher.primitive.stream;

import java.util.Arrays;

import claire.util.crypto.cipher.key.stream.KeyIBAA;
import claire.util.crypto.cipher.primitive.stream.IBAA.StateIBAA;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.crypto.IState;
import claire.util.standards.crypto.IStreamCipher;

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
		return null;
	}

	public void loadState(StateIBAA state)
	{

	}

	public void updateState(StateIBAA state)
	{
		
	}
	
	protected static abstract class StateIBAA implements IState<StateIBAA> {}

}

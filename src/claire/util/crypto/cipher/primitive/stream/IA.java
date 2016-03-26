package claire.util.crypto.cipher.primitive.stream;

import java.util.Arrays;

import claire.util.crypto.cipher.key.stream.KeyIA;
import claire.util.crypto.cipher.primitive.stream.IA.StateIA;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.crypto.IState;
import claire.util.standards.crypto.IStreamCipher;

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
		// TODO Auto-generated method stub
		return null;
	}

	public void loadState(StateIA state)
	{
		// TODO Auto-generated method stub
		
	}

	public void updateState(StateIA state)
	{
		// TODO Auto-generated method stub
		
	}

	protected abstract static class StateIA implements IState<StateIA> {}
	
}

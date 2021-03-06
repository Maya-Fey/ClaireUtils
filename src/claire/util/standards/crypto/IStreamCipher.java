package claire.util.standards.crypto;

import claire.util.logging.Log;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IStateMachine;

public interface IStreamCipher<Type extends IKey<?>, State extends IState<State>>
	   extends ICrypto<Type>,
	   		   IStateMachine<State> {
	
	void wipe();
	
	byte nextByte();
	void fill(byte[] arr, int start, int len);
	
	default byte[] getBytes(int amt)
	{
		byte[] bytes = new byte[amt];
		this.fill(bytes, 0, amt);
		return bytes;
	}
	
	default byte crypt(byte in)
	{
		return in ^= nextByte();
	}

	default void crypt(byte[] bytes, int start, int len)
	{
		while(len-- > 0)
		{
			bytes[start++] ^= nextByte();
		}
	}
	
	public static <State extends IState<State>> int testCipher(IStreamCipher<?, State> cip)
	{
		int e = 0;
		try {
			byte b1 = cip.nextByte();
			cip.reset();
			if(b1 != cip.nextByte()) {
				Log.err.println("Cipher resetting didn't work.");
				e++;
			}
			cip.reset();
			byte[] t1 = new byte[10];
			byte[] t2 = new byte[10];
			for(int i = 0; i < 10; i++)
				t1[i] = cip.nextByte();
			cip.reset();
			cip.fill(t2, 0, 10);
			if(!ArrayUtil.equals(t1, t2)) {
				Log.err.println("Filling array manuall and using function yielded different results.");
				e++;
			}
			State save = cip.getState();
			cip.fill(t1, 0, 10);
			cip.loadState(save);
			cip.fill(t2, 0, 10);
			if(!ArrayUtil.equals(t1, t2)) {
				Log.err.println("Saving and loading state did not work.");
				e++;
			}
			cip.updateState(save);
			cip.fill(t2, 0, 10);
			cip.loadState(save);
			cip.fill(t2, 0, 10);
			if(ArrayUtil.equals(t1, t2)) {
				Log.err.println("Updating state did not work.");
				e++;
			}
			cip.reset();
			cip.wipe();
			try {
				if(b1 == cip.nextByte()) {
					Log.err.println("Wiping the cipher had no effect");
					e++;
				}
			} catch(Exception ex) {}
		} catch (Exception ex) {
			e++;
			Log.err.println("Unexpected " + ex.getClass().getSimpleName() + ": " + ex.getMessage() + " encountered while testing " + cip.getClass().getSimpleName());
			ex.printStackTrace();
		}
		return e;
	}
	
}

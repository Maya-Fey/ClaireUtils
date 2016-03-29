package claire.util.crypto.hash.primitive;

import java.util.Arrays;

import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IState;

public final class SHA2_256 
	  		 extends SHA2_Base_32<SHA2_256> {

	public SHA2_256() 
	{
		super(32);
		reset();
	}

	protected void reset()
	{
		length = 0;
		STATE[0] = 0x6a09e667;
        STATE[1] = 0xbb67ae85;
        STATE[2] = 0x3c6ef372;
        STATE[3] = 0xa54ff53a;
        STATE[4] = 0x510e527f;
        STATE[5] = 0x9b05688c;
        STATE[6] = 0x1f83d9ab;
        STATE[7] = 0x5be0cd19;
	}

	protected void complete(byte[] remaining, int pos, byte[] out, int start)
	{
		byte[] bytes = new byte[64];
		System.arraycopy(remaining, 0, bytes, 0, pos);
		bytes[pos] = (byte) 0x80;
		length += pos;
		if(pos >= 56) {
			processNext(bytes, 0);
			length -= 64;
			Arrays.fill(bytes, (byte) 0);
		}
		Bits.BigEndian.longToBytes(length << 3, bytes, 56);
		processNext(bytes, 0);
		Bits.BigEndian.intsToBytes(STATE, 0, out, start, 8);
	}
	
	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		SHA2_256 blake = new SHA2_256();
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		int i = 0;
		i += IPersistable.test(state);
		return i;
	}

}

package claire.util.crypto.hash.primitive;

import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IState;

public class BLAKE256 
	   extends BLAKE_Base_32<BLAKE256> {
	
	private static final int[] IV = 
	{
		0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
		0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
	};

	public BLAKE256()
	{
		super(32);
	}

	protected int[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
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
		BLAKE256 blake = new BLAKE256();
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		int i = 0;
		i += IPersistable.test(state);
		return i;
	}

}

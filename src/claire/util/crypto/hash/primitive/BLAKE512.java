package claire.util.crypto.hash.primitive;

import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IState;

public class BLAKE512 
	   extends BLAKE_Base_64<BLAKE512> {
	
	private static final long[] IV = 
	{
		0x6a09e667f3bcc908L, 0xbb67ae8584caa73bL,
		0x3c6ef372fe94f82bL, 0xa54ff53a5f1d36f1L,
		0x510e527fade682d1L, 0x9b05688c2b3e6c1fL,
		0x1f83d9abfb41bd6bL, 0x5be0cd19137e2179L
	};

	public BLAKE512()
	{
		super(64);
	}

	protected long[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.BigEndian.longsToBytes(STATE, 0, out, start, 8);
	}

	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		BLAKE512 blake = new BLAKE512();
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		int i = 0;
		i += IPersistable.test(state);
		return i;
	}
	
}

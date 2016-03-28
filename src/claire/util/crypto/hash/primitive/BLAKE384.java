package claire.util.crypto.hash.primitive;

import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IState;

public class BLAKE384 
	   extends BLAKE_Base_64<BLAKE384> {
	
	private static final long[] IV = 
	{
		0xcbbb9d5dc1059ed8L, 0x629a292a367cd507L,
		0x9159015a3070dd17L, 0x152fecd8f70e5939L,
		0x67332667ffc00b31L, 0x8eb44a8768581511L,
		0xdb0c2e0d64f98fa7L, 0x47b5481dbefa4fa4L
	};

	public BLAKE384()
	{
		super(48);
	}

	protected long[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.BigEndian.longsToBytes(STATE, 0, out, start, 6);
	}

	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		BLAKE384 blake = new BLAKE384();
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		int i = 0;
		i += IPersistable.test(state);
		return i;
	}

}

package claire.util.crypto.hash.primitive;

import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IState;

public class BLAKE224 
	   extends BLAKE_Base_32<BLAKE224> {
	
	private static final int[] IV = 
	{
		0xc1059ed8, 0x367cd507, 0x3070dd17, 0xf70e5939,
		0xffc00b31, 0x68581511, 0x64f98fa7, 0xbefa4fa4
	};

	public BLAKE224()
	{
		super(28);
	}

	protected int[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.BigEndian.intsToBytes(STATE, 0, out, start, 7);
	}

	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		BLAKE224 blake = new BLAKE224();
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		int i = 0;
		i += IPersistable.test(state);
		return i;
	}
	
}

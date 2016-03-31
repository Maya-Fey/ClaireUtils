package claire.util.crypto.hash.primitive;

import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IState;

public class SHAvite224 
	   extends SHAvite_Base_32<SHAvite224> {
	
	private static final int[] IV = 
	{
		0x6774F31C, 0x990AE210, 0xC87D4274, 0xC9546371,
		0x62B2AEA8, 0x4B5801D8, 0x1B702860, 0x842F3017
	};

	public SHAvite224()
	{
		super(28);
	}

	protected int[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.intsToBytes(STATE, 0, out, start, 7);
	}

	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		SHAvite224 blake = new SHAvite224();
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		int i = 0;
		i += IPersistable.test(state);
		return i;
	}
	
}

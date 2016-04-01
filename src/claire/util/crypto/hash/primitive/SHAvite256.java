package claire.util.crypto.hash.primitive;

import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public class SHAvite256 
	   extends SHAvite_Base_32<SHAvite256> {
	
	private static final int[] IV = 
	{
		0x49bb3e47, 0x2674860d, 0xa8b392ac, 0x021ac4e6,
		0x409283cf, 0x620e5d86, 0x6d929dcb, 0x96cc2a8b
	};

	public SHAvite256()
	{
		super(32);
	}

	protected int[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.intsToBytes(STATE, 0, out, start, 8);
	}
	
	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		SHAvite256 blake = new SHAvite256();
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}

}

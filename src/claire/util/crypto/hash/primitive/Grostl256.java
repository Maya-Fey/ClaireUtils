package claire.util.crypto.hash.primitive;

import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public class Grostl256 
	   extends Grostl_Base_32<Grostl256> {

	public Grostl256() 
	{
		super(32);
	}

	protected void output(byte[] out, int start)
	{
		Bits.BigEndian.longToBytes(A[4] ^ B[4], out, start     );
		Bits.BigEndian.longToBytes(A[5] ^ B[5], out, start + 8 );
		Bits.BigEndian.longToBytes(A[6] ^ B[6], out, start + 16);
		Bits.BigEndian.longToBytes(A[7] ^ B[7], out, start + 24);
	}
	
	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		Grostl256 blake = new Grostl256();
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

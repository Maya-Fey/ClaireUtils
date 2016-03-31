package claire.util.crypto.hash.primitive;

import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IState;

public class Grostl512 
	   extends Grostl_Base_64<Grostl512> {

	public Grostl512() 
	{
		super(64);
	}

	protected void output(byte[] out, int start)
	{
		Bits.BigEndian.longToBytes(A[8 ] ^ B[8 ], out, start     );
		Bits.BigEndian.longToBytes(A[9 ] ^ B[9 ], out, start + 8 );
		Bits.BigEndian.longToBytes(A[10] ^ B[10], out, start + 16);
		Bits.BigEndian.longToBytes(A[11] ^ B[11], out, start + 24);
		Bits.BigEndian.longToBytes(A[12] ^ B[12], out, start + 32);
		Bits.BigEndian.longToBytes(A[13] ^ B[13], out, start + 40);
		Bits.BigEndian.longToBytes(A[14] ^ B[14], out, start + 48);
		Bits.BigEndian.longToBytes(A[15] ^ B[15], out, start + 56);
	}
	
	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		Grostl512 blake = new Grostl512();
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		int i = 0;
		i += IPersistable.test(state);
		return i;
	}

}

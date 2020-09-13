package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
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
	
	public int hashID()
	{
		return Hash.GROSTL256;
	}

	protected void output(byte[] out, int start, int max)
	{
		if(max > 24) {
			Bits.BigEndian.longToBytes(A[4] ^ B[4], out, start     );
			Bits.BigEndian.longToBytes(A[5] ^ B[5], out, start + 4 );
			Bits.BigEndian.longToBytes(A[6] ^ B[6], out, start + 12);
			Bits.BigEndian.longToBytes(A[7] ^ B[7], out, start + 20, max - 24);
		} else if(max > 16) {
			Bits.BigEndian.longToBytes(A[4] ^ B[4], out, start     );
			Bits.BigEndian.longToBytes(A[5] ^ B[5], out, start + 4 );
			Bits.BigEndian.longToBytes(A[6] ^ B[6], out, start + 12, max - 16);
		} else if(max > 8) {
			Bits.BigEndian.longToBytes(A[4] ^ B[4], out, start     );
			Bits.BigEndian.longToBytes(A[5] ^ B[5], out, start + 4, max - 8);
		} else {
			Bits.BigEndian.longToBytes(A[4] ^ B[4], out, start, max);
		}
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

	public String genString(char sep)
	{
		return "";
	}
	
	public HashFactory<Grostl256> factory()
	{
		return factory;
	}
	
	public static final Grostl256Factory factory = new Grostl256Factory();
	
	public static final class Grostl256Factory extends HashFactory<Grostl256>
	{

		public Grostl256 build(CryptoString str)
		{
			return new Grostl256();
		}

	}


}

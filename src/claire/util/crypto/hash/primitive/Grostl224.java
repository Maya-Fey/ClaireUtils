package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public class Grostl224 
	   extends Grostl_Base_32<Grostl224> {

	public Grostl224() 
	{
		super(28);
	}
	
	public int hashID()
	{
		return Hash.GROSTL224;
	}

	protected void output(byte[] out, int start, int max)
	{
		if(max > 20) {
			Bits.BigEndian.intToBytes((int) (A[4] ^ B[4]), out, start);
			Bits.BigEndian.longToBytes(A[5] ^ B[5], out, start + 4 );
			Bits.BigEndian.longToBytes(A[6] ^ B[6], out, start + 12);
			Bits.BigEndian.longToBytes(A[7] ^ B[7], out, start + 20, max - 20);
		} else if(max > 12) {
			Bits.BigEndian.intToBytes((int) (A[4] ^ B[4]), out, start);
			Bits.BigEndian.longToBytes(A[5] ^ B[5], out, start + 4 );
			Bits.BigEndian.longToBytes(A[6] ^ B[6], out, start + 12, max - 12);
		} else if(max > 4) {
			Bits.BigEndian.intToBytes((int) (A[4] ^ B[4]), out, start);
			Bits.BigEndian.longToBytes(A[5] ^ B[5], out, start + 4, max - 4);
		} else {
			Bits.BigEndian.intToBytes((int) (A[4] ^ B[4]), out, start, max);
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
		Grostl224 blake = new Grostl224();
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
	
	public HashFactory<Grostl224> factory()
	{
		return factory;
	}
	
	public static final Grostl224Factory factory = new Grostl224Factory();
	
	public static final class Grostl224Factory extends HashFactory<Grostl224>
	{

		public Grostl224 build(CryptoString str)
		{
			return new Grostl224();
		}

	}

}

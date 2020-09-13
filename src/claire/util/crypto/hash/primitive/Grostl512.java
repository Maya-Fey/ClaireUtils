package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public class Grostl512 
	   extends Grostl_Base_64<Grostl512> {

	public Grostl512() 
	{
		super(64);
	}
	
	public int hashID()
	{
		return Hash.GROSTL512;
	}

	protected void output(byte[] out, int start, int max)
	{
		if(max > 56) {
			Bits.BigEndian.longToBytes(A[8 ] ^ B[8 ], out, start     );
			Bits.BigEndian.longToBytes(A[9 ] ^ B[9 ], out, start + 8 );
			Bits.BigEndian.longToBytes(A[10] ^ B[10], out, start + 16);
			Bits.BigEndian.longToBytes(A[11] ^ B[11], out, start + 24);
			Bits.BigEndian.longToBytes(A[12] ^ B[12], out, start + 32);
			Bits.BigEndian.longToBytes(A[13] ^ B[13], out, start + 40);
			Bits.BigEndian.longToBytes(A[14] ^ B[14], out, start + 48);
			Bits.BigEndian.longToBytes(A[15] ^ B[15], out, start + 56, max - 56);
		} else if(max > 48) {
			Bits.BigEndian.longToBytes(A[8 ] ^ B[8 ], out, start     );
			Bits.BigEndian.longToBytes(A[9 ] ^ B[9 ], out, start + 8 );
			Bits.BigEndian.longToBytes(A[10] ^ B[10], out, start + 16);
			Bits.BigEndian.longToBytes(A[11] ^ B[11], out, start + 24);
			Bits.BigEndian.longToBytes(A[12] ^ B[12], out, start + 32);
			Bits.BigEndian.longToBytes(A[13] ^ B[13], out, start + 40);
			Bits.BigEndian.longToBytes(A[14] ^ B[14], out, start + 48, max - 48);
		} else if(max > 40) {
			Bits.BigEndian.longToBytes(A[8 ] ^ B[8 ], out, start     );
			Bits.BigEndian.longToBytes(A[9 ] ^ B[9 ], out, start + 8 );
			Bits.BigEndian.longToBytes(A[10] ^ B[10], out, start + 16);
			Bits.BigEndian.longToBytes(A[11] ^ B[11], out, start + 24);
			Bits.BigEndian.longToBytes(A[12] ^ B[12], out, start + 32);
			Bits.BigEndian.longToBytes(A[13] ^ B[13], out, start + 40, max - 40);
		} else if(max > 32) {
			Bits.BigEndian.longToBytes(A[8 ] ^ B[8 ], out, start     );
			Bits.BigEndian.longToBytes(A[9 ] ^ B[9 ], out, start + 8 );
			Bits.BigEndian.longToBytes(A[10] ^ B[10], out, start + 16);
			Bits.BigEndian.longToBytes(A[11] ^ B[11], out, start + 24);
			Bits.BigEndian.longToBytes(A[12] ^ B[12], out, start + 32, max - 32);
		} else if(max > 24) {
			Bits.BigEndian.longToBytes(A[8 ] ^ B[8 ], out, start     );
			Bits.BigEndian.longToBytes(A[9 ] ^ B[9 ], out, start + 8 );
			Bits.BigEndian.longToBytes(A[10] ^ B[10], out, start + 16);
			Bits.BigEndian.longToBytes(A[11] ^ B[11], out, start + 24, max - 24);
		} else if(max > 16) {
			Bits.BigEndian.longToBytes(A[8 ] ^ B[8 ], out, start     );
			Bits.BigEndian.longToBytes(A[9 ] ^ B[9 ], out, start + 8 );
			Bits.BigEndian.longToBytes(A[10] ^ B[10], out, start + 16, max - 16);
		} else if(max > 8) {
			Bits.BigEndian.longToBytes(A[8 ] ^ B[8 ], out, start     );
			Bits.BigEndian.longToBytes(A[9 ] ^ B[9 ], out, start + 8, max - 8);
		} else {
			Bits.BigEndian.longToBytes(A[8 ] ^ B[8 ], out, start, max);
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
		Grostl512 blake = new Grostl512();
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
	
	public HashFactory<Grostl512> factory()
	{
		return factory;
	}
	
	public static final Grostl512Factory factory = new Grostl512Factory();
	
	public static final class Grostl512Factory extends HashFactory<Grostl512>
	{

		public Grostl512 build(CryptoString str)
		{
			return new Grostl512();
		}

	}


}

package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
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
	
	public int hashID()
	{
		return Hash.BLAKE384;
	}

	protected long[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start, int max)
	{
		Bits.BigEndian.longsToSBytes(STATE, 0, out, start, 48 > max ? max : 48);
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
	
	public HashFactory<BLAKE384> factory()
	{
		return factory;
	}
	
	public static final BLAKE384Factory factory = new BLAKE384Factory();
	
	public static final class BLAKE384Factory extends HashFactory<BLAKE384>
	{

		public BLAKE384 build(CryptoString str)
		{
			return new BLAKE384();
		}
		
	}

}

package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public class BLAKE256 
	   extends BLAKE_Base_32<BLAKE256> {
	
	private static final int[] IV = 
	{
		0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
		0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
	};

	public BLAKE256()
	{
		super(32);
	}
	
	public int hashID()
	{
		return Hash.BLAKE256;
	}

	protected int[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start, int max)
	{
		Bits.BigEndian.intsToSBytes(STATE, 0, out, start, 32 > max ? max : 32);
	}

	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		BLAKE256 blake = new BLAKE256();
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
	
	public HashFactory<BLAKE256> factory()
	{
		return factory;
	}
	
	public static final BLAKE256Factory factory = new BLAKE256Factory();
	
	public static final class BLAKE256Factory extends HashFactory<BLAKE256>
	{

		public BLAKE256 build(CryptoString str)
		{
			return new BLAKE256();
		}
		
	}

}

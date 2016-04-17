package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public final class SHA2_384 
	  		 extends SHA2_Base_64<SHA2_384> {
	
	public SHA2_384() 
	{
		super(48);
	}

	public void reset()
	{
		super.reset();
		counter.reset();
		STATE[0] = 0xcbbb9d5dc1059ed8l;
        STATE[1] = 0x629a292a367cd507l;
        STATE[2] = 0x9159015a3070dd17l;
        STATE[3] = 0x152fecd8f70e5939l;
        STATE[4] = 0x67332667ffc00b31l;
        STATE[5] = 0x8eb44a8768581511l;
        STATE[6] = 0xdb0c2e0d64f98fa7l;
        STATE[7] = 0x47b5481dbefa4fa4l;
	}
	
	public int hashID()
	{
		return Hash.SHA2_384;
	}


	protected void complete(byte[] out, int start)
	{
		Bits.BigEndian.longsToBytes(STATE, 0, out, start, 6);
	}
	
	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		SHA2_384 blake = new SHA2_384();
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}
	
	public HashFactory<SHA2_384> factory()
	{
		return factory;
	}
	
	public static final SHA2_384Factory factory = new SHA2_384Factory();
	
	public static final class SHA2_384Factory extends HashFactory<SHA2_384>
	{

		public SHA2_384 build(CryptoString str)
		{
			return new SHA2_384();
		}
		
	}

}

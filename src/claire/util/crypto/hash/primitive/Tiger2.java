package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public class Tiger2 
	   extends Tiger_Base<Tiger2> {

	public Tiger2() 
	{
		super((byte) 0x80);
	}
	
	public int hashID()
	{
		return Hash.TIGER2;
	}
	
	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		Tiger2 blake = new Tiger2();
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}
	
	public HashFactory<Tiger2> factory()
	{
		return factory;
	}
	
	public static final Tiger2Factory factory = new Tiger2Factory();
	
	public static final class Tiger2Factory extends HashFactory<Tiger2>
	{

		public Tiger2 build(CryptoString str)
		{
			return new Tiger2();
		}
		
	}

}

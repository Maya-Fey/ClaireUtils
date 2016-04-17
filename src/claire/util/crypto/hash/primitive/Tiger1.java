package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public class Tiger1 
	   extends Tiger_Base<Tiger1> {

	public Tiger1() 
	{
		super((byte) 0x01);
	}
	
	public int hashID()
	{
		return Hash.TIGER1;
	}

	
	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		Tiger1 blake = new Tiger1();
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}
	
	public HashFactory<Tiger1> factory()
	{
		return factory;
	}
	
	public static final Tiger1Factory factory = new Tiger1Factory();
	
	public static final class Tiger1Factory extends HashFactory<Tiger1>
	{

		public Tiger1 build(CryptoString str)
		{
			return new Tiger1();
		}
		
	}

}

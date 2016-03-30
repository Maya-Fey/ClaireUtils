package claire.util.crypto.hash.primitive;

import claire.util.crypto.rng.RandUtils;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IState;

public class Tiger1 
	   extends Tiger_Base<Tiger1> {

	public Tiger1() 
	{
		super((byte) 0x01);
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
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		int i = 0;
		i += IPersistable.test(state);
		return i;
	}

}

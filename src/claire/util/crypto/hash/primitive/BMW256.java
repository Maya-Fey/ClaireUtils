package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public class BMW256 
	   extends BMW_Base_32<BMW256> {
	
	private static final int[] IV = 
	{
		0x40414243, 0x44454647, 0x48494a4b, 0x4c4d4e4f,
		0x50515253, 0x54555657, 0x58595a5b, 0x5c5d5e5f,
		0x60616263, 0x64656667, 0x68696a6b, 0x6c6d6e6f,
		0x70717273, 0x74757677, 0x78797a7b, 0x7c7d7e7f
	};

	public BMW256() 
	{
		super(32);
	}

	protected int[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.intsToBytes(STATE, 8, out, start, 8);
	}
	
	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		BMW256 blake = new BMW256();
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}
	
	public HashFactory<BMW256> factory()
	{
		return factory;
	}
	
	public static final BMW256Factory factory = new BMW256Factory();
	
	public static final class BMW256Factory extends HashFactory<BMW256>
	{

		public BMW256 build(CryptoString str)
		{
			return new BMW256();
		}
		
	}

}

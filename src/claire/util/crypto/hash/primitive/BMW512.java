package claire.util.crypto.hash.primitive;

import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public class BMW512 
	   extends BMW_Base_64<BMW512> {
	
	private static final long[] IV = 
	{
		0x8081828384858687L, 0x88898a8b8c8d8e8fL,
		0x9091929394959697L, 0x98999a9b9c9d9e9fL,
		0xa0a1a2a3a4a5a6a7L, 0xa8a9aaabacadaeafL,
		0xb0b1b2b3b4b5b6b7L, 0xb8b9babbbcbdbebfL,
		0xc0c1c2c3c4c5c6c7L, 0xc8c9cacbcccdcecfL,
		0xd0d1d2d3d4d5d6d7L, 0xd8d9dadbdcdddedfL,
		0xe0e1e2e3e4e5e6e7L, 0xe8e9eaebecedeeefL,
		0xf0f1f2f3f4f5f6f7L, 0xf8f9fafbfcfdfeffL
	};

	public BMW512() 
	{
		super(64);
	}

	protected long[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.longsToBytes(STATE, 8, out, start, 8);
	}

	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		BMW512 blake = new BMW512();
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}
	
	public HashFactory<BMW512> factory()
	{
		return factory;
	}
	
	public static final BMW512Factory factory = new BMW512Factory();
	
	public static final class BMW512Factory extends HashFactory<BMW512>
	{

		public BMW512 build(char[] params, char sep)
		{
			return new BMW512();
		}
		
	}
	
}

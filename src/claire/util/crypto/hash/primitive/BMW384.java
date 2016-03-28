package claire.util.crypto.hash.primitive;

import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IState;

public class BMW384 
	   extends BMW_Base_64<BMW384> {
	
	private static final long[] IV = 
	{
		0x0001020304050607L, 0x08090a0b0c0d0e0fL,
		0x1011121314151617L, 0x18191a1b1c1d1e1fL,
		0x2021222324252627L, 0x28292a2b2c2d2e2fL,
		0x3031323334353637L, 0x38393a3b3c3d3e3fL,
		0x4041424344454647L, 0x48494a4b4c4d4e4fL,
		0x5051525354555657L, 0x58595a5b5c5d5e5fL,
		0x6061626364656667L, 0x68696a6b6c6d6e6fL,
		0x7071727374757677L, 0x78797a7b7c7d7e7fL
	};

	public BMW384() 
	{
		super(48);
	}

	protected long[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.longsToBytes(STATE, 10, out, start, 6);
	}

	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		BMW384 blake = new BMW384();
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		int i = 0;
		i += IPersistable.test(state);
		return i;
	}
	
}

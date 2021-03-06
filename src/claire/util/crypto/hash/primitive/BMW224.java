package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public class BMW224 
	   extends BMW_Base_32<BMW224> {
	
	private static final int[] IV = 
	{
		0x00010203, 0x04050607, 0x08090a0b, 0x0c0d0e0f,
		0x10111213, 0x14151617, 0x18191a1b, 0x1c1d1e1f,
		0x20212223, 0x24252627, 0x28292a2b, 0x2c2d2e2f,
		0x30313233, 0x34353637, 0x38393a3b, 0x3c3d3e3f
	};

	public BMW224() 
	{
		super(28);
	}

	public int hashID()
	{
		return Hash.BMW224;
	}
	
	protected int[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start, int max)
	{
		Bits.intsToSBytes(STATE, 9, out, start, 28 > max ? max : 28);
	}
	
	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		BMW224 blake = new BMW224();
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
	
	public HashFactory<BMW224> factory()
	{
		return factory;
	}
	
	public static final BMW224Factory factory = new BMW224Factory();
	
	public static final class BMW224Factory extends HashFactory<BMW224>
	{

		public BMW224 build(CryptoString str)
		{
			return new BMW224();
		}

	}

}

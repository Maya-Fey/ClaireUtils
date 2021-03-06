package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public class JH512 
	   extends JHCore<JH512> {
	
	private static final long[] IV = 
	{
		0x6fd14b963e00aa17L, 0x636a2e057a15d543L,
		0x8a225e8d0c97ef0bL, 0xe9341259f2b3c361L,
		0x891da0c1536f801eL, 0x2aa9056bea2b6d80L,
		0x588eccdb2075baa6L, 0xa90f3a76baf83bf7L,
		0x0169e60541e34a69L, 0x46b58a8e2e6fe65aL,
		0x1047a7d0c1843c24L, 0x3b6e71b12d5ac199L,
		0xcf57f6ec9db1f856L, 0xa706887c5716b156L,
		0xe3c2fcdfe68517fbL, 0x545a4678cc8cdd4bL
	};

	public JH512()
	{
		super(64);
	}

	public int hashID()
	{
		return Hash.JH512;
	}
	
	protected long[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start, int max)
	{
		Bits.BigEndian.longsToSBytes(STATE, 8, out, start, 64 > max ? max : 64);
	}
	
	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		JH512 blake = new JH512();
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
	
	public HashFactory<JH512> factory()
	{
		return factory;
	}
	
	public static final JH512Factory factory = new JH512Factory();
	
	public static final class JH512Factory extends HashFactory<JH512>
	{

		public JH512 build(CryptoString str)
		{
			return new JH512();
		}

	}

}

package claire.util.crypto.hash.primitive;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public class JH224 
	   extends JHCore<JH224> {
	
	private static final long[] IV = 
	{
		0x2dfedd62f99a98acL, 0xae7cacd619d634e7L,
		0xa4831005bc301216L, 0xb86038c6c9661494L,
		0x66d9899f2580706fL, 0xce9ea31b1d9b1adcL,
		0x11e8325f7b366e10L, 0xf994857f02fa06c1L,
		0x1b4f1b5cd8c840b3L, 0x97f6a17f6e738099L,
		0xdcdf93a5adeaa3d3L, 0xa431e8dec9539a68L,
		0x22b4a98aec86a1e4L, 0xd574ac959ce56cf0L,
		0x15960deab5ab2bbfL, 0x9611dcf0dd64ea6eL
	};

	public JH224()
	{
		super(28);
	}

	protected long[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.BigEndian.intToBytes((int) STATE[12], out, start);
		Bits.BigEndian.longsToBytes(STATE, 13, out, start + 4, 3);
	}
	
	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		JH224 blake = new JH224();
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}
	
	public HashFactory<JH224> factory()
	{
		return factory;
	}
	
	public static final JH224Factory factory = new JH224Factory();
	
	public static final class JH224Factory extends HashFactory<JH224>
	{

		public JH224 build(CryptoString str)
		{
			return new JH224();
		}
		
	}

}

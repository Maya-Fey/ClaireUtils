package claire.util.crypto.hash.primitive;

import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;

public final class SHA2_224
	         extends SHA2_Base_32<SHA2_224> {

	public SHA2_224() 
	{
		super(28);
	}

	public void reset()
	{
		super.reset();
		length = 0;
		STATE[0] = 0xc1059ed8;
        STATE[1] = 0x367cd507;
        STATE[2] = 0x3070dd17;
        STATE[3] = 0xf70e5939;
        STATE[4] = 0xffc00b31;
        STATE[5] = 0x68581511;
        STATE[6] = 0x64f98fa7;
        STATE[7] = 0xbefa4fa4;
	}

	protected void complete(byte[] remaining, int pos, byte[] out, int start)
	{
		byte[] bytes = new byte[64];
		System.arraycopy(remaining, 0, bytes, 0, pos);
		bytes[pos] = (byte) 0x80;
		length += pos;
		if(pos >= 56) {
			processNext(bytes, 0);
			length -= 64;
			Arrays.fill(bytes, (byte) 0);
		}
		Bits.BigEndian.longToBytes(length << 3, bytes, 56);
		processNext(bytes, 0);
		Bits.BigEndian.intsToBytes(STATE, 0, out, start, 7);
	}
	
	/*
	 * This isn't actually required, just convenient because IState<?>
	 * doesn't cast to (T extends extends IPersistable<T> & IUUID<T>)
	 * so rather than create a special method this was used.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final int test()
	{
		SHA2_224 blake = new SHA2_224();
		int i = 0;
		i += IHash.test(blake);
		byte[] bytes = new byte[1000];
		RandUtils.fillArr(bytes);
		blake.add(bytes);
		IState state = blake.getState();
		i += IPersistable.test(state);
		return i;
	}
	
	public HashFactory<SHA2_224> factory()
	{
		return factory;
	}
	
	public static final SHA2_224Factory factory = new SHA2_224Factory();
	
	public static final class SHA2_224Factory extends HashFactory<SHA2_224>
	{

		public SHA2_224 build(CryptoString str)
		{
			return new SHA2_224();
		}
		
	}
	

}

package claire.util.crypto.hash.primitive;

import claire.util.memory.Bits;

public class SHAvite256 
	   extends SHAvite_Base_32 {
	
	private static final int[] IV = 
	{
		0x49bb3e47, 0x2674860d, 0xa8b392ac, 0x021ac4e6,
		0x409283cf, 0x620e5d86, 0x6d929dcb, 0x96cc2a8b
	};

	public SHAvite256()
	{
		super(32);
	}

	protected int[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.intsToBytes(STATE, 0, out, start, 8);
	}

}

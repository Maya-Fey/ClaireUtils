package claire.util.crypto.hash.primitive;

import claire.util.memory.Bits;

class BLAKE256 
extends BLAKE_Base_32<BLAKE256> {
	
	private static final int[] IV = 
	{
		0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
		0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
	};

	public BLAKE256()
	{
		super(32);
	}

	protected int[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.BigEndian.intsToBytes(STATE, 0, out, start, 8);
	}

	

}

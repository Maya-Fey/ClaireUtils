package claire.util.crypto.hash.primitive;

import claire.util.memory.Bits;

class BLAKE224 
extends BLAKE_Base_32 {
	
	private static final int[] IV = 
	{
		0xc1059ed8, 0x367cd507, 0x3070dd17, 0xf70e5939,
		0xffc00b31, 0x68581511, 0x64f98fa7, 0xbefa4fa4
	};

	public BLAKE224()
	{
		super(28);
	}

	protected int[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.BigEndian.intsToBytes(STATE, 0, out, start, 7);
	}

	

}

package claire.util.crypto.hash.primitive;

import claire.util.memory.Bits;

class BLAKE384 
extends BLAKE_Base_64 {
	
	private static final long[] IV = 
	{
		0xcbbb9d5dc1059ed8L, 0x629a292a367cd507L,
		0x9159015a3070dd17L, 0x152fecd8f70e5939L,
		0x67332667ffc00b31L, 0x8eb44a8768581511L,
		0xdb0c2e0d64f98fa7L, 0x47b5481dbefa4fa4L
	};

	public BLAKE384()
	{
		super(48);
	}

	protected long[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.BigEndian.longsToBytes(STATE, 0, out, start, 6);
	}

	

}

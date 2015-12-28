package claire.util.crypto.hash.primitive;

import claire.util.memory.Bits;

class BLAKE512 
extends BLAKE_Base_64 {
	
	private static final long[] IV = 
	{
		0x6a09e667f3bcc908L, 0xbb67ae8584caa73bL,
		0x3c6ef372fe94f82bL, 0xa54ff53a5f1d36f1L,
		0x510e527fade682d1L, 0x9b05688c2b3e6c1fL,
		0x1f83d9abfb41bd6bL, 0x5be0cd19137e2179L
	};

	public BLAKE512()
	{
		super(64);
	}

	protected long[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.BigEndian.longsToBytes(STATE, 0, out, start, 8);
	}

	

}

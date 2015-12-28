package claire.util.crypto.hash.primitive;

import claire.util.memory.Bits;

final class SHA2_512 
	  extends SHA2_Base_64 {

	public SHA2_512() {
		super(64);
	}

	protected void reset()
	{
		STATE[0] = 0x6a09e667f3bcc908L;
        STATE[1] = 0xbb67ae8584caa73bL;
        STATE[2] = 0x3c6ef372fe94f82bL;
        STATE[3] = 0xa54ff53a5f1d36f1L;
        STATE[4] = 0x510e527fade682d1L;
        STATE[5] = 0x9b05688c2b3e6c1fL;
        STATE[6] = 0x1f83d9abfb41bd6bL;
        STATE[7] = 0x5be0cd19137e2179L;
	}

	protected void complete(byte[] out, int start)
	{
		Bits.BigEndian.longsToBytes(STATE, 0, out, start, 8);
	}

}

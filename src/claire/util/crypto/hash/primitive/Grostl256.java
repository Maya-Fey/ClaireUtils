package claire.util.crypto.hash.primitive;

import claire.util.memory.Bits;

public class Grostl256 
	   extends Grostl_Base_32 {

	public Grostl256() 
	{
		super(32);
	}

	protected void output(byte[] out, int start)
	{
		Bits.BigEndian.longToBytes(A[4] ^ B[4], out, start     );
		Bits.BigEndian.longToBytes(A[5] ^ B[5], out, start + 8 );
		Bits.BigEndian.longToBytes(A[6] ^ B[6], out, start + 16);
		Bits.BigEndian.longToBytes(A[7] ^ B[7], out, start + 24);
	}

}

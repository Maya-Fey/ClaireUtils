package claire.util.crypto.hash.primitive;

import claire.util.memory.Bits;

public class Grostl224 
	   extends Grostl_Base_32 {

	public Grostl224() 
	{
		super(28);
	}

	protected void output(byte[] out, int start)
	{
		Bits.BigEndian.intToBytes((int) (A[4] ^ B[4]), out, start);
		Bits.BigEndian.longToBytes(A[5] ^ B[5], out, start + 4 );
		Bits.BigEndian.longToBytes(A[6] ^ B[6], out, start + 12);
		Bits.BigEndian.longToBytes(A[7] ^ B[7], out, start + 20);
	}

}

package claire.util.crypto.hash.primitive;

import claire.util.memory.Bits;

public class Grostl512 
	   extends Grostl_Base_64 {

	public Grostl512() 
	{
		super(64);
	}

	protected void output(byte[] out, int start)
	{
		Bits.BigEndian.longToBytes(A[8 ] ^ B[8 ], out, start     );
		Bits.BigEndian.longToBytes(A[9 ] ^ B[9 ], out, start + 8 );
		Bits.BigEndian.longToBytes(A[10] ^ B[10], out, start + 16);
		Bits.BigEndian.longToBytes(A[11] ^ B[11], out, start + 24);
		Bits.BigEndian.longToBytes(A[12] ^ B[12], out, start + 32);
		Bits.BigEndian.longToBytes(A[13] ^ B[13], out, start + 40);
		Bits.BigEndian.longToBytes(A[14] ^ B[14], out, start + 48);
		Bits.BigEndian.longToBytes(A[15] ^ B[15], out, start + 56);
	}

}

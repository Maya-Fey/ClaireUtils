package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.Grostl_Base_64.Grostl64State;
import claire.util.crypto.hash.primitive.MerkleHash.MerkleState;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

abstract class Grostl_Base_64<Hash extends Grostl_Base_64<Hash>> 
		 extends GrostlCore<Grostl64State, Hash> {
	
	protected final long[] A = new long[16],
		       			   B = new long[16],
		       			   C = new long[16];
	
	private long total;

	public Grostl_Base_64(int out)
	{
		super(128, out);
		reset();
	}
	
	public void reset()
	{
		super.reset();
		total = 0;
		Arrays.fill(A, 0);
		A[15] = this.outputLength() << 3;
	}
	
	private void P1()
	{
		for(int i = 0; i < 14; i++) 
		{
			B[0 ] ^= (long) (	    i) << 56;
			B[1 ] ^= (long) (0x10 + i) << 56;
			B[2 ] ^= (long) (0x20 + i) << 56;
			B[3 ] ^= (long) (0x30 + i) << 56;
			B[4 ] ^= (long) (0x40 + i) << 56;
			B[5 ] ^= (long) (0x50 + i) << 56;
			B[6 ] ^= (long) (0x60 + i) << 56;
			B[7 ] ^= (long) (0x70 + i) << 56;
			B[8 ] ^= (long) (0x80 + i) << 56;
			B[9 ] ^= (long) (0x90 + i) << 56;
			B[10] ^= (long) (0xA0 + i) << 56;
			B[11] ^= (long) (0xB0 + i) << 56;
			B[12] ^= (long) (0xC0 + i) << 56;
			B[13] ^= (long) (0xD0 + i) << 56;
			B[14] ^= (long) (0xE0 + i) << 56;
			B[15] ^= (long) (0xF0 + i) << 56;
			long t0 = PCUBE[0][(int) (B[0 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[1 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[2 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[3 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[4 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[5 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[6 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[11] 		& 0xFF];
			long t1 = PCUBE[0][(int) (B[1 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[2 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[3 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[4 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[5 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[6 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[7 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[12] 		& 0xFF];
			long t2 = PCUBE[0][(int) (B[2 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[3 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[4 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[5 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[6 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[7 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[8 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[13] 		& 0xFF];
			long t3 = PCUBE[0][(int) (B[3 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[4 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[5 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[6 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[7 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[8 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[9 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[14] 		& 0xFF];
			long t4 = PCUBE[0][(int) (B[4 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[5 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[6 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[7 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[8 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[9 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[10] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[15] 		& 0xFF];
			long t5 = PCUBE[0][(int) (B[5 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[6 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[7 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[8 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[9 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[10] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[11] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[0 ] 		& 0xFF];
			long t6 = PCUBE[0][(int) (B[6 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[7 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[8 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[9 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[10] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[11] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[12] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[1 ] 		& 0xFF];
			long t7 = PCUBE[0][(int) (B[7 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[8 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[9 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[10] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[11] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[12] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[13] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[2 ] 		& 0xFF];
			long t8 = PCUBE[0][(int) (B[8 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[9 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[10] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[11] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[12] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[13] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[14] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[3 ] 		& 0xFF];
			long t9 = PCUBE[0][(int) (B[9 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[10] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[11] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[12] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[13] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[14] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[15] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[4 ] 		& 0xFF];
			long tA = PCUBE[0][(int) (B[10] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[11] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[12] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[13] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[14] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[15] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[0 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[5 ] 		& 0xFF];
			long tB = PCUBE[0][(int) (B[11] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[12] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[13] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[14] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[15] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[0 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[1 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[6 ] 		& 0xFF];
			long tC = PCUBE[0][(int) (B[12] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[13] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[14] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[15] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[0 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[1 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[2 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[7 ] 		& 0xFF];
			long tD = PCUBE[0][(int) (B[13] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[14] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[15] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[0 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[1 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[2 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[3 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[8 ] 		& 0xFF];
			long tE = PCUBE[0][(int) (B[14] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[15] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[0 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[1 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[2 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[3 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[4 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[9 ] 		& 0xFF];
			long tF = PCUBE[0][(int) (B[15] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (B[0 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (B[1 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (B[2 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (B[3 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (B[4 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (B[5 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  B[10] 		& 0xFF];
			B[0 ] = t0;
			B[1 ] = t1;
			B[2 ] = t2;
			B[3 ] = t3;
			B[4 ] = t4;
			B[5 ] = t5;
			B[6 ] = t6;
			B[7 ] = t7;
			B[8 ] = t8;
			B[9 ] = t9;
			B[10] = tA;
			B[11] = tB;
			B[12] = tC;
			B[13] = tD;
			B[14] = tE;
			B[15] = tF;
		}
	}

	private void P2()
	{
		for(int i = 0; i < 14; i++) {
			C[0 ] ^= (long) i ^ -0x01L;
			C[1 ] ^= (long) i ^ -0x11L;
			C[2 ] ^= (long) i ^ -0x21L;
			C[3 ] ^= (long) i ^ -0x31L;
			C[4 ] ^= (long) i ^ -0x41L;
			C[5 ] ^= (long) i ^ -0x51L;
			C[6 ] ^= (long) i ^ -0x61L;
			C[7 ] ^= (long) i ^ -0x71L;
			C[8 ] ^= (long) i ^ -0x81L;
			C[9 ] ^= (long) i ^ -0x91L;
			C[10] ^= (long) i ^ -0xA1L;
			C[11] ^= (long) i ^ -0xB1L;
			C[12] ^= (long) i ^ -0xC1L;
			C[13] ^= (long) i ^ -0xD1L;
			C[14] ^= (long) i ^ -0xE1L;
			C[15] ^= (long) i ^ -0xF1L;
			long t0 = PCUBE[0][(int) (C[1 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[3 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[5 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[11] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[0 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[2 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[4 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[6 ] 		& 0xFF];
			long t1 = PCUBE[0][(int) (C[2 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[4 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[6 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[12] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[1 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[3 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[5 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[7 ] 		& 0xFF];
			long t2 = PCUBE[0][(int) (C[3 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[5 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[7 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[13] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[2 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[4 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[6 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[8 ] 		& 0xFF];
			long t3 = PCUBE[0][(int) (C[4 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[6 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[8 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[14] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[3 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[5 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[7 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[9 ] 		& 0xFF];
			long t4 = PCUBE[0][(int) (C[5 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[7 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[9 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[15] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[4 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[6 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[8 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[10] 		& 0xFF];
			long t5 = PCUBE[0][(int) (C[6 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[8 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[10] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[0 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[5 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[7 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[9 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[11] 		& 0xFF];
			long t6 = PCUBE[0][(int) (C[7 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[9 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[11] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[1 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[6 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[8 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[10] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[12] 		& 0xFF];
			long t7 = PCUBE[0][(int) (C[8 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[10] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[12] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[2 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[7 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[9 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[11] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[13] 		& 0xFF];
			long t8 = PCUBE[0][(int) (C[9 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[11] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[13] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[3 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[8 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[10] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[12] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[14] 		& 0xFF];
			long t9 = PCUBE[0][(int) (C[10] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[12] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[14] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[4 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[9 ] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[11] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[13] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[15] 		& 0xFF];
			long tA = PCUBE[0][(int) (C[11] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[13] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[15] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[5 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[10] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[12] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[14] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[0 ] 		& 0xFF];
			long tB = PCUBE[0][(int) (C[12] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[14] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[0 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[6 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[11] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[13] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[15] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[1 ] 		& 0xFF];
			long tC = PCUBE[0][(int) (C[13] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[15] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[1 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[7 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[12] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[14] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[0 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[2 ] 		& 0xFF];
			long tD = PCUBE[0][(int) (C[14] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[0 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[2 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[8 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[13] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[15] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[1 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[3 ] 		& 0xFF];
			long tE = PCUBE[0][(int) (C[15] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[1 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[3 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[9 ] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[14] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[0 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[2 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[4 ] 		& 0xFF];
			long tF = PCUBE[0][(int) (C[0 ] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[2 ] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[4 ] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[10] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[15] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[1 ] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[3 ] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[5 ] 		& 0xFF];
			C[0 ] = t0;
			C[1 ] = t1;
			C[2 ] = t2;
			C[3 ] = t3;
			C[4 ] = t4;
			C[5 ] = t5;
			C[6 ] = t6;
			C[7 ] = t7;
			C[8 ] = t8;
			C[9 ] = t9;
			C[10] = tA;
			C[11] = tB;
			C[12] = tC;
			C[13] = tD;
			C[14] = tE;
			C[15] = tF;
		}
	}

	public void processNext(byte[] bytes, int offset)
	{
		this.processNext(bytes, offset, true);
	}
	
	public void processNext(byte[] bytes, int offset, boolean count)
	{
		if(count)
			total++;
		Bits.BigEndian.bytesToLongs(bytes, offset, C, 0, 16);
		for(int i = 0; i < 16; i++)
			B[i] = C[i] ^ A[i];
		P1();
		P2();
		for(int i = 0; i < 16; i++)
			A[i] ^= B[i] ^ C[i];
	}

	protected abstract void output(byte[] remaining, int pos, int max);
	
	public void finalize(byte[] remaining, int pos, byte[] out, int start, int max)
	{
		Arrays.fill(remaining, pos, 128, (byte) 0);
		remaining[pos] = (byte) 0x80;
		if(pos >= 120)
		{
			processNext(remaining, 0);
			Arrays.fill(remaining, (byte) 0);
		}
		total++;
		Bits.BigEndian.longToBytes(total, remaining, 120);
		processNext(remaining, 0, false);
		System.arraycopy(A, 0, B, 0, 16);
		P1();
		output(out, start, max);
		reset();
	}
	
	public Grostl64State getState()
	{
		return new Grostl64State(this);
	}

	public void updateState(Grostl64State state)
	{
		state.update(this);
	}

	public void loadCustom(Grostl64State state)
	{
		System.arraycopy(state.A, 0, this.A, 0, 16);
		System.arraycopy(state.B, 0, this.B, 0, 16);
		System.arraycopy(state.C, 0, this.C, 0, 16);
		this.total = state.total;
	}
	
	public static final Grostl64StateFactory sfactory = new Grostl64StateFactory();
	
	protected static final class Grostl64State extends MerkleState<Grostl64State, Grostl_Base_64<? extends Grostl_Base_64<?>>>
	{
		protected long[] A,
						 B,
						 C;
		
		protected long total;
		
		public Grostl64State(byte[] bytes, int pos) 
		{
			super(bytes, pos);
		}
		
		public Grostl64State(Grostl_Base_64<?> stl) 
		{
			super(stl);
		}

		public Factory<Grostl64State> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.GROSTL64STATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeLongs(A);
			os.writeLongs(B);
			os.writeLongs(C);
			os.writeLong(total);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.longsToBytes(A, 0, bytes, start, 16); start += 128;
			Bits.longsToBytes(B, 0, bytes, start, 16); start += 128;
			Bits.longsToBytes(C, 0, bytes, start, 16); start += 128;
			Bits.longToBytes(total, bytes, start);
		}

		protected void addCustom(IIncomingStream os) throws IOException
		{
			A = os.readLongs(16);
			B = os.readLongs(16);
			C = os.readLongs(16);
			total = os.readLong();
		}

		protected void addCustom(byte[] bytes, int start)
		{
			A = new long[16];
			B = new long[16];
			C = new long[16];
			Bits.bytesToLongs(bytes, start, A, 0, 16); start += 128;
			Bits.bytesToLongs(bytes, start, B, 0, 16); start += 128;
			Bits.bytesToLongs(bytes, start, C, 0, 16); start += 128;
			total = Bits.longFromBytes(bytes, start);
		}

		protected void addCustom(Grostl_Base_64<? extends Grostl_Base_64<?>> hash)
		{
			A = ArrayUtil.copy(hash.A);
			B = ArrayUtil.copy(hash.B);
			C = ArrayUtil.copy(hash.C);
			total = hash.total;
		}

		protected void updateCustom(Grostl_Base_64<? extends Grostl_Base_64<?>> hash)
		{
			if(A == null)
				A = ArrayUtil.copy(hash.A);
			else
				System.arraycopy(hash.A, 0, A, 0, 16);
			if(B == null)
				B = ArrayUtil.copy(hash.B);
			else
				System.arraycopy(hash.B, 0, B, 0, 16);
			if(C == null)
				C = ArrayUtil.copy(hash.C);
			else
				System.arraycopy(hash.C, 0, C, 0, 16);
			total = hash.total;
		}

		protected void eraseCustom()
		{
			total = 0;
			Arrays.fill(A, 0);
			Arrays.fill(B, 0);
			Arrays.fill(C, 0);
			A = B = C = null;
		}

		protected boolean compareCustom(Grostl64State state)
		{
			return (state.total == total && ArrayUtil.equals(A, state.A)) && (ArrayUtil.equals(B, state.B) && ArrayUtil.equals(C, state.C));
		}

		protected int customSize()
		{
			return 392;
		}

		public int stateID()
		{
			return IState.GROSTL_64;
		}
		
	}
	
	protected static final class Grostl64StateFactory extends MerkleStateFactory<Grostl64State, Grostl_Base_64<? extends Grostl_Base_64<?>>>
	{

		protected Grostl64StateFactory() 
		{
			super(Grostl64State.class, 128);
		}

		protected Grostl64State construct(byte[] bytes, int pos)
		{
			return new Grostl64State(bytes, pos);
		}
		
	}


}

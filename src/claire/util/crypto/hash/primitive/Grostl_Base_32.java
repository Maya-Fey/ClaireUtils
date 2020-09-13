package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.Grostl_Base_32.Grostl32State;
import claire.util.crypto.hash.primitive.MerkleHash.MerkleState;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

abstract class Grostl_Base_32<Hash extends Grostl_Base_32<Hash>> 
		 extends GrostlCore<Grostl32State, Hash> {
	
	protected final long[] A = new long[8],
					       B = new long[8],
					       C = new long[8];
	
	protected long total;

	public Grostl_Base_32(int out) 
	{
		super(64, out);
		reset();
	}
	
	public void reset()
	{
		super.reset();
		total = 0;
		Arrays.fill(A, 0);
		A[7] = this.outputLength() << 3;
	}
	
	private void P1()
	{
		for(int i = 0; i < 10;)
		{
			B[0] ^= (long) (       i  ) << 56;
			B[1] ^= (long) (0x10 + i  ) << 56;
			B[2] ^= (long) (0x20 + i  ) << 56;
			B[3] ^= (long) (0x30 + i  ) << 56;
			B[4] ^= (long) (0x40 + i  ) << 56;
			B[5] ^= (long) (0x50 + i  ) << 56;
			B[6] ^= (long) (0x60 + i  ) << 56;
			B[7] ^= (long) (0x70 + i++) << 56;
			long t0 = PCUBE[0][(int) (B[0] >>> 56) & 0xFF]
				    ^ PCUBE[1][(int) (B[1] >>> 48) & 0xFF]
				    ^ PCUBE[2][(int) (B[2] >>> 40) & 0xFF]
				    ^ PCUBE[3][(int) (B[3] >>> 32) & 0xFF]
				    ^ PCUBE[4][(int) (B[4] >>> 24) & 0xFF]
				    ^ PCUBE[5][(int) (B[5] >>> 16) & 0xFF]
				    ^ PCUBE[6][(int) (B[6] >>> 8 ) & 0xFF]
				    ^ PCUBE[7][(int)  B[7] 		   & 0xFF];
			long t1 = PCUBE[0][(int) (B[1] >>> 56) & 0xFF]
				    ^ PCUBE[1][(int) (B[2] >>> 48) & 0xFF]
				    ^ PCUBE[2][(int) (B[3] >>> 40) & 0xFF]
				    ^ PCUBE[3][(int) (B[4] >>> 32) & 0xFF]
				    ^ PCUBE[4][(int) (B[5] >>> 24) & 0xFF]
				    ^ PCUBE[5][(int) (B[6] >>> 16) & 0xFF]
				    ^ PCUBE[6][(int) (B[7] >>> 8 ) & 0xFF]
				    ^ PCUBE[7][(int)  B[0] 		   & 0xFF];
			long t2 = PCUBE[0][(int) (B[2] >>> 56) & 0xFF]
				    ^ PCUBE[1][(int) (B[3] >>> 48) & 0xFF]
				    ^ PCUBE[2][(int) (B[4] >>> 40) & 0xFF]
				    ^ PCUBE[3][(int) (B[5] >>> 32) & 0xFF]
				    ^ PCUBE[4][(int) (B[6] >>> 24) & 0xFF]
				    ^ PCUBE[5][(int) (B[7] >>> 16) & 0xFF]
				    ^ PCUBE[6][(int) (B[0] >>> 8 ) & 0xFF]
				    ^ PCUBE[7][(int)  B[1] 		   & 0xFF];
			long t3 = PCUBE[0][(int) (B[3] >>> 56) & 0xFF]
				    ^ PCUBE[1][(int) (B[4] >>> 48) & 0xFF]
				    ^ PCUBE[2][(int) (B[5] >>> 40) & 0xFF]
				    ^ PCUBE[3][(int) (B[6] >>> 32) & 0xFF]
				    ^ PCUBE[4][(int) (B[7] >>> 24) & 0xFF]
				    ^ PCUBE[5][(int) (B[0] >>> 16) & 0xFF]
				    ^ PCUBE[6][(int) (B[1] >>> 8 ) & 0xFF]
				    ^ PCUBE[7][(int)  B[2] 		   & 0xFF];
			long t4 = PCUBE[0][(int) (B[4] >>> 56) & 0xFF]
				    ^ PCUBE[1][(int) (B[5] >>> 48) & 0xFF]
				    ^ PCUBE[2][(int) (B[6] >>> 40) & 0xFF]
				    ^ PCUBE[3][(int) (B[7] >>> 32) & 0xFF]
				    ^ PCUBE[4][(int) (B[0] >>> 24) & 0xFF]
				    ^ PCUBE[5][(int) (B[1] >>> 16) & 0xFF]
				    ^ PCUBE[6][(int) (B[2] >>> 8 ) & 0xFF]
				    ^ PCUBE[7][(int)  B[3] 		   & 0xFF];
			long t5 = PCUBE[0][(int) (B[5] >>> 56) & 0xFF]
				    ^ PCUBE[1][(int) (B[6] >>> 48) & 0xFF]
				    ^ PCUBE[2][(int) (B[7] >>> 40) & 0xFF]
				    ^ PCUBE[3][(int) (B[0] >>> 32) & 0xFF]
				    ^ PCUBE[4][(int) (B[1] >>> 24) & 0xFF]
				    ^ PCUBE[5][(int) (B[2] >>> 16) & 0xFF]
				    ^ PCUBE[6][(int) (B[3] >>> 8 ) & 0xFF]
				    ^ PCUBE[7][(int)  B[4] 		   & 0xFF];
			long t6 = PCUBE[0][(int) (B[6] >>> 56) & 0xFF]
				    ^ PCUBE[1][(int) (B[7] >>> 48) & 0xFF]
				    ^ PCUBE[2][(int) (B[0] >>> 40) & 0xFF]
				    ^ PCUBE[3][(int) (B[1] >>> 32) & 0xFF]
				    ^ PCUBE[4][(int) (B[2] >>> 24) & 0xFF]
				    ^ PCUBE[5][(int) (B[3] >>> 16) & 0xFF]
				    ^ PCUBE[6][(int) (B[4] >>> 8 ) & 0xFF]
				    ^ PCUBE[7][(int)  B[5] 		   & 0xFF];
			long t7 = PCUBE[0][(int) (B[7] >>> 56) & 0xFF]
				    ^ PCUBE[1][(int) (B[0] >>> 48) & 0xFF]
				    ^ PCUBE[2][(int) (B[1] >>> 40) & 0xFF]
				    ^ PCUBE[3][(int) (B[2] >>> 32) & 0xFF]
				    ^ PCUBE[4][(int) (B[3] >>> 24) & 0xFF]
				    ^ PCUBE[5][(int) (B[4] >>> 16) & 0xFF]
				    ^ PCUBE[6][(int) (B[5] >>> 8 ) & 0xFF]
				    ^ PCUBE[7][(int)  B[6] 		   & 0xFF];
			t0 ^= (long) (       i  ) << 56;
			t1 ^= (long) (0x10 + i  ) << 56;
			t2 ^= (long) (0x20 + i  ) << 56;
			t3 ^= (long) (0x30 + i  ) << 56;
			t4 ^= (long) (0x40 + i  ) << 56;
			t5 ^= (long) (0x50 + i  ) << 56;
			t6 ^= (long) (0x60 + i  ) << 56;
			t7 ^= (long) (0x70 + i++) << 56;
			B[0] = PCUBE[0][(int) (t0 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t1 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t2 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t3 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t4 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t5 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t6 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t7 		  & 0xFF];
			B[1] = PCUBE[0][(int) (t1 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t2 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t3 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t4 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t5 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t6 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t7 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t0 		  & 0xFF];
			B[2] = PCUBE[0][(int) (t2 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t3 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t4 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t5 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t6 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t7 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t0 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t1 		  & 0xFF];
			B[3] = PCUBE[0][(int) (t3 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t4 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t5 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t6 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t7 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t0 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t1 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t2 		  & 0xFF];
			B[4] = PCUBE[0][(int) (t4 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t5 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t6 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t7 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t0 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t1 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t2 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t3 		  & 0xFF];
			B[5] = PCUBE[0][(int) (t5 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t6 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t7 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t0 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t1 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t2 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t3 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t4 		  & 0xFF];
			B[6] = PCUBE[0][(int) (t6 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t7 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t0 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t1 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t2 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t3 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t4 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t5 		  & 0xFF];
			B[7] = PCUBE[0][(int) (t7 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t0 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t1 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t2 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t3 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t4 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t5 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t6 		  & 0xFF];			
		}
	}
	
	private void P2()
	{
		for(int i = 0; i < 10;) {
			C[0] ^= (long) i   ^ -0x01L;
			C[1] ^= (long) i   ^ -0x11L;
			C[2] ^= (long) i   ^ -0x21L;
			C[3] ^= (long) i   ^ -0x31L;
			C[4] ^= (long) i   ^ -0x41L;
			C[5] ^= (long) i   ^ -0x51L;
			C[6] ^= (long) i   ^ -0x61L;
			C[7] ^= (long) i++ ^ -0x71L;
			long t0 = PCUBE[0][(int) (C[1] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[3] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[5] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[7] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[0] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[2] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[4] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[6] 		   & 0xFF];
			long t1 = PCUBE[0][(int) (C[2] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[4] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[6] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[0] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[1] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[3] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[5] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[7] 		   & 0xFF];
			long t2 = PCUBE[0][(int) (C[3] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[5] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[7] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[1] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[2] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[4] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[6] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[0] 		   & 0xFF];
			long t3 = PCUBE[0][(int) (C[4] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[6] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[0] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[2] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[3] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[5] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[7] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[1] 		   & 0xFF];
			long t4 = PCUBE[0][(int) (C[5] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[7] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[1] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[3] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[4] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[6] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[0] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[2] 		   & 0xFF];
			long t5 = PCUBE[0][(int) (C[6] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[0] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[2] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[4] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[5] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[7] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[1] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[3] 		   & 0xFF];
			long t6 = PCUBE[0][(int) (C[7] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[1] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[3] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[5] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[6] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[0] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[2] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[4] 		   & 0xFF];
			long t7 = PCUBE[0][(int) (C[0] >>> 56) & 0xFF]
					^ PCUBE[1][(int) (C[2] >>> 48) & 0xFF]
					^ PCUBE[2][(int) (C[4] >>> 40) & 0xFF]
					^ PCUBE[3][(int) (C[6] >>> 32) & 0xFF]
					^ PCUBE[4][(int) (C[7] >>> 24) & 0xFF]
					^ PCUBE[5][(int) (C[1] >>> 16) & 0xFF]
					^ PCUBE[6][(int) (C[3] >>> 8 ) & 0xFF]
					^ PCUBE[7][(int)  C[5] 		   & 0xFF];
			t0 ^= (long) (i  ) ^ -0x01L;
			t1 ^= (long) (i  ) ^ -0x11L;
			t2 ^= (long) (i  ) ^ -0x21L;
			t3 ^= (long) (i  ) ^ -0x31L;
			t4 ^= (long) (i  ) ^ -0x41L;
			t5 ^= (long) (i  ) ^ -0x51L;
			t6 ^= (long) (i  ) ^ -0x61L;
			t7 ^= (long) (i++) ^ -0x71L;
			C[0] = PCUBE[0][(int) (t1 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t3 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t5 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t7 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t0 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t2 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t4 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t6 		  & 0xFF];
			C[1] = PCUBE[0][(int) (t2 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t4 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t6 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t0 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t1 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t3 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t5 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t7 		  & 0xFF];
			C[2] = PCUBE[0][(int) (t3 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t5 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t7 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t1 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t2 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t4 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t6 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t0 		  & 0xFF];
			C[3] = PCUBE[0][(int) (t4 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t6 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t0 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t2 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t3 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t5 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t7 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t1 		  & 0xFF];
			C[4] = PCUBE[0][(int) (t5 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t7 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t1 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t3 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t4 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t6 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t0 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t2 		  & 0xFF];
			C[5] = PCUBE[0][(int) (t6 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t0 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t2 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t4 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t5 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t7 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t1 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t3 		  & 0xFF];
			C[6] = PCUBE[0][(int) (t7 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t1 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t3 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t5 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t6 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t0 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t2 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t4 		  & 0xFF];
			C[7] = PCUBE[0][(int) (t0 >>> 56) & 0xFF]
				 ^ PCUBE[1][(int) (t2 >>> 48) & 0xFF]
				 ^ PCUBE[2][(int) (t4 >>> 40) & 0xFF]
				 ^ PCUBE[3][(int) (t6 >>> 32) & 0xFF]
				 ^ PCUBE[4][(int) (t7 >>> 24) & 0xFF]
				 ^ PCUBE[5][(int) (t1 >>> 16) & 0xFF]
				 ^ PCUBE[6][(int) (t3 >>> 8 ) & 0xFF]
				 ^ PCUBE[7][(int)  t5 		  & 0xFF];
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
		Bits.BigEndian.bytesToLongs(bytes, offset, C, 0, 8);
		for(int i = 0; i < 8; i++)
			B[i] = C[i] ^ A[i];
		P1();
		P2();
		for(int i = 0; i < 8; i++)
			A[i] ^= B[i] ^ C[i];
	}
	
	protected abstract void output(byte[] out, int start, int max);

	public void finalize(byte[] remaining, int pos, byte[] out, int start, int max)
	{
		Arrays.fill(remaining, pos, 64, (byte) 0);
		remaining[pos] = (byte) 0x80;
		if(pos >= 56)
		{
			processNext(remaining, 0);
			Arrays.fill(remaining, (byte) 0);
		}
		total++;
		Bits.BigEndian.longToBytes(total, remaining, 56);
		processNext(remaining, 0, false);
		System.arraycopy(A, 0, B, 0, 8);
		P1();
		output(out, start, max);
		reset();
	}
	
	public Grostl32State getState()
	{
		return new Grostl32State(this);
	}

	public void updateState(Grostl32State state)
	{
		state.update(this);
	}

	public void loadCustom(Grostl32State state)
	{
		System.arraycopy(state.A, 0, this.A, 0, 8);
		System.arraycopy(state.B, 0, this.B, 0, 8);
		System.arraycopy(state.C, 0, this.C, 0, 8);
		total = state.total;
	}
	
	public static final Grostl32StateFactory sfactory = new Grostl32StateFactory();
	
	protected static final class Grostl32State extends MerkleState<Grostl32State, Grostl_Base_32<? extends Grostl_Base_32<?>>>
	{
		protected long[] A,
						 B,
						 C;
		
		protected long total;
		
		public Grostl32State(byte[] bytes, int pos) 
		{
			super(bytes, pos);
		}
		
		public Grostl32State(Grostl_Base_32<?> stl) 
		{
			super(stl);
		}

		public Factory<Grostl32State> factory()
		{
			return sfactory;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.GROSTL32STATE;
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
			Bits.longsToBytes(A, 0, bytes, start, 8); start += 64;
			Bits.longsToBytes(B, 0, bytes, start, 8); start += 64;
			Bits.longsToBytes(C, 0, bytes, start, 8); start += 64;
			Bits.longToBytes(total, bytes, start);
		}

		protected void addCustom(IIncomingStream os) throws IOException
		{
			A = os.readLongs(8);
			B = os.readLongs(8);
			C = os.readLongs(8);
			total = os.readLong();
		}

		protected void addCustom(byte[] bytes, int start)
		{
			A = new long[8];
			B = new long[8];
			C = new long[8];
			Bits.bytesToLongs(bytes, start, A, 0, 8); start += 64;
			Bits.bytesToLongs(bytes, start, B, 0, 8); start += 64;
			Bits.bytesToLongs(bytes, start, C, 0, 8); start += 64;
			total = Bits.longFromBytes(bytes, start);
		}

		protected void addCustom(Grostl_Base_32<? extends Grostl_Base_32<?>> hash)
		{
			A = ArrayUtil.copy(hash.A);
			B = ArrayUtil.copy(hash.B);
			C = ArrayUtil.copy(hash.C);
			total = hash.total;
		}

		protected void updateCustom(Grostl_Base_32<? extends Grostl_Base_32<?>> hash)
		{
			if(A == null)
				A = ArrayUtil.copy(hash.A);
			else
				System.arraycopy(hash.A, 0, A, 0, 8);
			if(B == null)
				B = ArrayUtil.copy(hash.B);
			else
				System.arraycopy(hash.B, 0, B, 0, 8);
			if(C == null)
				C = ArrayUtil.copy(hash.C);
			else
				System.arraycopy(hash.C, 0, C, 0, 8);
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

		protected boolean compareCustom(Grostl32State state)
		{
			return (state.total == total && ArrayUtil.equals(A, state.A)) && (ArrayUtil.equals(B, state.B) && ArrayUtil.equals(C, state.C));
		}

		protected int customSize()
		{
			return 200;
		}

		public int stateID()
		{
			return IState.GROSTL_32;
		}
		
	}
	
	protected static final class Grostl32StateFactory extends MerkleStateFactory<Grostl32State, Grostl_Base_32<? extends Grostl_Base_32<?>>>
	{

		protected Grostl32StateFactory() 
		{
			super(Grostl32State.class, 64);
		}

		protected Grostl32State construct(byte[] bytes, int pos)
		{
			return new Grostl32State(bytes, pos);
		}
		
	}

}

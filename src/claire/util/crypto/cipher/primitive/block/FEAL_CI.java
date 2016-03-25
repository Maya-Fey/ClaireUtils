package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.cipher.key.block.KeyFEAL_CI;
import claire.util.crypto.rng.RandUtils;
import claire.util.encoding.Hex;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class FEAL_CI  
	   implements ISymmetric<KeyFEAL_CI> {
	
	private final short[] A = new short[4];
	private final short[] B = new short[4];
	private final short[] C = new short[4];
	private final short[] D = new short[4];
	
	private short[] schedule;
	
	protected KeyFEAL_CI key;
	private int rounds;
	private int estart;
	
	public FEAL_CI(KeyFEAL_CI key)
	{
		this.setKey(key);
	}

	public KeyFEAL_CI getKey()
	{
		return this.key;
	}

	private static short S1(short a, short b, short c, int r)
	{
		return Bits.rotateLeft((short) (a + b + c), r);
	}
	
	private static short S2(short a, short b, short c, int r)
	{
		return Bits.rotateRight((short) (a + b + c), r);
	}
	
	private static void FK(short[] A, short[] B)
	{
		short t;
		A[1] += A[0];
		A[2] += A[3];
		t = (short) (B[0] ^ A[2]);
		A[1] = S1(A[1], t, (short) 0xAB04, 13);
		t = (short) (A[1] ^ B[1]);
		A[2] = S2(A[2], t, (short) 0x42D7,  9);
		t = (short) (A[1] ^ B[2]);
		A[0] = S2(A[0], t, (short) 0xFFEA,  4);
		t = (short) (A[2] ^ B[3]);
		A[3] = S1(A[3], t, (short) 0x096F,  7);
	}
	
	private static void FR(short[] A, short[] B, short b1, short b2)
	{
		B[1] ^= b1;
		B[2] ^= b2;
		B[1] += B[0];
		B[2] += B[3];
		B[1] = S1(B[1], B[2], (short) (0xAB04 ^ B[0]), b1 & 15);
		B[2] = S2(B[2], B[1], (short) (0x42D7 ^ b1), B[3] & 15);
		B[0] = S2(B[0], B[1], (short) (0xFFEA ^ b2), B[0] & 15);
		B[3] = S1(B[3], B[2], (short) (0x096F ^ B[3]), b2 & 15);
		short t = A[0];
		A[0] = (short) (A[1] ^ B[0]);
		A[1] = (short) (A[2] ^ B[1]);
		A[2] = (short) (A[3] ^ B[2]);
		A[3] = (short) (t    ^ B[3]);
	}
	
	private static void FRI(short[] A, short[] B, short b2, short b1)
	{
		B[1] ^= b1;
		B[2] ^= b2;
		B[1] += B[0];
		B[2] += B[3];
		B[1] = S1(B[1], B[2], (short) (0xAB04 ^ B[0]), b1 & 15);
		B[2] = S2(B[2], B[1], (short) (0x42D7 ^ b1), B[3] & 15);
		B[0] = S2(B[0], B[1], (short) (0xFFEA ^ b2), B[0] & 15);
		B[3] = S1(B[3], B[2], (short) (0x096F ^ B[3]), b2 & 15);
		short t = A[3];
		A[3] = (short) (A[2] ^ B[2]);
		A[2] = (short) (A[1] ^ B[1]);
		A[1] = (short) (A[0] ^ B[0]);
		A[0] = (short) (t    ^ B[3]);
	}
	
	public void setKey(KeyFEAL_CI t)
	{
		this.key = t;
		rounds = t.getRounds();
		short[] key = t.getShorts();
		System.arraycopy(key, 0, A, 0, 4);
		System.arraycopy(key, 4, B, 0, 4);
		int req = 8 + t.getRounds();
		estart = t.getRounds() * 2;
		schedule = new short[req * 2];
		req -= 2;
		int i = 4;
		System.arraycopy(A, 0, C, 0, 4);
		FK(A, B);
		System.arraycopy(A, 0, schedule, 0, 4);
		short[] L = B,
			   R = A,
			   T;
		while(req > 0) {
			System.arraycopy(R, 0, D, 0, 4);
			D[0] ^= C[0];
			D[1] ^= C[1];
			D[2] ^= C[2];
			D[3] ^= C[3];
			System.arraycopy(L, 0, C, 0, 4);
			FK(L, D);
			if(req == 1) {
				System.arraycopy(L, 0, schedule, i, 2);
				break;
			} else
				System.arraycopy(L, 0, schedule, i, 4);
			i += 4;
			T = R;
			R = L;
			L = T;
			req -= 2;
		}
	}

	public void wipe()
	{
		Arrays.fill(A, (short) 0);
		Arrays.fill(B, (short) 0);
		Arrays.fill(C, (short) 0);
		Arrays.fill(D, (short) 0);
		Arrays.fill(schedule, (short) 0);
		key = null;
		schedule = null;
		rounds = estart = 0;
	}

	public int plaintextSize()
	{
		return 16;
	}
	
	public int ciphertextSize()
	{
		return 16;
	}	
	
	@SuppressWarnings("all")
	private static void printShorts(short[] A, short[] B)
	{
		System.out.println(Hex.toHexString(Bits.shortsToBytes(A)) + Hex.toHexString(Bits.shortsToBytes(B)));
	}
	
	public void decryptBlock(byte[] block, int start)
	{
		int r = rounds;
		int i = estart;
		int j = schedule.length;
		if((r & 1) == 1) {
			Bits.bytesToShorts(block, start + 0, B, 0, 4);
			Bits.bytesToShorts(block, start + 8, A, 0, 4);
			A[3] ^= schedule[--j];
			A[2] ^= schedule[--j];
			A[1] ^= schedule[--j];
			A[0] ^= schedule[--j];
			B[3] ^= schedule[--j];
			B[2] ^= schedule[--j];
			B[1] ^= schedule[--j];
			B[0] ^= schedule[--j];		
			System.arraycopy(B, 0, C, 0, 4);
			FRI(A, C, schedule[--i], schedule[--i]);
			r--;
		} else {
			Bits.bytesToShorts(block, start + 0, A, 0, 4);
			Bits.bytesToShorts(block, start + 8, B, 0, 4);
			B[3] ^= schedule[--j];
			B[2] ^= schedule[--j];
			B[1] ^= schedule[--j];
			B[0] ^= schedule[--j];
			A[3] ^= schedule[--j];
			A[2] ^= schedule[--j];
			A[1] ^= schedule[--j];
			A[0] ^= schedule[--j];		
		}
		while(r > 1) {
			System.arraycopy(A, 0, C, 0, 4);
			FRI(B, C, schedule[--i], schedule[--i]);
			System.arraycopy(B, 0, C, 0, 4);
			FRI(A, C, schedule[--i], schedule[--i]);
			r -= 2;
		}
		B[0] -= A[0];
		B[1] -= A[1];
		B[2] -= A[2];
		B[3] -= A[3];
		B[3] ^= schedule[--j];
		B[2] ^= schedule[--j];
		B[1] ^= schedule[--j];
		B[0] ^= schedule[--j];
		A[3] ^= schedule[--j];
		A[2] ^= schedule[--j];
		A[1] ^= schedule[--j];
		A[0] ^= schedule[--j];	
		Bits.shortsToBytes(A, 0, block, start + 0, 4);
		Bits.shortsToBytes(B, 0, block, start + 8, 4);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{	
		int r = rounds;
		int i = estart;
		int j = schedule.length;
		if((r & 1) == 1) {
			Bits.bytesToShorts(block, start0 + 0, B, 0, 4);
			Bits.bytesToShorts(block, start0 + 8, A, 0, 4);
			A[3] ^= schedule[--j];
			A[2] ^= schedule[--j];
			A[1] ^= schedule[--j];
			A[0] ^= schedule[--j];
			B[3] ^= schedule[--j];
			B[2] ^= schedule[--j];
			B[1] ^= schedule[--j];
			B[0] ^= schedule[--j];			
			System.arraycopy(B, 0, C, 0, 4);
			FRI(A, C, schedule[--i], schedule[--i]);
			r--;
		} else {
			Bits.bytesToShorts(block, start0 + 0, A, 0, 4);
			Bits.bytesToShorts(block, start0 + 8, B, 0, 4);
			B[3] ^= schedule[--j];
			B[2] ^= schedule[--j];
			B[1] ^= schedule[--j];
			B[0] ^= schedule[--j];
			A[3] ^= schedule[--j];
			A[2] ^= schedule[--j];
			A[1] ^= schedule[--j];
			A[0] ^= schedule[--j];
		}
		while(r > 1) {
			System.arraycopy(A, 0, C, 0, 4);
			FRI(B, C, schedule[--i], schedule[--i]);
			System.arraycopy(B, 0, C, 0, 4);
			FRI(A, C, schedule[--i], schedule[--i]);
			r -= 2;
		}
		B[0] -= A[0];
		B[1] -= A[1];
		B[2] -= A[2];
		B[3] -= A[3];
		B[3] ^= schedule[--j];
		B[2] ^= schedule[--j];
		B[1] ^= schedule[--j];
		B[0] ^= schedule[--j];
		A[3] ^= schedule[--j];
		A[2] ^= schedule[--j];
		A[1] ^= schedule[--j];
		A[0] ^= schedule[--j];	
		Bits.shortsToBytes(A, 0, out, start1 + 0, 4);
		Bits.shortsToBytes(B, 0, out, start1 + 8, 4);
	}

	public void encryptBlock(byte[] block, final int start)
	{			
		int r = rounds;
		int i = 0;
		int j = estart;
		Bits.bytesToShorts(block, start + 0, A, 0, 4);
		Bits.bytesToShorts(block, start + 8, B, 0, 4);
		A[0] ^= schedule[j++];
		A[1] ^= schedule[j++];
		A[2] ^= schedule[j++];
		A[3] ^= schedule[j++];
		B[0] ^= schedule[j++];
		B[1] ^= schedule[j++];
		B[2] ^= schedule[j++];
		B[3] ^= schedule[j++];
		B[0] += A[0];
		B[1] += A[1];
		B[2] += A[2];
		B[3] += A[3];
		while(r > 1) {
			System.arraycopy(B, 0, C, 0, 4);
			FR(A, C, schedule[i++], schedule[i++]);
			System.arraycopy(A, 0, C, 0, 4);
			FR(B, C, schedule[i++], schedule[i++]);
			r -= 2;
		}
		if(r == 1) {
			System.arraycopy(B, 0, C, 0, 4);
			FR(A, C, schedule[i++], schedule[i++]);
			B[0] ^= schedule[j++];
			B[1] ^= schedule[j++];
			B[2] ^= schedule[j++];
			B[3] ^= schedule[j++];
			A[0] ^= schedule[j++];
			A[1] ^= schedule[j++];
			A[2] ^= schedule[j++];
			A[3] ^= schedule[j++];
			Bits.shortsToBytes(B, 0, block, start + 0, 4);
			Bits.shortsToBytes(A, 0, block, start + 8, 4);
		} else {
			A[0] ^= schedule[j++];
			A[1] ^= schedule[j++];
			A[2] ^= schedule[j++];
			A[3] ^= schedule[j++];
			B[0] ^= schedule[j++];
			B[1] ^= schedule[j++];
			B[2] ^= schedule[j++];
			B[3] ^= schedule[j++];
			Bits.shortsToBytes(A, 0, block, start + 0, 4);
			Bits.shortsToBytes(B, 0, block, start + 8, 4);
		}
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int r = rounds;
		int i = 0;
		int j = estart;
		Bits.bytesToShorts(block, start0 + 0, A, 0, 4);
		Bits.bytesToShorts(block, start0 + 8, B, 0, 4);
		A[0] ^= schedule[j++];
		A[1] ^= schedule[j++];
		A[2] ^= schedule[j++];
		A[3] ^= schedule[j++];
		B[0] ^= schedule[j++];
		B[1] ^= schedule[j++];
		B[2] ^= schedule[j++];
		B[3] ^= schedule[j++];
		B[0] += A[0];
		B[1] += A[1];
		B[2] += A[2];
		B[3] += A[3];
		while(r > 1) {
			System.arraycopy(B, 0, C, 0, 4);
			FR(A, C, schedule[i++], schedule[i++]);
			System.arraycopy(A, 0, C, 0, 4);
			FR(B, C, schedule[i++], schedule[i++]);
			r -= 2;
		}
		if(r == 1) {
			System.arraycopy(B, 0, C, 0, 4);
			FR(A, C, schedule[i++], schedule[i++]);
			B[0] ^= schedule[j++];
			B[1] ^= schedule[j++];
			B[2] ^= schedule[j++];
			B[3] ^= schedule[j++];
			A[0] ^= schedule[j++];
			A[1] ^= schedule[j++];
			A[2] ^= schedule[j++];
			A[3] ^= schedule[j++];
			Bits.shortsToBytes(B, 0, out, start1 + 0, 4);
			Bits.shortsToBytes(A, 0, out, start1 + 8, 4);
		} else {
			A[0] ^= schedule[j++];
			A[1] ^= schedule[j++];
			A[2] ^= schedule[j++];
			A[3] ^= schedule[j++];
			B[0] ^= schedule[j++];
			B[1] ^= schedule[j++];
			B[2] ^= schedule[j++];
			B[3] ^= schedule[j++];
			Bits.shortsToBytes(A, 0, out, start1 + 0, 4);
			Bits.shortsToBytes(B, 0, out, start1 + 8, 4);
		}
	}
	
	public void reset() {}
	
	public static final int test()
	{
		final short[] shorts1 = new short[8];
		final short[] shorts2 = new short[8];
		RandUtils.fillArr(shorts1);
		RandUtils.fillArr(shorts2);
		KeyFEAL_CI a1 = new KeyFEAL_CI(shorts1, 31);
		KeyFEAL_CI a2 = new KeyFEAL_CI(shorts2, 48);
		FEAL_CI aes = new FEAL_CI(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

}

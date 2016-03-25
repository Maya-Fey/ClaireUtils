package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.cipher.key.block.KeyFEAL;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class FEAL  
	   implements ISymmetric<KeyFEAL> {
	
	private final byte[] A = new byte[4];
	private final byte[] B = new byte[4];
	private final byte[] C = new byte[4];
	private final byte[] D = new byte[4];
	
	private byte[] schedule;
	
	protected KeyFEAL key;
	private int rounds;
	private int estart;
	
	public FEAL(KeyFEAL key)
	{
		this.setKey(key);
	}

	public KeyFEAL getKey()
	{
		return this.key;
	}

	private static byte S(byte a, byte b, byte c)
	{
		return Bits.rotateLeft((byte) (a + b + c), 2);
	}
	
	private static void FK(byte[] A, byte[] B)
	{
		byte t;
		A[1] ^= A[0];
		A[2] ^= A[3];
		t = (byte) (B[0] ^ A[2]);
		A[1] = S(A[1], t, (byte) 1);
		t = (byte) (A[1] ^ B[1]);
		A[2] = S(A[2], t, (byte) 0);
		t = (byte) (A[1] ^ B[2]);
		A[0] = S(A[0], t, (byte) 0);
		t = (byte) (A[2] ^ B[3]);
		A[3] = S(A[3], t, (byte) 1);
	}
	
	private static void FR(byte[] A, byte[] B, byte b1, byte b2)
	{
		B[1] ^= b1;
		B[2] ^= b2;
		B[1] ^= B[0];
		B[2] ^= B[3];
		B[1] = S(B[1], B[2], (byte) 1);
		B[2] = S(B[2], B[1], (byte) 0);
		B[0] = S(B[0], B[1], (byte) 0);
		B[3] = S(B[3], B[2], (byte) 1);
		A[0] ^= B[0];
		A[1] ^= B[1];
		A[2] ^= B[2];
		A[3] ^= B[3];
	}
	
	private static void FRI(byte[] A, byte[] B, byte b1, byte b2)
	{
		B[1] ^= b2;
		B[2] ^= b1;
		B[1] ^= B[0];
		B[2] ^= B[3];
		B[1] = S(B[1], B[2], (byte) 1);
		B[2] = S(B[2], B[1], (byte) 0);
		B[0] = S(B[0], B[1], (byte) 0);
		B[3] = S(B[3], B[2], (byte) 1);
		A[0] ^= B[0];
		A[1] ^= B[1];
		A[2] ^= B[2];
		A[3] ^= B[3];
	}
	
	public void setKey(KeyFEAL t)
	{
		this.key = t;
		rounds = t.getRounds();
		byte[] key = t.getBytes();
		System.arraycopy(key, 0, A, 0, 4);
		System.arraycopy(key, 4, B, 0, 4);
		int req = 8 + t.getRounds();
		estart = t.getRounds() * 2;
		schedule = new byte[req * 2];
		req -= 2;
		int i = 4;
		System.arraycopy(A, 0, C, 0, 4);
		FK(A, B);
		System.arraycopy(A, 0, schedule, 0, 4);
		byte[] L = B,
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
		Arrays.fill(A, (byte) 0);
		Arrays.fill(B, (byte) 0);
		Arrays.fill(C, (byte) 0);
		Arrays.fill(D, (byte) 0);
		Arrays.fill(schedule, (byte) 0);
		key = null;
		schedule = null;
		rounds = estart = 0;
	}

	public int plaintextSize()
	{
		return 8;
	}
	
	public int ciphertextSize()
	{
		return 8;
	}	
	
	public void decryptBlock(byte[] block, int start)
	{
		int r = rounds;
		int i = estart;
		int j = schedule.length;
		for(int k = start + 7; k >= start; k--)
			block[k] ^= schedule[--j];
		if((r & 1) == 1) {
			System.arraycopy(block, start + 0, B, 0, 4);
			System.arraycopy(block, start + 4, A, 0, 4);
			B[0] ^= A[0];
			B[1] ^= A[1];
			B[2] ^= A[2];
			B[3] ^= A[3];
			System.arraycopy(B, 0, C, 0, 4);
			FRI(A, C, schedule[--i], schedule[--i]);
			r--;
		} else {
			System.arraycopy(block, start + 0, A, 0, 4);
			System.arraycopy(block, start + 4, B, 0, 4);
			B[0] ^= A[0];
			B[1] ^= A[1];
			B[2] ^= A[2];
			B[3] ^= A[3];
		}
		while(r > 1) {
			System.arraycopy(A, 0, C, 0, 4);
			FRI(B, C, schedule[--i], schedule[--i]);
			System.arraycopy(B, 0, C, 0, 4);
			FRI(A, C, schedule[--i], schedule[--i]);
			r -= 2;
		}
		B[0] ^= A[0];
		B[1] ^= A[1];
		B[2] ^= A[2];
		B[3] ^= A[3];
		System.arraycopy(A, 0, block, start + 0, 4);
		System.arraycopy(B, 0, block, start + 4, 4);
		for(int k = start + 7; k >= start; k--)
			block[k] ^= schedule[--j];
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{	
		int r = rounds;
		int i = estart;
		int j = schedule.length;
		if((r & 1) == 1) {
			System.arraycopy(block, start0 + 0, B, 0, 4);
			System.arraycopy(block, start0 + 4, A, 0, 4);
			A[3] ^= schedule[--j];
			A[2] ^= schedule[--j];
			A[1] ^= schedule[--j];
			A[0] ^= schedule[--j];
			B[3] ^= schedule[--j];
			B[2] ^= schedule[--j];
			B[1] ^= schedule[--j];
			B[0] ^= schedule[--j];			
			B[0] ^= A[0];
			B[1] ^= A[1];
			B[2] ^= A[2];
			B[3] ^= A[3];
			System.arraycopy(B, 0, C, 0, 4);
			FRI(A, C, schedule[--i], schedule[--i]);
			r--;
		} else {
			System.arraycopy(block, start0 + 0, A, 0, 4);
			System.arraycopy(block, start0 + 4, B, 0, 4);
			B[3] ^= schedule[--j];
			B[2] ^= schedule[--j];
			B[1] ^= schedule[--j];
			B[0] ^= schedule[--j];
			A[3] ^= schedule[--j];
			A[2] ^= schedule[--j];
			A[1] ^= schedule[--j];
			A[0] ^= schedule[--j];
			B[0] ^= A[0];
			B[1] ^= A[1];
			B[2] ^= A[2];
			B[3] ^= A[3];
		}
		while(r > 1) {
			System.arraycopy(A, 0, C, 0, 4);
			FRI(B, C, schedule[--i], schedule[--i]);
			System.arraycopy(B, 0, C, 0, 4);
			FRI(A, C, schedule[--i], schedule[--i]);
			r -= 2;
		}
		B[0] ^= A[0];
		B[1] ^= A[1];
		B[2] ^= A[2];
		B[3] ^= A[3];
		System.arraycopy(A, 0, out, start1 + 0, 4);
		System.arraycopy(B, 0, out, start1 + 4, 4);
		for(int k = start1 + 7; k >= start1; k--)
			out[k] ^= schedule[--j];
	}

	public void encryptBlock(byte[] block, final int start)
	{			
		int r = rounds;
		int i = 0;
		int j = estart;
		for(int k = start; k < start + 8; k++)
			block[k] ^= schedule[j++];
		System.arraycopy(block, start + 0, A, 0, 4);
		System.arraycopy(block, start + 4, B, 0, 4);
		B[0] ^= A[0];
		B[1] ^= A[1];
		B[2] ^= A[2];
		B[3] ^= A[3];
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
			B[0] ^= A[0];
			B[1] ^= A[1];
			B[2] ^= A[2];
			B[3] ^= A[3];
			System.arraycopy(B, 0, block, start + 0, 4);
			System.arraycopy(A, 0, block, start + 4, 4);
		} else {
			B[0] ^= A[0];
			B[1] ^= A[1];
			B[2] ^= A[2];
			B[3] ^= A[3];
			System.arraycopy(A, 0, block, start + 0, 4);
			System.arraycopy(B, 0, block, start + 4, 4);
		}
		for(int k = start; k < start + 8; k++)
			block[k] ^= schedule[j++];
		
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int r = rounds;
		int i = 0;
		int j = estart;
		System.arraycopy(block, start0 + 0, A, 0, 4);
		System.arraycopy(block, start0 + 4, B, 0, 4);
		A[0] ^= schedule[j++];
		A[1] ^= schedule[j++];
		A[2] ^= schedule[j++];
		A[3] ^= schedule[j++];
		B[0] ^= schedule[j++];
		B[1] ^= schedule[j++];
		B[2] ^= schedule[j++];
		B[3] ^= schedule[j++];
		B[0] ^= A[0];
		B[1] ^= A[1];
		B[2] ^= A[2];
		B[3] ^= A[3];
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
			B[0] ^= A[0];
			B[1] ^= A[1];
			B[2] ^= A[2];
			B[3] ^= A[3];
			System.arraycopy(B, 0, out, start1 + 0, 4);
			System.arraycopy(A, 0, out, start1 + 4, 4);
		} else {
			B[0] ^= A[0];
			B[1] ^= A[1];
			B[2] ^= A[2];
			B[3] ^= A[3];
			System.arraycopy(A, 0, out, start1 + 0, 4);
			System.arraycopy(B, 0, out, start1 + 4, 4);
		}
		for(int k = start1; k < start1 + 8; k++)
			out[k] ^= schedule[j++];
	}
	
	public void reset() {}
	
	public static final int test()
	{
		final byte[] bytes1 = new byte[8];
		final byte[] bytes2 = new byte[8];
		RandUtils.fillArr(bytes1);
		RandUtils.fillArr(bytes2);
		KeyFEAL a1 = new KeyFEAL(bytes1, 31);
		KeyFEAL a2 = new KeyFEAL(bytes2, 48);
		FEAL aes = new FEAL(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

}

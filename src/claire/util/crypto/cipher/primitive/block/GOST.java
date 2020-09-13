package claire.util.crypto.cipher.primitive.block;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeyGOST;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class GOST 
	   implements ISymmetric<KeyGOST> {

	private KeyGOST key;
	private byte[] SBOX;
	private int[] KEY;
	
	public GOST() {}
	
	public GOST(KeyGOST key)
	{
		this.setKey(key);
	}
	
	public KeyGOST getKey()
	{
		return this.key;
	}

	public void setKey(KeyGOST t)
	{
		this.key = t;
		this.SBOX = t.getSBOX();
		this.KEY = t.getKey();
	}

	public void wipe()
	{
		KEY = null;
		SBOX = null;
		key = null;
	}
	
	public int plaintextSize()
	{
		return 8;
	}
	
	public int ciphertextSize()
	{
		return 8;
	}

	private int F(int A, int B)
	{
		int C = A + B;
		int D =  SBOX[  	((C		 ) & 0x0F)]      ;
        	D += SBOX[ 16 + ((C >> 4 ) & 0x0F)] << 4 ;
        	D += SBOX[ 32 + ((C >> 8 ) & 0x0F)] << 8 ;
        	D += SBOX[ 48 + ((C >> 12) & 0x0F)] << 12;
        	D += SBOX[ 64 + ((C >> 16) & 0x0F)] << 16;
        	D += SBOX[ 80 + ((C >> 20) & 0x0F)] << 20;
        	D += SBOX[ 96 + ((C >> 24) & 0x0F)] << 24;
        	D += SBOX[112 + ((C >> 28) & 0x0F)] << 28;
        return Bits.rotateLeft(D, 11);
	}
	
	public void decryptBlock(byte[] block, int start)
	{
		int A = Bits.intFromBytes(block, start + 0),
			B = Bits.intFromBytes(block, start + 4),
			T;
			
		T = A; A = B ^ F(A, KEY[0]); B = T;
		T = A; A = B ^ F(A, KEY[1]); B = T;
		T = A; A = B ^ F(A, KEY[2]); B = T;
		T = A; A = B ^ F(A, KEY[3]); B = T;
		T = A; A = B ^ F(A, KEY[4]); B = T;
		T = A; A = B ^ F(A, KEY[5]); B = T;
		T = A; A = B ^ F(A, KEY[6]); B = T;
		T = A; A = B ^ F(A, KEY[7]); B = T;
		
		for(int i = 0; i < 2; i++)
		{
			T = A; A = B ^ F(A, KEY[7]); B = T;
			T = A; A = B ^ F(A, KEY[6]); B = T;
			T = A; A = B ^ F(A, KEY[5]); B = T;
			T = A; A = B ^ F(A, KEY[4]); B = T;
			T = A; A = B ^ F(A, KEY[3]); B = T;
			T = A; A = B ^ F(A, KEY[2]); B = T;
			T = A; A = B ^ F(A, KEY[1]); B = T;
			T = A; A = B ^ F(A, KEY[0]); B = T;
		}
			
		T = A; A = B ^ F(A, KEY[7]); B = T;
		T = A; A = B ^ F(A, KEY[6]); B = T;
		T = A; A = B ^ F(A, KEY[5]); B = T;
		T = A; A = B ^ F(A, KEY[4]); B = T;
		T = A; A = B ^ F(A, KEY[3]); B = T;
		T = A; A = B ^ F(A, KEY[2]); B = T;
		T = A; A = B ^ F(A, KEY[1]); B = T;
		
		B ^= F(A, KEY[0]);
			
		Bits.intToBytes(A, block, start + 0);
		Bits.intToBytes(B, block, start + 4);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int A = Bits.intFromBytes(block, start0    ),
			B = Bits.intFromBytes(block, start0 + 4),
			T;
		
		T = A; A = B ^ F(A, KEY[0]); B = T;
		T = A; A = B ^ F(A, KEY[1]); B = T;
		T = A; A = B ^ F(A, KEY[2]); B = T;
		T = A; A = B ^ F(A, KEY[3]); B = T;
		T = A; A = B ^ F(A, KEY[4]); B = T;
		T = A; A = B ^ F(A, KEY[5]); B = T;
		T = A; A = B ^ F(A, KEY[6]); B = T;
		T = A; A = B ^ F(A, KEY[7]); B = T;
		
		for(int i = 0; i < 2; i++)
		{
			T = A; A = B ^ F(A, KEY[7]); B = T;
			T = A; A = B ^ F(A, KEY[6]); B = T;
			T = A; A = B ^ F(A, KEY[5]); B = T;
			T = A; A = B ^ F(A, KEY[4]); B = T;
			T = A; A = B ^ F(A, KEY[3]); B = T;
			T = A; A = B ^ F(A, KEY[2]); B = T;
			T = A; A = B ^ F(A, KEY[1]); B = T;
			T = A; A = B ^ F(A, KEY[0]); B = T;
		}
			
		T = A; A = B ^ F(A, KEY[7]); B = T;
		T = A; A = B ^ F(A, KEY[6]); B = T;
		T = A; A = B ^ F(A, KEY[5]); B = T;
		T = A; A = B ^ F(A, KEY[4]); B = T;
		T = A; A = B ^ F(A, KEY[3]); B = T;
		T = A; A = B ^ F(A, KEY[2]); B = T;
		T = A; A = B ^ F(A, KEY[1]); B = T;
		
		B ^= F(A, KEY[0]);
		
		Bits.intToBytes(A, out, start1    );
		Bits.intToBytes(B, out, start1 + 4);
	}

	public void encryptBlock(byte[] block, int start)
	{
		int A = Bits.intFromBytes(block, start + 0),
			B = Bits.intFromBytes(block, start + 4),
			T;
		
		for(int i = 0; i < 3; i++)
		{
			T = A; A = B ^ F(A, KEY[0]); B = T;
			T = A; A = B ^ F(A, KEY[1]); B = T;
			T = A; A = B ^ F(A, KEY[2]); B = T;
			T = A; A = B ^ F(A, KEY[3]); B = T;
			T = A; A = B ^ F(A, KEY[4]); B = T;
			T = A; A = B ^ F(A, KEY[5]); B = T;
			T = A; A = B ^ F(A, KEY[6]); B = T;
			T = A; A = B ^ F(A, KEY[7]); B = T;
		}
		
		T = A; A = B ^ F(A, KEY[7]); B = T;
		T = A; A = B ^ F(A, KEY[6]); B = T;
		T = A; A = B ^ F(A, KEY[5]); B = T;
		T = A; A = B ^ F(A, KEY[4]); B = T;
		T = A; A = B ^ F(A, KEY[3]); B = T;
		T = A; A = B ^ F(A, KEY[2]); B = T;
		T = A; A = B ^ F(A, KEY[1]); B = T;
		
		B ^= F(A, KEY[0]);
		
		Bits.intToBytes(A, block, start + 0);
		Bits.intToBytes(B, block, start + 4);
	}
	
	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int A = Bits.intFromBytes(block, start0    ),
			B = Bits.intFromBytes(block, start0 + 4),
			T;
		
		for(int i = 0; i < 3; i++)
		{
			T = A; A = B ^ F(A, KEY[0]); B = T;
			T = A; A = B ^ F(A, KEY[1]); B = T;
			T = A; A = B ^ F(A, KEY[2]); B = T;
			T = A; A = B ^ F(A, KEY[3]); B = T;
			T = A; A = B ^ F(A, KEY[4]); B = T;
			T = A; A = B ^ F(A, KEY[5]); B = T;
			T = A; A = B ^ F(A, KEY[6]); B = T;
			T = A; A = B ^ F(A, KEY[7]); B = T;
		}
		
		T = A; A = B ^ F(A, KEY[7]); B = T;
		T = A; A = B ^ F(A, KEY[6]); B = T;
		T = A; A = B ^ F(A, KEY[5]); B = T;
		T = A; A = B ^ F(A, KEY[4]); B = T;
		T = A; A = B ^ F(A, KEY[3]); B = T;
		T = A; A = B ^ F(A, KEY[2]); B = T;
		T = A; A = B ^ F(A, KEY[1]); B = T;
		
		B ^= F(A, KEY[0]);
		
		Bits.intToBytes(A, out, start1    );
		Bits.intToBytes(B, out, start1 + 4);
	}

	public void reset() {}
	
	public static final int test()
	{
		final int[] bytes1 = new int[8];
		final int[] bytes2 = new int[8];
		final byte[] nib0 = new byte[64];
		RandUtils.fillArr(bytes1);
		RandUtils.fillArr(bytes2);
		RandUtils.fillArr(nib0);
		byte[] nibs = Bits.bytesToNibbles(nib0);
		KeyGOST a1 = new KeyGOST(bytes1);
		KeyGOST a2 = new KeyGOST(bytes2, nibs);
		GOST aes = new GOST(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

	public KeyFactory<KeyGOST> keyFactory()
	{
		return KeyGOST.factory;
	}

}

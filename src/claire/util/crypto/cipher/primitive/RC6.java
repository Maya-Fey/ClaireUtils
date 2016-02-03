package claire.util.crypto.cipher.primitive;

import java.util.Arrays;

import claire.util.crypto.cipher.key.KeyRC6;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.crypto.ISymmetric;

public class RC6 implements ISymmetric<KeyRC6> {
	
	private static final int P = 0xb7e15163;
    private static final int Q = 0x9e3779b9;
    
    private int[] KEY;
	
    private int rounds;
    private int start;
	private KeyRC6 key;

	public RC6(KeyRC6 a1) 
	{
		this.setKey(a1);
	}

	public KeyRC6 getKey()
	{
		return this.key;
	}

	public void setKey(KeyRC6 t)
	{
		this.key = t;
		int[] work = ArrayUtil.copy(t.getInts());
		rounds = t.getRounds();
		int p = rounds * 2 + 4;
		start = p - 1;
		KEY = new int[p];
		KEY[0] = P;
		for(int i = 1; i < KEY.length; i++)
			KEY[i] = KEY[i - 1] + Q;
		int times = (work.length > p) ?  work.length : p;
		int A, B, i1, i2;
		A = B = i1 = i2 = 0;
		for(int i = 0; i < times; i++)
		{
			A = KEY[i1] = Bits.rotateLeft(KEY[i1] + A + B, 3);
            B = work[i2] = Bits.rotateLeft(work[i2] + A + B, (A + B) & 31);
            i1 = (i1 + 1) %  KEY.length;
            i2 = (i2 + 1) % work.length;
		}
	}

	public void wipe()
	{
		Arrays.fill(KEY, 0);
		KEY = null;
		key = null;
		rounds = 0;
	}

	public int plaintextSize()
	{
		return 16;
	}
	
	public int ciphertextSize()
	{
		return 16;
	}

	public void decryptBlock(byte[] block, int start)
	{
		int A = Bits.intFromBytes(block, start + 0 ),
			B = Bits.intFromBytes(block, start + 4 ),
			C = Bits.intFromBytes(block, start + 8 ),
			D = Bits.intFromBytes(block, start + 12),
			E,
			F,
			T;
			
		int pos = this.start;
		C -= KEY[pos--];
		A -= KEY[pos--];
		
		for(int i = 0; i < rounds; i++)
		{
			E = F = 0;
            
            T = D;
            D = C;
            C = B;
            B = A;
            A = T;
            
            E = B * (2 * B + 1);
            E = Bits.rotateLeft(E, 5);
            
            F = D * (2 * D + 1);
            F = Bits.rotateLeft(F, 5);
            
            C -= KEY[pos--];
            C = Bits.rotateRight(C, E);
            C ^= F;
            
            A -= KEY[pos--];
            A = Bits.rotateRight(A,F);
            A ^= E;
		}
		
		D -= KEY[pos--];
		B -= KEY[pos--];
	
		Bits.intToBytes(A, block, start + 0 );
		Bits.intToBytes(B, block, start + 4 );
		Bits.intToBytes(C, block, start + 8 );
		Bits.intToBytes(D, block, start + 12);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int A = Bits.intFromBytes(block, start0     ),
			B = Bits.intFromBytes(block, start0 + 4 ),
			C = Bits.intFromBytes(block, start0 + 8 ),
			D = Bits.intFromBytes(block, start0 + 12),
			E,
			F,
			T;
			
		int pos = this.start;
		C -= KEY[pos--];
		A -= KEY[pos--];
		
		for(int i = 0; i < rounds; i++)
		{
			E = F = 0;
            
            T = D;
            D = C;
            C = B;
            B = A;
            A = T;
            
            E = B * (2 * B + 1);
            E = Bits.rotateLeft(E, 5);
            
            F = D * (2 * D + 1);
            F = Bits.rotateLeft(F, 5);
            
            C -= KEY[pos--];
            C = Bits.rotateRight(C, E);
            C ^= F;
            
            A -= KEY[pos--];
            A = Bits.rotateRight(A,F);
            A ^= E;
		}
		
		D -= KEY[pos--];
		B -= KEY[pos--];
	
		Bits.intToBytes(A, out, start1 + 0 );
		Bits.intToBytes(B, out, start1 + 4 );
		Bits.intToBytes(C, out, start1 + 8 );
		Bits.intToBytes(D, out, start1 + 12);
	}

	public void encryptBlock(byte[] block, int start)
	{
		int A = Bits.intFromBytes(block, start + 0 ),
			B = Bits.intFromBytes(block, start + 4 ),
			C = Bits.intFromBytes(block, start + 8 ),
			D = Bits.intFromBytes(block, start + 12),
			E,
			F,
			T;
		
		int pos = 0;
		B += KEY[pos++];
		D += KEY[pos++];
		for(int i = 0; i < rounds; i++)
		{
			E = F = 0;
            
            E = B * (2 * B + 1);
            E = Bits.rotateLeft(E, 5);
            
            F = D * (2 * D + 1);
            F = Bits.rotateLeft(F, 5);
            
            A ^= E;
            A = Bits.rotateLeft(A, F);
            A += KEY[pos++];
            
            C ^= F;
            C = Bits.rotateLeft(C, E);
            C += KEY[pos++];
            
            T = A;
            A = B;
            B = C;
            C = D;
            D = T;  
		}
		
		A += KEY[pos++];
		C += KEY[pos++];
		
		Bits.intToBytes(A, block, start + 0 );
		Bits.intToBytes(B, block, start + 4 );
		Bits.intToBytes(C, block, start + 8 );
		Bits.intToBytes(D, block, start + 12);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int A = Bits.intFromBytes(block, start0     ),
			B = Bits.intFromBytes(block, start0 + 4 ),
			C = Bits.intFromBytes(block, start0 + 8 ),
			D = Bits.intFromBytes(block, start0 + 12),
			E,
			F,
			T;
		
		int pos = 0;
		B += KEY[pos++];
		D += KEY[pos++];
		for(int i = 0; i < rounds; i++)
		{
			E = F = 0;
            
            E = B * (2 * B + 1);
            E = Bits.rotateLeft(E, 5);
            
            F = D * (2 * D + 1);
            F = Bits.rotateLeft(F, 5);
            
            A ^= E;
            A = Bits.rotateLeft(A, F);
            A += KEY[pos++];
            
            C ^= F;
            C = Bits.rotateLeft(C, E);
            C += KEY[pos++];
            
            T = A;
            A = B;
            B = C;
            C = D;
            D = T;  
		}
		
		A += KEY[pos++];
		C += KEY[pos++];
		
		Bits.intToBytes(A, out, start1 + 0 );
		Bits.intToBytes(B, out, start1 + 4 );
		Bits.intToBytes(C, out, start1 + 8 );
		Bits.intToBytes(D, out, start1 + 12);
	}

	public void reset() {}
	
	public static final int test()
	{
		final int[] ints1 = new int[13];
		final int[] ints2 = new int[33];
		RandUtils.fillArr(ints1);
		RandUtils.fillArr(ints2);
		KeyRC6 a1 = new KeyRC6(ints1, 6);
		KeyRC6 a2 = new KeyRC6(ints2, 12);
		RC6 aes = new RC6(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}
	
}

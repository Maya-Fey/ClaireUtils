package claire.util.crypto.cipher.primitive;

import java.util.Arrays;

import claire.util.crypto.cipher.key.KeyCAST6;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IRandom;
import claire.util.standards.crypto.ISymmetric;

public class CAST6 
	   extends CASTBase 
	   implements ISymmetric<KeyCAST6> {

	private KeyCAST6 key;
	
	private final int R;
	
	private final int[] KMASK;
	private final int[] KROT;
	
	public CAST6()
	{
		R = 12;
		KROT = new int[R << 2];
		KMASK = new int[R << 2];
	}
	
	public CAST6(int rounds)
	{
		R = rounds;
		KROT = new int[R << 2];
		KMASK = new int[R << 2];
	}

	public KeyCAST6 getKey()
	{
		return this.key;
	}

	public void setKey(KeyCAST6 t)
	{
		this.key = t;
		byte[] raw = t.getBytes();
		
		int A = 0x5a827999;
        int B = 0x6ed9eba1;
        int C = 19;
        int D = 17;
        
        int[] TROT = new int[R * 16];
        int[] TMASK = new int[R * 16];
        int[] KEY = new int[8];

        for(int i = 0; i < (R * 16); i++)
        { 
        	TMASK[i] = A;
          	A += B;    

          	 TROT[i] = C;
          	C = (C + D) & 0x1f;           
        }
		Bits.bytesToInts(raw, KEY);
		
		int j = 0, k = 0;
		for(int i = 0; i < R; i++)
        {            
            KEY[6] ^= F1(KEY[7], TMASK[j], TROT[j++]);
            KEY[5] ^= F2(KEY[6], TMASK[j], TROT[j++]);
            KEY[4] ^= F3(KEY[5], TMASK[j], TROT[j++]);
            KEY[3] ^= F1(KEY[4], TMASK[j], TROT[j++]);
            KEY[2] ^= F2(KEY[3], TMASK[j], TROT[j++]);
            KEY[1] ^= F3(KEY[2], TMASK[j], TROT[j++]);
            KEY[0] ^= F1(KEY[1], TMASK[j], TROT[j++]);
            KEY[7] ^= F2(KEY[0], TMASK[j], TROT[j++]);
            
            KEY[6] ^= F1(KEY[7], TMASK[j], TROT[j++]);
            KEY[5] ^= F2(KEY[6], TMASK[j], TROT[j++]);
            KEY[4] ^= F3(KEY[5], TMASK[j], TROT[j++]);
            KEY[3] ^= F1(KEY[4], TMASK[j], TROT[j++]);
            KEY[2] ^= F2(KEY[3], TMASK[j], TROT[j++]);
            KEY[1] ^= F3(KEY[2], TMASK[j], TROT[j++]);
            KEY[0] ^= F1(KEY[1], TMASK[j], TROT[j++]);
            KEY[7] ^= F2(KEY[0], TMASK[j], TROT[j++]);

             KROT[k  ] = KEY[0] & 0x1f;
            KMASK[k++] = KEY[7];
             KROT[k  ] = KEY[2] & 0x1f;
            KMASK[k++] = KEY[5];
             KROT[k  ] = KEY[4] & 0x1f;
            KMASK[k++] = KEY[3];
             KROT[k  ] = KEY[6] & 0x1f;            
            KMASK[k++] = KEY[1];
        }
	}

	public void destroyKey()
	{
		Arrays.fill(KMASK, 0);
		Arrays.fill(KROT, 0);
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
		int A = Bits.intFromBytes(block, start + 0 );
		int B = Bits.intFromBytes(block, start + 4 );
		int C = Bits.intFromBytes(block, start + 8 );
		int D = Bits.intFromBytes(block, start + 12);
		
		for(int i = (R << 2) - 4; i >= (R << 1); i -= 7)
		{
			C ^= F1(D, KMASK[i], KROT[i++]);
            B ^= F2(C, KMASK[i], KROT[i++]);
            A ^= F3(B, KMASK[i], KROT[i++]);
            D ^= F1(A, KMASK[i], KROT[i  ]);
		}
			
		for(int i = (R << 1); i > 0;)
		{
			D ^= F1(A, KMASK[--i], KROT[i]);
			A ^= F3(B, KMASK[--i], KROT[i]);
			B ^= F2(C, KMASK[--i], KROT[i]);
			C ^= F1(D, KMASK[--i], KROT[i]); 
		}
		
		Bits.intToBytes(A, block, start + 0 );
		Bits.intToBytes(B, block, start + 4 );
		Bits.intToBytes(C, block, start + 8 );
		Bits.intToBytes(D, block, start + 12);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int A = Bits.intFromBytes(block, start0     );
		int B = Bits.intFromBytes(block, start0 + 4 );
		int C = Bits.intFromBytes(block, start0 + 8 );
		int D = Bits.intFromBytes(block, start0 + 12);
		
		for(int i = (R << 2) - 4; i >= (R << 1); i -= 7)
		{
			C ^= F1(D, KMASK[i], KROT[i++]);
            B ^= F2(C, KMASK[i], KROT[i++]);
            A ^= F3(B, KMASK[i], KROT[i++]);
            D ^= F1(A, KMASK[i], KROT[i  ]);
		}
			
		for(int i = (R << 1); i > 0;)
		{
			D ^= F1(A, KMASK[--i], KROT[i]);
			A ^= F3(B, KMASK[--i], KROT[i]);
			B ^= F2(C, KMASK[--i], KROT[i]);
			C ^= F1(D, KMASK[--i], KROT[i]); 
		}
		
		Bits.intToBytes(A, out, start1     );
		Bits.intToBytes(B, out, start1 + 4 );
		Bits.intToBytes(C, out, start1 + 8 );
		Bits.intToBytes(D, out, start1 + 12);
	}

	public void encryptBlock(byte[] block, int start)
	{
		int A = Bits.intFromBytes(block, start + 0 );
		int B = Bits.intFromBytes(block, start + 4 );
		int C = Bits.intFromBytes(block, start + 8 );
		int D = Bits.intFromBytes(block, start + 12);
		
		for(int i = 0; i < (R << 1);)
		{
			C ^= F1(D, KMASK[i], KROT[i++]);
            B ^= F2(C, KMASK[i], KROT[i++]);
            A ^= F3(B, KMASK[i], KROT[i++]);
            D ^= F1(A, KMASK[i], KROT[i++]);
		}
			
		for(int i = (R << 1) + 4; i <= (R << 2); i += 8)
		{
			D ^= F1(A, KMASK[--i], KROT[i]);
			A ^= F3(B, KMASK[--i], KROT[i]);
			B ^= F2(C, KMASK[--i], KROT[i]);
			C ^= F1(D, KMASK[--i], KROT[i]); 
		}
		
		Bits.intToBytes(A, block, start + 0 );
		Bits.intToBytes(B, block, start + 4 );
		Bits.intToBytes(C, block, start + 8 );
		Bits.intToBytes(D, block, start + 12);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int A = Bits.intFromBytes(block, start0     );
		int B = Bits.intFromBytes(block, start0 + 4 );
		int C = Bits.intFromBytes(block, start0 + 8 );
		int D = Bits.intFromBytes(block, start0 + 12);
		
		for(int i = 0; i < (R << 1);)
		{
			C ^= F1(D, KMASK[i], KROT[i++]);
            B ^= F2(C, KMASK[i], KROT[i++]);
            A ^= F3(B, KMASK[i], KROT[i++]);
            D ^= F1(A, KMASK[i], KROT[i++]);
		}
			
		for(int i = (R << 1) + 4; i <= (R << 2); i += 8)
		{
			D ^= F1(A, KMASK[--i], KROT[i]);
			A ^= F3(B, KMASK[--i], KROT[i]);
			B ^= F2(C, KMASK[--i], KROT[i]);
			C ^= F1(D, KMASK[--i], KROT[i]); 
		}
		
		Bits.intToBytes(A, out, start1     );
		Bits.intToBytes(B, out, start1 + 4 );
		Bits.intToBytes(C, out, start1 + 8 );
		Bits.intToBytes(D, out, start1 + 12);
	}

	public KeyCAST6 newKey(IRandom rand)
	{
		byte[] n = new byte[32];
		RandUtils.fillArr(n, rand);
		return new KeyCAST6(n);
	}

	public void genKey(IRandom rand)
	{
		this.setKey(newKey(rand));
	}
	
	public void reset() {}

}

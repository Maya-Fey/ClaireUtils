package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeyCAST6;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class CAST6 
	   extends CASTBase 
	   implements ISymmetric<KeyCAST6> {

	private KeyCAST6 key;
	
	private int R;
	
	private int[] KMASK;
	private int[] KROT;
	
	public CAST6() {}
	
	public CAST6(KeyCAST6 key)
	{
		this.setKey(key);
	}

	public KeyCAST6 getKey()
	{
		return this.key;
	}

	public void setKey(KeyCAST6 t)
	{
		this.key = t;
		R = t.getRounds();
		byte[] raw = t.getBytes();
		
		int A = 0x5a827999;
        int B = 0x6ed9eba1;
        int C = 19;
        int D = 17;
        
        KMASK = new int[R << 2];
        KROT = new int[R << 2];
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
		Bits.bytesToIntsFull(raw, 0, KEY, 0);
		
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

	public void wipe()
	{
		Arrays.fill(KMASK, 0);
		Arrays.fill(KROT, 0);
		KMASK = KROT = null;
		this.R = 0;
		this.key = null;
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
	
	public void reset() {}

	public static final int test()
	{
		final byte[] bytes1 = new byte[17];
		final byte[] bytes2 = new byte[32];
		RandUtils.fillArr(bytes1);
		RandUtils.fillArr(bytes2);
		KeyCAST6 a1 = new KeyCAST6(bytes1, 6);
		KeyCAST6 a2 = new KeyCAST6(bytes2, 12);
		CAST6 aes = new CAST6(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

	public KeyFactory<KeyCAST6> keyFactory()
	{
		return KeyCAST6.factory;
	}
	
}

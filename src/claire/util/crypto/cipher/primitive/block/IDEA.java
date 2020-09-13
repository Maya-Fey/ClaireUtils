package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeyIDEA;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class IDEA 
	implements ISymmetric<KeyIDEA>
{
		
	private KeyIDEA key;
	
	private short[] KEY, IKEY;
	
	public IDEA() {}
	
	public IDEA(KeyIDEA idea)
	{
		this.setKey(idea);
	}

	public KeyIDEA getKey()
	{
		return key;
	}

	public void setKey(KeyIDEA t)
	{
		this.key = t;
		if(KEY == null) { 
			KEY = new short[52];
			IKEY = new short[52];
		}
		System.arraycopy(t.getShorts(), 0, KEY, 0, 8);
		int j = 8, k = 0;
		for(int i = 0; i < 5; i++) {
			KEY[j + 6] = (short) ((KEY[k + 7] << 9) | ((KEY[k] & 0xFFFF) >> 7)); 
			KEY[j + 7] = (short) ((KEY[k++] << 9) | ((KEY[k] & 0xFFFF) >> 7)); 
			KEY[j++] = (short) ((KEY[k++] << 9) | ((KEY[k] & 0xFFFF) >> 7)); 
			KEY[j++] = (short) ((KEY[k++] << 9) | ((KEY[k] & 0xFFFF) >> 7)); 
			KEY[j++] = (short) ((KEY[k++] << 9) | ((KEY[k] & 0xFFFF) >> 7)); 
			KEY[j++] = (short) ((KEY[k++] << 9) | ((KEY[k] & 0xFFFF) >> 7)); 
			KEY[j++] = (short) ((KEY[k++] << 9) | ((KEY[k] & 0xFFFF) >> 7)); 
			KEY[j++] = (short) ((KEY[k++] << 9) | ((KEY[k++] & 0xFFFF) >> 7)); 
			j += 2;
		}
		KEY[j + 2] = (short) ((KEY[k + 7] << 9) | ((KEY[k] & 0xFFFF) >> 7)); 
		KEY[j + 3] = (short) ((KEY[k++] << 9) | ((KEY[k++] & 0xFFFF) >> 7)); 
		KEY[j++] = (short) ((KEY[k++] << 9) | ((KEY[k] & 0xFFFF) >> 7)); 
		KEY[j++] = (short) ((KEY[k++] << 9) | ((KEY[k] & 0xFFFF) >> 7)); 
		
		k = 51;
		for(int i = 0; i < 8; i++) {
			IKEY[k] = mulInv(KEY[k--]);
			IKEY[k] = 		 KEY[k--];
			IKEY[k] =		 KEY[k--];
			IKEY[k] = mulInv(KEY[k--]);
			IKEY[k] = 		 KEY[k--];
			IKEY[k] = 		 KEY[k--];
		}
		IKEY[k] = mulInv(KEY[k--]);
		IKEY[k] = 		 KEY[k--];
		IKEY[k] =		 KEY[k--];
		IKEY[k] = mulInv(KEY[k--]);
	}

	public void reset() {}

	public void wipe()
	{
		if(KEY != null)
			Arrays.fill(KEY, (short) 0);
		KEY = null;
	}

	public int plaintextSize()
	{
		return 8;
	}

	public int ciphertextSize()
	{
		return 8;
	}
	
	private short mulInv(short def)
	{
		int t = def & 0xFFFF; 
		if(t < 2) 
			return def;
		int r = 0x10001;
		int t0 = 1;
		int t1 = 0;
		while(true) {
			t1 += r / t * t0;
			r %= t;
			if(r == 1) 
				return (short) ((0x10001 - t1) & 0xFFFF); 
			t0 += t / r * t1;
			t %= r;
			if(t == 1) 
				return (short) t0; 
		}
	}
	
	private short mul(int i1, int i2)
	{
		int r = (i1 &= 0xFFFF) * (i2 &= 0xFFFF);
		if(r != 0) 
			return (short) (((r & 0xFFFFFFFFL) % 0x10001) & 0xFFFF); 
		else 
			return (short) ((1 - i1 - i2) & 0xFFFF);
	}
	
	public void encryptBlock(byte[] block, int start)
	{
		short A, B, C, D, E, F;
		A = Bits.shortFromBytes(block, start    );
		B = Bits.shortFromBytes(block, start + 2);
		C = Bits.shortFromBytes(block, start + 4);
		D = Bits.shortFromBytes(block, start + 6);
		
		int k = 0;
		for(int i = 0; i < 7; i++) {
			A = mul(A, KEY[k++]);
			B += KEY[k++];
			C += KEY[k++];
			D = mul(D, KEY[k++]);
			E = (short) (A ^ C);
			F = (short) (B ^ D);
			E = mul(E, KEY[k++]);
			F += E;
			F = mul(F, KEY[k++]);
			E += F;
			A ^= E;
			C ^= E;
			B ^= F;
			D ^= F;
			E = B;
			B = C;
			C = E;
		}
		A = mul(A, KEY[k++]);
		B += KEY[k++];
		C += KEY[k++];
		D = mul(D, KEY[k++]);
	
		E = (short) (A ^ C);
		F = (short) (B ^ D);
		E = mul(E, KEY[k++]);
		F += E;
		F = mul(F, KEY[k++]);
		E += F;
		A ^= E;
		C ^= E;
		B ^= F;
		D ^= F;

		A = mul(A, KEY[k++]);
		B += KEY[k++];
		C += KEY[k++];
		D = mul(D, KEY[k++]);

		Bits.shortToBytes(A, block, start    );
		Bits.shortToBytes(B, block, start + 2);
		Bits.shortToBytes(C, block, start + 4);
		Bits.shortToBytes(D, block, start + 6);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		short A, B, C, D, E, F;
		A = Bits.shortFromBytes(block, start0    );
		B = Bits.shortFromBytes(block, start0 + 2);
		C = Bits.shortFromBytes(block, start0 + 4);
		D = Bits.shortFromBytes(block, start0 + 6);
		
		int k = 0;
		for(int i = 0; i < 7; i++) {
			A = mul(A, KEY[k++]);
			B += KEY[k++];
			C += KEY[k++];
			D = mul(D, KEY[k++]);
			E = (short) (A ^ C);
			F = (short) (B ^ D);
			E = mul(E, KEY[k++]);
			F += E;
			F = mul(F, KEY[k++]);
			E += F;
			A ^= E;
			C ^= E;
			B ^= F;
			D ^= F;
			E = B;
			B = C;
			C = E;
		}
		A = mul(A, KEY[k++]);
		B += KEY[k++];
		C += KEY[k++];
		D = mul(D, KEY[k++]);
	
		E = (short) (A ^ C);
		F = (short) (B ^ D);
		E = mul(E, KEY[k++]);
		F += E;
		F = mul(F, KEY[k++]);
		E += F;
		A ^= E;
		C ^= E;
		B ^= F;
		D ^= F;

		A = mul(A, KEY[k++]);
		B += KEY[k++];
		C += KEY[k++];
		D = mul(D, KEY[k++]);

		Bits.shortToBytes(A, out, start1    );
		Bits.shortToBytes(B, out, start1 + 2);
		Bits.shortToBytes(C, out, start1 + 4);
		Bits.shortToBytes(D, out, start1 + 6);
	}

	public void decryptBlock(byte[] block, int start)
	{
		short A, B, C, D, E, F;
		A = Bits.shortFromBytes(block, start    );
		B = Bits.shortFromBytes(block, start + 2);
		C = Bits.shortFromBytes(block, start + 4);
		D = Bits.shortFromBytes(block, start + 6);
		
		int k = 51;
		D = mul(D, IKEY[k--]);
		C -= IKEY[k--];
		B -= IKEY[k--];
		A = mul(A, IKEY[k--]);

		E = (short) (A ^ C);
		F = (short) (B ^ D);
		E = mul(E, IKEY[k-- - 1]);
		F += E;
		F = mul(F, IKEY[k-- + 1]);
		E += F;
		A ^= E;
		C ^= E;
		B ^= F;
		D ^= F;
		
		D = mul(D, IKEY[k--]);
		C -= IKEY[k--];
		B -= IKEY[k--];
		A = mul(A, IKEY[k--]);

		for(int i = 0; i < 7; i++) {
			E = B;
			B = C;
			C = E;
			E = (short) (A ^ C);
			F = (short) (B ^ D);
			E = mul(E, IKEY[k-- - 1]);
			F += E;
			F = mul(F, IKEY[k-- + 1]);
			E += F;
			A ^= E;
			C ^= E;
			B ^= F;
			D ^= F;
			
			D = mul(D, IKEY[k--]);
			C -= IKEY[k--];
			B -= IKEY[k--];
			A = mul(A, IKEY[k--]);	
		}
		
		Bits.shortToBytes(A, block, start    );
		Bits.shortToBytes(B, block, start + 2);
		Bits.shortToBytes(C, block, start + 4);
		Bits.shortToBytes(D, block, start + 6);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		short A, B, C, D, E, F;
		A = Bits.shortFromBytes(block, start0    );
		B = Bits.shortFromBytes(block, start0 + 2);
		C = Bits.shortFromBytes(block, start0 + 4);
		D = Bits.shortFromBytes(block, start0 + 6);
		
		int k = 51;
		D = mul(D, IKEY[k--]);
		C -= IKEY[k--];
		B -= IKEY[k--];
		A = mul(A, IKEY[k--]);

		E = (short) (A ^ C);
		F = (short) (B ^ D);
		E = mul(E, IKEY[k-- - 1]);
		F += E;
		F = mul(F, IKEY[k-- + 1]);
		E += F;
		A ^= E;
		C ^= E;
		B ^= F;
		D ^= F;
		
		D = mul(D, IKEY[k--]);
		C -= IKEY[k--];
		B -= IKEY[k--];
		A = mul(A, IKEY[k--]);

		for(int i = 0; i < 7; i++) {
			E = B;
			B = C;
			C = E;
			E = (short) (A ^ C);
			F = (short) (B ^ D);
			E = mul(E, IKEY[k-- - 1]);
			F += E;
			F = mul(F, IKEY[k-- + 1]);
			E += F;
			A ^= E;
			C ^= E;
			B ^= F;
			D ^= F;
			
			D = mul(D, IKEY[k--]);
			C -= IKEY[k--];
			B -= IKEY[k--];
			A = mul(A, IKEY[k--]);	
		}
		
		Bits.shortToBytes(A, out, start1    );
		Bits.shortToBytes(B, out, start1 + 2);
		Bits.shortToBytes(C, out, start1 + 4);
		Bits.shortToBytes(D, out, start1 + 6);
	}
	
	public static final int test()
	{
		short[] ints1 = new short[8];
		short[] ints2 = new short[8];
		RandUtils.fillArr(ints1);
		RandUtils.fillArr(ints2);
		KeyIDEA a1 = new KeyIDEA(ints1);
		KeyIDEA a2 = new KeyIDEA(ints2);
		IDEA aes = new IDEA(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

	public KeyFactory<KeyIDEA> keyFactory()
	{
		return KeyIDEA.factory;
	}
	
}

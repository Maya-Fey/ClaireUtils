package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeySimon64;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class Simon64 
	   implements ISymmetric<KeySimon64> {
	
	private static final byte[][] Z = new byte[][] 
	{
        {
        	1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 
        	1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 
        },
        {
        	1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 
        	1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 
        },
        {
        	1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 
        	0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 
        },
        {
        	1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 
        	0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 
        },
        {
        	1, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 
        	0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1,
        }
    };

	private KeySimon64 ref;
	private long[] key;
	
	public Simon64() {}
	
	public Simon64(KeySimon64 key)
	{
		this.setKey(key);
	}
	
	public KeySimon64 getKey()
	{
		return ref;
	}

	public void setKey(KeySimon64 t)
	{
		long[] raw = t.getLongs();
		
		switch(raw.length)
		{
			case 2:
				byte[] z = Z[2];
				key = new long[40];
				System.arraycopy(raw, 0, key, 0, 2);
				int j = 0;
				for(int i = 2; i < 40; i++) {
					long k = key[i - 1];
					k = Bits.rotateRight(k, 3) ^ Bits.rotateRight(k, 4);
					key[i] = k ^ key[j] ^ 0xFFFFFFFC ^ z[j++];
				}
				break;
			case 3:
				z = Z[3];
				key = new long[42];
				System.arraycopy(raw, 0, key, 0, 2);
				j = 0;
				for(int i = 3; i < 40; i++) {
					long k = key[i - 1];
					k = Bits.rotateRight(k, 3) ^ Bits.rotateRight(k, 4);
					key[i] = k ^ key[j] ^ 0xFFFFFFFC ^ z[j++];
				}
				break;
			case 4:
				z = Z[4];
				key = new long[44];
				System.arraycopy(raw, 0, key, 0, 2);
				j = 0;
				for(int i = 4; i < 40; i++) {
					long k = Bits.rotateRight(key[i - 1], 3);
					k ^= key[j + 1];
					k ^= Bits.rotateRight(k, 1);
					key[i] = k ^ key[j] ^ 0xFFFFFFFC ^ z[j++];
				}
				break;
		}		
	}

	public void encryptBlock(byte[] block, int start)
	{
		long a = Bits.longFromBytes(block, start    ),
			 b = Bits.longFromBytes(block, start + 8);

		long j = 0;
		for(int i = 0; i < key.length; i++)
		{
			j = a;
			a = b ^ key[i] ^ (Bits.rotateLeft(j, 8) & Bits.rotateLeft(j, 1)) ^ Bits.rotateLeft(j, 2);
			b = j;
		}
		
		Bits.longToBytes(a, block, start    );
		Bits.longToBytes(b, block, start + 8);
	
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		long a = Bits.longFromBytes(block, start0    ),
			 b = Bits.longFromBytes(block, start0 + 8);

		long j = 0;
		for(int i = 0; i < key.length; i++)
		{
			j = a;
			a = b ^ key[i] ^ (Bits.rotateLeft(j, 8) & Bits.rotateLeft(j, 1)) ^ Bits.rotateLeft(j, 2);
			b = j;
		}
		
		Bits.longToBytes(a, out, start1    );
		Bits.longToBytes(b, out, start1 + 8);
	}

	public void decryptBlock(byte[] block, int start)
	{
		long a = Bits.longFromBytes(block, start    ),
			 b = Bits.longFromBytes(block, start + 8);

		long j = 0;
		for(int i = key.length - 1; i > -1; i--)
		{
			j = a;
			a = b;
			b = j ^ key[i] ^ (Bits.rotateLeft(a, 8) & Bits.rotateLeft(a, 1)) ^ Bits.rotateLeft(a, 2);
 		}
		
		Bits.longToBytes(a, block, start    );
		Bits.longToBytes(b, block, start + 8);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		long a = Bits.longFromBytes(block, start0    ),
			 b = Bits.longFromBytes(block, start0 + 8);

		long j = 0;
		for(int i = key.length - 1; i > -1; i--)
		{
			j = a;
			a = b;
			b = j ^ key[i] ^ (Bits.rotateLeft(a, 8) & Bits.rotateLeft(a, 1)) ^ Bits.rotateLeft(a, 2);
 		}
		
		Bits.longToBytes(a, out, start1    );
		Bits.longToBytes(b, out, start1 + 8);
	}
	
	public void reset() {}

	public void wipe()
	{
		Arrays.fill(key, 0);
		key = null;
		ref = null;
	}

	public int plaintextSize()
	{
		return 16;
	}

	public int ciphertextSize()
	{
		return 16;
	}
	
	public static int test()
	{
		long[] ints1 = new long[2];
		long[] ints2 = new long[4];
		RandUtils.fillArr(ints1);
		RandUtils.fillArr(ints2);
		KeySimon64 a1 = new KeySimon64(ints1);
		KeySimon64 a2 = new KeySimon64(ints2);
		Simon64 aes = new Simon64(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}
	
	public KeyFactory<KeySimon64> keyFactory()
	{
		return KeySimon64.factory;
	}


}

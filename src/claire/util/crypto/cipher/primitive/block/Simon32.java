package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeySimon32;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class Simon32 
	   implements ISymmetric<KeySimon32> {
	
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

	private KeySimon32 ref;
	private int[] key;
	
	public Simon32() {}
	
	public Simon32(KeySimon32 key)
	{
		this.setKey(key);
	}
	
	public KeySimon32 getKey()
	{
		return ref;
	}

	public void setKey(KeySimon32 t)
	{
		int[] raw = t.getInts();
		
		switch(raw.length)
		{
			case 2:
				byte[] z = Z[1];
				key = new int[40];
				System.arraycopy(raw, 0, key, 0, 2);
				int j = 0;
				for(int i = 2; i < 40; i++) {
					int k = key[i - 1];
					k = Bits.rotateRight(k, 3) ^ Bits.rotateRight(k, 4);
					key[i] = k ^ key[j] ^ 0xFFFFFFFC ^ z[j++];
				}
				break;
			case 3:
				z = Z[2];
				key = new int[42];
				System.arraycopy(raw, 0, key, 0, 2);
				j = 0;
				for(int i = 3; i < 40; i++) {
					int k = key[i - 1];
					k = Bits.rotateRight(k, 3) ^ Bits.rotateRight(k, 4);
					key[i] = k ^ key[j] ^ 0xFFFFFFFC ^ z[j++];
				}
				break;
			case 4:
				z = Z[3];
				key = new int[44];
				System.arraycopy(raw, 0, key, 0, 2);
				j = 0;
				for(int i = 4; i < 40; i++) {
					int k = Bits.rotateRight(key[i - 1], 3);
					k ^= key[j + 1];
					k ^= Bits.rotateRight(k, 1);
					key[i] = k ^ key[j] ^ 0xFFFFFFFC ^ z[j++];
				}
				break;
		}		
	}

	public void encryptBlock(byte[] block, int start)
	{
		int a = Bits.intFromBytes(block, start    ),
			b = Bits.intFromBytes(block, start + 4);

		int j = 0;
		for(int i = 0; i < key.length; i++)
		{
			j = a;
			a = b ^ key[i] ^ (Bits.rotateLeft(j, 8) & Bits.rotateLeft(j, 1)) ^ Bits.rotateLeft(j, 2);
			b = j;
		}
		
		Bits.intToBytes(a, block, start    );
		Bits.intToBytes(b, block, start + 4);
	
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int a = Bits.intFromBytes(block, start0    ),
			b = Bits.intFromBytes(block, start0 + 4);

		int j = 0;
		for(int i = 0; i < key.length; i++)
		{
			j = a;
			a = b ^ key[i] ^ (Bits.rotateLeft(j, 8) & Bits.rotateLeft(j, 1)) ^ Bits.rotateLeft(j, 2);
			b = j;
		}
		
		Bits.intToBytes(a, out, start1    );
		Bits.intToBytes(b, out, start1 + 4);
	}

	public void decryptBlock(byte[] block, int start)
	{
		int a = Bits.intFromBytes(block, start    ),
			b = Bits.intFromBytes(block, start + 4);

		int j = 0;
		for(int i = key.length - 1; i > -1; i--)
		{
			j = a;
			a = b;
			b = j ^ key[i] ^ (Bits.rotateLeft(a, 8) & Bits.rotateLeft(a, 1)) ^ Bits.rotateLeft(a, 2);
 		}
		
		Bits.intToBytes(a, block, start    );
		Bits.intToBytes(b, block, start + 4);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int a = Bits.intFromBytes(block, start0    ),
			b = Bits.intFromBytes(block, start0 + 4);

		int j = 0;
		for(int i = key.length - 1; i > -1; i--)
		{
			j = a;
			a = b;
			b = j ^ key[i] ^ (Bits.rotateLeft(a, 8) & Bits.rotateLeft(a, 1)) ^ Bits.rotateLeft(a, 2);
 		}
		
		Bits.intToBytes(a, out, start1    );
		Bits.intToBytes(b, out, start1 + 4);
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
		return 8;
	}

	public int ciphertextSize()
	{
		return 8;
	}
	
	public static int test()
	{
		int[] ints1 = new int[2];
		int[] ints2 = new int[4];
		RandUtils.fillArr(ints1);
		RandUtils.fillArr(ints2);
		KeySimon32 a1 = new KeySimon32(ints1);
		KeySimon32 a2 = new KeySimon32(ints2);
		Simon32 aes = new Simon32(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

	public KeyFactory<KeySimon32> keyFactory()
	{
		return KeySimon32.factory;
	}

}

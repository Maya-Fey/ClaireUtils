package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeySimon16;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class Simon16 
	   implements ISymmetric<KeySimon16> {
	
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

	private KeySimon16 ref;
	private short[] key;
	
	public Simon16() {}
	
	public Simon16(KeySimon16 key)
	{
		this.setKey(key);
	}
	
	public KeySimon16 getKey()
	{
		return ref;
	}

	public void setKey(KeySimon16 t)
	{
		short[] raw = t.getShorts();
		
		switch(raw.length)
		{
			case 2:
				byte[] z = Z[2];
				key = new short[40];
				System.arraycopy(raw, 0, key, 0, 2);
				int j = 0;
				for(int i = 2; i < 40; i++) {
					short k = key[i - 1];
					k = (short) (Bits.rotateRight(k, 3) ^ Bits.rotateRight(k, 4));
					key[i] = (short) (k ^ key[j] ^ 0xFFFFFFFC ^ z[j++]);
				}
				break;
			case 3:
				z = Z[3];
				key = new short[42];
				System.arraycopy(raw, 0, key, 0, 2);
				j = 0;
				for(int i = 3; i < 40; i++) {
					short k = key[i - 1];
					k = (short) (Bits.rotateRight(k, 3) ^ Bits.rotateRight(k, 4));
					key[i] = (short) (k ^ key[j] ^ 0xFFFFFFFC ^ z[j++]);
				}
				break;
			case 4:
				z = Z[4];
				key = new short[44];
				System.arraycopy(raw, 0, key, 0, 2);
				j = 0;
				for(int i = 4; i < 40; i++) {
					short k = Bits.rotateRight(key[i - 1], 3);
					k ^= key[j + 1];
					k ^= Bits.rotateRight(k, 1);
					key[i] = (short) (k ^ key[j] ^ 0xFFFFFFFC ^ z[j++]);
				}
				break;
		}		
	}

	public void encryptBlock(byte[] block, int start)
	{
		short a = Bits.shortFromBytes(block, start    ),
			 b = Bits.shortFromBytes(block, start + 2);

		short j = 0;
		for(int i = 0; i < key.length; i++)
		{
			j = a;
			a = (short) (b ^ key[i] ^ (Bits.rotateLeft(j, 8) & Bits.rotateLeft(j, 1)) ^ Bits.rotateLeft(j, 2));
			b = j;
		}
		
		Bits.shortToBytes(a, block, start    );
		Bits.shortToBytes(b, block, start + 2);
	
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		short a = Bits.shortFromBytes(block, start0    ),
			 b = Bits.shortFromBytes(block, start0 + 2);

		short j = 0;
		for(int i = 0; i < key.length; i++)
		{
			j = a;
			a = (short) (b ^ key[i] ^ (Bits.rotateLeft(j, 8) & Bits.rotateLeft(j, 1)) ^ Bits.rotateLeft(j, 2));
			b = j;
		}
		
		Bits.shortToBytes(a, out, start1    );
		Bits.shortToBytes(b, out, start1 + 2);
	}

	public void decryptBlock(byte[] block, int start)
	{
		short a = Bits.shortFromBytes(block, start    ),
			 b = Bits.shortFromBytes(block, start + 2);

		short j = 0;
		for(int i = key.length - 1; i > -1; i--)
		{
			j = a;
			a = b;
			b = (short) (j ^ key[i] ^ (Bits.rotateLeft(a, 8) & Bits.rotateLeft(a, 1)) ^ Bits.rotateLeft(a, 2));
 		}
		
		Bits.shortToBytes(a, block, start    );
		Bits.shortToBytes(b, block, start + 2);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		short a = Bits.shortFromBytes(block, start0    ),
			 b = Bits.shortFromBytes(block, start0 + 2);

		short j = 0;
		for(int i = key.length - 1; i > -1; i--)
		{
			j = a;
			a = b;
			b = (short) (j ^ key[i] ^ (Bits.rotateLeft(a, 8) & Bits.rotateLeft(a, 1)) ^ Bits.rotateLeft(a, 2));
 		}
		
		Bits.shortToBytes(a, out, start1    );
		Bits.shortToBytes(b, out, start1 + 2);
	}
	
	public void reset() {}

	public void wipe()
	{
		Arrays.fill(key, (short) 0);
		key = null;
		ref = null;
	}

	public int plaintextSize()
	{
		return 4;
	}

	public int ciphertextSize()
	{
		return 4;
	}
	
	public static int test()
	{
		short[] ints1 = new short[2];
		short[] ints2 = new short[4];
		RandUtils.fillArr(ints1);
		RandUtils.fillArr(ints2);
		KeySimon16 a1 = new KeySimon16(ints1);
		KeySimon16 a2 = new KeySimon16(ints2);
		Simon16 aes = new Simon16(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

	public KeyFactory<KeySimon16> keyFactory()
	{
		return KeySimon16.factory;
	}

}

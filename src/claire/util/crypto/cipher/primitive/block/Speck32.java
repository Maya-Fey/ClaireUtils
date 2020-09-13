package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeySpeck32;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class Speck32 
	   implements ISymmetric<KeySpeck32> {
	
	private KeySpeck32 ref;
	private int[] key;
	
	public Speck32() {}
	
	public Speck32(KeySpeck32 key) 
	{
		this.setKey(key);
	}

	public KeySpeck32 getKey()
	{
		return ref;
	}

	public void setKey(KeySpeck32 t)
	{
		int[] raw = t.getInts();
		
		switch(raw.length)
		{
			case 2:
				key = new int[26];
				int a = raw[1], b = raw[0];
				for(int i = 1; i < key.length; i++) {
					a = Bits.rotateRight(a, 8);
					a += b;
					a ^= i;
					b = Bits.rotateLeft(b, 3);
					key[i] = b ^= a;
				}
				break;
			case 3:
				key = new int[27];
				a = raw[1]; b = raw[0];
				int c = raw[2], tmp;
				for(int i = 1; i < key.length; i++) {
					a = Bits.rotateRight(a, 8);
					a += b;
					a ^= i;
					b = Bits.rotateLeft(b, 3);
					key[i] = b ^= a;
					tmp = a;
					a = c;
					c = tmp;
				}
				break;
			case 4:
				key = new int[28];
				a = raw[1]; b = raw[0]; c = raw[2];
				int d = raw[3];
				for(int i = 1; i < key.length; i++) {
					a = Bits.rotateRight(a, 8);
					a += b;
					a ^= i;
					b = Bits.rotateLeft(b, 3);
					key[i] = b ^= a;
					tmp = a;
					a = c;
					c = d;
					d = tmp;
				}
				break;
		}	
	}

	public void encryptBlock(byte[] block, int start)
	{
		int a = Bits.intFromBytes(block, start + 0),
			b = Bits.intFromBytes(block, start + 4);
		
		for(int i = 0; i < key.length; i++) {
			a = Bits.rotateRight(a, 8);
			a += b;
			a ^= key[i];
			b = Bits.rotateLeft(b, 3);
			b ^= a;
		}
		
		Bits.intToBytes(a, block, start    );
		Bits.intToBytes(b, block, start + 4);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int a = Bits.intFromBytes(block, start0 + 0),
			b = Bits.intFromBytes(block, start0 + 4);
			
		for(int i = 0; i < key.length; i++) {
			a = Bits.rotateRight(a, 8);
			a += b;
			a ^= key[i];
			b = Bits.rotateLeft(b, 3);
			b ^= a;
		}
		
		Bits.intToBytes(a, out, start1    );
		Bits.intToBytes(b, out, start1 + 4);
	}

	public void decryptBlock(byte[] block, int start)
	{
		int a = Bits.intFromBytes(block, start + 0),
			b = Bits.intFromBytes(block, start + 4);
		
		for(int i = key.length - 1; i > -1; i--) {
			b ^= a;
			a ^= key[i];
			b = Bits.rotateRight(b, 3);
			a -= b;
			a = Bits.rotateLeft(a, 8);
		}
		
		Bits.intToBytes(a, block, start    );
		Bits.intToBytes(b, block, start + 4);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		int a = Bits.intFromBytes(block, start0 + 0),
			b = Bits.intFromBytes(block, start0 + 4);
			
		for(int i = key.length - 1; i > -1; i--) {
			b ^= a;
			a ^= key[i];
			b = Bits.rotateRight(b, 3);
			a -= b;
			a = Bits.rotateLeft(a, 8);
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
		KeySpeck32 a1 = new KeySpeck32(ints1);
		KeySpeck32 a2 = new KeySpeck32(ints2);
		Speck32 aes = new Speck32(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

	public KeyFactory<KeySpeck32> keyFactory()
	{
		return KeySpeck32.factory;
	}

}

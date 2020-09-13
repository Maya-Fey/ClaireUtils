package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeySpeck64;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class Speck64 
	   implements ISymmetric<KeySpeck64> {
	
	private KeySpeck64 ref;
	private long[] key;
	
	public Speck64() {}
	
	public Speck64(KeySpeck64 key) 
	{
		this.setKey(key);
	}

	public KeySpeck64 getKey()
	{
		return ref;
	}

	public void setKey(KeySpeck64 t)
	{
		long[] raw = t.getLongs();
		
		switch(raw.length)
		{
			case 2:
				key = new long[32];
				long a = raw[1], b = raw[0];
				for(int i = 1; i < key.length; i++) {
					a = Bits.rotateRight(a, 8);
					a += b;
					a ^= i;
					b = Bits.rotateLeft(b, 3);
					key[i] = b ^= a;
				}
				break;
			case 3:
				key = new long[33];
				a = raw[1]; b = raw[0];
				long c = raw[2], tmp;
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
				key = new long[34];
				a = raw[1]; b = raw[0]; c = raw[2];
				long d = raw[3];
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
		long a = Bits.longFromBytes(block, start + 0),
			 b = Bits.longFromBytes(block, start + 8);
		
		for(int i = 0; i < key.length; i++) {
			a = Bits.rotateRight(a, 8);
			a += b;
			a ^= key[i];
			b = Bits.rotateLeft(b, 3);
			b ^= a;
		}
		
		Bits.longToBytes(a, block, start    );
		Bits.longToBytes(b, block, start + 8);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		long a = Bits.longFromBytes(block, start0 + 0),
			 b = Bits.longFromBytes(block, start0 + 8);
			
		for(int i = 0; i < key.length; i++) {
			a = Bits.rotateRight(a, 8);
			a += b;
			a ^= key[i];
			b = Bits.rotateLeft(b, 3);
			b ^= a;
		}
		
		Bits.longToBytes(a, out, start1    );
		Bits.longToBytes(b, out, start1 + 8);
	}

	public void decryptBlock(byte[] block, int start)
	{
		long a = Bits.longFromBytes(block, start + 0),
			 b = Bits.longFromBytes(block, start + 8);
		
		for(int i = key.length - 1; i > -1; i--) {
			b ^= a;
			a ^= key[i];
			b = Bits.rotateRight(b, 3);
			a -= b;
			a = Bits.rotateLeft(a, 8);
		}
		
		Bits.longToBytes(a, block, start    );
		Bits.longToBytes(b, block, start + 8);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		long a = Bits.longFromBytes(block, start0 + 0),
			 b = Bits.longFromBytes(block, start0 + 8);
			
		for(int i = key.length - 1; i > -1; i--) {
			b ^= a;
			a ^= key[i];
			b = Bits.rotateRight(b, 3);
			a -= b;
			a = Bits.rotateLeft(a, 8);
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
		KeySpeck64 a1 = new KeySpeck64(ints1);
		KeySpeck64 a2 = new KeySpeck64(ints2);
		Speck64 aes = new Speck64(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

	public KeyFactory<KeySpeck64> keyFactory()
	{
		return KeySpeck64.factory;
	}

}

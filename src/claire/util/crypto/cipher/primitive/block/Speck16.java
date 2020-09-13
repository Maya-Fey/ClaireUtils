package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeySpeck16;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class Speck16 
	   implements ISymmetric<KeySpeck16> {
	
	private KeySpeck16 ref;
	private short[] key;
	
	public Speck16() {}
	
	public Speck16(KeySpeck16 key) 
	{
		this.setKey(key);
	}

	public KeySpeck16 getKey()
	{
		return ref;
	}

	public void setKey(KeySpeck16 t)
	{
		short[] raw = t.getShorts();
		
		switch(raw.length)
		{
			case 2:
				key = new short[20];
				short a = raw[1], b = raw[0];
				for(int i = 1; i < key.length; i++) {
					a = Bits.rotateRight(a, 7);
					a += b;
					a ^= i;
					b = Bits.rotateLeft(b, 2);
					key[i] = b ^= a;
				}
				break;
			case 3:
				key = new short[21];
				a = raw[1]; b = raw[0];
				short c = raw[2], tmp;
				for(int i = 1; i < key.length; i++) {
					a = Bits.rotateRight(a, 7);
					a += b;
					a ^= i;
					b = Bits.rotateLeft(b, 2);
					key[i] = b ^= a;
					tmp = a;
					a = c;
					c = tmp;
				}
				break;
			case 4:
				key = new short[22];
				a = raw[1]; b = raw[0]; c = raw[2];
				short d = raw[3];
				for(int i = 1; i < key.length; i++) {
					a = Bits.rotateRight(a, 7);
					a += b;
					a ^= i;
					b = Bits.rotateLeft(b, 2);
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
		short a = Bits.shortFromBytes(block, start + 0),
			  b = Bits.shortFromBytes(block, start + 2);
		
		for(int i = 0; i < key.length; i++) {
			a = Bits.rotateRight(a, 7);
			a += b;
			a ^= key[i];
			b = Bits.rotateLeft(b, 2);
			b ^= a;
		}
		
		Bits.shortToBytes(a, block, start    );
		Bits.shortToBytes(b, block, start + 2);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		short a = Bits.shortFromBytes(block, start0 + 0),
			  b = Bits.shortFromBytes(block, start0 + 2);
			
		for(int i = 0; i < key.length; i++) {
			a = Bits.rotateRight(a, 7);
			a += b;
			a ^= key[i];
			b = Bits.rotateLeft(b, 2);
			b ^= a;
		}
		
		Bits.shortToBytes(a, out, start1    );
		Bits.shortToBytes(b, out, start1 + 2);
	}

	public void decryptBlock(byte[] block, int start)
	{
		short a = Bits.shortFromBytes(block, start + 0),
			  b = Bits.shortFromBytes(block, start + 2);
		
		for(int i = key.length - 1; i > -1; i--) {
			b ^= a;
			a ^= key[i];
			b = Bits.rotateRight(b, 2);
			a -= b;
			a = Bits.rotateLeft(a, 7);
		}
		
		Bits.shortToBytes(a, block, start    );
		Bits.shortToBytes(b, block, start + 2);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		short a = Bits.shortFromBytes(block, start0 + 0),
			  b = Bits.shortFromBytes(block, start0 + 2);
			
		for(int i = key.length - 1; i > -1; i--) {
			b ^= a;
			a ^= key[i];
			b = Bits.rotateRight(b, 2);
			a -= b;
			a = Bits.rotateLeft(a, 7);
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
		KeySpeck16 a1 = new KeySpeck16(ints1);
		KeySpeck16 a2 = new KeySpeck16(ints2);
		Speck16 aes = new Speck16(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

	public KeyFactory<KeySpeck16> keyFactory()
	{
		return KeySpeck16.factory;
	}


}

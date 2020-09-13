package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeyDESX;
import claire.util.crypto.rng.RandUtils;
import claire.util.logging.Log;
import claire.util.standards.crypto.ISymmetric;

public class DESX 
	   implements ISymmetric<KeyDESX> {

	private KeyDESX key;
	
	private DES des = new DES();
	
	private byte[] k;
	
	public DESX() {}
	
	public DESX(KeyDESX des)
	{
		this.setKey(des);
	}
	
	public KeyDESX getKey()
	{
		return key;
	}

	public void setKey(KeyDESX t)
	{
		this.key = t;
		des.setKey(k = t.getBytes());
	}

	public void encryptBlock(byte[] block, int start)
	{
		block[start    ] ^= k[7 ];
		block[start + 1] ^= k[8 ];
		block[start + 2] ^= k[9 ];
		block[start + 3] ^= k[10];
		block[start + 4] ^= k[11];
		block[start + 5] ^= k[12];
		block[start + 6] ^= k[13];
		block[start + 7] ^= k[14];
		des.encryptBlock(block, start);
		block[start++] ^= k[15];
		block[start++] ^= k[16];
		block[start++] ^= k[17];
		block[start++] ^= k[18];
		block[start++] ^= k[19];
		block[start++] ^= k[20];
		block[start++] ^= k[21];
		block[start++] ^= k[22];
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		System.arraycopy(block, start0, out, start1, 8);
		out[start1    ] ^= k[7 ];
		out[start1 + 1] ^= k[8 ];
		out[start1 + 2] ^= k[9 ];
		out[start1 + 3] ^= k[10];
		out[start1 + 4] ^= k[11];
		out[start1 + 5] ^= k[12];
		out[start1 + 6] ^= k[13];
		out[start1 + 7] ^= k[14];
		des.encryptBlock(out, start1);
		out[start1++] ^= k[15];
		out[start1++] ^= k[16];
		out[start1++] ^= k[17];
		out[start1++] ^= k[18];
		out[start1++] ^= k[19];
		out[start1++] ^= k[20];
		out[start1++] ^= k[21];
		out[start1  ] ^= k[22];
	}

	public void reset() {}

	public void wipe()
	{
		Arrays.fill(k, (byte) 0);
		key = null;
		des.wipe();
	}

	public int plaintextSize()
	{
		return 8;
	}

	public int ciphertextSize()
	{
		return 8;
	}

	public void decryptBlock(byte[] block, int start)
	{
		block[start    ] ^= k[15];
		block[start + 1] ^= k[16];
		block[start + 2] ^= k[17];
		block[start + 3] ^= k[18];
		block[start + 4] ^= k[19];
		block[start + 5] ^= k[20];
		block[start + 6] ^= k[21];
		block[start + 7] ^= k[22];
		des.decryptBlock(block, start);
		block[start++] ^= k[7 ];
		block[start++] ^= k[8 ];
		block[start++] ^= k[9 ];
		block[start++] ^= k[10];
		block[start++] ^= k[11];
		block[start++] ^= k[12];
		block[start++] ^= k[13];
		block[start  ] ^= k[14];
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		System.arraycopy(block, start0, out, start1, 8);
		out[start1    ] ^= k[15];
		out[start1 + 1] ^= k[16];
		out[start1 + 2] ^= k[17];
		out[start1 + 3] ^= k[18];
		out[start1 + 4] ^= k[19];
		out[start1 + 5] ^= k[20];
		out[start1 + 6] ^= k[21];
		out[start1 + 7] ^= k[22];
		des.decryptBlock(out, start1);
		out[start1++] ^= k[7 ];
		out[start1++] ^= k[8 ];
		out[start1++] ^= k[9 ];
		out[start1++] ^= k[10];
		out[start1++] ^= k[11];
		out[start1++] ^= k[12];
		out[start1++] ^= k[13];
		out[start1  ] ^= k[14];
	}
	
	public static int test()
	{
		int e = 0;
		try {			
			byte[] ints1 = new byte[23];
			byte[] ints2 = new byte[23];
			RandUtils.fillArr(ints1);
			RandUtils.fillArr(ints2);
			KeyDESX a1 = new KeyDESX(ints1);
			KeyDESX a2 = new KeyDESX(ints2);
			DESX aes = new DESX(a1);
			e += ISymmetric.testSymmetric(aes, a2);
		} catch(Exception ex) {
			Log.err.println("Exception " + ex.getClass().getSimpleName() + " encountered while testing DESX: " + ex.getMessage());
			ex.printStackTrace();
			return ++e;
		}
		return e;
	}

	public KeyFactory<KeyDESX> keyFactory()
	{
		return KeyDESX.factory;
	}

}

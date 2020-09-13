package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeyTDEA;
import claire.util.crypto.rng.RandUtils;
import claire.util.logging.Log;
import claire.util.standards.crypto.ISymmetric;

public class TDEA 
	   implements ISymmetric<KeyTDEA> {

	private KeyTDEA key;
	
	private DES d1 = new DES(),
				d2 = new DES(),
				d3 = new DES();
	
	private byte[] k;
	
	public TDEA() {}
	
	public TDEA(KeyTDEA des)
	{
		this.setKey(des);
	}
	
	public KeyTDEA getKey()
	{
		return key;
	}

	public void setKey(KeyTDEA t)
	{
		this.key = t;
		byte[] raw = t.getBytes();
		d1.setKey(raw, 0 );
		d2.setKey(raw, 7 );
		d3.setKey(raw, 14);
	}

	public void encryptBlock(byte[] block, int start)
	{
		d1.encryptBlock(block, start);
		d2.decryptBlock(block, start);
		d3.encryptBlock(block, start);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		d1.encryptBlock(block, start0, out, start1);
		d2.decryptBlock(out, start1);
		d3.encryptBlock(out, start1);
	}

	public void reset() {}

	public void wipe()
	{
		Arrays.fill(k, (byte) 0);
		key = null;
		d1.wipe();
		d2.wipe();
		d3.wipe();
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
		d3.decryptBlock(block, start);
		d2.encryptBlock(block, start);
		d1.decryptBlock(block, start);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		d3.decryptBlock(block, start0, out, start1);
		d2.encryptBlock(out, start1);
		d1.decryptBlock(out, start1);
	}
	
	public static int test()
	{
		int e = 0;
		try {			
			byte[] ints1 = new byte[21];
			byte[] ints2 = new byte[21];
			RandUtils.fillArr(ints1);
			RandUtils.fillArr(ints2);
			KeyTDEA a1 = new KeyTDEA(ints1);
			KeyTDEA a2 = new KeyTDEA(ints2);
			TDEA aes = new TDEA(a1);
			e += ISymmetric.testSymmetric(aes, a2);
		} catch(Exception ex) {
			Log.err.println("Exception " + ex.getClass().getSimpleName() + " encountered while testing TDEA: " + ex.getMessage());
			ex.printStackTrace();
			return e++;
		}
		return e;
	}

	public KeyFactory<KeyTDEA> keyFactory()
	{
		return KeyTDEA.factory;
	}

}

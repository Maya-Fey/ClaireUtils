package claire.util.standards.crypto;

import claire.util.crypto.rng.RandUtils;
import claire.util.logging.Log;
import claire.util.memory.util.ArrayUtil;

public interface IEncrypter 
	   extends ICrypter {

	void encryptBlock(byte[] block, int start);
	void encryptBlock(byte[] block, int start0, byte[] out, int start1);
	
	default void encryptBlock(byte[] block)
	{
		this.encryptBlock(block, 0);
	}
	
	public static int testDecrypter(IEncrypter dec)
	{
		try {
			byte[] b1 = new byte[dec.plaintextSize()];
			byte[] b2 = new byte[dec.plaintextSize() + 20];
			byte[] b3 = new byte[dec.plaintextSize() + 30];
			RandUtils.fillArr(b1);
			System.arraycopy(b1, 0, b2, 20, b1.length);
			dec.encryptBlock(b1);
			dec.encryptBlock(b2, 20, b3, 30);
			if(ArrayUtil.equals(b1, 0, b3, 30, dec.ciphertextSize()))
				return 0;
			else {
				Log.err.println("In-place and copy encryption do not match for " + dec.getClass().getSimpleName() + " encrypter.");
				return 1;
			}			
		} catch (Exception e) {
			Log.err.println("An unexpected " + e.getClass().getSimpleName() + ": " + e.getMessage() + " occured while testing the functionality of " + dec.getClass().getSimpleName() + " decrypter.");
			return 1;
		}
	}
	
}

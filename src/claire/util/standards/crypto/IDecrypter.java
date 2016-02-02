package claire.util.standards.crypto;

import claire.util.crypto.rng.RandUtils;
import claire.util.logging.Log;
import claire.util.memory.util.ArrayUtil;

public interface IDecrypter 
	   extends ICrypter {
	
	void decryptBlock(byte[] block, int start);
	void decryptBlock(byte[] block, int start0, byte[] out, int start1);
	
	default void decryptBlock(byte[] block)
	{
		this.decryptBlock(block, 0);
	}
	
	public static int testDecrypter(IDecrypter dec)
	{
		try {
			byte[] b1 = new byte[dec.ciphertextSize()];
			byte[] b2 = new byte[dec.ciphertextSize() + 20];
			byte[] b3 = new byte[dec.ciphertextSize() + 30];
			RandUtils.fillArr(b1);
			System.arraycopy(b1, 0, b2, 20, b1.length);
			dec.decryptBlock(b1);
			dec.decryptBlock(b2, 20, b3, 30);
			if(ArrayUtil.equals(b1, 0, b3, 30, dec.plaintextSize()))
				return 0;
			else {
				Log.err.println("In-place and copy decryption do not match for " + dec.getClass().getSimpleName() + " decrypter.");
				return 1;
			}			
		} catch (Exception e) {
			Log.err.println("An unexpected " + e.getClass().getSimpleName() + ": " + e.getMessage() + " occured while testing the functionality of " + dec.getClass().getSimpleName() + " decrypter.");
			return 1;
		}
	}

}

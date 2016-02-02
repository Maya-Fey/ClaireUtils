package claire.util.standards.crypto;

import claire.util.crypto.rng.RandUtils;
import claire.util.logging.Log;
import claire.util.memory.util.ArrayUtil;

public interface ISymmetric<Key extends IKey<Key>>
	   extends ICrypto<Key>, 
	   		   IEncrypter, 
	   		   IDecrypter {
	
	public static int testSymmetric(ISymmetric<?> symm)
	{
		try {
			byte[] b0 = new byte[symm.ciphertextSize()];
			RandUtils.fillArr(b0);
			byte[] b1 = ArrayUtil.copy(b0);
			symm.encryptBlock(b1);
			symm.decryptBlock(b1);
			if(ArrayUtil.equals(b0, b1)) 
				return 0;
			else {
				Log.err.println("Encryption followed by decryption does not yeild the original value for class " + symm.getClass().getSimpleName());
				return 1;
			}
		} catch (Exception e) {
			Log.err.println("An unexpected " + e.getClass().getSimpleName() + ": " + e.getMessage() + " occured while testing the functionality of " + symm.getClass().getSimpleName());
			return 1;
		}
	}
	
}

package claire.util.standards.crypto;

import claire.util.crypto.rng.RandUtils;
import claire.util.logging.Log;
import claire.util.memory.util.ArrayUtil;

public interface ISymmetric<Key extends IKey<?>>
	   extends ICrypto<Key>, 
	   		   IEncrypter, 
	   		   IDecrypter {
	
	public static int testSymmetric(ISymmetric<? extends IKey<?>> cip, IKey<?> rep)
	{
		@SuppressWarnings("unchecked")
		ISymmetric<IKey<?>> symm = (ISymmetric<IKey<?>>) cip;
		int e = 0;
		try {
			byte[] b0 = new byte[symm.ciphertextSize()];
			RandUtils.fillArr(b0);
			byte[] b1 = ArrayUtil.copy(b0);
			byte[] t = new byte[b1.length];
			symm.encryptBlock(b1, 0, t, 0);
			symm.encryptBlock(b1);
			if(!ArrayUtil.equals(t, b1)) {
				Log.err.println("Encryption and offsetted encryption yield different results for class " + symm.getClass().getSimpleName());
				e++;
			}
			symm.decryptBlock(b1, 0, t, 0);
			symm.decryptBlock(b1);
			if(!ArrayUtil.equals(t, b1)) {
				Log.err.println("Decryption and offsetted decryption yield different results for class " + symm.getClass().getSimpleName());
				e++;
			}
			if(!ArrayUtil.equals(b0, b1)) {
				Log.err.println("Encryption followed by decryption does not yield the original value for class " + symm.getClass().getSimpleName());
				e++;
			}
			symm.encryptBlock(b1);
			IKey<?> orig = symm.getKey();
			symm.setKey(rep);
			byte[] b2 = ArrayUtil.copy(b0);
			if(symm.ciphertextSize() == b0.length)
			{
				symm.encryptBlock(b2);
				if(ArrayUtil.equals(b1, b2)) {
					Log.err.println("Changing the key had no effect for class " + symm.getClass().getSimpleName());
					e++;
				}
				symm.decryptBlock(b2);
				if(!ArrayUtil.equals(b0, b2)) {
					Log.err.println("Encryption followed by decryption after key change does not yield the original value for class " + symm.getClass().getSimpleName());
					e++;
				}
			} else {
				symm.setKey(orig);
				if(b0.length != symm.ciphertextSize()) {
					Log.err.println("Resetting key had inconsistent results for class " + symm.getClass().getSimpleName());
					e++;
				}
			}
			
			boolean suc = true;
			try {
				System.arraycopy(b0, 0, b1, 0, b0.length);
				System.arraycopy(b0, 0, b2, 0, b0.length);
				symm.encryptBlock(b1);
				symm.wipe();
				symm.encryptBlock(b2);
			} catch(Exception ex) {
				suc = false;
			}
			if(suc && ArrayUtil.equals(b1, b2)) {
				Log.err.println("Encryption still works after wipe for " + symm.getClass().getSimpleName());
				e++;
			}
			return e;
		} catch (Exception ex) {
			Log.err.println("An unexpected " + ex.getClass().getSimpleName() + ": " + ex.getMessage() + " occured while testing the functionality of " + symm.getClass().getSimpleName());
			ex.printStackTrace();
			return e + 1;
		}
	}
	
}

package claire.util.crypto.cipher.modes;

import claire.util.crypto.KeyFactory;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.ISymmetric;

public class ECB_Cipher<Key extends IKey<Key>, Cipher extends ISymmetric<Key>> 
	   extends BasicMode<Key, Cipher, ECBEncrypter, ECBDecrypter> {

	public ECB_Cipher(Cipher cipher) 
	{
		super(cipher);
		enc = new ECBEncrypter(cipher);
		dec = new ECBDecrypter(cipher);
	}
	
	public void reset()
	{
		enc.reset();
		dec.reset();
	}
	
	public KeyFactory<Key> keyFactory()
	{
		return this.cipher.keyFactory();
	}

}

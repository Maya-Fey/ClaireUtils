package claire.util.crypto.cipher;

import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.ISymmetric;

public class CBC_Cipher<Key extends IKey<Key>, Cipher extends ISymmetric<Key>> 
	   extends BasicMode<Key, Cipher, CBCEncrypter, CBCDecrypter> {

	public CBC_Cipher(Cipher cipher) 
	{
		super(cipher);
		enc = new CBCEncrypter(cipher);
		dec = new CBCDecrypter(cipher);
	}
	
	public void reset()
	{
		enc.reset();
		dec.reset();
	}
	
	public void setIV(byte[] bytes, int start)
	{
		enc.setIV(bytes, start);
		dec.setIV(bytes, start);
	}

}

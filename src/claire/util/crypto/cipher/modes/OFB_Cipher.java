package claire.util.crypto.cipher.modes;

import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.ISymmetric;

public class OFB_Cipher<Key extends IKey<Key>, Cipher extends ISymmetric<Key>> 
	   extends BasicMode<Key, Cipher, OFBEncrypter, OFBDecrypter> {

	public OFB_Cipher(Cipher cipher) 
	{
		super(cipher);
		enc = new OFBEncrypter(cipher);
		dec = new OFBDecrypter(cipher);
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

package claire.util.crypto.cipher;

import claire.util.standards.crypto.ISymmetric;
import claire.util.standards.crypto.ISymmetricKey;

public class OFB_Cipher<Key extends ISymmetricKey<Key>, Cipher extends ISymmetric<Key>> 
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

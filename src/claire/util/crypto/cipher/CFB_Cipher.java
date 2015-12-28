package claire.util.crypto.cipher;

import claire.util.standards.crypto.ISymmetric;
import claire.util.standards.crypto.ISymmetricKey;

public class CFB_Cipher<Key extends ISymmetricKey<Key>, Cipher extends ISymmetric<Key>> 
	   extends BasicMode<Key, Cipher, CFBEncrypter, CFBDecrypter> {

	public CFB_Cipher(Cipher cipher) 
	{
		super(cipher);
		enc = new CFBEncrypter(cipher);
		dec = new CFBDecrypter(cipher);
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

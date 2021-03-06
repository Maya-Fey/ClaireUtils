package claire.util.crypto.cipher.modes;

import claire.util.crypto.KeyFactory;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.ISymmetric;

public class PCBC_Cipher<Key extends IKey<Key>, Cipher extends ISymmetric<Key>> 
	   extends BasicMode<Key, Cipher, PCBCEncrypter, PCBCDecrypter> {

	public PCBC_Cipher(Cipher cipher) 
	{
		super(cipher);
		enc = new PCBCEncrypter(cipher);
		dec = new PCBCDecrypter(cipher);
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
	
	public KeyFactory<Key> keyFactory()
	{
		return this.cipher.keyFactory();
	}

}

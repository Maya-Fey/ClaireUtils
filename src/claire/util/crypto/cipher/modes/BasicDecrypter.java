package claire.util.crypto.cipher.modes;

import claire.util.standards.crypto.IDecrypter;

public abstract class BasicDecrypter
				implements IDecrypter {
	
	protected IDecrypter dec;
	
	protected BasicDecrypter(IDecrypter dec)
	{
		this.dec = dec;
	}

	public int plaintextSize()
	{
		return dec.plaintextSize();
	}

	public int ciphertextSize()
	{
		return dec.ciphertextSize();
	}

}

package claire.util.crypto.cipher;

import claire.util.standards.crypto.IEncrypter;

public abstract class BasicEncrypter
				implements IEncrypter {
	
	protected final IEncrypter enc;
	
	protected BasicEncrypter(IEncrypter enc)
	{
		this.enc = enc;
	}

	public int plaintextSize()
	{
		return enc.plaintextSize();
	}

	public int ciphertextSize()
	{
		return enc.ciphertextSize();
	}

}

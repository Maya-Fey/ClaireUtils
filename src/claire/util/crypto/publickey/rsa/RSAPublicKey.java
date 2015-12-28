package claire.util.crypto.publickey.rsa;

import claire.util.crypto.cipher.StateSpaceRequiredException;
import claire.util.math.UInt;
import claire.util.standards.IUUID;
import claire.util.standards.crypto.IPublicKey;

public abstract class RSAPublicKey<This extends RSAPublicKey<This>> 
				implements IPublicKey<This>,
						   IUUID<This> {

	public void encryptBlock(byte[] block, int start) throws StateSpaceRequiredException
	{
		throw new StateSpaceRequiredException();
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1) throws StateSpaceRequiredException
	{
		throw new StateSpaceRequiredException();
	}
	
	public boolean verify(byte[] signature, int start, byte[] message, int start1) throws StateSpaceRequiredException
	{
		throw new StateSpaceRequiredException();
	}
	
	protected abstract void crypt(UInt msg);
	protected abstract int getLen();
	protected abstract int modLen();

}

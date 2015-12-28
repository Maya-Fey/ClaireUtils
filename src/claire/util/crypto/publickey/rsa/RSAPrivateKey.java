package claire.util.crypto.publickey.rsa;

import claire.util.crypto.cipher.StateSpaceRequiredException;
import claire.util.math.UInt;
import claire.util.standards.IUUID;
import claire.util.standards.crypto.IPrivateKey;

public abstract class RSAPrivateKey<This extends RSAPrivateKey<This>> 
				implements IPrivateKey<This>,
						   IUUID<This> {

	public void decryptBlock(byte[] block, int start) throws StateSpaceRequiredException
	{
		throw new StateSpaceRequiredException();
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1) throws StateSpaceRequiredException
	{
		throw new StateSpaceRequiredException();
	}
	
	public void signBlock(byte[] signature, int start) throws StateSpaceRequiredException
	{
		throw new StateSpaceRequiredException();
	}
	
	public void signBlock(byte[] block, int start0, byte[] out, int start1) throws StateSpaceRequiredException
	{
		throw new StateSpaceRequiredException();
	}
	
	protected abstract void crypt(UInt msg);
	protected abstract int getLen();
	protected abstract int modLen();

}

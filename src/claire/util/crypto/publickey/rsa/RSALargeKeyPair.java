package claire.util.crypto.publickey.rsa;

import claire.util.io.Factory;

public class RSALargeKeyPair 
	   extends StdRSAKeyPair<RSALargeKeyPair, RSAStandardPublicKey, RSAStandardPrivateKey> {

	public RSALargeKeyPair(RSAStandardPublicKey pub, RSAStandardPrivateKey priv) 
	{
		super(pub, priv);
	}
	
	public static final Factory<RSALargeKeyPair> factory = new RSAKeyPairFactory<RSALargeKeyPair, RSAStandardPublicKey, RSAStandardPrivateKey>(RSALargeKeyPair.class, new RSALargeKeyPairConstructor(), RSAStandardPublicKey.factory, RSAStandardPrivateKey.factory);
	
	public Factory<RSALargeKeyPair> factory()
	{
		return factory;
	}
	
	private static final class RSALargeKeyPairConstructor
						 implements PairConstructor<RSALargeKeyPair, RSAStandardPublicKey, RSAStandardPrivateKey>
	{

		public RSALargeKeyPair construct(RSAStandardPublicKey pub, RSAStandardPrivateKey pri)
		{
			return new RSALargeKeyPair(pub, pri);
		}
		
	}	
	
}

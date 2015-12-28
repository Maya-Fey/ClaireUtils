package claire.util.crypto.publickey.rsa;

import claire.util.io.Factory;

public class RSAFastLargeKeyPair 
	   extends StdRSAKeyPair<RSAFastLargeKeyPair, RSAStandardPublicKey, RSAFastPrivateKey> {

	public RSAFastLargeKeyPair(RSAStandardPublicKey pub, RSAFastPrivateKey priv) 
	{
		super(pub, priv);
	}
	
	public static final Factory<RSAFastLargeKeyPair> factory = new RSAKeyPairFactory<RSAFastLargeKeyPair, RSAStandardPublicKey, RSAFastPrivateKey>(RSAFastLargeKeyPair.class, new RSAFastLargeKeyPairConstructor(), RSAStandardPublicKey.factory, RSAFastPrivateKey.factory);
	
	public Factory<RSAFastLargeKeyPair> factory()
	{
		return factory;
	}
	
	private static final class RSAFastLargeKeyPairConstructor
						 implements PairConstructor<RSAFastLargeKeyPair, RSAStandardPublicKey, RSAFastPrivateKey>
	{

		public RSAFastLargeKeyPair construct(RSAStandardPublicKey pub, RSAFastPrivateKey pri)
		{
			return new RSAFastLargeKeyPair(pub, pri);
		}
		
	}	
	
}

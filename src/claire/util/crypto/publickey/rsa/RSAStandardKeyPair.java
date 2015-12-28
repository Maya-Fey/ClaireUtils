package claire.util.crypto.publickey.rsa;

import claire.util.io.Factory;
import claire.util.standards._NAMESPACE;

public class RSAStandardKeyPair 
	   extends StdRSAKeyPair<RSAStandardKeyPair, RSAMicroPublicKey, RSAStandardPrivateKey> {

	public RSAStandardKeyPair(RSAMicroPublicKey pub, RSAStandardPrivateKey priv) 
	{
		super(pub, priv);
	}

	public static final Factory<RSAStandardKeyPair> factory = new RSAKeyPairFactory<RSAStandardKeyPair, RSAMicroPublicKey, RSAStandardPrivateKey>(RSAStandardKeyPair.class, new RSAStandardKeyPairConstructor(), RSAMicroPublicKey.factory, RSAStandardPrivateKey.factory);
	
	public Factory<RSAStandardKeyPair> factory()
	{
		return factory;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.RSAMICROPAIR;
	}
	
	private static final class RSAStandardKeyPairConstructor
						 implements PairConstructor<RSAStandardKeyPair, RSAMicroPublicKey, RSAStandardPrivateKey>
	{

		public RSAStandardKeyPair construct(RSAMicroPublicKey pub, RSAStandardPrivateKey pri)
		{
			return new RSAStandardKeyPair(pub, pri);
		}
		
	}
	
	
}

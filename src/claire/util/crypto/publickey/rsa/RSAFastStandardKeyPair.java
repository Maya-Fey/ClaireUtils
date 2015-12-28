package claire.util.crypto.publickey.rsa;

import claire.util.io.Factory;
import claire.util.standards._NAMESPACE;

public class RSAFastStandardKeyPair 
	   extends StdRSAKeyPair<RSAFastStandardKeyPair, RSAMicroPublicKey, RSAFastPrivateKey> {

	public RSAFastStandardKeyPair(RSAMicroPublicKey pub, RSAFastPrivateKey priv) 
	{
		super(pub, priv);
	}

	public static final Factory<RSAFastStandardKeyPair> factory = new RSAKeyPairFactory<RSAFastStandardKeyPair, RSAMicroPublicKey, RSAFastPrivateKey>(RSAFastStandardKeyPair.class, new RSAFastStandardKeyPairConstructor(), RSAMicroPublicKey.factory, RSAFastPrivateKey.factory);
	
	public Factory<RSAFastStandardKeyPair> factory()
	{
		return factory;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.RSAMICROPAIR;
	}
	
	private static final class RSAFastStandardKeyPairConstructor
						 implements PairConstructor<RSAFastStandardKeyPair, RSAMicroPublicKey, RSAFastPrivateKey>
	{

		public RSAFastStandardKeyPair construct(RSAMicroPublicKey pub, RSAFastPrivateKey pri)
		{
			return new RSAFastStandardKeyPair(pub, pri);
		}
		
	}
	
	
}

package claire.util.crypto.publickey.rsa;

import claire.util.io.Factory;

public class RSAKeyPair<Public extends RSAPublicKey<Public>, Private extends RSAPrivateKey<Private>>
		extends StdRSAKeyPair<RSAKeyPair<Public, Private>, Public, Private> {

	public RSAKeyPair(Public pub, Private priv) {
		super(pub, priv);
		factory = newFactory(pub.factory(), priv.factory());
	}
	
	protected final Factory<RSAKeyPair<Public, Private>> factory;
	
	public Factory<RSAKeyPair<Public, Private>> factory()
	{
		return this.factory;
	}
	
	protected StdRSAKeyPair.PairConstructor<RSAKeyPair<Public, Private>, Public, Private> newConstructor()
	{
		return new RSAKeyPairConstructor<Public, Private>();
	}
	
	@SuppressWarnings("unchecked")
	protected Factory<RSAKeyPair<Public, Private>> newFactory(Factory<Public> pub, Factory<Private> priv)
	{
		return new RSAKeyPairFactory<RSAKeyPair<Public, Private>, Public, Private>((Class<RSAKeyPair<Public, Private>>) this.getClass(), this.newConstructor(), pub, priv);
	}
	
	protected static final class RSAKeyPairConstructor<Public extends RSAPublicKey<Public>,
													   Private extends RSAPrivateKey<Private>>
						   implements PairConstructor<RSAKeyPair<Public, Private>, Public, Private> 
	{

		public RSAKeyPair<Public, Private> construct(Public pub, Private pri)
		{
			return new RSAKeyPair<Public, Private>(pub, pri);
		}
		
	}

}

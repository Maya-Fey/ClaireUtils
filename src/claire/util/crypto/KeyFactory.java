package claire.util.crypto;

import claire.util.io.Factory;
import claire.util.standards.crypto.IRandom;

public abstract class KeyFactory<Type> 
				extends Factory<Type> {

	public KeyFactory(Class<Type> class_) 
	{
		super(class_);
	}

	public abstract Type random(IRandom<?, ?> rand, CryptoString s) throws InstantiationException;

	public abstract int bytesRequired(CryptoString s) throws InstantiationException;

}

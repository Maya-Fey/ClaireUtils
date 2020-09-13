package claire.util.crypto.mac.key;

import claire.util.crypto.KeyFactory;
import claire.util.standards.crypto.IKey;

public abstract class MACKeyFactory<Key extends IKey<Key>> 
				extends KeyFactory<Key> {

	public MACKeyFactory(Class<Key> class_)
	{
		super(class_);
	}

	public abstract Key newFromBytes(byte[] bytes, int start, int len);

}

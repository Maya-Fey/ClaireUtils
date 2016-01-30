package claire.util.standards.crypto;

import claire.util.standards.CObject;

public interface IKey<Key extends IKey<Key>> 
	   extends CObject<Key> {
	
	/**
	 * Erases all secret data in the key. Once complete the object will be unusable.
	 */
	public void erase();

}

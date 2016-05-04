package claire.util.standards.crypto;

import claire.util.crypto.KeyFactory;
import claire.util.logging.Log;
import claire.util.standards.CObject;

public interface IKey<Key extends IKey<Key>> 
	   extends CObject<Key> {
	
	/**
	 * Erases all secret data in the key. Once complete the object will be unusable.
	 */
	public void erase();
	
	KeyFactory<Key> factory();
	
	public static int testKey(IKey<?> key)
	{
		int e = 0;
		try {
			IKey<?> k2 = key.createDeepClone();
			k2.erase();
			boolean suc = true;
			try {
				suc &= key.equals(k2);
			} catch(Exception ex) {
				suc = false;
			}
			if(suc) {
				Log.err.println("Erasing the key of class " + key.getClass().getSimpleName() + " had no effect.");
				e++;
			}
			return e;
		} catch(Exception ex) {
			Log.err.println("An unexpected " + ex.getClass().getSimpleName() + ": " + ex.getMessage() + " occured while testing the functionality of " + key.getClass().getSimpleName());
			return e + 1;
		}
	}

}

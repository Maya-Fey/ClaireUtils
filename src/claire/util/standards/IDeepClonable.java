package claire.util.standards;

import claire.util.logging.Log;

public interface IDeepClonable<T> {
	
	public T createDeepClone();
	
	public static <T extends IDeepClonable<T> & IUUID<T>> int test(T obj)
	{
		T clo;
		try {
			clo = obj.createDeepClone();
		} catch(Exception e) {
			Log.err.println("Error: Exception encountered while deep cloning " + obj.getClass().getSimpleName());
			return 1;
		}
		try {
			if(clo.equals(obj)) {
				return 0;
			} else {
				Log.err.println("Error: Cloned instances of " + obj.getClass().getSimpleName() + " were not equal in value.");
				return 1;
			}
		} catch(Exception e) {
			Log.err.println("Error: Exception encountered while comparing instances of " + obj.getClass().getSimpleName());
			return 1;
		}
		
	}

}

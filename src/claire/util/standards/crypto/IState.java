package claire.util.standards.crypto;

import claire.util.standards.IPersistable;
import claire.util.standards.IUUID;

public interface IState<Type extends IState<Type>> 
	   extends IPersistable<Type>,
	   		   IUUID<Type> {
	
	void erase();

}

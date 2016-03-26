package claire.util.standards.crypto;

import claire.util.standards.IPersistable;

public interface IState<Type extends IState<Type>> 
	   extends IPersistable<Type> {
	
	void erase();

}

package claire.util.standards.crypto;

import claire.util.standards.IPersistable;
import claire.util.standards.IUUID;

public interface IState<Type extends IState<Type>> 
	   extends IPersistable<Type>,
	   		   IUUID<Type> {
	
	void erase();
	
	int stateID();
	
	static int IA = 0;
	static int IBAA = 1;
	static int ISAAC = 2;
	static int RC4_DROP = 3;
	static int RC4 = 4;

}

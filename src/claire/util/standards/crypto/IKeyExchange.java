package claire.util.standards.crypto;

import claire.util.standards.CObject;

public interface IKeyExchange<Type extends IKeyExchange<Type>>
	   extends CObject<Type> {

	void absorb(Type t);
	Type create(Type t);
	
	void erase();
	
}

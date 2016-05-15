package claire.util.standards.crypto;

import claire.util.crypto.KeyFactory;
import claire.util.standards.IPersistable;

public interface IKeyExchange<Private extends IKey<Private>, Public extends IPersistable<Public>> {

	void setPrivate(Private key);
	Private getPrivate();
	
	Public genPublic();
	
	void output(Public other, byte[] bytes, int start);
	void output(Public other, byte[] bytes, int start, boolean consume);
	
	int outputLen();
	
	default byte[] output(Public other)
	{
		byte[] bytes = new byte[outputLen()];
		output(other, bytes, 0);
		return bytes;
	}
	
	KeyFactory<Private> keyFactory();
	
	void erase();
	
}

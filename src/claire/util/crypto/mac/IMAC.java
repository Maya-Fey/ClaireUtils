package claire.util.crypto.mac;

import claire.util.standards.crypto.ICrypto;
import claire.util.standards.crypto.IKey;

public interface IMAC<Key extends IKey<Key>> 
	   extends ICrypto<Key> {
	
	void auth(byte[] message, int start0, int len, byte[] out, int start1);
	
	void start();
	void add(byte[] data, int start, int len);
	void finish(byte[] out, int start);
	
	int outputLength();
	
}

package claire.util.crypto.cipher.key.block;

import java.io.IOException;

import claire.util.crypto.KeyFactory;
import claire.util.standards.crypto.IKey;
import claire.util.standards.io.IOutgoingStream;

public class KeyTwofish 
	   implements IKey<KeyTwofish> {

	@Override
	public KeyTwofish createDeepClone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void export(IOutgoingStream stream) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void export(byte[] bytes, int offset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int exportSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int NAMESPACE() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean sameAs(KeyTwofish obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void erase() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public KeyFactory<KeyTwofish> factory() {
		// TODO Auto-generated method stub
		return null;
	}

}

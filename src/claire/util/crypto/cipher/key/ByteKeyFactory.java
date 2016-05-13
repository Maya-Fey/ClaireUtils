package claire.util.crypto.cipher.key;

import java.io.IOException;

import claire.util.crypto.KeyFactory;
import claire.util.io.IOUtils;
import claire.util.standards.io.IIncomingStream;

public abstract class ByteKeyFactory<Key extends ByteKey<Key>>
	   		    extends KeyFactory<Key> {

	public ByteKeyFactory(Class<Key> class_) 
	{
		super(class_);
	}

	public Key resurrect(byte[] data, int start) throws InstantiationException
	{
		return construct(IOUtils.readByteArr(data, start));
	}

	public Key resurrect(IIncomingStream stream) throws InstantiationException, IOException
	{
		return construct(stream.readByteArr());
	}
	
	protected abstract Key construct(byte[] key);

}

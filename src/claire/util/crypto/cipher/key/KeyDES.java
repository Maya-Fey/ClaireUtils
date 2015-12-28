package claire.util.crypto.cipher.key;

import claire.util.standards._NAMESPACE;

public class KeyDES extends ByteKey<KeyDES> {

	public KeyDES(byte[] key)
	{
		super(KeyDES.class, key);
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYDES;
	}

	protected int getLength(byte[] bytes, int len)
	{
		return 7;
	}

	protected KeyDES construct(byte[] bytes)
	{
		return new KeyDES(bytes);
	}

}

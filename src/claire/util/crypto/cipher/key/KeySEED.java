package claire.util.crypto.cipher.key;

import claire.util.standards._NAMESPACE;

public class KeySEED extends ByteKey<KeySEED> {

	public KeySEED(byte[] key)
	{
		super(KeySEED.class, key);
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYSEED;
	}

	protected int getLength(byte[] bytes, int len)
	{
		return 16;
	}

	protected KeySEED construct(byte[] bytes)
	{
		return new KeySEED(bytes);
	}

}

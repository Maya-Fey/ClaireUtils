package claire.util.crypto.cipher.key;

import claire.util.standards._NAMESPACE;

public class KeySkipjack 
	   extends ByteKey<KeySkipjack> {

	public KeySkipjack(byte[] key, int size) 
	{
		super(KeySkipjack.class, key, size);
	}
	
	public KeySkipjack(byte[] key)
	{
		super(KeySkipjack.class, key);
	}
	
	protected int getLength(byte[] bytes, int size)
	{
		return 10;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYSKIPJACK;
	}

	protected KeySkipjack construct(byte[] bytes)
	{
		return new KeySkipjack(bytes);
	}

}

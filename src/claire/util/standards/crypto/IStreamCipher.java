package claire.util.standards.crypto;

public interface IStreamCipher<Type extends IKey<?>>
	   extends ICrypto<Type> {
	
	void reset();
	void wipe();
	
	byte nextByte();
	byte[] getBytes();
	
	void fill(byte[] arr);
	
	default byte crypt(byte in)
	{
		return in ^= nextByte();
	}

	default void crypt(byte[] bytes, int start, int len)
	{
		while(len-- > 0)
		{
			bytes[start++] ^= nextByte();
		}
	}
}

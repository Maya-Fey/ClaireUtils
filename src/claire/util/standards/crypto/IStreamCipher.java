package claire.util.standards.crypto;

public interface IStreamCipher<Type extends IKey<?>>
	   extends ICrypto<Type> {
	
	void reset();
	void wipe();
	
	byte nextByte();
	void fill(byte[] arr, int start, int len);
	
	default byte[] getBytes(int amt)
	{
		byte[] bytes = new byte[amt];
		this.fill(bytes, 0, amt);
		return bytes;
	}
	
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

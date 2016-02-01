package claire.util.crypto.cipher.key;

import java.io.IOException;
import java.util.Arrays;

import claire.util.io.Factory;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeySkipjack 
	   implements IKey<KeySkipjack> {
	
	private byte[] bytes;
	
	public KeySkipjack(byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public KeySkipjack createDeepClone()
	{
		return new KeySkipjack(ArrayUtil.copy(bytes));
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeBytes(bytes);
	}

	public void export(byte[] bytes, int offset)
	{
		System.arraycopy(this.bytes, 0, bytes, offset, 10);
	}
	
	public int exportSize()
	{
		return bytes.length * 4;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYSKIPJACK;
	}

	public boolean sameAs(KeySkipjack obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes);
	}

	public void erase()
	{
		Arrays.fill(bytes, (byte) 0);
		this.bytes = null;
	}
	
	public Factory<KeySkipjack> factory()
	{
		return factory;
	}
	
	public static final KeySkipjackFactory factory = new KeySkipjackFactory();

	public static final class KeySkipjackFactory extends Factory<KeySkipjack> {

		protected KeySkipjackFactory() 
		{
			super(KeySkipjack.class);
		}

		public KeySkipjack resurrect(byte[] data, int start) throws InstantiationException
		{
			byte[] bytes = new byte[10];
			System.arraycopy(data, start, bytes, 0, 10);
			return new KeySkipjack(bytes);
		}

		public KeySkipjack resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeySkipjack(stream.readBytes(10));
		}
		
	}

}

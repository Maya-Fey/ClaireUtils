package claire.util.crypto.cipher.key;

import java.io.IOException;
import java.util.Arrays;

import claire.util.io.Factory;
import claire.util.io.IOUtils;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeyBlowfish 
	   implements IKey<KeyBlowfish> {
	
	private byte[] bytes;
	
	public KeyBlowfish(byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.KEYBLOWFISH;
	}

	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public boolean sameAs(KeyBlowfish obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes);
	}

	public KeyBlowfish createDeepClone()
	{
		return new KeyBlowfish(ArrayUtil.copy(bytes));
	}

	public void erase()
	{
		Arrays.fill(bytes, (byte) 0);
		bytes = null;
	}
	
	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeByteArr(bytes);
	}

	public void export(byte[] bytes, int offset)
	{
		IOUtils.writeArr(this.bytes, bytes, offset);
	}

	public int exportSize()
	{
		return bytes.length + 4;
	}

	public Factory<KeyBlowfish> factory()
	{
		return factory;
	}

	private static final KeyBlowfishFactory factory = new KeyBlowfishFactory();

	private static final class KeyBlowfishFactory extends Factory<KeyBlowfish> {

		protected KeyBlowfishFactory()
		{
			super(KeyBlowfish.class);
		}

		public KeyBlowfish resurrect(byte[] data, int start) throws InstantiationException
		{
			return new KeyBlowfish(IOUtils.readByteArr(data, start));
		}

		public KeyBlowfish resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyBlowfish(stream.readByteArr());
		}
		
		
		
	}
}

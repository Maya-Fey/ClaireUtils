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

public class KeyCAST5 
	   implements IKey<KeyCAST5> {
	
	private byte[] bytes;
	
	public KeyCAST5(byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.KEYCAST5;
	}

	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public boolean sameAs(KeyCAST5 obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes);
	}

	public KeyCAST5 createDeepClone()
	{
		return new KeyCAST5(ArrayUtil.copy(bytes));
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

	public Factory<KeyCAST5> factory()
	{
		return factory;
	}

	private static final KeyCAST5Factory factory = new KeyCAST5Factory();

	private static final class KeyCAST5Factory extends Factory<KeyCAST5> {

		protected KeyCAST5Factory()
		{
			super(KeyCAST5.class);
		}

		public KeyCAST5 resurrect(byte[] data, int start) throws InstantiationException
		{
			return new KeyCAST5(IOUtils.readByteArr(data, start));
		}

		public KeyCAST5 resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyCAST5(stream.readByteArr());
		}
		
		
		
	}
}

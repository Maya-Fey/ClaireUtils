package claire.util.crypto.cipher.key;

import java.io.IOException;
import java.util.Arrays;

import claire.util.io.IOUtils;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.crypto.IKey;
import claire.util.standards.io.IOutgoingStream;

public abstract class ByteKey<Key extends ByteKey<Key>>
	   			implements IKey<Key> {
	
	protected byte[] bytes;
	
	public ByteKey(byte[] bytes)
	{
		this.bytes = bytes;
	}

	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public boolean sameAs(Key obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes);
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

}

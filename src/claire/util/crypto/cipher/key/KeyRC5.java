package claire.util.crypto.cipher.key;

import java.io.IOException;
import java.util.Arrays;

import claire.util.io.Factory;
import claire.util.io.IOUtils;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeyRC5 
	   implements IKey<KeyRC5> {
	
	private byte[] bytes;
	private int rounds;
	
	public KeyRC5(byte[] bytes, int rounds)
	{
		this.bytes = bytes;
		this.rounds = rounds;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.KEYRC5;
	}

	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public int getRounds()
	{
		return this.rounds;
	}
	
	public boolean sameAs(KeyRC5 obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes) && rounds == obj.rounds;
	}

	public KeyRC5 createDeepClone()
	{
		return new KeyRC5(ArrayUtil.copy(bytes), rounds);
	}

	public void erase()
	{
		Arrays.fill(bytes, (byte) 0);
		rounds = 0;
		bytes = null;
	}
	
	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInt(rounds);
		stream.writeByteArr(bytes);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.intToBytes(rounds, bytes, offset); offset += 4;
		IOUtils.writeArr(this.bytes, bytes, offset);
	}

	public int exportSize()
	{
		return bytes.length + 8;
	}

	public Factory<KeyRC5> factory()
	{
		return factory;
	}

	private static final KeyRC5Factory factory = new KeyRC5Factory();

	private static final class KeyRC5Factory extends Factory<KeyRC5> {

		protected KeyRC5Factory()
		{
			super(KeyRC5.class);
		}

		public KeyRC5 resurrect(byte[] data, int start) throws InstantiationException
		{
			int rounds = Bits.intFromBytes(data, start); start += 4;
			return new KeyRC5(IOUtils.readByteArr(data, start), rounds);
		}

		public KeyRC5 resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			int rounds = stream.readInt();
			return new KeyRC5(stream.readByteArr(), rounds);
		}
		
		
		
	}
}

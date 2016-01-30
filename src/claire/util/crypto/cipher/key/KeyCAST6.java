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

public class KeyCAST6 
	   implements IKey<KeyCAST6> {
	
	private byte[] bytes;
	private int rounds;
	
	/*
	 * Note: good round choice is twelve
	 */
	
	public KeyCAST6(byte[] bytes, int rounds)
	{
		this.bytes = bytes;
		this.rounds = rounds;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.KEYCAST6;
	}

	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public int getRounds()
	{
		return this.rounds;
	}
	
	public boolean sameAs(KeyCAST6 obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes) && rounds == obj.rounds;
	}

	public KeyCAST6 createDeepClone()
	{
		return new KeyCAST6(ArrayUtil.copy(bytes), rounds);
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

	public Factory<KeyCAST6> factory()
	{
		return factory;
	}

	private static final KeyCAST6Factory factory = new KeyCAST6Factory();

	private static final class KeyCAST6Factory extends Factory<KeyCAST6> {

		protected KeyCAST6Factory()
		{
			super(KeyCAST6.class);
		}

		public KeyCAST6 resurrect(byte[] data, int start) throws InstantiationException
		{
			int rounds = Bits.intFromBytes(data, start); start += 4;
			return new KeyCAST6(IOUtils.readByteArr(data, start), rounds);
		}

		public KeyCAST6 resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			int rounds = stream.readInt();
			return new KeyCAST6(stream.readByteArr(), rounds);
		}
		
		
		
	}
}

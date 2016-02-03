package claire.util.crypto.cipher.key;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.io.IOUtils;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
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
	
	public KeyCAST6(final byte[] bytes, final int rounds)
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
	
	public boolean sameAs(final KeyCAST6 obj)
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
	
	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeInt(rounds);
		stream.writeByteArr(bytes);
	}

	public void export(final byte[] bytes, int offset)
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

		public KeyCAST6 resurrect(final byte[] data, int start) throws InstantiationException
		{
			final int rounds = Bits.intFromBytes(data, start); start += 4;
			return new KeyCAST6(IOUtils.readByteArr(data, start), rounds);
		}

		public KeyCAST6 resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			int rounds = stream.readInt();
			return new KeyCAST6(stream.readByteArr(), rounds);
		}
		
	}
	
	public static final int test()
	{
		final byte[] ints = new byte[32];
		RandUtils.fillArr(ints);
		KeyCAST6 aes = new KeyCAST6(ints, 12);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}
	
}

package claire.util.crypto.cipher.key.stream;

import java.io.IOException;
import java.util.Arrays;

import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeyIA implements IKey<KeyIA> {
	
	/*
	 * WARNING: THIS CODE MAY NOT CONFORM TO ANY STANDARDS THAT MAY EXIST
	 * FOR THIS CIPHER. THIS CODE IS BASED UPON http://burtleburtle.net/bob/rand/isaac.html#IAcode.
	 * WHILE THE CODE ON THE CIPHER WAR CONCISE, THERE WAS NO MENTION WHATSOEVER
	 * OF A KEY SCHEDULE. THIS CLASS USES EXCLUSIVELY "FILLED-IN" DETAILS.
	 */
	
	private int[] ints;
	
	public KeyIA(int[] ints) 
	{
		this.ints = ints;
	}
	
	public KeyIA(byte[] bytes)
	{
		ints = new int[256];
		if(bytes.length < 1024)
			Bits.bytesToIntsFull(bytes, 0, ints, 0);
		else
			Bits.bytesToInts(bytes, 0, ints, 0, 256);
	}

	public KeyIA createDeepClone()
	{
		return new KeyIA(ArrayUtil.copy(ints));
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.KEYIA;
	}

	public boolean sameAs(KeyIA obj)
	{
		return ArrayUtil.equals(this.ints, obj.ints);
	}
	
	public void erase()
	{
		Arrays.fill(ints, 0);
		ints = null;
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInts(ints);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.intsToBytes(ints, 0, bytes, offset);
	}

	public int exportSize()
	{
		return 1024;
	}

	public Factory<KeyIA> factory()
	{
		return factory;
	}
	
	public static final KeyIAFactory factory = new KeyIAFactory();
	
	private static final class KeyIAFactory extends Factory<KeyIA>
	{
		public KeyIAFactory()
		{
			super(KeyIA.class);
		}

		public KeyIA resurrect(byte[] data, int start) throws InstantiationException
		{
			int[] ints = new int[256];
			Bits.bytesToInts(data, start, ints, 0, 256);
			return new KeyIA(ints);
		}

		public KeyIA resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyIA(stream.readInts(256));
		}
	}

}

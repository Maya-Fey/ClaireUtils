package claire.util.crypto.cipher.key;

import java.io.IOException;
import java.util.Arrays;

import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeySEED 
	   implements IKey<KeySEED> {
	
	private int[] ints;
	
	public KeySEED(int[] ints)
	{
		this.ints = ints;
	}
	
	public int[] getInts()
	{
		return this.ints;
	}
	
	public KeySEED createDeepClone()
	{
		return new KeySEED(ArrayUtil.copy(ints));
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInts(ints);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.intsToBytes(ints, 0, bytes, offset, 4);
	}
	
	public int exportSize()
	{
		return ints.length * 4;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYSEED;
	}

	public boolean sameAs(KeySEED obj)
	{
		return ArrayUtil.equals(ints, obj.ints);
	}

	public void erase()
	{
		Arrays.fill(ints, 0);
		this.ints = null;
	}
	
	public Factory<KeySEED> factory()
	{
		return factory;
	}
	
	public static final KeySEEDFactory factory = new KeySEEDFactory();

	public static final class KeySEEDFactory extends Factory<KeySEED> {

		protected KeySEEDFactory() 
		{
			super(KeySEED.class);
		}

		public KeySEED resurrect(byte[] data, int start) throws InstantiationException
		{
			int[] ints = new int[4];
			Bits.bytesToInts(data, start, ints, 0, 4);
			return new KeySEED(ints);
		}

		public KeySEED resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeySEED(stream.readInts(4));
		}
		
	}

}

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

public class KeyTEA 
	   implements IKey<KeyTEA> {
	
	/*
	 * 16 byte keys
	 */
	
	private int[] ints;
	
	public KeyTEA(int[] ints)
	{
		this.ints = ints;
	}
	
	public int[] getInts()
	{
		return this.ints;
	}
	
	public KeyTEA createDeepClone()
	{
		return new KeyTEA(ArrayUtil.copy(ints));
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
		return ints.length * 4 + 4;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYTEA;
	}

	public boolean sameAs(KeyTEA obj)
	{
		return ArrayUtil.equals(ints, obj.ints);
	}

	public void erase()
	{
		Arrays.fill(ints, 0);
		this.ints = null;
	}
	
	public Factory<KeyTEA> factory()
	{
		return factory;
	}
	
	public static final KeyTEAFactory factory = new KeyTEAFactory();

	public static final class KeyTEAFactory extends Factory<KeyTEA> {

		protected KeyTEAFactory() 
		{
			super(KeyTEA.class);
		}

		public KeyTEA resurrect(byte[] data, int start) throws InstantiationException
		{
			int[] ints = new int[4];
			Bits.bytesToInts(data, start, ints, 0, 4);
			return new KeyTEA(ints);
		}

		public KeyTEA resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyTEA(stream.readInts(4));
		}
		
	}

}

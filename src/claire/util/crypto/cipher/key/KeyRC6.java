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

public class KeyRC6 
	   implements IKey<KeyRC6> {
	
	private int[] ints;
	private int rounds;
	
	private KeyRC6(int[] ints, int rounds)
	{
		this.ints = ints;
		this.rounds = rounds;
	}

	public int getRounds()
	{
		return this.rounds;
	}
	
	public int[] getInts()
	{
		return this.ints;
	}
	
	public KeyRC6 createDeepClone()
	{
		return new KeyRC6(ArrayUtil.copy(ints), rounds);
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInt(rounds);
		stream.writeIntArr(ints);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.intToBytes(rounds, bytes, offset); offset += 4;
		IOUtils.writeArr(ints, bytes, offset);
	}
	
	public int exportSize()
	{
		return ints.length * 4 + 8;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYRC6;
	}

	public boolean sameAs(KeyRC6 obj)
	{
		return ArrayUtil.equals(ints, obj.ints);
	}

	public void erase()
	{
		Arrays.fill(ints, 0);
		this.rounds = 0;
		this.ints = null;
	}
	
	public Factory<KeyRC6> factory()
	{
		return factory;
	}
	
	public static final KeyRC6Factory factory = new KeyRC6Factory();

	public static final class KeyRC6Factory extends Factory<KeyRC6> {

		protected KeyRC6Factory() 
		{
			super(KeyRC6.class);
		}

		public KeyRC6 resurrect(byte[] data, int start) throws InstantiationException
		{
			int[] ints = IOUtils.readIntArr(data, start);
			start += ints.length * 4;
			return new KeyRC6(ints, Bits.intFromBytes(data, start));
		}

		public KeyRC6 resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			int rounds = stream.readInt();
			return new KeyRC6(stream.readIntArr(), rounds);
		}
		
	}

}

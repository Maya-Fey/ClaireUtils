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

public class KeyRC6 
	   implements IKey<KeyRC6> {
	
	private int[] ints;
	private int rounds;
	
	public KeyRC6(final int[] ints, final int rounds)
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

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeInt(rounds);
		stream.writeIntArr(ints);
	}

	public void export(final byte[] bytes, int offset)
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

	public boolean sameAs(final KeyRC6 obj)
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

		public KeyRC6 resurrect(final byte[] data, int start) throws InstantiationException
		{
			int r = Bits.intFromBytes(data, start); start += 4;
			return new KeyRC6(IOUtils.readIntArr(data, start), r);
		}

		public KeyRC6 resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			int rounds = stream.readInt();
			return new KeyRC6(stream.readIntArr(), rounds);
		}
		
	}
	
	public static final int test()
	{
		final int[] ints = new int[16];
		RandUtils.fillArr(ints);
		KeyRC6 aes = new KeyRC6(ints, 12);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		return i;
	}

}

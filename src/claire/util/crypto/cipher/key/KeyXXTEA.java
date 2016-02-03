package claire.util.crypto.cipher.key;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeyXXTEA 
	   implements IKey<KeyXXTEA> {
	
	/*
	 * 16 byte keys
	 */
	
	private int[] ints;
	private int words;
	
	public KeyXXTEA(final int[] ints, final int words)
	{
		this.ints = ints;
		this.words = words;
	}
	
	public int getWords()
	{
		return this.words;
	}
	
	public int[] getInts()
	{
		return this.ints;
	}
	
	public KeyXXTEA createDeepClone()
	{
		return new KeyXXTEA(ArrayUtil.copy(ints), words);
	}

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeInt(words);
		stream.writeInts(ints);
	}

	public void export(final byte[] bytes, int offset)
	{
		Bits.intToBytes(words, bytes, offset); offset += 4;
		Bits.intsToBytes(ints, 0, bytes, offset, 4);
	}
	
	public int exportSize()
	{
		return 20;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYXTEA;
	}

	public boolean sameAs(final KeyXXTEA obj)
	{
		return ArrayUtil.equals(ints, obj.ints) && words == obj.words;
	}

	public void erase()
	{
		Arrays.fill(ints, 0);
		this.ints = null;
		this.words = 0;
	}
	
	public Factory<KeyXXTEA> factory()
	{
		return factory;
	}
	
	public static final KeyTEAFactory factory = new KeyTEAFactory();

	public static final class KeyTEAFactory extends Factory<KeyXXTEA> {

		protected KeyTEAFactory() 
		{
			super(KeyXXTEA.class);
		}

		public KeyXXTEA resurrect(final byte[] data, int start) throws InstantiationException
		{
			int words = Bits.intFromBytes(data, start); 
			start += 4;
			int[] ints = new int[4];
			Bits.bytesToInts(data, start, ints, 0, 4);
			return new KeyXXTEA(ints, words);
		}

		public KeyXXTEA resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			final int words = stream.readInt();
			return new KeyXXTEA(stream.readInts(4), words);
		}
		
	}
	
	public static final int test()
	{
		final int[] ints = new int[4];
		RandUtils.fillArr(ints);
		KeyXXTEA aes = new KeyXXTEA(ints, 32);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

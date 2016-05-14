package claire.util.crypto.cipher.key.block;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeySEED 
	   implements IKey<KeySEED> {
	
	private int[] ints;
	
	public KeySEED(final int[] ints)
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

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeInts(ints);
	}

	public void export(final byte[] bytes, final int offset)
	{
		Bits.intsToBytes(ints, 0, bytes, offset, 4);
	}
	
	public int exportSize()
	{
		return 16;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYSEED;
	}

	public boolean sameAs(final KeySEED obj)
	{
		return ArrayUtil.equals(ints, obj.ints);
	}

	public void erase()
	{
		Arrays.fill(ints, 0);
		this.ints = null;
	}
	
	public KeyFactory<KeySEED> factory()
	{
		return factory;
	}
	
	public static final KeySEEDFactory factory = new KeySEEDFactory();

	public static final class KeySEEDFactory extends KeyFactory<KeySEED> {

		protected KeySEEDFactory() 
		{
			super(KeySEED.class);
		}

		public KeySEED resurrect(final byte[] data, final int start) throws InstantiationException
		{
			final int[] ints = new int[4];
			Bits.bytesToInts(data, start, ints, 0, 4);
			return new KeySEED(ints);
		}

		public KeySEED resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeySEED(stream.readInts(4));
		}

		public KeySEED random(IRandom<?, ?> rand, CryptoString s)
		{
			return new KeySEED(rand.readInts(4));
		}
		
	}
	
	public static final int test()
	{
		final int[] ints = new int[4];
		RandUtils.fillArr(ints);
		KeySEED aes = new KeySEED(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

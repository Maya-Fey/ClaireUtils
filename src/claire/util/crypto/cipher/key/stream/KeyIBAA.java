package claire.util.crypto.cipher.key.stream;

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

public class KeyIBAA implements IKey<KeyIBAA> {
	
	/*
	 * WARNING: THIS CODE MAY NOT CONFORM TO ANY STANDARDS THAT MAY EXIST
	 * FOR THIS CIPHER. THIS CODE IS BASED UPON http://burtleburtle.net/bob/rand/isaac.html#IBAAcode.
	 * WHILE THE CODE ON THE CIPHER WAR CONCISE, THERE WAS NO MENTION WHATSOEVER
	 * OF A KEY SCHEDULE. THIS CLASS USES EXCLUSIVELY "FILLED-IN" DETAILS.
	 */
	
	private int[] ints;
	
	/**
	 * Creates a new IBAA key from an existing internal array.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * 	<li>An integer array containing exactly 256 integers.</li>
	 * </ul>
	 * If the array contains an amount other than 256 integers, the persistability of this class
	 * will be compromised. If it contains less then 256, IBAA will not work when used with they key.
	 */
	public KeyIBAA(final int[] ints) 
	{
		this.ints = ints;
	}
	
	/**
	 * Creates a new IBAA key from a byte.<br>
	 * WARNING: It is recommended that you produces a full 256-integer or 1024-byte key
	 * first. This simply adds null padding after your bytes, which may have
	 * adverse effects on the security of IBAA. USE AT YOUR OWN RISK.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * 	<li>An byte array of any size greater than zero.</li>
	 * </ul>
	 * An array of zero or a null value results in undefined behavior.
	 */
	public KeyIBAA(final byte[] bytes)
	{
		ints = new int[256];
		if(bytes.length < 1024)
			Bits.bytesToIntsFull(bytes, 0, ints, 0);
		else
			Bits.bytesToInts(bytes, 0, ints, 0, 256);
	}
	
	/**
	 * Returns the integer array held by this key. Should be used internally by IBAA only.
	 * <br><Br>
	 * Returns: An integer array of exactly 256 ints, <i>assuming</i> this key was constructed correctly.
	 */
	public int[] getInts()
	{
		return this.ints;
	}

	public KeyIBAA createDeepClone()
	{
		return new KeyIBAA(ArrayUtil.copy(ints));
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.KEYIBAA;
	}

	public boolean sameAs(final KeyIBAA obj)
	{
		return ArrayUtil.equals(this.ints, obj.ints);
	}
	
	public void erase()
	{
		Arrays.fill(ints, 0);
		ints = null;
	}

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeInts(ints);
	}

	public void export(final byte[] bytes, final int offset)
	{
		Bits.intsToBytes(ints, 0, bytes, offset);
	}

	public int exportSize()
	{
		return 1024;
	}

	public KeyFactory<KeyIBAA> factory()
	{
		return factory;
	}
	
	public static final KeyIBAAFactory factory = new KeyIBAAFactory();
	
	protected static class KeyIBAAFactory extends KeyFactory<KeyIBAA>
	{
		public KeyIBAAFactory()
		{
			super(KeyIBAA.class);
		}

		public KeyIBAA resurrect(final byte[] data, final int start) throws InstantiationException
		{
			final int[] ints = new int[256];
			Bits.bytesToInts(data, start, ints, 0, 256);
			return new KeyIBAA(ints);
		}

		public KeyIBAA resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyIBAA(stream.readInts(256));
		}
		
		public KeyIBAA random(IRandom<?, ?> rand, CryptoString s)
		{
			int[] key = new int[256];
			rand.readInts(key);
			return new KeyIBAA(key);
		}

		public int bytesRequired(CryptoString s)
		{
			return 1024;
		}
	}
	
	public static final int test()
	{
		final int[] ints = new int[256];
		RandUtils.fillArr(ints);
		final KeyIBAA aes = new KeyIBAA(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

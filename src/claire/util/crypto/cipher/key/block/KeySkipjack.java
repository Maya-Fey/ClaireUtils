package claire.util.crypto.cipher.key.block;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeySkipjack 
	   implements IKey<KeySkipjack> {
	
	private byte[] bytes;
	
	public KeySkipjack(final byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public KeySkipjack createDeepClone()
	{
		return new KeySkipjack(ArrayUtil.copy(bytes));
	}

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeBytes(bytes);
	}

	public void export(final byte[] bytes, final int offset)
	{
		System.arraycopy(this.bytes, 0, bytes, offset, 10);
	}
	
	public int exportSize()
	{
		return 10;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYSKIPJACK;
	}

	public boolean sameAs(final KeySkipjack obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes);
	}

	public void erase()
	{
		Arrays.fill(bytes, (byte) 0);
		this.bytes = null;
	}
	
	public KeyFactory<KeySkipjack> factory()
	{
		return factory;
	}
	
	public static final KeySkipjackFactory factory = new KeySkipjackFactory();

	public static final class KeySkipjackFactory extends KeyFactory<KeySkipjack> {

		protected KeySkipjackFactory() 
		{
			super(KeySkipjack.class);
		}

		public KeySkipjack resurrect(final byte[] data, final int start) throws InstantiationException
		{
			byte[] bytes = new byte[10];
			System.arraycopy(data, start, bytes, 0, 10);
			return new KeySkipjack(bytes);
		}

		public KeySkipjack resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeySkipjack(stream.readBytes(10));
		}

		public KeySkipjack random(IRandom<?, ?> rand, CryptoString s)
		{
			byte[] bytes = new byte[10];
			rand.readBytes(bytes);
			return new KeySkipjack(bytes);
		}

		public int bytesRequired(CryptoString s)
		{
			return 10;
		}
		
	}
	
	public static final int test()
	{
		final byte[] bytes = new byte[10];
		RandUtils.fillArr(bytes);
		KeySkipjack aes = new KeySkipjack(bytes);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

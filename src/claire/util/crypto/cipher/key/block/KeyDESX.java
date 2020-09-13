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

public class KeyDESX 
	   implements IKey<KeyDESX> {
	
	private byte[] bytes;
	
	public KeyDESX(final byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public KeyDESX createDeepClone()
	{
		return new KeyDESX(ArrayUtil.copy(bytes));
	}

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeBytes(bytes);
	}

	public void export(final byte[] bytes, final int offset)
	{
		System.arraycopy(this.bytes, 0, bytes, offset, 23);
	}
	
	public int exportSize()
	{
		return 23;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYDESX;
	}

	public boolean sameAs(final KeyDESX obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes);
	}

	public void erase()
	{
		Arrays.fill(bytes, (byte) 0);
		this.bytes = null;
	}
	
	public KeyFactory<KeyDESX> factory()
	{
		return factory;
	}
	
	public static final KeyDESFactory factory = new KeyDESFactory();

	public static final class KeyDESFactory extends KeyFactory<KeyDESX> {

		protected KeyDESFactory() 
		{
			super(KeyDESX.class);
		}

		public KeyDESX resurrect(final byte[] data, final int start) throws InstantiationException
		{
			byte[] bytes = new byte[23];
			System.arraycopy(data, start, bytes, 0, 23);
			return new KeyDESX(bytes);
		}

		public KeyDESX resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyDESX(stream.readBytes(23));
		}

		public KeyDESX random(IRandom<?, ?> rand, CryptoString s)
		{
			return new KeyDESX(rand.readBytes(23));
		}
		
		public int bytesRequired(CryptoString s)
		{
			return 23;
		}
		
	}
	
	public static final int test()
	{
		final byte[] bytes = new byte[23];
		RandUtils.fillArr(bytes);
		KeyDESX aes = new KeyDESX(bytes);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

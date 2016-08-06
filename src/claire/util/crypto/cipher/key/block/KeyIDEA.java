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

public class KeyIDEA 
	   implements IKey<KeyIDEA> {
	
	private byte[] bytes;
	
	public KeyIDEA(final byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public KeyIDEA createDeepClone()
	{
		return new KeyIDEA(ArrayUtil.copy(bytes));
	}

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeBytes(bytes);
	}

	public void export(final byte[] bytes, final int offset)
	{
		System.arraycopy(this.bytes, 0, bytes, offset, 16);
	}
	
	public int exportSize()
	{
		return 16;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYIDEA;
	}

	public boolean sameAs(final KeyIDEA obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes);
	}

	public void erase()
	{
		Arrays.fill(bytes, (byte) 0);
		this.bytes = null;
	}
	
	public KeyFactory<KeyIDEA> factory()
	{
		return factory;
	}
	
	public static final KeyIDEAFactory factory = new KeyIDEAFactory();

	public static final class KeyIDEAFactory extends KeyFactory<KeyIDEA> {

		protected KeyIDEAFactory() 
		{
			super(KeyIDEA.class);
		}

		public KeyIDEA resurrect(final byte[] data, final int start) throws InstantiationException
		{
			byte[] bytes = new byte[16];
			System.arraycopy(data, start, bytes, 0, 16);
			return new KeyIDEA(bytes);
		}

		public KeyIDEA resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyIDEA(stream.readBytes(16));
		}

		public KeyIDEA random(IRandom<?, ?> rand, CryptoString s)
		{
			byte[] bytes = new byte[16];
			rand.readBytes(bytes);
			return new KeyIDEA(bytes);
		}
		
	}
	
	public static final int test()
	{
		final byte[] bytes = new byte[16];
		RandUtils.fillArr(bytes);
		KeyIDEA aes = new KeyIDEA(bytes);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

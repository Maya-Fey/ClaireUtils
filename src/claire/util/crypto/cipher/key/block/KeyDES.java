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

public class KeyDES 
	   implements IKey<KeyDES> {
	
	private byte[] bytes;
	
	public KeyDES(final byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public KeyDES createDeepClone()
	{
		return new KeyDES(ArrayUtil.copy(bytes));
	}

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeBytes(bytes);
	}

	public void export(final byte[] bytes, final int offset)
	{
		System.arraycopy(this.bytes, 0, bytes, offset, 7);
	}
	
	public int exportSize()
	{
		return 7;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYDES;
	}

	public boolean sameAs(final KeyDES obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes);
	}

	public void erase()
	{
		Arrays.fill(bytes, (byte) 0);
		this.bytes = null;
	}
	
	public KeyFactory<KeyDES> factory()
	{
		return factory;
	}
	
	public static final KeyDESFactory factory = new KeyDESFactory();

	public static final class KeyDESFactory extends KeyFactory<KeyDES> {

		protected KeyDESFactory() 
		{
			super(KeyDES.class);
		}

		public KeyDES resurrect(final byte[] data, final int start) throws InstantiationException
		{
			byte[] bytes = new byte[7];
			System.arraycopy(data, start, bytes, 0, 7);
			return new KeyDES(bytes);
		}

		public KeyDES resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyDES(stream.readBytes(7));
		}

		public KeyDES random(IRandom<?, ?> rand, CryptoString s)
		{
			return new KeyDES(rand.readBytes(7));
		}
		
	}
	
	public static final int test()
	{
		final byte[] bytes = new byte[7];
		RandUtils.fillArr(bytes);
		KeyDES aes = new KeyDES(bytes);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

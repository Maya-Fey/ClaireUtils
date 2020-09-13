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

public class KeyTDEA 
	   implements IKey<KeyTDEA> {
	
	private byte[] bytes;
	
	public KeyTDEA(final byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public KeyTDEA createDeepClone()
	{
		return new KeyTDEA(ArrayUtil.copy(bytes));
	}

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeBytes(bytes);
	}

	public void export(final byte[] bytes, final int offset)
	{
		System.arraycopy(this.bytes, 0, bytes, offset, 21);
	}
	
	public int exportSize()
	{
		return 21;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYTDEA;
	}

	public boolean sameAs(final KeyTDEA obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes);
	}

	public void erase()
	{
		Arrays.fill(bytes, (byte) 0);
		this.bytes = null;
	}
	
	public KeyFactory<KeyTDEA> factory()
	{
		return factory;
	}
	
	public static final KeyDESFactory factory = new KeyDESFactory();

	public static final class KeyDESFactory extends KeyFactory<KeyTDEA> {

		protected KeyDESFactory() 
		{
			super(KeyTDEA.class);
		}

		public KeyTDEA resurrect(final byte[] data, final int start) throws InstantiationException
		{
			byte[] bytes = new byte[21];
			System.arraycopy(data, start, bytes, 0, 21);
			return new KeyTDEA(bytes);
		}

		public KeyTDEA resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyTDEA(stream.readBytes(21));
		}

		public KeyTDEA random(IRandom<?, ?> rand, CryptoString s)
		{
			return new KeyTDEA(rand.readBytes(21));
		}

		public int bytesRequired(CryptoString s)
		{
			return 21;
		}
		
	}
	
	public static final int test()
	{
		final byte[] bytes = new byte[21];
		RandUtils.fillArr(bytes);
		KeyTDEA aes = new KeyTDEA(bytes);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

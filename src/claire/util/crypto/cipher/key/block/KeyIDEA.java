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

public class KeyIDEA 
	   implements IKey<KeyIDEA> {
	
	private short[] shorts;
	
	public KeyIDEA(final short[] shorts)
	{
		this.shorts = shorts;
	}
	
	public short[] getShorts()
	{
		return this.shorts;
	}
	
	public KeyIDEA createDeepClone()
	{
		return new KeyIDEA(ArrayUtil.copy(shorts));
	}

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeShorts(shorts);
	}

	public void export(final byte[] bytes, final int offset)
	{
		Bits.shortsToBytes(shorts, 0, bytes, offset, 8);
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
		return ArrayUtil.equals(shorts, obj.shorts);
	}

	public void erase()
	{
		Arrays.fill(shorts, (short) 0);
		this.shorts = null;
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
			short[] shorts = new short[8];
			Bits.bytesToShorts(data, start, shorts, 0, 8);
			return new KeyIDEA(shorts);
		}

		public KeyIDEA resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyIDEA(stream.readShorts(8));
		}

		public KeyIDEA random(IRandom<?, ?> rand, CryptoString s)
		{
			short[] shorts = new short[8];
			rand.readShorts(shorts);
			return new KeyIDEA(shorts);
		}
		
		public int bytesRequired(CryptoString s)
		{
			return 16;
		}
		
	}
	
	public static final int test()
	{
		final short[] shorts = new short[8];
		RandUtils.fillArr(shorts);
		KeyIDEA aes = new KeyIDEA(shorts);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

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

public class KeyFEAL_CI 
	   implements IKey<KeyFEAL_CI> {
	
	private short[] shorts;
	private int rounds;
	
	public KeyFEAL_CI(final short[] shorts, int rounds)
	{
		this.shorts = shorts;
		this.rounds = rounds;
	}
	
	public short[] getShorts()
	{
		return this.shorts;
	}
	
	public int getRounds()
	{
		return this.rounds;
	}
	
	public KeyFEAL_CI createDeepClone()
	{
		return new KeyFEAL_CI(ArrayUtil.copy(shorts), rounds);
	}

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeInt(rounds);
		stream.writeShorts(shorts);
	}

	public void export(final byte[] bytes, final int offset)
	{
		Bits.intToBytes(rounds, bytes, offset);
		Bits.shortsToBytes(shorts, 0, bytes, offset + 4, 8);
	}
	
	public int exportSize()
	{
		return 20;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYFEAL;
	}

	public boolean sameAs(final KeyFEAL_CI obj)
	{
		return ArrayUtil.equals(shorts, obj.shorts) && this.rounds == obj.rounds;
	}

	public void erase()
	{
		Arrays.fill(shorts, (byte) 0);
		this.rounds = 0;
		this.shorts = null;
	}
	
	public Factory<KeyFEAL_CI> factory()
	{
		return factory;
	}
	
	public static final KeyFEAL_CIFactory factory = new KeyFEAL_CIFactory();

	public static final class KeyFEAL_CIFactory extends Factory<KeyFEAL_CI> {

		protected KeyFEAL_CIFactory() 
		{
			super(KeyFEAL_CI.class);
		}

		public KeyFEAL_CI resurrect(final byte[] data, final int start) throws InstantiationException
		{
			int rounds = Bits.intFromBytes(data, start);
			short[] shorts = new short[8];
			Bits.bytesToShorts(data, start + 4, shorts, 0, 8);
			return new KeyFEAL_CI(shorts, rounds);
		}

		public KeyFEAL_CI resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			int rounds = stream.readInt();
			return new KeyFEAL_CI(stream.readShorts(8), rounds);
		}
		
	}
	
	public static final int test()
	{
		final short[] bytes = new short[8];
		RandUtils.fillArr(bytes);
		KeyFEAL_CI aes = new KeyFEAL_CI(bytes, 5);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

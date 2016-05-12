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

public class KeyFEAL 
	   implements IKey<KeyFEAL> {
	
	private byte[] bytes;
	private int rounds;
	
	public KeyFEAL(final byte[] bytes, int rounds)
	{
		this.bytes = bytes;
		this.rounds = rounds;
	}
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public int getRounds()
	{
		return this.rounds;
	}
	
	public KeyFEAL createDeepClone()
	{
		return new KeyFEAL(ArrayUtil.copy(bytes), rounds);
	}

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeInt(rounds);
		stream.writeBytes(bytes);
	}

	public void export(final byte[] bytes, final int offset)
	{
		Bits.intToBytes(rounds, bytes, offset);
		System.arraycopy(this.bytes, 0, bytes, offset + 4, 8);
	}
	
	public int exportSize()
	{
		return 12;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYFEAL;
	}

	public boolean sameAs(final KeyFEAL obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes) && this.rounds == obj.rounds;
	}

	public void erase()
	{
		Arrays.fill(bytes, (byte) 0);
		this.rounds = 0;
		this.bytes = null;
	}
	
	public KeyFactory<KeyFEAL> factory()
	{
		return factory;
	}
	
	public static final KeyFEALFactory factory = new KeyFEALFactory();

	public static final class KeyFEALFactory extends KeyFactory<KeyFEAL> {

		protected KeyFEALFactory() 
		{
			super(KeyFEAL.class);
		}

		public KeyFEAL resurrect(final byte[] data, final int start) throws InstantiationException
		{
			int rounds = Bits.intFromBytes(data, start);
			byte[] bytes = new byte[8];
			System.arraycopy(data, start + 4, bytes, 0, 8);
			return new KeyFEAL(bytes, rounds);
		}

		public KeyFEAL resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			int rounds = stream.readInt();
			return new KeyFEAL(stream.readBytes(8), rounds);
		}

		public KeyFEAL random(IRandom<?, ?> rand, CryptoString s)
		{
			byte[] bytes = new byte[12];
			rand.readBytes(bytes);
			//Default 8 rounds;
			int rounds;
			if(s.args() > 0) {
				rounds = s.args();
				if(rounds > 4096)
					throw new java.lang.IllegalArgumentException("Excessive amount of rounds specified");
				if(rounds < 0)
					throw new java.lang.IllegalArgumentException("Negative amount of rounds specified");
			} else
				rounds = 8;
			return new KeyFEAL(bytes, rounds);
		}
		
	}
	
	public static final int test()
	{
		final byte[] bytes = new byte[8];
		RandUtils.fillArr(bytes);
		KeyFEAL aes = new KeyFEAL(bytes, 5);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

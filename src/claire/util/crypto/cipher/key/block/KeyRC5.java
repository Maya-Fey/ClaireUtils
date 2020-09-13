package claire.util.crypto.cipher.key.block;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.io.IOUtils;
import claire.util.math.MathHelper;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeyRC5 
	   implements IKey<KeyRC5> {
	
	private byte[] bytes;
	private int rounds;
	
	public KeyRC5(final byte[] bytes, final int rounds)
	{
		this.bytes = bytes;
		this.rounds = rounds;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.KEYRC5;
	}

	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public int getRounds()
	{
		return this.rounds;
	}
	
	public boolean sameAs(KeyRC5 obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes) && rounds == obj.rounds;
	}

	public KeyRC5 createDeepClone()
	{
		return new KeyRC5(ArrayUtil.copy(bytes), rounds);
	}

	public void erase()
	{
		Arrays.fill(bytes, (byte) 0);
		rounds = 0;
		bytes = null;
	}
	
	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeInt(rounds);
		stream.writeByteArr(bytes);
	}

	public void export(final byte[] bytes, int offset)
	{
		Bits.intToBytes(rounds, bytes, offset); offset += 4;
		IOUtils.writeArr(this.bytes, bytes, offset);
	}

	public int exportSize()
	{
		return bytes.length + 8;
	}

	public KeyFactory<KeyRC5> factory()
	{
		return factory;
	}

	public static final KeyRC5Factory factory = new KeyRC5Factory();

	private static final class KeyRC5Factory extends KeyFactory<KeyRC5> {

		protected KeyRC5Factory()
		{
			super(KeyRC5.class);
		}

		public KeyRC5 resurrect(final byte[] data, int start) throws InstantiationException
		{
			int rounds = Bits.intFromBytes(data, start); start += 4;
			return new KeyRC5(IOUtils.readByteArr(data, start), rounds);
		}

		public KeyRC5 resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			int rounds = stream.readInt();
			return new KeyRC5(stream.readByteArr(), rounds);
		}

		public KeyRC5 random(IRandom<?, ?> rand, CryptoString s) throws InstantiationException
		{
			int rounds = 12;
			int len = 128;
			if(s.args() > 0) {
				rounds = s.nextArg().toInt();
				if(rounds < 1)
					throw new java.lang.InstantiationException("RC5 requires at least one round");
				if(rounds > 255)
					throw new java.lang.InstantiationException("RC5 has a maximum of 255 rounds");
				if(s.args() > 1) {
					len = s.nextArg().toInt();
					if(len < 1) 
						throw new java.lang.InstantiationException("RC5 requires at least one key bit");
					if(len > 2048) 
						throw new java.lang.InstantiationException("RC5 has a maximum key length of 2048 bits");
				}
			}
			byte[] bytes = new byte[((len - 1) / 8) + 1];
			rand.readBytes(bytes);
			MathHelper.truncate(bytes, len);
			return new KeyRC5(bytes, rounds);
		}

		public int bytesRequired(CryptoString s)
		{
			int len = 128;
			if(s.args() > 1) {
				len = s.nextArg().toInt();
				if(len < 1) 
					throw new java.lang.IllegalArgumentException("RC5 requires at least one key bit");
				if(len > 2048) 
					throw new java.lang.IllegalArgumentException("RC5 has a maximum key length of 2048 bits");
			}
			return ((len - 1) / 8) + 1;
		}
		
	}

	public static final int test()
	{
		final byte[] ints = new byte[16];
		RandUtils.fillArr(ints);
		KeyRC5 aes = new KeyRC5(ints, 12);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}
	
}

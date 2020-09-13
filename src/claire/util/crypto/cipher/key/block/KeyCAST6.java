package claire.util.crypto.cipher.key.block;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.io.IOUtils;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeyCAST6 
	   implements IKey<KeyCAST6> {
	
	private byte[] bytes;
	private int rounds;
	
	/*
	 * Note: good round choice is twelve
	 */
	
	public KeyCAST6(final byte[] bytes, final int rounds)
	{
		this.bytes = bytes;
		this.rounds = rounds;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.KEYCAST6;
	}

	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public int getRounds()
	{
		return this.rounds;
	}
	
	public boolean sameAs(final KeyCAST6 obj)
	{
		return ArrayUtil.equals(bytes, obj.bytes) && rounds == obj.rounds;
	}

	public KeyCAST6 createDeepClone()
	{
		return new KeyCAST6(ArrayUtil.copy(bytes), rounds);
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

	public KeyFactory<KeyCAST6> factory()
	{
		return factory;
	}

	public static final KeyCAST6Factory factory = new KeyCAST6Factory();

	private static final class KeyCAST6Factory extends KeyFactory<KeyCAST6> {

		protected KeyCAST6Factory()
		{
			super(KeyCAST6.class);
		}

		public KeyCAST6 resurrect(final byte[] data, int start) throws InstantiationException
		{
			final int rounds = Bits.intFromBytes(data, start); start += 4;
			return new KeyCAST6(IOUtils.readByteArr(data, start), rounds);
		}

		public KeyCAST6 resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			int rounds = stream.readInt();
			return new KeyCAST6(stream.readByteArr(), rounds);
		}

		public KeyCAST6 random(IRandom<?, ?> rand, CryptoString s)
		{
			int rounds = 12;
			if(s.args() > 0) {
				rounds = s.nextArg().toInt() & 0xFFFF; //Common-sense limit
				if(rounds < 1)
					throw new java.lang.IllegalArgumentException("You must have at least one round");
			}
			int amt = 32;
			if(s.args() > 1) {
				amt = s.nextArg().toInt();
				if(amt < 1)
					throw new java.lang.IllegalArgumentException("You cannot have a key length less than one");
				if(amt > 32)
					throw new java.lang.IllegalArgumentException("CAST6 can accept a maximum of 32 bytes of key information");
			}
			byte[] bytes = rand.readBytes(amt);
			return new KeyCAST6(bytes, rounds);
		}

		public int bytesRequired(CryptoString s)
		{
			if(s.args() > 1) {
				int amt = s.nextArg().toInt();
				amt = ((amt - 1) / 8) + 1;
				if(amt < 1)
					throw new java.lang.IllegalArgumentException("You cannot have a key length less than one");
				if(amt > 32)
					throw new java.lang.IllegalArgumentException("CAST6 can accept a maximum of 32 bytes of key information");
				return amt;
			}
			return 32;
		}
		
	}
	
	public static final int test()
	{
		final byte[] ints = new byte[32];
		RandUtils.fillArr(ints);
		KeyCAST6 aes = new KeyCAST6(ints, 12);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}
	
}

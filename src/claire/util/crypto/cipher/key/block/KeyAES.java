package claire.util.crypto.cipher.key.block;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.io.IOUtils;
import claire.util.math.MathHelper;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeyAES 
	   implements IKey<KeyAES> {
	
	private int[] ints;
	private int rounds;
	
	public KeyAES(int[] ints)
	{
		this.ints = ints;
		this.rounds = ints.length + 6;
	}
	
	private KeyAES(int[] ints, int rounds)
	{
		this.ints = ints;
		this.rounds = rounds;
	}

	public int getRounds()
	{
		return this.rounds;
	}
	
	public int[] getInts()
	{
		return this.ints;
	}
	
	public KeyAES createDeepClone()
	{
		return new KeyAES(ArrayUtil.copy(ints), rounds);
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeIntArr(ints);
	}

	public void export(byte[] bytes, int offset)
	{
		IOUtils.writeArr(ints, bytes, offset);
	}
	
	public int exportSize()
	{
		return ints.length * 4 + 4;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.AESKEY;
	}

	public boolean sameAs(KeyAES obj)
	{
		return ArrayUtil.equals(ints, obj.ints);
	}

	public void erase()
	{
		Arrays.fill(ints, 0);
		this.rounds = 0;
		this.ints = null;
	}
	
	public KeyFactory<KeyAES> factory()
	{
		return factory;
	}
	
	public static final KeyAESFactory factory = new KeyAESFactory();

	public static final class KeyAESFactory extends KeyFactory<KeyAES> {

		protected KeyAESFactory() 
		{
			super(KeyAES.class);
		}

		public KeyAES resurrect(byte[] data, int start) throws InstantiationException
		{
			return new KeyAES(IOUtils.readIntArr(data, start));
		}

		public KeyAES resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyAES(stream.readIntArr());
		}
		
		

		public KeyAES random(IRandom<?, ?> rand, CryptoString s)
		{
			int bits;
			if(s.args() > 0)
				bits = s.nextArg().toInt();
			else 
				bits = 128;
			if(bits < 0)
				throw new java.lang.IllegalArgumentException("Negative key length specified");
			int[] ints = new int[(bits / 32) + (((bits & 31) > 0) ? 1 : 0)];
			rand.readInts(ints);
			MathHelper.truncate(ints, bits);
			/*if(s.args() > 1) {
				int rounds = s.nextArg().toInt();
				if(rounds > 4096)
					throw new java.lang.IllegalArgumentException("Excessive amount of rounds specified");
				if(rounds < 0)
					throw new java.lang.IllegalArgumentException("Negative amount of rounds specified");
				return new KeyAES(ints, rounds);
			} else */ //TODO: Look into variable round count
				return new KeyAES(ints);
		}
		
	}
	
	public static final int test()
	{
		int[] ints = new int[6];
		RandUtils.fillArr(ints);
		KeyAES aes = new KeyAES(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

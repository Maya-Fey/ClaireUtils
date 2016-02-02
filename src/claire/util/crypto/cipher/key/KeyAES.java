package claire.util.crypto.cipher.key;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.io.IOUtils;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
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
	
	public Factory<KeyAES> factory()
	{
		return factory;
	}
	
	public static final KeyAESFactory factory = new KeyAESFactory();

	public static final class KeyAESFactory extends Factory<KeyAES> {

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
		
	}
	
	public static final int test()
	{
		int[] ints = new int[6];
		RandUtils.fillArr(ints);
		KeyAES aes = new KeyAES(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		return i;
	}

}

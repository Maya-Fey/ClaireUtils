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

public class KeyGOST 
       implements IKey<KeyGOST> {
	
	private static final byte[] DEFAULT = 
	{
        0x04, 0x0A, 0x09, 0x02, 0x0D, 0x08, 0x00, 0x0E,
        0x06, 0x0B, 0x01, 0x0C, 0x07, 0x0F, 0x05, 0x03, 
        0x0E, 0x0B, 0x04, 0x0C, 0x06, 0x0D, 0x0F, 0x0A, 
        0x02, 0x03, 0x08, 0x01, 0x00, 0x07, 0x05, 0x09, 
        0x05, 0x08, 0x01, 0x0D, 0x0A, 0x03, 0x04, 0x02, 
        0x0E, 0x0F, 0x0C, 0x07, 0x06, 0x00, 0x09, 0x0B, 
        0x07, 0x0D, 0x0A, 0x01, 0x00, 0x08, 0x09, 0x0F, 
        0x0E, 0x04, 0x06, 0x0C, 0x0B, 0x02, 0x05, 0x03, 
        0x06, 0x0C, 0x07, 0x01, 0x05, 0x0F, 0x0D, 0x08, 
        0x04, 0x0A, 0x09, 0x0E, 0x00, 0x03, 0x0B, 0x02, 
        0x04, 0x0B, 0x0A, 0x00, 0x07, 0x02, 0x01, 0x0D,
        0x03, 0x06, 0x08, 0x05, 0x09, 0x0C, 0x0F, 0x0E, 
        0x0D, 0x0B, 0x04, 0x01, 0x03, 0x0F, 0x05, 0x09,
        0x00, 0x0A, 0x0E, 0x07, 0x06, 0x08, 0x02, 0x0C, 
        0x01, 0x0F, 0x0D, 0x00, 0x05, 0x07, 0x0A, 0x04,
        0x09, 0x02, 0x03, 0x0E, 0x06, 0x0B, 0x08, 0x0C
    };

	private boolean hasS = false;
	
	private int[] key;
	
	private byte[] sbox;
	
	public KeyGOST(final int[] key) 
	{
		this.key = key;
	}
	
	public KeyGOST(final int[] key, final byte[] sbox)
	{
		this.key = key;
		this.sbox = sbox;
		this.hasS = true;
	}	
	
	public int[] getKey()
	{
		return this.key;
	}
	
	public byte[] getSBOX()
	{
		if(hasS) 
			return sbox;
		else
			return DEFAULT;
	}
	
	public void erase()
	{
		Arrays.fill(key, 0);
		Arrays.fill(sbox, (byte) 0);
		hasS = false;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.KEYGOST;
	}
	
	public boolean sameAs(final KeyGOST o)
	{
		if(hasS == o.hasS)
			if(hasS)
				if(!ArrayUtil.equals(sbox, o.sbox))
					return false;
				else;
			else;
		else
			return false;
		return ArrayUtil.equals(key, o.key);
	}

	public KeyGOST createDeepClone()
	{
		if(hasS)
			return new KeyGOST(ArrayUtil.copy(key), ArrayUtil.copy(sbox));
		else
			return new KeyGOST(ArrayUtil.copy(key));
	}

	public void export(final IOutgoingStream stream) throws IOException
	{
		stream.writeInts(key);
		stream.writeBool(hasS);
		if(hasS)			
			stream.writeNibbles(sbox);
	}

	public void export(final byte[] bytes, int offset)
	{
		Bits.intsToBytes(key, 0, bytes, offset, 8); offset += 32;
		bytes[offset++] = (byte) (hasS ? 1 : 0);
		if(hasS) 
			Bits.nibblesToBytes(sbox, 0, bytes, offset, 128);
	}

	public int exportSize()
	{
		return 33 + (hasS ? 64 : 0);
	}

	public Factory<KeyGOST> factory()
	{
		return factory;
	}
	
	private static final KeyGOSTFactory factory = new KeyGOSTFactory();

	public static final class KeyGOSTFactory extends Factory<KeyGOST> {

		protected KeyGOSTFactory() 
		{
			super(KeyGOST.class);
		}

		public KeyGOST resurrect(final byte[] data, int start) throws InstantiationException
		{
			final int[] key = new int[8];
			Bits.bytesToInts(data, start, key, 0, 8); start += 32;
			if(data[start++] == 1) {
				final byte[] sbox = new byte[128];
				Bits.bytesToNibbles(data, start, sbox, 0, 64);
				return new KeyGOST(key, sbox);
			} else
				return new KeyGOST(key);
		}

		public KeyGOST resurrect(final IIncomingStream stream) throws InstantiationException, IOException
		{
			final int[] key = stream.readInts(8);
			if(stream.readBool())
				return new KeyGOST(key, stream.readNibbles(128));
			else
				return new KeyGOST(key);
		}
		
	}
	
	public static final int test()
	{
		final int[] ints = new int[8];
		RandUtils.fillArr(ints);
		KeyGOST aes = new KeyGOST(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		RandUtils.fillArr(ints);
		final byte[] bytes = new byte[64];
		final byte[] nib = Bits.bytesToNibbles(bytes);
		aes = new KeyGOST(ints, nib);
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		i += IKey.testKey(aes);
		return i;
	}

}

package claire.util.crypto.mac.key;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.io.IOUtils;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class KeyHMAC 
	   implements IKey<KeyHMAC> {
	
	private byte[] key;
	
	public KeyHMAC(byte[] key)
	{
		this.key = key;
	}
	
	public byte[] getBytes()
	{
		return key;
	}

	public KeyHMAC createDeepClone()
	{
		return new KeyHMAC(ArrayUtil.copy(key));
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeByteArr(key);
	}

	public void export(byte[] bytes, int offset)
	{
		IOUtils.writeArr(key, bytes, offset);
	}

	public int exportSize()
	{
		return 4 + key.length;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.KEYHMAC;
	}

	public boolean sameAs(KeyHMAC obj)
	{
		return ArrayUtil.equals(obj.key, this.key);
	}

	public void erase()
	{
		Arrays.fill(key, (byte) 0);
		key = null;
	}
	
	public static final KeyHMACFactory factory = new KeyHMACFactory();

	public KeyFactory<KeyHMAC> factory()
	{
		return factory;
	}
	
	public static final class KeyHMACFactory
						extends KeyFactory<KeyHMAC>
	{

		public KeyHMACFactory()
		{
			super(KeyHMAC.class);
		}

		public KeyHMAC random(IRandom<?, ?> rand, CryptoString s) throws InstantiationException
		{
			if(s.args() < 2) 
				throw new java.lang.InstantiationException("A length and a hash identifier are required to create HMAC key");
			else if(s.args() > 3) 
				throw new java.lang.InstantiationException("Too many arguments; only a length in bits and a hash identifier are required");
			byte[] bytes = new byte[((s.nextArg().toInt() - 1) / 8) + 1];
			return new KeyHMAC(bytes);
		}

		public int bytesRequired(CryptoString s) throws InstantiationException
		{
			if(s.args() < 2) 
				throw new java.lang.InstantiationException("A length and a hash identifier are required to create HMAC key");
			else if(s.args() > 3) 
				throw new java.lang.InstantiationException("Too many arguments; only a length in bits and a hash identifier are required");
			return ((s.nextArg().toInt() - 1) / 8) + 1;
		}

		public KeyHMAC resurrect(byte[] data, int start) throws InstantiationException
		{
			return new KeyHMAC(IOUtils.readByteArr(data, start));
		}

		public KeyHMAC resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new KeyHMAC(stream.readByteArr());
		}
		
	}

}

package claire.util.crypto.publickey.rsa;

import java.io.IOException;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.math.MathHelper;
import claire.util.math.UInt;
import claire.util.memory.Bits;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IRandom;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class RSAStandardPrivateKey 
	   extends RSAPrivateKey<RSAStandardPrivateKey> {
	
	private final UInt mod;
	private final UInt exp;
	
	private int len;
	
	public RSAStandardPrivateKey(UInt mod, UInt exp, int len)
	{
		this.mod = mod;
		this.exp = exp;
		this.len = len;
	}

	protected void crypt(UInt msg)
	{
		MathHelper.p_modular_exponent(msg, exp, mod);
	}
	
	protected int getLen()
	{
		return this.len;
	}
	
	protected int modLen()
	{
		return this.mod.getIntLen();
	}
	
	public RSAStandardPrivateKey createDeepClone()
	{
		return new RSAStandardPrivateKey(mod.createDeepClone(), exp.createDeepClone(), len);
	}
	
	public int messageSize()
	{
		return this.len << 2;
	}

	public int signatureSize()
	{
		return mod.getIntLen() << 2;
	}

	public int plaintextSize()
	{
		return this.len << 2;
	}

	public int ciphertextSize()
	{
		return mod.getIntLen() << 2;
	}

	public void erase()
	{
		mod.zero();
		exp.zero();
		len = 0;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.RSAPRI;
	}

	public boolean sameAs(RSAStandardPrivateKey obj)
	{
		return (obj.len == this.len) && (mod.equals(obj.mod) && exp.equals(obj.exp));
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInt(len);
		stream.persist(mod);
		stream.persist(exp);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.intToBytes(len, bytes, offset);
		mod.export(bytes, offset += 4);
		exp.export(bytes, offset + mod.exportSize());
	}

	public int exportSize()
	{
		return 4 + mod.exportSize() + exp.exportSize();
	}

	public KeyFactory<RSAStandardPrivateKey> factory()
	{
		return factory;
	}
	
	public static final RSAPrivateKeyFactory factory = new RSAPrivateKeyFactory();

	private static final class RSAPrivateKeyFactory 
						 extends KeyFactory<RSAStandardPrivateKey> {

		private RSAPrivateKeyFactory() 
		{
			super(RSAStandardPrivateKey.class);
		}

		public RSAStandardPrivateKey resurrect(byte[] data, int start) throws InstantiationException
		{
			int len = Bits.intFromBytes(data, start);
			UInt mod = UInt.factory.resurrect(data, start += 4);
			return new RSAStandardPrivateKey(mod, UInt.factory.resurrect(data, start + mod.exportSize()), len);
		}

		public RSAStandardPrivateKey resurrect(IIncomingStream stream)throws InstantiationException, IOException
		{
			int len = stream.readInt();
			return new RSAStandardPrivateKey(UInt.factory.resurrect(stream), UInt.factory.resurrect(stream), len);
		}

		public RSAStandardPrivateKey random(IRandom<?, ?> rand, CryptoString s)
		{
			throw new java.lang.UnsupportedOperationException("RSA Keys must be generated in pairs");
		}
		
	}

}
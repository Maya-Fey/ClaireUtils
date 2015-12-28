package claire.util.crypto.publickey.rsa;

import java.io.IOException;

import claire.util.io.Factory;
import claire.util.math.MathHelper;
import claire.util.math.UInt;
import claire.util.memory.Bits;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class RSAStandardPublicKey 
	   extends RSAPublicKey<RSAStandardPublicKey> {
	
	private final UInt mod;
	private final UInt exp;
	
	private int len;
	
	public RSAStandardPublicKey(UInt mod, UInt exp, int len)
	{
		this.mod = mod;
		this.exp = exp;
		this.len = len;
	}
	
	protected void crypt(UInt msg)
	{
		MathHelper.p_modular_exponent_sure(msg, exp, mod);
	}
	
	protected int getLen()
	{
		return this.len;
	}
	
	protected int modLen()
	{
		return this.mod.getIntLen();
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
		return _NAMESPACE.RSAPUB;
	}

	public boolean sameAs(RSAStandardPublicKey obj)
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
		exp.export(bytes, (int) (offset + mod.exportSize()));
	}

	public int exportSize()
	{
		return 4 + mod.exportSize() + exp.exportSize();
	}

	public Factory<RSAStandardPublicKey> factory()
	{
		return factory;
	}
	
	public static final RSAPublicKeyFactory factory = new RSAPublicKeyFactory();

	private static final class RSAPublicKeyFactory 
						 extends Factory<RSAStandardPublicKey> {

		private RSAPublicKeyFactory() 
		{
			super(RSAStandardPublicKey.class);
		}

		public RSAStandardPublicKey resurrect(byte[] data, int start) throws InstantiationException
		{
			int len = Bits.intFromBytes(data, start);
			UInt mod = UInt.factory.resurrect(data, start += 4);
			return new RSAStandardPublicKey(mod, UInt.factory.resurrect(data, (int) (start + mod.exportSize())), len);
		}

		public RSAStandardPublicKey resurrect(IIncomingStream stream)throws InstantiationException, IOException
		{
			int len = stream.readInt();
			return new RSAStandardPublicKey(UInt.factory.resurrect(stream), UInt.factory.resurrect(stream), len);
		}
		
		
	}

}
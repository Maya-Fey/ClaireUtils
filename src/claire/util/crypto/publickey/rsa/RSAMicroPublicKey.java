package claire.util.crypto.publickey.rsa;

import java.io.IOException;

import claire.util.io.Factory;
import claire.util.math.MathHelper;
import claire.util.math.UInt;
import claire.util.memory.Bits;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class RSAMicroPublicKey 
	   extends RSAPublicKey<RSAMicroPublicKey> {
	
	private final UInt mod;

	private int exp;
	private int len;
	
	public RSAMicroPublicKey(UInt mod, int exp, int len)
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
		exp = 0;
		len = 0;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.RSAMICROPUB;
	}

	public boolean sameAs(RSAMicroPublicKey obj)
	{
		return (obj.len == this.len) && (mod.equals(obj.mod) && (exp == obj.exp));
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInt(len);
		stream.persist(mod);
		stream.writeInt(exp);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.intToBytes(len, bytes, offset);
		mod.export(bytes, offset += 4);
		Bits.intToBytes(exp, bytes, (int) (offset + mod.exportSize()));
	}

	public int exportSize()
	{
		return 8 + mod.exportSize();
	}

	public Factory<RSAMicroPublicKey> factory()
	{
		return factory;
	}
	
	public static final RSAPublicKeyFactory factory = new RSAPublicKeyFactory();

	private static final class RSAPublicKeyFactory 
						 extends Factory<RSAMicroPublicKey> {

		private RSAPublicKeyFactory() 
		{
			super(RSAMicroPublicKey.class);
		}

		public RSAMicroPublicKey resurrect(byte[] data, int start) throws InstantiationException
		{
			int len = Bits.intFromBytes(data, start);
			UInt mod = UInt.factory.resurrect(data, start += 4);
			return new RSAMicroPublicKey(mod, Bits.intFromBytes(data, (int) (start + mod.exportSize())), len);
		}

		public RSAMicroPublicKey resurrect(IIncomingStream stream)throws InstantiationException, IOException
		{
			int len = stream.readInt();
			return new RSAMicroPublicKey(UInt.factory.resurrect(stream), stream.readInt(), len);
		}
		
		
	}

}
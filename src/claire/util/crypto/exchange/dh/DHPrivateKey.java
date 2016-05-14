package claire.util.crypto.exchange.dh;

import java.io.IOException;

import claire.util.crypto.CryptoString;
import claire.util.crypto.KeyFactory;
import claire.util.math.MathHelper;
import claire.util.math.UInt;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
import claire.util.standards.crypto.IRandom;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class DHPrivateKey 
	   implements IKey<DHPrivateKey> {

	private DHParams dh;
	private UInt exp;
	
	public DHPrivateKey(DHParams dh, UInt u)
	{
		this.dh = dh;
		this.exp = u;
	}
	
	public DHParams getParams()
	{
		return dh;
	}
	
	public UInt getExp()
	{
		return exp;
	}
	
	public DHPrivateKey createDeepClone()
	{
		return new DHPrivateKey(dh, exp.createDeepClone());
	}
	
	public void export(IOutgoingStream stream) throws IOException
	{
		stream.persist(exp);
	}

	public void export(byte[] bytes, int offset)
	{
		exp.export(bytes, offset);
	}

	public int exportSize()
	{
		return exp.exportSize();
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.DHPRIVATEKEY;
	}

	public boolean sameAs(DHPrivateKey obj)
	{
		return exp.equals(obj.exp) && obj.dh.equals(dh); 
	}

	public void erase()
	{
		exp.zero();
		dh = null;
		exp = null;
	}

	public KeyFactory<DHPrivateKey> factory()
	{
		return new DHPrivateKeyFactory(dh);
	}
	
	public static final class DHPrivateKeyFactory extends KeyFactory<DHPrivateKey>
	{
		private final DHParams params;
		
		public DHPrivateKeyFactory(DHParams params)
		{
			super(DHPrivateKey.class);
			this.params = params;
		}

		public DHPrivateKey random(IRandom<?, ?> rand, CryptoString s)
		{
			return new DHPrivateKey(params, MathHelper.randomIntegerGood(params.getModulus(), rand));
		}

		public DHPrivateKey resurrect(byte[] data, int start) throws InstantiationException
		{
			return new DHPrivateKey(params, UInt.factory.resurrect(data, start));
		}

		public DHPrivateKey resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new DHPrivateKey(params, stream.resurrect(UInt.factory));
		}
	}

}

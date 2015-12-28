package claire.util.crypto.publickey.rsa;

import java.io.IOException;

import claire.util.io.Factory;
import claire.util.standards.IPersistable;
import claire.util.standards.IReferrable;
import claire.util.standards.IUUID;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public  abstract class StdRSAKeyPair<Type extends StdRSAKeyPair<Type, Public, Private>,
									 Public extends RSAPublicKey<Public>, 
									 Private extends RSAPrivateKey<Private>> 
	   				   implements IUUID<Type>,
	   				  			  IPersistable<Type>,
	   				  			  IReferrable<Type> {
	
	private Public pubkey;
	private Private privk;
	
	public StdRSAKeyPair(Public pub, Private priv)
	{
		this.pubkey = pub;
		this.privk = priv;
	}
	
	public final void setPublicKey(Public key)
	{
		this.pubkey = key;	
	}
	
	public final void setPrivateKey(Private key)
	{
		this.privk = key;
	}

	public final Public getPublicKey()
	{
		return this.pubkey;
	}

	public final Private getPrivateKey()
	{
		return this.privk;
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.RSAPAIR;
	}

	public final boolean sameAs(Type obj)
	{
		return this.pubkey.equals(obj.getPublicKey()) && this.privk.equals(obj.getPrivateKey());
	}

	public final void erase()
	{
		this.pubkey.erase();
		this.privk.erase();
	}

	public final void export(IOutgoingStream stream) throws IOException
	{
		stream.persist(pubkey);
		stream.persist(privk);
	}

	public final void export(byte[] bytes, int offset)
	{
		pubkey.export(bytes, offset);
		privk.export(bytes, offset + pubkey.exportSize());
	}

	public final int exportSize()
	{
		return pubkey.exportSize() + privk.exportSize();
	}	
	
	protected static final class RSAKeyPairFactory<Type extends StdRSAKeyPair<Type, Public, Private>, 
												   Public extends RSAPublicKey<Public>, 
												   Private extends RSAPrivateKey<Private>> 
						   extends Factory<Type>
	{

		private final Factory<Public> pubfac;
		private final Factory<Private> privf;
		private final PairConstructor<Type, Public, Private> con;
		
		protected RSAKeyPairFactory(Class<Type> class_, PairConstructor<Type, Public, Private> con, Factory<Public> p, Factory<Private> f) {
			super(class_);
			this.pubfac = p;
			this.privf = f;
			this.con = con;
		}

		public Type resurrect(byte[] data, int start) throws InstantiationException
		{
			Public pub = pubfac.resurrect(data, start);
			return con.construct(pub, privf.resurrect(data, start + pub.exportSize()));
		}

		public Type resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return con.construct(stream.resurrect(pubfac), stream.resurrect(privf));
		}
		
	}

	protected static interface PairConstructor<Type, Public, Private>
	{
		Type construct(Public pub, Private pri);
	}
}

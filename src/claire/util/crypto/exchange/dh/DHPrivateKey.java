package claire.util.crypto.exchange.dh;

import java.io.IOException;

import claire.util.crypto.KeyFactory;
import claire.util.math.UInt;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IKey;
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

	@Override
	public KeyFactory<DHPrivateKey> factory()
	{
		// TODO Auto-generated method stub
		return null;
	}

}

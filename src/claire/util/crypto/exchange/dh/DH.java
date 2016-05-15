package claire.util.crypto.exchange.dh;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.exchange.dh.DHPrivateKey.DHPrivateKeyFactory;
import claire.util.math.MathHelper;
import claire.util.math.UInt;
import claire.util.standards.crypto.IKeyExchange;

public class DH 
	   implements IKeyExchange<DHPrivateKey, UInt> {
	
	private DHParams params;
	private DHPrivateKey key;
	
	public DH(DHParams params)
	{
		this.params = params;
	}

	public void setPrivate(DHPrivateKey key)
	{
		this.key = key;
	}

	public DHPrivateKey getPrivate()
	{
		return key;
	}

	public UInt genPublic()
	{
		return (UInt) MathHelper.modular_exponent_sure(params.getGenerator(), key.getExp(), params.getModulus());
	}

	public void output(UInt other, byte[] bytes, int start)
	{
		// TODO Auto-generated method stub
		
	}

	public int outputLen()
	{
		return params.getModulus().getIntLen() * 4;
	}

	public KeyFactory<DHPrivateKey> keyFactory()
	{
		return new DHPrivateKeyFactory(params);
	}

	public void erase()
	{
		params = null;
		key = null;
	}

}

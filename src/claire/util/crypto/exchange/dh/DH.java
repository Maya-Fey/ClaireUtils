package claire.util.crypto.exchange.dh;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.exchange.dh.DHPrivateKey.DHPrivateKeyFactory;
import claire.util.math.MathHelper;
import claire.util.math.UInt;
import claire.util.memory.Bits;
import claire.util.standards.crypto.IKeyExchange;

public class DH 
	   implements IKeyExchange<DHPrivateKey, UInt> {
	
	private DHParams params;
	private DHPrivateKey key;
	
	private UInt t;
	
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
		if(t == null)
			t = other.createDeepClone();
		else 
			t.setTo(other);
		MathHelper.p_modular_exponent_sure(t, key.getExp(), params.getModulus());
		Bits.intsToBytes(t.getArr(), 0, bytes, start, other.getIntLen());
	}
	
	public void output(UInt other, byte[] bytes, int start, boolean consume)
	{
		if(consume) {
			MathHelper.p_modular_exponent_sure(other, key.getExp(), params.getModulus());
			Bits.intsToBytes(other.getArr(), 0, bytes, start, other.getIntLen());
		} else
			output(other, bytes, start);
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

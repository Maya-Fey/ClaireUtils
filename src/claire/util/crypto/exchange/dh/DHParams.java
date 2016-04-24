package claire.util.crypto.exchange.dh;

import claire.util.math.MathHelper;
import claire.util.math.UInt;

public class DHParams {

	private final UInt modulus;
	private final UInt generator;
	
	public DHParams(UInt generator, UInt modulus) 
	{
		this.generator = generator;
		this.modulus = modulus;
	}
	
	public UInt fromIndex(UInt u)
	{
		return (UInt) MathHelper.modular_exponent_sure(generator, u, modulus);
	}
	
	public void raiseTo(UInt u, UInt u2)
	{
		MathHelper.p_modular_exponent_sure(u, u2, modulus);
	}

}

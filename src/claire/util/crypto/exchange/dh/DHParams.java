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
		UInt r = generator.createDeepClone();
		MathHelper.p_modular_exponent_sure(r, u, modulus);
		return r;
	}
	
	public void raiseTo(UInt u, UInt u2)
	{
		MathHelper.p_modular_exponent_sure(u, u2, modulus);
	}

}

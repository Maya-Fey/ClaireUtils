package claire.util.crypto.exchange.dh;

import claire.util.math.UInt;

public class DHParams {

	private final UInt modulus;
	private final UInt generator;
	
	public DHParams(UInt generator, UInt modulus) 
	{
		this.generator = generator;
		this.modulus = modulus;
	}
	
	public UInt getModulus()
	{
		return modulus;
	}
	
	public UInt getGenerator()
	{
		return generator;
	}

}

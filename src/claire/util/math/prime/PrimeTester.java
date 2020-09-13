package claire.util.math.prime;

import java.math.BigInteger;

import claire.util.math.MathHelper;
import claire.util.math.exp.Exponentiator;
import claire.util.math.exp.IExponentiator;
import claire.util.math.primitive.ImmutableUInt;
import claire.util.math.primitive.UInt;
import claire.util.standards.crypto.IRandom;

public class PrimeTester {
	
	private final IRandom<?, ?> rng;
	
	private UInt test;
	private UInt exponent;
	private UInt witness;
	private IExponentiator<UInt> exp;
	
	public PrimeTester(IRandom<?, ?> rng, UInt sample)
	{
		this.rng = rng;
		test = sample.createDeepClone();
		exponent = sample.createDeepClone();
		witness = sample.createDeepClone();
		exp = new Exponentiator<UInt>(sample.createDeepClone());
	}
	
	public PrimeTester(IRandom<?, ?> rng, int len)
	{
		this.rng = rng;
		test = new UInt(len);
		exponent = new UInt(len);
		witness = new UInt(len);
		exp = new Exponentiator<UInt>(new UInt(len));
	}
	
	public PrimeTester(IExponentiator<UInt> exp, IRandom<?, ?> rng, UInt sample)
	{
		this.rng = rng;
		test = sample.createDeepClone();
		exponent = sample.createDeepClone();
		witness = sample.createDeepClone();
		this.exp = exp;
	}
	
	public PrimeTester(IExponentiator<UInt> exp, IRandom<?, ?> rng, int len)
	{
		this.rng = rng;
		test = new UInt(len);
		exponent = new UInt(len);
		witness = new UInt(len);
		this.exp = exp;
	}
	
	public void redefine(UInt sample)
	{
		test = sample.createDeepClone();
		exponent = sample.createDeepClone();
		witness = sample.createDeepClone();
		exp = new Exponentiator<UInt>(sample.createDeepClone());
	}
	
	/**
	 * This method finds whether a number is prime or not with a 
	 * probabilistic success rate. 
	 * <br><br>
	 * Note that <code>rng</code> is not cryptographically important, just needs
	 * to have a decent distribution.
	 * <br><br>
	 * <code>times</code> decides how many iterations the test runs. The more 
	 * iterations you run the greater probability the result is correct.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IUInteger greater then two</li>
	 * <li>A RNG</li>
	 * <li>An UInteger greater then zero</li>
	 * </ul>
	 * A prospective integer less then three will result in undefined behavior.
	 * <br><br>
	 * Returns: true if probably prime, false if composite
	 */
	public boolean isPrimeProbableMR(final UInt prospective1, int times)
	{
		ImmutableUInt prospective = new ImmutableUInt(prospective1, false);
		for(int i = 5; i < MathHelper.primes.length; i++) {
			test.setTo(prospective);
			test.p_modulo(MathHelper.primes[i]);
			if(!test.isNonZero()) 
				return false;
		}
		test.setTo(prospective);
		test.decrement();
		exponent.setTo(test);
		int checks = MathHelper.getLSB(exponent.getArr());
		MathHelper.p_leftShift(exponent, checks);
		while(times-- > 0)
		{
			witness.setTo(MathHelper.primes[rng.nextIntFast(MathHelper.primes.length)]);
			
			if(witness.isGreaterOrEqualTo(test)) {
				witness.p_modulo(test);
				if(witness.getArr()[0] < 2)
					witness.getArr()[0] += 2;
			}
			
			exp.p_modular_exponent(witness, exponent, prospective);
			if(witness.isEqualTo(1) || witness.isEqualTo(test))
				continue;
			boolean com = true;
			
			for(int i = 1; i < checks; i++) {
				witness.p_square();
				witness.p_modulo(prospective);
				if(witness.isEqualTo(1)) {
					System.out.println("dawdwa");
					if(new BigInteger(prospective.toCString().toString()).isProbablePrime(8))
						System.out.println("BigUInt disagrees with UUInt");
					return false;
				}
				if(witness.isEqualTo(test)) {
					com = false;
					break;
				}
			}
			if(com) {
				//if(new BigUInteger(prospective.toCString().toString()).isProbablePrime(8))
					//System.out.println("BigUInt disagrees with UUInt");
				return false;
			}
		}
		return true;
	}

}

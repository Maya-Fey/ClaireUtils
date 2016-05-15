package claire.util.math;

import claire.util.crypto.rng.RandUtils;
import claire.util.standards.IInteger;
import claire.util.standards.crypto.IRandom;

public class PrimeTester<Int extends IInteger<Int>> {
	
	private final IRandom<?, ?> rng;
	private final Int test;
	private final Int exponent;
	private final Int witness;
	private final Exponentiator<Int> exp;
	
	public PrimeTester(IRandom<?, ?> rng, Int sample)
	{
		this.rng = rng;
		test = sample.createDeepClone();
		exponent = sample.createDeepClone();
		witness = sample.createDeepClone();
		exp = new Exponentiator<Int>(sample.createDeepClone());
	}
	
	public PrimeTester(IRandom<?, ?> rng, IntegerFactory<Int> factory, int len)
	{
		this.rng = rng;
		test = factory.construct(len);
		exponent = factory.construct(len);
		witness = factory.construct(len);
		exp = new Exponentiator<Int>(factory.construct(len));
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
	 * <li>An IInteger greater then two</li>
	 * <li>A RNG</li>
	 * <li>An Integer greater then zero</li>
	 * </ul>
	 * A prospective integer less then three will result in undefined behavior.
	 * <br><br>
	 * Returns: true if probably prime, false if composite
	 */
	public boolean isPrimeProbableMR(final Int prospective, int times)
	{
		test.setTo(prospective);
		test.decrement();
		exponent.setTo(test);
		int checks = MathHelper.getLSB(exponent.getArr());
		MathHelper.p_leftShift(exponent, checks);
		while(times-- > 0)
		{
			RandUtils.fillArr(witness.getArr(), rng);
			witness.p_modulo(test);
			/* 
			 * Note: This check will cause a very rare problem of the witness
			 * being incremented beyond the modulus. 
			 */
			if(witness.getArr()[0] < 2 && witness.getArr()[0] >= 0) 
				witness.getArr()[0] += 2;
			exp.p_modular_exponent(witness, exponent, prospective);
			if(witness.isEqualTo(1) || witness.isEqualTo(test))
				continue;
			boolean com = true;
			
			for(int i = 1; i < checks; i++) {
				witness.p_square();
				witness.p_modulo(prospective);
				if(witness.isEqualTo(1))
					return false;
				if(witness.isEqualTo(test)) {
					com = false;
					break;
				}
			}
			if(com)
				return false;
		}
		return true;
	}

}

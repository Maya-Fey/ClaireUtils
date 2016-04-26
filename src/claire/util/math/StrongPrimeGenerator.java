package claire.util.math;

import claire.util.crypto.rng.RandUtils;
import claire.util.standards.IInteger;
import claire.util.standards.crypto.IRandom;

public class StrongPrimeGenerator<Int extends IInteger<Int>>
	   extends PrimeGenerator<Int> {
	
	public StrongPrimeGenerator(int st, Int min, Int max)
	{
		this(st, min, max, 2, RandUtils.dprng);
	}
	
	public StrongPrimeGenerator(int st, Int min, Int max, int primes)
	{
		this(st, min, max, primes, RandUtils.dprng);
	}
	
	public StrongPrimeGenerator(int st, Int min, Int max, int primes, IRandom<?> rand)
	{
		this(st, min, max, primes, rand, rand);
	}

	public StrongPrimeGenerator(int st, Int min, Int max, int primes, IRandom<?> rand, IRandom<?> rand2) 
	{
		super(st, min, max, primes, rand, rand2);
	}
	
	public Int nextPrime()
	{		
		while(true)
		{
			Int p = super.nextPrime();
			temp.setTo(p);
			temp.decrement();
			temp.p_divide(2);
			if(this.tester.isPrimeProbableMR(temp, this.st)) {
				return p;
			}
			this.nextCanidate();
		}
	}

}

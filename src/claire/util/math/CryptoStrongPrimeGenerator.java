package claire.util.math;

import claire.util.crypto.rng.RandUtils;
import claire.util.standards.IInteger;
import claire.util.standards.crypto.IRandom;

public class CryptoStrongPrimeGenerator<Int extends IInteger<Int>>
	   extends CryptoPrimeGenerator<Int> {
	
	public CryptoStrongPrimeGenerator(int st, int bits, IntegerFactory<Int> fac)
	{
		this(st, bits, fac, 2, RandUtils.dprng);
	}
	
	public CryptoStrongPrimeGenerator(int st, int bits, IntegerFactory<Int> fac, int primes)
	{
		this(st, bits, fac, primes, RandUtils.dprng);
	}
	
	public CryptoStrongPrimeGenerator(int st, int bits, IntegerFactory<Int> fac, int primes, IRandom<?, ?> rand)
	{
		this(st, bits, fac, primes, rand, rand);
	}

	public CryptoStrongPrimeGenerator(int st, int bits, IntegerFactory<Int> fac, int primes, IRandom<?, ?> rand, IRandom<?, ?> rand2) 
	{
		super(st, bits, fac, primes, rand, rand2);
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

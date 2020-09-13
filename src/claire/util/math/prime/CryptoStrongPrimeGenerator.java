package claire.util.math.prime;

import claire.util.concurrency.gen.TaskMonitor;
import claire.util.crypto.rng.RandUtils;
import claire.util.math.primitive.UInt;
import claire.util.standards.crypto.IRandom;

public class CryptoStrongPrimeGenerator
	   extends CryptoPrimeGenerator {
	
	public CryptoStrongPrimeGenerator(int st, int bits)
	{
		this(st, bits, 2, RandUtils.dprng);
	}
	
	public CryptoStrongPrimeGenerator(int st, int bits, int primes)
	{
		this(st, bits, primes, RandUtils.dprng);
	}
	
	public CryptoStrongPrimeGenerator(int st, int bits, int primes, IRandom<?, ?> rand)
	{
		this(st, bits, primes, rand, rand);
	}

	public CryptoStrongPrimeGenerator(int st, int bits, int primes, IRandom<?, ?> rand, IRandom<?, ?> rand2) 
	{
		super(st, bits, primes, rand, rand2);
	}
	
	public UInt nextPrime()
	{		
		while(true)
		{
			UInt p = super.nextPrime();
			temp.setTo(p);
			temp.decrement();
			temp.p_divide(2);
			if(this.tester.isPrimeProbableMR(temp, this.st)) {
				return p;
			}
			this.nextCanidate();
		}
	}
	
	public UInt nextPrimeThread(TaskMonitor mon)
	{		
		while(!mon.isDone())
		{
			UInt p = super.nextPrime();
			temp.setTo(p);
			temp.decrement();
			temp.p_divide(2);
			if(this.tester.isPrimeProbableMR(temp, this.st)) {
				return p;
			}
			this.nextCanidate();
		}
		return null;
	}

}

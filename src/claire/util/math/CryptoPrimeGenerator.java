package claire.util.math;

import claire.util.crypto.rng.RandUtils;
import claire.util.standards.IGenerator;
import claire.util.standards.IInteger;
import claire.util.standards.crypto.IRandom;

public class CryptoPrimeGenerator<Int extends IInteger<Int>> 
	   implements IGenerator<Int> {
	
	public static final int[] PRIMES = new int[]
		{
			  2,   3,   5,   7,  11,  13,  17, 
			 19,  23,  29,  31,  37,  41,  43,  
			 47,  53,  59,  61,  67,  71,  73,  
			 79,  83,  89,  97, 101, 103, 107, 
			109, 113, 127, 131, 137, 139, 149, 
			151, 157, 163, 167, 173, 179, 181, 
			191, 193, 197, 199, 211, 223, 227, 
			229, 233, 239, 241, 251, 257, 263, 
			269, 271, 277, 281, 283, 293, 307, 
			311, 313, 317, 331, 337, 347, 349, 
			353, 359, 367, 373, 379, 383, 389, 
			397, 401, 409, 419, 421, 431, 433, 
			439, 443, 449, 457, 461, 463, 467, 
			479, 487, 491, 499, 503, 509, 521, 
			523, 541
		};
	
	public static final int[] SPACE = new int[]
		{
			 1,  1,  1,  1,  1,  1,  1,  1,  1,
			 1,  2,  2,  2,  2,  2,  2,  3,  3,
			 3,  3,  3,  3,  4,  4,  4,  4,  4,
			 5,  5,  5,  5,  6,  6,  6,  6,  6,
			 7,  7,  7,  7,  8,  8,  8,  8,  9,
			 9,  9,  9,  9, 10, 10, 10, 10, 11,
			11, 11, 11, 12, 12, 12, 12, 13, 13,
			13, 14, 14, 14, 14, 15, 15, 15, 15,
			16, 16, 16, 16, 17, 17, 17, 17, 18,
			18, 18, 19, 19, 19, 19, 20, 20, 20,
			21, 21, 21, 21, 22, 22, 22, 22, 23,
			23, 23
		};
	
	protected final int st,
			    		primes;
	
	protected final IRandom<?, ?> rand;
	protected final PrimeTester<Int> tester;
	
	private final Int add;

	protected Int temp;
	
	private Int canidate;
	private Int temp2;
	private int bits;
	
	public CryptoPrimeGenerator(int st, int bits, IntegerFactory<Int> factory)
	{
		this(st, bits, factory, 2, RandUtils.dprng);
	}
	
	public CryptoPrimeGenerator(int st, int bits, IntegerFactory<Int> factory, int primes)
	{
		this(st, bits, factory, primes, RandUtils.dprng);
	}
	
	public CryptoPrimeGenerator(int st, int bits, IntegerFactory<Int> factory, int primes, IRandom<?, ?> rand)
	{
		this(st, bits, factory, primes, rand, rand);
	}
	
	public CryptoPrimeGenerator(int st, int bits, IntegerFactory<Int> factory, int primes, IRandom<?, ?> rand, IRandom<?, ?> rand2)
	{
		int ints = bits * 32 + (((bits & 31) > 0) ? 1 : 0);
		this.bits = bits;
		this.st = st;
		this.rand = rand;
		this.primes = primes;
		this.temp2 = factory.construct(ints);
		this.tester = new PrimeTester<Int>(rand2, temp2);
		Int add = factory.construct(ints);
		if(add.getIntLen() < SPACE[primes])
			throw new java.lang.IllegalArgumentException("The number of primes is too large for a " + bits + "-bit number");
		temp = add.createDeepClone();
		add.setTo(1);
		for(int i = 0; i < primes; i++)
			add.p_multiply(PRIMES[i]);
		this.add = add;
	}
	
	public void nextCanidate()
	{
		if(canidate == null)
			nextCanidate(temp2.createDeepClone());
		else
			nextCanidate(canidate);
	}
	
	public void nextCanidate(Int canidate)
	{
		MathHelper.strictRandomInteger(canidate, rand, bits);
		if(!canidate.isOdd())
			canidate.increment();
		int t = 0;
		temp.setTo(1);
		while(true)
		{
			boolean done = t >= primes;
			for(int i = t; i < primes; i++) {
				if(canidate.modulo(PRIMES[i], temp2).isNonZero()) {
					t++;
					temp.p_multiply(PRIMES[i]);
				} else 
					break;
			}
			if(done)
				break;
			else {
				canidate.p_add(temp);
				continue;
			}
		}
		this.canidate = canidate;
	}
	
	public Int nextPrime()
	{		
		if(canidate == null)
			this.nextCanidate();
		while(true) {
			canidate.p_add(add);
			while(!tester.isPrimeProbableMR(canidate, st)) {
				canidate.p_add(add);	
				if(canidate.getBits() > bits)
					this.nextCanidate(canidate);
			}
			return canidate.createDeepClone();
		}
	}
	
	public void setBits(int bits)
	{
		this.bits = bits;
	}
	
	public int getBits()
	{
		return this.bits;
	}

	public Int generate()
	{
		return this.nextPrime();
	}

}

package claire.util.crypto.rng;

import claire.util.encoding.CString;
import claire.util.math.MathHelper;
import claire.util.math.UInt1024;
import claire.util.memory.Bits;
import claire.util.standards.IRandom;

public final class RNGTest {
	
	public static final long ENSURE_POSITIVE(long l)
	{
		if(l < 0) return Long.MAX_VALUE;
		return l;
	}

	public static long runAverageTest(IRandom rng, long times)
	{
		for(int i = 0; i < 5; i++)
			rng.nextInt();
		final int first = rng.nextInt();
		int second = rng.nextInt();
		int count = 1;
		long total = 0;
		long mod = 200;
		for(long i = 0; i < times; i++)
		{
			int inte = rng.nextInt();	
			total += inte;
			if(inte == first)
				count++;
			if(inte == second) 
				count++;
			if(mod == i) {
				mod += 200;
				second = inte;
			}
		}
		System.out.println(count);
		if(count > 1)
			if(count > 50)
				return Long.MAX_VALUE;
			else 
				return ENSURE_POSITIVE(MathHelper.distancefrom(total, 0) * count);
		return MathHelper.distancefrom(total, 0);
	}
	
	/**
	 * Collects the bits that are different from RNG output. Compares against an
	 * average of 16 bits (50% chance of a change)
	 * 
	 * Lower scores are higher
	 */
	public static long runDifferenceTest(IRandom rng, long times)
	{
		long total = 0;
		long ideal = 16 * times;
		int prev = rng.nextInt();
		int next;
		for(int i = 0; i < times; i++)
		{
			next = rng.nextInt();
			total += Bits.countDifferent(prev, next);
			prev = next;
		}
		return MathHelper.distancefrom(total, ideal);
	}
	
	public static int[] runAdvancedDifferenceTest(IRandom rng, int times)
	{
		int[] out = new int[33];
		int prev = rng.nextInt();
		int next;
		for(int i = 0; i < times; i++)
		{
			next = rng.nextInt();
			out[Bits.countDifferent(prev, next)]++;
			prev = next;
		}
		return out;
	}

	public static int runRunsTest(IRandom rng, long times)
	{
		int prev = rng.nextInt();
		long gt = 0, 
			 lt = 0,
			 runs = 1;
		boolean gtp;
		int j = rng.nextInt();
		if(j > prev) {
			gtp = true;
			gt++;
		} else {
			lt++;
			gtp = false;
		}
		prev = j;
		for(int i = 1; i < times; i++) {
			j = rng.nextInt();
			if(j > prev) {
				gt++;
				if(!gtp) {
					runs++;
					gtp ^= true;
				}
			} else if(j < prev) {
				lt++;
				if(gtp) {
					runs++;
					gtp ^= true;
				}
			}
			prev = j;
		}
		long expected = ((2 * gt * lt) / times) + 1;
		System.out.println(2 * gt * lt);
		System.out.println(((2 * gt * lt) - times));
		long deviation = (2 * gt * lt * ((2 * gt * lt) - times)) / (times * times * (times - 1));
		System.out.println(deviation);
		UInt1024 t1 = new UInt1024(String.valueOf(2 * gt * lt));
		t1.p_multiply(((2 * gt * lt) - times));
		t1.p_divide(times);
		t1.p_divide(times);
		t1.p_divide(times - 1);
		System.out.println(t1.toCString());
		UInt1024 t2 = new UInt1024(new CString((expected - runs) * 100));
		t2.p_divide(t1);
		return t2.toInt32();
	}
	
}

package claire.util.crypto.publickey.rsa;

import claire.util.encoding.CString;
import claire.util.math.MathHelper;
import claire.util.math.PrimeGenerator;
import claire.util.math.StrongPrimeGenerator;
import claire.util.math.UInt;
import claire.util.standards.crypto.IRandom;

public class RSAKeyGenerator {
	
	private static final int scan_tests = 4;

	public static RSALargeKeyPair generateBig(final int size, int amt, IRandom<?> rng, UInt exp, int primes, boolean strong)
	{
		int per = size / amt;
		if(size == 0)
			throw new java.lang.IllegalArgumentException("The size per prime is less then one 32-bit integer.");
		UInt min = new UInt("0", per + 1);
		UInt max = new UInt("0", per * 2 + 1);
		UInt mod = new UInt("1", size + (amt / 24) + 1);
		UInt phi = new UInt("1", size + (amt / 24) + 1);
		MathHelper.getMinIntValue(min, per);
		MathHelper.getMinBitValue(max, (per << 5) + 1);
		PrimeGenerator<UInt> pg;
		if(strong)
			pg = new StrongPrimeGenerator<UInt>(scan_tests, min, max, primes, rng);
		else
			pg = new PrimeGenerator<UInt>(scan_tests, min, max, primes, rng);
		UInt t = null;
		pg.setMinMax(min, max);
		boolean extra = (size % amt) != 0;
		int to = extra ? amt - 1 : amt;
		for(int i = 0; i < to; i++) {
			pg.nextCanidate();
			t = pg.nextPrime();
			t.decrement();
			if(MathHelper.gcd(t, exp).doesNotEqual(1)) {
				i--;
				continue;
			}
			phi.p_multiply(t);
			t.increment();
			mod.p_multiply(t);
		}
		
		if(extra) {
			per = size % amt;
			min.zero();
			max.zero();
			MathHelper.getMinIntValue(min, per);
			MathHelper.getMinBitValue(max, (per << 5) + 2);
			pg.nextCanidate();
			t = pg.nextPrime();
			mod.p_multiply(t);
			t.decrement();
			phi.p_multiply(t);	
		}
		
		while(MathHelper.gcd(exp, phi).doesNotEqual(1)) {
			phi.p_divide(t);
			t.increment();
			mod.p_divide(t);
			pg.nextCanidate();
			t = pg.nextPrime();
			mod.p_multiply(t);
			t.decrement();
			phi.p_multiply(t);
		}
		if(exp.getIntLen() < phi.getIntLen())
			exp = exp.getLarge(phi.getIntLen());
		UInt inv = MathHelper.u_modular_inverse(exp, phi);
		UInt message = new UInt("2301738971289", mod.getIntLen() * 2);
		UInt test = message.createDeepClone();
		MathHelper.p_modular_exponent(test, exp, mod);
		MathHelper.p_modular_exponent(test, inv, mod);
		if(message.doesNotEqual(test))
			return generateBig(size, amt, rng, exp, primes, strong);
		RSAStandardPrivateKey priv = new RSAStandardPrivateKey(mod, inv, size);
		RSAStandardPublicKey pub = new RSAStandardPublicKey(mod.createDeepClone(), exp, size);
		return new RSALargeKeyPair(pub, priv);
	}
	
	public static RSAFastLargeKeyPair generateBigFast(final int size, int amt, IRandom<?> rng, UInt exp, int primes, boolean strong)
	{
		int per = size / amt;
		if(size == 0)
			throw new java.lang.IllegalArgumentException("The size per prime is less then one 32-bit integer.");
		UInt min = new UInt("0", per + 1);
		UInt max = new UInt("0", per * 2 + 1);
		UInt mod = new UInt("1", size + (amt / 24) + 1);
		UInt phi = new UInt("1", size + (amt / 24) + 1);
		MathHelper.getMinIntValue(min, per);
		MathHelper.getMinBitValue(max, (per << 5) + 1);
		PrimeGenerator<UInt> pg;
		if(strong)
			pg = new StrongPrimeGenerator<UInt>(scan_tests, min, max, primes, rng);
		else
			pg = new PrimeGenerator<UInt>(scan_tests, min, max, primes, rng);
		UInt t = null;
		pg.setMinMax(min, max);
		boolean extra = (size % amt) != 0;
		int to = extra ? amt - 1 : amt;
		UInt[] parr = new UInt[amt];
		for(int i = 0; i < to; i++) {
			pg.nextCanidate();
			t = pg.nextPrime();
			t.decrement();
			if(MathHelper.gcd(t, exp).doesNotEqual(1)) {
				i--;
				continue;
			}
			phi.p_multiply(t);
			t.increment();
			mod.p_multiply(t);
			parr[i] = t;
		}
		
		if(extra) {
			per = size % amt;
			min.zero();
			max.zero();
			MathHelper.getMinIntValue(min, per);
			MathHelper.getMinBitValue(max, (per << 5) + 2);
			pg.nextCanidate();
			t = pg.nextPrime();
			mod.p_multiply(t);
			t.decrement();
			phi.p_multiply(t);	
			parr[amt - 1] = t;
		}
		
		while(MathHelper.gcd(exp, phi).doesNotEqual(1)) {
			phi.p_divide(t);
			t.increment();
			mod.p_divide(t);
			pg.nextCanidate();
			t = pg.nextPrime();
			mod.p_multiply(t);
			t.decrement();
			phi.p_multiply(t);
			parr[amt - 1] = t;
		}
		if(exp.getIntLen() < phi.getIntLen())
			exp = exp.getLarge(phi.getIntLen());
		UInt inv = MathHelper.u_modular_inverse(exp, phi);	
		UInt message = new UInt("2301738971289", mod.getIntLen() * 2);
		UInt test = message.createDeepClone();
		MathHelper.p_modular_exponent(test, exp, mod);
		MathHelper.p_modular_exponent(test, inv, mod);
		if(message.doesNotEqual(test))
			return generateBigFast(size, amt, rng, exp, primes, strong);
		RSAFastPrivateKey priv = new RSAFastPrivateKey(mod, inv, parr, size);
		RSAStandardPublicKey pub = new RSAStandardPublicKey(mod.createDeepClone(), exp, size);
		return new RSAFastLargeKeyPair(pub, priv);
	}
	
	public static RSAStandardKeyPair generateSmall(final int size, int amt, IRandom<?> rng, int iexp, int primes, boolean strong)
	{
		int per = size / amt;
		if(size == 0)
			throw new java.lang.IllegalArgumentException("The size per prime is less then one 32-bit integer.");
		UInt min = new UInt("0", per + 1);
		UInt max = new UInt("0", per * 2 + 1);
		UInt mod = new UInt("1", size + (amt / 24) + 1);
		UInt phi = new UInt("1", size + (amt / 24) + 1);
		UInt exp = new UInt(new CString(iexp), size + (amt / 24) + 1);
		MathHelper.getMinIntValue(min, per);
		MathHelper.getMinBitValue(max, (per << 5) + 1);
		PrimeGenerator<UInt> pg;
		if(strong)
			pg = new StrongPrimeGenerator<UInt>(scan_tests, min, max, primes, rng);
		else
			pg = new PrimeGenerator<UInt>(scan_tests, min, max, primes, rng);
		UInt t = null;
		pg.setMinMax(min, max);
		boolean extra = (size % amt) != 0;
		int to = extra ? amt - 1 : amt;
		
		for(int i = 0; i < to; i++) {
			pg.nextCanidate();
			t = pg.nextPrime();
			t.decrement();
			if(MathHelper.gcd(t, exp).doesNotEqual(1)) {
				i--;
				continue;
			}
			phi.p_multiply(t);
			t.increment();
			mod.p_multiply(t);
		}
		
		if(extra) {
			per = size % amt;
			min.zero();
			max.zero();
			MathHelper.getMinIntValue(min, per);
			MathHelper.getMinBitValue(max, (per << 5) + 2);
			pg.nextCanidate();
			t = pg.nextPrime();
			mod.p_multiply(t);
			t.decrement();
			phi.p_multiply(t);	
		}
		
		while(MathHelper.gcd(exp, phi).doesNotEqual(1)) {
			phi.p_divide(t);
			t.increment();
			mod.p_divide(t);
			pg.nextCanidate();
			t = pg.nextPrime();
			mod.p_multiply(t);
			t.decrement();
			phi.p_multiply(t);
		}
		if(exp.getIntLen() < phi.getIntLen())
			exp = exp.getLarge(phi.getIntLen());
		UInt inv = MathHelper.u_modular_inverse(exp, phi);
		UInt message = new UInt("2301738971289", mod.getIntLen() * 2);
		UInt test = message.createDeepClone();
		MathHelper.p_modular_exponent(test, exp, mod);
		MathHelper.p_modular_exponent(test, inv, mod);
		if(message.doesNotEqual(test))
			return generateSmall(size, amt, rng, iexp, primes, strong);
		RSAStandardPrivateKey priv = new RSAStandardPrivateKey(mod, inv, size);
		RSAMicroPublicKey pub = new RSAMicroPublicKey(mod.createDeepClone(), iexp, size);
		return new RSAStandardKeyPair(pub, priv);
	}
	
	public static RSAFastStandardKeyPair generateSmallFast(final int size, int amt, IRandom<?> rng, int iexp, int primes, boolean strong)
	{
		int per = size / amt;
		if(size == 0)
			throw new java.lang.IllegalArgumentException("The size per prime is less then one 32-bit integer.");
		UInt min = new UInt("0", per + 1);
		UInt max = new UInt("0", per * 2 + 1);
		UInt mod = new UInt("1", size + (amt / 24) + 1);
		UInt phi = new UInt("1", size + (amt / 24) + 1);
		MathHelper.getMinIntValue(min, per);
		MathHelper.getMinBitValue(max, (per << 5) + 1);
		UInt exp = new UInt(new CString(iexp), size + (amt / 24) + 1);
		PrimeGenerator<UInt> pg;
		if(strong)
			pg = new StrongPrimeGenerator<UInt>(scan_tests, min, max, primes, rng);
		else
			pg = new PrimeGenerator<UInt>(scan_tests, min, max, primes, rng);
		UInt t = null;
		pg.setMinMax(min, max);
		boolean extra = (size % amt) != 0;
		int to = extra ? amt - 1 : amt;
		UInt[] parr = new UInt[amt];
		for(int i = 0; i < to; i++) {
			pg.nextCanidate();
			t = pg.nextPrime();
			t.decrement();
			if(MathHelper.gcd(t, exp).doesNotEqual(1)) {
				i--;
				continue;
			}
			phi.p_multiply(t);
			t.increment();
			mod.p_multiply(t);
			parr[i] = t;
		}
		
		if(extra) {
			per = size % amt;
			min.zero();
			max.zero();
			MathHelper.getMinIntValue(min, per);
			MathHelper.getMinBitValue(max, (per << 5) + 2);
			pg.nextCanidate();
			t = pg.nextPrime();
			mod.p_multiply(t);
			t.decrement();
			phi.p_multiply(t);	
			parr[amt - 1] = t;
		}
		
		while(MathHelper.gcd(exp, phi).doesNotEqual(1)) {
			phi.p_divide(t);
			t.increment();
			mod.p_divide(t);
			pg.nextCanidate();
			t = pg.nextPrime();
			mod.p_multiply(t);
			t.decrement();
			phi.p_multiply(t);
			parr[amt - 1] = t;
		}
		if(exp.getIntLen() < phi.getIntLen())
			exp = exp.getLarge(phi.getIntLen());
		UInt inv = MathHelper.u_modular_inverse(exp, phi);	
		UInt message = new UInt("2301738971289", mod.getIntLen() * 2);
		UInt test = message.createDeepClone();
		MathHelper.p_modular_exponent(test, exp, mod);
		MathHelper.p_modular_exponent(test, inv, mod);
		if(message.doesNotEqual(test))
			return generateSmallFast(size, amt, rng, iexp, primes, strong);
		RSAFastPrivateKey priv = new RSAFastPrivateKey(mod, inv, parr, size);
		RSAMicroPublicKey pub = new RSAMicroPublicKey(mod.createDeepClone(), iexp, size);
		return new RSAFastStandardKeyPair(pub, priv);
	}
	
}

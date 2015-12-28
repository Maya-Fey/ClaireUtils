package claire.util.math;

import java.math.BigInteger;

import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IInteger;
import claire.util.standards.IRandom;

public final class MathHelper {
	
	public static void shiftArrayRight(int[] arr, int places)
	{
		int bplaces = places / 32;
		if(bplaces != 0)
			ArrayUtil.shiftRight(arr, bplaces);
		places &= 31;
		if(places > 0)
		{
			int start = arr.length - bplaces - 1;
			int invplaces = 32 - places;
			while(start > 0)
				arr[start] = (arr[start--] >>> places) | (arr[start] << invplaces);
			arr[start] >>>= places;
		}
	}
	
	public static void shiftArrayLeft(int[] arr, int places)
	{
		int bplaces = places / 32;
		if(bplaces != 0)
			ArrayUtil.shiftLeft(arr, bplaces);
		places &= 31;
		if(places > 0)
		{
			int end = arr.length - bplaces - 1;
			int invplaces = 32 - places;
			int i = 0;
			while(i < end)
				arr[i] = (arr[i++] << places) | (arr[i] >>> invplaces);
			arr[i] <<= places;
		}
	}
	
	/**
	 * Shifts array right. Takes into account Bit Big-Endianness vs
	 * Int little-endianness
	 * 
	 * @param arr
	 * @param places
	 */
	public static void shiftArrayRightBE(int[] arr, int places)
	{
		final int bplaces; 
		if(places > 31) {
			bplaces = places / 32;
			places &= 31;
		} else {
			bplaces = 0;
		}
		
		if(places > 0)
		{
			int start = arr.length - bplaces - 1;
			int invplaces = 32 - places;
			while(start > 0)
				arr[start] = (arr[start--] << places) | (arr[start] >>> invplaces);
			arr[0] <<= places;
		}
		if(bplaces != 0)
			ArrayUtil.shiftRight(arr, bplaces);
	}
	
	/**
	 * Shifts array left. Takes into account Bit Big-Endianness vs
	 * Int little-endianness
	 * 
	 * @param arr
	 * @param places
	 */
	public static void shiftArrayLeftBE(int[] arr, int places)
	{
		final int bplaces; 
		if(places > 31) {
			bplaces = places / 32;
			places &= 31;
		} else {
			bplaces = 0;
		}
		
		if(places > 0)
		{
			int end = arr.length - bplaces - 1;
			int invplaces = 32 - places;
			int i = 0;
			while(i < end)
				arr[i] = (arr[i++] >>> places) | (arr[i] << invplaces);
			arr[i] >>>= places;
		}
		if(bplaces != 0)
			ArrayUtil.shiftLeft(arr, bplaces);
	}
	
	public static int getLSB(int[] arr)
	{
		int total = 0;
		int i = -1;
		while(arr[++i] == 0) 
			total += 32;
		
		return total + Bits.getLSB(arr[i]);
	}
	
	public static int getMSB(int[] arr)
	{
		int total = arr.length << 5;
		int i = arr.length;
		while(arr[--i] == 0)
			total -= 32;
		total -= 32;
		return total + Bits.getMSB(arr[i]);
	}
	
	public static void p_rightShift(IInteger<?> i, int bits)
	{
		shiftArrayRightBE(i.getArr(), bits);
	}
	
	public static void p_leftShift(IInteger<?> i, int bits)
	{
		shiftArrayLeftBE(i.getArr(), bits);
	}
	
	public static int getRealLength(int[] num)
	{
		int i = num.length - 1;
		while(num[i] == 0 && i != 0)
			i--;
		return i + 1;
	}

	public static double absolute(double d)
	{
		if(d < 0) { d *= (double)-1; }
		return d;
	}
	
	public static long absolute(long l)
	{
		if(l < 0) { l *= -1; }
		return l;
	}
	
	public static long exponent(long i, long exponent)
	{
		if(exponent == 0) {
			return 1;
		} else if(exponent == 1) {
			return i;
		} else {
			long f = i;
			for(int i2 = 1; i2 < exponent; i2++)
				f *= i;
			return f;
		}
	}
	
	public static long ceiling(long ... longs)
	{
		long r = longs[0];
		for(int i = 1; i < longs.length; i++)
			if(longs[i] > r) 
				r = longs[i];
		return r;
	}
	
	public static long floor(long ... longs)
	{
		long r = longs[0];
		for(int i = 1; i < longs.length; i++)
			if(longs[i] < r) 
				r = longs[i];
		return r;
	}
	
	public static double ceiling(double ... longs)
	{
		double r = longs[0];
		for(int i = 1; i < longs.length; i++)
			if(longs[i] > r) 
				r = longs[i];
		return r;
	}
	
	public static double floor(double ... longs)
	{
		double r = longs[0];
		for(int i = 1; i < longs.length; i++)
			if(longs[i] < r) 
				r = longs[i];
		return r;
	}
	
	public static long distancefrom(long i1, long i2)
	{
		return ceiling(i1, i2) - floor(i1, i2);
	}
	
	/**
	 * Multiplies Base2^32 Integer by one 32-bit integer.
	 * Returns any carry-over from the calculation.
	 * 
	 * @param r1
	 * @param mul
	 */
	public static int mul1(int[] r1, int tlen, int mul)
	{
		long carry = 0;
		long mull = ((long) mul & 0xFFFFFFFFL);
		int k = 0;
		for(; k < tlen; k++)
		{
			carry += mull * ((long) r1[k] & 0xFFFFFFFFL);
			r1[k] = (int) carry;
			carry >>>= 32;
		}
		while(k < r1.length && carry != 0) {
			r1[k] = (int) carry;
			carry >>>= 32;
		}
		return (int) carry;
	}
	
	public static void p_safe_exponent(IInteger<?> i, int exponent)
	{
		if(exponent == 0)
			i.setTo("1");
		else if(exponent == 1)
			return;
		else {
			IInteger<?> i2 = i.createDeepClone();
			i2.p_square();
			long mask = 0x8000000000000000L;
			while((mask & exponent) == 0)
				mask >>>= 1;
			mask >>>= 1;
			while(mask != 0) {
				if((exponent & mask) == 0) {
					i2.p_multiply(i);
					i.p_square();
				} else {
					i.p_multiply(i2);
					i2.p_square();
				}
				mask >>>= 1;
			}
		}
	}
	
	public static void p_exponent(IInteger<?> i, int exponent)
	{
		if(exponent == 0) {
			i.setTo(1);
			return;
		} 
		if(exponent == 1)
			return;
		IInteger<?> o = i.createDeepClone();
		o.setTo(1);
		while(exponent > 1)
		{
			if((exponent & 1) == 1) 
				o.p_multiply(i);
			i.p_square();
			exponent >>>= 1;
		}
		i.p_multiply(o);
	}
	
	public static void p_exponent(IInteger<?> i, IInteger<?> exponent)
	{
		if(getRealLength(exponent.getArr()) == 1)
			p_exponent(i, exponent.getArr()[0]);
		else
			p_exponent_sure(i, exponent);
	}
	
	public static void p_exponent_sure(IInteger<?> i, IInteger<?> exponent)
	{
		if(!exponent.isNonZero()) {
			i.setTo(1);
			return;
		}
		if(exponent.isEqualTo(1))
			return;
		IInteger<?> o = i.createDeepClone();
		o.setTo(1);
		int bit = 0;
		int max = getMSB(exponent.getArr());
		while(bit < max)
		{
			if(exponent.bitAt(bit++)) 
				o.p_multiply(i);
			i.p_square();			
		}
		i.p_multiply(o);
	}
	
	public static void p_modular_exponent(IInteger<?> i, int exponent, IInteger<?> mod)
	{
		if(exponent == 0) {
			i.setTo(1);
			return;
		} 
		if(i.isGreaterOrEqualTo(mod))
			i.p_modulo(mod);
		if(exponent == 1)
			return;
		IInteger<?> o = i.createDeepClone();
		o.setTo(1);
		while(exponent > 1)
		{
			if((exponent & 1) == 1) {
				o.p_multiply(i);
				o.p_modulo(mod);
			}
			i.p_square();
			i.p_modulo(mod);
			exponent >>>= 1;
		}
		i.p_multiply(o);
		i.p_modulo(mod);
	}
	
	public static void p_modular_exponent(IInteger<?> i, IInteger<?> exponent, IInteger<?> mod)
	{
		if(getRealLength(exponent.getArr()) == 1)
			p_modular_exponent(i, exponent.getArr()[0], mod);
		else
			p_modular_exponent_sure(i, exponent, mod);
	}
	
	public static void p_modular_exponent_sure(IInteger<?> i, IInteger<?> exponent, IInteger<?> mod)
	{
		if(!exponent.isNonZero()) {
			i.setTo(1);
			return;
		}
		if(i.isGreaterOrEqualTo(mod)) {
			i.p_modulo(mod);
		}
		if(exponent.isEqualTo(1))
			return;
		IInteger<?> o = i.createDeepClone();
		o.setTo(1);
		int bit = 0;
		int max = getMSB(exponent.getArr());
		while(bit < max)
		{
			if(exponent.bitAt(bit++)) {
				o.p_multiply(i);
				o.p_modulo(mod);
			}	
			i.p_square();
			i.p_modulo(mod);
			
		}
		i.p_multiply(o);
		i.p_modulo(mod);
	}
	
	public static BigInteger p_modular_exponent(BigInteger i, BigInteger exponent, BigInteger mod)
	{
		if(exponent.equals(BigInteger.ZERO)) {
			return BigInteger.ONE;
		} else if(exponent.equals(BigInteger.ONE))
			return i;
		BigInteger n = exponent;
		BigInteger o = BigInteger.ONE;
		while(n.compareTo(BigInteger.ONE) > 0)
		{
			if(n.testBit(0)) {
				o = o.multiply(i);
				o = o.mod(mod);
			} 
			i = i.multiply(i);
			i = i.mod(mod);
			n = n.shiftRight(1);
		}
		return i.multiply(o).mod(mod);
	}
	
	public static <T extends IInteger<T>> IInteger<T> gcd(IInteger<T> i1, IInteger<T> i2)
	{
		i1 = i1.createDeepClone();
		i2 = i2.createDeepClone();
		IInteger<T> i3 = i1.createDeepClone();
		i3.setTo(1);
		while(!i1.isOdd() && !i2.isOdd()) {
			p_leftShift(i1, 1);
			p_leftShift(i2, 1);
			p_rightShift(i3, 1);
		}
		while(i1.doesNotEqual(i2)) {
			if(!i1.isOdd())
				p_leftShift(i1, 1);
			else if(!i2.isOdd())
				p_leftShift(i2, 1);
			else if(i1.isGreaterThan(i2)) {
				i1.p_subtract(i2);
				p_leftShift(i1, 1);
			} else {
				i2.p_subtract(i1);
				p_leftShift(i2, 1);
			}
		}
		i3.p_multiply(i1);
		return i3;
	}
	
	public static long gcd(long i1, long i2)
	{
		long i3 = 1;
		while((i1 & 1) == 0 && (i2 & 1) == 0) {
			i1 >>>= 1;
			i2 >>>= 2;
			i3 <<= 1;
		}
		while(i1 != i2) {
			if((i1 & 1) == 0)
				i1 >>>= 1;
			else if((i2 & 1) == 0)
				i2 >>>= 1;
			else if(i1 > i2) {
				i1 -= i2;
				i1 >>>= 1;
			} else {
				i2 -= i1;
				i2 >>>= 1;
			}
		}
		return i3 * i1;
	}
	
	public static <T extends IInteger<T>> IInteger<T> lcm(IInteger<T> i1, IInteger<T> i2)
	{
		IInteger<T> gcd = gcd(i1, i2);
		IInteger<T> X = i1.createDeepClone();
		X.p_divide(gcd);
		X.p_multiply(i2);
		return X;
	}
	
	public static long lcm(long i1, long i2)
	{
		return (i1 / gcd(i1, i2)) * i2;
	}
	
	public static boolean isPrimeProbableMR(IInteger<?> prospective, IRandom rng, int times)
	{
		IInteger<?> test = prospective.createDeepClone();
		test.decrement();
		IInteger<?> exponent = test.createDeepClone();
		IInteger<?> witness = prospective.createDeepClone();
		int checks = getLSB(exponent.getArr());
		p_leftShift(exponent, checks);
		while(times-- > 0)
		{
			RandUtils.fillArr(witness.getArr(), rng);
			witness.p_modulo(prospective);
			if(witness.getArr()[0] < 2 && witness.getArr()[0] >= 0) 
				witness.getArr()[0] += 2;
			p_modular_exponent(witness, exponent, prospective);
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
	
	public static UInt u_modular_inverse(UInt product, UInt modulus)
	{
		SInt s1 = new SInt(product.getArr());
		SInt s2 = new SInt(modulus.getArr());
		return new UInt(modular_inverse(s1, s2).getArr());
	}
	
	public static <Int extends IInteger<Int>> Int modular_inverse(Int product, Int modulus)
	{
		Int t = product.createDeepClone();
		t.zero();
		Int r = modulus.createDeepClone();
		Int t2 = t.createDeepClone();
		t2.increment();
		Int r2 = product.createDeepClone();
		if(r2.isGreaterOrEqualTo(modulus))
			r2.p_modulo(modulus);
		Int q;
		Int T;
		while(r2.isNonZero())
		{
			q = r.divide(r2);
			T = t;
			t = t2;
			t2 = T;
			t2.p_subtract(t.multiply(q));
			T = r;
			r = r2;
			r2 = T;
			r2.p_subtract(r.multiply(q));
		}
		if(r.isGreaterThan(1))
			t.zero();
		else if(t.isNegative())
			t.p_add(modulus);
		return t;	
	}
	
	public static int modular_inverse(int product, int modulus)
	{
		int t = 0;
		int r = modulus;
		int t2 = 1;
		int r2 = product;
		int q;
		int T;
		while(r2 != 0)
		{
			q = r / r2;
			T = t;
			t = t2;
			t2 = T - q * t;
			T = r;
			r = r2;
			r2 = T - q * r;
		}
		if(r > 1)
			return 0;
		else if(t < 0)
			return t + modulus;
		else 
			return t;	
	}
	
	/**
	 * Returns comparator based on int1 compared to int2 (ie, greater
	 * then means int1 > int2, not int2 > int1)
	 * <br>
	 * <ul>
	 * <li> 1 = Greater than</li>
	 * <li> 0 = Equal to</li>
	 * <li>-1 = Less than</li>
	 * </ul>
	 * @param int1 Integer 1
	 * @param int2 Integer 2
	 * @return Comparator
	 */
	public static int absolute_compare(int[] int1, int[] int2)
	{
		int i = int1.length - 1;
		if(int1.length > int2.length) {
			i = int2.length - 1;
			for(int j = int2.length; j < int1.length; j++)
				if(int1[j] != 0)
					return 1;
		} else if(int2.length > int1.length) {
			for(int j = int1.length; j < int2.length; j++)
				if(int2[j] != 0)
					return -1;
		}
		for(; i >= 0; i--) {
			if(int1[i] != int2[i])
				if(Bits.u_greaterThan(int1[i], int2[i]))
					return 1;
				else
					return -1;
		}
		return 0;
	}
	
	public static void getMinBitValue(IInteger<?> i, int bits)
	{
		i.increment();
		p_rightShift(i, bits - 1);
	}
	
	public static void getMinIntValue(IInteger<?> i, int ints)
	{
		i.getArr()[ints] = 1;
	}
	
	/**
	 * Fuck. Java.
	 * 
	 * @param divd The signed dividend
	 * @param divi The signed divisor
	 * @return The signed quotient
	 * <br><br>
	 * How does it return the result as if it were unsigned? Maaaaagic.
	 */
	public static long divideC(long divd, long divi)
	{
		if(divi < 0) {
			if(Bits.u_greaterOrEqual(divd, divi))
				return 1;
			else
				return 0;
		}
		if(divd > 0)
			return divd / divi;
		else {
			//Attention: You have reached the end of my patience. You have been warned.
			long approx = (divd >>> 1) / (divi >>> 1);
			long inv = divd - approx * divi;
			while(inv < 0) {
				approx--;
				inv += divi;
			}
			while(inv >= divi) {
				approx++;
				inv -= divi;
			}
			return approx;
		}
	}
	
	public static long moduloC(long divd, long divi)
	{
		if(divi < 0) {
			if(Bits.u_greaterOrEqual(divd, divi))
				return divd - divi;
			else
				return divd;
		}
		if(divd > 0)
			return divd % divi;
		else {
			//Attention: You have reached the end of my patience. You have been warned.
			long approx = (divd >>> 1) / (divi >>> 1);
			long inv = divd - approx * divi;
			while(inv < 0) {
				approx--;
				inv += divi;
			}
			while(inv >= divi) {
				approx++;
				inv -= divi;
			}
			return approx * divi;
		}
	}
	
	/**
	 * For use when an algorithm assumes there is a value in
	 * a spot when there isn't, but doesn't actually need to 
	 * write to that spot at any point.
	 * 
	 * @author Claire
	 */
	public static final class SafeArray {
		
		private final int[] array;
		
		public SafeArray(int[] array) { this.array = array; }
		
		public int get(int i)
		{
			try {
				return array[i];
			} catch(java.lang.ArrayIndexOutOfBoundsException e) {
				return 0;
			}
		}
		
		public void set(int i, int val)
		{
			if(val != 0)
				array[i] = val;
		}
	}
}

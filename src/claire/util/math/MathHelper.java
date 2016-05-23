package claire.util.math;

import java.math.BigInteger;

import claire.util.crypto.rng.RandUtils;
import claire.util.logging.Log;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.memory.util.Pointer;
import claire.util.standards.IInteger;
import claire.util.standards.crypto.IRandom;

public final class MathHelper {
	
	/**
	 * This method shifts an integer array right by <code>places</code> <i>bit
	 * positions</i>. Not to be confused with the array rotation methods in <code>
	 * ArrayUtil</code>. This method shifts both the <i>array</i> right (e.g, with
	 * a shift of 32 (one whole integer), it would move an int in position one to
	 * position two), and the <i>integers</i> right (in code it uses the <code>>>>
	 * </code> operator).
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An integer array of any length</li>
	 * <li>An integer greater then or equal to zero</li>
	 * </ul>
	 * If a number less then zero is passed, then an exception will be thrown, however
	 * the array is not guaranteed to survive intact.
	 * <br><br>
	 * Returns: void
	 * <br>
	 */
	public static void shiftArrayRight(final int[] arr, int places)
	{
		final int bplaces = places / 32;
		if(bplaces != 0)
			ArrayUtil.shiftRight(arr, bplaces);
		places &= 31;
		if(places > 0)
		{
			final int invplaces = 32 - places;
			int start = arr.length - bplaces - 1;
			while(start > 0)
				arr[start] = (arr[start--] >>> places) | (arr[start] << invplaces);
			arr[start] >>>= places;
		}
	}
	
	/**
	 * This method shifts an integer array left by <code>places</code> <i>bit
	 * positions</i>. Not to be confused with the array rotation methods in <code>
	 * ArrayUtil</code>. This method shifts both the <i>array</i> left (e.g, with
	 * a shift of 32 (one whole integer), it would move an int in position two to
	 * position one), and the <i>integers</i> left (in code it uses the <code><<
	 * </code> operator).
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An integer array of any length</li>
	 * <li>An integer greater then or equal to zero</li>
	 * </ul>
	 * If a number less then zero is passed, then an exception will be thrown, however
	 * the array is not guaranteed to survive intact.
	 * <br><br>
	 * Returns: void
	 * <br>
	 */
	public static void shiftArrayLeft(final int[] arr, int places)
	{
		final int bplaces = places / 32;
		if(bplaces != 0)
			ArrayUtil.shiftLeft(arr, bplaces);
		places &= 31;
		if(places > 0)
		{
			final int invplaces = 32 - places;
			int end = arr.length - bplaces - 1;
			int i = 0;
			while(i < end)
				arr[i] = (arr[i++] << places) | (arr[i] >>> invplaces);
			arr[i] <<= places;
		}
	}
	
	/**
	 * This method shifts an integer array right by <code>places</code> <i>bit
	 * positions</i>. Not to be confused with the array rotation methods in <code>
	 * ArrayUtil</code>. While this method shifts the <i>array</i> right (e.g, with
	 * a shift of 32 (one whole integer), it would move an int in position one to
	 * position two), it the integers <i><b>left</b></i> (in code it uses the <code><<
	 * </code> operator). This is so that multi-integer arithmetic with little-endian
	 * integer array but big bit-endian individual integers can be shifted properly
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An integer array of any length</li>
	 * <li>An integer greater then or equal to zero</li>
	 * </ul>
	 * If a number less then zero is passed, then an exception will be thrown, however
	 * the array is not guaranteed to survive intact.
	 * <br><br>
	 * Returns: void
	 * <br>
	 */
	public static void shiftArrayRightBE(final int[] arr, int places)
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
			final int invplaces = 32 - places;
			int start = arr.length - bplaces - 1;
			while(start > 0)
				arr[start] = (arr[start--] << places) | (arr[start] >>> invplaces);
			arr[0] <<= places;
		}
		if(bplaces != 0)
			ArrayUtil.shiftRight(arr, bplaces);
	}
	
	/**
	 * This method shifts an integer array left by <code>places</code> <i>bit
	 * positions</i>. Not to be confused with the array rotation methods in <code>
	 * ArrayUtil</code>. While this method shifts the <i>array</i> right (e.g, with
	 * a shift of 32 (one whole integer), it would move an int in position one to
	 * position two), it the integers <i><b>right</b></i> (in code it uses the <code>>>>
	 * </code> operator). This is so that multi-integer arithmetic with bit-endian
	 * integer array but little bit-endian individual integers can be shifted properly
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An integer array of any length</li>
	 * <li>An integer greater then or equal to zero</li>
	 * </ul>
	 * If a number less then zero is passed, then an exception will be thrown, however
	 * the array is not guaranteed to survive intact.
	 * <br><br>
	 * Returns: void
	 * <br>
	 */
	public static void shiftArrayLeftBE(final int[] arr, int places)
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
			final int invplaces = 32 - places,
					  end = arr.length - bplaces - 1;
			int i = 0;
			while(i < end)
				arr[i] = (arr[i++] >>> places) | (arr[i] << invplaces);
			arr[i] >>>= places;
		}
		if(bplaces != 0)
			ArrayUtil.shiftLeft(arr, bplaces);
	}
	
	/**
	 * This method finds the least significant bit in an integer array. 
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An integer array with a length greater then zero, but less then 2<sup>26</sup>
	 * , where at least one nonzero value is present</li>
	 * </ul>
	 * <br>
	 * <ul>
	 * <li>If an array with a length of zero is passed, a NullPointerException
	 * will be thrown</li>
	 * <li>If an array entirely comprised of zeros is passed, a NullPointerException
	 * will be thrown.</li>
	 * <li>If an array too large is passed, this method may return a negative value,
	 * and will always return an incorrect value.</li>
	 * </ul>
	 * <br>
	 * Returns: an integer indicating the position of the Least Significant bit from
	 * position <code>[0]</code> in the array from position <code>[0]</code> in the 
	 * integer (0x00000001 position). 
	 */
	public static int getLSB(final int[] arr)
	{
		int total = 0;
		int i = -1;
		while(arr[++i] == 0) 
			total += 32;
		return total + Bits.getLSB(arr[i]);
	}
	
	/**
	 * This method finds the most significant bit in an integer array. 
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An integer array with a length greater then zero, but less then 2<sup>26</sup>
	 * , where at least one nonzero value is present</li>
	 * </ul>
	 * <br>
	 * <ul>
	 * <li>If an array with a length of zero is passed, a ArrayIndexOutOfBoundsException
	 * will be thrown</li>
	 * <li>If an array entirely comprised of zeros is passed, a NullPointerException
	 * will be thrown.</li>
	 * <li>If an array too large is passed, this method may return a negative value,
	 * and will always return an incorrect value.</li>
	 * </ul>
	 * <br>
	 * Returns: an integer indicating the position of the Most Significant bit from
	 * position <code>[0]</code> in the array from position <code>[0]</code> in the 
	 * integer (0x00000001 position). 
	 */
	public static int getMSB(final int[] arr)
	{
		int total = arr.length << 5;
		int i = arr.length;
		while(arr[--i] == 0)
			total -= 32;
		total -= 32;
		return total + Bits.getMSB(arr[i]);
	}
	
	public static void truncate(int[] ints, int bits)
	{
		int i = bits / 32;
		bits &= 31;
		if(i < ints.length) {
			ints[i] = Bits.truncate(ints[i++], bits);
			while(i < ints.length)
				ints[i++] = 0;
		}
	}
	
	public static void truncate(IInteger<?> in, int bits)
	{
		truncate(in.getArr(), bits);
	}
	
	public static <Int extends IInteger<Int>> Int randomInteger(IntegerFactory<Int> factory, IRandom<?, ?> rand, int bits)
	{
		int intz = bits / 32;
		int rem = bits & 31;
		boolean trunc = rem != 0;
		if(trunc)
			intz++;
		int[] ints = new int[intz--];
		rand.readInts(ints);
		if(trunc)
			ints[intz] = Bits.truncate(ints[intz], rem);
		return factory.construct(ints);
	}
	
	public static <Int extends IInteger<Int>> Int randomInteger(IntegerFactory<Int> factory, int req, IRandom<?, ?> rand, int bits)
	{
		int intz = bits / 32;
		int rem = bits & 31;
		boolean trunc = rem != 0;
		if(trunc)
			intz++;
		int[] ints = new int[req];
		rand.readInts(ints, 0, intz--);
		if(trunc)
			ints[intz] = Bits.truncate(ints[intz], rem);
		return factory.construct(ints);
	}
	
	public static <Int extends IInteger<Int>> void randomInteger(Int i, IRandom<?, ?> rand, int bits)
	{
		int intz = bits / 32;
		int rem = bits & 31;
		boolean trunc = rem != 0;
		if(trunc)
			intz++;
		int[] ints = i.getArr();
		rand.readInts(ints, 0, intz--);
		if(trunc)
			ints[intz] = Bits.truncate(ints[intz], rem);
	}
	
	public static <Int extends IInteger<Int>> Int strictRandomInteger(IntegerFactory<Int> factory, IRandom<?, ?> rand, int bits)
	{
		int intz = bits / 32;
		int rem = bits & 31;
		boolean trunc = rem != 0;
		if(trunc)
			intz++;
		int[] ints = new int[intz--];
		rand.readInts(ints);
		if(trunc)
			ints[intz] = Bits.truncate(ints[intz], rem);
		Bits.setBit(ints, bits - 1, true);
		return factory.construct(ints);
	}
	
	public static <Int extends IInteger<Int>> Int strictRandomInteger(IntegerFactory<Int> factory, int req, IRandom<?, ?> rand, int bits)
	{
		int intz = bits / 32;
		int rem = bits & 31;
		boolean trunc = rem != 0;
		if(trunc)
			intz++;
		int[] ints = new int[req];
		rand.readInts(ints, 0, intz--);
		if(trunc)
			ints[intz] = Bits.truncate(ints[intz], rem);
		Bits.setBit(ints, bits - 1, true);
		return factory.construct(ints);
	}
	
	public static <Int extends IInteger<Int>> void strictRandomInteger(Int i, IRandom<?, ?> rand, int bits)
	{
		int intz = bits / 32;
		int rem = bits & 31;
		boolean trunc = rem != 0;
		if(trunc)
			intz++;
		int[] ints = i.getArr();
		rand.readInts(ints, 0, intz--);
		if(trunc)
			ints[intz] = Bits.truncate(ints[intz], rem);
		Bits.setBit(ints, bits - 1, true);
	}
	public static <Int extends IInteger<Int>> Int randomIntegerFast(Int max, IRandom<?, ?> rand) //Fast, modulo bias
	{
		Int kek = max.createDeepClone();
		int[] ints = kek.getArr();
		rand.readInts(ints, 0, getRealLength(ints));
		kek.p_modulo(max);
		return kek;
	}
	
	public static <Int extends IInteger<Int>> Int randomIntegerGood(Int max, IRandom<?, ?> rand) //Good (but not perfect)
	{
		Int kek = max.createDeepClone();
		//generate roundup(max.getBits()) + 64 bits of data
		int[] ints = kek.getArr();
		rand.readInts(ints, 0, (max.getBits() / 32) + (((max.getBits() & 31) > 0) ? 1 : 0) + 2);
		kek.p_modulo(max);
		return kek;
	}
	
	public static <Int extends IInteger<Int>> Int randomIntegerUniform(Int max, IRandom<?, ?> rand) //As good as the RNG (zero modulo bias). 
	{
		Int kek = max.createDeepClone();
		int[] ints = kek.getArr();
		int req = getRealLength(ints);
		int bits = max.getBits();
		do {
			rand.readInts(ints, 0, req);
			truncate(ints, bits);
		} while(kek.isGreaterOrEqualTo(max));
		return kek;
	}
	
	/**
	 * This method shifts an integer right by <code>places</code> <i>bit
	 * positions</i>. 
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any specification</li>
	 * <li>An integer greater then or equal to zero</li>
	 * </ul>
	 * If a number less then zero is passed, then an exception will be thrown, however
	 * the array is not guaranteed to survive intact.
	 * <br><br>
	 * Returns: void
	 * <br>
	 */
	public static void p_rightShift(final IInteger<?> i, final int bits)
	{
		shiftArrayRightBE(i.getArr(), bits);
	}
	
	/**
	 * This method shifts an integer left by <code>places</code> <i>bit
	 * positions</i>. 
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any specification</li>
	 * <li>An integer greater then or equal to zero</li>
	 * </ul>
	 * If a number less then zero is passed, then an exception will be thrown, however
	 * the array is not guaranteed to survive intact.
	 * <br><br>
	 * Returns: void
	 * <br>
	 */
	public static void p_leftShift(final IInteger<?> i, final int bits)
	{
		shiftArrayLeftBE(i.getArr(), bits);
	}
	
	/**
	 * This method finds the amounts of integers actually required for the array
	 * to retain the same value.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An array with a length greater then zero</li>
	 * </ul>
	 * <br>
	 * If the length is zero, an ArrayIndexOutOfBounds Exception will be thrown.
	 * <br><br>
	 * Returns: the amount of integers required to represent the array as a little-
	 * endian multi-word integer.
	 */
	public static int getRealLength(final int[] num)
	{
		int i = num.length - 1;
		while(i != -1 && num[i] == 0)
			i--;
		return i + 1;
	}

	/**
	 * This method returns the <i>absolute</i> value of the number passed (the
	 * distance from zero on the number line).
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A double of any value</li>
	 * </ul>
	 * This method is safe, and will always return the correct number (not accounting
	 * for floating-point errors).
	 * <br><br>
	 * Returns: the absolute value of double <code>d</code>.
	 */
	public static double absolute(double d)
	{
		if(d < 0) { d *= (double)-1; }
		return d;
	}
	
	/**
	 * This method returns the <i>absolute</i> value of the number passed (the
	 * distance from zero on the number line).
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A long of any value</li>
	 * </ul>
	 * This method is safe, and will always return the correct number.
	 * <br><br>
	 * Returns: the absolute value of long <code>l</code>.
	 */
	public static long absolute(long l)
	{
		if(l < 0) { l *= -1; }
		return l;
	}
	
	
	
	/**
	 * This method finds the greatest value in an array of longs.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An array of longs with length greater then zero</li>
	 * </ul>
	 * If an array of length zero is passed, and ArrayIndexOutOfBounds
	 * Exception will be thrown.
	 * <br><br>
	 * Returns: The long in the array with the greatest value.
	 */
	public static long ceiling(final long ... longs)
	{
		long r = longs[0];
		for(int i = 1; i < longs.length; i++)
			if(longs[i] > r) 
				r = longs[i];
		return r;
	}
	
	/**
	 * This method finds the smallest value in an array of longs.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An array of longs with length greater then zero</li>
	 * </ul>
	 * If an array of length zero is passed, and ArrayIndexOutOfBounds
	 * Exception will be thrown.
	 * <br><br>
	 * Returns: The long in the array with the smallest value.
	 */
	public static long floor(final long ... longs)
	{
		long r = longs[0];
		for(int i = 1; i < longs.length; i++)
			if(longs[i] < r) 
				r = longs[i];
		return r;
	}
	
	/**
	 * This method finds the greatest value in an array of doubles.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An array of doubles with length greater then zero</li>
	 * </ul>
	 * If an array of length zero is passed, and ArrayIndexOutOfBounds
	 * Exception will be thrown.
	 * <br><br>
	 * Returns: The doubles in the array with the greatest value.
	 */
	public static double ceiling(final double ... longs)
	{
		double r = longs[0];
		for(int i = 1; i < longs.length; i++)
			if(longs[i] > r) 
				r = longs[i];
		return r;
	}
	
	/**
	 * This method finds the smallest value in an array of doubles.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An array of doubles with length greater then zero</li>
	 * </ul>
	 * If an array of length zero is passed, and ArrayIndexOutOfBounds
	 * Exception will be thrown.
	 * <br><br>
	 * Returns: The doubles in the array with the smallest value.
	 */
	public static double floor(final double ... longs)
	{
		double r = longs[0];
		for(int i = 1; i < longs.length; i++)
			if(longs[i] < r) 
				r = longs[i];
		return r;
	}
	
	/**
	 * This method finds the distance between two numbers on the number line
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>Any two longs that must have a distance of less then 2<sup>63</sup></li>
	 * </ul>
	 * If the two longs have a distance greater then 2<sup>63</sup>, then
	 * a negative signed value will return. If parsed in an unsigned manner, the
	 * value will be correct.
	 * <br><br>
	 * Returns: The long in the array with the greatest value.
	 */
	public static long distancefrom(long l1, long l2)
	{
		return l1 > l2 ? l1 - l2 : l2 - l1;
	}
	
	public static void add1(int[] r1, int add)
	{
		int i = r1[0];
		r1[0] += add;
		if(Bits.u_greaterThan(i, r1[0])) {
			r1[1]++;
			i = 1;
			while(r1[i] == 0 && ++i < r1.length)
				r1[i]++;
		}
	}
	
	public static int subtract1(int[] r1, int sub)
	{
		int bf = r1[0];
		r1[0] -= sub;
		if(Bits.u_greaterThan(r1[0], bf)) {
			bf = 1;
			do {
				r1[bf]--;
			} while(r1[bf] == -1 && ++bf < r1.length);
			if(bf == r1.length)
				return 1;
		}
		return 0;
	}
	
	/**
	 * Multiplies Base2^32 Integer by one 32-bit integer.
	 * Returns any carry-over from the calculation.
	 * 
	 * No documentation required. If you are using this method
	 * outside of claire.util.math, then you are doing something 
	 * wrong.
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
		if(k < r1.length && carry != 0) {
			r1[k] = (int) carry;
			carry = 0;
		}
		return (int) carry;
	}
	
	/**
	 * TODO: Write documentation for method once finished.
	 */
	public static long exponent(long i, long exponent)
	{
		if(exponent == 0) {
			return 1;
		} 
		if(exponent == 1)
			return i;
		long o = 1;
		while(exponent > 1)
		{
			if((exponent & 1) == 1) 
				o *= i;
			i *= i;
			exponent >>>= 1;
		}
		return i * o;
	}
	
	public static long modular_exponent(long i, long exponent, long mod)
	{
		if(exponent == 0) {
			return 1;
		} 
		if(exponent == 1)
			return i;
		long o = 1;
		while(exponent > 1)
		{
			if((exponent & 1) == 1) { 
				o *= i;
				o %= mod;
			}
			i *= i;
			i %= mod;
			exponent >>>= 1;
		}
		return (i * o) % mod;
	}
	
	/**
	 * This method takes the exponent of an IInteger by a 32-bit integer
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An Integer greater then or equal to zero</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns: void, however the result ends up in <code>i</code>.
	 */
	public static void p_exponent(final IInteger<?> i, int exponent)
	{
		if(exponent == 0) {
			i.setTo(1);
			return;
		} 
		if(exponent == 1)
			return;
		IInteger<?> o = i.createDeepClone();
		int max = Bits.getMSB(exponent);
		int bit = max - 1;
		while(bit > -1)
		{
			i.p_square();
			if(Bits.getBit(exponent, bit--)) 
				i.p_multiply(o);
		}
	}
	
	/**
	 * This method takes the exponent of an IInteger by another IInteger
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An IInteger greater then or equal to zero</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns: void, however the result ends up in <code>i</code>.
	 */
	public static void p_exponent(final IInteger<?> i, final IInteger<?> exponent)
	{
		if(getRealLength(exponent.getArr()) == 1)
			p_exponent(i, exponent.getArr()[0]);
		else
			p_exponent_sure(i, exponent);
	}
	
	/**
	 * This method takes the exponent of an IInteger by another IInteger, without
	 * testing if the number can fit in 32 bits.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An IInteger greater then or equal to zero</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns: void, however the result ends up in <code>i</code>.
	 */
	public static void p_exponent_sure(final IInteger<?> i, final IInteger<?> exponent)
	{
		if(!exponent.isNonZero()) {
			i.setTo(1);
			return;
		}
		if(exponent.isEqualTo(1))
			return;
		final IInteger<?> o = i.createDeepClone();
		final int max = getMSB(exponent.getArr());
		int bit = max - 1;
		while(bit >= 0)
		{
			i.p_square();	
			if(exponent.bitAt(bit--)) 
				i.p_multiply(o);			
		}
	}
	
	/**
	 * This method takes the exponent of an IInteger by an IInteger,
	 * modulo a IInteger.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An IInteger greater then or equal to zero</li>
	 * <li>An IInteger of any value</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns: void, however the result ends up in <code>i</code>.
	 */
	public static void p_modular_exponent(final IInteger<?> i, final IInteger<?> exponent, final IInteger<?> mod)
	{
		if(getRealLength(exponent.getArr()) == 1)
			p_modular_exponent(i, exponent.getArr()[0] & 0xFFFFFFFFL, mod);
		else
			p_modular_exponent_sure(i, exponent, mod);
	}
	
	/**
	 * This method takes the exponent of an IInteger by a 32-bit integer,
	 * modulo a IInteger.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An Integer greater then or equal to zero</li>
	 * <li>An IInteger of any value</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns: void, however the result ends up in <code>i</code>.
	 */
	public static void p_modular_exponent(final IInteger<?> i, long exponent, final IInteger<?> mod)
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
		int max = Bits.getMSB(exponent);
		int bit = max - 1;
		while(bit > -1)
		{
			i.p_square();
			i.p_modulo(mod);
			if(Bits.getBit(exponent, bit--)) {
				i.p_multiply(o);
				i.p_modulo(mod);
			}
		}
	}
	
	/**
	 * This method takes the exponent of an IInteger by an IInteger,
	 * modulo an IInteger without checking if the exponent can fit
	 * int 32 bits.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An IInteger greater then or equal to zero</li>
	 * <li>An IInteger of any value</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns: void, however the result ends up in <code>i</code>.
	 */
	public static void p_modular_exponent_sure(final IInteger<?> i, final IInteger<?> exponent, final IInteger<?> mod)
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
		final IInteger<?> o = i.createDeepClone();
		final int max = getMSB(exponent.getArr());
		int bit = max - 1;
		while(bit > -1)
		{
			i.p_square();
			i.p_modulo(mod);		
			if(exponent.bitAt(bit--)) {
				i.p_multiply(o);
				i.p_modulo(mod);
			}	
		}
	}
	
	/**
	 * This method takes the exponent of an IInteger by a 32-bit integer
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An Integer greater then or equal to zero</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns: <i>i</i><sup><i>exponent</i></sup>
	 */
	public static IInteger<?> exponent(final IInteger<?> i, int exponent)
	{
		IInteger<?> n = i.createDeepClone();
		if(exponent == 0) {
			n.setTo(1);
			return n;
		} 
		if(exponent == 1)
			return n;
		IInteger<?> o = i.createDeepClone();
		o.setTo(1);
		while(exponent > 1)
		{
			if((exponent & 1) == 1) 
				o.p_multiply(i);
			n.p_square();
			exponent >>>= 1;
		}
		n.p_multiply(o);
		return n;
	}
	
	/**
	 * This method takes the exponent of an IInteger by another IInteger
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An IInteger greater then or equal to zero</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns: <i>i</i><sup><i>exponent</i></sup>
	 */
	public static IInteger<?> exponent(final IInteger<?> i, final IInteger<?> exponent)
	{
		if(getRealLength(exponent.getArr()) == 1)
			return exponent(i, exponent.getArr()[0]);
		else
			return exponent_sure(i, exponent);
	}
	
	/**
	 * This method takes the exponent of an IInteger by another IInteger, without
	 * testing if the number can fit in 32 bits.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An IInteger greater then or equal to zero</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns:<i>i</i><sup><i>exponent</i></sup>
	 */
	public static IInteger<?> exponent_sure(final IInteger<?> i, final IInteger<?> exponent)
	{
		IInteger<?> n = i.createDeepClone();
		if(!exponent.isNonZero()) {
			n.setTo(1);
			return n;
		}
		if(exponent.isEqualTo(1))
			return n;
		final IInteger<?> o = i.createDeepClone();
		final int max = getMSB(exponent.getArr());
		int bit = 0;
		o.setTo(1);
		while(bit < max)
		{
			if(exponent.bitAt(bit++)) 
				o.p_multiply(i);
			n.p_square();			
		}
		n.p_multiply(o);
		return n;
	}
	
	/**
	 * This method takes the exponent of an IInteger by an IInteger,
	 * modulo a IInteger.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An IInteger greater then or equal to zero</li>
	 * <li>An IInteger of any value</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns: <i>i</i><sup><i>exponent</i></sup> mod <i>mod</i>
	 */
	public static IInteger<?> modular_exponent(final IInteger<?> i, final IInteger<?> exponent, final IInteger<?> mod)
	{
		if(getRealLength(exponent.getArr()) == 1)
			return modular_exponent(i, exponent.getArr()[0] & 0xFFFFFFFFL, mod);
		else
			return modular_exponent_sure(i, exponent, mod);
	}
	
	/**
	 * This method takes the exponent of an IInteger by a 32-bit integer,
	 * modulo a IInteger.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An Integer greater then or equal to zero</li>
	 * <li>An IInteger of any value</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns: <i>i</i><sup><i>exponent</i></sup> mod <i>mod</i>
	 */
	public static IInteger<?> modular_exponent(final IInteger<?> i, long exponent, final IInteger<?> mod)
	{
		IInteger<?> n = i.createDeepClone();
		if(exponent == 0) {
			n.setTo(1);
			return n;
		} 
		if(n.isGreaterOrEqualTo(mod))
			n.p_modulo(mod);
		if(exponent == 1)
			return n;
		final IInteger<?> o = i.createDeepClone();
		o.setTo(1);
		while(exponent > 1)
		{
			if((exponent & 1) == 1) {
				o.p_multiply(i);
				o.p_modulo(mod);
			}
			n.p_square();
			n.p_modulo(mod);
			exponent >>>= 1;
		}
		n.p_multiply(o);
		n.p_modulo(mod);
		return n;
	}
	
	/**
	 * This method takes the exponent of an IInteger by an IInteger,
	 * modulo an IInteger without checking if the exponent can fit
	 * int 32 bits.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An IInteger greater then or equal to zero</li>
	 * <li>An IInteger of any value</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns: <i>i</i><sup><i>exponent</i></sup> mod <i>mod</i>
	 */
	public static IInteger<?> modular_exponent_sure(final IInteger<?> i, final IInteger<?> exponent, final IInteger<?> mod)
	{
		IInteger<?> n = i.createDeepClone();
		if(!exponent.isNonZero()) {
			n.setTo(1);
			return n;
		}
		if(n.isGreaterOrEqualTo(mod)) {
			n.p_modulo(mod);
		}
		if(exponent.isEqualTo(1))
			return n;
		final IInteger<?> o = i.createDeepClone();
		final int max = getMSB(exponent.getArr());
		int bit = 0;
		o.setTo(1);
		while(bit < max)
		{
			if(exponent.bitAt(bit++)) {
				o.p_multiply(i);
				o.p_modulo(mod);
			}	
			n.p_square();
			n.p_modulo(mod);		
		}
		n.p_multiply(o);
		n.p_modulo(mod);
		return n;
	}
	
	/**
	 * This method takes greatest common divisor of two IIntegers.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An Int greater then or equal to one</li>
	 * <li>An Int greater then or equal to one</li>
	 * </ul>
	 * <br>
	 * <ul>
	 * <li>Either IInteger being 0 or 1 will result in an infinite loop.</li>
	 * <li>Negative numbers will result in undefined behavior/</li>
	 * <br><br>
	 * Returns: An Int of the same type that holds the value of the greatest
	 * common devisor of <code>i1</code> and <code>i2</code>.
	 */
	public static <Int extends IInteger<Int>> IInteger<Int> gcd(IInteger<Int> i1, IInteger<Int> i2)
	{
		i1 = i1.createDeepClone();
		i2 = i2.createDeepClone();
		IInteger<Int> i3 = i1.createDeepClone();
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
	
	/**
	 * This method takes greatest common divisor of two Longs.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An Long greater then or equal to one</li>
	 * <li>An Long greater then or equal to one</li>
	 * </ul>
	 * <br>
	 * <ul>
	 * <li>Either Long being 0 or 1 will result in an infinite loop.</li>
	 * <li>Negative numbers will result in undefined behavior/</li>
	 * <br><br>
	 * Returns: An Long of the same type that holds the value of the greatest
	 * common devisor of <code>i1</code> and <code>i2</code>.
	 */
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
	
	/**
	 * This method takes lowest common multiple of two IIntegers.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An Int greater then or equal to one</li>
	 * <li>An Int greater then or equal to one</li>
	 * </ul>
	 * <br>
	 * <ul>
	 * <li>Either IInteger being 0 or 1 will result in an infinite loop.</li>
	 * <li>Negative numbers will result in undefined behavior/</li>
	 * <br><br>
	 * Returns: An Int of the same type that holds the value of the lowest
	 * common multiple of <code>i1</code> and <code>i2</code>.
	 */
	public static <Int extends IInteger<Int>> IInteger<Int> lcm(final IInteger<Int> i1, final IInteger<Int> i2)
	{
		IInteger<Int> gcd = gcd(i1, i2);
		IInteger<Int> X = i1.createDeepClone();
		X.p_divide(gcd);
		X.p_multiply(i2);
		return X;
	}
	
	/**
	 * This method takes lowest common multiple of two Longs.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An Long greater then or equal to one</li>
	 * <li>An Long greater then or equal to one</li>
	 * </ul>
	 * <br>
	 * <ul>
	 * <li>Either Long being 0 or 1 will result in an infinite loop.</li>
	 * <li>Negative numbers will result in undefined behavior/</li>
	 * <br><br>
	 * Returns: An Long of the same type that holds the value of the lowest
	 * common multiple of <code>i1</code> and <code>i2</code>.
	 */
	public static long lcm(long i1, long i2)
	{
		return (i1 / gcd(i1, i2)) * i2;
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
	public static boolean isPrimeProbableMR(final IInteger<?> prospective, final IRandom<?, ?> rng, int times)
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
			witness.p_modulo(test);
			/* 
			 * Note: This check will cause a very rare problem of the witness
			 * being incremented beyond the modulus. 
			 */
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
	
	/**
	 * A helper method to compute the modular inverse of <code>product</code>
	 * mod <code>modulus</code> where product and modulus are unsigned.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A UInt greater then one, coprime to <code>modulus</code></li>
	 * <li>A UInt greater then <code>product</code></li>
	 * </ul>
	 * <br><br>
	 * <ul>
	 * <li>If <code>product</code> is not coprime to <code>modulus</code>, this
	 * will likely return 0, though doesn't have too</li>
	 * <li>Any value being zero will return zero</li>
	 * <li>Values of one for either the product or modulus will result in
	 * undefined behavior</li>
	 * </ul>
	 * <br><br>
	 * Returns: The modular inverse of product mod modulus.
	 */
	public static UInt u_modular_inverse(final UInt product, final UInt modulus)
	{
		SInt s1 = new SInt(product.getArr());
		SInt s2 = new SInt(modulus.getArr());
		return new UInt(modular_inverse(s1, s2).getArr());
	}
	
	/**
	 * A method to compute the modular inverse of <code>product</code>
	 * mod <code>modulus</code>.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A Signed IInteger greater then one, coprime to <code>modulus</code></li>
	 * <li>A Signed IInteger then <code>product</code></li>
	 * </ul>
	 * <br><br>
	 * <ul>
	 * <li>If <code>product</code> is not coprime to <code>modulus</code>, this
	 * will likely return 0, though doesn't have too</li>
	 * <li>Any lower then two for either product or modulus will result in 
	 * undefined behavior.</li>
	 * <li>Either operand being unsigned will result in undefined behavior</li>
	 * </ul>
	 * <br><br>
	 * Returns: The modular inverse of product mod modulus.
	 */
	public static <Int extends IInteger<Int>> Int modular_inverse(final Int product, final Int modulus)
	{
		Int t = product.createDeepClone(); t.zero();
		Int r = modulus.createDeepClone();
		Int t2 = t.createDeepClone(); t2.increment();
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
	
	/**
	 * A method to compute the modular inverse of <code>product</code>
	 * mod <code>modulus</code>.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A Long greater then one, coprime to <code>modulus</code></li>
	 * <li>A Long greater then <code>product</code></li>
	 * </ul>
	 * <br><br>
	 * <ul>
	 * <li>If <code>product</code> is not coprime to <code>modulus</code>, this
	 * will likely return 0, though doesn't have too</li>
	 * <li>Any lower then two for either product or modulus will result in 
	 * undefined behavior.</li>
	 * </ul>
	 * <br><br>
	 * Returns: The modular inverse of product mod modulus.
	 */
	public static int modular_inverse(final int product, final int modulus)
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
	 * then means int1 > int2, not int2 > int1). Comparator treats integer
	 * array as little-endian.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An integer array</li>
	 * <li>An integer array</li>
	 * </ul>
	 * This method is safe for all inputs.
	 * <br><br>
	 * Returns: and integer according to this table:
	 * <ul>
	 * <li> 1 = Greater than</li>
	 * <li> 0 = Equal to</li>
	 * <li>-1 = Less than</li>
	 * </ul>
	 */
	public static int absolute_compare(final int[] int1, final int[] int2)
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
	
	public static int absolute_compare(final int[] int1, int i2)
	{
		int len = getRealLength(int1);
		if(len > 1)
			return 1;
		else if(len == 1) {
			int j = int1[0];
			if(j > i2)
				return 1;
			else if(j == i2)
				return 0;
			else
				return -1;
		} else
			return i2 > 0 ? 1 : 0;
	}
	
	/**
	 * This method takes an IInteger and makes it equal to 2<sup>
	 * <code>bits</code></sup>. Or you could say it sets the <code>
	 * bits</code>-th bit starting at 0 being the first bit.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger equal to zero</li>
	 * <li>An Integer greater or equal to zero</li>
	 * </ul>
	 * <br>
	 * <ul>
	 * <li>If <code>i != 0</code> then the behavior of this method is undefined.</li>
	 * <li>If <code>bits < 0</code> then the behavior of this method is undefined.</li>
	 * </ul>
	 * <br>
	 * Returns: null, <code>i</code> is set to the desired value.
	 */
	public static void getMinBitValue(final IInteger<?> i, final int bits)
	{
		i.increment();
		p_rightShift(i, bits);
	}
	
	/**
	 * This method takes an IInteger and sets its <code>bits</code>
	 * -th integer to one. This is equivalent to running <code>
	 * getMinBitValue(ints * 32)</code>, however this method will return
	 * faster.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger equal to zero</li>
	 * <li>An Integer greater or equal to zero</li>
	 * </ul>
	 * <br>
	 * <ul>
	 * <li>If <code>i != 0</code> then the behavior of this method is undefined.</li>
	 * <li>If <code>bits < 0</code> then the behavior of this method is undefined.</li>
	 * </ul>
	 * <br>
	 * Returns: null, <code>i</code> is set to the desired value.
	 */
	public static void getMinIntValue(final IInteger<?> i, final int ints)
	{
		i.getArr()[ints] = 1;
	}
	
	/**
	 * This method divides two long integers as if they were unsigned,
	 * will take much longer then normal division by several times (Try
	 * to avoid this).
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A long of any value</li>
	 * <li>A long of any value</li>
	 * </ul>
	 * This method is safe for any set of inputs.
	 * <br><br>
	 * Returns: The result of unsigned division between <code>divd / divi</code>
	 */
	public static long divideC(final long divd, final long divi)
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
	
	/**
	 * This method computes the modulus two long integers as if they were 
	 * unsigned, will take much longer then normal modulus by several times 
	 * (Try to avoid this).
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A long of any value</li>
	 * <li>A long of any value</li>
	 * </ul>
	 * This method is safe for any set of inputs.
	 * <br><br>
	 * Returns: The result of unsigned modular reduction between <code>divd %
	 * divi</code>
	 */
	public static long moduloC(final long divd, final long divi)
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
			return divd - (approx * divi);
		}
	}
	
	/**
	 * This method divides two long integers as if they were unsigned,
	 * will take much longer then normal division by several times (Try
	 * to avoid this). 
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A long of any value</li>
	 * <li>A long of any value</li>
	 * <li>A long pointer<li>
	 * </ul>
	 * This method is safe for any non-null set of inputs.
	 * <br><br>
	 * Returns: The result of unsigned division between <code>divd / divi</code>,
	 * puts the result of unsigned modular reduction in <code>p</code>.
	 */
	public static long dividemodC(final long divd, final long divi, final Pointer<Long> p)
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
			p.set(divd - (approx * divi));
			return approx;
		}
	}
	
	public static <Int extends IInteger<Int>> Int genPrimitiveRoot(Int strongPrime)
	{
		Int t1 = strongPrime.createDeepClone();
		Int t2 = strongPrime.createDeepClone();
		t1.decrement();
		t1.p_divide(2);
		int start = 2;
		while(true)
		{
			t2.setTo(start);
			MathHelper.p_modular_exponent(t2, t1, strongPrime);
			if(t2.equals(1)) {
				start++;
				continue;
			}
			t2.setTo(start);
			MathHelper.p_modular_exponent(t2, 2, strongPrime);
			if(t2.equals(1)) {
				start++;
				continue;
			}
			break;
		}
		t2.setTo(start);
		return t2;
	}
	
	public static <Int extends IInteger<Int>> Int genPrimitiveRoot(Int strongPrime, int start)
	{
		Int t1 = strongPrime.createDeepClone();
		Int t2 = strongPrime.createDeepClone();
		t1.decrement();
		t1.p_divide(2);
		while(true)
		{
			t2.setTo(start);
			MathHelper.p_modular_exponent(t2, t1, strongPrime);
			if(t2.equals(1)) {
				start++;
				continue;
			}
			t2.setTo(start);
			MathHelper.p_modular_exponent(t2, 2, strongPrime);
			if(t2.equals(1)) {
				start++;
				continue;
			}
			break;
		}
		t2.setTo(start);
		return t2;
	}
	
	public static <Int extends IInteger<Int>>Int getPrimitiveRoot(Int strongPrime, Int start)
	{
		Int t1 = strongPrime.createDeepClone();
		Int t2 = strongPrime.createDeepClone();
		Int t3 = start.createDeepClone();
		t1.setTo(strongPrime);
		t1.decrement();
		t1.p_divide(2);
		while(true)
		{
			t2.setTo(t3);
			MathHelper.p_modular_exponent(t2, t1, strongPrime);
			if(t2.equals(1)) {
				t3.increment();
				continue;
			}
			t2.setTo(start);
			MathHelper.p_modular_exponent(t2, 2, strongPrime);
			if(t2.equals(1)) {
				t3.increment();
				continue;
			}
			break;
		}
		t2.setTo(start);
		return t2;
	}
	
	public static int testExponentiation()
	{
		int er = 0;
		try {
			int exp = RandUtils.inRange(50, 600);
			UInt u = new UInt("7", 128);
			UInt u2 = u.createDeepClone();
			UInt e = new UInt(Integer.toString(exp), 8);
			BigInteger b = new BigInteger("7");
			b = b.pow(exp);
			p_exponent(u2, exp);
			if(!b.toString().equals(u2.toString())) {
				er++;
				Log.err.println("p_exponent failed to deliver proper results");
			}
			u2 = u.createDeepClone();
			p_exponent(u2, e);
			if(!b.toString().equals(u2.toString())) {
				er++;
				Log.err.println("p_exponent with IInteger failed to deliver proper results");
			}
			u2 = u.createDeepClone();
			p_exponent_sure(u2, e);
			if(!b.toString().equals(u2.toString())) {
				er++;
				Log.err.println("p_exponent_sure with IInteger failed to deliver proper results");
			}
			return er;
		} catch(Exception e) {
			Log.err.println("Exception encountered while testing exponentiation of integers: " + e.getMessage());
			e.printStackTrace();
			return er + 1;
		}
	}
	
	public static int testModularExponentiation()
	{
		int er = 0;
		try {
			UInt u1 = MathHelper.randomInteger(UInt.ifactory, 32, RandUtils.dprng, 16 * 32 - 1);
			UInt u2 = MathHelper.randomInteger(UInt.ifactory, 32, RandUtils.dprng, 16 * 32);
			UInt u3 = MathHelper.randomInteger(UInt.ifactory, 32, RandUtils.dprng, 16 * 32);
			UInt u4;
			BigInteger b1 = new BigInteger(u1.toString());
			BigInteger b2 = new BigInteger(u2.toString());
			BigInteger b3 = new BigInteger(u3.toString());
			b1 = b1.modPow(b3, b2);
			u4 = u1.createDeepClone();
			p_modular_exponent(u4, u3, u2);
			if(!b1.toString().equals(u4.toString())) {
				er++;
				Log.err.println("p_modular_exponent failed to deliver proper results");
			}
			b1 = new BigInteger(u1.toString());
			b1 = b1.modPow(b3, b2);
			u4 = u1.createDeepClone();
			p_modular_exponent_sure(u4, u3, u2);
			if(!b1.toString().equals(u4.toString())) {
				er++;
				Log.err.println("p_modular_exponent_sure failed to deliver proper results");
			}
			b1 = new BigInteger(u1.toString());
			b1 = b1.modPow(new BigInteger("3432423"), b2);
			u4 = u1.createDeepClone();
			p_modular_exponent(u4, 3432423, u2);
			if(!b1.toString().equals(u4.toString())) {
				er++;
				Log.err.println("p_modular_exponent with int with IInteger failed to deliver proper results");
			}
			return er;
		} catch(Exception e) {
			Log.err.println("Exception encountered while testing exponentiation of integers: " + e.getMessage());
			e.printStackTrace();
			return er + 1;
		}
	}
	
	public static int testModularInverse()
	{
		UInt u1 = new UInt(32);
		UInt u2 = new UInt(32);
		UInt u3;
		while(true)
		{
			RandUtils.fillArr(u1.getArr(), 0, 16);
			RandUtils.fillArr(u2.getArr(), 0, 12);
			if(gcd(u1, u2).toString().equals("1"))
				break;
		}
		u3 = u_modular_inverse(u1, u2);
		u1.p_multiply(u3);
		u1.p_modulo(u2);
		if(!u1.isEqualTo(1)) {
			Log.err.println("Error: modular inverse did not multiply too 1");
			return 1;
		}
		return 0;
	}
	
	public static int test()
	{
		//TODO: Complete tests
		int er = 0;
		er += testExponentiation();
		er += testModularExponentiation();
		er += testModularInverse();
		return er;
	}
	
}

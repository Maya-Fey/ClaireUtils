package claire.util.math.exp;

import java.math.BigInteger;

import claire.util.crypto.rng.RandUtils;
import claire.util.logging.Log;
import claire.util.math.MathHelper;
import claire.util.math.MontgomeryHelper;
import claire.util.math.primitive.UInt;
import claire.util.memory.Bits;
import claire.util.standards.IInteger;

public class SlidingMontgomeryExponentiator
	   implements IExponentiator<UInt> {

	private final MontgomeryHelper mont = new MontgomeryHelper();
	
	private final UInt o;
		
	private final UInt[] ints;
		
	private final int max, 
				      gen;
	
	public SlidingMontgomeryExponentiator(UInt t, int bits)
	{
		this.o = t;
		max = bits;
		ints = t.iFactory().array((int) MathHelper.exponent(2, bits - 1));
		for(int i = 0; i < ints.length; i++)
			ints[i] = t.createDeepClone();
		gen = ints.length - 1;
	}
	
	private void construct(UInt begin, UInt modulus)
	{
		mont.init(modulus);
		ints[0].setTo(begin);
		mont.montgomerize(ints[0]);
		if(max == 2) {
			ints[1].setTo(ints[0]);
			ints[1].p_square();
			mont.montReduce(ints[1]);
			ints[1].p_multiply(ints[0]);
			mont.montReduce(ints[1]);
		} else if(max > 2) {
			o.setTo(ints[0]);
			o.p_square();
			mont.montReduce(o);
			int i = gen;
			int j = 1;
			UInt prev = ints[0];
			while(i-- > 0) {
				ints[j].setTo(prev);
				prev = ints[j++];
				prev.p_multiply(o); 
				mont.montReduce(prev);
			}
		}
	}
	
	/**
	 * Unsupported
	 */
	public void p_exponent(final UInt i, int exponent)
	{
		throw new java.lang.UnsupportedOperationException();
	}
	
	/**
	 * Unsupported
	 */
	public void p_exponent_sure(final UInt i, final IInteger<?> exponent)
	{
		throw new java.lang.UnsupportedOperationException();
	}
	
	/**
	 * This method takes the exponent of an IInteger by a 32-bit integer,
	 * modulo a IInteger.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An UInteger greater then or equal to zero</li>
	 * <li>An IInteger of any value</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns: void, however the result ends up in <code>i</code>.
	 */
	public void p_modular_exponent(final UInt i, int exponent, UInt mod)
	{
		if(exponent == 0) {
			i.setTo(1);
			return;
		} 
		if(i.isGreaterOrEqualTo(mod))
			i.p_modulo(mod);
		if(exponent == 1)
			return;
		construct(i, mod);
		int bmax = Bits.getMSB(exponent);
		int bit = bmax - 1;
		int targ = 1,
			len = 1,
			j = 1;
		while(bit > -1 && j < max) {
			j++;
			if(Bits.getBit(exponent, bit--)) {
				targ <<= j - len;
				targ |= 1;
				len = j;
			}
		}
		targ >>>= 1;
		bit += j - len;
		i.setTo(ints[targ]);
		while(bit > -1)
		{
			if(!Bits.getBit(exponent, bit--)) {
				i.p_square();
				mont.montReduce(i);
			} else {
				targ = 1;
				len = 1;
				j = 1;
				while(bit > -1 && j < max) {
					j++;
					if(Bits.getBit(exponent, bit--)) {
						targ <<= j - len;
						targ |= 1;
						len = j;
					} 
				}
				targ >>>= 1;
				bit += j - len;
				while(len-- > 0) {
					i.p_square();
					mont.montReduce(i);
				}
				i.p_multiply(ints[targ]);
				mont.montReduce(i);
			}
		}
		mont.demontgomerize(i);
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
	public void p_modular_exponent_sure(final UInt i, final IInteger<?> exponent, final UInt mod)
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
		construct(i, mod);
		int bmax = MathHelper.getMSB(exponent.getArr());
		int bit = bmax - 1;
		int targ = 1,
			len = 1,
			j = 1;
		while(bit > -1 && j < max) {
			j++;
			if(exponent.bitAt(bit--)) {
				targ <<= j - len;
				targ |= 1;
				len = j;
			}
		}
		targ >>>= 1;
		bit += j - len;
		i.setTo(ints[targ]);

		while(bit > -1)
		{
			if(!exponent.bitAt(bit--)) {
				i.p_square();
				mont.montReduce(i);
			} else {
				targ = 1;
				len = 1;
				j = 1;
				while(bit > -1 && j < max) {
					j++;
					if(exponent.bitAt(bit--)) {
						targ <<= j - len;
						targ |= 1;
						len = j;
					} 
				}
				targ >>>= 1;
				bit += j - len;
				while(len-- > 0) {
					i.p_square();
					mont.montReduce(i);
				}
				i.p_multiply(ints[targ]);
				mont.montReduce(i);
			}
		}
		mont.demontgomerize(i);
	}
	
	/**
	 * Unsupported
	 */
	public UInt exponent(final UInt n, int exponent)
	{
		throw new java.lang.UnsupportedOperationException();
	}
	
	/**
	 * Unsupported
	 */
	public UInt exponent_sure(final UInt n, final IInteger<?> exponent)
	{
		throw new java.lang.UnsupportedOperationException();
	}
	
	/**
	 * This method takes the exponent of an IInteger by a 32-bit integer,
	 * modulo a IInteger.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An IInteger of any value</li>
	 * <li>An UInteger greater then or equal to zero</li>
	 * <li>An IInteger of any value</li>
	 * </ul>
	 * An negative exponent will result in undefined behavior.
	 * <br><br>
	 * Returns: UInteger of specified type
	 */
	public UInt modular_exponent(final UInt n, int exponent, UInt mod)
	{
		UInt i = n.createDeepClone();
		if(exponent == 0) {
			i.setTo(1);
			return i;
		} 
		if(i.isGreaterOrEqualTo(mod))
			i.p_modulo(mod);
		if(exponent == 1)
			return i;
		construct(n, mod);
		int bmax = Bits.getMSB(exponent);
		int bit = bmax - 1;
		int targ = 1,
			len = 1,
			j = 1;
		while(bit > -1 && j < max) {
			j++;
			if(Bits.getBit(exponent, bit--)) {
				targ <<= j - len;
				targ |= 1;
				len = j;
			}
		}
		targ >>>= 1;
		bit += j - len;
		i.setTo(ints[targ]);
		while(bit > -1)
		{
			if(!Bits.getBit(exponent, bit--)) {
				i.p_square();
				mont.montReduce(i);
			} else {
				targ = 1;
				len = 1;
				j = 1;
				while(bit > -1 && j < max) {
					j++;
					if(Bits.getBit(exponent, bit--)) {
						targ <<= j - len;
						targ |= 1;
						len = j;
					} 
				}
				targ >>>= 1;
				bit += j - len;
				while(len-- > 0) {
					i.p_square();
					mont.montReduce(i);
				}
				i.p_multiply(ints[targ]);
				mont.montReduce(i);
			}
		}
		mont.demontgomerize(i);
		return i;
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
	 * Returns: UInteger of specified type
	 */
	public UInt modular_exponent_sure(final UInt n, final IInteger<?> exponent, final UInt mod)
	{
		UInt i = n.createDeepClone();
		if(!exponent.isNonZero()) {
			i.setTo(1);
			return i;
		}
		if(i.isGreaterOrEqualTo(mod)) {
			i.p_modulo(mod);
		}
		if(exponent.isEqualTo(1))
			return i;
		construct(n, mod);
		int bmax = MathHelper.getMSB(exponent.getArr());
		int bit = bmax - 1;
		int targ = 1,
			len = 1,
			j = 1;
		while(bit > -1 && j < max) {
			j++;
			if(exponent.bitAt(bit--)) {
				targ <<= j - len;
				targ |= 1;
				len = j;
			}
		}
		targ >>>= 1;
		bit += j - len;
		i.setTo(ints[targ]);
		while(bit > -1)
		{
			if(!exponent.bitAt(bit--)) {
				i.p_square();
				mont.montReduce(i);
			} else {
				targ = 1;
				len = 1;
				j = 1;
				while(bit > -1 && j < max) {
					j++;
					if(exponent.bitAt(bit--)) {
						targ <<= j - len;
						targ |= 1;
						len = j;
					} 
				}
				targ >>>= 1;
				bit += j - len;
				while(len-- > 0) {
					i.p_square();
					mont.montReduce(i);
				}
				i.p_multiply(ints[targ]);
				mont.montReduce(i);
			}
		}
		mont.demontgomerize(i);
		return i;
	}
	
	public static int testModularExponentiation()
	{
		int er = 0;
		try {
			UInt u1 = MathHelper.randomInteger(UInt.ifactory, 33, RandUtils.dprng, 16 * 32 - 1);
			UInt u2 = MathHelper.randomInteger(UInt.ifactory, 33, RandUtils.dprng, 16 * 32);
			UInt u3 = MathHelper.randomInteger(UInt.ifactory, 33, RandUtils.dprng, 16 * 32);
			if(!u1.isOdd())
				u1.increment();
			if(!u2.isOdd())
				u2.increment();
			if(!u3.isOdd())
				u3.increment();
			SlidingMontgomeryExponentiator d = new SlidingMontgomeryExponentiator(u1.createDeepClone(), 4);
			UInt u4;
			BigInteger b1 = new BigInteger(u1.toString());
			BigInteger b2 = new BigInteger(u2.toString());
			BigInteger b3 = new BigInteger(u3.toString());
			b1 = b1.modPow(b3, b2);
			u4 = u1.createDeepClone();
			d.p_modular_exponent(u4, u3, u2);
			if(!b1.toString().equals(u4.toString())) {
				er++;
				Log.err.println("p_modular_exponent failed to deliver proper results");
			}
			b1 = new BigInteger(u1.toString());
			b1 = b1.modPow(b3, b2);
			u4 = u1.createDeepClone();
			d.p_modular_exponent_sure(u4, u3, u2);
			if(!b1.toString().equals(u4.toString())) {
				er++;
				Log.err.println("p_modular_exponent_sure failed to deliver proper results");
			}
			b1 = new BigInteger(u1.toString());
			b1 = b1.modPow(new BigInteger("3432423"), b2);
			u4 = u1.createDeepClone();
			d.p_modular_exponent(u4, 3432423, u2);
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
	
	public static int testReturn()
	{
		int er = 0;
		try {
			UInt u1 = MathHelper.randomInteger(UInt.ifactory, 33, RandUtils.dprng, 16 * 32 - 1);
			SlidingMontgomeryExponentiator d = new SlidingMontgomeryExponentiator(u1.createDeepClone(), 4);
			UInt u2 = MathHelper.randomInteger(UInt.ifactory, 33, RandUtils.dprng, 16 * 32);
			UInt u3 = MathHelper.randomInteger(UInt.ifactory, 33, RandUtils.dprng, 16 * 32);
			if(!u1.isOdd())
				u1.increment();
			if(!u2.isOdd())
				u2.increment();
			if(!u3.isOdd())
				u3.increment();
			UInt u4 = u1.createDeepClone();
			UInt u5 = d.modular_exponent(u4, u3, u2);
			d.p_modular_exponent(u4, u3, u2);
			if(!u4.isEqualTo(u5)) {
				er++;
				Log.err.println("p_modular_exponent failed to deliver consistent results");
			}
			u4 = u1.createDeepClone();
			u5 = d.modular_exponent_sure(u4, u3, u2);
			d.p_modular_exponent_sure(u4, u3, u2);
			if(!u4.isEqualTo(u5)) {
				er++;
				Log.err.println("p_modular_exponent_sure failed to deliver consistent results");
			}
			u4 = u1.createDeepClone();
			u5 = d.modular_exponent(u4, 3432423, u2);
			d.p_modular_exponent(u4, 3432423, u2);
			if(!u4.isEqualTo(u5)) {
				er++;
				Log.err.println("p_modular_exponent with int with IInteger failed to deliver consistent results");
			}
			return er;
		} catch(Exception e) {
			Log.err.println("Exception encountered while testing exponentiation of integers: " + e.getMessage());
			e.printStackTrace();
			return er + 1;
		}
	}
	
	public static int test()
	{
		int er = 0;
		er += testModularExponentiation();
		er += testReturn();
		return er;
	}
	
	public static int getOptimalMax(int bits)
	{
		if(bits <= 128) 
			return 4;
		else if(bits <= 600)
			return 5;
		else if(bits <= 1600)
			return 6;
		else if(bits <= 5000)
			return 7;
		else
			return 8;
	}
	
}

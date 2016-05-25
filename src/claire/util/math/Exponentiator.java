package claire.util.math;

import java.math.BigInteger;

import claire.util.crypto.rng.RandUtils;
import claire.util.logging.Log;
import claire.util.memory.Bits;
import claire.util.standards.IInteger;

public class Exponentiator<Int extends IInteger<Int>> {

	private final Int o;
	
	public Exponentiator(Int t)
	{
		this.o = t;
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
	public void p_exponent(final Int i, int exponent)
	{
		if(exponent == 0) {
			i.setTo(1);
			return;
		} 
		if(exponent == 1)
			return;
		o.setTo(i);
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
	public void p_exponent(final Int i, final IInteger<?> exponent)
	{
		if(MathHelper.getRealLength(exponent.getArr()) == 1)
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
	public void p_exponent_sure(final Int i, final IInteger<?> exponent)
	{
		if(!exponent.isNonZero()) {
			i.setTo(1);
			return;
		}
		if(exponent.isEqualTo(1))
			return;
		o.setTo(i);
		final int max = MathHelper.getMSB(exponent.getArr());
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
	public void p_modular_exponent(final Int i, final Int exponent, final Int mod)
	{
		if(MathHelper.getRealLength(exponent.getArr()) == 1)
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
	public void p_modular_exponent(final Int i, long exponent, Int mod)
	{
		if(exponent == 0) {
			i.setTo(1);
			return;
		} 
		if(i.isGreaterOrEqualTo(mod))
			i.p_modulo(mod);
		if(exponent == 1)
			return;
		o.setTo(i);
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
	public void p_modular_exponent_sure(final Int i, final Int exponent, final Int mod)
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
		o.setTo(i);
		final int max = MathHelper.getMSB(exponent.getArr());
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
	 * Returns: Integer of specified type
	 */
	public Int exponent(final Int n, int exponent)
	{
		Int i = n.createDeepClone();
		if(exponent == 0) {
			i.setTo(1);
			return i;
		} 
		if(exponent == 1)
			return i;
		o.setTo(i);
		int max = Bits.getMSB(exponent);
		int bit = max - 1;
		while(bit > -1)
		{
			i.p_square();
			if(Bits.getBit(exponent, bit--)) 
				i.p_multiply(o);
		}
		return i;
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
	 * Returns: Integer of specified type
	 */
	public Int exponent(final Int i, final IInteger<?> exponent)
	{
		if(MathHelper.getRealLength(exponent.getArr()) == 1)
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
	 * Returns: Integer of specified type
	 */
	public Int exponent_sure(final Int n, final IInteger<?> exponent)
	{
		Int i = n.createDeepClone();
		if(!exponent.isNonZero()) {
			i.setTo(1);
			return i;
		}
		if(exponent.isEqualTo(1))
			return i;
		final int max = MathHelper.getMSB(exponent.getArr());
		int bit = 0;
		o.setTo(1);
		while(bit < max)
		{
			if(exponent.bitAt(bit++)) 
				o.p_multiply(i);
			i.p_square();			
		}
		i.p_multiply(o);
		return i;
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
	 * Returns: Integer of specified type
	 */
	public Int modular_exponent(final Int i, final Int exponent, final Int mod)
	{
		if(MathHelper.getRealLength(exponent.getArr()) == 1)
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
	 * Returns: Integer of specified type
	 */
	public Int modular_exponent(final Int n, long exponent, Int mod)
	{
		Int i = n.createDeepClone();
		if(exponent == 0) {
			i.setTo(1);
			return i;
		} 
		if(i.isGreaterOrEqualTo(mod))
			i.p_modulo(mod);
		if(exponent == 1)
			return i;
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
	 * Returns: Integer of specified type
	 */
	public Int modular_exponent_sure(final Int n, final Int exponent, final Int mod)
	{
		Int i = n.createDeepClone();
		if(!exponent.isNonZero()) {
			i.setTo(1);
			return i;
		}
		if(i.isGreaterOrEqualTo(mod)) {
			i.p_modulo(mod);
		}
		if(exponent.isEqualTo(1))
			return i;
		final int max = MathHelper.getMSB(exponent.getArr());
		int bit = 0;
		o.setTo(1);
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
		return i;
	}
	
	public static int testExponentiation()
	{
		int er = 0;
		try {
			int exp = RandUtils.inRange(50, 600);
			UInt u = new UInt("7", 128);
			Exponentiator<UInt> d = new Exponentiator<UInt>(u.createDeepClone());
			UInt u2 = u.createDeepClone();
			UInt e = new UInt(Integer.toString(exp), 8);
			BigInteger b = new BigInteger("7");
			b = b.pow(exp);
			d.p_exponent(u2, exp);
			if(!b.toString().equals(u2.toString())) {
				er++;
				Log.err.println("p_exponent failed to deliver proper results");
			}
			u2 = u.createDeepClone();
			d.p_exponent(u2, e);
			if(!b.toString().equals(u2.toString())) {
				er++;
				Log.err.println("p_exponent with IInteger failed to deliver proper results");
			}
			u2 = u.createDeepClone();
			d.p_exponent_sure(u2, e);
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
			Exponentiator<UInt> d = new Exponentiator<UInt>(u1.createDeepClone());
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
			int exp = RandUtils.inRange(50, 600);
			UInt u1 = new UInt("7", 128);
			Exponentiator<UInt> d = new Exponentiator<UInt>(u1.createDeepClone());
			UInt u2 = u1.createDeepClone();
			UInt e = new UInt(Integer.toString(exp), 8);
			UInt u3 = d.exponent(u2, exp);
			d.p_exponent(u2, exp);
			if(!u2.isEqualTo(u3)) {
				er++;
				Log.err.println("p_exponent failed to deliver consistent results");
			}
			u2 = u1.createDeepClone();
			u3 = d.exponent(u2, e);
			d.p_exponent(u2, e);
			if(!u2.isEqualTo(u3)) {
				er++;
				Log.err.println("p_exponent with IInteger failed to deliver consistent results");
			}
			u2 = u1.createDeepClone();
			u3 = d.exponent(u2, e);
			d.p_exponent_sure(u2, e);
			if(!u2.isEqualTo(u3)) {
				er++;
				Log.err.println("p_exponent_sure with IInteger failed to deliver consistent results");
			}
			
			u1 = MathHelper.randomInteger(UInt.ifactory, 32, RandUtils.dprng, 16 * 32 - 1);
			u2 = MathHelper.randomInteger(UInt.ifactory, 32, RandUtils.dprng, 16 * 32);
			u3 = MathHelper.randomInteger(UInt.ifactory, 32, RandUtils.dprng, 16 * 32);
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
				Log.err.println("p_modular_exponent with int with IInteger failed to deliver proper results");
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
		er += testExponentiation();
		er += testModularExponentiation();
		er += testReturn();
		return er;
	}
	
}

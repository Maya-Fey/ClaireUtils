package claire.util.math;

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
	}
	
}
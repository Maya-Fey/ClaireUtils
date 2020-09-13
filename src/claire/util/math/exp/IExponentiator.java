package claire.util.math.exp;

import claire.util.math.MathHelper;
import claire.util.standards.IInteger;

public interface IExponentiator<Int extends IInteger<Int>>
{
	void p_exponent(Int i, int exp);
	void p_exponent_sure(Int i, IInteger<?> exp);
	
	void p_modular_exponent(Int i, int exp, Int mod);
	void p_modular_exponent_sure(Int i, IInteger<?> exp, Int Mod);
	
	Int exponent(Int i, int exp);
	Int exponent_sure(Int i, IInteger<?> exp);
	
	Int modular_exponent(Int i, int exp, Int mod);
	Int modular_exponent_sure(Int i, IInteger<?> exp, Int Mod);
	
	default void p_exponent(Int i, IInteger<?> exp)
	{
		if(MathHelper.getRealLength(exp.getArr()) == 1)
			p_exponent(i, exp.getArr()[0]);
		else
			p_exponent_sure(i, exp);
	}
	
	default void p_modular_exponent(Int i, IInteger<?> exp, Int mod)
	{
		if(MathHelper.getRealLength(exp.getArr()) == 1)
			p_modular_exponent(i, exp.getArr()[0], mod);
		else
			p_modular_exponent_sure(i, exp, mod);
	}
	
	default Int exponent(Int i, IInteger<?> exp)
	{
		if(MathHelper.getRealLength(exp.getArr()) == 1)
			return exponent(i, exp.getArr()[0]);
		else
			return exponent_sure(i, exp);
	}
	
	default Int modular_exponent(Int i, IInteger<?> exp, Int mod)
	{
		if(MathHelper.getRealLength(exp.getArr()) == 1)
			return modular_exponent(i, exp.getArr()[0], mod);
		else
			return modular_exponent_sure(i, exp, mod);
	}
	
	
}

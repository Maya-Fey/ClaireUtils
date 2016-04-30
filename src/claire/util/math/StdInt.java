package claire.util.math;

import claire.util.encoding.CString;
import claire.util.memory.Bits;
import claire.util.standards.IInteger;

public abstract class StdInt<Type extends StdInt<Type>> 
				implements IInteger<Type> {
	
	protected StdInt()
	{
		this.create();
	}
	
	protected StdInt(CString s)
	{
		this.create();
		IInteger.make(this, s.array());
	}
	
	protected StdInt(String s)
	{
		this.create();
		IInteger.make(this, s.toCharArray());
	}
	
	protected abstract void create();
	
	protected abstract void p_add(int[] ints);
	protected abstract void p_subtract(int[] ints);
	protected abstract void p_multiply(int[] ints);
	protected abstract void p_divide(int[] ints);
	protected abstract void p_modulo(int[] ints);
	
	public boolean isGreaterThan(int i)
	{		
		return MathHelper.absolute_compare(this.getArr(), i) == 1;
	}
	
	public boolean isLesserThan(int i)
	{		
		return MathHelper.absolute_compare(this.getArr(), i) == -1;
	}
	
	public boolean isEqualTo(int i)
	{		
		return MathHelper.absolute_compare(this.getArr(), i) == 0;
	}
	
	public boolean doesNotEqual(int i)
	{		
		return MathHelper.absolute_compare(this.getArr(), i) != 0;
	}
	
	public boolean isGreaterOrEqualTo(int i)
	{		
		return MathHelper.absolute_compare(this.getArr(), i) >= 0;
	}
	
	public boolean isLesserOrEqualTo(int i)
	{
		return MathHelper.absolute_compare(this.getArr(), i) <= 0;
	}
	
	public int getBits()
	{
		return MathHelper.getMSB(this.getArr()) + 1;
	}
	
	public void setBit(int pos, boolean bit)
	{
		Bits.setBit(this.getArr(), pos, bit);
	}
	
	public String toString()
	{
		return new String(this.toChars());
	}

}

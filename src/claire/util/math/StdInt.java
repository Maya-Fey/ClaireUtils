package claire.util.math;

import claire.util.encoding.CString;
import claire.util.memory.Bits;
import claire.util.standards.IInteger;

public abstract class StdInt<Type extends StdInt<Type>> 
				implements IInteger<Type> {
	
	protected final int[] split = new int[2];
	
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
	
	public boolean isGreaterThan(long i)
	{
		Bits.splitLong(i, split, 0);
		return MathHelper.absolute_compare(this.getArr(), split) == 1;
	}
	
	public boolean isLesserThan(long i)
	{
		Bits.splitLong(i, split, 0);
		return MathHelper.absolute_compare(this.getArr(), split) == -1;
	}
	
	public boolean isEqualTo(long i)
	{
		Bits.splitLong(i, split, 0);
		return MathHelper.absolute_compare(this.getArr(), split) == 0;
	}
	
	public boolean doesNotEqual(long i)
	{
		Bits.splitLong(i, split, 0);
		return MathHelper.absolute_compare(this.getArr(), split) != 0;
	}
	
	public boolean isGreaterOrEqualTo(long i)
	{
		Bits.splitLong(i, split, 0);
		return MathHelper.absolute_compare(this.getArr(), split) >= 0;
	}
	
	public boolean isLesserOrEqualTo(long i)
	{
		Bits.splitLong(i, split, 0);
		return MathHelper.absolute_compare(this.getArr(), split) <= 0;
	}
	
	public int getBits()
	{
		return MathHelper.getMSB(this.getArr());
	}
	
	public String toString()
	{
		return new String(this.toChars());
	}

}

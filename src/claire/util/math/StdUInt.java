package claire.util.math;

import claire.util.encoding.CString;
import claire.util.memory.Bits;
import claire.util.standards.IInteger;

public abstract class StdUInt<Type extends StdUInt<Type>> 
				extends StdInt<Type> {
	
	protected final int[] split = new int[2];
	
	protected StdUInt()
	{
		super();
	}
	
	protected StdUInt(CString s)
	{
		super(s);
	}
	
	protected StdUInt(String s)
	{
		super(s);
	}
	
	public void p_add(IInteger<?> i)
	{
		this.p_add(i.getArr());
	}
	
	public void p_subtract(IInteger<?> i)
	{
		this.p_subtract(i.getArr());
	}

	public void p_multiply(IInteger<?> i)
	{
		this.p_multiply(i.getArr());
	}
	
	public void p_divide(final IInteger<?> i)
	{
		this.p_divide(i.getArr());
	}

	public void p_modulo(IInteger<?> i)
	{
		this.p_modulo(i.getArr());
	}

	public void p_add(long i)
	{
		Bits.splitLong(i, split, 0);
		this.p_add(split);
	}

	public void p_subtract(long i)
	{
		Bits.splitLong(i, split, 0);
		this.p_subtract(split);
	}

	public void p_multiply(long i)
	{
		Bits.splitLong(i, split, 0);
		this.p_multiply(split);
	}

	public void p_divide(long i)
	{
		Bits.splitLong(i, split, 0);
		this.p_divide(split);
	}

	public void p_modulo(long i)
	{
		Bits.splitLong(i, split, 0);
		this.p_modulo(split);
	}
	
	public void setTo(IInteger<?> other)
	{
		this.setArr(other.getArr());
	}
	
	public void setNegative(boolean b) {}
	public void invertSign() {}
	public boolean isNegative() { return false; }
	public boolean isSigned() { return false; }

}

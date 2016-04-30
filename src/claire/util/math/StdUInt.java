package claire.util.math;

import claire.util.encoding.CString;
import claire.util.standards.IInteger;

public abstract class StdUInt<Type extends StdUInt<Type>> 
				extends StdInt<Type> {

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
	
	public void setTo(IInteger<?> other)
	{
		this.setArr(other.getArr());
	}
	
	public void setNegative(boolean b) {}
	public void invertSign() {}
	public boolean isNegative() { return false; }
	public boolean isSigned() { return false; }

}

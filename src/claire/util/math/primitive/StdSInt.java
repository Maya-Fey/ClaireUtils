package claire.util.math.primitive;

import claire.util.encoding.CString;
import claire.util.math.MathHelper;
import claire.util.standards.IInteger;

public abstract class StdSInt<Type extends StdSInt<Type>> 
				extends StdInt<Type> {
	
	protected int[] temp;
	
	protected StdSInt()
	{
		super();
	}
	
	protected StdSInt(CString s)
	{
		super(s);
	}
	
	protected StdSInt(String s)
	{
		super(s);
	}
	
	protected abstract void ip_add(int i);
	protected abstract void ip_subtract(int i);
	protected abstract void ip_multiply(int i);
	protected abstract void ip_divide(int i);
	protected abstract void ip_modulo(int i);
	protected abstract  int ip_divmod(int i);
	
	public void p_add(IInteger<?> i)
	{
		if(this.isNegative() == i.isNegative())
			this.p_add(i.getArr(), i.realLen());
		else 
			p_subtract_known(i.getArr(), super.isLesserOrEqualTo(i), i.realLen());
	}
	
	private void p_subtract_known(int i, boolean greater)
	{
		if(greater) {
			if(temp == null || temp.length < this.getIntLen())
				temp = new int[this.getIntLen()];
			System.arraycopy(this.getArr(), 0, temp, 0, this.getIntLen());
			this.setTo(i);
			this.p_subtract(temp, 1);
			this.invertSign();
		} else 
			this.ip_subtract(i);
	}
	
	private void p_subtract_known(int[] arr, boolean greater, int len)
	{
		if(greater) {
			if(temp == null || temp.length < this.getIntLen())
				temp = new int[this.getIntLen()];
			System.arraycopy(this.getArr(), 0, temp, 0, this.getIntLen());
			this.setArr(arr);
			this.p_subtract(temp, MathHelper.getRealLength(temp));
			this.invertSign();
		} else 
			this.p_subtract(arr, len);
	}
	
	public void p_subtract(IInteger<?> i)
	{
		if(this.isNegative() == i.isNegative())
			p_subtract_known(i.getArr(), super.isLesserOrEqualTo(i), i.realLen());
		else 
			p_add(i.getArr(), i.realLen());
	}

	public void p_multiply(IInteger<?> i)
	{
		boolean thisn = this.isNegative();
		boolean in = i.isNegative();
		if(thisn ^ in)
			if(!i.isNonZero()) {
				this.zero();
				return;
			}
		if(thisn && in)
			this.invertSign();
		if(!thisn && in)
			this.invertSign();
		this.p_multiply(i.getArr(), i.realLen());
		
	}
	
	public void p_divide(final IInteger<?> i)
	{
		boolean thisn = this.isNegative();
		boolean in = i.isNegative();
		if(thisn ^ in)
			if(!i.isNonZero()) {
				this.zero();
				return;
			}
		if(in)
			this.invertSign();
		this.p_divide(i.getArr(), i.realLen());
	}

	public void p_modulo(IInteger<?> i)
	{
		boolean thisn = this.isNegative();
		boolean in = i.isNegative();
		if(thisn ^ in)
			if(!i.isNonZero()) {
				this.zero();
				return;
			}
		if(in)
			this.invertSign();
		this.p_modulo(i.getArr(), i.realLen());
	}

	public void p_add(int i)
	{
		boolean negative = i < 0;
		if(negative) 
			i = ~i + 1;
		if(this.isNegative() == negative)
			this.ip_add(i);
		else
			p_subtract_known(i, super.isLesserOrEqualTo(i));
	}

	public void p_subtract(int i)
	{
		boolean negative = i < 0;
		if(negative) 
			i = ~i + 1;
		if(this.isNegative() == negative)
			p_subtract_known(i, super.isLesserOrEqualTo(i));
		else
			ip_add(i);
		
	}

	public void p_multiply(int i)
	{
		boolean negative = i < 0;
		if(negative) 
			i = ~i + 1;
		boolean thisn = this.isNegative();
		if(thisn ^ negative)
			if(i == 0) {
				this.zero();
				return;
			}
		if(negative)
			this.invertSign();
		this.ip_multiply(i);
	}

	public void p_divide(int i)
	{
		boolean negative = i < 0;
		if(negative) 
			i = ~i + 1;
		if(i == 0) {
			this.zero();
			return;
		}
		if(negative)
			this.invertSign();
		this.ip_divide(i);
	}
	
	public void p_modulo(int i)
	{
		boolean negative = i < 0;
		if(negative) 
			i = ~i + 1;
		if(i == 0) {
			this.zero();
			return;
		}
		if(negative)
			this.invertSign();
		this.ip_modulo(i);
	}
	
	public int p_divmod(int i)
	{
		boolean negative = i < 0;
		if(negative) 
			i = ~i + 1;
		if(i == 0) {
			this.zero();
			return 0;
		}
		if(negative)
			this.invertSign();
		return this.ip_divmod(i);
	}
	
	public boolean isGreaterThan(IInteger<?> i)
	{
		boolean neg = this.isNegative();
		if(neg == i.isNegative())
			if(neg)
				return super.isLesserThan(i);
			else
				return super.isGreaterThan(i);
		else if(!neg)
			return true;
		else
			return false;
	}

	public boolean isLesserThan(IInteger<?> i)
	{
		boolean neg = this.isNegative();
		if(neg == i.isNegative())
			if(neg)
				return super.isGreaterThan(i);
			else
				return super.isLesserThan(i);
		else if(neg)
			return true;
		else
			return false;
	}

	public boolean isEqualTo(IInteger<?> i)
	{
		if(this.isNegative() == i.isNegative())
			if(super.isEqualTo(i))
				return true;
		return false;
	}

	public boolean doesNotEqual(IInteger<?> i)
	{
		return !isEqualTo(i);
	}

	public boolean isGreaterOrEqualTo(IInteger<?> i)
	{
		boolean neg = this.isNegative();
		if(neg == i.isNegative())
			if(neg)
				return super.isLesserOrEqualTo(i);
			else
				return super.isGreaterOrEqualTo(i);
		else if(!neg)
			return true;
		else
			return false;
	}

	public boolean isLesserOrEqualTo(IInteger<?> i)
	{
		boolean neg = this.isNegative();
		if(neg == i.isNegative())
			if(neg)
				return super.isGreaterOrEqualTo(i);
			else
				return super.isLesserOrEqualTo(i);
		else if(neg)
			return true;
		else
			return false;
	}

	public boolean isGreaterThan(int i)
	{
		boolean neg = this.isNegative();
		if(neg == i < 0)
			if(neg)
				return super.isLesserThan(i);
			else
				return super.isGreaterThan(i);
		else if(!neg)
			return true;
		else
			return false;
	}

	public boolean isLesserThan(int i)
	{
		boolean neg = this.isNegative();
		if(neg == i < 0)
			if(neg)
				return super.isGreaterThan(i);
			else
				return super.isLesserThan(i);
		else if(neg)
			return true;
		else
			return false;
	}

	public boolean isEqualTo(int i)
	{
		if(this.isNegative() == i < 0)
			if(super.isEqualTo(i))
				return true;
		return false;
	}

	public boolean doesNotEqual(int i)
	{
		return !isEqualTo(i);
	}

	public boolean isGreaterOrEqualTo(int i)
	{
		boolean neg = this.isNegative();
		if(neg == i < 0)
			if(neg)
				return super.isLesserOrEqualTo(i);
			else
				return super.isGreaterOrEqualTo(i);
		else if(!neg)
			return true;
		else
			return false;
	}

	public boolean isLesserOrEqualTo(int i)
	{
		boolean neg = this.isNegative();
		if(neg == i < 0)
			if(neg)
				return super.isGreaterOrEqualTo(i);
			else
				return super.isLesserOrEqualTo(i);
		else if(neg)
			return true;
		else
			return false;
	}
	
	public boolean isSigned()
	{
		return true;
	}

}

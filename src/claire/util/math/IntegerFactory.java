package claire.util.math;

import claire.util.standards.IInteger;

public abstract class IntegerFactory<Int extends IInteger<Int>> {
	
	public abstract int construct(int len);

}

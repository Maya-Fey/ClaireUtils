package claire.util.math;

import claire.util.standards.IInteger;

public abstract class IntegerFactory<Int extends IInteger<?>> {
	
	public abstract Int construct(int len);
	public abstract Int construct(int[] ints);

}

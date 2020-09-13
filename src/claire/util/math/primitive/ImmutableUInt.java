package claire.util.math.primitive;

import claire.util.math.MathHelper;
import claire.util.memory.util.ArrayUtil;

public class ImmutableUInt 
	   extends UInt {

	private final int len;
	
	private ImmutableUInt(int[] ints)
	{
		super(ints);
		len = MathHelper.getRealLength(ints);
	}
	
	public ImmutableUInt(UInt u)
	{
		this(ArrayUtil.copy(u.getArr()));
	}
	
	public ImmutableUInt(UInt u, boolean copy)
	{
		this(copy ? ArrayUtil.copy(u.getArr()) : u.getArr());
	}
	
	public int realLen()
	{
		return len;
	}

}

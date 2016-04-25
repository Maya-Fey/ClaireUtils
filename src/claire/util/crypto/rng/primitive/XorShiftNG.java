package claire.util.crypto.rng.primitive;

public class XorShiftNG 
	   extends XorShiftRNG {
	
	public XorShiftNG() 
	{
    	super();
	}
	
	public XorShiftNG(boolean b)
	{
		super(b);
	}
	
	public XorShiftNG(LongSeed seed)
	{
		super(seed);
	}

	public XorShiftNG(long l)
	{
		super(l);
	}
	
	protected void update()
	{
		this.last ^= (this.last << 21);
	    this.last ^= (this.last >>> 35);
	    this.last ^= (this.last << 4);
	}

}

package claire.util.crypto.rng.primitive;

public class FastXorShift 
	   extends XorShiftRNG {
	
	public FastXorShift() 
	{
    	super();
	}
	
	public FastXorShift(boolean b)
	{
		super(b);
	}
	
	public FastXorShift(LongSeed seed)
	{
		super(seed);
	}

	public FastXorShift(long l)
	{
		super(l);
	}
	
	protected void update()
	{
		/*this.last ^= (this.last << 22);
		this.last ^= (this.last >> 19);*/
		this.last ^= (this.last >> 25);
		this.last ^= (this.last << 24);
	}

}

package claire.util.crypto.rng.primitive;

public class FastXorShift extends XorShiftRNG {
	
	public FastXorShift()
	{
		super();
	}
	
	public FastXorShift(long seed)
	{
		super(seed);
	}
	
	protected void update()
	{
		/*this.last ^= (this.last << 22);
		this.last ^= (this.last >> 19);*/
		this.last ^= (this.last >> 25);
		this.last ^= (this.last << 24);
	}

}

package claire.util.crypto.rng.primitive;

public class XorShiftNG extends XorShiftRNG {
	
	public XorShiftNG(int seed) {
		super(seed);
	}
	
	public XorShiftNG() {
		
	}

	protected void update()
	{
		this.last ^= (this.last << 21);
	    this.last ^= (this.last >>> 35);
	    this.last ^= (this.last << 4);
	}

}

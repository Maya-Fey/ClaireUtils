package claire.util.standards;

public interface IRandom {
	
	/*
	 * Integer arithmetic
	 */
	public boolean nextBoolean();
	public byte nextByte();
	public short nextShort();
	public int nextInt();
	public long nextLong();
	
	default int nextIntFast(int max)
	{
		return (this.nextInt() >>> 1) % max;
	}
	
	default int nextIntGood(int max)
	{
		int bits, val;
	    do {
	    	bits = (this.nextInt() >>> 1);
	    	val = bits % max;
        } while(bits - val + (max - 1) < 0);
	    return val;
	}	

}

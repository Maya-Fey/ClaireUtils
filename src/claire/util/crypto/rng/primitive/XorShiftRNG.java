package claire.util.crypto.rng.primitive;

import claire.util.standards.crypto.IRandom;

public abstract class XorShiftRNG implements IRandom {

	protected long last;

	public XorShiftRNG() {
    	this((System.currentTimeMillis() ^ (System.nanoTime() << 12)));
	}
	
	protected abstract void update();
	
	public XorShiftRNG(@SuppressWarnings("unused") boolean b)
	{
		this.last = 0;
	}

	public XorShiftRNG(long seed) {
    	this.last = seed;
    	update();
	}
	
	public XorShiftRNG(int seed) {
    	this.last = seed + (((long) ~seed) << 32);
    	update();
	}
	
	public void setSeed(long seed)
	{
		this.last = seed;
	}
	
	public int nextInt() {
		update();
    	return (int) this.last;
	}
	
	public long nextLong() {
		update();
    	return this.last;
	}

	public int nextInt(int max) {
		update();
    	int out = (int) this.last % max;     
    	return (out < 0) ? -out : out;
	}

	public float nextFloat() {
		update();
    	int out = (int)this.last % 100000000;
    	if(out < 0) { out *= -1; }
    	return (float)out / 100000000;
	}
	
	public double nextDouble() {
		update();
    	long out = (long)this.last % (long)((long)100000000 * (long)1000000000);
    	if(out < 0L) { out *= -1; }
    	return (double)out / (long)((long)100000000 * (long)1000000000);
	}
	
	public boolean nextBoolean()
	{
		update();
    	return (this.last & 1) > 0;
	}

	public byte nextByte()
	{
		update();
		return (byte) this.last;
	}

	public short nextShort()
	{
		update();
		return (short) this.last;
	}

}
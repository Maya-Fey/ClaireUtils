package claire.util.concurrency;

import claire.util.standards.IDeepClonable;
import claire.util.standards.IUUID;
import claire.util.standards._NAMESPACE;

/**
 * Used by threads that need to do a precise amount of instructions every interval. Automatically
 * catches up if ticks take to long, and waits if ticks were completed ahead of schedule.
 * 
 * @author Claire
 */
public class Regulator 
	   implements IUUID<Regulator>, IDeepClonable<Regulator> {
	
	private final long tickTime;
	
	private long last;
	private long behind;
	
	/**
	 * Specify the amount of ticks per second
	 */
	public Regulator(int ticksPerSec)
	{
		this.tickTime = (Timer.SECOND) / ticksPerSec;
	}
	
	public Regulator(int ticksPerSec, int interval)
	{
		this.tickTime = (interval) / ticksPerSec;
	}
	
	Regulator(long i, @SuppressWarnings("unused") boolean b) {
		this.tickTime = i;
	}
	
	public final void start()
	{
		this.last = System.nanoTime();
	}
	
	/**
	 * Signal that the thread has completed another iteration. Thread will wait if it is
	 * ahead, or no if it is behind. Keeps track of total time behind.
	 * 
	 * @throws InterruptedException
	 */
	public final void end() throws InterruptedException
	{
		long tt = System.nanoTime() - this.last;
		if(tt < this.tickTime) {
			long delta = this.tickTime - tt;
			if(this.behind > 0)
				if(this.behind > delta)
					this.behind -= delta;
				else {
					this.wait(delta - behind);
					this.behind = 0;
				}
			else
				this.wait(delta);
		} else
			this.behind += tt - this.tickTime;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.REGULATOR;
	}

	public Regulator createDeepClone()
	{
		return new Regulator(this.tickTime, false);
	}

	public boolean sameAs(Regulator obj)
	{
		return this.tickTime == obj.tickTime;
	}

}

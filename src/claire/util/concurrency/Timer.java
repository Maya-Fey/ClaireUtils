package claire.util.concurrency;

import java.io.IOException;

import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.standards.CObject;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public final class Timer 
			 implements CObject<Timer> {
	
	public static final long HOUR = 1000 * 1000 * 1000 * 60 * 60;
	public static final long MINUTE = 1000 * 1000 * 1000 * 60;
	public static final long SECOND = 1000 * 1000 * 1000;
	public static final long MILLISECOND = 1000 * 1000;
	public static final long MICROSECOND = 1000;
	
	public static long microseconds(long nano)
	{
		return nano / MICROSECOND;
	}
	
	public static long milliseconds(long nano)
	{
		return nano / MILLISECOND;
	}
	
	public static long seconds(long nano)
	{
		return nano / SECOND;
	}
	
	public static long minutes(long nano)
	{
		return nano / MINUTE;
	}
	
	public static long hours(long nano)
	{
		return nano / HOUR;
	}
	
	private long time;
	
	public Timer()
	{
		reset();
	}
	
	private Timer(long time)
	{
		this.time = time;
	}
	
	public void reset()
	{
		time = System.nanoTime();
	}
	
	public void adjust(long time)
	{
		this.time += time;
	}
	
	public long microseconds()
	{
		return (System.nanoTime() - time) / MICROSECOND;
	}
	
	public long milliseconds()
	{
		return (System.nanoTime() - time) / MILLISECOND;
	}
	
	public long seconds()
	{
		return (System.nanoTime() - time) / SECOND;
	}
	
	public long minutes()
	{
		return (System.nanoTime() - time) / MINUTE;
	}
	
	public long hours()
	{
		return (System.nanoTime() - time) / HOUR;
	}

	public Timer createDeepClone()
	{
		return new Timer(time);
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.TIMER;
	}

	public boolean sameAs(Timer obj)
	{
		return obj.time == this.time;
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeLong(time);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.longToBytes(time, bytes, offset);
	}

	public int exportSize()
	{
		return 8;
	}
	
	public static final Factory<Timer> factory = new TimerFactory();

	public Factory<Timer> factory()
	{
		return factory;
	}

	private static final class TimerFactory 
						 extends Factory<Timer> {

		protected TimerFactory() 
		{
			super(Timer.class);
		}

		public Timer resurrect(byte[] data, int start) throws InstantiationException
		{
			return new Timer(Bits.longFromBytes(data, start));
		}

		public Timer resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new Timer(stream.readLong());
		}
		
		
	}
	
}

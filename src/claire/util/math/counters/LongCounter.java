package claire.util.math.counters;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.io.IOUtils;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.CObject;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class LongCounter
	   implements CObject<LongCounter> {
	
	private final long[] longs;
	
	public LongCounter(int size)
	{
		longs = new long[size];
	}
	
	public LongCounter(long[] longs)
	{
		this.longs = longs;
	}
	
	public void add(long add)
	{
		longs[0] += add;
		int pos = 0;
		while(Bits.u_greaterThan(add, longs[pos++]))
		{
			longs[pos]++;
			add = 1;
		}
	}
	
	public void remove(long rem)
	{
		longs[0] -= rem;
		int pos = 0;
		while(Bits.u_greaterThan(longs[pos++], rem))
		{
			longs[pos]--;
			rem = 1;
		}
	}
	
	public long[] getLongs()
	{
		return this.longs;
	}
	
	public void reset()
	{
		Arrays.fill(longs, 0);
	}
	
	public LongCounter createDeepClone()
	{
		return new LongCounter(ArrayUtil.copy(longs));
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.INTCOUNTER;
	}

	public boolean sameAs(LongCounter obj)
	{
		return ArrayUtil.equals(this.longs, obj.longs);
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeLongArr(longs);
	}
	
	public void export(byte[] bytes, int offset)
	{
		IOUtils.writeArr(longs, bytes, offset);
	}

	public int exportSize()
	{
		return this.longs.length * 8 + 4;
	}

	public Factory<LongCounter> factory()
	{
		return factory;
	}
	
	public static final LongCounterFactory factory = new LongCounterFactory();
	
	private static final class LongCounterFactory extends Factory<LongCounter>
	{
		public LongCounterFactory()
		{
			super(LongCounter.class);
		}

		public LongCounter resurrect(byte[] data, int start) throws InstantiationException
		{
			return new LongCounter(IOUtils.readLongArr(data, start));
		}
		
		public LongCounter resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new LongCounter(stream.readLongArr());
		}

	}
	
	public static final int test()
	{
		final long[] ints = new long[256];
		RandUtils.fillArr(ints);
		final LongCounter aes = new LongCounter(ints);
		int i = 0;
		i += IPersistable.test(aes);
		i += IDeepClonable.test(aes);
		return i;
	}
}

package claire.util.math.counters;

import java.io.IOException;
import java.util.Arrays;

import claire.util.io.Factory;
import claire.util.io.IOUtils;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.CObject;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class IntCounter 
	   implements CObject<IntCounter> {
	
	private final int[] ints;
	
	public IntCounter(int size)
	{
		ints = new int[size];
	}
	
	public IntCounter(int[] ints)
	{
		this.ints = ints;
	}
	
	public void add(int add)
	{
		ints[0] += add;
		int pos = 0;
		while(Bits.u_greaterThan(add, ints[pos++]))
		{
			ints[pos]++;
			add = 1;
		}
	}
	
	public void remove(int rem)
	{
		ints[0] -= rem;
		int pos = 0;
		while(Bits.u_greaterThan(ints[pos++], rem))
		{
			ints[pos]--;
			rem = 1;
		}
	}
	
	public int[] getInts()
	{
		return this.ints;
	}
	
	public void reset()
	{
		Arrays.fill(ints, 0);
	}

	public IntCounter createDeepClone()
	{
		return new IntCounter(ArrayUtil.copy(ints));
	}
	
	public int NAMESPACE()
	{
		return _NAMESPACE.INTCOUNTER;
	}

	public boolean sameAs(IntCounter obj)
	{
		return ArrayUtil.equals(this.ints, obj.ints);
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeIntArr(ints);
	}
	
	public void export(byte[] bytes, int offset)
	{
		IOUtils.writeArr(ints, bytes, offset);
	}

	public int exportSize()
	{
		return this.ints.length * 4 + 4;
	}

	public Factory<IntCounter> factory()
	{
		return factory;
	}
	
	public static final IntCounterFactory factory = new IntCounterFactory();
	
	private static final class IntCounterFactory extends Factory<IntCounter>
	{
		public IntCounterFactory()
		{
			super(IntCounter.class);
		}

		public IntCounter resurrect(byte[] data, int start) throws InstantiationException
		{
			return new IntCounter(IOUtils.readIntArr(data, start));
		}
		
		public IntCounter resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new IntCounter(stream.readIntArr());
		}
	}

	

}

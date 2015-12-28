package claire.util.memory.buffer;

import java.lang.reflect.Array;

import claire.util.memory.util.ArrayUtil;

@SuppressWarnings("unchecked")
public class Aggregator<Type> {
	
	protected Type[] array;
	protected final Class<Type> class_;
	
	private int pos = 0;
	
	public Aggregator(Class<Type> class_, int initSize) {
		this.class_ = class_;
		this.array = (Type[]) Array.newInstance(class_, initSize);
	}
	
	public Aggregator(Type[] init)
	{
		this.array = init;
		this.class_ = (Class<Type>) init.getClass().getComponentType();
		pos = init.length;
	}
	
	public void add(Type item)
	{
		if(pos == array.length) 
			ArrayUtil.upsize(array, 1);
		array[pos++] = item;
	}
	
	public void add(Type ... items)
	{
		this.add(items, 0, items.length);
	}
	
	public void add(Type[] items, int start, int len)
	{
		this.array = ArrayUtil.upsize(this.array, len - (array.length - pos));
		System.arraycopy(items, start, this.array, pos, len);
		pos += len;
	}
	
	public Type[] getFinal()
	{
		if(pos != array.length) 
		{
			Type[] ret = (Type[]) Array.newInstance(class_, pos);
			System.arraycopy(array, 0, ret, 0, pos);
			return ret;
		} else
			return this.array;
	}

	public void reset(int amt)
	{
		this.array = (Type[]) Array.newInstance(class_, amt);
	}
	
	public void reset()
	{
		this.reset(0);
	}
	
	public static final class ByteAggregator
	{
		protected byte[] array;
		
		private int pos;
		
		public ByteAggregator(int initSize) 
		{
			this.array = new byte[initSize];
		}
		
		public ByteAggregator(byte[] init)
		{
			this.array = init;
			pos = init.length;
		}
		
		public void add(byte item)
		{
			if(pos == array.length) 
				ArrayUtil.upsize(array, 1);
			array[pos++] = item;
		}
		
		public void add(byte ... items)
		{
			this.add(items, 0, items.length);
		}
		
		public void add(byte[] items, int start, int len)
		{
			this.array = ArrayUtil.upsize(this.array, len - (array.length - pos));
			System.arraycopy(items, start, this.array, pos, len);
			pos += len;
		}
		
		public byte[] getFinal()
		{
			if(pos != array.length) 
			{
				byte[] ret = new byte[pos];
				System.arraycopy(array, 0, ret, 0, pos);
				return ret;
			} else
				return this.array;
		}

		public void reset(int amt)
		{
			this.array = new byte[amt];
		}
		
		public void reset()
		{
			this.reset(0);
		}
		
	}
	
	public static final class ShortAggregator
	{
		protected short[] array;
		
		private int pos;
		
		public ShortAggregator(int initSize) 
		{
			this.array = new short[initSize];
		}
		
		public ShortAggregator(short[] init)
		{
			this.array = init;
			pos = init.length;
		}
		
		public void add(short item)
		{
			if(pos == array.length) 
				ArrayUtil.upsize(array, 1);
			array[pos++] = item;
		}
		
		public void add(short ... items)
		{
			this.add(items, 0, items.length);
		}
		
		public void add(short[] items, int start, int len)
		{
			this.array = ArrayUtil.upsize(this.array, len - (array.length - pos));
			System.arraycopy(items, start, this.array, pos, len);
			pos += len;
		}
		
		public short[] getFinal()
		{
			if(pos != array.length) 
			{
				short[] ret = new short[pos];
				System.arraycopy(array, 0, ret, 0, pos);
				return ret;
			} else
				return this.array;
		}

		public void reset(int amt)
		{
			this.array = new short[amt];
		}
		
		public void reset()
		{
			this.reset(0);
		}
	}
	
	public static final class CharAggregator
	{
		protected char[] array;
		
		private int pos;
		
		public CharAggregator(int initSize) 
		{
			this.array = new char[initSize];
		}
		
		public CharAggregator(char[] init)
		{
			this.array = init;
			pos = init.length;
		}
		
		public void add(char item)
		{
			if(pos == array.length) 
				ArrayUtil.upsize(array, 1);
			array[pos++] = item;
		}
		
		public void add(char ... items)
		{
			this.add(items, 0, items.length);
		}
		
		public void add(char[] items, int start, int len)
		{
			this.array = ArrayUtil.upsize(this.array, len - (array.length - pos));
			System.arraycopy(items, start, this.array, pos, len);
			pos += len;
		}
		
		public char[] getFinal()
		{
			if(pos != array.length) 
			{
				char[] ret = new char[pos];
				System.arraycopy(array, 0, ret, 0, pos);
				return ret;
			} else
				return this.array;
		}

		public void reset(int amt)
		{
			this.array = new char[amt];
		}
		
		public void reset()
		{
			this.reset(0);
		}
	}
	
	public static final class IntAggregator
	{
		protected int[] array;
		
		private int pos;
		
		public IntAggregator(int initSize) 
		{
			this.array = new int[initSize];
		}
		
		public IntAggregator(int[] init)
		{
			this.array = init;
			pos = init.length;
		}
		
		public void add(int item)
		{
			if(pos == array.length) 
				ArrayUtil.upsize(array, 1);
			array[pos++] = item;
		}
		
		public void add(int ... items)
		{
			this.add(items, 0, items.length);
		}
		
		public void add(int[] items, int start, int len)
		{
			this.array = ArrayUtil.upsize(this.array, len - (array.length - pos));
			System.arraycopy(items, start, this.array, pos, len);
			pos += len;
		}
		
		public int[] getFinal()
		{
			if(pos != array.length) 
			{
				int[] ret = new int[pos];
				System.arraycopy(array, 0, ret, 0, pos);
				return ret;
			} else
				return this.array;
		}

		public void reset(int amt)
		{
			this.array = new int[amt];
		}
		
		public void reset()
		{
			this.reset(0);
		}
	}
	
	public static final class LongAggregator
	{
		protected long[] array;
		
		private int pos;
		
		public LongAggregator(int initSize) 
		{
			this.array = new long[initSize];
		}
		
		public LongAggregator(long[] init)
		{
			this.array = init;
			pos = init.length;
		}
		
		public void add(long item)
		{
			if(pos == array.length) 
				ArrayUtil.upsize(array, 1);
			array[pos++] = item;
		}
		
		public void add(long ... items)
		{
			this.add(items, 0, items.length);
		}
		
		public void add(long[] items, int start, int len)
		{
			this.array = ArrayUtil.upsize(this.array, len - (array.length - pos));
			System.arraycopy(items, start, this.array, pos, len);
			pos += len;
		}
		
		public long[] getFinal()
		{
			if(pos != array.length) 
			{
				long[] ret = new long[pos];
				System.arraycopy(array, 0, ret, 0, pos);
				return ret;
			} else
				return this.array;
		}

		public void reset(int amt)
		{
			this.array = new long[amt];
		}
		
		public void reset()
		{
			this.reset(0);
		}
	}

}

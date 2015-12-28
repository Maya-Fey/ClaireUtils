package claire.util.io;

import java.io.IOException;

import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.CObject;
import claire.util.standards.IIterable;
import claire.util.standards.IIterator;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class PositionIndex 
	   implements IIterable<Integer>,
	   			  CObject<PositionIndex> {

	private int[] start;
	
	private int overflow;
	private int pos;
	private int lastlen;
	
	public PositionIndex()
	{
		this(10);
	}
	
	public PositionIndex(int start)
	{
		this.start = new int[start];
		this.pos = 0;
		this.overflow = start << 2;
		this.lastlen = 0;
	}
	
	public PositionIndex(int[] start, int pos, int lastlen)
	{
		this.start = start;
		this.pos = pos;
		this.overflow = (start.length >> 1) + 1;
		this.lastlen = lastlen;
	}
	
	public void setOverflowRate(int rate)
	{
		this.overflow = rate;
	}
	
	public int getLength(int index)
	{
		int next = index + 1;
		if(next == pos)
			return this.lastlen;
		return start[next] - start[index];
	}
	
	public int getTotalAt(int index)
	{
		int next = index + 1;
		if(next == pos)
			return start[index] + this.lastlen;
		return start[next];
	}
	
	public int getTotalLength()
	{
		if(pos == 0)
			return 0;
		return start[pos - 1] + this.lastlen;
	}
	
	public void add(int len)
	{
		if(pos == start.length)
			overflow();
		if(pos > 0) 
			start[pos] = start[pos - 1] + this.lastlen;
		pos++;
		this.lastlen = len;
	}
	
	public void deleteFrom(int pos)
	{
		if(pos > 0)
			this.lastlen = getLength(pos - 1);
		else
			this.lastlen = 0;
		this.pos = pos;		
	}
	
	public int indexFromPos(int pos)
	{
		if(pos >= this.getTotalLength())
			throw new java.lang.ArrayIndexOutOfBoundsException();
		if(this.pos < 50)
			return linear(pos);
		else
			return binary(pos);
	}
	
	public final int getStart(int index)
	{
		return start[index];
	}
	
	public final int getTop()
	{
		return this.pos - 1;
	}
	
	public final int getSize()
	{
		return this.pos;
	}
	
	public final void update(int up)
	{
		this.lastlen += up;
	}
	
	public final void increment()
	{
		this.lastlen++;
	}
	
	private int linear(int pos)
	{
		int i = this.pos - 1;
		if(pos >= start[i])
			if(pos < (start[i] + this.lastlen))
				return i;
			else
				throw new java.lang.ArrayIndexOutOfBoundsException("No index exists that contains that position.");
		while(i > 0) 
			if(pos >= start[--i])
				return i;
		throw new java.lang.ArithmeticException();
	}
	
	private int binary(int pos)
	{
		int cur = this.pos / 2;
		int upper = this.pos;
		int lower = 0;
		while(true)
		{
			if(start[cur] > pos) {
				upper = cur;		
			} else if(pos >= getTotalAt(cur)) {
				lower = cur;
			} else {
				return cur;
			}
			cur = lower + ((upper - lower) >>> 1);
		}
	}
	
	private void overflow()
	{
		start = ArrayUtil.upsize(start, overflow);
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.POSITIONINDEX;
	}
	
	public boolean sameAs(PositionIndex obj)
	{
		return ArrayUtil.equals(start, obj.start) && this.lastlen == obj.lastlen;
	}
	
	public PositionIndex createDeepClone()
	{
		return new PositionIndex(ArrayUtil.copy(start), pos, lastlen);
	}
	
	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInt(pos);
		stream.writeInts(start, 0, pos);
		stream.writeInt(lastlen);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.intToBytes(pos, bytes, offset);
		Bits.intsToBytes(start, 0, bytes, offset += 4, pos);
		Bits.intToBytes(lastlen, bytes, offset + (pos << 2));
	}

	public int exportSize()
	{
		return (pos << 2) + 8;
	}

	public Factory<PositionIndex> factory()
	{
		return factory;
	}
	
	public IIterator<Integer> iterator()
	{
		return new PositionIndexIterator();
	}
	
	public static Factory<PositionIndex> getFactory()
	{
		return factory;
	}
	
	public static final PositionIndexFactory factory = new PositionIndexFactory();
	
	public static final class PositionIndexFactory extends Factory<PositionIndex>
	{
		public PositionIndexFactory()
		{
			super(PositionIndex.class);
		}

		public PositionIndex resurrect(byte[] data, int start)
		{
			int pos = Bits.intFromBytes(data, start);
			int[] istart = new int[pos];
			Bits.bytesToInts(data, start += 4, istart, 0, pos);
			return new PositionIndex(istart, pos, Bits.intFromBytes(data, start + (pos << 2)));
		}

		public PositionIndex resurrect(IIncomingStream stream) throws IOException
		{
			int pos = stream.readInt();
			int[] start = stream.readInts(pos);
			return new PositionIndex(start, pos, stream.readInt());
		}
		
	}
	
	private final class PositionIndexIterator implements IIterator<Integer>
	{
		private int pos = 0;

		public boolean hasNext()
		{
			return this.pos != PositionIndex.this.pos;
		}

		public Integer next()
		{
			try {
				return start[pos];
			} finally {
				pos++;
			}
		}

		public void skip()
		{
			pos++;
		}

		public void skip(int amt)
		{
			pos += amt;
		}
		
	}

}

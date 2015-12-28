package claire.util.memory.array;

import java.util.Arrays;

import claire.util.encoding.CString;
import claire.util.memory.util.ArrayUtil;
import claire.util.memory.util.Pair;
import claire.util.standards.IIterator;
import claire.util.standards.IPointer;

public class IndexedMemory<Type> 
	   extends Memory<Type> {
	
	private String[] index;
	
	public IndexedMemory(Class<Type> class_, int initSize) {
		super(class_, initSize);
		index = new String[initSize];
	}
	
	public IndexedMemory(Type[] val, String[] index)
	{
		super(val);
		this.index = index;
	}
	
	protected void overflow()
	{
		super.overflow();
		index = ArrayUtil.upsize(index, rate);
	}
	
	public int allocate(Type t)
	{
		int pos = super.allocate(t);
		index[pos] = "NULL";
		return pos;
	}
	
	public int allocate(Type t, String index)
	{
		int pos = super.allocate(t);
		this.index[pos] = index;
		return pos;
	}
	
	public void overwrite(String s, Type t)
	{
		for(int i = 0; i < index.length; i++)
			if(index[i].equals(s))
			{
				array[i] = t;
				return;
			}
	}
	
	public Type get(String s)
	{
		for(int i = 0; i < index.length; i++)
			if(!free[i])
				if(new CString(index[i]).equals(new CString(s)))
					return array[i];
		throw new java.lang.NullPointerException();
	}
	
	public void free(String s)
	{
		for(int i = 0; i < index.length; i++)
			if(index[i].equals(s)) {
				super.free(i);
				return;
			}
	}
	
	public IPointer<Type> pointer(String s)
	{
		for(int i = 0; i < index.length; i++)
			if(index[i].equals(s)) 
				return this.pointer(i);
		throw new java.lang.NullPointerException();
	}
	
	public String[] index()
	{
		return this.index;
	}
	
	public int reorganize()
	{
		boolean free = false;
		int startpos = 0,
			endpos = 0;
		int last = 0;
		for(int i = 0; i < this.size();)
		{
			if(free) {
				if(!this.free[i++]) {
					endpos = i - 1;
					int len = 1;
					for(; i < this.size(); i++)
						if(!this.free[i])
							len++;
						else 
							break;
					System.arraycopy(this.array, endpos, this.array, startpos, len);
					System.arraycopy(this.index, endpos, this.index, startpos, len);
					System.arraycopy(this.free, endpos, this.free, startpos, len);
					Arrays.fill(this.free, startpos + len, i, true);
					last = i = startpos + len;
					free = false;
				}
			} else {
				startpos = i;
				if(this.free[i++])
					free = true;
				else
					last = i;
			}
		}
		return last;
	}

	public IIterator<Pair<String, Type>> pairIterator()
	{
		return this.new IMIterator();
	}
	
	public IIterator<String> indexIterator()
	{
		return this.new IndexIterator();
	}
	
	protected class IMIterator 
			  implements IIterator<Pair<String, Type>>
	{
		int pos = 0;

		public boolean hasNext()
		{
			try {
				while(free[pos])
				{
					pos++;
				}
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				return false;
			}
			return true;
		}

		public Pair<String, Type> next()
		{
			if(free[pos]) {
				while(free[pos])
				{
					pos++;
				}
			}
			try {
				return new Pair<String, Type>(index[pos], array[pos]);
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
	
	protected class IndexIterator 
			  implements IIterator<String> 
	{
		protected int pos = 0;
		
		public boolean hasNext()
		{
			try {
				while(free[pos])
				{
					pos++;
				}
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				return false;
			}
			return true;
		}

		public String next()
		{
			if(free[pos]) {
				while(free[pos])
				{
					pos++;
				}
			}
			try {
				return index[pos];
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

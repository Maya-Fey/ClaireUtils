package claire.util.memory.util;

import java.io.IOException;

import claire.util.io.Factory;
import claire.util.standards.CObject;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

@SuppressWarnings("hiding")
public class CPair<Object extends CObject<Object>, Pair extends CObject<Pair>> 
	   implements CObject<CPair<Object, Pair>> {

	private Object t;
	private Pair e;
	
	public CPair(Object t, Pair e)
	{
		this.t = t;
		this.e = e;
	}
	
	public void setObject(Object t)
	{
		this.t = t;
	}
	
	public void setPair(Pair e)
	{
		this.e = e;
	}
	
	public Object getObject()
	{
		return this.t;
	}
	
	public Pair getPair()
	{
		return this.e;
	}	
	
	public CPair<Object, Pair> createDeepClone()
	{
		return new CPair<Object, Pair>(t.createDeepClone(), e.createDeepClone());
	}

	@Override
	public void export(IOutgoingStream stream) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void export(byte[] bytes, int offset)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int exportSize()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public Factory<CPair<Object, Pair>> factory()
	{
		return new CPairFactory();
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.PAIR;
	}

	public boolean sameAs(CPair<Object, Pair> obj)
	{
		return obj.getObject().equals(t) && obj.getPair().equals(e);
	}
	
	private final class CPairFactory extends Factory<CPair<Object, Pair>>
	{

		@SuppressWarnings("unchecked")
		protected CPairFactory() 
		{
			// Yeah yeah shut up. The classes are identical anyway
			super((Class<CPair<Object, Pair>>) CPair.this.getClass());
		}

		public CPair<Object, Pair> resurrect(byte[] data)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CPair<Object, Pair> resurrect(byte[] data, int start)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CPair<Object, Pair> resurrect(IIncomingStream stream)
				throws IOException
		{
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}

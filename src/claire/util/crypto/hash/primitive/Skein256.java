package claire.util.crypto.hash.primitive;

import java.io.IOException;

import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.hash.primitive.Skein256.Skein256State;
import claire.util.io.Factory;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class Skein256
	   extends MerkleHash<Skein256State, Skein256> {
	
	private long t1, t2;
	
	private long[] state = new long[4];
	
	protected Skein256(int out)
	{
		super(32, out);
	}
	
	public int hashID()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public HashFactory<Skein256> factory()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Skein256State getState()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void updateState(Skein256State state)
	{
		// TODO Auto-generated method stub
		
	}

	public void processNext(byte[] bytes, int offset)
	{
		// TODO Auto-generated method stub
		
	}

	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		// TODO Auto-generated method stub
		
	}

	public void loadCustom(Skein256State state)
	{
		// TODO Auto-generated method stub
		
	}

	public static final class Skein256State 
						extends MerkleHash.MerkleState<Skein256State, Skein256>
	{

		public Skein256State(byte[] bytes, int pos)
		{
			super(bytes, pos);
			// TODO Auto-generated constructor stub
		}

		public int stateID()
		{
			// TODO Auto-generated method stub
			return 0;
		}

		public Factory<Skein256State> factory()
		{
			// TODO Auto-generated method stub
			return null;
		}

		public int NAMESPACE()
		{
			// TODO Auto-generated method stub
			return 0;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			// TODO Auto-generated method stub
			
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			// TODO Auto-generated method stub
			
		}

		protected void addCustom(IIncomingStream os) throws IOException
		{
			// TODO Auto-generated method stub
			
		}

		protected void addCustom(byte[] bytes, int start)
		{
			// TODO Auto-generated method stub
			
		}

		protected void addCustom(Skein256 hash)
		{
			// TODO Auto-generated method stub
			
		}

		protected void updateCustom(Skein256 hash)
		{
			// TODO Auto-generated method stub
			
		}

		protected void eraseCustom()
		{
			// TODO Auto-generated method stub
			
		}

		protected boolean compareCustom(Skein256State state)
		{
			// TODO Auto-generated method stub
			return false;
		}

		protected int customSize()
		{
			// TODO Auto-generated method stub
			return 0;
		}
		
	}

}

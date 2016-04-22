package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.MerkleHash.MerkleState;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.crypto.IHash;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public abstract class MerkleHash<State extends MerkleState<State, ?>, Hash extends MerkleHash<State, Hash>> 
				implements IHash<Hash, State> {
	
	protected final int size;
	
	protected final byte[] temp;

	protected int pos = 0;
	
	private final int out;
	
	protected MerkleHash(int size, int out)
	{
		this.size = size;
		this.out = out;
		this.temp = new byte[size];
	}

	public void add(byte[] bytes, int start, int length)
	{
		int rem = length;
		if(pos + rem >= size && pos > 0) {
			rem -= size - pos;
			System.arraycopy(bytes, start, temp, pos, size - pos);
			this.processNext(temp, 0);
			pos = 0;
		}
		while(rem >= size) {
			this.processNext(bytes, bytes.length - rem);
			rem -= size;
		}
		System.arraycopy(bytes, bytes.length - rem, temp, pos, rem);
		pos += rem;
	}
	
	public abstract void processNext(byte[] bytes, int offset);
	public abstract void finalize(byte[] remaining, int pos, byte[] out, int start);
	public abstract void loadCustom(State state);

	public void finish(byte[] out, int start)
	{
		try {
			this.finalize(temp, pos, out, start);
		} finally {
			pos = 0;
		}
	}
	
	public void reset()
	{
		pos = 0;
	}

	public int outputLength()
	{
		return this.out;
	}
	
	public int desiredInputLength()
	{
		return this.size;
	}
	
	public void loadState(State state)
	{
		System.arraycopy(state.temp, 0, this.temp, 0, size);
		this.pos = state.pos;
		this.loadCustom(state);
	}
	
	public static abstract class MerkleState<State extends MerkleState<State, ?>, Hash extends MerkleHash<State, ?>> implements IState<State>
	{
		protected byte[] temp;
		protected int pos;
		
		public MerkleState(byte[] bytes, int pos)
		{
			this.temp = bytes;
			this.pos = pos;
		}
		
		public MerkleState(Hash hash)
		{
			this.temp = ArrayUtil.copy(hash.temp);
			this.pos = hash.pos;
			this.addCustom(hash);
		}
		
		public void update(Hash hash)
		{
			if(temp == null)
				this.temp = ArrayUtil.copy(hash.temp);
			else
				System.arraycopy(hash.temp, 0, this.temp, 0, hash.size);
			this.pos = hash.pos;
			this.updateCustom(hash);
		}
		
		protected abstract void persistCustom(IOutgoingStream os) throws IOException;
		protected abstract void persistCustom(byte[] bytes, int start);
		protected abstract void addCustom(IIncomingStream os) throws IOException;
		protected abstract void addCustom(byte[] bytes, int start);
		protected abstract void addCustom(Hash hash);
		protected abstract void updateCustom(Hash hash);
		protected abstract void eraseCustom();
		
		protected abstract boolean compareCustom(State state);
		
		protected abstract int customSize();
		
		public void export(IOutgoingStream stream) throws IOException
		{
			stream.writeInt(pos);
			stream.writeBytes(temp);
			this.persistCustom(stream);
		}

		public void export(byte[] bytes, int offset)
		{
			Bits.intToBytes(pos, bytes, offset); offset += 4;
			System.arraycopy(temp, 0, bytes, offset, temp.length); offset += temp.length;
			this.persistCustom(bytes, offset);
		}

		public int exportSize()
		{
			return 4 + temp.length + customSize();
		}

		public boolean sameAs(State obj)
		{
			return (this.pos == obj.pos && ArrayUtil.equals(obj.temp, this.temp)) && this.compareCustom(obj);
		}

		public void erase()
		{
			Arrays.fill(temp, (byte) 0);
			temp = null;
			pos = 0;
		}
		
	}
	
	protected static abstract class MerkleStateFactory<State extends MerkleState<State, Hash>, Hash extends MerkleHash<State, ?>>
							  extends Factory<State>
	{
		protected final int size;
		
		protected MerkleStateFactory(Class<State> class_, int size)
		{
			super(class_);
			this.size = size;
		}
		
		protected abstract State construct(byte[] bytes, int pos);
		
		public State resurrect(IIncomingStream is) throws IOException
		{
			int pos = is.readInt();
			byte[] bytes = is.readBytes(size);
			State state = construct(bytes, pos);
			state.addCustom(is);
			return state;
		}
		
		public State resurrect(byte[] bytes, int start)
		{
			int pos = Bits.intFromBytes(bytes, start); start += 4;
			byte[] temp = new byte[size];
			System.arraycopy(bytes, start, temp, 0, size); start += size;
			State state = construct(temp, pos);
			state.addCustom(bytes, start);
			return state;
		}
		
	}

}

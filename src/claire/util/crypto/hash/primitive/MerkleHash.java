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

public abstract class MerkleHash<State extends MerkleState<State, Hash>, Hash extends MerkleHash<State, Hash>> 
				implements IHash<State> {
	
	protected final int size;
	
	private final int out;
	
	private final byte[] temp;

	private int pos = 0;
	
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

	public void finish(byte[] out, int start)
	{
		try {
			this.finalize(temp, pos, out, start);
		} finally {
			pos = 0;
		}
	}

	public int outputLength()
	{
		return this.out;
	}
	
	public void persistTemp(IOutgoingStream os) throws IOException
	{
		os.writeInt(pos);
		os.writeBytes(temp);
	}
	
	public void persistTemp(byte[] bytes, int offset)
	{
		Bits.intToBytes(pos, bytes, offset); offset += 4;
		System.arraycopy(temp, 0, bytes, offset, size);
	}
	
	protected static abstract class MerkleState<State extends MerkleState<State, Hash>, Hash extends MerkleHash<State, Hash>> implements IState<State>
	{
		protected byte[] temp;
		protected int pos;
		
		protected abstract void persistCustom(IOutgoingStream os);
		protected abstract void persistCustom(byte[] bytes, int start);
		protected abstract void addCustom(IIncomingStream os);
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
		}

		public int exportSize()
		{
			return 4 + temp.length + customSize();
		}

		public Factory<State> factory()
		{
			// TODO Auto-generated method stub
			return null;
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

}

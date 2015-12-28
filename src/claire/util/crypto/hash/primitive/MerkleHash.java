package claire.util.crypto.hash.primitive;

import claire.util.standards.crypto.IHash;

public abstract class MerkleHash implements IHash {
	
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

}

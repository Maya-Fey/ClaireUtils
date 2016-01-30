package claire.util.standards.crypto;

public interface IHash {
	
	void add(byte[] bytes, int start, int length);
	
	void finish(byte[] out, int start);
	
	int outputLength();
	
	default void add(byte[] bytes)
	{
		add(bytes, 0, bytes.length);
	}

	default void finish(byte[] out)
	{
		finish(out, 0);
	}
	
	default byte[] finish()
	{
		byte[] bytes = new byte[this.outputLength()];
		finish(bytes, 0);
		return bytes;
	}
	
	default void hash(byte[] in, byte[] out)
	{
		hash(in, 0, out, 0);
	}
	
	default void hash(byte[] in, int start0, byte[] out, int start1)
	{
		this.add(in, start0, in.length);
		this.finish(out, start1);
	}
	
	default void hash(byte[] in, int start0, int length, byte[] out, int start1)
	{
		this.add(in, start0, length);
		this.finish(out, start1);
	}
	
	default byte[] hash(byte[] in, int start)
	{
		byte[] out = new byte[this.outputLength()];
		hash(in, start, out, 0);
		return out;
	}
	
	default byte[] hash(byte[] in)
	{
		byte[] out = new byte[this.outputLength()];
		hash(in, 0, out, 0);
		return out;
	}
	
}

package claire.util.memory.buffer;

public class ByteRegulator {
	
	private final byte[] in;
	private int pos;
	
	public ByteRegulator(byte[] b)
	{
		this.in = b;
	}
	
	public int accept(byte[] bytes, int start, int len)
	{
		int amt = in.length - pos;
		if(len < amt) {
			System.arraycopy(bytes, start, in, pos, len);
			pos += len;
			return len;
		} else {
			System.arraycopy(bytes, start, in, pos, amt);
			pos = 0;
			return -amt;
		}
	}

}

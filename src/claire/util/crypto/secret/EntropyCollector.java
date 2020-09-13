package claire.util.crypto.secret;

import java.util.Arrays;

import claire.util.memory.Bits;
import claire.util.memory.buffer.ByteRegulator;
import claire.util.standards.IReferrable;
import claire.util.standards.crypto.IHash;

public class EntropyCollector<Hash extends IHash<Hash, ?>> 
	   implements IReferrable<EntropyCollector<Hash>> {
	
	private final Hash hash;
	private final ByteRegulator reg;
	private final byte[][] data;
	private final byte[] in;
	private final int rounds;
	
	public EntropyCollector(Hash hash, int bytes, int rounds)
	{
		this.hash = hash;
		this.rounds = 2;
		in = new byte[hash.outputLength()];
		reg = new ByteRegulator(in);
		data = new byte[((bytes - 1) / hash.outputLength()) + 1][hash.outputLength()];
		if(data.length < 256 || hash.outputLength() == 1)
			for(int i = 0; i < data.length; i++) 
				data[i][0] = (byte) (i & 0xFF);
		else
			for(int i = 0; i < data.length; i++)
				Bits.shortToBytes((short) (i & 0xFFFF), data[i], 0);
	}
	
	public EntropyCollector(Hash hash, byte[][] data, byte[] in, int rounds)
	{
		this.hash = hash;
		this.data = data;
		this.in = in;
		this.rounds = rounds;
		this.reg = new ByteRegulator(in);
	}
	
	private void hashRound()
	{
		//This is to prevent the very unlikely scenario in which two branches become identical
		if(data.length < 256 || hash.outputLength() == 1)
			for(int i = 0; i < data.length; i++) 
				data[i][0] ^= (byte) (i & 0xFF);
		else
			for(int i = 0; i < data.length; i++) {
				data[i][0] ^= (byte) (i & 0xFF);
				data[i][1] ^= (byte) ((i >>> 8) & 0xFF);
			}
		for(int i = 0; i < data.length; i++) {
			hash.add(data[i]);
			hash.finish(data[i]);
		}
	}
	
	private void addData()
	{
		for(int i = 0; i < data.length; i++)
			for(int j = 0; j < in.length; j++)
				data[i][j] ^= in[j];
		for(int i = 0; i < rounds; i++)
			this.hashRound();
	}
	
	public void add(byte[] data, int start, int len) {
		while(len > 0) {
			int back = reg.accept(data, start, len);
			if(back < 0) {
				addData();
				back = -back;
			}
			start += back;
			len -= back;
		}
	}
	
	public byte[] getFinal()
	{
		byte[] bytes = new byte[hash.outputLength() * data.length];
		for(int i = 0; i < data.length; i++)
			System.arraycopy(data[i], 0, bytes, i * hash.outputLength(), hash.outputLength());
		return bytes;
	}
	
	public void erase()
	{
		for(int i = 0; i < data.length; i++)
			Arrays.fill(data[i], (byte) 0);
		Arrays.fill(in, (byte) 0);
	}
	
}

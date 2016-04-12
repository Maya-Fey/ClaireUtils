package claire.util.crypto.hash;

import java.io.File;
import java.io.IOException;

import claire.util.encoding.Hex;
import claire.util.io.IOUtils;
import claire.util.memory.buffer.PrimitiveDeaggregator;
import claire.util.standards.IPersistable;
import claire.util.standards.crypto.IHash;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class HashFunction 
	   implements IOutgoingStream {
	
	private final IHash<?, ?> hash;
	
	private final byte[] buf = new byte[8];
	private final PrimitiveDeaggregator deag = new PrimitiveDeaggregator(buf);
	
	public HashFunction(IHash<?, ?> hash)
	{
		this.hash = hash;
	}
	
	public void addFile(File file) throws IOException
	{
		byte[] bytes = IOUtils.toBytes(file);
		hash.add(bytes);
	}

	public void close() {}

	public void writeBool(boolean data)
	{
		if(data)
			buf[0] = 1;
		else
			buf[0] = 0;
		hash.add(buf, 0, 1);
	}

	public void writeByte(byte data)
	{
		buf[0] = data;
		hash.add(buf, 0, 1);
	}

	public void writeShort(short data)
	{
		deag.processShort(data);
		hash.add(buf, 0, 2);
	}

	public void writeChar(char data)
	{
		deag.processChar(data);
		hash.add(buf, 0, 2);
	}

	public void writeInt(int data)
	{
		deag.processInt(data);
		hash.add(buf, 0, 4);
	}

	public void writeLong(long data)
	{
		deag.processLong(data);
		hash.add(buf, 0, 8);
	}

	public void writeBools(boolean[] bools, int off, int len)
	{
		while(len > 8)
		{
			buf[0] = (byte) (bools[off++] ? 1 : 0);
			buf[1] = (byte) (bools[off++] ? 1 : 0);
			buf[2] = (byte) (bools[off++] ? 1 : 0);
			buf[3] = (byte) (bools[off++] ? 1 : 0);
			buf[4] = (byte) (bools[off++] ? 1 : 0);
			buf[5] = (byte) (bools[off++] ? 1 : 0);
			buf[6] = (byte) (bools[off++] ? 1 : 0);
			buf[7] = (byte) (bools[off++] ? 1 : 0);
			hash.add(buf, 0, 8);
			len -= 8;
		}
		if(len > 0)
		{
			int i = 0;
			while(len-- > 0)
				buf[i++] = (byte) (bools[off++] ? 1 : 0);
			hash.add(buf, 0, i);
		}
	}

	public void writeNibbles(byte[] nibbles, int off, int len)
	{
		while(len > 16) {
			buf[0] = (byte) (((nibbles[off++] & 0xF) << 4) |
							  (nibbles[off++] & 0xF));
			buf[1] = (byte) (((nibbles[off++] & 0xF) << 4) |
							  (nibbles[off++] & 0xF));
			buf[2] = (byte) (((nibbles[off++] & 0xF) << 4) |
							  (nibbles[off++] & 0xF));
			buf[3] = (byte) (((nibbles[off++] & 0xF) << 4) |
							  (nibbles[off++] & 0xF));
			buf[4] = (byte) (((nibbles[off++] & 0xF) << 4) |
							  (nibbles[off++] & 0xF));
			buf[5] = (byte) (((nibbles[off++] & 0xF) << 4) |
							  (nibbles[off++] & 0xF));
			buf[6] = (byte) (((nibbles[off++] & 0xF) << 4) |
							  (nibbles[off++] & 0xF));
			buf[7] = (byte) (((nibbles[off++] & 0xF) << 4) |
							  (nibbles[off++] & 0xF));
			hash.add(buf, 0, 8);
			len -= 16;
		}
		if(len > 1)
		{
			int i = 0;
			while(len-- > 1)
				buf[i] = (byte) (((nibbles[off++] & 0xF) << 4) |
								  (nibbles[off++] & 0xF));
			hash.add(buf, 0, i);
		}
	}

	public void writeBytes(byte[] bytes, int off, int len)
	{
		hash.add(bytes, off, len);
	}

	public void writeShorts(short[] shorts, int off, int len)
	{
		while(len-- > 0)
		{
			deag.processShort(shorts[off++]);
			hash.add(buf, 0, 8);
		}
	}

	public void writeChars(char[] charss, int off, int len)
	{
		while(len-- > 0)
		{
			deag.processChar(charss[off++]);
			hash.add(buf, 0, 8);
		}
	}

	public void writeInts(int[] ints, int off, int len)
	{
		while(len-- > 0)
		{
			deag.processInt(ints[off++]);
			hash.add(buf, 0, 8);
		}
	}

	public void writeLongs(long[] longs, int off, int len) 
	{
		while(len-- > 0)
		{
			deag.processLong(longs[off++]);
			hash.add(buf, 0, 8);
		}
	}
	
	public void persist(IPersistable<?> obj)
	{
		try {
			obj.export(this);
		} catch (IOException e) {}
	}
	
	public void rewind(long pos) {}
	public void skip(long pos) {}
	public void seek(long pos) {}
	public IIncomingStream getIncoming()  { return null; }
	
	public String getHashString()
	{
		return Hex.toHexString(hash.finish());
	}
	
}

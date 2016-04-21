package claire.util.memory.buffer;

import java.io.IOException;

import claire.util.memory.Bits;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class BufferedOutgoingStream 
	   implements IOutgoingStream {

	private final IOutgoingStream os;
	private final byte[] buffer;
	
	private final int l2,
					  l4,
					  l8,
					  l2t,
					  l4t,
					  l8t;
	
	private int pos = 0;
	
	public BufferedOutgoingStream(IOutgoingStream os, byte[] buffer)
	{
		this.os = os;
		this.buffer = buffer;
		l2 = buffer.length >>> 1;
		l4 = buffer.length >>> 2;
		l8 = buffer.length >>> 3;
		l2t = l2 << 1;
		l4t = l4 << 2;
		l8t = l8 << 3;
	}
	
	public BufferedOutgoingStream(IOutgoingStream os, int size)
	{
		this.os = os;
		this.buffer = new byte[size];
		l2 = size >>> 1;
		l4 = size >>> 2;
		l8 = size >>> 3;
		l2t = l2 << 1;
		l4t = l4 << 2;
		l8t = l8 << 3;
	}
	
	public byte[] getBuffer()
	{
		return this.buffer;
	}
	
	private void doIt(int incoming) throws IOException
	{
		if(pos + incoming > buffer.length) {
			os.writeBytes(buffer, 0, pos);
			pos = 0;
		}
	}
	
	public void flush() throws IOException
	{
		os.writeBytes(buffer, 0, pos);
		pos = 0;
	}
	
	private void flush(int pos) throws IOException
	{
		os.writeBytes(buffer, 0, pos);
	}
	
	public void close() throws IOException	
	{
		if(pos > 0)
			this.flush();
		os.close();
	}

	public void writeBool(boolean data) throws IOException
	{
		doIt(1);
		buffer[pos++] = (byte) (data ? 1 : 0);
	}

	public void writeByte(byte data) throws IOException
	{
		doIt(1);
		buffer[pos++] = data;
	}

	public void writeShort(short data) throws IOException
	{
		doIt(2);
		buffer[pos++] = (byte)  data;
		buffer[pos++] = (byte) (data >>>  8);
	}

	public void writeChar(char data) throws IOException
	{
		doIt(2);
		buffer[pos++] = (byte)  data;
		buffer[pos++] = (byte) (data >>>  8);
	}

	public void writeInt(int data) throws IOException
	{
		doIt(4);
		buffer[pos++] = (byte)  data;
		buffer[pos++] = (byte) (data >>>= 8);
		buffer[pos++] = (byte) (data >>>= 8);
		buffer[pos++] = (byte) (data >>>  8);
	}

	public void writeLong(long data) throws IOException
	{
		doIt(8);
		buffer[pos++] = (byte)  data;
		buffer[pos++] = (byte) (data >>>= 8);
		buffer[pos++] = (byte) (data >>>= 8);
		buffer[pos++] = (byte) (data >>>= 8);
		buffer[pos++] = (byte) (data >>>= 8);
		buffer[pos++] = (byte) (data >>>= 8);
		buffer[pos++] = (byte) (data >>>= 8);
		buffer[pos++] = (byte) (data >>>  8);
	}

	public void writeBools(boolean[] bools, int off, int len) throws IOException
	{
		int rem = buffer.length - pos;
		if(rem > len) {
			while(len-- > 0)
				buffer[pos++] = (byte) (bools[off++] ? 1 : 0);
		} else {
			while(pos < buffer.length)
				buffer[pos++] = (byte) (bools[off++] ? 1 : 0);
			len -= rem;
			flush();
			while(len > buffer.length) {
				pos = 0;
				while(pos < buffer.length)
					buffer[pos++] = (byte) (bools[off++] ? 1 : 0);
				len -= buffer.length;
				flush();
			}
			pos = 0;
			while(len-- > 0)
				buffer[pos++] = (byte) (bools[off++] ? 1 : 0);
		}
	}

	public void writeNibbles(byte[] nibbles, int off, int len) throws IOException
	{
		int rem = buffer.length - pos;
		len /= 2;
		if(rem > len) {
			while(len-- > 0) {
				buffer[pos++] = ((byte) (((nibbles[off++] & 0xF) << 4) |
										  (nibbles[off++] & 0xF)));
			}
		} else {
			while(pos < buffer.length)
				buffer[pos++] = ((byte) (((nibbles[off++] & 0xF) << 4) |
						  				  (nibbles[off++] & 0xF)));
			len -= rem;
			flush();
			while(len > buffer.length) {
				pos = 0;
				while(pos < buffer.length)
					buffer[pos++] = ((byte) (((nibbles[off++] & 0xF) << 4) |
							 				  (nibbles[off++] & 0xF)));
				len -= buffer.length;
				flush();
			}
			pos = 0;
			while(len-- > 0)
				buffer[pos++] = ((byte) (((nibbles[off++] & 0xF) << 4) |
						  			      (nibbles[off++] & 0xF)));
		}
	}

	public void writeBytes(byte[] bytes, int off, int len) throws IOException
	{
		int rem = buffer.length - pos;
		if(rem > len) {
			System.arraycopy(bytes, off, buffer, pos, len);
			pos += len;
		} else {
			System.arraycopy(bytes, off, buffer, pos, rem);
			off += rem;
			len -= rem;
			flush(buffer.length);
			while(len > buffer.length) {
				System.arraycopy(bytes, off, buffer, 0, buffer.length);
				off += buffer.length;
				len -= buffer.length;
				flush(buffer.length);
			}
			System.arraycopy(bytes, off, buffer, 0, len);
			pos = len;
		}
	}

	public void writeShorts(short[] shorts, int off, int len) throws IOException
	{
		int rem = (buffer.length - pos) >>> 1;
		if(rem > len) {
			Bits.shortsToBytes(shorts, off, buffer, pos, len);
			pos += len * 2;
		} else {
			Bits.shortsToBytes(shorts, off, buffer, pos, rem);
			off += rem;
			len -= rem;
			flush(pos + (rem * 2));
			while(len > l2) {
				Bits.shortsToBytes(shorts, off, buffer, 0, l2);
				off += l2;
				len -= l2;
				flush(l2t);
			}
			Bits.shortsToBytes(shorts, off, buffer, 0, len);
			pos = len * 2;
		}
	}

	public void writeChars(char[] chars, int off, int len) throws IOException
	{
		int rem = (buffer.length - pos) >>> 1;
		if(rem > len) {
			Bits.charsToBytes(chars, off, buffer, pos, len);
			pos += len * 2;
		} else {
			Bits.charsToBytes(chars, off, buffer, pos, rem);
			off += rem;
			len -= rem;
			flush(pos + (rem * 2));
			while(len > l2) {
				Bits.charsToBytes(chars, off, buffer, 0, l2);
				off += l2;
				len -= l2;
				flush(l2t);
			}
			Bits.charsToBytes(chars, off, buffer, 0, len);
			pos = len * 2;
		}
	}

	public void writeInts(int[] ints, int off, int len) throws IOException
	{
		int rem = (buffer.length - pos) >>> 2;
		if(rem > len) {
			Bits.intsToBytes(ints, off, buffer, pos, len);
			pos += len * 4;
		} else {
			Bits.intsToBytes(ints, off, buffer, pos, rem);
			off += rem;
			len -= rem;
			flush(pos + (rem * 4));
			while(len > l4) {
				Bits.intsToBytes(ints, off, buffer, 0, l4);
				off += l4;
				len -= l4;
				flush(l4t);
			}
			Bits.intsToBytes(ints, off, buffer, 0, len);
			pos = len * 4;
		}
	}

	public void writeLongs(long[] longs, int off, int len) throws IOException
	{
		int rem = (buffer.length - pos) >>> 3;
		if(rem > len) {
			Bits.longsToBytes(longs, off, buffer, pos, len);
			pos += len * 8;
		} else {
			Bits.longsToBytes(longs, off, buffer, pos, rem);
			off += rem;
			len -= rem;
			flush(pos + (rem * 8));
			while(len > l8) {
				Bits.longsToBytes(longs, off, buffer, 0, l8);
				off += l8;
				len -= l8;
				flush(l8t);
			}
			Bits.longsToBytes(longs, off, buffer, 0, len);
			pos = len * 8;
		}
	}

	public void rewind(long pos) throws IOException
	{
		this.rewind(pos);
	}

	public void skip(long pos) throws IOException
	{
		this.seek(pos);
	}

	public void seek(long pos) throws IOException
	{
		os.seek(pos);
	}

	public IIncomingStream getIncoming() throws UnsupportedOperationException, IOException
	{
		return os.getIncoming();
	}
	
	public static final int test()
	{
		byte[] arr = new byte[5000];
		return IOutgoingStream.testWrapper(arr, new BufferedOutgoingStream(new ByteArrayOutgoingStream(arr), new byte[15]));
	}

}

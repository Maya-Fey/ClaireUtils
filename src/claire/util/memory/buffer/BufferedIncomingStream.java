package claire.util.memory.buffer;

import java.io.IOException;

import claire.util.memory.Bits;
import claire.util.standards.io.IIncomingStream;

public class BufferedIncomingStream
	   implements IIncomingStream {
	
	private final IIncomingStream is;
	private final byte[] buffer;
	
	private final int l2,
					  l4,
					  l8;
	
	private int pos;
	
	public BufferedIncomingStream(IIncomingStream is, byte[] buffer)
	{
		this.is = is;
		this.buffer = buffer;
		l2 = buffer.length >>> 1;
		l4 = buffer.length >>> 2;
		l8 = buffer.length >>> 3;
	}
	
	public BufferedIncomingStream(IIncomingStream is, int size)
	{
		this.is = is;
		this.buffer = new byte[size];
		l2 = size >>> 1;
		l4 = size >>> 2;
		l8 = size >>> 3;
	}

	public void close() throws IOException
	{
		is.close();
	}

	private final void ensure(int amt) throws IOException
	{
		int len = buffer.length - pos;
		if(len > amt) {
			System.arraycopy(buffer, pos, buffer, 0, len);
			is.readBytes(buffer, len, pos);
			pos = 0;
		}
	}
	
	private final void next() throws IOException
	{
		if(pos == buffer.length)
			is.readBytes(buffer);
	}
	
	private final void flush() throws IOException
	{
		int len = buffer.length - pos;
		System.arraycopy(buffer, pos, buffer, 0, len);
		is.readBytes(buffer, len, pos);
		pos = 0;
	}
	
	public boolean readBool() throws IOException
	{
		next();
		return buffer[pos++] == 1;
	}

	public byte readByte() throws IOException
	{
		next();
		return buffer[pos++];
	}

	public short readShort() throws IOException
	{
		ensure(2);
		return (short) (((buffer[pos++] & 0x00FF)) |
			             (buffer[pos++] & 0x00FF) << 8);
	}

	public char readChar() throws IOException
	{
		ensure(2);
		return (char) (((buffer[pos++] & 0x00FF)) |
			            (buffer[pos++] & 0x00FF) << 8);
	}

	public int readInt() throws IOException
	{
		ensure(4);
		return (int) (((buffer[pos++] & 0x00FF))       |
			          ((buffer[pos++] & 0x00FF) << 8 ) |
			          ((buffer[pos++] & 0x00FF) << 16) |
			          ((buffer[pos++] & 0x00FF) << 24));
	}

	public long readLong() throws IOException
	{
		ensure(8);
		return (long) (((buffer[pos++] & 0x00FF))       |
			           ((buffer[pos++] & 0x00FF) << 8 ) |
			           ((buffer[pos++] & 0x00FF) << 16) |
			           ((buffer[pos++] & 0x00FF) << 24) |
			           ((buffer[pos++] & 0x00FF) << 32) |
			           ((buffer[pos++] & 0x00FF) << 40) |
			           ((buffer[pos++] & 0x00FF) << 48) |
			           ((buffer[pos++] & 0x00FF) << 56));
	}

	public void readBools(boolean[] out, int off, int amt) throws IOException
	{
		int len = buffer.length - pos;
		if(len > amt) {
			while(amt-- > 0)
				out[off++] = buffer[pos++] == 1;
		} else {
			amt -= len;
			while(len-- > 0)
				out[off++] = buffer[pos++] == 1;
			int i = 0;
			while(amt > buffer.length) {
				is.readBytes(buffer);
				i = 0;
				len = buffer.length;
				while(len-- > 0)
					out[off++] = buffer[i++] == 1;
				amt -= buffer.length;
			}
			is.readBytes(buffer);
			while(amt-- > 0)
				out[off++] = buffer[pos++] == 1;
		}
	}

	public void readNibbles(byte[] out, int off, int amt) throws IOException
	{
		amt >>>= 1;
		int len = buffer.length - pos;
		if(len > amt) {
			while(amt-- > 0) {
				out[off++] = (byte) ((buffer[pos  ] & 0xFF) >>> 4); 
				out[off++] = (byte)  (buffer[pos++] & 0x0F); 
			}
		} else {
			amt -= len;
			while(len-- > 0) {
				out[off++] = (byte) ((buffer[pos  ] & 0xFF) >>> 4); 
				out[off++] = (byte)  (buffer[pos++] & 0x0F); 
			}
			int i = 0;
			while(amt > buffer.length) {
				is.readBytes(buffer);
				i = 0;
				len = buffer.length;
				while(len-- > 0) {
					out[off++] = (byte) ((buffer[i  ] & 0xFF) >>> 4); 
					out[off++] = (byte)  (buffer[i++] & 0x0F); 
				}
				amt -= buffer.length;
			}
			is.readBytes(buffer);
			while(amt-- > 0) {
				out[off++] = (byte) ((buffer[pos  ] & 0xFF) >>> 4); 
				out[off++] = (byte)  (buffer[pos++] & 0x0F); 
			}
		}
	}

	public void readBytes(byte[] out, int off, int bytes) throws IOException
	{
		int len = buffer.length - pos;
		if(len > bytes) {
			System.arraycopy(buffer, pos, out, off, bytes);
			pos += bytes;
		} else {
			System.arraycopy(buffer, pos, out, off, len);
			off += len;
			bytes -= len;
			while(bytes > buffer.length) {
				is.readBytes(buffer);
				System.arraycopy(buffer, pos, out, off, buffer.length);
				off += buffer.length;
				bytes -= buffer.length;
			}
			is.readBytes(buffer);
			System.arraycopy(buffer, pos, out, off, bytes);
			pos = bytes;
		}
	}

	public void readShorts(short[] shorts, int off, int amt) throws IOException
	{
		int len = (buffer.length - pos) >> 1;
		if(len > amt) {
			Bits.bytesToShorts(buffer, pos, shorts, off, amt);
			pos += amt * 2;
		} else {
			Bits.bytesToShorts(buffer, pos, shorts, off, len);
			off += len;
			amt -= len;
			while(amt > buffer.length) {
				flush();
				Bits.bytesToShorts(buffer, pos, shorts, off, l2);
				off += buffer.length;
				amt -= buffer.length;
			}
			flush();
			Bits.bytesToShorts(buffer, pos, shorts, off, amt);
			pos = amt * 2;
		}
	}

	public void readChars(char[] chars, int off, int amt) throws IOException
	{
		int len = (buffer.length - pos) >> 1;
		if(len > amt) {
			Bits.bytesToChars(buffer, pos, chars, off, amt);
			pos += amt * 2;
		} else {
			Bits.bytesToChars(buffer, pos, chars, off, len);
			off += len;
			amt -= len;
			while(amt > buffer.length) {
				flush();
				Bits.bytesToChars(buffer, pos, chars, off, l2);
				off += buffer.length;
				amt -= buffer.length;
			}
			flush();
			Bits.bytesToChars(buffer, pos, chars, off, amt);
			pos = amt * 2;
		}
	}

	public void readInts(int[] ints, int off, int amt) throws IOException
	{
		int len = (buffer.length - pos) >> 1;
		if(len > amt) {
			Bits.bytesToInts(buffer, pos, ints, off, amt);
			pos += amt * 4;
		} else {
			Bits.bytesToInts(buffer, pos, ints, off, len);
			off += len;
			amt -= len;
			while(amt > buffer.length) {
				flush();
				Bits.bytesToInts(buffer, pos, ints, off, l4);
				off += buffer.length;
				amt -= buffer.length;
			}
			flush();
			Bits.bytesToInts(buffer, pos, ints, off, amt);
			pos = amt * 4;
		}
	}

	public void readLongs(long[] longs, int off, int amt) throws IOException
	{
		int len = (buffer.length - pos) >> 1;
		if(len > amt) {
			Bits.bytesToLongs(buffer, pos, longs, off, amt);
			pos += amt * 8;
		} else {
			Bits.bytesToLongs(buffer, pos, longs, off, len);
			off += len;
			amt -= len;
			while(amt > buffer.length) {
				flush();
				Bits.bytesToLongs(buffer, pos, longs, off, l8);
				off += buffer.length;
				amt -= buffer.length;
			}
			flush();
			Bits.bytesToLongs(buffer, pos, longs, off, amt);
			pos = amt * 8;
		}
	}

	public void rewind(long len) throws IOException
	{
		is.rewind(len);
		is.readBytes(buffer);
	}

	public void skip(long pos) throws IOException
	{
		is.skip(pos);
		is.readBytes(buffer);
	}

	public void seek(long pos) throws IOException
	{
		is.seek(pos);
		is.readBytes(buffer);
	}

	public long available() throws IOException
	{
		return is.available();
	}

}

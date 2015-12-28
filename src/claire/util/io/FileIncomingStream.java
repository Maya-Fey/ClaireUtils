package claire.util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import claire.util.memory.Bits;
import claire.util.standards.io.IIncomingStream;

public class FileIncomingStream 
	   implements IIncomingStream {
	
	private final RandomAccessFile stream;
	
	private final byte[] buffer = new byte[8];
	
	public FileIncomingStream(File file) throws FileNotFoundException
	{
		this.stream = new RandomAccessFile(file, "r");
	}

	public void close() throws IOException
	{
		stream.close();
	}

	public byte readByte() throws IOException
	{
		return stream.readByte();
	}

	public short readShort() throws IOException
	{
		if(stream.read(buffer, 0, 2) != 2)
			throw new java.io.IOException("Bytes could not be read");
		return Bits.shortFromBytes(buffer, 0);
	}

	public char readChar() throws IOException
	{
		if(stream.read(buffer, 0, 2) != 2)
			throw new java.io.IOException("Bytes could not be read");
		return Bits.charFromBytes(buffer, 0);
	}

	public int readInt() throws IOException
	{
		if(stream.read(buffer, 0, 4) != 4)
			throw new java.io.IOException("Bytes could not be read");
		return Bits.intFromBytes(buffer, 0);
	}

	public long readLong() throws IOException
	{
		if(stream.read(buffer, 0, 8) != 8)
			throw new java.io.IOException("Bytes could not be read");
		return Bits.longFromBytes(buffer, 0);
	}

	public void readBytes(byte[] out, int off, int bytes) throws IOException
	{
		if(stream.read(out, off, bytes) != bytes)
			throw new java.io.IOException("Bytes could not be read");
	}

	public void readShorts(short[] shorts, int off, int amt) throws IOException
	{
		while(amt >= 4) {
			if(stream.read(buffer, 0, 8) != 8)
				throw new java.io.IOException("Bytes could not be read");
			Bits.bytesToShorts(buffer, 0, shorts, off, 4);
			off += 4;
			amt -= 4;
		}
		if(amt > 0) {
			int bytes = amt << 1;
			if(stream.read(buffer, 0, bytes) != bytes)
				throw new java.io.IOException("Bytes could not be read");
			Bits.bytesToShorts(buffer, 0, shorts, off, amt);
		}
	}

	public void readChars(char[] chars, int off, int amt) throws IOException
	{
		while(amt >= 4) {
			if(stream.read(buffer, 0, 8) != 8)
				throw new java.io.IOException("Bytes could not be read");
			Bits.bytesToChars(buffer, 0, chars, off, 4);
			off += 4;
			amt -= 4;
		}
		if(amt > 0) {
			int bytes = amt << 1;
			if(stream.read(buffer, 0, bytes) != bytes)
				throw new java.io.IOException("Bytes could not be read");
			Bits.bytesToChars(buffer, 0, chars, off, amt);
		}
	}

	public void readInts(int[] ints, int off, int amt) throws IOException
	{
		while(amt >= 2) {
			if(stream.read(buffer, 0, 8) != 8)
				throw new java.io.IOException("Bytes could not be read");
			Bits.bytesToInts(buffer, 0, ints, off, 2);
			off += 2;
			amt -= 2;
		}
		if(amt > 0) {
			if(stream.read(buffer, 0, 4) != 4)
				throw new java.io.IOException("Bytes could not be read");
			Bits.bytesToInts(buffer, 0, ints, off, amt);
		}
	}

	public void readLongs(long[] longs, int off, int amt) throws IOException
	{
		while(amt-- > 0) 
			longs[off++] = this.readLong();
	}

	public void rewind(long pos) throws IOException
	{
		this.seek(stream.getFilePointer() - pos);
	}

	public void skip(long pos) throws IOException
	{
		this.seek(stream.getFilePointer() + pos);
	}

	public void seek(long pos) throws IOException
	{
		stream.seek(pos);
	}

	public long available() throws IOException
	{
		return stream.length() - stream.getFilePointer();
	}

}

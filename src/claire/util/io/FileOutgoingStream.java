package claire.util.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import claire.util.memory.Bits;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class FileOutgoingStream 
	   implements IOutgoingStream {
	
	private final File file;
	private final RandomAccessFile stream;
	private final byte[] buffer = new byte[8];
	
	public FileOutgoingStream(File file) throws IOException
	{
		this.stream = new RandomAccessFile(file, "rw");
		stream.getChannel().truncate(0);
		this.file = file;
	}
	
	public FileOutgoingStream(File file, boolean append) throws IOException
	{
		this.stream = new RandomAccessFile(file, "rw");
		if(append)
			stream.seek(file.length());
		else
			stream.getChannel().truncate(0);
		this.file = file;
	}

	public void close() throws IOException
	{
		stream.close();
	}

	public void writeByte(byte data) throws IOException
	{
		stream.writeByte(data);
	}

	public void writeShort(short data) throws IOException
	{
		Bits.shortToBytes(data, buffer, 0);
		stream.write(buffer, 0, 2);
	}

	public void writeChar(char data) throws IOException
	{
		Bits.charToBytes(data, buffer, 0);
		stream.write(buffer, 0, 2);
	}

	public void writeInt(int data) throws IOException
	{
		Bits.intToBytes(data, buffer, 0);
		stream.write(buffer, 0, 4);
	}

	public void writeLong(long data) throws IOException
	{
		Bits.longToBytes(data, buffer, 0);
		stream.write(buffer, 0, 8);
	}

	public void writeBytes(byte[] bytes, int off, int len) throws IOException
	{
		stream.write(bytes, off, len);
	}

	public void writeShorts(short[] shorts, int off, int len) throws IOException
	{
		for(int i = off; i < len; i++)
			this.writeShort(shorts[i]);
	}

	public void writeChars(char[] chars, int off, int len) throws IOException
	{
		for(int i = off; i < len; i++)
			this.writeChar(chars[i]);
	}

	public void writeInts(int[] ints, int off, int len) throws IOException
	{
		for(int i = off; i < len; i++)
			this.writeInt(ints[i]);
	}

	public void writeLongs(long[] longs, int off, int len) throws IOException
	{
		for(int i = off; i < len; i++)
			this.writeLong(longs[i]);
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

	public IIncomingStream getIncoming() throws UnsupportedOperationException, IOException
	{
		return new FileIncomingStream(file);
	}

	public void writeBool(boolean data) throws IOException
	{
		this.writeByte((byte) (data ? 1 : 0)); 
	}

	public void writeBools(boolean[] bools, int off, int len) throws IOException
	{
		while(len >= 8) {
			int i = 0; 
			while(i < 8)
				buffer[i++] = (byte) (bools[off++] ? 1 : 0);
			stream.write(buffer, 0, 8);
			len -= 8;
		}
		if(len > 0) {
			int i = 0; 
			while(i < 8)
				buffer[i++] = (byte) (bools[off++] ? 1 : 0);
			stream.write(buffer, 0, len);
		}
	}

	public void writeNibbles(byte[] nibbles, int off, int len) throws IOException
	{
		len >>>= 1;
		while(len >= 8) {
			int i = 0; 
			while(i < 8)
				buffer[i++] = (byte) (((nibbles[off++] & 0xF) << 4) |
					    		       (nibbles[off++] & 0xF));
			stream.write(buffer, 0, 8);
			len -= 8;
		}
		if(len > 0) {
			int i = 0; 
			while(i < 8)
				buffer[i++] = (byte) (((nibbles[off++] & 0xF) << 4) |
									   (nibbles[off++] & 0xF));
			stream.write(buffer, 0, len);
		}
	}

}

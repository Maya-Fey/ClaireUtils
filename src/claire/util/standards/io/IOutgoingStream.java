package claire.util.standards.io;

import java.io.Closeable;
import java.io.IOException;

import claire.util.encoding.CString;
import claire.util.standards.IPersistable;

public interface IOutgoingStream 
	   extends Closeable {
	
	void writeByte(byte data) throws IOException;
	void writeShort(short data) throws IOException;
	void writeChar(char data) throws IOException;
	void writeInt(int data) throws IOException;
	void writeLong(long data) throws IOException;
	
	void writeBytes(byte[] bytes, int off, int len) throws IOException;
	void writeShorts(short[] shorts, int off, int len) throws IOException;
	void writeChars(char[] charss, int off, int len) throws IOException;
	void writeInts(int[] ints, int off, int len) throws IOException;
	void writeLongs(long[] longs, int off, int len) throws IOException;
	
	void rewind(long pos) throws IOException;
	void skip(long pos) throws IOException;
	void seek(long pos) throws IOException;
	
	IIncomingStream getIncoming() throws UnsupportedOperationException, IOException;
	
	default void writeString(CString s) throws IOException
	{
		this.writeChars(s.array());
	}
	
	default void writeBytes(byte[] bytes) throws IOException
	{
		this.writeBytes(bytes, 0, bytes.length);
	}
	
	default void writeShorts(short[] shorts) throws IOException
	{
		this.writeShorts(shorts, 0, shorts.length);
	}
	
	default void writeChars(char[] chars) throws IOException
	{
		this.writeChars(chars, 0, chars.length);
	}
	
	default void writeInts(int[] ints) throws IOException
	{
		this.writeInts(ints, 0, ints.length);
	}
	
	default void writeLongs(long[] longs) throws IOException
	{
		this.writeLongs(longs, 0, longs.length);
	}
	
	default void persist(IPersistable<?> obj) throws IOException
	{
		obj.export(this);
	}
	
	default void writeByteArr(byte[] arr) throws IOException
	{
		this.writeInt(arr.length);
		this.writeBytes(arr, 0, arr.length);
	}
	
	default void writeShortArr(short[] arr) throws IOException
	{
		this.writeInt(arr.length);
		this.writeShorts(arr, 0, arr.length);
	}
	
	default void writeCharArr(char[] arr) throws IOException
	{
		this.writeInt(arr.length);
		this.writeChars(arr, 0, arr.length);
	}
	
	default void writeIntArr(int[] arr) throws IOException
	{
		this.writeInt(arr.length);
		this.writeInts(arr, 0, arr.length);
	}
	
	default void writeLongArr(long[] arr) throws IOException
	{
		this.writeInt(arr.length);
		this.writeLongs(arr, 0, arr.length);
	}

}

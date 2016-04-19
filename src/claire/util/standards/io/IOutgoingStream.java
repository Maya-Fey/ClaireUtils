package claire.util.standards.io;

import java.io.Closeable;
import java.io.IOException;

import claire.util.crypto.rng.RandUtils;
import claire.util.encoding.CString;
import claire.util.logging.Log;
import claire.util.memory.buffer.ByteArrayOutgoingStream;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IPersistable;

public interface IOutgoingStream 
	   extends Closeable {
	
	void writeBool(boolean data) throws IOException;
	void writeByte(byte data) throws IOException;
	void writeShort(short data) throws IOException;
	void writeChar(char data) throws IOException;
	void writeInt(int data) throws IOException;
	void writeLong(long data) throws IOException;
	
	void writeBools(boolean[] bools, int off, int len) throws IOException;
	void writeNibbles(byte[] nibbles, int off, int len) throws IOException;
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
	
	default void writeBools(boolean[] bools) throws IOException
	{
		this.writeBools(bools, 0, bools.length);
	}
	
	default void writeNibbles(byte[] nibbles) throws IOException
	{
		this.writeNibbles(nibbles, 0, nibbles.length);
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
	
	default void writeBoolArr(boolean[] arr) throws IOException
	{
		this.writeInt(arr.length);
		this.writeBools(arr);
	}
	
	default void writeNibbleArr(byte[] arr) throws IOException
	{
		this.writeInt(arr.length);
		this.writeNibbleArr(arr);
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
	
	public static int testWrapper(byte[] arr, IOutgoingStream os2)
	{
		try {
			byte[] arr2 = new byte[arr.length];
			ByteArrayOutgoingStream os3 = new ByteArrayOutgoingStream(arr2);
			boolean[] bools = new boolean[57];
			byte[] bytes = new byte[57];
			short[] shorts = new short[57];
			char[] chars = new char[57];
			int[] ints = new int[57];
			long[] longs = new long[57];
			RandUtils.fillArr(bools);
			RandUtils.fillArr(bytes);
			RandUtils.fillArr(shorts);
			RandUtils.fillArr(chars);
			RandUtils.fillArr(ints);
			RandUtils.fillArr(longs);
			for(boolean b : bools) {
				os2.writeBool(b);
				os3.writeBool(b);
			}
			for(byte b : bytes) {
				os2.writeByte(b);
				os3.writeByte(b);
			}
			for(short b : shorts) {
				os2.writeShort(b);
				os3.writeShort(b);
			}
			for(char b : chars) {
				os2.writeChar(b);
				os3.writeChar(b);
			}
			for(int b : ints) {
				os2.writeInt(b);
				os3.writeInt(b);
			}
			for(long b : longs) {
				os2.writeLong(b);
				os3.writeLong(b);
			}
			os2.writeBools(bools);
			os2.writeBytes(bytes);
			os2.writeShorts(shorts);
			os2.writeChars(chars);
			os2.writeInts(ints);
			os2.writeLongs(longs);
			os3.writeBools(bools);
			os3.writeBytes(bytes);
			os3.writeShorts(shorts);
			os3.writeChars(chars);
			os3.writeInts(ints);
			os3.writeLongs(longs);
			os2.close();
			os3.close();
			if(!ArrayUtil.equals(arr, arr2)) {
				Log.err.println("Wrapper around output stream did not have identical output to unwrapped.");
				return 1;
			} else
				return 0;
		} catch(Exception e) {
			Log.err.println("An unexpected " + e.getClass().getSimpleName() + ": " + e.getMessage() + " occured while testing  " + os2.getClass().getSimpleName());
			e.printStackTrace();
			return 1;
		}
	}

}

package claire.util.standards;

import java.io.File;
import java.io.IOException;

import claire.util.io.Factory;
import claire.util.io.FileOutgoingStream;
import claire.util.logging.Log;
import claire.util.memory.Bits;
import claire.util.memory.buffer.ByteArrayIncomingStream;
import claire.util.memory.buffer.ByteArrayOutgoingStream;
import claire.util.standards.io.IOutgoingStream;

public interface IPersistable<T> {
	
	void export(IOutgoingStream stream) throws IOException;
	void export(byte[] bytes, int offset);
	int exportSize();
	Factory<T> factory();
	
	default byte[] export() 
	{
		byte[] bytes = new byte[(int) this.exportSize()];
		this.export(bytes);
		return bytes;
	}
	
	default void export(byte[] bytes) 
	{
		this.export(bytes, 0);
	}
	
	default void export(File file) throws IOException
	{
		FileOutgoingStream stream = new FileOutgoingStream(file);
		stream.persist(this);
		stream.close();
	}
	
	public static <T extends IPersistable<T> & IUUID<T>> int test(T t)
	{
		try {
			int e = 0;
			Factory<T> f = t.factory();
			byte[] bytes = new byte[t.exportSize() + 20];
			t.export(bytes, 20);
			T t2 = f.resurrect(bytes, 20);
			if(!t.equals(t2)) {
				Log.err.println("Persisting and resurrecting from raw bytes did not yield the same object for class " + t.getClass().getSimpleName());
				e++;
			}
			boolean b = true;
			try {
				t.export(bytes, 21);
			} catch (Exception ex) {
				b = false;
			}
			if(b) {
				Log.err.println("Succeeded in persisting " + t.getClass().getSimpleName() + " with insufficient space, likely misreported required size");
				e++;
			}
			ByteArrayOutgoingStream os = new ByteArrayOutgoingStream(bytes);
			t.export(os);
			T t3 = f.resurrect(new ByteArrayIncomingStream(bytes));
			if(!t.equals(t3)) {
				Log.err.println("Persisting and resurrecting from stream did not yield the same object for class " + t.getClass().getSimpleName());
				e++;
			}
			if(!t2.equals(t3)) {
				Log.err.println("Persisting and resurrecting from stream and raw bytes did not yield the same object for class " + t.getClass().getSimpleName());
				e++;
			}
			return e;
		} catch (Exception e) {
			Log.err.println("An unexpected " + e.getClass().getSimpleName() + ": " + e.getMessage() + " occured while testing the persistability of " + t.getClass().getSimpleName());
			e.printStackTrace();
			return 1;
		}
	}
	
	public static int persistByte(byte b, byte[] stream, int offset)
	{
		stream[offset++] = b;
		return offset;
	}
	
	public static int persistShort(short b, byte[] stream, int offset)
	{
		Bits.shortToBytes(b, stream, offset);
		return offset + 2;
	}
	
	public static int persistChar(char b, byte[] stream, int offset)
	{
		Bits.charToBytes(b, stream, offset);
		return offset + 2;
	}
	
	public static int persistInt(int b, byte[] stream, int offset)
	{
		Bits.intToBytes(b, stream, offset);
		return offset + 4;
	}
	
	public static int persistLong(long b, byte[] stream, int offset)
	{
		Bits.longToBytes(b, stream, offset);
		return offset + 8;
	}
	
	public static int persistBytes(byte[] bytes, byte[] stream, int offset)
	{
		System.arraycopy(bytes, 0, stream, offset, bytes.length);
		return offset + bytes.length;
	}
	
	public static int persistShorts(short[] shorts, byte[] stream, int offset)
	{
		Bits.shortsToBytes(shorts, 0, stream, offset, shorts.length);
		return offset + (shorts.length * 2);
	}
	
	public static int persistChars(char[] chars, byte[] stream, int offset)
	{
		Bits.charsToBytes(chars, 0, stream, offset, chars.length);
		return offset + (chars.length * 2);
	}
	
	public static int persistInts(int[] ints, byte[] stream, int offset)
	{
		Bits.intsToBytes(ints, 0, stream, offset, ints.length);
		return offset + (ints.length * 4);
	}
	
	public static int persistLongs(long[] longs, byte[] stream, int offset)
	{
		Bits.longsToBytes(longs, 0, stream, offset, longs.length);
		return offset + (longs.length * 8);
	}
	
	public static byte readByte(byte[] bytes, int offset)
	{
		return bytes[offset];
	}
	
	public static short readShort(byte[] bytes, int offset)
	{
		return Bits.shortFromBytes(bytes, offset);
	}
	
	public static char readChar(byte[] bytes, int offset)
	{
		return Bits.charFromBytes(bytes, offset);
	}
	
	public static int readInt(byte[] bytes, int offset)
	{
		return Bits.intFromBytes(bytes, offset);
	}
	
	public static long readLong(byte[] bytes, int offset)
	{
		return Bits.longFromBytes(bytes, offset);
	}
	
	public static byte[] readBytes(byte[] bytes, int offset, int len)
	{
		byte[] n = new byte[len];
		System.arraycopy(bytes, offset, n, 0, len);
		return n;
	}
	
	public static short[] readShorts(byte[] bytes, int offset, int len)
	{
		short[] n = new short[len];
		Bits.bytesToShorts(bytes, offset, n, 0, len);
		return n;
	}
	
	public static char[] readChars(byte[] bytes, int offset, int len)
	{
		char[] n = new char[len];
		Bits.bytesToChars(bytes, offset, n, 0, len);
		return n;
	}
	
	public static int[] readInts(byte[] bytes, int offset, int len)
	{
		int[] n = new int[len];
		Bits.bytesToInts(bytes, offset, n, 0, len);
		return n;
	}
	
	public static long[] readLongs(byte[] bytes, int offset, int len)
	{
		long[] n = new long[len];
		Bits.bytesToLongs(bytes, offset, n, 0, len);
		return n;
	}
	
	public static int readBytes(byte[] to, byte[] bytes, int offset)
	{
		System.arraycopy(bytes, offset, to, 0, to.length);
		return offset + to.length;
	}
	
	public static int readShorts(short[] to, byte[] bytes, int offset)
	{
		Bits.bytesToShorts(bytes, offset, to, 0, to.length);
		return offset + (to.length * 2);
	}
	
	public static int readChars(char[] to, byte[] bytes, int offset)
	{
		Bits.bytesToChars(bytes, offset, to, 0, to.length);
		return offset + (to.length * 2);
	}
	
	public static int readInts(int[] to, byte[] bytes, int offset)
	{
		Bits.bytesToInts(bytes, offset, to, 0, to.length);
		return offset + (to.length * 4);
	}
	
	public static int readLongs(long[] to, byte[] bytes, int offset)
	{
		Bits.bytesToLongs(bytes, offset, to, 0, to.length);
		return offset + (to.length * 8);
	}
	
	
}

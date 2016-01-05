package claire.util.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import claire.util.memory.Bits;

public final class IOUtils {

	public static byte[] toBytes(File in) throws IOException
	{
		byte[] file = new byte[(int) in.length()];
		DataInputStream data = new DataInputStream(new FileInputStream(in));
		try {
			data.readFully(file);
		} finally {
			data.close();
		}
		return file;
	}
	
	public static void clearDirectoryLight(File in)
	{
		for(File file : in.listFiles())
			if(file.isFile())
				file.delete();
	}
	
	public static void clearDirectory(File in)
	{
		for(File file : in.listFiles())
			if(file.isDirectory()) 
				clearDirectory(file);
			else
				file.delete();
		in.delete();
	}
	
	public static FileOutputStream getOut(FileInputStream fin) throws IOException
	{
		return new FileOutputStream(fin.getFD());
	}

	public static FileInputStream getIn(FileOutputStream fin) throws IOException
	{
		return new FileInputStream(fin.getFD());
	}
	
	public static int writeArr(byte[] arr, byte[] data, int off)
	{
		Bits.intToBytes(arr.length, data, off); 
		System.arraycopy(arr, 0, data, off += 4, arr.length); off += arr.length;
		return off;
	}
	
	public static int writeArr(short[] arr, byte[] data, int off)
	{
		Bits.intToBytes(arr.length, data, off); 
		Bits.shortsToBytes(arr, 0, data, off += 4, arr.length); off += arr.length * 2;
		return off;
	}
	
	public static int writeArr(char[] arr, byte[] data, int off)
	{
		Bits.intToBytes(arr.length, data, off); 
		Bits.charsToBytes(arr, 0, data, off += 4, arr.length); off += arr.length * 2;
		return off;
	}
	
	public static int writeArr(int[] arr, byte[] data, int off)
	{
		Bits.intToBytes(arr.length, data, off); 
		Bits.intsToBytes(arr, 0, data, off += 4, arr.length); off += arr.length * 4;
		return off;
	}
	
	public static int writeArr(long[] arr, byte[] data, int off)
	{
		Bits.intToBytes(arr.length, data, off); 
		Bits.longsToBytes(arr, 0, data, off += 4, arr.length); off += arr.length * 8;
		return off;
	}
	
	public static byte[] readByteArr(byte[] bytes, int off)
	{
		byte[] arr = new byte[Bits.intFromBytes(bytes, off)];
		System.arraycopy(bytes, off += 4, arr, 0, arr.length);
		return arr;
	}
	
	public static short[] readShortArr(byte[] bytes, int off)
	{
		short[] arr = new short[Bits.intFromBytes(bytes, off)];
		Bits.bytesToShorts(bytes, off += 4, arr, 0, arr.length);
		return arr;
	}
	
	public static char[] readCharArr(byte[] bytes, int off)
	{
		char[] arr = new char[Bits.intFromBytes(bytes, off)];
		Bits.bytesToChars(bytes, off += 4, arr, 0, arr.length);
		return arr;
	}
	
	public static int[] readIntArr(byte[] bytes, int off)
	{
		int[] arr = new int[Bits.intFromBytes(bytes, off)];
		Bits.bytesToInts(bytes, off += 4, arr, 0, arr.length);
		return arr;
	}
	
	public static long[] readLongArr(byte[] bytes, int off)
	{
		long[] arr = new long[Bits.intFromBytes(bytes, off)];
		Bits.bytesToLongs(bytes, off += 4, arr, 0, arr.length);
		return arr;
	}
}

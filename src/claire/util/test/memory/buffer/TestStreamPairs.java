package claire.util.test.memory.buffer;

import java.io.File;

import claire.util.crypto.rng.RandUtils;
import claire.util.crypto.rng.primitive.JRandom;
import claire.util.io.FileBidirectionalStream;
import claire.util.io.FileOutgoingStream;
import claire.util.logging.Log;
import claire.util.memory.buffer.ByteArrayOutgoingStream;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;
import claire.util.test.Test;

public class TestStreamPairs {
	
	private static void runTest(IOutgoingStream out) throws Exception
	{
		JRandom rng = new JRandom();
		int[] sizes = new int[10];
		for(int i = 0; i < 10; i++)
			sizes[i] = rng.nextInt(128);
		
		byte[] indbytes = new byte[sizes[0]];
		short[] indshorts = new short[sizes[1]];
		char[] indchars = new char[sizes[2]];
		int[] indints = new int[sizes[3]];
		long[] indlongs = new long[sizes[4]];
		
		byte[] bytes = new byte[sizes[5]];
		short[] shorts = new short[sizes[6]];
		char[] chars = new char[sizes[7]];
		int[] ints = new int[sizes[8]];
		long[] longs = new long[sizes[9]];
		
		RandUtils.fillArr(indbytes, rng);
		RandUtils.fillArr(indshorts, rng);
		RandUtils.fillArr(indchars, rng);
		RandUtils.fillArr(indints, rng);
		RandUtils.fillArr(indlongs, rng);
		RandUtils.fillArr(bytes, rng);
		RandUtils.fillArr(shorts, rng);
		RandUtils.fillArr(chars, rng);
		RandUtils.fillArr(ints, rng);
		RandUtils.fillArr(longs, rng);
		
		for(byte b : indbytes)
			out.writeByte(b);
		for(short b : indshorts)
			out.writeShort(b);
		for(char b : indchars)
			out.writeChar(b);
		for(int b : indints)
			out.writeInt(b);
		for(long b : indlongs)
			out.writeLong(b);
		
		out.writeBytes(bytes);
		out.writeShorts(shorts);
		out.writeChars(chars);
		out.writeInts(ints);
		out.writeLongs(longs);
		
		IIncomingStream in = out.getIncoming();
		out.close();
		
		in.skip(2);
		in.readByte();
		in.rewind(3);
		
		byte[] indbytesver = new byte[sizes[0]];
		short[] indshortsver = new short[sizes[1]];
		char[] indcharsver = new char[sizes[2]];
		int[] indintsver = new int[sizes[3]];
		long[] indlongsver = new long[sizes[4]];
		
		byte[] bytesver = new byte[sizes[5]];
		short[] shortsver = new short[sizes[6]];
		char[] charsver = new char[sizes[7]];
		int[] intsver = new int[sizes[8]];
		long[] longsver = new long[sizes[9]];
		
		for(int i = 0; i < indbytesver.length; i++)
			indbytesver[i] = in.readByte();
		for(int i = 0; i < indshortsver.length; i++)
			indshortsver[i] = in.readShort();
		for(int i = 0; i < indcharsver.length; i++)
			indcharsver[i] = in.readChar();
		for(int i = 0; i < indintsver.length; i++)
			indintsver[i] = in.readInt();
		for(int i = 0; i < indlongsver.length; i++)
			indlongsver[i] = in.readLong();
		
		in.readBytes(bytesver);
		in.readShorts(shortsver);
		in.readChars(charsver);
		in.readInts(intsver);
		in.readLongs(longsver);
		
		for(int i = 0; i < indbytesver.length; i++)
			if(indbytesver[i] != indbytes[i]) {
				Test.reportError("Error while reading from stream: individual bytes don't match source bytes");
				return;
			}
		for(int i = 0; i < indshortsver.length; i++)
			if(indshortsver[i] != indshorts[i]) {
				Test.reportError("Error while reading from stream: individual shorts don't match source shorts");
				return;
			}
		for(int i = 0; i < indcharsver.length; i++)
			if(indcharsver[i] != indchars[i]) {
				Test.reportError("Error while reading from stream: individual chars don't match source chars");
				return;
			}
		for(int i = 0; i < indintsver.length; i++)
			if(indintsver[i] != indints[i]) {
				Test.reportError("Error while reading from stream: individual ints don't match source ints");
				return;
			}
		for(int i = 0; i < indlongsver.length; i++)
			if(indlongsver[i] != indlongs[i]) {
				Test.reportError("Error while reading from stream: individual longs don't match source longs");
				return;
			}
		for(int i = 0; i < bytesver.length; i++)
			if(bytesver[i] != bytes[i]) {
				Test.reportError("Error while reading from stream: bytes don't match source bytes");
				return;
			}
		for(int i = 0; i < shortsver.length; i++)
			if(shortsver[i] != shorts[i]) {
				Test.reportError("Error while reading from stream: shorts don't match source shorts");
				return;
			}
		for(int i = 0; i < charsver.length; i++)
			if(charsver[i] != chars[i]) {
				Test.reportError("Error while reading from stream: chars don't match source chars");
				return;
			}
		for(int i = 0; i < intsver.length; i++)
			if(intsver[i] != ints[i]) {
				Test.reportError("Error while reading from stream: ints don't match source ints");
				return;
			}
		for(int i = 0; i < longsver.length; i++)
			if(longsver[i] != longs[i]) {
				Test.reportError("Error while reading from stream: longs don't match source longs");
				return;
			}
		
	}
	
	public static void runTests() throws Exception
	{
		Log.info.println("Testing byte array streams...");
		runTest(new ByteArrayOutgoingStream(new byte[4000]));
		Log.info.println("Testing file streams...");
		File file = new File("test.tmp");
		FileOutgoingStream fos = new FileOutgoingStream(file);
		runTest(fos);
		fos.close();
		Log.info.println("Testing file bidirectional stream.");
		FileBidirectionalStream fbi = new FileBidirectionalStream(file);
		runTest(fbi);
		fbi.close();
	}

}

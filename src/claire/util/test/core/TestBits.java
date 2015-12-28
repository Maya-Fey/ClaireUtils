package claire.util.test.core;

import claire.util.crypto.rng.primitive.JRandom;
import claire.util.logging.Log;
import claire.util.memory.Bits;
import claire.util.test.Test;

public class TestBits {
	
	public static void testArrayRotation()
	{
		JRandom rand = new JRandom();
		int rot = rand.nextInt(63) + 1;
		boolean[] bools = new boolean[64];
		boolean[] cbools = new boolean[64];
		for(int i = 0; i < 64; i++)
			bools[i] = rand.nextBoolean();
		System.arraycopy(bools, 0, cbools, 0, 64);
		Bits.rotateLeft(cbools, rot);
		Bits.rotateRight(cbools, rot);
		for(int i = 0; i < 64; i++)
			if(bools[i] != cbools[i]) {
				Test.reportError("Bit array rotation is not consistant");
				break;
			}
		byte[] bytes = new byte[64];
		byte[] cbytes = new byte[64];
		for(int i = 0; i < 64; i++)
			bytes[i] = rand.nextByte();
		System.arraycopy(bytes, 0, cbytes, 0, 64);
		Bits.rotateLeft(cbytes, rot);
		Bits.rotateRight(cbytes, rot);
		for(int i = 0; i < 64; i++)
			if(bytes[i] != cbytes[i]) {
				Test.reportError("Byte array rotation is not consistant");
				break;
			}
		short[] shorts = new short[64];
		short[] cshorts = new short[64];
		for(int i = 0; i < 64; i++)
			shorts[i] = rand.nextShort();
		System.arraycopy(shorts, 0, cshorts, 0, 64);
		Bits.rotateLeft(cshorts, rot);
		Bits.rotateRight(cshorts, rot);
		for(int i = 0; i < 64; i++)
			if(shorts[i] != cshorts[i]) {
				Test.reportError("Short array rotation is not consistant");
				break;
			}
		char[] chars = new char[64];
		char[] cchars = new char[64];
		for(int i = 0; i < 64; i++)
			chars[i] = (char) rand.nextShort();
		System.arraycopy(chars, 0, cchars, 0, 64);
		Bits.rotateLeft(cchars, rot);
		Bits.rotateRight(cchars, rot);
		for(int i = 0; i < 64; i++)
			if(chars[i] != cchars[i]) {
				Test.reportError("char array rotation is not consistant");
				break;
			}
		int[] ints = new int[64];
		int[] cints = new int[64];
		for(int i = 0; i < 64; i++)
			ints[i] = rand.nextInt();
		System.arraycopy(ints, 0, cints, 0, 64);
		Bits.rotateLeft(cints, rot);
		Bits.rotateRight(cints, rot);
		for(int i = 0; i < 64; i++)
			if(ints[i] != cints[i]) {
				Test.reportError("Int array rotation is not consistant");
				break;
			}
		long[] longs = new long[64];
		long[] clongs = new long[64];
		for(int i = 0; i < 64; i++)
			longs[i] = rand.nextLong();
		System.arraycopy(longs, 0, clongs, 0, 64);
		Bits.rotateLeft(clongs, rot);
		Bits.rotateRight(clongs, rot);
		for(int i = 0; i < 64; i++)
			if(longs[i] != clongs[i]) {
				Test.reportError("Long array rotation is not consistant");
				break;
			}
	}
	
	public static void testBitCompression()
	{
		JRandom rand = new JRandom();
		boolean[] bits = new boolean[256];
		for(int i = 0; i < 256; i++)
			bits[i] = rand.nextBoolean();
		byte[] bytes = Bits.bitsToByte(bits);
		boolean[] confirm = Bits.bytesToBits(bytes);
		for(int i = 0; i < 256; i++)
			if(confirm[i] ^ bits[i]) {
				Log.info.println(confirm[i] + " : " + i + " : " + bits[i]);
				Test.reportError("Bit to byte compression and decompression is inconsistant");
			}
		short[] shorts = Bits.bitsToShort(bits);
		confirm = Bits.shortsToBits(shorts);
		for(int i = 0; i < 256; i++)
			if(confirm[i] ^ bits[i]) {
				Log.info.println(confirm[i] + " : " + i + " : " + bits[i]);
				Test.reportError("Bit to short compression and decompression is inconsistant");
			}
		char[] chars = Bits.bitsToChar(bits);
		confirm = Bits.charsToBits(chars);
		for(int i = 0; i < 256; i++)
			if(confirm[i] ^ bits[i]) {
				Log.info.println(confirm[i] + " : " + i + " : " + bits[i]);
				Test.reportError("Bit to char compression and decompression is inconsistant");
			}
		int[] ints = Bits.bitsToInt(bits);
		confirm = Bits.intsToBits(ints);
		for(int i = 0; i < 256; i++)
			if(confirm[i] ^ bits[i]) {
				Log.info.println(confirm[i] + " : " + i + " : " + bits[i]);
				Test.reportError("Bit to int compression and decompression is inconsistant");
			}
		long[] longs = Bits.bitsToLong(bits);
		confirm = Bits.longsToBits(longs);
		for(int i = 0; i < 256; i++)
			if(confirm[i] ^ bits[i]) {
				Log.info.println(confirm[i] + " : " + i + " : " + bits[i]);
				Test.reportError("Bit to long compression and decompression is inconsistant");
			}
	}
	
	public static void testByteCompression()
	{
		JRandom rand = new JRandom();
		byte[] bytes = new byte[256];
		for(int i = 0; i < 256; i++)
			bytes[i] = rand.nextByte();
		boolean[] bits = Bits.bytesToBits(bytes);
		byte[] confirm = Bits.bitsToByte(bits);
		for(int i = 0; i < 256; i++)
			if(bytes[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing bytes to and from bits yielded diffent results.");
				break;
			}
		short[] shorts = Bits.bytesToShorts(bytes);
		confirm = Bits.shortsToBytes(shorts);
		for(int i = 0; i < 256; i++)
			if(bytes[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing bytes to and from shorts yielded diffent results.");
				break;
			}
		char[] chars = Bits.bytesToChars(bytes);
		confirm = Bits.charsToBytes(chars);
		for(int i = 0; i < 256; i++)
			if(bytes[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing bytes to and from chars yielded diffent results.");
				break;
			}
		int[] ints = Bits.bytesToInts(bytes);
		confirm = Bits.intsToBytes(ints);
		for(int i = 0; i < 256; i++)
			if(bytes[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing bytes to and from ints yielded diffent results.");
				break;
			}
		long[] longs = Bits.bytesToLongs(bytes);
		confirm = Bits.longsToBytes(longs);
		for(int i = 0; i < 256; i++)
			if(bytes[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing bytes to and from longs yielded diffent results.");
				break;
			}
	}
	
	public static void testShortCompression()
	{
		JRandom rand = new JRandom();
		short[] shorts = new short[256];
		for(int i = 0; i < 256; i++)
			shorts[i] = rand.nextShort();
		boolean[] bits = Bits.shortsToBits(shorts);
		short[] confirm = Bits.bitsToShort(bits);
		for(int i = 0; i < 256; i++)
			if(shorts[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing shorts to and from bits yielded diffent results.");
				break;
			}
		byte[] bytes = Bits.shortsToBytes(shorts);
		confirm = Bits.bytesToShorts(bytes);
		for(int i = 0; i < 256; i++)
			if(shorts[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing shorts to and from bytes yielded diffent results.");
				break;
			}
		char[] chars = Bits.shortsToChars(shorts);
		confirm = Bits.charsToShorts(chars);
		for(int i = 0; i < 256; i++)
			if(shorts[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing shorts to and from chars yielded diffent results.");
				break;
			}
		int[] ints = Bits.shortsToInts(shorts);
		confirm = Bits.intsToShorts(ints);
		for(int i = 0; i < 256; i++)
			if(shorts[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing shorts to and from ints yielded diffent results.");
				break;
			}
		long[] longs = Bits.shortsToLongs(shorts);
		confirm = Bits.longsToShorts(longs);
		for(int i = 0; i < 256; i++)
			if(shorts[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing shorts to and from longs yielded diffent results.");
				break;
			}
	}
	
	public static void testCharCompression()
	{
		JRandom rand = new JRandom();
		char[] chars = new char[256];
		for(int i = 0; i < 256; i++)
			chars[i] = (char) rand.nextShort();
		boolean[] bits = Bits.charsToBits(chars);
		char[] confirm = Bits.bitsToChar(bits);
		for(int i = 0; i < 256; i++)
			if(chars[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing chars to and from bits yielded diffent results.");
				break;
			}
		byte[] bytes = Bits.charsToBytes(chars);
		confirm = Bits.bytesToChars(bytes);
		for(int i = 0; i < 256; i++)
			if(chars[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing chars to and from bytes yielded diffent results.");
				break;
			}
		short[] shorts = Bits.charsToShorts(chars);
		confirm = Bits.shortsToChars(shorts);
		for(int i = 0; i < 256; i++)
			if(chars[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing chars to and from shorts yielded diffent results.");
				break;
			}
		int[] ints = Bits.charsToInts(chars);
		confirm = Bits.intsToChars(ints);
		for(int i = 0; i < 256; i++)
			if(chars[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing chars to and from ints yielded diffent results.");
				break;
			}
		long[] longs = Bits.charsToLongs(chars);
		confirm = Bits.longsToChars(longs);
		for(int i = 0; i < 256; i++)
			if(chars[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing chars to and from longs yielded diffent results.");
				break;
			}
	}
	
	public static void testIntCompression()
	{
		JRandom rand = new JRandom();
		int[] ints = new int[256];
		for(int i = 0; i < 256; i++)
			ints[i] = (int) rand.nextShort();
		boolean[] bits = Bits.intsToBits(ints);
		int[] confirm = Bits.bitsToInt(bits);
		for(int i = 0; i < 256; i++)
			if(ints[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing ints to and from bits yielded diffent results.");
				break;
			}
		byte[] bytes = Bits.intsToBytes(ints);
		confirm = Bits.bytesToInts(bytes);
		for(int i = 0; i < 256; i++)
			if(ints[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing ints to and from bytes yielded diffent results.");
				break;
			}
		short[] shorts = Bits.intsToShorts(ints);
		confirm = Bits.shortsToInts(shorts);
		for(int i = 0; i < 256; i++)
			if(ints[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing ints to and from shorts yielded diffent results.");
				break;
			}
		char[] chars = Bits.intsToChars(ints);
		confirm = Bits.charsToInts(chars);
		for(int i = 0; i < 256; i++)
			if(ints[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing ints to and from chars yielded diffent results.");
				break;
			}
		long[] longs = Bits.intsToLongs(ints);
		confirm = Bits.longsToInts(longs);
		for(int i = 0; i < 256; i++)
			if(ints[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing ints to and from longs yielded diffent results.");
				break;
			}
	}
	
	public static void testLongCompression()
	{
		JRandom rand = new JRandom();
		long[] longs = new long[256];
		for(int i = 0; i < 256; i++)
			longs[i] = (long) rand.nextShort();
		boolean[] bits = Bits.longsToBits(longs);
		long[] confirm = Bits.bitsToLong(bits);
		for(int i = 0; i < 256; i++)
			if(longs[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing longs to and from bits yielded diffent results.");
				break;
			}
		byte[] bytes = Bits.longsToBytes(longs);
		confirm = Bits.bytesToLongs(bytes);
		for(int i = 0; i < 256; i++)
			if(longs[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing longs to and from bytes yielded diffent results.");
				break;
			}
		short[] shorts = Bits.longsToShorts(longs);
		confirm = Bits.shortsToLongs(shorts);
		for(int i = 0; i < 256; i++)
			if(longs[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing longs to and from shorts yielded diffent results.");
				break;
			}
		char[] chars = Bits.longsToChars(longs);
		confirm = Bits.charsToLongs(chars);
		for(int i = 0; i < 256; i++)
			if(longs[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing longs to and from chars yielded diffent results.");
				break;
			}
		int[] ints = Bits.longsToInts(longs);
		confirm = Bits.intsToLongs(ints);
		for(int i = 0; i < 256; i++)
			if(longs[i] != confirm[i]) {
				Test.reportError("Compressing and decompressing longs to and from ints yielded diffent results.");
				break;
			}
	}
	
	public static void testSingleWordMethods()
	{
		final int TEST_SIZE = 64;
		JRandom rand = new JRandom();
		short[] shorts = new short[TEST_SIZE];
		for(int i = 0; i < TEST_SIZE; i++)
			shorts[i] = rand.nextShort();
		byte[] test = new byte[TEST_SIZE * 2];
		for(int i = 0; i < TEST_SIZE; i++)
			Bits.shortToBytes(shorts[i], test, i << 1);
		byte[] confirm = new byte[TEST_SIZE * 2]; 
		Bits.shortsToBytes(shorts, confirm);
		for(int i = 0; i < TEST_SIZE; i++)
			if(test[i] != confirm[i]) {
				Test.reportError("Singular and plural short to byte methods returned different results");
				break;
			}
		short[] short2 = new short[TEST_SIZE];
		for(int i = 0; i < TEST_SIZE; i++)
			short2[i] = Bits.shortFromBytes(test, i << 1);
		for(int i = 0; i < TEST_SIZE; i++)
			if(short2[i] != shorts[i]) {
				Test.reportError("Singular and plural byte to short methods returned different results");
				break;
			}
		
		char[] chars = new char[TEST_SIZE];
		for(int i = 0; i < TEST_SIZE; i++)
			chars[i] = (char) rand.nextShort();
		test = new byte[TEST_SIZE * 2];
		for(int i = 0; i < TEST_SIZE; i++)
			Bits.charToBytes(chars[i], test, i << 1);
		confirm = new byte[TEST_SIZE * 2]; 
		Bits.charsToBytes(chars, confirm);
		for(int i = 0; i < TEST_SIZE; i++)
			if(test[i] != confirm[i]) {
				Test.reportError("Singular and plural char to byte methods returned different results");
				break;
			}
		char[] char2 = new char[TEST_SIZE];
		for(int i = 0; i < TEST_SIZE; i++)
			char2[i] = Bits.charFromBytes(test, i << 1);
		for(int i = 0; i < TEST_SIZE; i++)
			if(char2[i] != chars[i]) {
				Test.reportError("Singular and plural byte to char methods returned different results");
				break;
			}
		
		int[] ints = new int[TEST_SIZE];
		for(int i = 0; i < TEST_SIZE; i++)
			ints[i] = rand.nextInt();
		test = new byte[TEST_SIZE * 4];
		for(int i = 0; i < TEST_SIZE; i++)
			Bits.intToBytes(ints[i], test, i << 2);
		confirm = new byte[TEST_SIZE * 4]; 
		Bits.intsToBytes(ints, confirm);
		for(int i = 0; i < TEST_SIZE; i++)
			if(test[i] != confirm[i]) {
				Test.reportError("Singular and plural int to byte methods returned different results");
				break;
			}
		int[] int2 = new int[TEST_SIZE];
		for(int i = 0; i < TEST_SIZE; i++)
			int2[i] = Bits.intFromBytes(test, i << 2);
		for(int i = 0; i < TEST_SIZE; i++)
			if(int2[i] != ints[i]) {
				Test.reportError("Singular and plural byte to int methods returned different results");
				break;
			}
		
		long[] longs = new long[TEST_SIZE];
		for(int i = 0; i < TEST_SIZE; i++)
			longs[i] = rand.nextLong();
		test = new byte[TEST_SIZE * 8];
		for(int i = 0; i < TEST_SIZE; i++)
			Bits.longToBytes(longs[i], test, i << 3);
		confirm = new byte[TEST_SIZE * 8]; 
		Bits.longsToBytes(longs, confirm);
		for(int i = 0; i < TEST_SIZE; i++)
			if(test[i] != confirm[i]) {
				Test.reportError("Singular and plural long to byte methods returned different results");
				break;
			}
		long[] long2 = new long[TEST_SIZE];
		for(int i = 0; i < TEST_SIZE; i++)
			long2[i] = Bits.longFromBytes(test, i << 3);
		for(int i = 0; i < TEST_SIZE; i++)
			if(long2[i] != longs[i]) {
				Test.reportError("Singular and plural byte to long methods returned different results");
				break;
			}
	}
	
	public static void runTests()
	{
		Log.info.println("Running tests on Bits.java");
		Log.info.println("Testing primitive array rotation...");
		testArrayRotation();
		Log.info.println("Testing bit conversion...");
		testBitCompression();
		Log.info.println("Testing byte conversion...");
		testByteCompression();
		Log.info.println("Testing short conversion...");
		testShortCompression();
		Log.info.println("Testing char conversion...");
		testCharCompression();
		Log.info.println("Testing int conversion...");
		testIntCompression();
		Log.info.println("Testing long conversion...");
		testLongCompression();
		Log.info.println("Testing single word to byte conversions...");
		testSingleWordMethods();
	}

}

package claire.util.test.encoding;

import java.io.File;
import java.io.IOException;

import claire.util.crypto.rng.RandUtils;
import claire.util.encoding.CTFL;
import claire.util.encoding.CTFS;
import claire.util.encoding.Decoder;
import claire.util.encoding.Encoder;
import claire.util.encoding.TextReader;
import claire.util.encoding.TextWriter;
import claire.util.encoding.UTF8;
import claire.util.io.FileIncomingStream;
import claire.util.io.FileOutgoingStream;
import claire.util.logging.Log;
import claire.util.memory.buffer.ByteArrayIncomingStream;
import claire.util.memory.buffer.ByteArrayOutgoingStream;
import claire.util.test.Test;

public final class Encodings {
	
	private static final char[] ALL = new char[65535];
	private static final int[] Test32 = new int[1000];
	private static final long[] Test64 = new long[1000];
	
	static {
		for(char c = 0; c < 65535; c++)
			ALL[c] = c;
		long c = 0x10000;
		for(int i = 0; i < 1000; i++)
			Test32[i] = (int) c++;
		c = 0x100000000L;
		for(int i = 0; i < 1000; i++)
			Test64[i] = c++;
	}
	
	public static void testUTF8()
	{
		String s = new String(ALL);
		byte[] real = s.getBytes();
		byte[] test = UTF8.fromUTF16(ALL);
		byte[] stream = new byte[test.length];
		for(int i = 0; i < 65535; i++)
			if(real[i] != test[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Converting widechar to UTF-8 failed.");
				break;
			}
		ByteArrayOutgoingStream os = new ByteArrayOutgoingStream(stream);
		try {
			UTF8.fromUTF16(os, ALL);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting widechar to UTF-8 on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 65535; i++)
			if(stream[i] != test[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream widechar -> UTF-8 conversion is not congruent with array conversion.");
				break;
			}
		char[] chars = UTF8.toUTF16(test);
		for(int i = 0; i < 65535; i++)
			if(ALL[i] != chars[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Converting UTF-8 to widechar failed.");
				break;
			}
		char[] stream2 = new char[chars.length];
		ByteArrayIncomingStream is = new ByteArrayIncomingStream(test);
		try {
			UTF8.toUTF16(is, stream2);
			is.close();
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting widechar to UTF-8 on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 65535; i++)
			if(stream2[i] != chars[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream UTF-8 -> widechar conversion is not congruent with array conversion.");
				break;
			}
		int[] ints = UTF8.toUTF32(test);
		for(int i = 0; i < 65535; i++)
			if(chars[i] != ints[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Converting UTF-8 to widechar and doubleword have inconsistent results.");
				break;
			}
		int[] stream3 = new int[ints.length];
		try {
			is.close();
			is = new ByteArrayIncomingStream(test);
			UTF8.toUTF32(is, stream3);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting UTF-8 to widechar on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 65535; i++)
			if(stream3[i] != ints[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream UTF-8 -> doubleword conversion is not congruent with array conversion.");
				break;
			}
		byte[] chal = UTF8.fromUTF32(ints);
		for(int i = 0; i < 65535; i++)
			if(test[i] != chal[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Converting UTF-8 from doubleword.");
				break;
			}
		os = new ByteArrayOutgoingStream(stream);
		try {
			UTF8.fromUTF32(os, ints);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting doubleword to UTF-8 on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 65535; i++)
			if(stream[i] != test[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream doubleword -> UTF-8 conversion is not congruent with array conversion.");
				break;
			}
		test = UTF8.fromUTF32(Test32);
		ints = UTF8.toUTF32(test);
		for(int i = 0; i < 1000; i++)
			if(ints[i] != Test32[i]) {
				Log.err.println("Char " + i + " does not match.");
				Test.reportError("Converting doubleword to and from UTF-8 failed.");
				break;
			}
		stream3 = new int[ints.length];
		try {
			is.close();
			is = new ByteArrayIncomingStream(test);
			UTF8.toUTF32(is, stream3);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting UTF-8 to doubleword on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 1000; i++)
			if(stream3[i] != ints[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream UTF-8 -> doubleword conversion is not congruent with array conversion.");
				break;
			}
		chars = UTF8.toUTF16(test);
		for(int i = 0; i < 1000; i++)
			if(chars[i] != 0xFFFD) {
				Log.err.println("Char " + i + " is not replacement character. (" + chars[i] + " or " + (int) chars[i] + ")");
				Test.reportError("Detected undefined behavior when converting doubleword UTF-8 to widechar.");
				break;
			}
		stream2 = new char[chars.length];
		try {
			is.close();
			is = new ByteArrayIncomingStream(test);
			UTF8.toUTF16(is, stream2);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting UTF-8 to widechar on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 1000; i++)
			if(stream2[i] != chars[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream UTF-8 -> widechar conversion is not congruent with array conversion.");
				break;
			}
	}
	
	public static void testCTFL()
	{
		ByteArrayOutgoingStream os;
		ByteArrayIncomingStream is;
		byte[] test = CTFL.fromUTF16(ALL);
		byte[] stream = new byte[test.length];
		char[] stream2;
		int[] stream3;
		long[] stream4;
		os = new ByteArrayOutgoingStream(stream);
		try {
			CTFL.fromUTF16(os, ALL);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting widechar to CTF-L on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < test.length; i++)
			if(stream[i] != test[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream widechar -> CTF-L conversion is not congruent with array conversion.");
				break;
			}
		
		char[] chars = CTFL.toUTF16(test);
		for(int i = 0; i < 65535; i++)
			if(ALL[i] != chars[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("converting CTF-L to widechar failed.");
				break;
			}

		stream2 = new char[chars.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFL.toUTF16(is, stream2);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting widechar to CTF-L on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 65535; i++)
			if(stream2[i] != chars[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-L -> widechar conversion is not congruent with array conversion.");
				break;
			}
		
		int[] ints = CTFL.toUTF32(test);
		for(int i = 0; i < 65535; i++)
			if(chars[i] != ints[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Converting CTF-L to widechar and doubleword have inconsistent results.");
				break;
			}
		stream3 = new int[ints.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFL.toUTF32(is, stream3);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-L to doubleword on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 65535; i++)
			if(stream3[i] != ints[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-L -> doubleword conversion is not congruent with array conversion.");
				break;
			}
		
		long[] longs = CTFL.toUTF64(test);
		for(int i = 0; i < 65535; i++)
			if(chars[i] != longs[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Converting CTF-L to widechar and quadword have inconsistent results.");
				break;
			}
		stream4 = new long[longs.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFL.toUTF64(is, stream4);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-L to quadword on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 65535; i++)
			if(stream4[i] != longs[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-L -> quadword conversion is not congruent with array conversion.");
				break;
			}
		
		byte[] chal = CTFL.fromUTF32(ints);
		for(int i = 0; i < 65535; i++)
			if(test[i] != chal[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Converting CTF-L from doubleword.");
				break;
			}
		os = new ByteArrayOutgoingStream(stream);
		try {
			CTFL.fromUTF32(os, ints);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting doubleword to CTF-L on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < test.length; i++)
			if(stream[i] != test[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream doubleword -> CTF-L conversion is not congruent with array conversion.");
				break;
			}
		
		chal = CTFL.fromUTF64(longs);
		for(int i = 0; i < 65535; i++)
			if(test[i] != chal[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Converting CTF-L from quadword.");
				break;
			}
		os = new ByteArrayOutgoingStream(stream);
		try {
			CTFL.fromUTF64(os, longs);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting quadword to CTF-L on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < test.length; i++)
			if(stream[i] != test[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream quadword -> CTF-L conversion is not congruent with array conversion.");
				break;
			}
		
		test = CTFL.fromUTF32(Test32);
		os = new ByteArrayOutgoingStream(stream);
		try {
			CTFL.fromUTF32(os, Test32);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting doubleword to CTF-L on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < test.length; i++)
			if(stream[i] != test[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream doubleword -> CTF-L conversion is not congruent with array conversion.");
				break;
			}
		
		ints = CTFL.toUTF32(test);
		for(int i = 0; i < 1000; i++)
			if(ints[i] != Test32[i]) {
				Log.err.println("Char " + i + " does not match.");
				Test.reportError("Converting doubleword to and from CTF-L failed.");
			}
		stream3 = new int[ints.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFL.toUTF32(is, stream3);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-L to doubleword on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 1000; i++)
			if(stream3[i] != ints[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-L -> doubleword conversion is not congruent with array conversion.");
				break;
			}
		
		chars = CTFL.toUTF16(test);
		for(int i = 0; i < 1000; i++)
			if(chars[i] != 0xFFFD) {
				Log.err.println("Char " + i + " is not replacement character. (" + chars[i] + " or " + (int) chars[i] + ")");
				Test.reportError("Detected undefined behavior when converting doubleword CTF-L to widechar.");
				break;
			}
		stream2 = new char[chars.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFL.toUTF16(is, stream2);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-L to widechar on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 1000; i++)
			if(stream3[i] != ints[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-L -> widechar conversion is not congruent with array conversion.");
				break;
			}
		
		test = CTFL.fromUTF64(Test64);
		os = new ByteArrayOutgoingStream(stream);
		try {
			CTFL.fromUTF64(os, Test64);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting quadword to CTF-L on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < test.length; i++)
			if(stream[i] != test[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream quadword -> CTF-L conversion is not congruent with array conversion.");
				break;
			}
		
		longs = CTFL.toUTF64(test);
		for(int i = 0; i < 1000; i++)
			if(longs[i] != Test64[i]) {
				Log.err.println("Char " + i + " does not match.");
				Test.reportError("Converting doubleword to and from CTF-L failed.");
			}
		stream4 = new long[longs.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFL.toUTF64(is, stream4);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-L to quadword on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 1000; i++)
			if(stream4[i] != longs[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-L -> quadword conversion is not congruent with array conversion.");
				break;
			}
		
		chars = CTFL.toUTF16(test);
		for(int i = 0; i < 1000; i++)
			if(chars[i] != 0xFFFD) {
				Log.err.println("Char " + i + " is not replacement character. (" + chars[i] + " or " + (int) chars[i] + ")");
				Test.reportError("Detected undefined behavior when converting quadword CTF-L to widechar.");
				break;
			}
		stream2 = new char[chars.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFL.toUTF16(is, stream2);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-L to widechar on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 1000; i++)
			if(stream2[i] != chars[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-L -> widechar conversion is not congruent with array conversion.");
				break;
			}
		
		ints = CTFL.toUTF32(test);
		for(int i = 0; i < 1000; i++)
			if(ints[i] != 0xFFFD) {
				Log.err.println("Char " + i + " is not replacement character. (" + chars[i] + " or " + (int) chars[i] + ")");
				Test.reportError("Detected undefined behavior when converting quadword CTF-L to doubleword.");
				break;
			}
		stream3 = new int[ints.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFL.toUTF32(is, stream3);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-L to doubleword on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 1000; i++)
			if(stream3[i] != ints[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-L -> doubleword conversion is not congruent with array conversion.");
				break;
			}
	}
	
	public static void testCTFS()
	{
		ByteArrayOutgoingStream os;
		ByteArrayIncomingStream is;
		byte[] test = CTFS.fromUTF16(ALL);
		byte[] stream = new byte[test.length];
		char[] stream2;
		int[] stream3;
		long[] stream4;
		os = new ByteArrayOutgoingStream(stream);
		try {
			CTFS.fromUTF16(os, ALL);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting widechar to CTF-S on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < test.length; i++)
			if(stream[i] != test[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream widechar -> CTF-S conversion is not congruent with array conversion.");
				break;
			}
		
		char[] chars = CTFS.toUTF16(test);
		for(int i = 0; i < 65535; i++)
			if(ALL[i] != chars[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("converting CTF-S to widechar failed.");
				break;
			}

		stream2 = new char[chars.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFS.toUTF16(is, stream2);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting widechar to CTF-S on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 65535; i++)
			if(stream2[i] != chars[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-S -> widechar conversion is not congruent with array conversion.");
				break;
			}
		
		int[] ints = CTFS.toUTF32(test);
		for(int i = 0; i < 65535; i++)
			if(chars[i] != ints[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Converting CTF-S to widechar and doubleword have inconsistent results.");
				break;
			}
		stream3 = new int[ints.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFS.toUTF32(is, stream3);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-S to doubleword on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 65535; i++)
			if(stream3[i] != ints[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-S -> doubleword conversion is not congruent with array conversion.");
				break;
			}
		
		long[] longs = CTFS.toUTF64(test);
		for(int i = 0; i < 65535; i++)
			if(chars[i] != longs[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Converting CTF-S to widechar and quadword have inconsistent results.");
				break;
			}
		stream4 = new long[longs.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFS.toUTF64(is, stream4);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-S to quadword on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 65535; i++)
			if(stream4[i] != longs[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-S -> quadword conversion is not congruent with array conversion.");
				break;
			}
		
		byte[] chal = CTFS.fromUTF32(ints);
		for(int i = 0; i < 65535; i++)
			if(test[i] != chal[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Converting CTF-S from doubleword.");
				break;
			}
		os = new ByteArrayOutgoingStream(stream);
		try {
			CTFS.fromUTF32(os, ints);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting doubleword to CTF-S on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < test.length; i++)
			if(stream[i] != test[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream doubleword -> CTF-S conversion is not congruent with array conversion.");
				break;
			}
		
		chal = CTFS.fromUTF64(longs);
		for(int i = 0; i < 65535; i++)
			if(test[i] != chal[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Converting CTF-S from quadword.");
				break;
			}
		os = new ByteArrayOutgoingStream(stream);
		try {
			CTFS.fromUTF64(os, longs);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting quadword to CTF-S on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < test.length; i++)
			if(stream[i] != test[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream quadword -> CTF-S conversion is not congruent with array conversion.");
				break;
			}
		
		test = CTFS.fromUTF32(Test32);
		os = new ByteArrayOutgoingStream(stream);
		try {
			CTFS.fromUTF32(os, Test32);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting doubleword to CTF-S on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < test.length; i++)
			if(stream[i] != test[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream doubleword -> CTF-S conversion is not congruent with array conversion.");
				break;
			}
		
		ints = CTFS.toUTF32(test);
		for(int i = 0; i < 1000; i++)
			if(ints[i] != Test32[i]) {
				Log.err.println("Char " + i + " does not match.");
				Test.reportError("Converting doubleword to and from CTF-S failed.");
			}
		stream3 = new int[ints.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFS.toUTF32(is, stream3);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-S to doubleword on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 1000; i++)
			if(stream3[i] != ints[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-S -> doubleword conversion is not congruent with array conversion.");
				break;
			}
		
		chars = CTFS.toUTF16(test);
		for(int i = 0; i < 1000; i++)
			if(chars[i] != 0xFFFD) {
				Log.err.println("Char " + i + " is not replacement character. (" + chars[i] + " or " + (int) chars[i] + ")");
				Test.reportError("Detected undefined behavior when converting doubleword CTF-S to widechar.");
				break;
			}
		stream2 = new char[chars.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFS.toUTF16(is, stream2);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-S to widechar on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 1000; i++)
			if(stream3[i] != ints[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-S -> widechar conversion is not congruent with array conversion.");
				break;
			}
		
		test = CTFS.fromUTF64(Test64);
		os = new ByteArrayOutgoingStream(stream);
		try {
			CTFS.fromUTF64(os, Test64);
		} catch (Exception e) {
			Test.reportError("Encountered exception while converting quadword to CTF-S on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < test.length; i++)
			if(stream[i] != test[i]) {
				Log.err.println("Byte " + i + " does not match.");
				Test.reportError("Stream quadword -> CTF-S conversion is not congruent with array conversion.");
				break;
			}
		
		longs = CTFS.toUTF64(test);
		for(int i = 0; i < 1000; i++)
			if(longs[i] != Test64[i]) {
				Log.err.println("Char " + i + " does not match.");
				Test.reportError("Converting doubleword to and from CTF-S failed.");
			}
		stream4 = new long[longs.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFS.toUTF64(is, stream4);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-S to quadword on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 1000; i++)
			if(stream4[i] != longs[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-S -> quadword conversion is not congruent with array conversion.");
				break;
			}
		
		chars = CTFS.toUTF16(test);
		for(int i = 0; i < 1000; i++)
			if(chars[i] != 0xFFFD) {
				Log.err.println("Char " + i + " is not replacement character. (" + chars[i] + " or " + (int) chars[i] + ")");
				Test.reportError("Detected undefined behavior when converting quadword CTF-S to widechar.");
				break;
			}
		stream2 = new char[chars.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFS.toUTF16(is, stream2);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-S to widechar on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 1000; i++)
			if(stream2[i] != chars[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-S -> widechar conversion is not congruent with array conversion.");
				break;
			}
		
		ints = CTFS.toUTF32(test);
		for(int i = 0; i < 1000; i++)
			if(ints[i] != 0xFFFD) {
				Log.err.println("Char " + i + " is not replacement character. (" + chars[i] + " or " + (int) chars[i] + ")");
				Test.reportError("Detected undefined behavior when converting quadword CTF-S to doubleword.");
				break;
			}
		stream3 = new int[ints.length];
		is = new ByteArrayIncomingStream(test);
		try {
			CTFS.toUTF32(is, stream3);
		} catch(Exception e) {
			Test.reportError("Encountered exception while converting CTF-S to doubleword on a stream.");
			e.printStackTrace(System.out);
		}
		for(int i = 0; i < 1000; i++)
			if(stream3[i] != ints[i]) {
				Log.err.println("Character " + i + " does not match.");
				Test.reportError("Stream CTF-S -> doubleword conversion is not congruent with array conversion.");
				break;
			}
	}
	
	public static void testRW(String encoding)
	{
		File f = new File(encoding + ".tmp");
		FileOutgoingStream fos;
		try {
			fos = new FileOutgoingStream(f);
		} catch (IOException e) {
			Test.reportError("Error encountered while creating file outgoing stream, aborting test.");
			e.printStackTrace(System.out);
			return;
		}
		Encoder enc = new Encoder(encoding);
		TextWriter writer = new TextWriter(enc, fos);
		char[][] chars = new char[8][16];
		for(int i = 0; i < 8; i++) {
			RandUtils.fillArr(chars[i]);
			try {
				writer.println(chars[i]);
			} catch (Exception e) {
				Test.reportError("Error encountered while writing to TextWriter, aborting test.");
				e.printStackTrace(System.out);
				return;
			}	
		}
		try {
			fos.close();
		} catch (IOException e) {
			Test.reportError("Error encountered while closing file outgoing stream, aborting test.");
			e.printStackTrace(System.out);
			return;
		}
		FileIncomingStream fis;
		try {
			fis = new FileIncomingStream(f);
		} catch (IOException e) {
			Test.reportError("Error encountered while creating file incoming stream, aborting test.");
			e.printStackTrace(System.out);
			return;
		}
		Decoder dec = new Decoder(encoding);
		TextReader reader = new TextReader(dec, fis);
		for(int i = 0; i < 8; i++) {
			try {
				char[] test = reader.readLine();
				for(int j = 0; j < 16; j++)
					if(test[j] != chars[i][j]) {
						Log.err.println("Character " + j + " on line " + i + " does not match");
						Test.reportError("Reading and writing from " + encoding + " readers is inconsistent");
						return;
					}
			} catch (Exception e) {
				Test.reportError("Error encountered while reading from TextReader, aborting test.");
				e.printStackTrace(System.out);
				return;
			}	
		}
		try {
			fos.close();
			fis.close();
		} catch (IOException e) {
			Test.reportError("Error encountered while closing file incoming stream, aborting test.");
			e.printStackTrace(System.out);
			return;
		}
		f.delete();
	}
	
	public static void runTests()
	{
		Log.info.println("Testing character encodings...");
		Log.info.println("UTF-8...");
		testUTF8();
		Log.info.println("CTF-L...");
		testCTFL();
		Log.info.println("CTF-S...");
		testCTFS();
		Log.info.println("Testing writer/readers");
		Log.info.println("UTF-8...");
		testRW("UTF-8");
		Log.info.println("CTF-L...");
		testRW("CTF-L");
		Log.info.println("CTF-S...");
		testRW("CTF-S");
	}

}

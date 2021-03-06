package claire.util.test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import claire.util.concurrency.gen.CountdownMonitor;
import claire.util.concurrency.gen.GeneratorThread;
import claire.util.concurrency.gen.ResultsCollector;
import claire.util.concurrency.gen.TaskMonitor;
import claire.util.crypto.CryptoString;
import claire.util.crypto.cipher.key.block.KeySimon32;
import claire.util.crypto.cipher.primitive.block.Simon32;
import claire.util.crypto.rng.RandUtils;
import claire.util.crypto.rng.primitive.FastXorShift;
import claire.util.crypto.rng.primitive.XorShiftNG;
import claire.util.encoding.CString;
import claire.util.encoding.EncodingUtil;
import claire.util.encoding.Hex;
import claire.util.math.MathHelper;
import claire.util.math.prime.CryptoPrimeGenerator;
import claire.util.math.primitive.UInt;
import claire.util.memory.Bits;

public final class Main {
	
	@SuppressWarnings("all")
	private static final void end()
	{
		System.out.print((char) 0);
		System.err.print((char) 0);
		System.err.flush();
		System.out.flush();
		System.out.print((char) 0);
		System.err.print((char) 0);
		System.err.flush();
		System.out.flush();
		throw new java.lang.NullPointerException();
	}
	
	public static BigInteger bigint(UInt u)
	{
		return new BigInteger(new String(u.toChars()));
	}
	
	public static void main(String[] args) throws Exception
	{
		
		//Test.runTests();
		//end();
		int len = 8192;
		int primes = 70;
		CryptoPrimeGenerator pg1 = new CryptoPrimeGenerator(4, len, primes);
		//pg1.test();
		
		
		CryptoPrimeGenerator pg2 = new CryptoPrimeGenerator(4, len, primes);
		CryptoPrimeGenerator pg3 = new CryptoPrimeGenerator(4, len, primes);
		CryptoPrimeGenerator pg4 = new CryptoPrimeGenerator(4, len, primes);
		CryptoPrimeGenerator pg5 = new CryptoPrimeGenerator(4, len, primes);
		CryptoPrimeGenerator pg6 = new CryptoPrimeGenerator(4, len, primes);
		CryptoPrimeGenerator pg7 = new CryptoPrimeGenerator(4, len, primes);
		CryptoPrimeGenerator pg8 = new CryptoPrimeGenerator(4, len, primes);
		TaskMonitor mon = new CountdownMonitor(2);
		UInt[] pa = new UInt[2];
		ResultsCollector<UInt> rc = new ResultsCollector<UInt>(pa, 0);
		GeneratorThread<UInt> t1 = new GeneratorThread<UInt>(pg1, rc, mon);
		GeneratorThread<UInt> t2 = new GeneratorThread<UInt>(pg2, rc, mon);
		GeneratorThread<UInt> t3 = new GeneratorThread<UInt>(pg3, rc, mon);
		GeneratorThread<UInt> t4 = new GeneratorThread<UInt>(pg4, rc, mon);
		GeneratorThread<UInt> t5 = new GeneratorThread<UInt>(pg5, rc, mon);
		GeneratorThread<UInt> t6 = new GeneratorThread<UInt>(pg6, rc, mon);
		GeneratorThread<UInt> t7 = new GeneratorThread<UInt>(pg7, rc, mon);
		GeneratorThread<UInt> t8 = new GeneratorThread<UInt>(pg8, rc, mon);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		t8.start();
		mon.waitOn();
		System.out.println(pa[0]);
		System.out.println(pa[1]);
		end();
		Test.runTests();
		Simon32 aria = new Simon32();
		aria.setKey(KeySimon32.factory.random(new FastXorShift(3309), new CryptoString("96".toCharArray(), 0, 2, 's')));
		byte[] bytes1 = Hex.fromHex("deadbeefbeefdeaddeadbeefbeefdead");
		System.out.println(Hex.toHex(bytes1));
		aria.encryptBlock(bytes1);
		System.out.println(Hex.toHex(bytes1));
		aria.decryptBlock(bytes1);
		System.out.println(Hex.toHex(bytes1));
		end();
		
		
		/*
		Test.runTests();
		end();
		CryptoPrimeGenerator pg213 = new CryptoPrimeGenerator(8, 512, 50);
		long t = System.currentTimeMillis();
		for(int i = 0; i < 30; i++)
		{
			UInt u123 = pg213.nextPrime();
			pg213.nextCanidate();
			System.out.println(u123);
			System.out.println(u123.getBits());
		}
		System.out.println((System.currentTimeMillis() - t) / 1000);
		//System.out.println(MathHelper.isPrimeProbableMR(u123, new XorShiftNG(), 64));
		end();*/
		Test.runTests();
		end();
		Test.runTests();
		end();
		CryptoPrimeGenerator pg = new CryptoPrimeGenerator(8, 512, 50);
		UInt u = pg.nextPrime();
		System.out.println(u);
		System.out.println(u.getBits());
		System.out.println(MathHelper.isPrimeProbableMR(u, new XorShiftNG(), 64));
		end();
		System.out.println(MathHelper.getRealLength(new int[] { 0, 0, 0, 0 }));
		//CryptoStrongPrimeGenerator<UInt> p = new CryptoStrongPrimeGenerator<UInt>(8, new UInt(new CString(0x70000000L), 64), new UInt(new CString(0xFF0000000L), 64));
		//System.out.println(p.nextPrime());
		long mod = 2648397443L;
		long i = 2;
		long j =  100;
		int prims = 0;
		int norms = 0;
		long[] ints = new long[] { (mod - 1) / 2, 2 };
		while(j-- > 0)
			while(true) {
				boolean prim = true;
				for(long k : ints) {
					if(MathHelper.modular_exponent(i, k, mod) == 1) {
						prim = false;
						break;
					}
					
				}
				if(prim) {
					System.out.println("Found primitive root: " + i++);
					//i++;
					prims++;
					break;
				} else 
					i++;
				norms++;
				j--;
			}
		System.out.println(prims + " primitive roots.");
		System.out.println(norms + " shitty roots.");
		
		end();
		
		//*/

		
		/*
		int[][][] table = new int[16][16][16];
		for(int i = 0; i < 16; i++)
		{
			for(int j = 0; j < 16; j++)
				for(int k = 0; k < 16; k++) {
					//System.out.println("Key " + i + " with input diff of " + (k ^ j) + " (" + k + " ^ " + j + ") results in output diff of " + (((k + i) & 15) ^ ((j + i) & 15)));
					table[i][j ^ k][((j + i) & 15) ^ ((k + i) & 15)]++;
				}
		}
		for(int j = 0; j < 16; j++)
			for(int k = 0; k < 16; k++)
				for(int i = 0; i < 16; i++)
					if(table[i][j][k] > 0)
						System.out.println("Differential " + j + " --> " + k + " holds with a probability of " + table[i][j][k] + "/16 with key " + i);
		*/
		/*File data = new File("test.dat");
		File indx = new File("test.ini");
		Datafile2<CString> db = new Datafile2<CString>(data, indx, CString.factory);
		/*db.persist(new CString("Topkek0"));
		db.persist(new CString("Topkek1"));
		db.persist(new CString("Topkek2"));
		db.persist(new CString("Topkek3"));
		db.persist(new CString("Topkek4"));
		db.persist(new CString("Topkek5"));
		db.persist(new CString("Topkek6"));
		db.persist(new CString("Topkek7"));
		db.persist(new CString("Topkek8"));
		db.persist(new CString("Topkek9"));*/
		/*System.out.println(db.resurrect(9));
		db.save();
		
		*/
	}
	
	public static void intStrToByte(String str)
	{
		StringBuilder build = new StringBuilder();
		byte b = 0;
		char[] chars = new char[6];
		chars[4] = ',';
		chars[5] = ' ';
		for(char c : str.toCharArray())
		{
			if(c == ',') {
				Arrays.fill(chars, 0, 3, ' ');
				String byt = String.valueOf(b);
				System.arraycopy(byt.toCharArray(), 0, chars, 4 - byt.length(), byt.length());
				build.append(chars);
				b = 0;
				continue;
			}
			b *= 10;
			b += Byte.parseByte(new String(new char[] {c}));
		}
		System.out.println(build.toString());
	}
	
	/*public static void bearImage(int size, String name) throws IOException
	{
		int SAMPLE = size;
		byte[] data = new byte[SAMPLE * SAMPLE * 3];
		int[] totals = new int[256];
		$BEAR3 bear = new $BEAR3(1, 1, 1, 2, 4, 8, 0, 1, 1);
		//SHA_2 sha = new SHA_2(512);
		int len = data.length >> 3;
		int[] one = new int[1];
		long s = System.nanoTime();
		for(int i = 0; i < len; i++) {
			one[0] = i + 4132423;
			bear.init(Bits.intsToBytes(one));
			bear.hash();
			bear.getOut(data, i << 3);
			//System.arraycopy(bytes, 0, data, i << 7, 128);
		}
		long f = (System.nanoTime() - s) / (1000 * 1000);
		System.out.println("Completed " + len + " hashes in " + f + " milliseconds at " + (f / len) + " milliseconds per hash.");	
		for(byte b : data)
			totals[(int)b & 0xFF]++;
		for(int i = 0; i < 256; i++)
			System.out.println(Hex.toHexStr((byte) i) + ": " + totals[i]);
		File fi = new File(name);
		BufferedImage i = ImageUtil.imageFromBytes(data, SAMPLE, SAMPLE);
		ImageUtil.saveImage(i, fi);
	}*/
	
	public static void convertByteString(String s)
	{
		//String s = ", 0xd9, 0x78, 0xf9, 0xc4, 0x19, 0xdd, 0xb5, 0xed, 0x28, 0xe9, 0xfd, 0x79, 0x4a, 0xa0, 0xd8, 0x9d, 0xc6, 0x7e, 0x37, 0x83, 0x2b, 0x76, 0x53, 0x8e, 0x62, 0x4c, 0x64, 0x88, 0x44, 0x8b, 0xfb, 0xa2, 0x17, 0x9a, 0x59, 0xf5, 0x87, 0xb3, 0x4f, 0x13, 0x61, 0x45, 0x6d, 0x8d, 0x09, 0x81, 0x7d, 0x32, 0xbd, 0x8f, 0x40, 0xeb, 0x86, 0xb7, 0x7b, 0x0b, 0xf0, 0x95, 0x21, 0x22, 0x5c, 0x6b, 0x4e, 0x82, 0x54, 0xd6, 0x65, 0x93, 0xce, 0x60, 0xb2, 0x1c, 0x73, 0x56, 0xc0, 0x14, 0xa7, 0x8c, 0xf1, 0xdc, 0x12, 0x75, 0xca, 0x1f, 0x3b, 0xbe, 0xe4, 0xd1, 0x42, 0x3d, 0xd4, 0x30, 0xa3, 0x3c, 0xb6, 0x26, 0x6f, 0xbf, 0x0e, 0xda, 0x46, 0x69, 0x07, 0x57, 0x27, 0xf2, 0x1d, 0x9b, 0xbc, 0x94, 0x43, 0x03, 0xf8, 0x11, 0xc7, 0xf6, 0x90, 0xef, 0x3e, 0xe7, 0x06, 0xc3, 0xd5, 0x2f, 0xc8, 0x66, 0x1e, 0xd7, 0x08, 0xe8, 0xea, 0xde, 0x80, 0x52, 0xee, 0xf7, 0x84, 0xaa, 0x72, 0xac, 0x35, 0x4d, 0x6a, 0x2a, 0x96, 0x1a, 0xd2, 0x71, 0x5a, 0x15, 0x49, 0x74, 0x4b, 0x9f, 0xd0, 0x5e, 0x04, 0x18, 0xa4, 0xec, 0xc2, 0xe0, 0x41, 0x6e, 0x0f, 0x51, 0xcb, 0xcc, 0x24, 0x91, 0xaf, 0x50, 0xa1, 0xf4, 0x70, 0x39, 0x99, 0x7c, 0x3a, 0x85, 0x23, 0xb8, 0xb4, 0x7a, 0xfc, 0x02, 0x36, 0x5b, 0x25, 0x55, 0x97, 0x31, 0x2d, 0x5d, 0xfa, 0x98, 0xe3, 0x8a, 0x92, 0xae, 0x05, 0xdf, 0x29, 0x10, 0x67, 0x6c, 0xba, 0xc9, 0xd3, 0x00, 0xe6, 0xcf, 0xe1, 0x9e, 0xa8, 0x2c, 0x63, 0x16, 0x01, 0x3f, 0x58, 0xe2, 0x89, 0xa9, 0x0d, 0x38, 0x34, 0x1b, 0xab, 0x33, 0xff, 0xb0, 0xbb, 0x48, 0x0c, 0x5f, 0xb9, 0xb1, 0xcd, 0x2e, 0xc5, 0xf3, 0xdb, 0x47, 0xe5, 0xa5, 0x9c, 0x77, 0x0a, 0xa6, 0x20, 0x68, 0xfe, 0x7f, 0xc1, 0xad";
		String[] s2 = s.split(", 0x");
		char[] alphabet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		String f = "";
		String f2 = "";
		boolean b = true;
		for(String s3 : s2) {
			if(b) {
				b = !b;
				continue;
			}
			byte byt = 0;
			char[] chars = s3.toCharArray();
			for(char c : alphabet)
				if(c == chars[0])
					break;
				else
					byt += 16;
			for(char c : alphabet)
				if(c == chars[1])
					break;
				else
					byt++;
			String buf = String.valueOf(byt);
			for(int i = buf.length(); i < 4; i++)
				f += ' ';
			f += buf + ", ";
			f2 += Hex.toHexStr(byt) + ", ";
		}
		
		System.out.println(f);
		System.out.println(f2);
	}
	
	public static void convertShortString(CString s)
	{
		//String s = ", 0xd9, 0x78, 0xf9, 0xc4, 0x19, 0xdd, 0xb5, 0xed, 0x28, 0xe9, 0xfd, 0x79, 0x4a, 0xa0, 0xd8, 0x9d, 0xc6, 0x7e, 0x37, 0x83, 0x2b, 0x76, 0x53, 0x8e, 0x62, 0x4c, 0x64, 0x88, 0x44, 0x8b, 0xfb, 0xa2, 0x17, 0x9a, 0x59, 0xf5, 0x87, 0xb3, 0x4f, 0x13, 0x61, 0x45, 0x6d, 0x8d, 0x09, 0x81, 0x7d, 0x32, 0xbd, 0x8f, 0x40, 0xeb, 0x86, 0xb7, 0x7b, 0x0b, 0xf0, 0x95, 0x21, 0x22, 0x5c, 0x6b, 0x4e, 0x82, 0x54, 0xd6, 0x65, 0x93, 0xce, 0x60, 0xb2, 0x1c, 0x73, 0x56, 0xc0, 0x14, 0xa7, 0x8c, 0xf1, 0xdc, 0x12, 0x75, 0xca, 0x1f, 0x3b, 0xbe, 0xe4, 0xd1, 0x42, 0x3d, 0xd4, 0x30, 0xa3, 0x3c, 0xb6, 0x26, 0x6f, 0xbf, 0x0e, 0xda, 0x46, 0x69, 0x07, 0x57, 0x27, 0xf2, 0x1d, 0x9b, 0xbc, 0x94, 0x43, 0x03, 0xf8, 0x11, 0xc7, 0xf6, 0x90, 0xef, 0x3e, 0xe7, 0x06, 0xc3, 0xd5, 0x2f, 0xc8, 0x66, 0x1e, 0xd7, 0x08, 0xe8, 0xea, 0xde, 0x80, 0x52, 0xee, 0xf7, 0x84, 0xaa, 0x72, 0xac, 0x35, 0x4d, 0x6a, 0x2a, 0x96, 0x1a, 0xd2, 0x71, 0x5a, 0x15, 0x49, 0x74, 0x4b, 0x9f, 0xd0, 0x5e, 0x04, 0x18, 0xa4, 0xec, 0xc2, 0xe0, 0x41, 0x6e, 0x0f, 0x51, 0xcb, 0xcc, 0x24, 0x91, 0xaf, 0x50, 0xa1, 0xf4, 0x70, 0x39, 0x99, 0x7c, 0x3a, 0x85, 0x23, 0xb8, 0xb4, 0x7a, 0xfc, 0x02, 0x36, 0x5b, 0x25, 0x55, 0x97, 0x31, 0x2d, 0x5d, 0xfa, 0x98, 0xe3, 0x8a, 0x92, 0xae, 0x05, 0xdf, 0x29, 0x10, 0x67, 0x6c, 0xba, 0xc9, 0xd3, 0x00, 0xe6, 0xcf, 0xe1, 0x9e, 0xa8, 0x2c, 0x63, 0x16, 0x01, 0x3f, 0x58, 0xe2, 0x89, 0xa9, 0x0d, 0x38, 0x34, 0x1b, 0xab, 0x33, 0xff, 0xb0, 0xbb, 0x48, 0x0c, 0x5f, 0xb9, 0xb1, 0xcd, 0x2e, 0xc5, 0xf3, 0xdb, 0x47, 0xe5, 0xa5, 0x9c, 0x77, 0x0a, 0xa6, 0x20, 0x68, 0xfe, 0x7f, 0xc1, 0xad";
		CString[] s2 = s.split(new CString(", 0x"));		
		CString f = new CString();
		boolean b = true;
		for(CString s3 : s2) {
			if(b) {
				b = !b;
				continue;
			}
			short shrt = EncodingUtil.shortFromHex(s3);
			String buf = String.valueOf(shrt);
			for(int i = buf.length(); i < 6; i++)
				f.add(' ');
			f.add(buf + ", ");
		}
		
		System.out.println(f);
	}

}

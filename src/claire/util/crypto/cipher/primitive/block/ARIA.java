package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeyARIA;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class ARIA 
	   implements ISymmetric<KeyARIA> {
	
	private static long[] C1 = new long[] {
		0x517cc1b727220a94L, 0xfe13abe8fa9a6ee0L
	};
	
	private static long[] C2 = new long[] {
		0x6db14acc9e21c820L, 0xff28b1d5ef5de2b0L
	};
	
	private static long[] C3 = new long[] {
		0xdb92371d2126e970L, 0x0324977504e8c90eL
	};
	
	private static byte[] S1 = new byte[] {
		  99,  124,  119,  123,  -14,  107,  111,  -59,   48,    1,  103,   43,   -2,  -41,  -85,  118, 
		 -54, -126,  -55,  125,   -6,   89,   71,  -16,  -83,  -44,  -94,  -81, -100,  -92,  114,  -64,  
		 -73,   -3, -109,   38,   54,   63,   -9,  -52,   52,  -91,  -27,  -15,  113,  -40,   49,   21,   
		   4,  -57,   35,  -61,   24, -106,    5, -102,    7,   18, -128,  -30,  -21,   39,  -78,  117,    
		   9, -125,   44,   26,   27,  110,   90,  -96,   82,   59,  -42,  -77,   41,  -29,   47, -124,   
		  83,  -47,    0,  -19,   32,   -4,  -79,   91,  106,  -53,  -66,   57,   74,   76,   88,  -49,  
		 -48,  -17,  -86,   -5,   67,   77,   51, -123,   69,   -7,    2,  127,   80,   60,  -97,  -88,   
		  81,  -93,   64, -113, -110,  -99,   56,  -11,  -68,  -74,  -38,   33,   16,   -1,  -13,  -46, 
		 -51,   12,   19,  -20,   95, -105,   68,   23,  -60,  -89,  126,   61,  100,   93,   25,  115,   
		  96, -127,   79,  -36,   34,   42, -112, -120,   70,  -18,  -72,   20,  -34,   94,   11,  -37,  
		 -32,   50,   58,   10,   73,    6,   36,   92,  -62,  -45,  -84,   98, -111, -107,  -28,  121,  
		 -25,  -56,   55,  109, -115,  -43,   78,  -87,  108,   86,  -12,  -22,  101,  122,  -82,    8,  
		 -70,  120,   37,   46,   28,  -90,  -76,  -58,  -24,  -35,  116,   31,   75,  -67, -117, -118,  
		 112,   62,  -75,  102,   72,    3,  -10,   14,   97,   53,   87,  -71, -122,  -63,   29,  -98,  
		 -31,   -8, -104,   17,  105,  -39, -114, -108, -101,   30, -121,  -23,  -50,   85,   40,  -33, 
		-116,  -95, -119,   13,  -65,  -26,   66,  104,   65, -103,   45,   15,  -80,   84,  -69,   22, 
	};

	private static byte[] S2 = new byte[] {
		 -30,   78,   84,   -4, -108,  -62,   74,  -52,   98,   13,  106,   70,   60,   77, -117,  -47,  
		  94,   -6,  100,  -53,  -76, -105,  -66,   43,  -68,  119,   46,    3,  -45,   25,   89,  -63,   
		  29,    6,   65,  107,   85,  -16, -103,  105,  -22, -100,   24,  -82,   99,  -33,  -25,  -69,   
		   0,  115,  102,   -5, -106,   76, -123,  -28,   58,    9,   69,  -86,   15,  -18,   16,  -21,   
		  45,  127,  -12,   41,  -84,  -49,  -83, -111, -115,  120,  -56, -107,   -7,   47,  -50,  -51,    
		   8,  122, -120,   56,   92, -125,   42,   40,   71,  -37,  -72,  -57, -109,  -92,   18,   83,   
		  -1, -121,   14,   49,   54,   33,   88,   72,    1, -114,   55,  116,   50,  -54,  -23,  -79,  
		 -73,  -85,   12,  -41,  -60,   86,   66,   38,    7, -104,   96,  -39,  -74,  -71,   17,   64,  
		 -20,   32, -116,  -67,  -96,  -55, -124,    4,   73,   35,  -15,   79,   80,   31,   19,  -36,  
		 -40,  -64,  -98,   87,  -29,  -61,  123,  101,   59,    2, -113,   62,  -24,   37, -110,  -27,   
		  21,  -35,   -3,   23,  -87,  -65,  -44, -102,  126,  -59,   57,  103,   -2,  118,  -99,   67,  
		 -89,  -31,  -48,  -11,  104,  -14,   27,   52,  112,    5,  -93, -118,  -43,  121, -122,  -88,   
		  48,  -58,   81,   75,   30,  -90,   39,  -10,   53,  -46,  110,   36,   22, -126,   95,  -38,  
		 -26,  117,  -94,  -17,   44,  -78,   28,  -97,   93,  111, -128,   10,  114,   68, -101,  108, 
		-112,   11,   91,   51,  125,   90,   82,  -13,   97,  -95,   -9,  -80,  -42,   63,  124,  109,  
		 -19,   20,  -32,  -91,   61,   34,  -77,   -8, -119,  -34,  113,   26,  -81,  -70,  -75, -127, 
	};
	
	private static byte[] S3 = new byte[] {
		  82,    9,  106,  -43,   48,   54,  -91,   56,  -65,   64,  -93,  -98, -127,  -13,  -41,   -5,  
		 124,  -29,   57, -126, -101,   47,   -1, -121,   52, -114,   67,   68,  -60,  -34,  -23,  -53,   
		  84,  123, -108,   50,  -90,  -62,   35,   61,  -18,   76, -107,   11,   66,   -6,  -61,   78,    
		   8,   46,  -95,  102,   40,  -39,   36,  -78,  118,   91,  -94,   73,  109, -117,  -47,   37,  
		 114,   -8,  -10,  100, -122,  104, -104,   22,  -44,  -92,   92,  -52,   93,  101,  -74, -110,  
		 108,  112,   72,   80,   -3,  -19,  -71,  -38,   94,   21,   70,   87,  -89, -115,  -99, -124, 
		-112,  -40,  -85,    0, -116,  -68,  -45,   10,   -9,  -28,   88,    5,  -72,  -77,   69,    6, 
		 -48,   44,   30, -113,  -54,   63,   15,    2,  -63,  -81,  -67,    3,    1,   19, -118,  107,  
		  58, -111,   17,   65,   79,  103,  -36,  -22, -105,  -14,  -49,  -50,  -16,  -76,  -26,  115,
		-106,  -84,  116,   34,  -25,  -83,   53, -123,  -30,   -7,   55,  -24,   28,  117,  -33,  110,   
		  71,  -15,   26,  113,   29,   41,  -59, -119,  111,  -73,   98,   14,  -86,   24,  -66,   27,   
		  -4,   86,   62,   75,  -58,  -46,  121,   32, -102,  -37,  -64,   -2,  120,  -51,   90,  -12,   
		  31,  -35,  -88,   51, -120,    7,  -57,   49,  -79,   18,   16,   89,   39, -128,  -20,   95,   
		  96,   81,  127,  -87,   25,  -75,   74,   13,   45,  -27,  122,  -97, -109,  -55, -100,  -17,  
		 -96,  -32,   59,   77,  -82,   42,  -11,  -80,  -56,  -21,  -69,   60, -125,   83, -103,   97,   
		  23,   43,    4,  126,  -70,  119,  -42,   38,  -31,  105,   20,   99,   85,   33,   12,  125, 
	};
	
	private static byte[] S4 = new byte[] {
		  48,  104, -103,   27, -121,  -71,   33,  120,   80,   57,  -37,  -31,  114,    9,   98,   60,   
		  62,  126,   94, -114,  -15,  -96,  -52,  -93,   42,   29,   -5,  -74,  -42,   32,  -60, -115, 
		-127,  101,  -11, -119,  -53,  -99,  119,  -58,   87,   67,   86,   23,  -44,   64,   26,   77,  
		 -64,   99,  108,  -29,  -73,  -56,  100,  106,   83,  -86,   56, -104,   12,  -12, -101,  -19,  
		 127,   34,  118,  -81,  -35,   58,   11,   88,  103, -120,    6,  -61,   53,   13,    1, -117, 
		-116,  -62,  -26,   95,    2,   36,  117, -109,  102,   30,  -27,  -30,   84,  -40,   16,  -50,  
		 122,  -24,    8,   44,   18, -105,   50,  -85,  -76,   39,   10,   35,  -33,  -17,  -54,  -39,  
		 -72,   -6,  -36,   49,  107,  -47,  -83,   25,   73,  -67,   81, -106,  -18,  -28,  -88,   65, 
		 -38,   -1,  -51,   85, -122,   54,  -66,   97,   82,   -8,  -69,   14, -126,   72,  105, -102,  
		 -32,   71,  -98,   92,    4,   75,   52,   21,  121,   38,  -89,  -34,   41,  -82, -110,  -41, 
		-124,  -23,  -46,  -70,   93,  -13,  -59,  -80,  -65,  -92,   59,  113,   68,   70,   43,   -4,  
		 -21,  111,  -43,  -10,   20,   -2,  124,  112,   90,  125,   -3,   47,   24, -125,   22,  -91, 
		-111,   31,    5, -107,  116,  -87,  -63,   91,   74, -123,  109,   19,    7,   79,   78,   69,  
		 -78,   15,  -55,   28,  -90,  -68,  -20,  115, -112,  123,  -49,   89, -113,  -95,   -7,   45, 
		 -14,  -79,    0, -108,   55,  -97,  -48,   46, -100,  110,   40,   63, -128,  -16,   61,  -45,   
		  37, -118,  -75,  -25,   66,  -77,  -57,  -22,   -9,   76,   17,   51,    3,  -94,  -84,   96, 
	};
	
	private KeyARIA src;
	
	private byte[][] ek, dk;
	private byte[] buf = new byte[16], buf2 = new byte[16];
	private int len;
	
	public ARIA() {}
	
	public ARIA(KeyARIA key) 
	{
		this.setKey(key);
	}

	public KeyARIA getKey() 
	{
		return src;
	}
	
	private void subBytes1(final byte[] buf)
	{
		buf[0 ] = S1[buf[0 ] & 0xFF];
		buf[1 ] = S2[buf[1 ] & 0xFF];
		buf[2 ] = S3[buf[2 ] & 0xFF];
		buf[3 ] = S4[buf[3 ] & 0xFF];
		buf[4 ] = S1[buf[4 ] & 0xFF];
		buf[5 ] = S2[buf[5 ] & 0xFF];
		buf[6 ] = S3[buf[6 ] & 0xFF];
		buf[7 ] = S4[buf[7 ] & 0xFF];
		buf[8 ] = S1[buf[8 ] & 0xFF];
		buf[9 ] = S2[buf[9 ] & 0xFF];
		buf[10] = S3[buf[10] & 0xFF];
		buf[11] = S4[buf[11] & 0xFF];
		buf[12] = S1[buf[12] & 0xFF];
		buf[13] = S2[buf[13] & 0xFF];
		buf[14] = S3[buf[14] & 0xFF];
		buf[15] = S4[buf[15] & 0xFF];
	}
	
	private void subBytes2(final byte[] buf)
	{
		buf[0 ] = S3[buf[0 ] & 0xFF];
		buf[1 ] = S4[buf[1 ] & 0xFF];
		buf[2 ] = S1[buf[2 ] & 0xFF];
		buf[3 ] = S2[buf[3 ] & 0xFF];
		buf[4 ] = S3[buf[4 ] & 0xFF];
		buf[5 ] = S4[buf[5 ] & 0xFF];
		buf[6 ] = S1[buf[6 ] & 0xFF];
		buf[7 ] = S2[buf[7 ] & 0xFF];
		buf[8 ] = S3[buf[8 ] & 0xFF];
		buf[9 ] = S4[buf[9 ] & 0xFF];
		buf[10] = S1[buf[10] & 0xFF];
		buf[11] = S2[buf[11] & 0xFF];
		buf[12] = S3[buf[12] & 0xFF];
		buf[13] = S4[buf[13] & 0xFF];
		buf[14] = S1[buf[14] & 0xFF];
		buf[15] = S2[buf[15] & 0xFF];
	}
	
	private void mix(final byte[] buf, final byte buf2[])
	{
		buf2[0 ]                                                                   = buf[3];
		buf2[1 ] = buf2[10] = buf2[11]                                             = buf[2];
		buf2[2 ] = buf2[5 ] = buf2[7 ] = buf2[12] = buf2[15]                       = buf[1];
		buf2[3 ] = buf2[4 ] = buf2[6 ] = buf2[8 ] = buf2[9 ] = buf2[13] = buf2[14] = buf[0];
		buf2[0 ] ^= buf[4 ];
		buf2[0 ] ^= buf[6 ];
		buf2[0 ] ^= buf[8 ];
		buf2[0 ] ^= buf[9 ];
		buf2[0 ] ^= buf[13];
		buf2[0 ] ^= buf[14];
		buf2[1 ] ^= buf[5 ];
		buf2[1 ] ^= buf[7 ];
		buf2[1 ] ^= buf[8 ];
		buf2[1 ] ^= buf[9 ];
		buf2[1 ] ^= buf[12];
		buf2[1 ] ^= buf[15];
		buf2[2 ] ^= buf[4 ];
		buf2[2 ] ^= buf[6 ];
		buf2[2 ] ^= buf[10];
		buf2[2 ] ^= buf[11];
		buf2[2 ] ^= buf[12];
		buf2[2 ] ^= buf[15];
		buf2[3 ] ^= buf[5 ];
		buf2[3 ] ^= buf[7 ];
		buf2[3 ] ^= buf[10];
		buf2[3 ] ^= buf[11];
		buf2[3 ] ^= buf[13];
		buf2[3 ] ^= buf[14];
		buf2[4 ] ^= buf[2 ];
		buf2[4 ] ^= buf[5 ];
		buf2[4 ] ^= buf[8 ];
		buf2[4 ] ^= buf[11];
		buf2[4 ] ^= buf[14];
		buf2[4 ] ^= buf[15];
		buf2[5 ] ^= buf[3 ];
		buf2[5 ] ^= buf[4 ];
		buf2[5 ] ^= buf[9 ];
		buf2[5 ] ^= buf[10];
		buf2[5 ] ^= buf[14];
		buf2[5 ] ^= buf[15];
		buf2[6 ] ^= buf[2 ];
		buf2[6 ] ^= buf[7 ];
		buf2[6 ] ^= buf[9 ];
		buf2[6 ] ^= buf[10];
		buf2[6 ] ^= buf[12];
		buf2[6 ] ^= buf[13];
		buf2[7 ] ^= buf[3 ];
		buf2[7 ] ^= buf[6 ];
		buf2[7 ] ^= buf[8 ];
		buf2[7 ] ^= buf[11];
		buf2[7 ] ^= buf[12];
		buf2[7 ] ^= buf[13];
		buf2[8 ] ^= buf[1 ];
		buf2[8 ] ^= buf[4 ];
		buf2[8 ] ^= buf[7 ];
		buf2[8 ] ^= buf[10];
		buf2[8 ] ^= buf[13];
		buf2[8 ] ^= buf[15];
		buf2[9 ] ^= buf[1 ];
		buf2[9 ] ^= buf[5 ];
		buf2[9 ] ^= buf[6 ];
		buf2[9 ] ^= buf[11];
		buf2[9 ] ^= buf[12];
		buf2[9 ] ^= buf[14];
		buf2[10] ^= buf[3 ];
		buf2[10] ^= buf[5 ];
		buf2[10] ^= buf[6 ];
		buf2[10] ^= buf[8 ];
		buf2[10] ^= buf[13];
		buf2[10] ^= buf[15];
		buf2[11] ^= buf[3 ];
		buf2[11] ^= buf[4 ];
		buf2[11] ^= buf[7 ];
		buf2[11] ^= buf[9 ];
		buf2[11] ^= buf[12];
		buf2[11] ^= buf[14];
		buf2[12] ^= buf[2 ];
		buf2[12] ^= buf[6 ];
		buf2[12] ^= buf[7 ];
		buf2[12] ^= buf[9 ];
		buf2[12] ^= buf[11];
		buf2[12] ^= buf[12];
		buf2[13] ^= buf[3 ];
		buf2[13] ^= buf[6 ];
		buf2[13] ^= buf[7 ];
		buf2[13] ^= buf[8 ];
		buf2[13] ^= buf[10];
		buf2[13] ^= buf[13];
		buf2[14] ^= buf[3 ];
		buf2[14] ^= buf[4 ];
		buf2[14] ^= buf[5 ];
		buf2[14] ^= buf[9 ];
		buf2[14] ^= buf[11];
		buf2[14] ^= buf[14];
		buf2[15] ^= buf[2 ];
		buf2[15] ^= buf[4 ];
		buf2[15] ^= buf[5 ];
		buf2[15] ^= buf[8 ];
		buf2[15] ^= buf[10];
		buf2[15] ^= buf[15];
	} 
	
	public void FO(long PL, long PR, long KL, long KR)
	{
		PR ^= KL;
		PR ^= KR;
		Bits.longToBytes(PL, buf, 0);
		Bits.longToBytes(PR, buf, 8);
		this.subBytes1(buf);
		this.mix(buf, buf2);
	}
	
	public void FE(long PL, long PR, long KL, long KR)
	{
		PR ^= KL;
		PR ^= KR;
		Bits.longToBytes(PL, buf, 0);
		Bits.longToBytes(PR, buf, 8);
		this.subBytes2(buf);
		this.mix(buf, buf2);
	}
	
	public void FO(byte[] P, byte[] K, byte[] out)
	{
		P[0 ] ^= K[0 ]; 
		P[1 ] ^= K[1 ]; 
		P[2 ] ^= K[2 ]; 
		P[3 ] ^= K[3 ]; 
		P[4 ] ^= K[4 ]; 
		P[5 ] ^= K[5 ]; 
		P[6 ] ^= K[6 ]; 
		P[7 ] ^= K[7 ]; 
		P[8 ] ^= K[8 ]; 
		P[9 ] ^= K[9 ];
		P[10] ^= K[10]; 
		P[11] ^= K[11]; 
		P[12] ^= K[12]; 
		P[13] ^= K[13]; 
		P[14] ^= K[14]; 
		P[15] ^= K[15]; 
		this.subBytes1(P);
		this.mix(P, out);
	}
	
	public void FE(byte[] P, byte[] K, byte[] out)
	{
		P[0 ] ^= K[0 ]; 
		P[1 ] ^= K[1 ]; 
		P[2 ] ^= K[2 ]; 
		P[3 ] ^= K[3 ]; 
		P[4 ] ^= K[4 ]; 
		P[5 ] ^= K[5 ]; 
		P[6 ] ^= K[6 ]; 
		P[7 ] ^= K[7 ]; 
		P[8 ] ^= K[8 ]; 
		P[9 ] ^= K[9 ];
		P[10] ^= K[10]; 
		P[11] ^= K[11]; 
		P[12] ^= K[12]; 
		P[13] ^= K[13]; 
		P[14] ^= K[14]; 
		P[15] ^= K[15]; 
		this.subBytes2(P);
		this.mix(P, out);
	}
	
	public void setKey(KeyARIA t) 
	{
		long[] raw = t.getLongs();
		len = raw.length;
		
		long W0L, W0R,
			 W1L, W1R,
			 W2L, W2R,
			 W3L, W3R;
		
		W0L = raw[0];
		W0R = raw[1];
		long[] s = len == 2 ? C1 : len == 3 ? C2 : C3;
		FO(raw[0], raw[1], s[0], s[1]);
		W1L = Bits.longFromBytes(buf2, 0);
		if(len > 2)
			W1L ^= raw[2];
		W1R = Bits.longFromBytes(buf2, 8);
		if(len > 3)
			W1R ^= raw[3];
		s = len == 2 ? C2 : len == 3 ? C3 : C1;
		FE(W1L, W1R, s[0], s[1]);
		W2L = Bits.longFromBytes(buf2, 0) ^ W0L;
		W2R = Bits.longFromBytes(buf2, 8) ^ W0R;
		s = len == 2 ? C3 : len == 3 ? C1 : C2;
		FO(W2L, W2R, s[0], s[1]);
		W3L = Bits.longFromBytes(buf2, 0) ^ W1L;
		W3R = Bits.longFromBytes(buf2, 8) ^ W1R;
		
		ek = new byte[len == 2 ? 13 : len == 3 ? 15 : 17][16];
		dk = new byte[ek.length][16];
		
		int i = ek.length - 1;
		Bits.longToBytes(W0L ^ ((W1L >>> 19) | (W1R << 45)), ek[0], 0);
		Bits.longToBytes(W0R ^ ((W1R >>> 19) | (W1L << 45)), ek[0], 8);
		System.arraycopy(ek[0], 0, dk[i--], 0, 16);
		
		Bits.longToBytes(W1L ^ ((W2L >>> 19) | (W2R << 45)), ek[1], 0);
		Bits.longToBytes(W1R ^ ((W2R >>> 19) | (W2L << 45)), ek[1], 8);
		this.mix(ek[1], dk[i--]);
		
		Bits.longToBytes(W2L ^ ((W3L >>> 19) | (W3R << 45)), ek[2], 0);
		Bits.longToBytes(W2R ^ ((W3R >>> 19) | (W3L << 45)), ek[2], 8);
		this.mix(ek[2], dk[i--]);
		
		Bits.longToBytes(W3L ^ ((W0L >>> 31) | (W0R << 33)), ek[3], 0);
		Bits.longToBytes(W3R ^ ((W0R >>> 31) | (W0L << 33)), ek[3], 8);
		this.mix(ek[3], dk[i--]);
		
		Bits.longToBytes(W0L ^ ((W1L >>> 31) | (W1R << 33)), ek[4], 0);
		Bits.longToBytes(W0R ^ ((W1R >>> 31) | (W1L << 33)), ek[4], 8);
		this.mix(ek[4], dk[i--]);
		
		Bits.longToBytes(W1L ^ ((W2L >>> 31) | (W2R << 33)), ek[5], 0);
		Bits.longToBytes(W1R ^ ((W2R >>> 31) | (W2L << 33)), ek[5], 8);
		this.mix(ek[5], dk[i--]);
		
		Bits.longToBytes(W2L ^ ((W3L >>> 31) | (W3R << 33)), ek[6], 0);
		Bits.longToBytes(W2R ^ ((W3R >>> 31) | (W3L << 33)), ek[6], 8);
		this.mix(ek[6], dk[i--]);
		
		Bits.longToBytes(W3L ^ ((W0L >>> 31) | (W0R << 33)), ek[7], 0);
		Bits.longToBytes(W3R ^ ((W0R >>> 31) | (W0L << 33)), ek[7], 8);
		this.mix(ek[7], dk[i--]);
		
		Bits.longToBytes(W0L ^ ((W1R >>>  3) | (W1L << 61)), ek[8], 0);
		Bits.longToBytes(W0R ^ ((W1L >>>  3) | (W1R << 61)), ek[8], 8);
		this.mix(ek[8], dk[i--]);
		
		Bits.longToBytes(W1L ^ ((W2R >>>  3) | (W2L << 61)), ek[9], 0);
		Bits.longToBytes(W1R ^ ((W2L >>>  3) | (W2R << 61)), ek[9], 8);
		this.mix(ek[9], dk[i--]);
		
		Bits.longToBytes(W2L ^ ((W3R >>>  3) | (W3L << 61)), ek[10], 0);
		Bits.longToBytes(W2R ^ ((W3L >>>  3) | (W3R << 61)), ek[10], 8);
		this.mix(ek[10], dk[i--]);
		
		Bits.longToBytes(W3L ^ ((W0R >>>  3) | (W0L << 61)), ek[11], 0);
		Bits.longToBytes(W3R ^ ((W0L >>>  3) | (W0R << 61)), ek[11], 8);
		this.mix(ek[11], dk[i--]);
		
		Bits.longToBytes(W0L ^ ((W1R >>> 33) | (W1L << 31)), ek[12], 0);
		Bits.longToBytes(W0R ^ ((W1L >>> 33) | (W1R << 31)), ek[12], 8);
		if(len > 2) {
			this.mix(ek[12], dk[i--]);
			
			Bits.longToBytes(W1L ^ ((W2R >>> 33) | (W2L << 31)), ek[13], 0);
			Bits.longToBytes(W1R ^ ((W2L >>> 33) | (W2R << 31)), ek[13], 8);
			this.mix(ek[13], dk[i--]);
			
			Bits.longToBytes(W2L ^ ((W3R >>> 33) | (W3L << 31)), ek[14], 0);
			Bits.longToBytes(W2R ^ ((W3L >>> 33) | (W3R << 31)), ek[14], 8);
			if(len > 3) {
				this.mix(ek[14], dk[i--]);
				
				Bits.longToBytes(W3L ^ ((W0R >>> 33) | (W0L << 31)), ek[15], 0);
				Bits.longToBytes(W3R ^ ((W0L >>> 33) | (W0R << 31)), ek[15], 8);
				this.mix(ek[15], dk[i--]);
				
				Bits.longToBytes(W0L ^ ((W1R >>> 45) | (W1L << 19)), ek[16], 0);
				Bits.longToBytes(W0R ^ ((W1L >>> 45) | (W1R << 19)), ek[16], 8);
				System.arraycopy(ek[16], 0, dk[i--], 0, 16);
			} else {
				System.arraycopy(ek[14], 0, dk[i--], 0, 16);
			}
		} else {
			System.arraycopy(ek[12], 0, dk[i--], 0, 16);
		}
	}

	public void encryptBlock(byte[] block, int start) 
	{
		System.arraycopy(block, start, buf, 0, 16);
		int i = 0;
		FO(buf , ek[i++], buf2);
		FE(buf2, ek[i++], buf );
		FO(buf , ek[i++], buf2);
		FE(buf2, ek[i++], buf );
		FO(buf , ek[i++], buf2);
		FE(buf2, ek[i++], buf );
		FO(buf , ek[i++], buf2);
		FE(buf2, ek[i++], buf );
		FO(buf , ek[i++], buf2);
		FE(buf2, ek[i++], buf );
		FO(buf , ek[i++], buf2);
		if(len > 2) {
			FE(buf2, ek[i++], buf );
			FO(buf , ek[i++], buf2);
			if(len > 3) {
				FE(buf2, ek[i++], buf );
				FO(buf , ek[i++], buf2);
			}
		}
		buf2[0 ] ^= ek[i  ][0 ];	
		buf2[1 ] ^= ek[i  ][1 ];	
		buf2[2 ] ^= ek[i  ][2 ];	
		buf2[3 ] ^= ek[i  ][3 ];	
		buf2[4 ] ^= ek[i  ][4 ];	
		buf2[5 ] ^= ek[i  ][5 ];	
		buf2[6 ] ^= ek[i  ][6 ];	
		buf2[7 ] ^= ek[i  ][7 ];	
		buf2[8 ] ^= ek[i  ][8 ];	
		buf2[9 ] ^= ek[i  ][9 ];	
		buf2[10] ^= ek[i  ][10];	
		buf2[11] ^= ek[i  ][11];	
		buf2[12] ^= ek[i  ][12];	
		buf2[13] ^= ek[i  ][13];	
		buf2[14] ^= ek[i  ][14];	
		buf2[15] ^= ek[i++][15];	
		this.subBytes2(buf2);
		buf2[0 ] ^= ek[i  ][0 ];	
		buf2[1 ] ^= ek[i  ][1 ];	
		buf2[2 ] ^= ek[i  ][2 ];	
		buf2[3 ] ^= ek[i  ][3 ];	
		buf2[4 ] ^= ek[i  ][4 ];	
		buf2[5 ] ^= ek[i  ][5 ];	
		buf2[6 ] ^= ek[i  ][6 ];	
		buf2[7 ] ^= ek[i  ][7 ];	
		buf2[8 ] ^= ek[i  ][8 ];	
		buf2[9 ] ^= ek[i  ][9 ];	
		buf2[10] ^= ek[i  ][10];	
		buf2[11] ^= ek[i  ][11];	
		buf2[12] ^= ek[i  ][12];	
		buf2[13] ^= ek[i  ][13];	
		buf2[14] ^= ek[i  ][14];	
		buf2[15] ^= ek[i++][15];	
		System.arraycopy(buf2, 0, block, start, 16);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1) 
	{
		System.arraycopy(block, start0, buf, 0, 16);
		int i = 0;
		FO(buf , ek[i++], buf2);
		FE(buf2, ek[i++], buf );
		FO(buf , ek[i++], buf2);
		FE(buf2, ek[i++], buf );
		FO(buf , ek[i++], buf2);
		FE(buf2, ek[i++], buf );
		FO(buf , ek[i++], buf2);
		FE(buf2, ek[i++], buf );
		FO(buf , ek[i++], buf2);
		FE(buf2, ek[i++], buf );
		FO(buf , ek[i++], buf2);
		if(len > 2) {
			FE(buf2, ek[i++], buf );
			FO(buf , ek[i++], buf2);
			if(len > 3) {
				FE(buf2, ek[i++], buf );
				FO(buf , ek[i++], buf2);
			}
		}
		buf2[0 ] ^= ek[i  ][0 ];	
		buf2[1 ] ^= ek[i  ][1 ];	
		buf2[2 ] ^= ek[i  ][2 ];	
		buf2[3 ] ^= ek[i  ][3 ];	
		buf2[4 ] ^= ek[i  ][4 ];	
		buf2[5 ] ^= ek[i  ][5 ];	
		buf2[6 ] ^= ek[i  ][6 ];	
		buf2[7 ] ^= ek[i  ][7 ];	
		buf2[8 ] ^= ek[i  ][8 ];	
		buf2[9 ] ^= ek[i  ][9 ];	
		buf2[10] ^= ek[i  ][10];	
		buf2[11] ^= ek[i  ][11];	
		buf2[12] ^= ek[i  ][12];	
		buf2[13] ^= ek[i  ][13];	
		buf2[14] ^= ek[i  ][14];	
		buf2[15] ^= ek[i++][15];	
		this.subBytes2(buf2);
		buf2[0 ] ^= ek[i  ][0 ];	
		buf2[1 ] ^= ek[i  ][1 ];	
		buf2[2 ] ^= ek[i  ][2 ];	
		buf2[3 ] ^= ek[i  ][3 ];	
		buf2[4 ] ^= ek[i  ][4 ];	
		buf2[5 ] ^= ek[i  ][5 ];	
		buf2[6 ] ^= ek[i  ][6 ];	
		buf2[7 ] ^= ek[i  ][7 ];	
		buf2[8 ] ^= ek[i  ][8 ];	
		buf2[9 ] ^= ek[i  ][9 ];	
		buf2[10] ^= ek[i  ][10];	
		buf2[11] ^= ek[i  ][11];	
		buf2[12] ^= ek[i  ][12];	
		buf2[13] ^= ek[i  ][13];	
		buf2[14] ^= ek[i  ][14];	
		buf2[15] ^= ek[i++][15];	
		System.arraycopy(buf2, 0, out, start1, 16);
	}

	public void decryptBlock(byte[] block, int start) 
	{
		System.arraycopy(block, start, buf, 0, 16);
		int i = 0;
		FO(buf , dk[i++], buf2);
		FE(buf2, dk[i++], buf );
		FO(buf , dk[i++], buf2);
		FE(buf2, dk[i++], buf );
		FO(buf , dk[i++], buf2);
		FE(buf2, dk[i++], buf );
		FO(buf , dk[i++], buf2);
		FE(buf2, dk[i++], buf );
		FO(buf , dk[i++], buf2);
		FE(buf2, dk[i++], buf );
		FO(buf , dk[i++], buf2);
		if(len > 2) {
			FE(buf2, dk[i++], buf );
			FO(buf , dk[i++], buf2);
			if(len > 3) {
				FE(buf2, dk[i++], buf );
				FO(buf , dk[i++], buf2);
			}
		}
		buf2[0 ] ^= dk[i  ][0 ];	
		buf2[1 ] ^= dk[i  ][1 ];	
		buf2[2 ] ^= dk[i  ][2 ];	
		buf2[3 ] ^= dk[i  ][3 ];	
		buf2[4 ] ^= dk[i  ][4 ];	
		buf2[5 ] ^= dk[i  ][5 ];	
		buf2[6 ] ^= dk[i  ][6 ];	
		buf2[7 ] ^= dk[i  ][7 ];	
		buf2[8 ] ^= dk[i  ][8 ];	
		buf2[9 ] ^= dk[i  ][9 ];	
		buf2[10] ^= dk[i  ][10];	
		buf2[11] ^= dk[i  ][11];	
		buf2[12] ^= dk[i  ][12];	
		buf2[13] ^= dk[i  ][13];	
		buf2[14] ^= dk[i  ][14];	
		buf2[15] ^= dk[i++][15];	
		this.subBytes2(buf2);
		buf2[0 ] ^= dk[i  ][0 ];	
		buf2[1 ] ^= dk[i  ][1 ];	
		buf2[2 ] ^= dk[i  ][2 ];	
		buf2[3 ] ^= dk[i  ][3 ];	
		buf2[4 ] ^= dk[i  ][4 ];	
		buf2[5 ] ^= dk[i  ][5 ];	
		buf2[6 ] ^= dk[i  ][6 ];	
		buf2[7 ] ^= dk[i  ][7 ];	
		buf2[8 ] ^= dk[i  ][8 ];	
		buf2[9 ] ^= dk[i  ][9 ];	
		buf2[10] ^= dk[i  ][10];	
		buf2[11] ^= dk[i  ][11];	
		buf2[12] ^= dk[i  ][12];	
		buf2[13] ^= dk[i  ][13];	
		buf2[14] ^= dk[i  ][14];	
		buf2[15] ^= dk[i++][15];	
		System.arraycopy(buf2, 0, block, start, 16);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1) 
	{
		System.arraycopy(block, start0, buf, 0, 16);
		int i = 0;
		FO(buf , dk[i++], buf2);
		FE(buf2, dk[i++], buf );
		FO(buf , dk[i++], buf2);
		FE(buf2, dk[i++], buf );
		FO(buf , dk[i++], buf2);
		FE(buf2, dk[i++], buf );
		FO(buf , dk[i++], buf2);
		FE(buf2, dk[i++], buf );
		FO(buf , dk[i++], buf2);
		FE(buf2, dk[i++], buf );
		FO(buf , dk[i++], buf2);
		if(len > 2) {
			FE(buf2, dk[i++], buf );
			FO(buf , dk[i++], buf2);
			if(len > 3) {
				FE(buf2, dk[i++], buf );
				FO(buf , dk[i++], buf2);
			}
		}
		buf2[0 ] ^= dk[i  ][0 ];	
		buf2[1 ] ^= dk[i  ][1 ];	
		buf2[2 ] ^= dk[i  ][2 ];	
		buf2[3 ] ^= dk[i  ][3 ];	
		buf2[4 ] ^= dk[i  ][4 ];	
		buf2[5 ] ^= dk[i  ][5 ];	
		buf2[6 ] ^= dk[i  ][6 ];	
		buf2[7 ] ^= dk[i  ][7 ];	
		buf2[8 ] ^= dk[i  ][8 ];	
		buf2[9 ] ^= dk[i  ][9 ];	
		buf2[10] ^= dk[i  ][10];	
		buf2[11] ^= dk[i  ][11];	
		buf2[12] ^= dk[i  ][12];	
		buf2[13] ^= dk[i  ][13];	
		buf2[14] ^= dk[i  ][14];	
		buf2[15] ^= dk[i++][15];	
		this.subBytes2(buf2);
		buf2[0 ] ^= dk[i  ][0 ];	
		buf2[1 ] ^= dk[i  ][1 ];	
		buf2[2 ] ^= dk[i  ][2 ];	
		buf2[3 ] ^= dk[i  ][3 ];	
		buf2[4 ] ^= dk[i  ][4 ];	
		buf2[5 ] ^= dk[i  ][5 ];	
		buf2[6 ] ^= dk[i  ][6 ];	
		buf2[7 ] ^= dk[i  ][7 ];	
		buf2[8 ] ^= dk[i  ][8 ];	
		buf2[9 ] ^= dk[i  ][9 ];	
		buf2[10] ^= dk[i  ][10];	
		buf2[11] ^= dk[i  ][11];	
		buf2[12] ^= dk[i  ][12];	
		buf2[13] ^= dk[i  ][13];	
		buf2[14] ^= dk[i  ][14];	
		buf2[15] ^= dk[i++][15];	
		System.arraycopy(buf2, 0, out, start1, 16);
	}
	
	public void reset() {}

	public void wipe() 
	{
		for(int i = 0; i < ek.length; i++) {
			Arrays.fill(ek[i], (byte) 0);
			Arrays.fill(dk[i], (byte) 0);
		}
		Arrays.fill(ek, null);
		Arrays.fill(dk, null);
		ek = dk = null;
		Arrays.fill(buf, (byte) 0);
		Arrays.fill(buf2, (byte) 0);
	}

	public int plaintextSize() 
	{
		return 16;
	}

	public int ciphertextSize() 
	{
		return 16;
	}
	
	public static int test()
	{
		long[] ints1 = new long[2];
		long[] ints2 = new long[4];
		RandUtils.fillArr(ints1);
		RandUtils.fillArr(ints2);
		KeyARIA a1 = new KeyARIA(ints1);
		KeyARIA a2 = new KeyARIA(ints2);
		ARIA aes = new ARIA(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

	public KeyFactory<KeyARIA> keyFactory()
	{
		return KeyARIA.factory;
	}

}

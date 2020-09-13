package claire.util.crypto.cipher.primitive.block;

import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.block.KeyCamellia;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class Camellia 
	   implements ISymmetric<KeyCamellia> {
	
	private static final long SIG1 = 0xa09e667f3bcc908bL,
							  SIG2 = 0xb67ae8584caa73b2L,
							  SIG3 = 0xc6ef372fe94f82beL,
							  SIG4 = 0x54ff53a5f1d36f1cL,
							  SIG5 = 0x10e527fade682d1dL,
							  SIG6 = 0xb05688c2b3e6c1fdL;
	
	private static final byte[] S1 = new byte[] {
		 112, -126,   44,  -20,  -77,   39,  -64,  -27,  -28, -123,   87,   53,  -22,   12,  -82,   65,   
		  35,  -17,  107, -109,   69,   25,  -91,   33,  -19,   14,   79,   78,   29,  101, -110,  -67, 
		-122,  -72,  -81, -113,  124,  -21,   31,  -50,   62,   48,  -36,   95,   94,  -59,   11,   26,  
		 -90,  -31,   57,  -54,  -43,   71,   93,   61,  -39,    1,   90,  -42,   81,   86,  108,   77,
		-117,   13, -102,  102,   -5,  -52,  -80,   45,  116,   18,   43,   32,  -16,  -79, -124, -103, 
		 -33,   76,  -53,  -62,   52,  126,  118,    5,  109,  -73,  -87,   49,  -47,   23,    4,  -41,   
		  20,   88,   58,   97,  -34,   27,   17,   28,   50,   15, -100,   22,   83,   24,  -14,   34,   
		  -2,   68,  -49,  -78,  -61,  -75,  122, -111,   36,    8,  -24,  -88,   96,   -4,  105,   80, 
		 -86,  -48,  -96,  125,  -95, -119,   98, -105,   84,   91,   30, -107,  -32,   -1,  100,  -46,  
		  16,  -60,    0,   72,  -93,   -9,  117,  -37, -118,    3,  -26,  -38,    9,   63,  -35, -108, 
		-121,   92, -125,    2,  -51,   74, -112,   51,  115,  103,  -10,  -13,  -99,  127,  -65,  -30,   
		  82, -101,  -40,   38,  -56,   55,  -58,   59, -127, -106,  111,   75,   19,  -66,   99,   46, 
		 -23,  121,  -89, -116,  -97,  110,  -68, -114,   41,  -11,   -7,  -74,   47,   -3,  -76,   89,  
		 120, -104,    6,  106,  -25,   70,  113,  -70,  -44,   37,  -85,   66, -120,  -94, -115,   -6,  
		 114,    7,  -71,   85,   -8,  -18,  -84,   10,   54,   73,   42,  104,   60,   56,  -15,  -92,   
		  64,   40,  -45,  123,  -69,  -55,   67,  -63,   21,  -29,  -83,  -12,  119,  -57, -128,  -98, 
	};
	
	private static final byte[] S2 = new byte[] {
		 -32,    5,   88,  -39,  103,   78, -127,  -53,  -55,   11,  -82,  106,  -43,   24,   93, -126,   
		  70,  -33,  -42,   39, -118,   50,   75,   66,  -37,   28,  -98, -100,   58,  -54,   37,  123,  
		  13,  113,   95,   31,   -8,  -41,   62,  -99,  124,   96,  -71,  -66,  -68, -117,   22,   52,   
		  77,  -61,  114, -107,  -85, -114,  -70,  122,  -77,    2,  -76,  -83,  -94,  -84,  -40, -102,   
		  23,   26,   53,  -52,   -9, -103,   97,   90,  -24,   36,   86,   64,  -31,   99,    9,   51,  
		 -65, -104, -105, -123,  104,   -4,  -20,   10,  -38,  111,   83,   98,  -93,   46,    8,  -81,   
		  40,  -80,  116,  -62,  -67,   54,   34,   56,  100,   30,   57,   44,  -90,   48,  -27,   68,   
		  -3, -120,  -97,  101, -121,  107,  -12,   35,   72,   16,  -47,   81,  -64,   -7,  -46,  -96,   
		  85,  -95,   65,   -6,   67,   19,  -60,   47,  -88,  -74,   60,   43,  -63,   -1,  -56,  -91,   
		  32, -119,    0, -112,   71,  -17,  -22,  -73,   21,    6,  -51,  -75,   18,  126,  -69,   41,   
		  15,  -72,    7,    4, -101, -108,   33,  102,  -26,  -50,  -19,  -25,   59,   -2,  127,  -59,  
		 -92,   55,  -79,   76, -111,  110, -115,  118,    3,   45,  -34, -106,   38,  125,  -58,   92,  
		 -45,  -14,   79,   25,   63,  -36,  121,   29,   82,  -21,  -13,  109,   94,   -5,  105,  -78,  
		 -16,   49,   12,  -44,  -49, -116,  -30,  117,  -87,   74,   87, -124,   17,   69,   27,  -11,  
		 -28,   14,  115,  -86,  -15,  -35,   89,   20,  108, -110,   84,  -48,  120,  112,  -29,   73, 
		-128,   80,  -89,  -10,  119, -109, -122, -125,   42,  -57,   91,  -23,  -18, -113,    1,   61,
	};
	
	private static final byte[] S3 = new byte[] {
		  56,   65,   22,  118,  -39, -109,   96,  -14,  114,  -62,  -85, -102,  117,    6,   87,  -96, 
		-111,   -9,  -75,  -55,  -94, -116,  -46, -112,  -10,    7,  -89,   39, -114,  -78,   73,  -34,  
		  67,   92,  -41,  -57,   62,  -11, -113,  103,   31,   24,  110,  -81,   47,  -30, -123,   13,   
		  83,  -16, -100,  101,  -22,  -93,  -82,  -98,  -20, -128,   45,  107,  -88,   43,   54,  -90,  
		 -59, -122,   77,   51,   -3,  102,   88, -106,   58,    9, -107,   16,  120,  -40,   66,  -52,  
		 -17,   38,  -27,   97,   26,   63,   59, -126,  -74,  -37,  -44, -104,  -24, -117,    2,  -21,   
		  10,   44,   29,  -80,  111, -115, -120,   14,   25, -121,   78,   11,  -87,   12,  121,   17,  
		 127,   34,  -25,   89,  -31,  -38,   61,  -56,   18,    4,  116,   84,   48,  126,  -76,   40,  
		  85,  104,   80,  -66,  -48,  -60,   49,  -53,   42,  -83,   15,  -54,  112,   -1,   50,  105,    
		   8,   98,    0,   36,  -47,   -5,  -70,  -19,   69, -127,  115,  109, -124,  -97,  -18,   74,  
		 -61,   46,  -63,    1,  -26,   37,   72, -103,  -71,  -77,  123,   -7,  -50,  -65,  -33,  113,   
		  41,  -51,  108,   19,  100, -101,   99,  -99,  -64,   75,  -73,  -91, -119,   95,  -79,   23,  
		 -12,  -68,  -45,   70,  -49,   55,   94,   71, -108,   -6,   -4,   91, -105,   -2,   90,  -84,   
		  60,   76,    3,   53,  -13,   35,  -72,   93,  106, -110,  -43,   33,   68,   81,  -58,  125,  
		  57, -125,  -36,  -86,  124,  119,   86,    5,   27,  -92,   21,   52,   30,   28,   -8,   82,  
		  32,   20,  -23,  -67,  -35,  -28,  -95,  -32, -118,  -15,  -42,  122,  -69,  -29,   64,   79, 
	};
	
	private static final byte[] S4 = new byte[] {
		 112,   44,  -77,  -64,  -28,   87,  -22,  -82,   35,  107,   69,  -91,  -19,   79,   29, -110, 
		-122,  -81,  124,   31,   62,  -36,   94,   11,  -90,   57,  -43,   93,  -39,   90,   81,  108, 
		-117, -102,   -5,  -80,  116,   43,  -16, -124,  -33,  -53,   52,  118,  109,  -87,  -47,    4,   
		  20,   58,  -34,   17,   50, -100,   83,  -14,   -2,  -49,  -61,  122,   36,  -24,   96,  105,  
		 -86,  -96,  -95,   98,   84,   30,  -32,  100,   16,    0,  -93,  117, -118,  -26,    9,  -35,
		-121, -125,  -51, -112,  115,  -10,  -99,  -65,   82,  -40,  -56,  -58, -127,  111,   19,   99,  
		 -23,  -89,  -97,  -68,   41,   -7,   47,  -76,  120,    6,  -25,  113,  -44,  -85, -120, -115, 
		 114,  -71,   -8,  -84,   54,   42,   60,  -15,   64,  -45,  -69,   67,   21,  -83,  119, -128, 
		-126,  -20,   39,  -27, -123,   53,   12,   65,  -17, -109,   25,   33,   14,   78,  101,  -67,  
		 -72, -113,  -21,  -50,   48,   95,  -59,   26,  -31,  -54,   71,   61,    1,  -42,   86,   77,   
		  13,  102,  -52,   45,   18,   32,  -79, -103,   76,  -62,  126,    5,  -73,   49,   23,  -41,   
		  88,   97,   27,   28,   15,   22,   24,   34,   68,  -78,  -75, -111,    8,  -88,   -4,   80,  
		 -48,  125, -119, -105,   91, -107,   -1,  -46,  -60,   72,   -9,  -37,    3,  -38,   63, -108,  
		  92,    2,   74,   51,  103,  -13,  127,  -30, -101,   38,   55,   59, -106,   75,  -66,   46,  
		 121, -116,  110, -114,  -11,  -74,   -3,   89, -104,  106,   70,  -70,   37,   66,  -94,   -6,    
		   7,   85,  -18,   10,   73,  104,   56,  -92,   40,  123,  -55,  -63,  -29,  -12,  -57,  -98, 
	};
	
	private final byte[] buf = new byte[8];
	private long[] ekw, eke, ek;
	private boolean lon;

	private KeyCamellia key;
	
	public Camellia() {}
	
	public Camellia(KeyCamellia key)
	{
		this.setKey(key);
	}

	public KeyCamellia getKey() 
	{
		return key;
	}
	
	private long F(long in, long k)
	{
		Bits.longToBytes(in ^ k, buf, 0);
		buf[0] = S1[buf[0] & 0xFF];
		buf[1] = S2[buf[1] & 0xFF];
		buf[2] = S3[buf[2] & 0xFF];
		buf[3] = S4[buf[3] & 0xFF];
		buf[4] = S2[buf[4] & 0xFF];
		buf[5] = S3[buf[5] & 0xFF];
		buf[6] = S4[buf[6] & 0xFF];
		buf[7] = S1[buf[7] & 0xFF];
		int i1 = Bits.intFromBytes(buf, 0), i2 = Bits.intFromBytes(buf, 4);
		i1 ^= (i2 <<  8) | (i2 >> 24);
	    i2 ^= (i1 << 16) | (i1 >> 16);
	    i1 ^= (i2 >>  8) | (i2 << 24);
	    i2 ^= (i1 >>  8) | (i1 << 24);
		return (((long) i1) << 32) | (i2 & 0xFFFFFFFFL);
	}
	
	private long FL(long in, long k)
	{
		int i1, i2, k1, k2;
		i1 = (int) (in >>> 32);
		i2 = (int)  in;
		k1 = (int) (k >>> 32);
		k2 = (int)  k;
		i2 ^= Bits.rotateLeft(i1 & k1, 1);
		i1 ^= i2 | k2;
		return (((long) i1) << 32) | (i2 & 0xFFFFFFFFL);
	}
	
	private long IFL(long in, long k)
	{
		int i1, i2, k1, k2;
		i1 = (int) (in >>> 32);
		i2 = (int)  in;
		k1 = (int) (k >>> 32);
		k2 = (int)  k;
		i1 ^= i2 | k2;
		i2 ^= Bits.rotateLeft(i1 & k1, 1);
		return (((long) i1) << 32) | (i2 & 0xFFFFFFFFL);
	}

	public void setKey(KeyCamellia t) 
	{
		this.key = t;
		long[] src = t.getLongs();
		
		long KAL, KAR,
			 D1, D2,
			 KRL, KRR;
		
		if(src.length == 3) {
			KRL = src[2];
			KRR = ~src[2];
		} else if(src.length == 4) {
			KRL = src[2];
			KRR = src[3];
		} else {
			KRL = KRR = 0L;
		}
		
		lon = src.length > 2;
		
		ekw = new long[4];
		eke = new long[lon ? 6 : 4];
		ek = new long[lon ? 24 : 18];

		D1 = src[0] ^ KRL;
		D2 = src[1] ^ KRR;
		D2 ^= F(D1, SIG1);
		D1 ^= F(D2, SIG2);
		D1 ^= src[0];
		D2 ^= src[1];
		D2 ^= F(D1, SIG3);
		D1 ^= F(D2, SIG4);
		KAL = D1;
		KAR = D2;
		
		if(lon) {
			D1 = KAL ^ KRL;
			D2 = KAR ^ KRR;
			D2 ^= F(D1, SIG5);
			D1 ^= F(D2, SIG6);
			long KBL = D1, KBR = D2;
			
			ekw[0 ] = src[0];
			ekw[1 ] = src[1];
			ek [0 ] = KBL;
			ek [1 ] = KBR;
			ek [2 ] = (KRL << 15) | (KRR >>> 49);
			ek [3 ] = (KRR << 15) | (KRL >>> 49);
			ek [4 ] = (KAL << 15) | (KAR >>> 49);
			ek [5 ] = (KAR << 15) | (KAL >>> 49);
			eke[0 ] = (KRL << 30) | (KRR >>> 34);
			eke[1 ] = (KRR << 30) | (KRL >>> 34);
			ek [6 ] = (KBL << 30) | (KBR >>> 34);
			ek [7 ] = (KBR << 30) | (KBL >>> 34);
			ek [8 ] = (src[0] << 45) | (src[1] >>> 19);
			ek [9 ] = (src[1] << 45) | (src[0] >>> 19);
			ek [10] = (KAL << 45) | (KAR >>> 19);
			ek [11] = (KAR << 45) | (KAL >>> 19);
			eke[2 ] = (src[0] << 60) | (src[1] >>>  4);
			eke[3 ] = (src[1] << 60) | (src[0] >>>  4);
			ek [12] = (KRL << 60) | (KRR >>>  4);
			ek [13] = (KRR << 60) | (KRL >>>  4);
			ek [14] = (KBL << 60) | (KBR >>>  4);
			ek [15] = (KBR << 60) | (KBL >>>  4);
			ek [16] = (src[1] << 13) | (src[0] >>> 49);
			ek [17] = (src[0] << 13) | (src[1] >>> 49);
			eke[4 ] = (KAR << 13) | (KAL >>> 49);
			eke[5 ] = (KAL << 13) | (KAR >>> 49);
			ek [18] = (KRR << 30) | (KRL >>> 34);
			ek [19] = (KRL << 30) | (KRR >>> 34);
			ek [20] = (KAR << 30) | (KAL >>> 34);
			ek [21] = (KAL << 30) | (KAR >>> 34);
			ek [22] = (src[1] << 47) | (src[0] >>> 17);
			ek [23] = (src[0] << 47) | (src[1] >>> 17);
			ekw[2 ] = (KBR << 47) | (KBL >>> 17);
			ekw[3 ] = (KBL << 47) | (KBR >>> 17);
		} else {
			ekw[0 ] = src[0];
			ekw[1 ] = src[1];
			ek [0 ] = KAL;
			ek [1 ] = KAR;
			ek [2 ] = (src[0] << 15) | (src[1] >>> 49);
			ek [3 ] = (src[1] << 15) | (src[0] >>> 49);
			ek [4 ] = (KAL << 15) | (KAR >>> 49);
			ek [5 ] = (KAR << 15) | (KAL >>> 49);
			eke[0 ] = (KAL << 30) | (KAR >>> 34);
			eke[1 ] = (KAR << 30) | (KAL >>> 34);
			ek [6 ] = (src[0] << 45) | (src[1] >>> 19);
			ek [7 ] = (src[1] << 45) | (src[0] >>> 19);
			ek [8 ] = (KAL << 45) | (KAR >>> 19);
			ek [9 ] = (src[1] << 60) | (src[0] >>>  4);
			ek [10] = (KAL << 60) | (KAR >>>  4);
			ek [11] = (KAR << 60) | (KAL >>>  4);
			eke[2 ] = (src[1] << 13) | (src[0] >>> 49);
			eke[3 ] = (src[0] << 13) | (src[1] >>> 49);
			ek [12] = (src[1] << 30) | (src[0] >>> 34);
			ek [13] = (src[0] << 30) | (src[1] >>> 34);
			ek [14] = (KAR << 30) | (KAL >>> 34);
			ek [15] = (KAL << 30) | (KAR >>> 34);
			ek [16] = (src[1] << 47) | (src[0] >>> 17);
			ek [17] = (src[0] << 47) | (src[1] >>> 17);
			ekw[2 ] = (KAR << 47) | (KAL >>> 17);
			ekw[3 ] = (KAL << 47) | (KAR >>> 17);
		}
	}

	public void encryptBlock(byte[] block, int start) 
	{
		long D1, D2;
		D1 = Bits.longFromBytes(block, start    ) ^ ekw[0];
		D2 = Bits.longFromBytes(block, start + 8) ^ ekw[1];
		D2 ^= F(D1, ek[0 ]);
		D1 ^= F(D2, ek[1 ]);
		D2 ^= F(D1, ek[2 ]);
		D1 ^= F(D2, ek[3 ]);
		D2 ^= F(D1, ek[4 ]);
		D1 ^= F(D2, ek[5 ]);
		D1  =  FL(D1, eke[0]);
		D2  = IFL(D2, eke[1]);
		D2 ^= F(D1, ek[6 ]);
		D1 ^= F(D2, ek[7 ]);
		D2 ^= F(D1, ek[8 ]);
		D1 ^= F(D2, ek[9 ]);
		D2 ^= F(D1, ek[10]);
		D1 ^= F(D2, ek[11]);
		D1  =  FL(D1, eke[2]);
		D2  = IFL(D2, eke[3]);
		D2 ^= F(D1, ek[12]);
		D1 ^= F(D2, ek[13]);
		D2 ^= F(D1, ek[14]);
		D1 ^= F(D2, ek[15]);
		D2 ^= F(D1, ek[16]);
		D1 ^= F(D2, ek[17]);
		if(lon) {
			D1  =  FL(D1, eke[4]);
			D2  = IFL(D2, eke[5]);
			D2 ^= F(D1, ek[18]);
			D1 ^= F(D2, ek[19]);
			D2 ^= F(D1, ek[20]);
			D1 ^= F(D2, ek[21]);
			D2 ^= F(D1, ek[22]);
			D1 ^= F(D2, ek[23]);
		}
		D2 ^= ekw[2];
		D1 ^= ekw[3];
		
		Bits.longToBytes(D2, block, 0);
		Bits.longToBytes(D1, block, 8);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1) 
	{
		long D1, D2;
		D1 = Bits.longFromBytes(block, start0    ) ^ ekw[0];
		D2 = Bits.longFromBytes(block, start0 + 8) ^ ekw[1];
		D2 ^= F(D1, ek[0 ]);
		D1 ^= F(D2, ek[1 ]);
		D2 ^= F(D1, ek[2 ]);
		D1 ^= F(D2, ek[3 ]);
		D2 ^= F(D1, ek[4 ]);
		D1 ^= F(D2, ek[5 ]);
		D1  =  FL(D1, eke[0]);
		D2  = IFL(D2, eke[1]);
		D2 ^= F(D1, ek[6 ]);
		D1 ^= F(D2, ek[7 ]);
		D2 ^= F(D1, ek[8 ]);
		D1 ^= F(D2, ek[9 ]);
		D2 ^= F(D1, ek[10]);
		D1 ^= F(D2, ek[11]);
		D1  =  FL(D1, eke[2]);
		D2  = IFL(D2, eke[3]);
		D2 ^= F(D1, ek[12]);
		D1 ^= F(D2, ek[13]);
		D2 ^= F(D1, ek[14]);
		D1 ^= F(D2, ek[15]);
		D2 ^= F(D1, ek[16]);
		D1 ^= F(D2, ek[17]);
		if(lon) {
			D1  =  FL(D1, eke[4]);
			D2  = IFL(D2, eke[5]);
			D2 ^= F(D1, ek[18]);
			D1 ^= F(D2, ek[19]);
			D2 ^= F(D1, ek[20]);
			D1 ^= F(D2, ek[21]);
			D2 ^= F(D1, ek[22]);
			D1 ^= F(D2, ek[23]);
		}
		D2 ^= ekw[2];
		D1 ^= ekw[3];
		
		Bits.longToBytes(D2, out, start1    );
		Bits.longToBytes(D1, out, start1 + 8);
	}
	
	public void decryptBlock(byte[] block, int start) 
	{
		long D1, D2;
		D1 = Bits.longFromBytes(block, start    ) ^ ekw[2];
		D2 = Bits.longFromBytes(block, start + 8) ^ ekw[3];
		int i = lon ? 23 : 17, j = lon ? 5 : 3;
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D1  =  FL(D1, eke[j--]);
		D2  = IFL(D2, eke[j--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D1  =  FL(D1, eke[j--]);
		D2  = IFL(D2, eke[j--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		if(lon) {
			D1  =  FL(D1, eke[j--]);
			D2  = IFL(D2, eke[j--]);
			D2 ^= F(D1, ek[i--]);
			D1 ^= F(D2, ek[i--]);
			D2 ^= F(D1, ek[i--]);
			D1 ^= F(D2, ek[i--]);
			D2 ^= F(D1, ek[i--]);
			D1 ^= F(D2, ek[i--]);
		}
		D2 ^= ekw[0];
		D1 ^= ekw[1];
		
		Bits.longToBytes(D2, block, start    );
		Bits.longToBytes(D1, block, start + 8);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1) 
	{
		long D1, D2;
		D1 = Bits.longFromBytes(block, start0    ) ^ ekw[2];
		D2 = Bits.longFromBytes(block, start0 + 8) ^ ekw[3];
		int i = lon ? 23 : 17, j = lon ? 5 : 3;
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D1  =  FL(D1, eke[j--]);
		D2  = IFL(D2, eke[j--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D1  =  FL(D1, eke[j--]);
		D2  = IFL(D2, eke[j--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		D2 ^= F(D1, ek[i--]);
		D1 ^= F(D2, ek[i--]);
		if(lon) {
			D1  =  FL(D1, eke[j--]);
			D2  = IFL(D2, eke[j--]);
			D2 ^= F(D1, ek[i--]);
			D1 ^= F(D2, ek[i--]);
			D2 ^= F(D1, ek[i--]);
			D1 ^= F(D2, ek[i--]);
			D2 ^= F(D1, ek[i--]);
			D1 ^= F(D2, ek[i--]);
		}
		D2 ^= ekw[0];
		D1 ^= ekw[1];
		
		Bits.longToBytes(D2, out, start1    );
		Bits.longToBytes(D1, out, start1 + 8);
	}

	public void reset() {}

	public void wipe() 
	{
		Arrays.fill(ek, 0);
		Arrays.fill(eke, 0);
		Arrays.fill(ekw, 0);
		lon = false;
		ek = eke = ekw = null;
		Arrays.fill(buf, (byte) 0);
		key = null;
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
		KeyCamellia a1 = new KeyCamellia(ints1);
		KeyCamellia a2 = new KeyCamellia(ints2);
		Camellia aes = new Camellia(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}

	public KeyFactory<KeyCamellia> keyFactory()
	{
		return KeyCamellia.factory;
	}

}

package claire.util.crypto.cipher.primitive;

public class Twofish {
	
	private static final byte[][] PCUBE =
		{
			{
					-87,  103,  -77,  -24,    4,   -3,  -93,  118, 
				   -102, -110, -128,  120,  -28,  -35,  -47,   56,   
				     13,  -58,   53, -104,   24,   -9,  -20,  108,   
				     67,  117,   55,   38,   -6,   19, -108,   72,  
				    -14,  -48, -117,   48, -124,   84,  -33,   35,   
				     25,   91,   61,   89,  -13,  -82,  -94, -126,   
				     99,    1, -125,   46,  -39,   81, -101,  124,  
				    -90,  -21,  -91,  -66,   22,   12,  -29,   97,  
				    -64, -116,   58,  -11,  115,   44,   37,   11,  
				    -69,   78, -119,  107,   83,  106,  -76,  -15,  
				    -31,  -26,  -67,   69,  -30,  -12,  -74,  102,  
				    -52, -107,    3,   86,  -44,   28,   30,  -41,   
				     -5,  -61, -114,  -75,  -23,  -49,  -65,  -70,  
				    -22,  119,   57,  -81,   51,  -55,   98,  113, 
				   -127,  121,    9,  -83,   36,  -51,   -7,  -40,  
				    -27,  -59,  -71,   77,   68,    8, -122,  -25,  
				    -95,   29,  -86,  -19,    6,  112,  -78,  -46,   
				     65,  123,  -96,   17,   49,  -62,   39, -112,   
				     32,  -10,   96,   -1, -106,   92,  -79,  -85,  
				    -98, -100,   82,   27,   95, -109,   10,  -17, 
				   -111, -123,   73,  -18,   45,   79, -113,   59,   
				     71, -121,  109,   70,  -42,   62,  105,  100,   
				     42,  -50,  -53,   47,   -4, -105,    5,  122,  
				    -84,  127,  -43,   26,   75,   14,  -89,   90,   
				     40,   20,   63,   41, -120,   60,   76,    2,  
				    -72,  -38,  -80,   23,   85,   31, -118,  125,   
				     87,  -57, -115,  116,  -73,  -60,  -97,  114,  
				    126,   21,   34,   18,   88,    7, -103,   52,  
				    110,   80,  -34,  104,  101,  -68,  -37,   -8,  
				    -56,  -88,   43,   64,  -36,   -2,   50,  -92,  
				    -54,   16,   33,  -16,  -45,   93,   15,    0,  
				    111,  -99,   54,   66,   74,   94,  -63,  -32, 
			},
			{
				 	117,  -13,  -58,  -12,  -37,  123,   -5,  -56,   
				 	 74,  -45,  -26,  107,   69,  125,  -24,   75,  
				 	-42,   50,  -40,   -3,   55,  113,  -15,  -31,   
				 	 48,   15,   -8,   27, -121,   -6,    6,   63,   
				 	 94,  -70,  -82,   91, -118,    0,  -68,  -99,  
				 	109,  -63,  -79,   14, -128,   93,  -46,  -43,  
				 	-96, -124,    7,   20,  -75, -112,   44,  -93,  
				 	-78,  115,   76,   84, -110,  116,   54,   81,   
				 	 56,  -80,  -67,   90,   -4,   96,   98, -106,  
				 	108,   66,   -9,   16,  124,   40,   39, -116,   
				 	 19, -107, -100,  -57,   36,   70,   59,  112,  
				 	-54,  -29, -123,  -53,   17,  -48, -109,  -72,  
				 	-90, -125,   32,   -1,  -97,  119,  -61,  -52,    
				 	  3,  111,    8,  -65,   64,  -25,   43,  -30,  
				 	121,   12,  -86, -126,   65,   58,  -22,  -71,  
				 	-28, -102,  -92, -105,  126,  -38,  122,   23,  
				 	102, -108,  -95,   29,   61,  -16,  -34,  -77,   
				 	 11,  114,  -89,   28,  -17,  -47,   83,   62,
				   -113,   51,   38,   95,  -20,  118,   42,   73, 
				   -127, -120,  -18,   33,  -60,   26,  -21,  -39,  
				    -59,   57, -103,  -51,  -83,   49, -117,    1,   
				     24,   35,  -35,   31,   78,   45,   -7,   72,   
				     79,  -14,  101, -114,  120,   92,   88,   25, 
				   -115,  -27, -104,   87,  103,  127,    5,  100,  
				    -81,   99,  -74,   -2,  -11,  -73,   60,  -91,  
				    -50,  -23,  104,   68,  -32,   77,   67,  105,  
				     41,   46,  -84,   21,   89,  -88,   10,  -98,  
				    110,   71,  -33,   52,   53,  106,  -49,  -36,   
				     34,  -55,  -64, -101, -119,  -44,  -19,  -85,   
				     18,  -94,   13,   82,  -69,    2,   47,  -87,  
				    -41,   97,   30,  -76,   80,    4,  -10,  -62,   
				     22,   37, -122,   86,   85,    9,  -66, -111
			}
		};
	
		private static final int P_00 = 1;
	    private static final int P_01 = 0;
	    private static final int P_02 = 0;
	    private static final int P_03 = P_01 ^ 1;
	    private static final int P_04 = 1;

	    private static final int P_10 = 0;
	    private static final int P_11 = 0;
	    private static final int P_12 = 1;
	    private static final int P_13 = P_11 ^ 1;
	    private static final int P_14 = 0;
 
	    private static final int P_20 = 1;
 	    private static final int P_21 = 1;
	    private static final int P_22 = 0;
	    private static final int P_23 = P_21 ^ 1;
	    private static final int P_24 = 0;

	    private static final int P_30 = 0;
	    private static final int P_31 = 1;
	    private static final int P_32 = 1;
	    private static final int P_33 = P_31 ^ 1;
	    private static final int P_34 = 1;
	
		private static final int GF256_FDBK =   0x169;
	    private static final int GF256_FDBK_2 = 0x169 / 2;
	    private static final int GF256_FDBK_4 = 0x169 / 4;
	    
	    private static final int[][] MATRIX = new int[4][256];
	    
	    static
	    {
	    	initMatrix();
	    }
	    
	    private static final void initMatrix()
	    {
	    	int[] m1 = new int[2];
	        int[] X = new int[2];
	        int[] Y = new int[2];
	        int i, j;
	        for (i = 0; i < 256; i++) {
	           j = PCUBE[0][i] & 0xFF;
	           m1[0] = j;
	           X[0] = Matrix_X(j) & 0xFF;
	           Y[0] = Matrix_Y(j) & 0xFF;

	           j = PCUBE[1][i] & 0xFF;
	           m1[1] = j;
	           X[1] = Matrix_X(j) & 0xFF;
	           Y[1] = Matrix_Y(j) & 0xFF;

	           MATRIX[0][i] = m1[P_00] <<  0 | 
	                       	   X[P_00] <<  8 |
	                           Y[P_00] << 16 |
	                           Y[P_00] << 24;
	           
	           MATRIX[1][i] =  Y[P_10] <<  0 |
	                           Y[P_10] <<  8 |
	                           X[P_10] << 16 |
	                          m1[P_10] << 24;
	           
	           MATRIX[2][i] =  X[P_20] <<  0 |
	                           Y[P_20] <<  8 |
	                          m1[P_20] << 16 |
	                           Y[P_20] << 24;
	           
	           MATRIX[3][i] =  X[P_30] <<  0 |
	                          m1[P_30] <<  8 |
	                           Y[P_30] << 16 |
	                           X[P_30] << 24;
	        }
	    }
	    
	    private static final int LFSR1( int x ) {
	        return (x >> 1) ^
	              ((x & 0x01) != 0 ? GF256_FDBK_2 : 0);
	     }

	     private static final int LFSR2( int x ) {
	        return (x >> 2) ^
	              ((x & 0x02) != 0 ? GF256_FDBK_2 : 0) ^
	              ((x & 0x01) != 0 ? GF256_FDBK_4 : 0);
	     }
	     
	     private static final int Matrix_X(int x) 
	     { 
	    	 return x ^ LFSR2(x); 
	     }            
	     
	     private static final int Matrix_Y(int x)
	     { 
	    	 return x ^ LFSR1(x) ^ LFSR2(x); 
	     } 

}

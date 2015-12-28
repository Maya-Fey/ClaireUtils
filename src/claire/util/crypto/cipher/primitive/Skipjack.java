package claire.util.crypto.cipher.primitive;

import java.util.Arrays;

import claire.util.crypto.cipher.key.KeySkipjack;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.IRandom;
import claire.util.standards.crypto.ISymmetric;

public class Skipjack implements ISymmetric<KeySkipjack> {
	
	private static final byte[] PERMUTE = 
		{
		 	-93,  -41,    9, -125,   -8,   72,  -10,  -12,  
		 	-77,   33,   21,  120, -103,  -79,  -81,   -7,  
		 	-25,   45,   77, -118,  -50,   76,  -54,   46,   
		 	 82, -107,  -39,   30,   78,   56,   68,   40,   
		 	 10,  -33,    2,  -96,   23,  -15,   96,  104,   
		 	 18,  -73,  122,  -61,  -23,   -6,   61,   83, 
		   -106, -124,  107,  -70,  -14,   99, -102,   25,  
		    124,  -82,  -27,  -11,   -9,   22,  106,  -94,   
		     57,  -74,  123,   15,  -63, -109, -127,   27,  
		    -18,  -76,   26,  -22,  -48, -111,   47,  -72,   
		     85,  -71,  -38, -123,   63,   65,  -65,  -32,   
		     90,   88, -128,   95,  102,   11,  -40, -112,   
		     53,  -43,  -64,  -89,   51,    6,  101,  105,   
		     69,    0, -108,   86,  109, -104, -101,  118, 
		   -105,   -4,  -78,  -62,  -80,   -2,  -37,   32,  
		    -31,  -21,  -42,  -28,  -35,   71,   74,   29,   
		     66,  -19,  -98,  110,   73,   60,  -51,   67,   
		     39,  -46,    7,  -44,  -34,  -57,  103,   24, 
		   -119,  -53,   48,   31, -115,  -58, -113,  -86,  
		    -56,  116,  -36,  -55,   93,   92,   49,  -92, 
		    112, -120,   97,   44,  -97,   13,   43, -121,   
		     80, -126,   84,  100,   38,  125,    3,   64,   
		     52,   75,   28,  115,  -47,  -60,   -3,   59,  
		    -52,   -5,  127,  -85,  -26,   62,   91,  -91,  
		    -83,    4,   35, -100,   20,   81,   34,  -16,   
		     41,  121,  113,  126,   -1, -116,   14,  -30,   
		     12,  -17,  -68,  114,  117,  111,   55,  -95,  
		    -20,  -45, -114,   98, -117, -122,   16,  -24,    
		      8,  119,   17,  -66, -110,   79,   36,  -59,   
		     50,   54,  -99,  -49,  -13,  -90,  -69,  -84,   
		     94,  108,  -87,   19,   87,   37,  -75,  -29,  
		    -67,  -88,   58,    1,    5,   89,   42,   70, 
		};
	
	private KeySkipjack key;
	
	private final byte[][] KEY = new byte[4][32];

	public Skipjack()
	{
		this.genKey(RandUtils.dprng);
	}
	
	public Skipjack(KeySkipjack key)
	{
		this.setKey(key);
	}
	
	public KeySkipjack getKey()
	{
		return this.key;
	}

	public void setKey(KeySkipjack t)
	{
		this.key = t;
		byte[] key = t.getBytes();
		for(int i = 0; i < 32;)
		{
			KEY[0][i] = key[i++ % 10];
			KEY[1][i] = key[i++ % 10];
			KEY[2][i] = key[i++ % 10];
			KEY[3][i] = key[i++ % 10];
		}
	}

	public void destroyKey()
	{
		for(byte[] subkey : KEY)
			Arrays.fill(subkey, (byte) 0);
	}

	public int plaintextSize()
	{
		return 8;
	}
	
	public int ciphertextSize()
	{
		return 8;
	}
	
	private short unmix(int pos, short state)
	{
		byte c1, c2, c3, c4, c5, c6;
		c1 = (byte) state;
		c2 = (byte) ((state >> 8) & 0xFF);
		c3 = (byte) ((PERMUTE[(c2 ^ KEY[3][pos]) & 0xFF] ^ c1) & 0xFF);
		c4 = (byte) ((PERMUTE[(c3 ^ KEY[2][pos]) & 0xFF] ^ c2) & 0xFF);
		c5 = (byte) ((PERMUTE[(c4 ^ KEY[1][pos]) & 0xFF] ^ c3) & 0xFF);
		c6 = (byte) ((PERMUTE[(c5 ^ KEY[0][pos]) & 0xFF] ^ c4) & 0xFF);
		return Bits.shortFromBytes(new byte[] { c5, c6 }, 0);
	}
	
	private short mix(int pos, short state)
	{
		byte c1, c2, c3, c4, c5, c6;
		c1 = (byte) ((state >> 8) & 0xFF);
		c2 = (byte) state;
		c3 = (byte) ((PERMUTE[(c2 ^ KEY[0][pos]) & 0xFF] ^ c1) & 0xFF);
		c4 = (byte) ((PERMUTE[(c3 ^ KEY[1][pos]) & 0xFF] ^ c2) & 0xFF);
		c5 = (byte) ((PERMUTE[(c4 ^ KEY[2][pos]) & 0xFF] ^ c3) & 0xFF);
		c6 = (byte) ((PERMUTE[(c5 ^ KEY[3][pos]) & 0xFF] ^ c4) & 0xFF);
		return Bits.shortFromBytes(new byte[] { c6, c5 }, 0);
	}

	public void decryptBlock(byte[] block, int start)
	{
		int cnt = start;
		short w2 = (short) ((block[cnt++] << 8) + (block[cnt++] & 0xff));
        short w1 = (short) ((block[cnt++] << 8) + (block[cnt++] & 0xff));
        short w4 = (short) ((block[cnt++] << 8) + (block[cnt++] & 0xff));
        short w3 = (short) ((block[cnt++] << 8) + (block[cnt  ] & 0xff));
        
		int i = 31;
        for(; i >= 24;)
        {
        	short tmp = w4;
            w4 = w3;
            w3 = w2;
            w2 = unmix(i, w1);
            w1 = (short) (w2 ^ tmp ^ (i + 1));
            i--;
        }
        for(; i >= 16;)
        {
        	short tmp = w4;
            w4 = w3;
            w3 = (short) (w1 ^ w2 ^ (i + 1));
            w2 = unmix(i, w1);
            w1 = tmp;
            i--;
        }
        for(; i >= 8;)
        {
        	short tmp = w4;
            w4 = w3;
            w3 = w2;
            w2 = unmix(i, w1);
            w1 = (short) (w2 ^ tmp ^ (i + 1));
            i--;
        }
        for(; i >= 0;)
        {
        	short tmp = w4;
            w4 = w3;
            w3 = (short) (w1 ^ w2 ^ (i + 1));
            w2 = unmix(i, w1);
            w1 = tmp;
            i--;
        }
		
        cnt = start;
        block[cnt++] = (byte)((w2 >> 8));
        block[cnt++] = (byte)(w2);
        block[cnt++] = (byte)((w1 >> 8));
        block[cnt++] = (byte)(w1);
        block[cnt++] = (byte)((w4 >> 8));
        block[cnt++] = (byte)(w4);
        block[cnt++] = (byte)((w3 >> 8));
        block[cnt  ] = (byte)(w3);
	}

	public void decryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		short w2 = (short) ((block[start0 + 0] << 8) + (block[start0 + 1] & 0xff));
        short w1 = (short) ((block[start0 + 2] << 8) + (block[start0 + 3] & 0xff));
        short w4 = (short) ((block[start0 + 4] << 8) + (block[start0 + 5] & 0xff));
        short w3 = (short) ((block[start0 + 6] << 8) + (block[start0 + 7] & 0xff));
        
        int i = 31;
        for(; i >= 24;)
        {
        	short tmp = w4;
            w4 = w3;
            w3 = w2;
            w2 = unmix(i, w1);
            w1 = (short) (w2 ^ tmp ^ (i + 1));
            i--;
        }
        for(; i >= 16;)
        {
        	short tmp = w4;
            w4 = w3;
            w3 = (short) (w1 ^ w2 ^ (i + 1));
            w2 = unmix(i, w1);
            w1 = tmp;
            i--;
        }
        for(; i >= 8;)
        {
        	short tmp = w4;
            w4 = w3;
            w3 = w2;
            w2 = unmix(i, w1);
            w1 = (short) (w2 ^ tmp ^ (i + 1));
            i--;
        }
        for(; i >= 0;)
        {
        	short tmp = w4;
            w4 = w3;
            w3 = (short) (w1 ^ w2 ^ (i + 1));
            w2 = unmix(i, w1);
            w1 = tmp;
            i--;    	
        }
        
        out[start1 + 0] = (byte)((w2 >> 8));
        out[start1 + 1] = (byte)(w2);
        out[start1 + 2] = (byte)((w1 >> 8));
        out[start1 + 3] = (byte)(w1);
        out[start1 + 4] = (byte)((w4 >> 8));
        out[start1 + 5] = (byte)(w4);
        out[start1 + 6] = (byte)((w3 >> 8));
        out[start1 + 7] = (byte)(w3);
	}

	public void encryptBlock(byte[] block, int start)
	{
		int cnt = start;
		short w1 = (short) ((block[cnt++] << 8) + (block[cnt++] & 0xff));
        short w2 = (short) ((block[cnt++] << 8) + (block[cnt++] & 0xff));
        short w3 = (short) ((block[cnt++] << 8) + (block[cnt++] & 0xff));
        short w4 = (short) ((block[cnt++] << 8) + (block[cnt  ] & 0xff));
        
        int i = 0;
        for(; i < 8;)
        {
        	short tmp = w4;
            w4 = w3;
            w3 = w2;
            w2 = mix(i, w1);
            w1 = (short) (w2 ^ tmp ^ ++i);
        }
        for(; i < 16;)
        {
            short tmp = w4;
            w4 = w3;
            w3 = (short) (w1 ^ w2 ^ (i + 1));
            w2 = mix(i, w1);
            w1 = tmp;
            i++;
        }
        for(; i < 24;)
        {
        	short tmp = w4;
            w4 = w3;
            w3 = w2;
            w2 = mix(i, w1);
            w1 = (short) (w2 ^ tmp ^ ++i);
        }
        for(; i < 32;)
        {
            short tmp = w4;
            w4 = w3;
            w3 = (short) (w1 ^ w2 ^ (i + 1));
            w2 = mix(i, w1);
            w1 = tmp;
            i++;
        }
        
        cnt = start;
        block[cnt++] = (byte)((w1 >> 8));
        block[cnt++] = (byte)(w1);
        block[cnt++] = (byte)((w2 >> 8));
        block[cnt++] = (byte)(w2);
        block[cnt++] = (byte)((w3 >> 8));
        block[cnt++] = (byte)(w3);
        block[cnt++] = (byte)((w4 >> 8));
        block[cnt  ] = (byte)(w4);
	}

	public void encryptBlock(byte[] block, int start0, byte[] out, int start1)
	{
		short w1 = (short) ((block[start0 + 0] << 8) + (block[start0 + 1] & 0xff));
        short w2 = (short) ((block[start0 + 2] << 8) + (block[start0 + 3] & 0xff));
        short w3 = (short) ((block[start0 + 4] << 8) + (block[start0 + 5] & 0xff));
        short w4 = (short) ((block[start0 + 6] << 8) + (block[start0 + 7] & 0xff));
        
        int i = 0;
        for(; i < 8;)
        {
        	short tmp = w4;
            w4 = w3;
            w3 = w2;
            w2 = mix(i, w1);
            w1 = (short) (w2 ^ tmp ^ ++i);
        }
        for(; i < 16;)
        {
            short tmp = w4;
            w4 = w3;
            w3 = (short) (w1 ^ w2 ^ (i + 1));
            w2 = mix(i, w1);
            w1 = tmp;
            i++;
        }
        for(; i < 24;)
        {
        	short tmp = w4;
            w4 = w3;
            w3 = w2;
            w2 = mix(i, w1);
            w1 = (short) (w2 ^ tmp ^ ++i);
        }
        for(; i < 32;)
        {
            short tmp = w4;
            w4 = w3;
            w3 = (short) (w1 ^ w2 ^ (i + 1));
            w2 = mix(i, w1);
            w1 = tmp;
            i++;
        }
        
        out[start1 + 0] = (byte)((w1 >> 8));
        out[start1 + 1] = (byte)(w1);
        out[start1 + 2] = (byte)((w2 >> 8));
        out[start1 + 3] = (byte)(w2);
        out[start1 + 4] = (byte)((w3 >> 8));
        out[start1 + 5] = (byte)(w3);
        out[start1 + 6] = (byte)((w4 >> 8));
        out[start1 + 7] = (byte)(w4);
	}

	public KeySkipjack newKey(IRandom rand)
	{
		byte[] bytes = new byte[10];
		RandUtils.fillArr(bytes, rand);
		return new KeySkipjack(bytes);
	}

	public void genKey(IRandom rand)
	{
		this.setKey(newKey(rand));
	}

	public void reset() {}
	
}

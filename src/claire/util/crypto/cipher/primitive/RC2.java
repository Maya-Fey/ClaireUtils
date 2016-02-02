package claire.util.crypto.cipher.primitive;

import java.util.Arrays;

import claire.util.crypto.cipher.key.KeyRC2;
import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.standards.crypto.ISymmetric;

public class RC2 
	   implements ISymmetric<KeyRC2> {
	
	private static final byte[] SBOX = {
		 -39,  120,   -7,  -60,   25,  -35,  -75,  -19, 
		  40,  -23,   -3,  121,   74,  -96,  -40,  -99, 
		 -58,  126,   55, -125,   43,  118,   83, -114, 
		  98,   76,  100, -120,   68, -117,   -5,  -94, 
		  23, -102,   89,  -11, -121,  -77,   79,   19, 
		  97,   69,  109, -115,    9, -127,  125,   50, 
		 -67, -113,   64,  -21, -122,  -73,  123,   11, 
		 -16, -107,   33,   34,   92,  107,   78, -126, 
		  84,  -42,  101, -109,  -50,   96,  -78,   28, 
		 115,   86,  -64,   20,  -89, -116,  -15,  -36, 
		  18,  117,  -54,   31,   59,  -66,  -28,  -47, 
		  66,   61,  -44,   48,  -93,   60,  -74,   38, 
		 111,  -65,   14,  -38,   70,  105,    7,   87, 
		  39,  -14,   29, -101,  -68, -108,   67,    3, 
		 - 8,   17,  -57,  -10, -112,  -17,   62,  -25, 
		   6,  -61,  -43,   47,  -56,  102,   30,  -41, 
		   8,  -24,  -22,  -34, -128,   82,  -18,   -9, 
		-124,  -86,  114,  -84,   53,   77,  106,   42, 
		-106,   26,  -46,  113,   90,   21,   73,  116, 
		  75,  -97,  -48,   94,    4,   24,  -92,  -20, 
		 -62,  -32,   65,  110,   15,   81,  -53,  -52, 
		  36, -111,  -81,   80,  -95,  -12,  112,   57, 
		-103,  124,   58, -123,   35,  -72,  -76,  122, 
		  -4,    2,   54,   91,   37,   85, -105,   49, 
		  45,   93,   -6, -104,  -29, -118, -110,  -82, 
		   5,  -33,   41,   16,  103,  108,  -70,  -55, 
		 -45,    0,  -26,  -49,  -31,  -98,  -88,   44, 
		  99,   22,    1,   63,   88,  -30, -119,  -87, 
		  13,   56,   52,   27,  -85,   51,   -1,  -80, 
		 -69,   72,   12,   95,  -71,  -79,  -51,   46, 
		 -59,  -13,  -37,   71,  -27,  -91, -100,  119, 
		  10,  -90,   32,  104,   -2,  127,  -63,  -83
	};
	
	private KeyRC2 key;
	
	private short[] keyData = new short[64];
	
	public RC2(KeyRC2 key)
	{
		this.setKey(key);
	}

	public KeyRC2 getKey()
	{
		return this.key;
	}

	public void setKey(KeyRC2 t)
	{
		this.key = t;
		
		byte[] oldKey = this.key.getBytes();
		byte[] newKey = new byte[128];
		int length = oldKey.length;
		System.arraycopy(oldKey, 0, newKey, 0, length);
		
		for(int i = length; i < 128; i++)
			newKey[i] = SBOX[(newKey[i - length] + newKey[i - 1]) & 0xFF];
		
		newKey[128 - length] = SBOX[newKey[128 - length] & 0xFF];
		for(int i = 127 - length; i >= 0; i--)
			newKey[i] = SBOX[(newKey[i + length] ^ newKey[i + 1]) & 0xFF];
		Bits.bytesToShorts(newKey, keyData);
	}

	public void wipe()
	{
		Arrays.fill(keyData, (short) 0); 
		key = null;
	}

	public int plaintextSize()
	{
		return 8;
	}
	
	public int ciphertextSize()
	{
		return 8;
	}

	public void encryptBlock(byte[] block, int start)
	{
		int cnt = start;
		short i1 = (short) ((block[cnt++] & 0xFF) | (block[cnt++] & 0xFF) << 8);
		short i2 = (short) ((block[cnt++] & 0xFF) | (block[cnt++] & 0xFF) << 8);
		short i3 = (short) ((block[cnt++] & 0xFF) | (block[cnt++] & 0xFF) << 8);
		short i4 = (short) ((block[cnt++] & 0xFF) | (block[cnt  ] & 0xFF) << 8);
		int j = 0;
		for (int i = 0; i < 16; i++)
		{
			i1 = (short) ((i1 + (i2 & ~i4) + (i3 & i4) + (keyData[j++] & 0xFFFF)) & 0xFFFF);
			i1 = Bits.rotateLeft(i1, 1);
			i2 = (short) ((i2 + (i3 & ~i1) + (i4 & i1) + (keyData[j++] & 0xFFFF)) & 0xFFFF);
			i2 = Bits.rotateLeft(i2, 2);
			i3 = (short) ((i3 + (i4 & ~i2) + (i1 & i2) + (keyData[j++] & 0xFFFF)) & 0xFFFF);
			i3 = Bits.rotateLeft(i3, 3);
			i4 = (short) ((i4 + (i1 & ~i3) + (i2 & i3) + (keyData[j++] & 0xFFFF)) & 0xFFFF);
			i4 = Bits.rotateLeft(i4, 5);
			
			if ((i == 4) || (i == 10))
			{
				i1 += (keyData[i4 & 0x3F] & 0xFFFF);
				i2 += (keyData[i1 & 0x3F] & 0xFFFF);
				i3 += (keyData[i2 & 0x3F] & 0xFFFF);
				i4 += (keyData[i3 & 0x3F] & 0xFFFF);
			}
		}
		cnt = start;
		block[cnt++] = (byte)  i1;
		block[cnt++] = (byte) (i1 >>> 8);
		block[cnt++] = (byte)  i2;
		block[cnt++] = (byte) (i2 >>> 8);
		block[cnt++] = (byte)  i3;
		block[cnt++] = (byte) (i3 >>> 8);
		block[cnt++] = (byte)  i4;
		block[cnt  ] = (byte) (i4 >>> 8);
	}

	public void encryptBlock(byte[] block, int start, byte[] out, int start1)
	{
		short i1 = (short) ((block[start++] & 0xFF) | (block[start++] & 0xFF) << 8);
		short i2 = (short) ((block[start++] & 0xFF) | (block[start++] & 0xFF) << 8);
		short i3 = (short) ((block[start++] & 0xFF) | (block[start++] & 0xFF) << 8);
		short i4 = (short) ((block[start++] & 0xFF) | (block[start  ] & 0xFF) << 8);
		int j = 0;
		for (int i = 0; i < 16; i++)
		{
			i1 = (short) ((i1 + (i2 & ~i4) + (i3 & i4) + (keyData[j++] & 0xFFFF)) & 0xFFFF);
			i1 = Bits.rotateLeft(i1, 1);
			i2 = (short) ((i2 + (i3 & ~i1) + (i4 & i1) + (keyData[j++] & 0xFFFF)) & 0xFFFF);
			i2 = Bits.rotateLeft(i2, 2);
			i3 = (short) ((i3 + (i4 & ~i2) + (i1 & i2) + (keyData[j++] & 0xFFFF)) & 0xFFFF);
			i3 = Bits.rotateLeft(i3, 3);
			i4 = (short) ((i4 + (i1 & ~i3) + (i2 & i3) + (keyData[j++] & 0xFFFF)) & 0xFFFF);
			i4 = Bits.rotateLeft(i4, 5);
			
			if ((i == 4) || (i == 10))
			{
				i1 += (keyData[i4 & 0x3F] & 0xFFFF);
				i2 += (keyData[i1 & 0x3F] & 0xFFFF);
				i3 += (keyData[i2 & 0x3F] & 0xFFFF);
				i4 += (keyData[i3 & 0x3F] & 0xFFFF);
			}
		}
		out[start1++] = (byte)  i1;
		out[start1++] = (byte) (i1 >>> 8);
		out[start1++] = (byte)  i2;
		out[start1++] = (byte) (i2 >>> 8);
		out[start1++] = (byte)  i3;
		out[start1++] = (byte) (i3 >>> 8);
		out[start1++] = (byte)  i4;
		out[start1  ] = (byte) (i4 >>> 8);
	}

	public void decryptBlock(byte[] block, int start)
	{
		int cnt = start;
		short i1 = (short) ((block[cnt++] & 0xFF) | (block[cnt++] & 0xFF) << 8);
		short i2 = (short) ((block[cnt++] & 0xFF) | (block[cnt++] & 0xFF) << 8);
		short i3 = (short) ((block[cnt++] & 0xFF) | (block[cnt++] & 0xFF) << 8);
		short i4 = (short) ((block[cnt++] & 0xFF) | (block[cnt  ] & 0xFF) << 8);
		int j = 63;
		for (int i = 0; i < 16; i++)
		{
			i4 = Bits.rotateRight(i4, 5);
			i4 = (short) ((i4 - (i1 & ~i3) - (i2 & i3) - keyData[j--]) & 0xFFFF);
			i3 = Bits.rotateRight(i3, 3);
			i3 = (short) ((i3 - (i4 & ~i2) - (i1 & i2) - keyData[j--]) & 0xFFFF);
			i2 = Bits.rotateRight(i2, 2);
			i2 = (short) ((i2 - (i3 & ~i1) - (i4 & i1) - keyData[j--]) & 0xFFFF);
			i1 = Bits.rotateRight(i1, 1);
			i1 = (short) ((i1 - (i2 & ~i4) - (i3 & i4) - keyData[j--]) & 0xFFFF);
			if ((i == 4) || (i == 10))
			{
				i4 = (short) ((i4 - keyData[i3 & 0x3F]) & 0xFFFF);
				i3 = (short) ((i3 - keyData[i2 & 0x3F]) & 0xFFFF);
				i2 = (short) ((i2 - keyData[i1 & 0x3F]) & 0xFFFF);
				i1 = (short) ((i1 - keyData[i4 & 0x3F]) & 0xFFFF);
			}
		}
		cnt = start;
		block[cnt++] = (byte)  i1;
		block[cnt++] = (byte) (i1 >>> 8);
		block[cnt++] = (byte)  i2;
		block[cnt++] = (byte) (i2 >>> 8);
		block[cnt++] = (byte)  i3;
		block[cnt++] = (byte) (i3 >>> 8);
		block[cnt++] = (byte)  i4;
		block[cnt  ] = (byte) (i4 >>> 8);
	}

	public void decryptBlock(byte[] block, int start, byte[] out, int start1)
	{
		short i1 = (short) ((block[start++] & 0xFF) | (block[start++] & 0xFF) << 8);
		short i2 = (short) ((block[start++] & 0xFF) | (block[start++] & 0xFF) << 8);
		short i3 = (short) ((block[start++] & 0xFF) | (block[start++] & 0xFF) << 8);
		short i4 = (short) ((block[start++] & 0xFF) | (block[start  ] & 0xFF) << 8);
		int j = 63;
		for (int i = 0; i < 16; i++)
		{
			i4 = Bits.rotateRight(i4, 5);
			i4 = (short) ((i4 - (i1 & ~i3) - (i2 & i3) - keyData[j--]) & 0xFFFF);
			i3 = Bits.rotateRight(i3, 3);
			i3 = (short) ((i3 - (i4 & ~i2) - (i1 & i2) - keyData[j--]) & 0xFFFF);
			i2 = Bits.rotateRight(i2, 2);
			i2 = (short) ((i2 - (i3 & ~i1) - (i4 & i1) - keyData[j--]) & 0xFFFF);
			i1 = Bits.rotateRight(i1, 1);
			i1 = (short) ((i1 - (i2 & ~i4) - (i3 & i4) - keyData[j--]) & 0xFFFF);
			if ((i == 4) || (i == 10))
			{
				i4 = (short) ((i4 - keyData[i3 & 0x3F]) & 0xFFFF);
				i3 = (short) ((i3 - keyData[i2 & 0x3F]) & 0xFFFF);
				i2 = (short) ((i2 - keyData[i1 & 0x3F]) & 0xFFFF);
				i1 = (short) ((i1 - keyData[i4 & 0x3F]) & 0xFFFF);
			}
		}
		out[start1++] = (byte)  i1;
		out[start1++] = (byte) (i1 >>> 8);
		out[start1++] = (byte)  i2;
		out[start1++] = (byte) (i2 >>> 8);
		out[start1++] = (byte)  i3;
		out[start1++] = (byte) (i3 >>> 8);
		out[start1++] = (byte)  i4;
		out[start1  ] = (byte) (i4 >>> 8);
	}
	
	public void reset() {}

	public static final int test()
	{
		final byte[] bytes1 = new byte[12];
		final byte[] bytes2 = new byte[17];
		RandUtils.fillArr(bytes1);
		RandUtils.fillArr(bytes2);
		KeyRC2 a1 = new KeyRC2(bytes1);
		KeyRC2 a2 = new KeyRC2(bytes2);
		RC2 aes = new RC2(a1);
		return ISymmetric.testSymmetric(aes, a2);
	}
	
}

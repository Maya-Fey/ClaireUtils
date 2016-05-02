package claire.util.crypto.rng;

import claire.util.crypto.rng.primitive.FastXorShift;
import claire.util.crypto.rng.primitive.JRandom;
import claire.util.standards.crypto.IRandom;

public class RandUtils {
	
	public static final IRandom<?, ?> dprng = new FastXorShift();
	public static final JRandom guass = new JRandom();
	
	public static final int inRange(int min, int max)
	{
		int t = inrange_fast(dprng, max - min);
		return t + min;
	}
	
	public static final int inRange(int min, int max, IRandom<?, ?> prng)
	{
		int t = inrange_fast(prng, max - min);
		return t + min;
	}
	
	public static final int vary(int in, int max)
	{
		if(dprng.readBool())
			return in + inrange_fast(dprng, max);
		else 
			return in - inrange_fast(dprng, max);
	}	
	
	public static final int vary(int in, int max, IRandom<?, ?> prng)
	{
		if(prng.readBool())
			return in + inrange_fast(prng, max);
		else 
			return in - inrange_fast(prng, max);
	}	
	
	public static final double guassPositive(JRandom prng)
	{
		return (prng.nextGaussian() + 1.0D) / 2;
	}
	
	public static final double guassPositive()
	{
		return (guass.nextGaussian() + 1.0D) / 2;
	}
	
	public static final void fillArr(boolean[] i)
	{
		fillArr(i, 0, i.length);
	}
	
	public static final void fillArr(boolean[] i, int start, int len)
	{
		for(int j = 0; j < len; j++)
			i[start++] = dprng.readByte() < 1;
	}
	
	public static final void fillArr(boolean[] i, IRandom<?, ?> prng)
	{
		fillArr(i, 0, i.length, prng);
	}
	
	public static final void fillArr(boolean[] i, int start, int len, IRandom<?, ?> prng)
	{
		for(int j = 0; j < len; j++)
			i[start++] = prng.readByte() < 1;
	}
	
	public static final void fillArr(byte[] i)
	{
		fillArr(i, 0, i.length);
	}
	
	public static final void fillArr(byte[] i, int start, int len)
	{
		for(int j = 0; j < len; j++)
			i[start++] = dprng.readByte();
	}
	
	public static final void fillArr(byte[] i, IRandom<?, ?> prng)
	{
		fillArr(i, 0, i.length, prng);
	}
	
	public static final void fillArr(byte[] i, int start, int len, IRandom<?, ?> prng)
	{
		for(int j = 0; j < len; j++)
			i[start++] = prng.readByte();
	}
	
	public static final void fillArr(char[] i)
	{
		fillArr(i, 0, i.length);
	}
	
	public static final void fillArr(char[] i, int start, int len)
	{
		for(int j = 0; j < len; j++)
			i[start++] = (char) dprng.readShort();
	}
	
	public static final void fillArr(char[] i, IRandom<?, ?> prng)
	{
		fillArr(i, 0, i.length, prng);
	}
	
	public static final void fillArr(char[] i, int start, int len, IRandom<?, ?> prng)
	{
		for(int j = 0; j < len; j++)
			i[start++] = (char) prng.readShort();
	}
	
	public static final void fillArr(short[] i)
	{
		fillArr(i, 0, i.length);
	}
	
	public static final void fillArr(short[] i, int start, int len)
	{
		for(int j = 0; j < len; j++)
			i[start++] = dprng.readShort();
	}
	
	public static final void fillArr(short[] i, IRandom<?, ?> prng)
	{
		fillArr(i, 0, i.length, prng);
	}
	
	public static final void fillArr(short[] i, int start, int len, IRandom<?, ?> prng)
	{
		for(int j = 0; j < len; j++)
			i[start++] = prng.readShort();
	}
	
	public static final void fillArr(int[] i)
	{
		fillArr(i, 0, i.length);
	}
	
	public static final void fillArr(int[] i, int start, int len)
	{
		for(int j = 0; j < len; j++)
			i[start++] = dprng.readInt();
	}
	
	public static final void fillArr(int[] i, IRandom<?, ?> prng)
	{
		fillArr(i, 0, i.length, prng);
	}
	
	public static final void fillArr(int[] i, int start, int len, IRandom<?, ?> prng)
	{
		for(int j = 0; j < len; j++)
			i[start++] = prng.readInt();
	}
	
	public static final void fillArr(long[] i)
	{
		fillArr(i, 0, i.length);
	}
	
	public static final void fillArr(long[] i, int start, int len)
	{
		for(int j = 0; j < len; j++)
			i[start++] = dprng.readLong();
	}
	
	public static final void fillArr(long[] i, IRandom<?, ?> prng)
	{
		fillArr(i, 0, i.length, prng);
	}
	
	public static final void fillArr(long[] i, int start, int len, IRandom<?, ?> prng)
	{
		for(int j = 0; j < len; j++)
			i[start++] = prng.readLong();
	}
	
	public static final void randomize(byte[] arr, IRandom<?, ?> prng)
	{
		byte t1;
		int t2;
		for(int i = arr.length - 1; i > 1; i--)
		{
			t1 = arr[i];
			t2 = prng.nextIntFast(i + 1);
			arr[i] = arr[t2];
			arr[t2] = t1;
		}	
	}
	
	public static final void randomize(short[] arr, IRandom<?, ?> prng)
	{
		short t1;
		int t2;
		for(int i = arr.length - 1; i > 1; i--)
		{
			t1 = arr[i];
			t2 = prng.nextIntFast(i + 1);
			arr[i] = arr[t2];
			arr[t2] = t1;
		}	
	}
	
	public static final void randomize(char[] arr, IRandom<?, ?> prng)
	{
		char t1;
		int t2;
		for(int i = arr.length - 1; i > 1; i--)
		{
			t1 = arr[i];
			t2 = prng.nextIntFast(i + 1);
			arr[i] = arr[t2];
			arr[t2] = t1;
		}	
	}
	
	public static final void randomize(int[] arr, IRandom<?, ?> prng)
	{
		int t1;
		int t2;
		for(int i = arr.length - 1; i > 1; i--)
		{
			t1 = arr[i];
			t2 = prng.nextIntFast(i + 1);
			arr[i] = arr[t2];
			arr[t2] = t1;
		}	
	}
	
	public static final void randomize(long[] arr, IRandom<?, ?> prng)
	{
		long t1;
		int t2;
		for(int i = arr.length - 1; i > 1; i--)
		{
			t1 = arr[i];
			t2 = prng.nextIntFast(i + 1);
			arr[i] = arr[t2];
			arr[t2] = t1;
		}	
	}
	
	public static final void randomize(Object[] arr, IRandom<?, ?> prng)
	{
		Object t1;
		int t2;
		for(int i = arr.length - 1; i > 1; i--)
		{
			t1 = arr[i];
			t2 = prng.nextIntFast(i + 1);
			arr[i] = arr[t2];
			arr[t2] = t1;
		}	
	}
	
	public static final void randomize(byte[] arr)
	{
		byte t1;
		int t2;
		for(int i = arr.length - 1; i > 1; i--)
		{
			t1 = arr[i];
			t2 = dprng.nextIntFast(i + 1);
			arr[i] = arr[t2];
			arr[t2] = t1;
		}	
	}
	
	public static final void randomize(short[] arr)
	{
		short t1;
		int t2;
		for(int i = arr.length - 1; i > 1; i--)
		{
			t1 = arr[i];
			t2 = dprng.nextIntFast(i + 1);
			arr[i] = arr[t2];
			arr[t2] = t1;
		}	
	}
	
	public static final void randomize(char[] arr)
	{
		char t1;
		int t2;
		for(int i = arr.length - 1; i > 1; i--)
		{
			t1 = arr[i];
			t2 = dprng.nextIntFast(i + 1);
			arr[i] = arr[t2];
			arr[t2] = t1;
		}	
	}
	
	public static final void randomize(int[] arr)
	{
		int t1;
		int t2;
		for(int i = arr.length - 1; i > 1; i--)
		{
			t1 = arr[i];
			t2 = dprng.nextIntFast(i + 1);
			arr[i] = arr[t2];
			arr[t2] = t1;
		}	
	}
	
	public static final void randomize(long[] arr)
	{
		long t1;
		int t2;
		for(int i = arr.length - 1; i > 1; i--)
		{
			t1 = arr[i];
			t2 = dprng.nextIntFast(i + 1);
			arr[i] = arr[t2];
			arr[t2] = t1;
		}	
	}
	
	public static final void randomize(Object[] arr)
	{
		Object t1;
		int t2;
		for(int i = arr.length - 1; i > 1; i--)
		{
			t1 = arr[i];
			t2 = dprng.nextIntFast(i + 1);
			arr[i] = arr[t2];
			arr[t2] = t1;
		}	
	}
	
	public static final int inrange_fast(IRandom<?, ?> rand, int max)
	{
		return (rand.readInt() & 0x7FFFFFFF) % max;
	}
	
	public static final int inrange_best(IRandom<?, ?> rand, int max)
	{
		int bits, val;
	    do {
	    	bits = (rand.readInt() >>> 1);
	    	val = bits % max;
        } while(bits - val + (max - 1) < 0);
	    return val;
	}

}

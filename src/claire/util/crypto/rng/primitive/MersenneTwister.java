package claire.util.crypto.rng.primitive;
	
import java.util.Arrays;

import claire.util.standards.crypto.IRandom;

public class MersenneTwister 
	   implements IRandom<MersenneSeed> {

	// Period parameters
	private static final int N = 624;
	private static final int M = 397;
	private static final int MATRIX_A = 0x9908b0df;   //    private static final * constant vector a
	private static final int UPPER_MASK = 0x80000000; // most significant w-r bits
	private static final int LOWER_MASK = 0x7fffffff; // least significant r bits
	
	
	// Tempering parameters
	private static final int TEMPERING_MASK_B = 0x9d2c5680;
	private static final int TEMPERING_MASK_C = 0xefc60000;
	
	private int mt[]; // the array for the state vector
	private int mti; // mti==N+1 means mt[N] is not initialized
	
	private static final int mag01[] = new int[] { 0x0, MATRIX_A };
	
	private MersenneSeed seed;
	
	public MersenneTwister()
	{
	    this(System.currentTimeMillis());
	}
	
	public MersenneTwister(long seed)
	{
	    setSeed(seed);
	}
		
	public MersenneTwister(int[] array)
	{
	    setSeed(array);
	}
	
	public MersenneTwister(boolean seed)
	{
	    if(seed) {
	    	this.seed = new MersenneSeed(getSeed(System.currentTimeMillis()));
	    	this.setSeed(this.seed);
	    } else {
	    	setSeed(System.currentTimeMillis());
	    }
	}
	
	public MersenneTwister(long seed, boolean seednew)
	{
		if(seednew) {
	    	this.seed = new MersenneSeed(getSeed(seed));
	    	this.setSeed(this.seed);
	    } else {
	    	setSeed(seed);
	    }
	}
		
	public MersenneTwister(int[] seed, boolean seednew)
	{
		if(seednew) {
	    	this.seed = new MersenneSeed(getSeed(seed));
	    	this.setSeed(this.seed);
	    } else {
	    	setSeed(seed);
	    }
	}
	
	public MersenneTwister(MersenneSeed seed)
	{
		this.setSeed(seed);
	}
	
	private final int update()
	{
		int y;
	    
	    if (mti >= N)   // generate N words at one time
	    {
	        int kk;
	        final int[] mt = this.mt; // locals are slightly faster 
	        final int[] mag01 = MersenneTwister.mag01; // locals are slightly faster 
	        
	        for (kk = 0; kk < N - M; kk++)
	        {
	            y = (mt[kk] & UPPER_MASK) | (mt[kk+1] & LOWER_MASK);
	            mt[kk] = mt[kk+M] ^ (y >>> 1) ^ mag01[y & 0x1];
	        }
	        for (; kk < N-1; kk++)
	        {
	            y = (mt[kk] & UPPER_MASK) | (mt[kk+1] & LOWER_MASK);
	            mt[kk] = mt[kk+(M-N)] ^ (y >>> 1) ^ mag01[y & 0x1];
	        }
	        y = (mt[N-1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
	        mt[N-1] = mt[M-1] ^ (y >>> 1) ^ mag01[y & 0x1];
	
	        mti = 0;
	    }
	
	    y = mt[mti++];
	    y ^= y >>> 11;                          // TEMPERING_SHIFT_U(y)
	    y ^= (y << 7) & TEMPERING_MASK_B;       // TEMPERING_SHIFT_S(y)
	    y ^= (y << 15) & TEMPERING_MASK_C;      // TEMPERING_SHIFT_T(y)
	    return y ^= (y >>> 18);   
	}
	
	/**
	 * Initalize the pseudo random number generator.  Don't
	 * pass in a long that's bigger than an int (Mersenne Twister
	 * only uses the first 32 bits for its seed).   
	 */
	public final void setSeed(long seed)
	{	
	    mt = new int[N];	
	    mt[0]= (int)(seed & 0xffffffff);
	    for (mti=1; mti<N; mti++) 
	    {
	        mt[mti] = (1812433253 * (mt[mti-1] ^ (mt[mti-1] >>> 30)) + mti); 
	        /* See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier. */
	        /* In the previous versions, MSBs of the seed affect   */
	        /* only MSBs of the array mt[].                        */
	        /* 2002/01/09 modified by Makoto Matsumoto             */
	    }
	}
	
	
	/**
	 * Sets the seed of the MersenneTwister using an array of integers.
	 * Your array must have a non-zero length.  Only the first 624 integers
	 * in the array are used; if the array is shorter than this then
	 * integers are repeatedly used in a wrap-around fashion.
	 */
	
	public void setSeed(int[] array)
	{
	    if (array.length == 0)
	        throw new IllegalArgumentException("Array length must be greater than zero");
	    int i, j, k;
	    setSeed(19650218);
	    i=1; j=0;
	    k = (N>array.length ? N : array.length);
	    for (; k!=0; k--) 
	    {
	        mt[i] = (mt[i] ^ ((mt[i-1] ^ (mt[i-1] >>> 30)) * 1664525)) + array[j] + j; /* non linear */
	        i++;
	        j++;
	        if (i>=N) { mt[0] = mt[N-1]; i=1; }
	        if (j>=array.length) j=0;
	    }
	    for (k=N-1; k!=0; k--) 
	    {
	        mt[i] = (mt[i] ^ ((mt[i-1] ^ (mt[i-1] >>> 30)) * 1566083941)) - i; /* non linear */
	        i++;
	        if (i>=N) 
	        {
	            mt[0] = mt[N-1]; i=1; 
	        }
	    }
	    mt[0] = 0x80000000; /* MSB is 1; assuring non-zero initial array */ 
	}
	
	public static final int[] getSeed(long seed)
	{	
	    int[] mt = new int[N];	
	    mt[0] = (int)(seed & 0xffffffff);
	    for (int mti=1; mti<N; mti++) 
	    {
	        mt[mti] = (1812433253 * (mt[mti-1] ^ (mt[mti-1] >>> 30)) + mti); 
	        /* See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier. */
	        /* In the previous versions, MSBs of the seed affect   */
	        /* only MSBs of the array mt[].                        */
	        /* 2002/01/09 modified by Makoto Matsumoto             */
	    }
	    return mt;
	}
	
	public static int[] getSeed(int[] array)
	{
	    if (array.length == 0)
	        throw new IllegalArgumentException("Array length must be greater than zero");
	    int i, j, k;
	    int[] mt = getSeed(19650218);
	    i=1; j=0;
	    k = (N>array.length ? N : array.length);
	    for (; k!=0; k--) 
	    {
	        mt[i] = (mt[i] ^ ((mt[i-1] ^ (mt[i-1] >>> 30)) * 1664525)) + array[j] + j; /* non linear */
	        i++;
	        j++;
	        if (i>=N) { mt[0] = mt[N-1]; i=1; }
	        if (j>=array.length) j=0;
	    }
	    for (k=N-1; k!=0; k--) 
	    {
	        mt[i] = (mt[i] ^ ((mt[i-1] ^ (mt[i-1] >>> 30)) * 1566083941)) - i; /* non linear */
	        i++;
	        if (i>=N) 
	        {
	            mt[0] = mt[N-1]; i=1; 
	        }
	    }
	    mt[0] = 0x80000000; /* MSB is 1; assuring non-zero initial array */ 
	    return mt;
	}

	@Override
	public boolean readBool()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte readByte()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short readShort()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char readChar()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int readInt()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long readLong()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void readBools(boolean[] out, int off, int amt)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readNibbles(byte[] out, int off, int amt)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readBytes(byte[] out, int off, int bytes)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readShorts(short[] shorts, int off, int amt)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readChars(char[] chars, int off, int amt)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readInts(int[] ints, int off, int amt)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readLongs(long[] longs, int off, int amt)
	{
		// TODO Auto-generated method stub
		
	}

	public MersenneSeed getSeed()
	{
		return this.seed;
	}

	public void setSeed(MersenneSeed key)
	{
		this.seed = key;
		System.arraycopy(key.getSeed(), 0, mt, 0, N);
	}

	public void reset()
	{
		mti = 0;
		System.arraycopy(seed.getSeed(), 0, mt, 0, N);
	}

	public void wipe()
	{
		Arrays.fill(mt, 0);
		mti = 0;
	}
	
	
}
	
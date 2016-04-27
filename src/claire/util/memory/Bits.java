package claire.util.memory;

import claire.util.encoding.CString;
import claire.util.memory.array.DynArray;
import claire.util.standards.IIterable;
import claire.util.standards.IIterator;
import claire.util.standards.IReferrable;
import claire.util.standards.IUUID;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IRandom;

/*
 * A massive, overly monolithic class with all your bit-twiddling,
 * array-shifting, primitive-converting, logic-functing needs. Complete
 * with a coffee machine.
 */

public interface Bits<Type extends Bits<Type>> 
	   extends IUUID<Type>, IReferrable<Type>, IIterable<Boolean> {
	
	public static final int[] BYTESHIFTS =
	{
		0, 8, 16, 24, 32, 40, 48, 56
	};
	
	public static final byte[] LSB_TABLE = new byte[]
		{
		    0, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    6, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    7, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    6, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 
		    4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0
		};
	
	public static final byte[] MSB_TABLE = new byte[]
		{
		    0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 
		    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 
		    5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 
		    5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 
		    6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 
		    6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 
		    6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 
		    6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 
		    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
		    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
		    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
		    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
		    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
		    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
		    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
		    7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7
		};
	
	public static final int[] BIT32_TABLE = new int[]
		{
			0x1,
			0x2,
			0x4,
			0x8,
			0x10,
			0x20,
			0x40,
			0x80,
			0x100,
			0x200,
			0x400,
			0x800,
			0x1000,
			0x2000,
			0x4000,
			0x8000,
			0x10000,
			0x20000,
			0x40000,
			0x80000,
			0x100000,
			0x200000,
			0x400000,
			0x800000,
			0x1000000,
			0x2000000,
			0x4000000,
			0x8000000,
			0x10000000,
			0x20000000,
			0x40000000,
			0x80000000
		};
	
	public static final long[] BIT64_TABLE = new long[]
		{
			0x1L,
			0x2L,
			0x4L,
			0x8L,
			0x10L,
			0x20L,
			0x40L,
			0x80L,
			0x100L,
			0x200L,
			0x400L,
			0x800L,
			0x1000L,
			0x2000L,
			0x4000L,
			0x8000L,
			0x10000L,
			0x20000L,
			0x40000L,
			0x80000L,
			0x100000L,
			0x200000L,
			0x400000L,
			0x800000L,
			0x1000000L,
			0x2000000L,
			0x4000000L,
			0x8000000L,
			0x10000000L,
			0x20000000L,
			0x40000000L,
			0x80000000L,
			0x100000000L,
			0x200000000L,
			0x400000000L,
			0x800000000L,
			0x1000000000L,
			0x2000000000L,
			0x4000000000L,
			0x8000000000L,
			0x10000000000L,
			0x20000000000L,
			0x40000000000L,
			0x80000000000L,
			0x100000000000L,
			0x200000000000L,
			0x400000000000L,
			0x800000000000L,
			0x1000000000000L,
			0x2000000000000L,
			0x4000000000000L,
			0x8000000000000L,
			0x10000000000000L,
			0x20000000000000L,
			0x40000000000000L,
			0x80000000000000L,
			0x100000000000000L,
			0x200000000000000L,
			0x400000000000000L,
			0x800000000000000L,
			0x1000000000000000L,
			0x2000000000000000L,
			0x4000000000000000L,
			0x8000000000000000L
		};

	public static final int[] TRUNC32_TABLE = new int[]
		{
			0x00000000,
			0x00000001,
			0x00000003,
			0x00000007,
			0x0000000F,
			0x0000001F,
			0x0000003F,
			0x0000007F,
			0x000000FF,
			0x000001FF,
			0x000003FF,
			0x000007FF,
			0x00000FFF,
			0x00001FFF,
			0x00003FFF,
			0x00007FFF,
			0x0000FFFF,
			0x0001FFFF,
			0x0003FFFF,
			0x0007FFFF,
			0x000FFFFF,
			0x001FFFFF,
			0x003FFFFF,
			0x007FFFFF,
			0x00FFFFFF,
			0x01FFFFFF,
			0x03FFFFFF,
			0x07FFFFFF,
			0x0FFFFFFF,
			0x1FFFFFFF,
			0x3FFFFFFF,
			0x7FFFFFFF,
			0xFFFFFFFF
		};
	
	public static final long[] TRUNC64_TABLE = new long[]
		{
			0x000000000000000L,
			0x000000000000001L,
			0x000000000000003L,
			0x000000000000007L,
			0x00000000000000FL,
			0x00000000000001FL,
			0x00000000000003FL,
			0x00000000000007FL,
			0x0000000000000FFL,
			0x0000000000001FFL,
			0x0000000000003FFL,
			0x0000000000007FFL,
			0x000000000000FFFL,
			0x000000000001FFFL,
			0x000000000003FFFL,
			0x000000000007FFFL,
			0x00000000000FFFFL,
			0x00000000001FFFFL,
			0x00000000003FFFFL,
			0x00000000007FFFFL,
			0x0000000000FFFFFL,
			0x0000000001FFFFFL,
			0x0000000003FFFFFL,
			0x0000000007FFFFFL,
			0x000000000FFFFFFL,
			0x000000001FFFFFFL,
			0x000000003FFFFFFL,
			0x000000007FFFFFFL,
			0x00000000FFFFFFFL,
			0x00000001FFFFFFFL,
			0x00000003FFFFFFFL,
			0x00000007FFFFFFFL,
			0x0000000FFFFFFFFL,
			0x0000001FFFFFFFFL,
			0x0000003FFFFFFFFL,
			0x0000007FFFFFFFFL,
			0x000000FFFFFFFFFL,
			0x000001FFFFFFFFFL,
			0x000003FFFFFFFFFL,
			0x000007FFFFFFFFFL,
			0x00000FFFFFFFFFFL,
			0x00001FFFFFFFFFFL,
			0x00003FFFFFFFFFFL,
			0x00007FFFFFFFFFFL,
			0x0000FFFFFFFFFFFL,
			0x0001FFFFFFFFFFFL,
			0x0003FFFFFFFFFFFL,
			0x0007FFFFFFFFFFFL,
			0x000FFFFFFFFFFFFL,
			0x001FFFFFFFFFFFFL,
			0x003FFFFFFFFFFFFL,
			0x007FFFFFFFFFFFFL,
			0x00FFFFFFFFFFFFFL,
			0x01FFFFFFFFFFFFFL,
			0x03FFFFFFFFFFFFFL,
			0x07FFFFFFFFFFFFFL,
			0x0FFFFFFFFFFFFFFL,
			0x1FFFFFFFFFFFFFFL,
			0x3FFFFFFFFFFFFFFL,
			0x7FFFFFFFFFFFFFFL,
			0xFFFFFFFFFFFFFFFL
		};
	/**
	 * Returns the capacity of the bit array.
	 */
	int getCapacity();
	
	/**
	 * Sets the bits in the array to the bits specified. Ignores extra bits.
	 */
	void set(boolean[] array);
	
	/**
	 * Sets the bit at the specified index to the specified value.
	 */
	void set(int index, boolean value);
	
	/**
	 * Returns the bit at the specified index.
	 */
	boolean get(int i);
	
	/**
	 * Returns this object as a bit array.
	 */
	boolean[] get();

	default void AND(boolean b, int i)
	{
		this.set(i, b & this.get(i));
	}

	default void OR(boolean b, int i)
	{
		this.set(i, b | this.get(i));
	}

	default void XOR(boolean b, int i)
	{
		this.set(i, b ^ this.get(i));
	}

	default void NAND(boolean b, int i)
	{
		this.set(i, !(b & this.get(i)));
	}

	default void NOR(boolean b, int i)
	{
		this.set(i, !(b | this.get(i)));
	}
	
	default void XNOR(boolean b, int i)
	{
		this.set(i, !(b ^ this.get(i)));
	}
	
	default void NOT(int i)
	{
		this.set(i, !this.get(i));
	}
	
	default void AND(boolean[] b)
	{
		for(int i = 0; i < this.getCapacity(); i++)
			this.AND(b[i], i);
	}

	default void OR(boolean[] b)
	{
		for(int i = 0; i < this.getCapacity(); i++)
			this.OR(b[i], i);
	}

	default void XOR(boolean[] b)
	{
		for(int i = 0; i < this.getCapacity(); i++)
			this.XOR(b[i], i);
	}

	default void NAND(boolean[] b)
	{
		for(int i = 0; i < this.getCapacity(); i++)
			this.NAND(b[i], i);
	}

	default void NOR(boolean[] b)
	{
		for(int i = 0; i < this.getCapacity(); i++)
			this.NOR(b[i], i);
	}

	default void XNOR(boolean[] b)
	{
		for(int i = 0; i < this.getCapacity(); i++)
			this.XNOR(b[i], i);
	}
	
	default CString toCString()
	{
		char[] chars = new char[this.getCapacity()];
		boolean[] b = this.get();
		for(int i = 0; i < b.length; i++)
			if(b[i])
				chars[i] = '1';
			else
				chars[i] = '0';
		return new CString(chars);
	}
	
	public void NOT();
	
	public void ROR(int i);
	public void ROL(int i);
	
	public static int getMSB(byte in)
	{
		return MSB_TABLE[in];
	}
	
	public static int getMSB(short in)
	{
		if((in & 0xFF) != in)
			return MSB_TABLE[(in >>> 8) & 0xFF] + 8;
		else
			return MSB_TABLE[(byte) in];
	}
	
	public static int getMSB(int in)
	{
		if     ((in & 0xFFFFFF) != in)
			return MSB_TABLE[(in >>> 24) & 0xFF] + 24;
		else if((in & 0x00FFFF) != in)
			return MSB_TABLE[(in >>> 16) & 0xFF] + 16;
		else if((in & 0x0000FF) != in)
			return MSB_TABLE[(in >>>  8) & 0xFF] +  8;
		else
			return MSB_TABLE[in & 0xFF];
	}
	
	public static int getMSB(long in)
	{
		if     ((in & 0xFFFFFFFFFFFFFFL) != in)
			return MSB_TABLE[(int) ((in >>> 56) & 0xFF)] + 56;
		else if((in & 0x00FFFFFFFFFFFFL) != in)
			return MSB_TABLE[(int) ((in >>> 48) & 0xFF)] + 48;
		else if((in & 0x0000FFFFFFFFFFL) != in)
			return MSB_TABLE[(int) ((in >>> 40) & 0xFF)] + 40;
		else if((in & 0x000000FFFFFFFFL) != in)
			return MSB_TABLE[(int) ((in >>> 48) & 0xFF)] + 32;
		else if((in & 0x00000000FFFFFFL) != in)
			return MSB_TABLE[(int) ((in >>> 40) & 0xFF)] + 24;
		else if((in & 0x0000000000FFFFL) != in)
			return MSB_TABLE[(int) ((in >>> 16) & 0xFF)] + 16;
		else if((in & 0x000000000000FFL) != in)
			return MSB_TABLE[(int) ((in >>>  8) & 0xFF)] +  8;
		else
			return MSB_TABLE[(byte) in];
	}
	
	public static int getLSB(byte in)
	{
		return LSB_TABLE[in];
	}
	
	public static int getLSB(short in)
	{
		if((in & 0xFF) == 0)
			return LSB_TABLE[(in >>> 8) & 0xFF] + 8;
		else
			return LSB_TABLE[(byte) in];
	}
	
	public static int getLSB(int in)
	{
		if     ((in & 0xFFFFFF) == 0)
			return LSB_TABLE[(in >>> 24) & 0xFF] + 24;
		else if((in & 0x00FFFF) == 0)
			return LSB_TABLE[(in >>> 16) & 0xFF] + 16;
		else if((in & 0x0000FF) == 0)
			return LSB_TABLE[(in >>>  8) & 0xFF] +  8;
		else
			return LSB_TABLE[in & 0xFF];
	}
	
	public static int getLSB(long in)
	{
		if     ((in & 0xFFFFFFFFFFFFFFL) == 0)
			return LSB_TABLE[(int) ((in >>> 56) & 0xFF)] + 56;
		else if((in & 0x00FFFFFFFFFFFFL) == 0)
			return LSB_TABLE[(int) ((in >>> 48) & 0xFF)] + 48;
		else if((in & 0x0000FFFFFFFFFFL) == 0)
			return LSB_TABLE[(int) ((in >>> 40) & 0xFF)] + 40;
		else if((in & 0x000000FFFFFFFFL) == 0)
			return LSB_TABLE[(int) ((in >>> 48) & 0xFF)] + 32;
		else if((in & 0x00000000FFFFFFL) == 0)
			return LSB_TABLE[(int) ((in >>> 40) & 0xFF)] + 24;
		else if((in & 0x0000000000FFFFL) == 0)
			return LSB_TABLE[(int) ((in >>> 16) & 0xFF)] + 16;
		else if((in & 0x000000000000FFL) == 0)
			return LSB_TABLE[(int) ((in >>>  8) & 0xFF)] +  8;
		else
			return LSB_TABLE[(byte) in];
	}
	
	public static byte truncate(byte in, int places)
	{
		return (byte) (in & TRUNC32_TABLE[places]);
	}
	
	public static short truncate(short in, int places)
	{
		return (short) (in & TRUNC32_TABLE[places]);
	}
	
	public static int truncate(int in, int places)
	{
		return in & TRUNC32_TABLE[places];
	}
	
	public static long truncate(long in, int places)
	{
		return in & TRUNC64_TABLE[places];
	}
	
	public static byte truncateBy(byte in, int places)
	{
		return (byte) (in & ~TRUNC32_TABLE[places]);
	}
	
	public static short truncateBy(short in, int places)
	{
		return (short) (in & ~TRUNC32_TABLE[places]);
	}
	
	public static int truncateBy(int in, int places)
	{
		return in & ~TRUNC32_TABLE[places];
	}
	
	public static long truncateBy(long in, int places)
	{
		return in & ~TRUNC64_TABLE[places];
	}
	
	public static boolean getBit(byte t, int p)
	{
		return (t & BIT32_TABLE[p]) != 0;
	}
	
	public static boolean getBit(short t, int p)
	{
		return (t & BIT32_TABLE[p]) != 0;
	}
	
	public static boolean getBit(int t, int p)
	{
		return (t & BIT32_TABLE[p]) != 0;
	}
	
	public static boolean getBit(long t, int p)
	{
		return (t & BIT64_TABLE[p]) != 0;
	}
	
	public static long UNSIGNED_DIVIDE(int i, int divisor)
	{
		if(divisor == 0) throw new java.lang.ArithmeticException("Division by Zero");
		return ((long) i & 0xFFFFFFFFL) / ((long) divisor & 0xFFFFFFFFL);
	}
	
	/**
	 * Checks whether the first operand is greater or equal to than the second.
	 * 
	 * @param i1
	 * @param i2
	 * @return
	 */
	public static boolean u_greaterOrEqual(int i1, int i2)
	{
		if((i1 ^ 0x80000000) >= (i2 ^ 0x80000000)) 
			return true;
		else
			return false;
	}
	
	/**
	 * Checks whether the first operand is greater or equal to than the second.
	 * 
	 * @param i1
	 * @param i2
	 * @return
	 */
	public static boolean u_greaterOrEqual(long i1, long i2)
	{
		if((i1 ^ 0x8000000000000000L) >= (i2 ^ 0x8000000000000000L)) 
			return true;
		else
			return false;
	}
	
	/**
	 * Checks whether the first operand is greater than the second.
	 * 
	 * @param i1
	 * @param i2
	 * @return
	 */
	public static boolean u_greaterThan(int i1, int i2)
	{
		if((i1 ^ 0x80000000) > (i2 ^ 0x80000000)) 
			return true;
		else
			return false;
	}
	
	/**
	 * Checks whether the first operand is greater than the second.
	 * 
	 * @param i1
	 * @param i2
	 * @return
	 */
	public static boolean u_greaterThan(long i1, long i2)
	{
		if((i1 ^ 0x8000000000000000L) > (i2 ^ 0x8000000000000000L)) 
			return true;
		else
			return false;
	}
	
	/**
	 * Please note that this does not accound for any digit grouping or negative sign
	 * 
	 * @param words
	 * @return
	 */
	public static int APPROXIMATEB10DIDGITS(int words)
	{
		/*
		 * Approximately 9.7 base10 digits per 32-bits
		 */
		return words * 10;
	}
	
	public static int[] splitLong(long l)
	{
		final int[] i = new int[2];
		i[0] = (int) l;
		i[1] = (int) (l >>> 32);
		return i;
	}
	
	public static int[] splitLong(long l, int[] i, int start)
	{
		i[start++] = (int) l;
		i[start  ] = (int) (l >>> 32);
		return i;
	}
	
	public static long getLong(int major, int minor)
	{
		return (((long) major) << 32) | ((long) minor & 0xFFFFFFFFL);
	}
	
	public static boolean canFit32(long l)
	{
		if(l > Integer.MAX_VALUE)
			return true;
		return false;
	}
	
	public static boolean[] randomBits(int amount, IRandom<?> prng)
	{
		boolean[] n = new boolean[amount];
		int i = 0;
		while(amount > 0)
		{
			long l = prng.readLong();
			int j = 0;
			while(j < 64 && amount-- > 0)
				n[i++] = (l & BIT64_TABLE[j++]) != 0;
		}
		return n;		
	}
	
	public static byte byteFromBool(final boolean[] bools)
	{
		byte b = 0x00;
		for(int i = 0; i < 8; i++)
			if(bools[i])
				b |= 1 << i;
		return b;
	}
	
	public static short shortFromBool(final boolean[] bools)
	{
		short b = 0x0000;
		for(int i = 0; i < 16; i++)
			if(bools[i])
				b |= 1 << i;
		return b;
	}
	
	public static int intFromBool(final boolean[] bools)
	{
		int b = 0x00000000;
		for(int i = 0; i < 32; i++)
			if(bools[i])
				b |= 1 << i;
		return b;
	}
	
	public static long longFromBool(final boolean[] bools)
	{
		long b = 0x00000000;
		for(int i = 0; i < 64; i++)
			if(bools[i])
				b |= (long) 1 << i;
		return b;
	}
	
	public static long longFromBytes(byte[] bytes, int start)
	{
		return ((((long) bytes[start++] & 0xFF)) |
				(((long) bytes[start++] & 0xFF) <<  8) |
				(((long) bytes[start++] & 0xFF) << 16) |
				(((long) bytes[start++] & 0xFF) << 24) |
				(((long) bytes[start++] & 0xFF) << 32) |
				(((long) bytes[start++] & 0xFF) << 40) |
				(((long) bytes[start++] & 0xFF) << 48) |
				(((long) bytes[start  ] & 0xFF) << 56));
	}
		
	public static long longFromBytes(byte[] bytes, int start, int len)
	{
		long l = 0;
		int roof = start + len, shift = 0;
		while(start < roof) {
			l |= (long) (bytes[start++] & 0xFF) << shift;
			shift += 8;
		}
		return l;
	}
	
	
	public static int intFromBytes(byte[] bytes, int start)
	{
		return ((((int) bytes[start++] & 0xFF)) |
				(((int) bytes[start++] & 0xFF) <<  8) |
				(((int) bytes[start++] & 0xFF) << 16) |
				(((int) bytes[start  ] & 0xFF) << 24));
	}
	
	public static int intFromBytes(byte[] bytes, int start, int len)
	{
		int l = 0;
		int roof = start + len, shift = 0;
		while(start < roof) {
			l |= (int) (bytes[start++] & 0xFF) << shift;
			shift += 8;
		}
		return l;
	}
	
	public static short shortFromBytes(byte[] bytes, int start)
	{
		return (short) ((((short) bytes[start++] & 0xFF)) |
				        (((short) bytes[start  ] & 0xFF) << 8));
	}
	
	public static short shortFromBytes(byte[] bytes, int start, int len)
	{
		short l = 0;
		int roof = start + len, shift = 0;
		while(start < roof) {
			l |= (short) (bytes[start++] & 0xFF) << shift;
			shift += 8;
		}
		return l;
	}
	
	public static char charFromBytes(byte[] bytes, int start)
	{
		return (char) ((((char) bytes[start++] & 0xFF)) |
				       (((char) bytes[start  ] & 0xFF) << 8));
	}
	
	public static char charFromBytes(byte[] bytes, int start, int len)
	{
		char l = 0;
		int roof = start + len, shift = 0;
		while(start < roof) {
			l |= (char) (bytes[start++] & 0xFF) << shift;
			shift += 8;
		}
		return l;
	}
	
	public static void rotateLeft(boolean[] bits, int pos)
	{
		final boolean[] temp = new boolean[pos];
		System.arraycopy(bits, 0, temp, 0, pos);
		System.arraycopy(bits, pos, bits, 0, bits.length - pos);
		System.arraycopy(temp, 0, bits, bits.length - pos, pos);
	}
	
	public static void rotateRight(boolean[] bits, int pos)
	{
		final boolean[] temp = new boolean[pos];
		System.arraycopy(bits, bits.length - pos, temp, 0, pos);
		System.arraycopy(bits, 0, bits, pos, bits.length - pos);
		System.arraycopy(temp, 0, bits, 0, pos);
	}
	
	public static void rotateLeft(byte[] bits, int pos)
	{
		final byte[] temp = new byte[pos];
		System.arraycopy(bits, 0, temp, 0, pos);
		System.arraycopy(bits, pos, bits, 0, bits.length - pos);
		System.arraycopy(temp, 0, bits, bits.length - pos, pos);
	}
	
	public static void rotateRight(byte[] bits, int pos)
	{
		final byte[] temp = new byte[pos];
		System.arraycopy(bits, bits.length - pos, temp, 0, pos);
		System.arraycopy(bits, 0, bits, pos, bits.length - pos);
		System.arraycopy(temp, 0, bits, 0, pos);
	}
	
	public static void rotateLeft(char[] bits, int pos)
	{
		final char[] temp = new char[pos];
		System.arraycopy(bits, 0, temp, 0, pos);
		System.arraycopy(bits, pos, bits, 0, bits.length - pos);
		System.arraycopy(temp, 0, bits, bits.length - pos, pos);
	}
	
	public static void rotateRight(char[] bits, int pos)
	{
		final char[] temp = new char[pos];
		System.arraycopy(bits, bits.length - pos, temp, 0, pos);
		System.arraycopy(bits, 0, bits, pos, bits.length - pos);
		System.arraycopy(temp, 0, bits, 0, pos);
	}
	
	public static void rotateLeft(short[] bits, int pos)
	{
		final short[] temp = new short[pos];
		System.arraycopy(bits, 0, temp, 0, pos);
		System.arraycopy(bits, pos, bits, 0, bits.length - pos);
		System.arraycopy(temp, 0, bits, bits.length - pos, pos);
	}
	
	public static void rotateRight(short[] bits, int pos)
	{
		final short[] temp = new short[pos];
		System.arraycopy(bits, bits.length - pos, temp, 0, pos);
		System.arraycopy(bits, 0, bits, pos, bits.length - pos);
		System.arraycopy(temp, 0, bits, 0, pos);
	}
	
	public static void rotateLeft(int[] bits, int pos)
	{
		final int[] temp = new int[pos];
		System.arraycopy(bits, 0, temp, 0, pos);
		System.arraycopy(bits, pos, bits, 0, bits.length - pos);
		System.arraycopy(temp, 0, bits, bits.length - pos, pos);
	}
	
	public static void rotateRight(int[] bits, int pos)
	{
		final int[] temp = new int[pos];
		System.arraycopy(bits, bits.length - pos, temp, 0, pos);
		System.arraycopy(bits, 0, bits, pos, bits.length - pos);
		System.arraycopy(temp, 0, bits, 0, pos);
	}
	
	public static void rotateLeft(long[] bits, int pos)
	{
		final long[] temp = new long[pos];
		System.arraycopy(bits, 0, temp, 0, pos);
		System.arraycopy(bits, pos, bits, 0, bits.length - pos);
		System.arraycopy(temp, 0, bits, bits.length - pos, pos);
	}
	
	public static void rotateRight(long[] bits, int pos)
	{
		final long[] temp = new long[pos];
		System.arraycopy(bits, bits.length - pos, temp, 0, pos);
		System.arraycopy(bits, 0, bits, pos, bits.length - pos);
		System.arraycopy(temp, 0, bits, 0, pos);
	}
	
	public static void rotateLeft(boolean[] bits, int start, int len, int pos)
	{
		final boolean[] temp = new boolean[pos];
		System.arraycopy(bits, start, temp, 0, pos);
		System.arraycopy(bits, start + pos, bits, start, len - pos);
		System.arraycopy(temp, 0, bits, start + len - pos, pos);
	}
	
	public static void rotateLeft(byte[] bits, int start, int len, int pos)
	{
		final byte[] temp = new byte[pos];
		System.arraycopy(bits, start, temp, 0, pos);
		System.arraycopy(bits, start + pos, bits, start, len - pos);
		System.arraycopy(temp, 0, bits, start + len - pos, pos);
	}
	
	public static void rotateLeft(short[] bits, int start, int len, int pos)
	{
		final short[] temp = new short[pos];
		System.arraycopy(bits, start, temp, 0, pos);
		System.arraycopy(bits, start + pos, bits, start, len - pos);
		System.arraycopy(temp, 0, bits, start + len - pos, pos);
	}
	
	public static void rotateLeft(char[] bits, int start, int len, int pos)
	{
		final char[] temp = new char[pos];
		System.arraycopy(bits, start, temp, 0, pos);
		System.arraycopy(bits, start + pos, bits, start, len - pos);
		System.arraycopy(temp, 0, bits, start + len - pos, pos);
	}
	
	public static void rotateLeft(int[] bits, int start, int len, int pos)
	{
		final int[] temp = new int[pos];
		System.arraycopy(bits, start, temp, 0, pos);
		System.arraycopy(bits, start + pos, bits, start, len - pos);
		System.arraycopy(temp, 0, bits, start + len - pos, pos);
	}
	
	public static void rotateLeft(long[] bits, int start, int len, int pos)
	{
		final long[] temp = new long[pos];
		System.arraycopy(bits, start, temp, 0, pos);
		System.arraycopy(bits, start + pos, bits, start, len - pos);
		System.arraycopy(temp, 0, bits, start + len - pos, pos);
	}
	
	public static void rotateRight(boolean[] bits, int start, int len, int pos)
	{
		final boolean[] temp = new boolean[pos];
		System.arraycopy(bits, start + pos, temp, 0, pos);
		System.arraycopy(bits, start, bits, start + pos, len - pos);
		System.arraycopy(temp, 0, bits, start + pos, pos);
	}
	
	public static void rotateRight(byte[] bits, int start, int len, int pos)
	{
		final byte[] temp = new byte[pos];
		System.arraycopy(bits, start + len - pos, temp, 0, pos);
		System.arraycopy(bits, start, bits, start + pos, len - pos);
		System.arraycopy(temp, 0, bits, start, pos);
	}
	
	public static void rotateRight(short[] bits, int start, int len, int pos)
	{
		final short[] temp = new short[pos];
		System.arraycopy(bits, start + len - pos, temp, 0, pos);
		System.arraycopy(bits, start, bits, start + pos, len - pos);
		System.arraycopy(temp, 0, bits, start, pos);
	}
	
	public static void rotateRight(char[] bits, int start, int len, int pos)
	{
		final char[] temp = new char[pos];
		System.arraycopy(bits, start + len - pos, temp, 0, pos);
		System.arraycopy(bits, start, bits, start + pos, len - pos);
		System.arraycopy(temp, 0, bits, start, pos);
	}
	
	public static void rotateRight(int[] bits, int start, int len, int pos)
	{
		final int[] temp = new int[pos];
		System.arraycopy(bits, start + len - pos, temp, 0, pos);
		System.arraycopy(bits, start, bits, start + pos, len - pos);
		System.arraycopy(temp, 0, bits, start, pos);
	}
	
	public static void rotateRight(long[] bits, int start, int len, int pos)
	{
		final long[] temp = new long[pos];
		System.arraycopy(bits, start + len - pos, temp, 0, pos);
		System.arraycopy(bits, start, bits, start + pos, len - pos);
		System.arraycopy(temp, 0, bits, start, pos);
	}
	
	public static void rotateLeft(boolean[] bits, boolean[] temp, int pos)
	{
		System.arraycopy(bits, 0, temp, 0, pos);
		System.arraycopy(bits, pos, bits, 0, bits.length - pos);
		System.arraycopy(temp, 0, bits, bits.length - pos, pos);
	}
	
	public static void rotateRight(boolean[] bits, boolean[] temp, int pos)
	{
		System.arraycopy(bits, bits.length - pos, temp, 0, pos);
		System.arraycopy(bits, 0, bits, pos, bits.length - pos);
		System.arraycopy(temp, 0, bits, 0, pos);
	}
	
	public static void rotateLeft(byte[] bits, byte[] temp, int pos)
	{
		System.arraycopy(bits, 0, temp, 0, pos);
		System.arraycopy(bits, pos, bits, 0, bits.length - pos);
		System.arraycopy(temp, 0, bits, bits.length - pos, pos);
	}
	
	public static void rotateRight(byte[] bits, byte[] temp, int pos)
	{
		System.arraycopy(bits, bits.length - pos, temp, 0, pos);
		System.arraycopy(bits, 0, bits, pos, bits.length - pos);
		System.arraycopy(temp, 0, bits, 0, pos);
	}
	
	public static void rotateLeft(char[] bits, char[] temp, int pos)
	{
		System.arraycopy(bits, 0, temp, 0, pos);
		System.arraycopy(bits, pos, bits, 0, bits.length - pos);
		System.arraycopy(temp, 0, bits, bits.length - pos, pos);
	}
	
	public static void rotateRight(char[] bits, char[] temp, int pos)
	{
		System.arraycopy(bits, bits.length - pos, temp, 0, pos);
		System.arraycopy(bits, 0, bits, pos, bits.length - pos);
		System.arraycopy(temp, 0, bits, 0, pos);
	}
	
	public static void rotateLeft(short[] bits, short[] temp, int pos)
	{
		System.arraycopy(bits, 0, temp, 0, pos);
		System.arraycopy(bits, pos, bits, 0, bits.length - pos);
		System.arraycopy(temp, 0, bits, bits.length - pos, pos);
	}
	
	public static void rotateRight(short[] bits, short[] temp, int pos)
	{
		System.arraycopy(bits, bits.length - pos, temp, 0, pos);
		System.arraycopy(bits, 0, bits, pos, bits.length - pos);
		System.arraycopy(temp, 0, bits, 0, pos);
	}
	
	public static void rotateLeft(int[] bits, int[] temp, int pos)
	{
		System.arraycopy(bits, 0, temp, 0, pos);
		System.arraycopy(bits, pos, bits, 0, bits.length - pos);
		System.arraycopy(temp, 0, bits, bits.length - pos, pos);
	}
	
	public static void rotateRight(int[] bits, int[] temp, int pos)
	{
		System.arraycopy(bits, bits.length - pos, temp, 0, pos);
		System.arraycopy(bits, 0, bits, pos, bits.length - pos);
		System.arraycopy(temp, 0, bits, 0, pos);
	}
	
	public static void rotateLeft(long[] bits, long[] temp, int pos)
	{
		System.arraycopy(bits, 0, temp, 0, pos);
		System.arraycopy(bits, pos, bits, 0, bits.length - pos);
		System.arraycopy(temp, 0, bits, bits.length - pos, pos);
	}
	
	public static void rotateRight(long[] bits, long[] temp, int pos)
	{
		System.arraycopy(bits, bits.length - pos, temp, 0, pos);
		System.arraycopy(bits, 0, bits, pos, bits.length - pos);
		System.arraycopy(temp, 0, bits, 0, pos);
	}
	
	public static void rotateLeft(boolean[] bits, boolean[] temp,int start, int len, int pos)
	{
		System.arraycopy(bits, start, temp, 0, pos);
		System.arraycopy(bits, start + pos, bits, start, len - pos);
		System.arraycopy(temp, 0, bits, start + len - pos, pos);
	}
	
	public static void rotateLeft(byte[] bits, byte[] temp, int start, int len, int pos)
	{
		System.arraycopy(bits, start, temp, 0, pos);
		System.arraycopy(bits, start + pos, bits, start, len - pos);
		System.arraycopy(temp, 0, bits, start + len - pos, pos);
	}
	
	public static void rotateLeft(short[] bits, short[] temp, int start, int len, int pos)
	{
		System.arraycopy(bits, start, temp, 0, pos);
		System.arraycopy(bits, start + pos, bits, start, len - pos);
		System.arraycopy(temp, 0, bits, start + len - pos, pos);
	}
	
	public static void rotateLeft(char[] bits, char[] temp, int start, int len, int pos)
	{
		System.arraycopy(bits, start, temp, 0, pos);
		System.arraycopy(bits, start + pos, bits, start, len - pos);
		System.arraycopy(temp, 0, bits, start + len - pos, pos);
	}
	
	public static void rotateLeft(int[] bits, int[] temp, int start, int len, int pos)
	{
		System.arraycopy(bits, start, temp, 0, pos);
		System.arraycopy(bits, start + pos, bits, start, len - pos);
		System.arraycopy(temp, 0, bits, start + len - pos, pos);
	}
	
	public static void rotateLeft(long[] bits, long[] temp, int start, int len, int pos)
	{
		System.arraycopy(bits, start, temp, 0, pos);
		System.arraycopy(bits, start + pos, bits, start, len - pos);
		System.arraycopy(temp, 0, bits, start + len - pos, pos);
	}
	
	public static void rotateRight(boolean[] bits, boolean[] temp,int start, int len, int pos)
	{
		System.arraycopy(bits, start + len - pos, temp, 0, pos);
		System.arraycopy(bits, start, bits, start + pos, len - pos);
		System.arraycopy(temp, 0, bits, start, pos);
	}
	
	public static void rotateRight(byte[] bits, byte[] temp, int start, int len, int pos)
	{
		System.arraycopy(bits, start + len - pos, temp, 0, pos);
		System.arraycopy(bits, start, bits, start + pos, len - pos);
		System.arraycopy(temp, 0, bits, start, pos);
	}
	
	public static void rotateRight(short[] bits, short[] temp, int start, int len, int pos)
	{
		System.arraycopy(bits, start + len - pos, temp, 0, pos);
		System.arraycopy(bits, start, bits, start + pos, len - pos);
		System.arraycopy(temp, 0, bits, start, pos);
	}
	
	public static void rotateRight(char[] bits, char[] temp, int start, int len, int pos)
	{
		System.arraycopy(bits, start + len - pos, temp, 0, pos);
		System.arraycopy(bits, start, bits, start + pos, len - pos);
		System.arraycopy(temp, 0, bits, start, pos);
	}
	
	public static void rotateRight(int[] bits, int[] temp, int start, int len, int pos)
	{
		System.arraycopy(bits, start + len - pos, temp, 0, pos);
		System.arraycopy(bits, start, bits, start + pos, len - pos);
		System.arraycopy(temp, 0, bits, start, pos);
	}
	
	public static void rotateRight(long[] bits, long[] temp, int start, int len, int pos)
	{
		System.arraycopy(bits, start + len - pos, temp, 0, pos);
		System.arraycopy(bits, start, bits, start + pos, len - pos);
		System.arraycopy(temp, 0, bits, start, pos);
	}
	
	public static void rotateLeft1(boolean[] bits)
	{
		boolean temp = bits[0];
		System.arraycopy(bits, 1, bits, 0, bits.length - 1);
		bits[bits.length - 1] = temp;
	}
	
	public static void rotateRight1(boolean[] bits)
	{
		boolean temp = bits[bits.length - 1];
		System.arraycopy(bits, 0, bits, 1, bits.length - 1);
		bits[0] = temp;
	}
	
	public static void rotateLeft1(byte[] bits)
	{
		byte temp = bits[0];
		System.arraycopy(bits, 1, bits, 0, bits.length - 1);
		bits[bits.length - 1] = temp;
	}
	
	public static void rotateRight1(byte[] bits)
	{
		byte temp = bits[bits.length - 1];
		System.arraycopy(bits, 0, bits, 1, bits.length - 1);
		bits[0] = temp;
	}
	
	public static void rotateLeft1(short[] bits)
	{
		short temp = bits[0];
		System.arraycopy(bits, 1, bits, 0, bits.length - 1);
		bits[bits.length - 1] = temp;
	}
	
	public static void rotateRight1(short[] bits)
	{
		short temp = bits[bits.length - 1];
		System.arraycopy(bits, 0, bits, 1, bits.length - 1);
		bits[0] = temp;
	}
	
	public static void rotateLeft1(char[] bits)
	{
		char temp = bits[0];
		System.arraycopy(bits, 1, bits, 0, bits.length - 1);
		bits[bits.length - 1] = temp;
	}
	
	public static void rotateRight1(char[] bits)
	{
		char temp = bits[bits.length - 1];
		System.arraycopy(bits, 0, bits, 1, bits.length - 1);
		bits[0] = temp;
	}
	
	public static void rotateLeft1(int[] bits)
	{
		int temp = bits[0];
		System.arraycopy(bits, 1, bits, 0, bits.length - 1);
		bits[bits.length - 1] = temp;
	}
	
	public static void rotateRight1(int[] bits)
	{
		int temp = bits[bits.length - 1];
		System.arraycopy(bits, 0, bits, 1, bits.length - 1);
		bits[0] = temp;
	}
	
	public static void rotateLeft1(long[] bits)
	{
		long temp = bits[0];
		System.arraycopy(bits, 1, bits, 0, bits.length - 1);
		bits[bits.length - 1] = temp;
	}
	
	public static void rotateRight1(long[] bits)
	{
		long temp = bits[bits.length - 1];
		System.arraycopy(bits, 0, bits, 1, bits.length - 1);
		bits[0] = temp;
	}
	
	public static void rotateLeft1(boolean[] bits, int start, int len)
	{
		boolean temp = bits[start];
		System.arraycopy(bits, start + 1, bits, start, len - 1);
		bits[start + len - 1] = temp;
	}
	
	public static void rotateLeft1(byte[] bits, int start, int len)
	{
		byte temp = bits[start];
		System.arraycopy(bits, start + 1, bits, start, len - 1);
		bits[start + len - 1] = temp;
	}
	
	public static void rotateLeft1(short[] bits, int start, int len)
	{
		short temp = bits[start];
		System.arraycopy(bits, start + 1, bits, start, len - 1);
		bits[start + len - 1] = temp;
	}
	
	public static void rotateLeft1(char[] bits, int start, int len)
	{
		 char temp = bits[start];
		System.arraycopy(bits, start + 1, bits, start, len - 1);
		bits[start + len - 1] = temp;
	}
	
	public static void rotateLeft1(int[] bits, int start, int len)
	{
		int temp = bits[start];
		System.arraycopy(bits, start + 1, bits, start, len - 1);
		bits[start + len - 1] = temp;
	}
	
	public static void rotateLeft1(long[] bits, int start, int len)
	{
		long temp = bits[start];
		System.arraycopy(bits, start + 1, bits, start, len - 1);
		bits[start + len - 1] = temp;
	}
	
	public static void rotateRight1(boolean[] bits, int start, int len)
	{
		boolean temp = bits[start + len - 1];
		System.arraycopy(bits, start, bits, start + 1, len - 1);
		bits[start] = temp;
	}
	
	public static void rotateRight1(byte[] bits, int start, int len)
	{
		final byte temp = bits[start + len - 1];
		System.arraycopy(bits, start, bits, start + 1, len - 1);
		bits[start] = temp;
	}
	
	public static void rotateRight1(short[] bits, int start, int len)
	{
		final short temp = bits[start + len - 1];
		System.arraycopy(bits, start, bits, start + 1, len - 1);
		bits[start] = temp;
	}
	
	public static void rotateRight1(char[] bits, int start, int len)
	{
		final char temp = bits[start + len - 1];
		System.arraycopy(bits, start, bits, start + 1, len - 1);
		bits[start] = temp;
	}
	
	public static void rotateRight1(int[] bits, int start, int len)
	{
		final int temp = bits[start + len - 1];
		System.arraycopy(bits, start, bits, start + 1, len - 1);
		bits[start] = temp;
	}
	
	public static void rotateRight1(long[] bits, int start, int len)
	{
		final long temp = bits[start + len - 1];
		System.arraycopy(bits, start, bits, start + 1, len - 1);
		bits[start] = temp;
	}
	
	public static int countTrue(byte in)
	{
		return countTrue(in & 0xFF);
	}
	
	public static int countTrue(short in)
	{
		return countTrue(in & 0xFFFF);
	}
	
	public static int countTrue(int in)
	{
		int i = 0;
		for(; in != 0; in >>>= 1) {
			if((in & 1) != 0)
				i++;
		}
		return i;
	}
	
	public static int countTrue(long in)
	{
		int i = 0;
		for(; in != 0; in >>>= 1) {
			if((in & 1) != 0)
				i++;
		}
		return i;
	}
	
	public static int countDifferent(byte start, byte end)
	{
		return countTrue(start ^ end);
	}
	
	public static int countDifferent(short start, short end)
	{
		return countTrue(start ^ end);
	}
	
	public static int countDifferent(int start, int end)
	{
		return countTrue(start ^ end);
	}
	
	public static int countDifferent(long start, long end)
	{
		return countTrue(start ^ end);
	}
	
	public static boolean powerOfTwo(byte in)
	{
		return (in & (in - 1)) == 0;
	}
	
	public static boolean powerOfTwo(short in)
	{
		return (in & (in - 1)) == 0;
	}
	
	public static boolean powerOfTwo(char in)
	{
		return (in & (in - 1)) == 0;
	}
	
	public static boolean powerOfTwo(int in)
	{
		return (in & (in - 1)) == 0;
	}
	
	public static boolean powerOfTwo(long in)
	{
		return (in & (in - 1)) == 0;
	}
	
	public static void bitsToByte(boolean[] bits, int start0, byte[] bytes, int start1, int len)
	{
		int byteroof = start1 + len, roof;
		byte mask;
		while(start1 < byteroof) {
			byte num = 0;
			mask = 0x01;
			roof = start0 + 8;
			while(start0 < roof) {
				if(bits[start0++]) 
					num |= mask;
				mask <<= 1;
			}
			bytes[start1++] = num;
		}
	}
	
	public static void bitsToByte(boolean[] bits, int start0, byte[] bytes, int start1)
	{
		bitsToByte(bits, start0, bytes, start1, (bits.length >> 3) + ((bits.length % 8 > 0) ? 1 : 0));
	}
	
	public static void bitsToByte(boolean[] bits, byte[] bytes)
	{
		bitsToByte(bits, 0, bytes, 0, (bits.length >> 3) + ((bits.length % 8 > 0) ? 1 : 0));
	}
	
	public static byte[] bitsToByte(boolean[] bits, int start, int len)
	{
		byte[] bytes = new byte[(len >> 3) + (len % 8 > 0 ? 1 : 0)];
		bitsToByte(bits, start, bytes, 0, len);
		return bytes;
	}
	
	public static byte[] bitsToByte(boolean[] bits)
	{
		byte[] bytes = new byte[(bits.length >> 3) + (bits.length % 8 > 0 ? 1 : 0)];
		bitsToByte(bits, bytes);
		return bytes;
	}
	
	public static void bitsToShort(boolean[] bits, int start0, short[] shorts, int start1, int len)
	{
		int shortroof = start1 + len, roof;
		short mask;
		while(start1 < shortroof) {
			short num = 0;
			mask = 0x0001;
			roof = start0 + 16;
			while(start0 < roof) {
				if(bits[start0++]) 
					num |= mask;
				mask <<= 1;
			}
			shorts[start1++] = num;
		}
	}
	
	public static void bitsToShort(boolean[] bits, int start0, short[] shorts, int start1)
	{
		bitsToShort(bits, start0, shorts, start1, (bits.length >> 4) + ((bits.length % 16 > 0) ? 1 : 0));
	}
	
	public static void bitsToShort(boolean[] bits, short[] shorts)
	{
		bitsToShort(bits, 0, shorts, 0, (bits.length >> 4) + ((bits.length % 16 > 0) ? 1 : 0));
	}
	
	public static short[] bitsToShort(boolean[] bits)
	{
		short[] shorts = new short[(bits.length >> 4) + (bits.length % 16 > 0 ? 1 : 0)];
		bitsToShort(bits, shorts);
		return shorts;
	}
	
	public static short[] bitsToShort(boolean[] bits, int start, int len)
	{
		short[] shorts = new short[(len >> 4) + (len % 16 > 0 ? 1 : 0)];
		bitsToShort(bits, start, shorts, 0, len);
		return shorts;
	}
	
	public static void bitsToChar(boolean[] bits, int start0, char[] chars, int start1, int len)
	{
		int charroof = start1 + len, roof;
		char mask;
		while(start1 < charroof) {
			char num = 0;
			mask = 0x0001;
			roof = start0 + 16;
			while(start0 < roof) {
				if(bits[start0++]) 
					num |= mask;
				mask <<= 1;
			}
			chars[start1++] = num;
		}
	}
	
	public static void bitsToChar(boolean[] bits, int start0, char[] chars, int start1)
	{
		bitsToChar(bits, start0, chars, start1, (bits.length >> 4) + ((bits.length % 16 > 0) ? 1 : 0));
	}
	
	public static void bitsToChar(boolean[] bits, char[] chars)
	{
		bitsToChar(bits, 0, chars, 0, (bits.length >> 4) + ((bits.length % 16 > 0) ? 1 : 0));
	}
	
	public static char[] bitsToChar(boolean[] bits, int start, int len)
	{
		char[] chars = new char[(len>> 4) + (len % 16 > 0 ? 1 : 0)];
		bitsToChar(bits, start, chars, 0, len);
		return chars;
	}
	
	public static char[] bitsToChar(boolean[] bits)
	{
		char[] chars = new char[(bits.length >> 4) + (bits.length % 16 > 0 ? 1 : 0)];
		bitsToChar(bits, chars);
		return chars;
	}
	
	public static void bitsToInt(boolean[] bits, int start0, int[] ints, int start1, int len)
	{
		int introof = start1 + len, roof;
		int mask;
		while(start1 < introof) {
			int num = 0;
			mask = 0x00000001;
			roof = start0 + 32;
			while(start0 < roof) {
				if(bits[start0++]) 
					num |= mask;
				mask <<= 1;
			}
			ints[start1++] = num;
		}
	}
	
	public static void bitsToInt(boolean[] bits, int start0, int[] ints, int start1)
	{
		bitsToInt(bits, start0, ints, start1, (bits.length >> 5) + ((bits.length % 32 > 0) ? 1 : 0));
	}
	
	public static void bitsToInt(boolean[] bits, int[] ints)
	{
		bitsToInt(bits, 0, ints, 0, (bits.length >> 5) + ((bits.length % 32 > 0) ? 1 : 0));
	}
	
	public static int[] bitsToInt(boolean[] bits, int start, int len)
	{
		int[] ints = new int[(len >> 5) + (len % 32 > 0 ? 1 : 0)];
		bitsToInt(bits, start, ints, 0, len);
		return ints;
	}
	
	public static int[] bitsToInt(boolean[] bits)
	{
		int[] ints = new int[(bits.length >> 5) + (bits.length % 32 > 0 ? 1 : 0)];
		bitsToInt(bits, ints);
		return ints;
	}
	
	public static void bitsToLong(boolean[] bits, int start0, long[] longs, int start1, int len)
	{
		int longroof = start1 + len, roof;
		long mask;
		while(start1 < longroof) {
			long num = 0;
			mask = 0x0000000000000001L;
			roof = start0 + 64;
			while(start0 < roof) {
				if(bits[start0++]) 
					num |= mask;
				mask <<= 1;
			}
			longs[start1++] = num;
		}
	}
	
	public static void bitsToLong(boolean[] bits, int start0, long[] longs, int start1)
	{
		bitsToLong(bits, start0, longs, start1, (bits.length >> 6) + ((bits.length % 64 > 0) ? 1 : 0));
	}
	
	public static void bitsToLong(boolean[] bits, long[] longs)
	{
		bitsToLong(bits, 0, longs, 0, (bits.length >> 6) + ((bits.length % 64 > 0) ? 1 : 0));
	}
	
	public static long[] bitsToLong(boolean[] bits, int start, int len)
	{
		long[] longs = new long[(len >> 6) + (len % 64 > 0 ? 1 : 0)];
		bitsToLong(bits, start, longs, 0, len);
		return longs;
	}
	
	public static long[] bitsToLong(boolean[] bits)
	{
		long[] longs = new long[(bits.length >> 6) + (bits.length % 64 > 0 ? 1 : 0)];
		bitsToLong(bits, longs);
		return longs;
	}
	
	public static void nibblesToBits(byte[] nibbles, int start0, boolean[] bits, int start1, int len)
	{
		int roof = len + start0;
		byte num;
		while(start0 < roof)
		{
			num = nibbles[start0++];
			bits[start1++] = (num & 0x1) != 0;
			bits[start1++] = (num & 0x2) != 0;
			bits[start1++] = (num & 0x4) != 0;
			bits[start1++] = (num & 0x8) != 0;
		}
	}
	
	public static void nibblesToBits(byte[] nibbles, int start0, boolean[] bits, int start1)
	{
		nibblesToBits(nibbles, start0, bits, start1, nibbles.length - start0);
	}
	
	public static void nibblesToBits(byte[] nibbles, boolean[] bits)
	{
		nibblesToBits(nibbles, 0, bits, 0, nibbles.length);
	}
	
	public static boolean[] nibblesToBits(byte[] nibbles, int start, int len)
	{
		boolean[] bits = new boolean[len << 2];
		nibblesToBits(nibbles, start, bits, 0, len);
		return bits;
	}
	
	public static boolean[] nibblesToBits(byte[] nibbles)
	{
		boolean[] bits = new boolean[nibbles.length << 2];
		nibblesToBits(nibbles, bits);
		return bits;
	}
	
	public static void nibblesToBytes(byte[] nibbles, int start0, byte[] bytes, int start1, int len)
	{
		final int roof = start0 + len;
		while(start0 < roof)
		{
			bytes[start1++] = (byte) (((nibbles[start0++] & 0xF) << 4) |
				                       (nibbles[start0++] & 0xF));
		}
	}
	
	public static void nibblesToBytes(byte[] nibbles, int start0, byte[] bytes, int start1)
	{
		nibblesToBytes(nibbles, start0, bytes, start1, nibbles.length);
	}
	
	public static void nibblesToBytes(byte[] nibbles, byte[] bytes)
	{
		nibblesToBytes(nibbles, 0, bytes, 0, nibbles.length);
	}
	
	public static byte[] nibblesToBytes(byte[] nibbles, int start, int len)
	{
		byte[] bytes = new byte[len >>> 1];
		nibblesToBytes(nibbles, start, bytes, 0, len);
		return bytes;
	}
	
	public static byte[] nibblesToBytes(byte[] nibbles)
	{
		byte[] bytes = new byte[nibbles.length >>> 1];
		nibblesToBytes(nibbles, bytes);
		return bytes;
	}
	
	public static void nibblesToShorts(byte[] nibbles, int start0, short[] shorts, int start1, int len)
	{
		int roof = len + start1;
		while(start1 < roof)
		{
			shorts[start1++] = (short) (((nibbles[start0++] & 0xF) << 12) |
  				  					    ((nibbles[start0++] & 0xF) << 8 ) |
  				  					    ((nibbles[start0++] & 0xF) << 4 ) |
  				  					    ((nibbles[start0++] & 0xF)));
		}
	}
	
	public static void nibblesToShorts(byte[] nibbles, int start0, short[] shorts, int start1)
	{
		nibblesToShorts(nibbles, start0, shorts, start1, shorts.length - start1);
	}
	
	public static void nibblesToShorts(byte[] nibbles, short[] shorts)
	{
		nibblesToShorts(nibbles, 0, shorts, 0, shorts.length);
	}
	
	public static short[] nibblesToShorts(byte[] nibbles, int start, int len)
	{
		short[] shorts = new short[len >>> 2];
		nibblesToShorts(nibbles, start, shorts, 0, len);
		return shorts;
	}
	
	public static short[] nibblesToShorts(byte[] nibbles)
	{
		short[] shorts = new short[nibbles.length >>> 2];
		nibblesToShorts(nibbles, shorts);
		return shorts;
	}
	
	public static void nibblesToChars(byte[] nibbles, int start0, char[] chars, int start1, int len)
	{
		int roof = len + start1;
		while(start1 < roof)
		{
			chars[start1++] = (char) (((nibbles[start0++] & 0xF) << 12) |
					    			  ((nibbles[start0++] & 0xF) << 8 ) |
					    			  ((nibbles[start0++] & 0xF) << 4 ) |
					    			  ((nibbles[start0++] & 0xF)));
		}
	}
	
	public static void nibblesToChars(byte[] nibbles, int start0, char[] chars, int start1)
	{
		nibblesToChars(nibbles, start0, chars, start1, chars.length - start1);
	}
	
	public static void nibblesToChars(byte[] nibbles, char[] chars)
	{
		nibblesToChars(nibbles, 0, chars, 0, chars.length);
	}
	
	public static char[] nibblesToChars(byte[] nibbles, int start, int len)
	{
		char[] chars = new char[len >> 1];
		nibblesToChars(nibbles, start, chars, 0, len);
		return chars;
	}
	
	public static char[] nibblesToChars(byte[] nibbles)
	{
		char[] chars = new char[nibbles.length >> 1];
		nibblesToChars(nibbles, chars);
		return chars;
	}
	
	public static void nibblesToInts(byte[] nibbles, int start0, int[] ints, int start1, int len)
	{
		int roof = len + start1;
		while(start1 < roof)
		{
			ints[start1++] = (((nibbles[start0++] & 0xF) << 28) |
	    			  		  ((nibbles[start0++] & 0xF) << 24) |
	    			  		  ((nibbles[start0++] & 0xF) << 20) |
	    			  		  ((nibbles[start0++] & 0xF) << 16) |
	    			  		  ((nibbles[start0++] & 0xF) << 12) |
	    			  		  ((nibbles[start0++] & 0xF) << 8 ) |
	    			  		  ((nibbles[start0++] & 0xF) << 4 ) |
	    			  		  ((nibbles[start0++] & 0xF)));
		}
	}
	
	public static void nibblesToInts(byte[] nibbles, int start0, int[] ints, int start1)
	{
		nibblesToInts(nibbles, start0, ints, start1, ints.length - start1);
	}
	
	public static void nibblesToInts(byte[] nibbles, int[] ints)
	{
		nibblesToInts(nibbles, 0, ints, 0, ints.length);
	}
	
	public static int[] nibblesToInts(byte[] nibbles, int start, int len)
	{
		int[] ints = new int[len >> 2];
		nibblesToInts(nibbles, start, ints, 0, len);
		return ints;
	}
	
	public static int[] nibblesToInts(byte[] nibbles)
	{
		int[] ints = new int[nibbles.length >> 2];
		nibblesToInts(nibbles, ints);
		return ints;
	}
	
	public static void nibblesToLongs(byte[] nibbles, int start0, long[] longs, int start1, int len)
	{
		long roof = len + start1;
		while(start1 < roof)
		{
			longs[start1++] = (((nibbles[start0++] & 0xFL) << 60) |
							   ((nibbles[start0++] & 0xFL) << 56) |
							   ((nibbles[start0++] & 0xFL) << 52) |
							   ((nibbles[start0++] & 0xFL) << 48) |
							   ((nibbles[start0++] & 0xFL) << 44) |
	  		  		   		   ((nibbles[start0++] & 0xFL) << 40) |
	  		  		   		   ((nibbles[start0++] & 0xFL) << 36) |
	  		  		   		   ((nibbles[start0++] & 0xFL) << 32) |
	  		  		   		   ((nibbles[start0++] & 0xFL) << 38) |
			  		  		   ((nibbles[start0++] & 0xFL) << 24) |
			  		  		   ((nibbles[start0++] & 0xFL) << 20) |
			  		  		   ((nibbles[start0++] & 0xFL) << 16) |
			  		  		   ((nibbles[start0++] & 0xFL) << 12) |
			  		  		   ((nibbles[start0++] & 0xFL) << 8 ) |
			  		  		   ((nibbles[start0++] & 0xFL) << 4 ) |
			  		  		   ((nibbles[start0++] & 0xFL)));
		}
	}
	
	public static void nibblesToLongs(byte[] nibbles, int start0, long[] longs, int start1)
	{
		nibblesToLongs(nibbles, start0, longs, start1, longs.length - start1);
	}
	
	public static void nibblesToLongs(byte[] nibbles, long[] longs)
	{
		nibblesToLongs(nibbles, 0, longs, 0, longs.length);
	}
	
	public static long[] nibblesToLongs(byte[] nibbles, int start, int len)
	{
		long[] longs = new long[len >> 3];
		nibblesToLongs(nibbles, start, longs, 0, len);
		return longs;
	}
	
	public static long[] nibblesToLongs(byte[] nibbles)
	{
		long[] longs = new long[nibbles.length >> 3];
		nibblesToLongs(nibbles, longs);
		return longs;
	}
	
	public static void bytesToBits(byte[] bytes, int start0, boolean[] bits, int start1, int len)
	{
		int roof = len + start0;
		byte mask, num;
		while(start0 < roof)
		{
			num = bytes[start0++];
			mask = 1;
			while(mask != 0) {
				bits[start1++] = (num & mask) != 0;
				mask <<= 1;
			}
		}
	}
	
	public static void bytesToBits(byte[] bytes, int start0, boolean[] bits, int start1)
	{
		bytesToBits(bytes, start0, bits, start1, bytes.length - start0);
	}
	
	public static void bytesToBits(byte[] bytes, boolean[] bits)
	{
		bytesToBits(bytes, 0, bits, 0, bytes.length);
	}
	
	public static boolean[] bytesToBits(byte[] bytes, int start, int len)
	{
		boolean[] bits = new boolean[len << 3];
		bytesToBits(bytes, start, bits, 0, len);
		return bits;
	}
	
	public static boolean[] bytesToBits(byte[] bytes)
	{
		boolean[] bits = new boolean[bytes.length << 3];
		bytesToBits(bytes, bits);
		return bits;
	}
	
	public static void bytesToNibbles(byte[] bytes, int start0, byte[] nibbles, int start1, int len)
	{
		final int roof = start0 + len;
		while(start0 < roof)
		{
			nibbles[start1++] = (byte) ((bytes[start0  ] & 0xFF) >>> 4); 
			nibbles[start1++] = (byte)  (bytes[start0++] & 0x0F); 
		}
	}
	
	public static void bytesToNibbles(byte[] bytes, int start0, byte[] nibbles, int start1)
	{
		bytesToNibbles(bytes, start0, nibbles, start1, bytes.length);
	}
	
	public static void bytesToNibbles(byte[] bytes, byte[] nibbles)
	{
		bytesToNibbles(bytes, 0, nibbles, 0, bytes.length);
	}
	
	public static byte[] bytesToNibbles(byte[] bytes, int start, int len)
	{
		byte[] nibbles = new byte[len << 1];
		bytesToNibbles(bytes, start, nibbles, 0, len);
		return nibbles;
	}
	
	public static byte[] bytesToNibbles(byte[] bytes)
	{
		byte[] nibbles = new byte[bytes.length << 1];
		bytesToNibbles(bytes, nibbles);
		return nibbles;
	}
	
	public static void bytesToShortsFull(byte[] bytes, int start0, short[] shorts, int start1)
	{
		int len = bytes.length - start0;
		int intlen = len >> 1;
		bytesToShorts(bytes, start0, shorts, start1, intlen);
		int rem = len & 1;
		if(rem > 0)
			shorts[start1 + intlen] = Bits.shortFromBytes(bytes, bytes.length - rem, rem);
	}
	
	public static short[] bytesToShortsFull(byte[] bytes)
	{
		if((bytes.length & 1) > 0) {
			short[] shorts = new short[(bytes.length >> 1) + 1];
			bytesToShortsFull(bytes, 0, shorts, 0);
			return shorts;
		} else 
			return bytesToShorts(bytes);
	}
	
	public static void bytesToShorts(byte[] bytes, int start0, short[] shorts, int start1, int len)
	{
		int roof = len + start1;
		while(start1 < roof)
		{
			shorts[start1++] = (short) (((bytes[start0++] & 0x00FF)) |
									     (bytes[start0++] & 0x00FF) << 8);
		}
	}
	
	public static void bytesToShorts(byte[] bytes, int start0, short[] shorts, int start1)
	{
		bytesToShorts(bytes, start0, shorts, start1, shorts.length - start1);
	}
	
	public static void bytesToShorts(byte[] bytes, short[] shorts)
	{
		bytesToShorts(bytes, 0, shorts, 0, shorts.length);
	}
	
	public static short[] bytesToShorts(byte[] bytes, int start, int len)
	{
		short[] shorts = new short[len >> 1];
		bytesToShorts(bytes, start, shorts, 0, len);
		return shorts;
	}
	
	public static short[] bytesToShorts(byte[] bytes)
	{
		short[] shorts = new short[bytes.length >> 1];
		bytesToShorts(bytes, shorts);
		return shorts;
	}
	
	public static void bytesToChars(byte[] bytes, int start0, char[] chars, int start1, int len)
	{
		int roof = len + start1;
		while(start1 < roof)
		{
			chars[start1++] = (char) (((bytes[start0++] & 0x00FF)) |
				    					(bytes[start0++]) << 8);
		}
	}
	
	public static void bytesToChars(byte[] bytes, int start0, char[] chars, int start1)
	{
		bytesToChars(bytes, start0, chars, start1, chars.length - start1);
	}
	
	public static void bytesToChars(byte[] bytes, char[] chars)
	{
		bytesToChars(bytes, 0, chars, 0, chars.length);
	}
	
	public static char[] bytesToChars(byte[] bytes, int start, int len)
	{
		char[] chars = new char[len >> 1];
		bytesToChars(bytes, start, chars, 0, len);
		return chars;
	}
	
	public static char[] bytesToChars(byte[] bytes)
	{
		char[] chars = new char[bytes.length >> 1];
		bytesToChars(bytes, chars);
		return chars;
	}
	
	public static void bytesToIntsFull(byte[] bytes, int start0, int[] ints, int start1, int len)
	{
		int intlen = len >> 2;
		bytesToInts(bytes, start0, ints, start1, intlen);
		int rem = len & 3;
		if(rem > 0)
			ints[start1 + intlen] = Bits.intFromBytes(bytes, bytes.length - rem, rem);
	}
	
	public static void bytesToIntsFull(byte[] bytes, int start0, int[] ints, int start1)
	{
		int len = bytes.length - start0;
		int intlen = len >> 2;
		bytesToInts(bytes, start0, ints, start1, intlen);
		int rem = len & 3;
		if(rem > 0)
			ints[start1 + intlen] = Bits.intFromBytes(bytes, bytes.length - rem, rem);
	}
	
	public static int[] bytesToIntsFull(byte[] bytes)
	{
		if((bytes.length & 3) > 0) {
			int[] ints = new int[(bytes.length >> 2) + 1];
			bytesToIntsFull(bytes, 0, ints, 0);
			return ints;
		} else 
			return bytesToInts(bytes);
	}
	
	public static void bytesToInts(byte[] bytes, int start0, int[] ints, int start1, int len)
	{
		int roof = len + start1;
		while(start1 < roof)
		{
			ints[start1++] = ((((int) bytes[start0++] & 0xFF)) |
							  (((int) bytes[start0++] & 0xFF) << 8 ) |
							  (((int) bytes[start0++] & 0xFF) << 16) |
							  (((int) bytes[start0++] & 0xFF) << 24));
		}
	}
	
	public static void bytesToInts(byte[] bytes, int start0, int[] ints, int start1)
	{
		bytesToInts(bytes, start0, ints, start1, ints.length - start1);
	}
	
	public static void bytesToInts(byte[] bytes, int[] ints)
	{
		bytesToInts(bytes, 0, ints, 0, ints.length);
	}
	
	public static int[] bytesToInts(byte[] bytes, int start, int len)
	{
		int[] ints = new int[len >> 2];
		bytesToInts(bytes, start, ints, 0, len);
		return ints;
	}
	
	public static int[] bytesToInts(byte[] bytes)
	{
		int[] ints = new int[bytes.length >> 2];
		bytesToInts(bytes, ints);
		return ints;
	}
	
	public static void bytesToLongsFull(byte[] bytes, int start0, long[] ints, int start1)
	{
		int len = bytes.length - start0;
		int intlen = len >> 3;
		bytesToLongs(bytes, start0, ints, start1, intlen);
		int rem = len & 7;
		if(rem > 0)
			ints[start1 + intlen] = Bits.longFromBytes(bytes, bytes.length - rem, rem);
	}
	
	public static long[] bytesToLongsFull(byte[] bytes)
	{
		if((bytes.length & 7) > 0) {
			long[] ints = new long[(bytes.length >> 3) + 1];
			bytesToLongsFull(bytes, 0, ints, 0);
			return ints;
		} else 
			return bytesToLongs(bytes);
	}
	
	public static void bytesToLongs(byte[] bytes, int start0, long[] longs, int start1, int len)
	{
		long roof = len + start1;
		while(start1 < roof)
		{
			longs[start1++] = ((((long) bytes[start0++] & 0xFF)) |
							   (((long) bytes[start0++] & 0xFF) <<  8) |
							   (((long) bytes[start0++] & 0xFF) << 16) |
							   (((long) bytes[start0++] & 0xFF) << 24) |
							   (((long) bytes[start0++] & 0xFF) << 32) |
							   (((long) bytes[start0++] & 0xFF) << 40) |
							   (((long) bytes[start0++] & 0xFF) << 48) |
							   (((long) bytes[start0++] & 0xFF) << 56));
		}
	}
	
	public static void bytesToLongs(byte[] bytes, int start0, long[] longs, int start1)
	{
		bytesToLongs(bytes, start0, longs, start1, longs.length - start1);
	}
	
	public static void bytesToLongs(byte[] bytes, long[] longs)
	{
		bytesToLongs(bytes, 0, longs, 0, longs.length);
	}
	
	public static long[] bytesToLongs(byte[] bytes, int start, int len)
	{
		long[] longs = new long[len >> 3];
		bytesToLongs(bytes, start, longs, 0, len);
		return longs;
	}
	
	public static long[] bytesToLongs(byte[] bytes)
	{
		long[] longs = new long[bytes.length >> 3];
		bytesToLongs(bytes, longs);
		return longs;
	}
	
	public static void shortsToBits(short[] shorts, int start0, boolean[] bits, int start1, int len)
	{
		int roof = len + start0;
		short mask, num;
		while(start0 < roof)
		{
			num = shorts[start0++];
			mask = 1;
			while(mask != 0) {
				bits[start1++] = (num & mask) != 0;
				mask <<= 1;
			}
		}
	}
	
	public static void shortsToBits(short[] shorts, int start0, boolean[] bits, int start1)
	{
		shortsToBits(shorts, start0, bits, start1, shorts.length - start0);
	}
	
	public static void shortsToBits(short[] shorts, boolean[] bits)
	{
		shortsToBits(shorts, 0, bits, 0, shorts.length);
	}
	
	public static boolean[] shortsToBits(short[] shorts, int start, int len)
	{
		boolean[] bits = new boolean[len << 4];
		shortsToBits(shorts, start, bits, 0, len);
		return bits;
	}
	
	public static boolean[] shortsToBits(short[] shorts)
	{
		boolean[] bits = new boolean[shorts.length << 4];
		shortsToBits(shorts, bits);
		return bits;
	}
	
	public static void shortToBytes(short data, byte[] bytes, int start)
	{
		bytes[start++] = (byte)  data;
		bytes[start++] = (byte) (data >>> 8);
	}
	
	public static void shortsToBytes(short[] shorts, int start0, byte[] bytes, int start1, int len)
	{
		int roof = len + start0;
		short reg;
		while(start0 < roof)
		{
			reg = shorts[start0++];
			bytes[start1++] = (byte)  reg;
			bytes[start1++] = (byte) (reg >>> 8);
		}
	}
	
	public static void shortsToBytes(short[] shorts, int start0, byte[] bytes, int start1)
	{
		shortsToBytes(shorts, start0, bytes, start1, shorts.length - start0);
	}
	
	public static void shortsToBytes(short[] shorts, byte[] bytes)
	{
		shortsToBytes(shorts, 0, bytes, 0);
	}
	
	public static byte[] shortsToBytes(short[] shorts, int start, int len)
	{
		byte[] bytes = new byte[len << 1];
		shortsToBytes(shorts, start, bytes, 0, len);
		return bytes;
	}
	
	public static byte[] shortsToBytes(short[] shorts)
	{
		byte[] bytes = new byte[shorts.length << 1];
		shortsToBytes(shorts, bytes);
		return bytes;
	}
	
	public static void shortsToChars(short[] shorts, int start0, char[] chars, int start1, int len)
	{
		int roof = len + start0;
		while(start0 < roof)
		{
			chars[start1++] = (char) shorts[start0++];
		}
	}
	
	public static void shortsToChars(short[] shorts, int start0, char[] chars, int start1)
	{
		shortsToChars(shorts, start0, chars, start1, shorts.length - start0);
	}
	
	public static void shortsToChars(short[] shorts, char[] chars)
	{
		shortsToChars(shorts, 0, chars, 0);
	}
	
	public static char[] shortsToChars(short[] shorts, int start, int len)
	{
		char[] chars = new char[len];
		shortsToChars(shorts, start, chars, 0, len);
		return chars;
	}
	
	public static char[] shortsToChars(short[] shorts)
	{
		char[] chars = new char[shorts.length];
		shortsToChars(shorts, chars);
		return chars;
	}
	
	public static void shortsToInts(short[] shorts, int start0, int[] ints, int start1, int len)
	{
		int roof = len + start1;
		while(start1 < roof)
		{
			ints[start1++] = ((int) (shorts[start0++] & 0xFFFF)) |
							 ((int) (shorts[start0++] & 0xFFFF) << 16);
		}
	}
	
	public static void shortsToInts(short[] shorts, int start0, int[] ints, int start1)
	{
		shortsToInts(shorts, start0, ints, start1, ints.length - start0);
	}
	
	public static void shortsToInts(short[] shorts, int[] ints)
	{
		shortsToInts(shorts, 0, ints, 0);
	}
	
	public static int[] shortsToInts(short[] shorts, int start, int len)
	{
		int[] ints = new int[len >> 1];
		shortsToInts(shorts, start, ints, 0, len);
		return ints;
	}
	
	public static int[] shortsToInts(short[] shorts)
	{
		int[] ints = new int[shorts.length >> 1];
		shortsToInts(shorts, ints);
		return ints;
	}
	
	public static void shortsToLongs(short[] shorts, int start0, long[] longs, int start1, int len)
	{
		int roof = len + start1;
		while(start1 < roof)
		{
			longs[start1++] = ((long) (shorts[start0++] & 0xFFFF)) |
							  ((long) (shorts[start0++] & 0xFFFF) << 16) |
							  ((long) (shorts[start0++] & 0xFFFF) << 32) |
							  ((long) (shorts[start0++] & 0xFFFF) << 48);
		}
	}
	
	public static void shortsToLongs(short[] shorts, int start0, long[] longs, int start1)
	{
		shortsToLongs(shorts, start0, longs, start1, longs.length - start1);
	}
	
	public static void shortsToLongs(short[] shorts, long[] longs)
	{
		shortsToLongs(shorts, 0, longs, 0);
	}
	
	public static long[] shortsToLongs(short[] shorts, int start, int len)
	{
		long[] longs = new long[len >> 2];
		shortsToLongs(shorts, start, longs, 0, len);
		return longs;
	}
	
	public static long[] shortsToLongs(short[] shorts)
	{
		long[] longs = new long[shorts.length >> 2];
		shortsToLongs(shorts, longs);
		return longs;
	}
	
	public static void charsToBits(char[] chars, int start0, boolean[] bits, int start1, int len)
	{
		int roof = len + start0;
		char mask, num;
		while(start0 < roof)
		{
			num = chars[start0++];
			mask = 1;
			while(mask != 0) {
				bits[start1++] = (num & mask) != 0;
				mask <<= 1;
			}
		}
	}
	
	public static void charsToBits(char[] chars, int start0, boolean[] bits, int start1)
	{
		charsToBits(chars, start0, bits, start1, chars.length - start0);
	}
	
	public static void charsToBits(char[] chars, boolean[] bits)
	{
		charsToBits(chars, 0, bits, 0, chars.length);
	}
	
	public static boolean[] charsToBits(char[] chars, int start, int len)
	{
		boolean[] bits = new boolean[len << 4];
		charsToBits(chars, start, bits, 0, len);
		return bits;
	}
	
	public static boolean[] charsToBits(char[] chars)
	{
		boolean[] bits = new boolean[chars.length << 4];
		charsToBits(chars, bits);
		return bits;
	}
	
	public static void charToBytes(char data, byte[] bytes, int start)
	{
		bytes[start++] = (byte)  data;
		bytes[start++] = (byte) (data >>> 8);
	}
	
	public static void charsToBytes(char[] chars, int start0, byte[] bytes, int start1, int len)
	{
		int roof = len + start0;
		char reg;
		while(start0 < roof)
		{
			reg = chars[start0++];
			bytes[start1++] = (byte)  reg;
			bytes[start1++] = (byte) (reg >>> 8);
		}
	}
	
	public static void charsToBytes(char[] chars, int start0, byte[] bytes, int start1)
	{
		charsToBytes(chars, start0, bytes, start1, chars.length - start0);
	}
	
	public static void charsToBytes(char[] chars, byte[] bytes)
	{
		charsToBytes(chars, 0, bytes, 0);
	}
	
	public static byte[] charsToBytes(char[] chars, int start, int len)
	{
		byte[] bytes = new byte[len << 1];
		charsToBytes(chars, start, bytes, 0, len);
		return bytes;
	}
	
	public static byte[] charsToBytes(char[] chars)
	{
		byte[] bytes = new byte[chars.length << 1];
		charsToBytes(chars, bytes);
		return bytes;
	}
	
	public static void charsToShorts(char[] chars, int start0, short[] shorts, int start1, int len)
	{
		int roof = len + start0;
		while(start0 < roof)
		{
			shorts[start1++] = (short) chars[start0++];
		}
	}
	
	public static void charsToShorts(char[] chars, int start0, short[] shorts, int start1)
	{
		charsToShorts(chars, start0, shorts, start1, chars.length - start0);
	}
	
	public static void charsToShorts(char[] chars, short[] shorts)
	{
		charsToShorts(chars, 0, shorts, 0);
	}
	
	public static short[] charsToShorts(char[] chars, int start, int len)
	{
		short[] shorts = new short[len];
		charsToShorts(chars, start, shorts, 0, len);
		return shorts;
	}
	
	public static short[] charsToShorts(char[] chars)
	{
		short[] shorts = new short[chars.length];
		charsToShorts(chars, shorts);
		return shorts;
	}
	
	public static void charsToInts(char[] chars, int start0, int[] ints, int start1, int len)
	{
		int roof = len + start1;
		while(start1 < roof)
		{
			ints[start1++] = ((int) (chars[start0++] & 0xFFFF)) |
							 ((int) (chars[start0++] & 0xFFFF) << 16);
		}
	}
	
	public static void charsToInts(char[] chars, int start0, int[] ints, int start1)
	{
		charsToInts(chars, start0, ints, start1, ints.length - start1);
	}
	
	public static void charsToInts(char[] chars, int[] ints)
	{
		charsToInts(chars, 0, ints, 0);
	}
	
	public static int[] charsToInts(char[] chars, int start, int len)
	{
		int[] ints = new int[len >> 1];
		charsToInts(chars, start, ints, 0, len);
		return ints;
	}
	
	public static int[] charsToInts(char[] chars)
	{
		int[] ints = new int[chars.length >> 1];
		charsToInts(chars, ints);
		return ints;
	}
	
	public static void charsToLongs(char[] chars, int start0, long[] longs, int start1, int len)
	{
		int roof = len + start1;
		while(start1 < roof)
		{
			longs[start1++] = ((long) (chars[start0++] & 0xFFFF)) |
							  ((long) (chars[start0++] & 0xFFFF) << 16) |
							  ((long) (chars[start0++] & 0xFFFF) << 32) |
							  ((long) (chars[start0++] & 0xFFFF) << 48);
		}
	}
	
	public static void charsToLongs(char[] chars, int start0, long[] longs, int start1)
	{
		charsToLongs(chars, start0, longs, start1, longs.length - start1);
	}
	
	public static void charsToLongs(char[] chars, long[] longs)
	{
		charsToLongs(chars, 0, longs, 0);
	}
	
	public static long[] charsToLongs(char[] chars, int start, int len)
	{
		long[] longs = new long[len >> 2];
		charsToLongs(chars, start, longs, 0, len);
		return longs;
	}
	
	public static long[] charsToLongs(char[] chars)
	{
		long[] longs = new long[chars.length >> 2];
		charsToLongs(chars, longs);
		return longs;
	}
	
	public static void intsToBits(int[] ints, int start0, boolean[] bits, int start1, int len)
	{
		int roof = len + start0;
		int mask, num;
		while(start0 < roof)
		{
			num = ints[start0++];
			mask = 1;
			while(mask != 0) {
				bits[start1++] = (num & mask) != 0;
				mask <<= 1;
			}
		}
	}
	
	public static void intsToBits(int[] ints, int start0, boolean[] bits, int start1)
	{
		intsToBits(ints, start0, bits, start1, ints.length - start0);
	}
	
	public static void intsToBits(int[] ints, boolean[] bits)
	{
		intsToBits(ints, 0, bits, 0, ints.length);
	}
	
	public static boolean[] intsToBits(int[] ints)
	{
		boolean[] bits = new boolean[ints.length << 5];
		intsToBits(ints, bits);
		return bits;
	}
	
	public static byte[] intToBytes(int data)
	{
		byte[] bytes = new byte[4];
		bytes[0] = (byte)  data;
		bytes[1] = (byte) (data >>> 8);
		bytes[2] = (byte) (data >>> 16);
		bytes[3] = (byte) (data >>> 24);
		return bytes;
	}
	
	public static void intToBytes(int data, byte[] bytes, int start)
	{
		bytes[start++] = (byte)  data;
		bytes[start++] = (byte) (data >>> 8);
		bytes[start++] = (byte) (data >>> 16);
		bytes[start++] = (byte) (data >>> 24);
	}
	
	public static void intToBytes(int data, byte[] bytes, int start, int len)
	{
		bytes[start] = (byte) data;
		for(int i = 1; i < len; i++) 
			bytes[start + i] = (byte) (data >>> BYTESHIFTS[i]);
	}
	
	public static void intsToSBytes(int[] ints, int start0, byte[] bytes, int start1, int len)
	{
		int last = len & 3;
		int intlen = len >> 2;
		intsToBytes(ints, start0, bytes, start1, intlen);
		if(last > 0)
			intToBytes(ints[start0 + intlen], bytes, start1 + len - last, last);
	}
	
	public static void intsToBytes(int[] ints, int start0, byte[] bytes, int start1, int len)
	{
		int roof = len + start0;
		int reg;
		while(start0 < roof)
		{
			reg = ints[start0++];
			bytes[start1++] = (byte) reg;
			bytes[start1++] = (byte) (reg >>> 8);
			bytes[start1++] = (byte) (reg >>> 16);
			bytes[start1++] = (byte) (reg >>> 24);
		}
	}
	
	public static void intsToBytes(int[] ints, int start0, byte[] bytes, int start1)
	{
		intsToBytes(ints, start0, bytes, start1, ints.length - start0);
	}
	
	public static void intsToBytes(int[] ints, byte[] bytes)
	{
		intsToBytes(ints, 0, bytes, 0, ints.length);
	}
	
	public static byte[] intsToBytes(int[] ints)
	{
		byte[] bytes = new byte[ints.length << 2];
		intsToBytes(ints, bytes);
		return bytes;
	}
	
	public static void intsToShorts(int[] ints, int start0, short[] shorts, int start1, int len)
	{
		int roof = len + start0;
		int reg;
		while(start0 < roof)
		{
			reg = ints[start0++];
			shorts[start1++] = (short) reg;
			shorts[start1++] = (short) (reg >>> 16);
		}
	}
	
	public static void intsToShorts(int[] ints, int start0, short[] shorts, int start1)
	{
		intsToShorts(ints, start0, shorts, start1, ints.length - start0);
	}
	
	public static void intsToShorts(int[] ints, short[] shorts)
	{
		intsToShorts(ints, 0, shorts, 0, ints.length);
	}
	
	public static short[] intsToShorts(int[] ints)
	{
		short[] shorts = new short[ints.length << 1];
		intsToShorts(ints, shorts);
		return shorts;
	}
	
	public static void intsToChars(int[] ints, int start0, char[] chars, int start1, int len)
	{
		int roof = len + start0;
		int reg;
		while(start0 < roof)
		{
			reg = ints[start0++];
			chars[start1++] = (char) reg;
			chars[start1++] = (char) (reg >>> 16);
		}
	}
	
	public static void intsToChars(int[] ints, int start0, char[] chars, int start1)
	{
		intsToChars(ints, start0, chars, start1, ints.length - start0);
	}
	
	public static void intsToChars(int[] ints, char[] chars)
	{
		intsToChars(ints, 0, chars, 0, ints.length);
	}
	
	public static char[] intsToChars(int[] ints)
	{
		char[] chars = new char[ints.length << 1];
		intsToChars(ints, chars);
		return chars;
	}
	
	public static void intsToLongs(int[] ints, int start0, long[] longs, int start1, int len)
	{
		int roof = start1 + len;
		while(start1 < roof)
		{
			longs[start1++] =  (ints[start0++] & 0xFFFFFFFFL) |
							  ((ints[start0++] & 0xFFFFFFFFL) << 32);
		}
	}
	
	public static void intsToLongs(int[] ints, int start0, long[] longs, int start1)
	{
		intsToLongs(ints, start0, longs, start1, longs.length - start1);
	}
	
	public static void intsToLongs(int[] ints, long[] longs)
	{
		intsToLongs(ints, 0, longs, 0, longs.length);
	}
	
	public static long[] intsToLongs(int[] ints)
	{
		long[] longs = new long[ints.length >> 1];
		intsToLongs(ints, longs);
		return longs;
	}
	
	public static void longsToBits(long[] longs, int start0, boolean[] bits, int start1, int len)
	{
		int roof = len + start0;
		long mask, num;
		while(start0 < roof)
		{
			num = longs[start0++];
			mask = 1;
			while(mask != 0) {
				bits[start1++] = (num & mask) != 0;
				mask <<= 1;
			}
		}
	}
	
	public static void longsToBits(long[] longs, int start0, boolean[] bits, int start1)
	{
		longsToBits(longs, start0, bits, start1, longs.length - start0);
	}
	
	public static void longsToBits(long[] longs, boolean[] bits)
	{
		longsToBits(longs, 0, bits, 0, longs.length);
	}
	
	public static boolean[] longsToBits(long[] longs)
	{
		boolean[] bits = new boolean[longs.length << 6];
		longsToBits(longs, bits);
		return bits;
	}
	
	public static byte[] longToBytes(long data)
	{
		byte[] bytes = new byte[8];
		bytes[0] = (byte)  data;
		bytes[1] = (byte) (data >>> 8);
		bytes[2] = (byte) (data >>> 16);
		bytes[3] = (byte) (data >>> 24);
		bytes[4] = (byte) (data >>> 32);
		bytes[5] = (byte) (data >>> 40);
		bytes[6] = (byte) (data >>> 48);
		bytes[7] = (byte) (data >>> 56);
		return bytes;
	}
	
	public static void longToBytes(long data, byte[] bytes, int start)
	{
		bytes[start++] = (byte)  data;
		bytes[start++] = (byte) (data >>> 8);
		bytes[start++] = (byte) (data >>> 16);
		bytes[start++] = (byte) (data >>> 24);
		bytes[start++] = (byte) (data >>> 32);
		bytes[start++] = (byte) (data >>> 40);
		bytes[start++] = (byte) (data >>> 48);
		bytes[start++] = (byte) (data >>> 56);
	}
	
	public static void longsToBytes(long[] longs, int start0, byte[] bytes, int start1, int len)
	{
		int roof = len + start0;
		long reg;
		while(start0 < roof)
		{
			reg = longs[start0++];
			bytes[start1++] = (byte) reg;
			bytes[start1++] = (byte) (reg >>> 8);
			bytes[start1++] = (byte) (reg >>> 16);
			bytes[start1++] = (byte) (reg >>> 24);
			bytes[start1++] = (byte) (reg >>> 32);
			bytes[start1++] = (byte) (reg >>> 40);
			bytes[start1++] = (byte) (reg >>> 48);
			bytes[start1++] = (byte) (reg >>> 56);
		}
	}
	
	public static void longsToBytes(long[] longs, int start0, byte[] bytes, int start1)
	{
		longsToBytes(longs, start0, bytes, start1, longs.length - start0);
	}
	
	public static void longsToBytes(long[] longs, byte[] bytes)
	{
		longsToBytes(longs, 0, bytes, 0, longs.length);
	}
	
	public static byte[] longsToBytes(long[] longs)
	{
		byte[] bytes = new byte[longs.length << 3];
		longsToBytes(longs, bytes);
		return bytes;
	}
	
	public static void longsToShorts(long[] longs, int start0, short[] shorts, int start1, int len)
	{
		int roof = len + start0;
		long reg;
		while(start0 < roof)
		{
			reg = longs[start0++];
			shorts[start1++] = (short) reg;
			shorts[start1++] = (short) (reg >>> 16);
			shorts[start1++] = (short) (reg >>> 32);
			shorts[start1++] = (short) (reg >>> 48);
		}
	}
	
	public static void longsToShorts(long[] longs, int start0, short[] shorts, int start1)
	{
		longsToShorts(longs, start0, shorts, start1, longs.length - start0);
	}
	
	public static void longsToShorts(long[] longs, short[] shorts)
	{
		longsToShorts(longs, 0, shorts, 0, longs.length);
	}
	
	public static short[] longsToShorts(long[] longs)
	{
		short[] shorts = new short[longs.length << 2];
		longsToShorts(longs, shorts);
		return shorts;
	}
	
	public static void longsToChars(long[] longs, int start0, char[] chars, int start1, int len)
	{
		int roof = len + start0;
		long reg;
		while(start0 < roof)
		{
			reg = longs[start0++];
			chars[start1++] = (char) reg;
			chars[start1++] = (char) (reg >>> 16);
			chars[start1++] = (char) (reg >>> 32);
			chars[start1++] = (char) (reg >>> 48);
		}
	}
	
	public static void longsToChars(long[] longs, int start0, char[] chars, int start1)
	{
		longsToChars(longs, start0, chars, start1, longs.length - start0);
	}
	
	public static void longsToChars(long[] longs, char[] chars)
	{
		longsToChars(longs, 0, chars, 0, longs.length);
	}
	
	public static char[] longsToChars(long[] longs)
	{
		char[] chars = new char[longs.length << 2];
		longsToChars(longs, chars);
		return chars;
	}
	
	public static void longsToInts(long[] longs, int start0, int[] ints, int start1, int len)
	{
		int roof = len + start0;
		long reg;
		while(start0 < roof)
		{
			reg = longs[start0++];
			ints[start1++] = (int) reg;
			ints[start1++] = (int) (reg >>> 32);
		}
	}
	
	public static void longsToInts(long[] longs, int start0, int[] ints, int start1)
	{
		longsToInts(longs, start0, ints, start1, longs.length - start0);
	}
	
	public static void longsToInts(long[] longs, int[] ints)
	{
		longsToInts(longs, 0, ints, 0, longs.length);
	}
	
	public static int[] longsToInts(long[] longs)
	{
		int[] ints = new int[longs.length << 1];
		longsToInts(longs, ints);
		return ints;
	}
	
	public static int countEndingZeroes(int[] arr)
	{
		int n = 0;
		for(int i = arr.length - 1; i >= 0; i--)
			if(arr[i] == 0)
				n++;
			else
				break;
		return n;
	}
	
	public static byte rotateLeft(byte b, int k)
	{
		return (byte) ((b << k) | ((b & 0xFF) >>> (8 - k)));
	}
	
	public static byte rotateRight(byte b, int k)
	{
		return (byte) (((b & 0xFF) >>> k) | (b << (8 - k)));
	}
	
	public static short rotateLeft(short b, int k)
	{
		return (short) ((b << k) | ((b & 0xFFFF) >>> (16 - k)));
	}
	
	public static short rotateRight(short b, int k)
	{
		return (short) (((b & 0xFFFF) >>> k) | (b << (16 - k)));
	}
	
	public static int rotateLeft(int b, int k)
	{
		return (b << k) | (b >>> (32 - k));
	}
	
	public static int rotateRight(int b, int k)
	{
		return ((b & 0xFFFFFFFF) >>> k) | (b << (32 - k));
	}
	
	public static long rotateLeft(long b, int k)
	{
		return (b << k) | ((b & 0xFFFFFFFFFFFFFFFFL) >>> (64 - k));
	}
	
	public static long rotateRight(long b, int k)
	{
		return (b >>> k) | (b << (64 - k));
	}
	
	public static int getTrue(boolean[] b)
	{
		int i = 0;
		for(boolean b2 : b)
			if(b2) i++;
		return i;
	}
	
	public static final class IntBuilder 
						implements IUUID<IntBuilder>, IReferrable<IntBuilder>
	{
		
		private int temp;
		private int pos = 0;
		
		public void add(byte b, int bitpos)
		{
			temp |= ((int) b << bitpos);
		}
		
		public void add(byte b)
		{
			temp |= ((int) b << pos);
			pos += 8;
		}
		
		public int get()
		{
			pos = 0;
			try {
				return temp;
			} finally {
				temp = 0;
			}
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.INTBUILDER;
		}

		public boolean sameAs(IntBuilder obj)
		{
			return (this.pos == obj.pos) && (this.temp == obj.temp);
		}
		
	}
	
	public static final class Bit8 
						extends BasicBits<Bit8> {
		
		public int getCapacity()
		{
			return 8;
		}

		byte bits = 0x00;
		
		public Bit8() {}
		
		public Bit8(byte b)
		{
			this.bits = b;
		}
		
		public Bit8(boolean[] b)
		{
			this.set(b);
		}
		
		public void set(byte b)
		{
			this.bits = b;
		}
		
		public void set(boolean[] b)
		{
			for(int i = 0; i < 8; i++)
				set(i, b[i]);
		}

		public void set(final int i, final boolean b)
		{
			if(b) 
				bits |= 1 << i;
			else {
				if(get(i))
					bits -= 1 << i;
			}
		}
		

		public boolean get(int i)
		{
			return (bits & (1 << i)) != 0;
		}

		public boolean[] get()
		{
			final boolean[] b = new boolean[8];
			for(int i = 0; i < 8; i++)
				b[i] = this.get(i);
			return b;
		}

		public void NOT()
		{
			this.bits = (byte) ~this.bits;
		}

		public void ROR(int i)
		{
			throw new java.lang.UnsupportedOperationException();
		}

		public void ROL(int i)
		{
			throw new java.lang.UnsupportedOperationException();
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.BIT8;
		}

		public boolean sameAs(Bit8 bits)
		{
			return bits.bits == this.bits;
		}
		
	}
	
	public static final class Bit16 
						extends BasicBits<Bit16> {
		
		public int getCapacity()
		{
			return 16;
		}

		short bits = 0x0000;
		
		public Bit16() {}
		
		public Bit16(short b)
		{
			this.bits = b;
		}
		
		public Bit16(boolean[] b)
		{
			this.set(b);
		}
		
		public void set(short b)
		{
			this.bits = b;
		}
		
		public void set(boolean[] b)
		{
			for(int i = 0; i < 16; i++)
				set(i, b[i]);
		}

		public void set(final int i, final boolean b)
		{
			if(b) 
				bits |= 1 << i;
			else {
				if(get(i))
					bits -= 1 << i;
			}
		}
		
		public void setStarting(int s, byte b)
		{
			Bit8 n = new Bit8(b);
			for(int i = s; i < s + n.getCapacity(); i++)
				this.set(i, n.get(i - s));
		}
		
		public boolean get(int i)
		{
			return (bits & (1 << i)) != 0;
		}

		public boolean[] get()
		{
			final boolean[] b = new boolean[16];
			for(int i = 0; i < 16; i++)
				b[i] = this.get(i);
			return b;
		}

		public void NOT()
		{
			this.bits = (byte) ~this.bits;
		}

		public void ROR(int i)
		{
			this.bits >>>= i;
		}

		public void ROL(int i)
		{
			this.bits <<= i;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.BIT16;
		}

		public boolean sameAs(Bit16 bits)
		{
			return bits.bits == this.bits;
		}
		
		
	}
	
	public static final class Bit32 
						extends BasicBits<Bit32> {
		
		public int getCapacity()
		{
			return 32;
		}

		public int bits = 0x00000000;
		
		public Bit32() {}
		
		public Bit32(int b)
		{
			this.bits = b;
		}
		
		public Bit32(boolean[] b)
		{
			this.set(b);
		}
		
		public void set(int b)
		{
			this.bits = b;
		}
		
		public void set(boolean[] b)
		{
			for(int i = 0; i < 32; i++)
				set(i, b[i]);
		}

		public void set(final int i, final boolean b)
		{
			if(b) 
				bits |= 1 << i;
			else {
				if(get(i))
					bits -= 1 << i;
			}
		}
		

		public boolean get(int i)
		{
			return (bits & (1 << i)) != 0;
		}

		public boolean[] get()
		{
			final boolean[] b = new boolean[32];
			for(int i = 0; i < 32; i++)
				b[i] = this.get(i);
			return b;
		}
		
		public void NOT()
		{
			this.bits = (byte) ~this.bits;
		}

		public void ROR(int i)
		{
			this.bits >>>= i;
		}

		public void ROL(int i)
		{
			this.bits <<= i;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.BIT32;
		}

		public boolean sameAs(Bit32 bits)
		{
			return bits.bits == this.bits;
		}
		
	}
	
	public static final class Bit64 
						extends BasicBits<Bit64> {
		
		public int getCapacity()
		{
			return 64;
		}
	
		long bits = 0x0000000000000;
		
		public Bit64() {}
		
		public Bit64(long l)
		{
			this.bits = l;
		}
		
		public Bit64(boolean[] b)
		{
			this.set(b);
		}
		
		public void set(long b)
		{
			this.bits = b;
		}
		
		public void set(boolean[] b)
		{
			for(int i = 0; i < 64; i++)
				set(i, b[i]);
		}

		public void set(final int i, final boolean b)
		{
			if(b) 
				bits |= 1 << i;
			else {
				if(get(i))
					bits -= 1 << i;
			}
		}
		
		public void setStarting(int s, byte b)
		{
			Bit8 n = new Bit8(b);
			for(int i = s; i < s + n.getCapacity(); i++)
				this.set(i, n.get(i - s));
		}
		
		public void setStarting(int pos, short s)
		{
			Bit16 n = new Bit16(s);
			for(int i = pos; i < pos + n.getCapacity(); i++)
				this.set(i, n.get(i - pos));
		}
		
		public void setStarting(int pos, int s)
		{
			Bit32 n = new Bit32(s);
			for(int i = pos; i < pos + n.getCapacity(); i++)
				this.set(i, n.get(i - pos));
		}

		public boolean get(int i)
		{
			return (bits & ((long) 1 << i)) != 0;
		}

		public boolean[] get()
		{
			final boolean[] b = new boolean[64];
			for(int i = 0; i < 64; i++)
				b[i] = this.get(i);
			return b;
		}
		
		public void NOT()
		{
			this.bits = (byte) ~this.bits;
		}

		public void ROR(int i)
		{
			this.bits >>>= i;
		}

		public void ROL(int i)
		{
			this.bits <<= i;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.BIT64;
		}

		public boolean sameAs(Bit64 bits)
		{
			return bits.bits == this.bits;
		}
				
	}	

	public static final class BitBool 
						extends BasicBits<BitBool> {
		
		private boolean[] bits;
		
		public BitBool(int size)
		{
			this.bits = new boolean[size];
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.BITBOOL;
		}

		public boolean sameAs(BitBool bits)
		{
			if(bits.getCapacity() == this.bits.length) {
				for(int i = 0; i < this.bits.length; i++)
					if(this.bits[i] ^ bits.bits[i])
						return false;
				return true;
			} else
				return false;
		}

		public int getCapacity()
		{
			return bits.length;
		}

		public void set(boolean[] b)
		{
			if(b.length != 32) throw new java.lang.IllegalArgumentException();
			this.bits = b;
		}

		public void set(int i, boolean b)
		{
			bits[i] = b;
		}

		public boolean get(int i)
		{
			return bits[i];
		}

		public boolean[] get()
		{
			return this.bits;
		}

		public void NOT()
		{
			for(int i = 0; i < bits.length; i++)
				bits[i] = !bits[i];
		}

		public void ROR(int pos)
		{
			final boolean[] n = new boolean[32];
			int cutoff = bits.length - pos;
			System.arraycopy(bits, 0, n, pos, cutoff);
			for(int i = 0; i < pos; i++)
				n[i] = bits[cutoff + i];
			this.bits = n;
		}

		public void ROL(int pos)
		{
			final boolean[] n = new boolean[32];
			int cutoff = bits.length - pos;
			System.arraycopy(bits, pos, n, 0, cutoff);
			for(int i = cutoff; i < bits.length; i++)
				n[i] = bits[i - cutoff];
			this.bits = n;
		}	
		
	}
	
	@SuppressWarnings("unchecked")
	public static class CompoundBits<T extends Bits<T>> 
				   extends BasicBits<CompoundBits<T>> {
		
		final DynArray<T> bits;
		final int each;
		
		public CompoundBits(T ... t)
		{
			bits = new DynArray<T>(t);
			each = t[0].getCapacity();
		}
		
		public void extend(int i)
		{
			bits.overflow(i);
		}
		
		public void extend(T ... t)
		{
			bits.add(t);
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.COMPOUNDBITS + bits.get(0).NAMESPACE();
		}

		public int getCapacity()
		{
			return this.each * bits.size();
		}

		public void set(boolean[] b)
		{
			throw new java.lang.UnsupportedOperationException();
		}

		public void set(int i, boolean b)
		{
			int arrpos = i / this.each;
			int pos = i - (arrpos * this.each);
			bits.get(arrpos).set(pos, b);
		}

		public boolean get(int i)
		{
			int arrpos = i / this.each;
			int pos = i - (arrpos * this.each);
			return bits.get(arrpos).get(pos);
		}

		public boolean[] get()
		{
			boolean[] bool = new boolean[bits.size() * this.each];
			for(int i = 0; i < bits.size(); i++)
				System.arraycopy(bits.get(i).get(), 0, bool, i * this.each, this.each);
			return bool;
		}

		public void NOT()
		{
			for(int i = 0; i < bits.size(); i++)
				bits.get(i).NOT();
		}

		public void ROR(int i)
		{
			throw new java.lang.UnsupportedOperationException();
		}

		public void ROL(int i)
		{
			throw new java.lang.UnsupportedOperationException();
		}

		public boolean sameAs(CompoundBits<T> obj)
		{
			if(obj.getCapacity() == this.getCapacity()) {
				for(int i = 0; i < this.getCapacity(); i++)
					if(obj.get(i) ^ this.get(i))
						return false;
				return true;
			}
			return false;
		}
		
	}
	
	public static final class FastDynBool 
						extends BasicBits<FastDynBool> {

		BooleanBuffer bits;
		
		public FastDynBool(boolean ... b)
		{
			bits = new BooleanBuffer(b);
		}
		
		public FastDynBool(int i)
		{
			bits = new BooleanBuffer(i);
		}
		
		public int NAMESPACE()
		{
			return _NAMESPACE.FASTDYNBOOL;
		}

		public int getCapacity()
		{
			return bits.length();
		}

		public void set(boolean[] b)
		{
			this.bits = new BooleanBuffer(b);
		}

		public void set(int i, boolean b)
		{
			this.bits.set(i, b);
		}

		public boolean get(int i)
		{
			return this.bits.get(i);
		}

		public boolean[] get()
		{
			return bits.get();
		}

		public void NOT()
		{
			for(int i = 0; i < bits.length(); i++)
				bits.set(i, !bits.get(i));
		}

		public void ROR(int i)
		{
			throw new java.lang.UnsupportedOperationException();
		}

		public void ROL(int i)
		{
			throw new java.lang.UnsupportedOperationException();
		}
		
		public boolean sameAs(FastDynBool obj)
		{
			if(this.getCapacity() == obj.getCapacity())  {
				for(int i = 0; i < this.getCapacity(); i++)
					if(this.get(i) ^ obj.get(i))
						return false;
				return true;
			}
			return false;
		}
		
		public static final class BooleanBuffer {
			
			boolean[] bools;
			
			public BooleanBuffer(int len)
			{
				this.bools = new boolean[len];
			}
			
			public BooleanBuffer(boolean ... b)
			{
				this.bools = b;
			}
			
			public boolean[] get()
			{
				return this.bools;
			}
			
			public void set(int i, boolean b)
			{
				try {
					this.bools[i] = b;
				} catch (java.lang.ArrayIndexOutOfBoundsException e) {
					this.overflow(i - bools.length);
					this.set(i, b);
				}
			}
			
			public boolean get(int i)
			{
				return this.bools[i];
			}
			
			public void overflow(int i)
			{
				boolean[] n = new boolean[this.bools.length + i];
				System.arraycopy(bools, 0, n, 0, bools.length);
				this.bools = n;
			}
			
			public int length()
			{
				return bools.length;
			}
			
		}
		
	}
	
	/**
	 * An ugly workaround to the lack of interface inner classes
	 * 
	 * @author Claire
	 * @param <Type>
	 */
	public static abstract class BasicBits<Type extends BasicBits<Type>> 
						   implements Bits<Type>
	{
		private final class BitIterator implements IIterator<Boolean>
		{
			private int pos = 0;
			private final int MAX = BasicBits.this.getCapacity();
			
			public boolean hasNext()
			{
				return pos < MAX;
			}

			public Boolean next()
			{
				return new Boolean(BasicBits.this.get(pos));
			}
			
			public void skip()
			{
				pos++;
			}
			
			public void skip(int amt)
			{
				this.pos += amt;
			}
			
		}
		
		public IIterator<Boolean> iterator()
		{
			return new BitIterator();
		}
	}
	
	/**
	 * Don't ask for full Big Endian support. I make methods here as I need them
	 * 
	 * @author Claire
	 */
	public static final class BigEndian
	{
		public static void bytesToInts(byte[] bytes, int start0, int[] ints, int start1, int len)
		{
			int roof = len + start1;
			while(start1 < roof)
			{
				ints[start1++] = ((((int) bytes[start0++] & 0xFF) << 24) |
								  (((int) bytes[start0++] & 0xFF) << 16) |
								  (((int) bytes[start0++] & 0xFF) << 8) |
								  (((int) bytes[start0++] & 0xFF)));
			}
		}
		
		public static void bytesToInts(byte[] bytes, int start0, int[] ints, int start1)
		{
			bytesToInts(bytes, start0, ints, start1, ints.length - start1);
		}
		
		public static void bytesToInts(byte[] bytes, int[] ints)
		{
			bytesToInts(bytes, 0, ints, 0, ints.length);
		}
		
		public static int[] bytesToInts(byte[] bytes)
		{
			int[] ints = new int[bytes.length >> 2];
			bytesToInts(bytes, ints);
			return ints;
		}
		
		public static void intsToBytes(int[] ints, int start0, byte[] bytes, int start1, int len)
		{
			int roof = len + start0;
			int reg;
			while(start0 < roof)
			{
				reg = ints[start0++];
				bytes[start1++] = (byte) (reg >>> 24);
				bytes[start1++] = (byte) (reg >>> 16);
				bytes[start1++] = (byte) (reg >>> 8);
				bytes[start1++] = (byte) reg;			
			}
		}
		
		public static void intsToBytes(int[] ints, int start0, byte[] bytes, int start1)
		{
			intsToBytes(ints, start0, bytes, start1, ints.length - start0);
		}
		
		public static void intsToBytes(int[] ints, byte[] bytes)
		{
			intsToBytes(ints, 0, bytes, 0, ints.length);
		}
		
		public static byte[] intsToBytes(int[] ints)
		{
			byte[] bytes = new byte[ints.length << 2];
			intsToBytes(ints, bytes);
			return bytes;
		}
		
		public static void longToBytes(long data, byte[] bytes, int start)
		{
			bytes[start++] = (byte) (data >>> 56);
			bytes[start++] = (byte) (data >>> 48);
			bytes[start++] = (byte) (data >>> 40);
			bytes[start++] = (byte) (data >>> 32);
			bytes[start++] = (byte) (data >>> 24);
			bytes[start++] = (byte) (data >>> 16);
			bytes[start++] = (byte) (data >>> 8);
			bytes[start  ] = (byte) data;
		}
		
		public static void longsToBytes(long[] longs, int start0, byte[] bytes, int start1, int len)
		{
			int roof = len + start0;
			long reg;
			while(start0 < roof)
			{
				reg = longs[start0++];
				bytes[start1++] = (byte) (reg >>> 56);
				bytes[start1++] = (byte) (reg >>> 48);
				bytes[start1++] = (byte) (reg >>> 40);
				bytes[start1++] = (byte) (reg >>> 32);
				bytes[start1++] = (byte) (reg >>> 24);
				bytes[start1++] = (byte) (reg >>> 16);
				bytes[start1++] = (byte) (reg >>> 8);
				bytes[start1++] = (byte) reg;	
			}
		}
		
		public static void longsToBytes(long[] longs, int start0, byte[] bytes, int start1)
		{
			longsToBytes(longs, start0, bytes, start1, longs.length - start0);
		}
		
		public static void longsToBytes(long[] longs, byte[] bytes)
		{
			longsToBytes(longs, 0, bytes, 0, longs.length);
		}
		
		public static byte[] longsToBytes(long[] longs)
		{
			byte[] bytes = new byte[longs.length << 3];
			longsToBytes(longs, bytes);
			return bytes;
		}
		
		public static long longFromBytes(byte[] bytes, int start)
		{
			return ((((long) bytes[start++] & 0xFF) << 56) |
					(((long) bytes[start++] & 0xFF) << 48) |
					(((long) bytes[start++] & 0xFF) << 40) |
					(((long) bytes[start++] & 0xFF) << 32) |
					(((long) bytes[start++] & 0xFF) << 24) |
					(((long) bytes[start++] & 0xFF) << 16) |
					(((long) bytes[start++] & 0xFF) <<  8) |
					(((long) bytes[start  ] & 0xFF)));
		}
		
		public static void bytesToLongs(byte[] bytes, int start0, long[] longs, int start1, int len)
		{
			long roof = len + start1;
			while(start1 < roof)
			{
				longs[start1++] = ((((long) bytes[start0++] & 0xFF) << 56) |
								   (((long) bytes[start0++] & 0xFF) << 48) |
								   (((long) bytes[start0++] & 0xFF) << 40) |
								   (((long) bytes[start0++] & 0xFF) << 32) |
								   (((long) bytes[start0++] & 0xFF) << 24) |
								   (((long) bytes[start0++] & 0xFF) << 16) |
								   (((long) bytes[start0++] & 0xFF) <<  8) |
								   (((long) bytes[start0++] & 0xFF)      ));
			}
		}
		
		public static void bytesToLongs(byte[] bytes, int start0, long[] longs, int start1)
		{
			bytesToLongs(bytes, start0, longs, start1, longs.length - start1);
		}
		
		public static void bytesToLongs(byte[] bytes, long[] longs)
		{
			bytesToLongs(bytes, 0, longs, 0, longs.length);
		}
		
		public static long[] bytesToLongs(byte[] bytes)
		{
			long[] longs = new long[bytes.length >> 3];
			bytesToLongs(bytes, longs);
			return longs;
		}
		
		public static void shortToBytes(short data, byte[] bytes, int start)
		{
			bytes[start++] = (byte) (data >>> 8);
			bytes[start++] = (byte)  data;
		}
		
		public static void intToBytes(int data, byte[] bytes, int start)
		{
			bytes[start++] = (byte) (data >>> 24);
			bytes[start++] = (byte) (data >>> 16);
			bytes[start++] = (byte) (data >>> 8);		
			bytes[start++] = (byte)  data;		
		}
	}
	
}

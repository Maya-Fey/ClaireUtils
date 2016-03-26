package claire.util.memory.buffer;

import claire.util.memory.Bits;

/**
 * This class accepts bytes and produces integers of variable length.
 */
public class PrimitiveAggregator {
	
	private final byte[] buffer;
	private int pos = 0;
	
	/**
	 * Constructs a new aggregator with a newly-allocated 8-byte array. 
	 */
	public PrimitiveAggregator()
	{
		this.buffer = new byte[8];
	}
	
	/**
	 * Constructs an aggregator with an array of you input.
	 * The array MUST be at least 8 bytes in size.
	 */
	public PrimitiveAggregator(final byte[] buffer)
	{
		this.buffer = buffer;
	}
	
	/**
	 * Returns the internal array used by this aggregator.
	 * <br><br>
	 * Returns: a byte array of at least 8 bytes. May be more if user-defined. 
	 */
	public byte[] getBuffer()
	{
		return this.buffer;
	}
	
	/**
	 * Adds a single byte to the array and increments the position pointer.
	 * <br>
	 * Expects: 
	 * <ul>
	 *   <li>One byte of any value</li>
	 * </ul>
	 * <br>
	 * Returns: nothing 
	 */
	public void addByte(final byte b)
	{
		buffer[pos++] = b;
	}
	
	/**
	 * Resets the position pointer to index zero.
	 */
	public void reset()
	{
		pos = 0;
	}
	
	/**
	 * Adds a variable amount of bytes to the internal array.<br>
	 * <b>DOES NOT AFFECT THE POSITION POINTER</b>
	 * <br>
	 * Expects: 
	 * <ul>
	 *   <li>An non-null array of bytes with a maxmium length of 8 bytes</li>
	 * </ul>
	 * <br>
	 * If the array contains over 8 bytes, an ArrayIndexOutOfBounds exception will be thrown.
	 * <br><br>
	 * Returns: nothing 
	 */
	public void addBytes(final byte[] bytes)
	{
		System.arraycopy(bytes, 0, buffer, pos, bytes.length);
	}
	
	
	/**
	 * Adds a variable amount of bytes to the internal array, accepts parameters allowing for mid-array depositing<br>
	 * <b>DOES NOT AFFECT THE POSITION POINTER</b>
	 * <br>
	 * Expects: 
	 * <ul>
	 *   <li>An non-null array of bytes</li>
	 *   <li>A start index indicating where to start copying</li>
	 *   <li>A length indicator how many bytes to copy</li>
	 * </ul>
	 * <br>
	 * If length is over 8, an ArrayIndexOutOfBounds exception will be thrown.<br>
	 * A negative start or length value results in undefined behavior.<br>
	 * A start value greater then the length of the array will result in an ArrayIndexOutOfBoundsException
	 * <br><br>
	 * Returns: nothing 
	 */
	public void addBytes(final byte[] bytes, final int start, final int len)
	{
		System.arraycopy(bytes, start, buffer, pos, len);
	}
	
	/**
	 * Returns a 2-byte short integer from the bytes stored in the array. 
	 */
	public short getShort()
	{
		return Bits.shortFromBytes(buffer, 0);
	}
	
	/**
	 * Returns a 2-byte character from the bytes stored in the array. 
	 */
	public char getChar()
	{
		return Bits.charFromBytes(buffer, 0);
	}

	/**
	 * Returns a 4-byte =integer from the bytes stored in the array. 
	 */
	public int getInt()
	{
		return Bits.intFromBytes(buffer, 0);
	}

	/**
	 * Returns a 8-byte long integer from the bytes stored in the array. 
	 */
	public long getLong()
	{
		return Bits.longFromBytes(buffer, 0);
	}

}

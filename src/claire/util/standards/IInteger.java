package claire.util.standards;

import java.math.BigInteger;
import java.util.Arrays;

import claire.util.crypto.rng.RandUtils;
import claire.util.encoding.CString;
import claire.util.encoding.EncodingUtil;
import claire.util.encoding.PartialString;
import claire.util.logging.Log;
import claire.util.math.IntegerFactory;
import claire.util.math.MathHelper;
import claire.util.memory.Bits;

/**
 * Note: This interface does NOT define:
 * <ul>
 * 	<li>Overflow behavior</li>
 * 	<li>Signed modular behavior</li>
 * 	<li>Conversion behavior</li>
 * 	<li>Integer ordering</li>
 * 	<li>Operations with other implementations</li>
 * </ul>
 * Use at your own risk, and be careful of accepting a IInteger
 * of any type before considering the possibility of undefined 
 * behavior. Implementing classes of this interface are not required
 * to give all internal state data given a call to getArr() only an
 * integer representation (in <b>undefined</b> order) of the integer.
 * You have been warned.
 * <br>
 * @author Claire
 * @param <Type> The implementation used
 */
public interface IInteger<Type extends IInteger<Type>> 
	   extends CObject<Type> {
	
	default Type add(IInteger<?> i)
	{
		Type n = this.createDeepClone();
		n.p_add(i);
		return n;
	}
	default Type subtract(IInteger<?> i)
	{
		Type n = this.createDeepClone();
		n.p_subtract(i);
		return n;
	}
	default Type multiply(IInteger<?> i)
	{
		Type n = this.createDeepClone();
		n.p_multiply(i);
		return n;
	}
	default Type divide(IInteger<?> i)
	{
		Type n = this.createDeepClone();
		n.p_divide(i);
		return n;
	}
	default Type modulo(IInteger<?> i)
	{
		Type n = this.createDeepClone();
		n.p_modulo(i);
		return n;
	}
	default Type exponent(IInteger<?> i)
	{
		Type n = this.createDeepClone();
		n.p_exponent(i);
		return n;
	}
	
	default Type add(IInteger<?> i, Type n)
	{
		n.setTo(this);
		n.p_add(i);
		return n;
	}
	default Type subtract(IInteger<?> i, Type n)
	{
		n.setTo(this);
		n.p_subtract(i);
		return n;
	}
	default Type multiply(IInteger<?> i, Type n)
	{
		n.setTo(this);
		n.p_multiply(i);
		return n;
	}
	default Type divide(IInteger<?> i, Type n)
	{
		n.setTo(this);
		n.p_divide(i);
		return n;
	}
	default Type modulo(IInteger<?> i, Type n)
	{
		n.setTo(this);
		n.p_modulo(i);
		return n;
	}
	default Type exponent(IInteger<?> i, Type n)
	{
		n.setTo(this);
		n.p_exponent(i);
		return n;
	}
	
	void p_add(IInteger<?> i);
	void p_subtract(IInteger<?> i);
	void p_multiply(IInteger<?> i);
	void p_divide(IInteger<?> i);
	void p_modulo(IInteger<?> i);
	IInteger<?> p_divmod(IInteger<?> i);
	
	default void p_exponent(IInteger<?> i)
	{
		if(!i.isNonZero()) {
			this.setTo(1);
			return;
		} else if(i.isEqualTo(1))
			return;
		IInteger<?> n = i.createDeepClone();
		Type o = this.createDeepClone();
		o.setTo(1);
		while(n.isGreaterThan(1))
		{
			if(n.isOdd()) {
				o.p_multiply(this);
				this.p_square();
				n.decrement();
				n.p_divide(2);
			} else {
				this.p_square();
				n.p_divide(2);
			}
		}
		this.p_multiply(o);
	}
	
	boolean isSigned();
	boolean isNegative();
	
	default boolean isGreaterThan(IInteger<?> i)
	{
		return MathHelper.absolute_compare(this.getArr(), i.getArr()) == 1;
	}
	
	default boolean isLesserThan(IInteger<?> i)
	{
		return MathHelper.absolute_compare(this.getArr(), i.getArr()) == -1;
	}
	
	default boolean isEqualTo(IInteger<?> i)
	{
		return MathHelper.absolute_compare(this.getArr(), i.getArr()) == 0;
	}

	default boolean doesNotEqual(IInteger<?> i)
	{
		return MathHelper.absolute_compare(this.getArr(), i.getArr()) != 0;
	}
	
	default boolean isGreaterOrEqualTo(IInteger<?> i)
	{
		return MathHelper.absolute_compare(this.getArr(), i.getArr()) >= 0;
	}
	
	default boolean isLesserOrEqualTo(IInteger<?> i)
	{
		return MathHelper.absolute_compare(this.getArr(), i.getArr()) <= 0;
	}
		
	void increment();
	void decrement();
	
	int[] getInts();
	int[] getArr();
	void upsize(int size);
	Type getLarge(int size);
	
	byte toInt8();
	short toInt16();
	int toInt32();
	long toInt64();
	
	default Type add(int i)
	{
		Type n = this.createDeepClone();
		n.p_add(i);
		return n;
	}
	default Type subtract(int i)
	{
		Type n = this.createDeepClone();
		n.p_subtract(i);
		return n;
	}
	default Type multiply(int i)
	{
		Type n = this.createDeepClone();
		n.p_multiply(i);
		return n;
	}
	default Type divide(int i)
	{
		Type n = this.createDeepClone();
		n.p_divide(i);
		return n;
	}
	default Type modulo(int i)
	{
		Type n = this.createDeepClone();
		n.p_modulo(i);
		return n;
	}
	default Type exponent(int i)
	{
		Type n = this.createDeepClone();
		n.p_exponent(i);
		return n;
	}
	
	default Type add(int i, Type n)
	{
		n.setTo(this);
		n.p_add(i);
		return n;
	}
	default Type subtract(int i, Type n)
	{
		n.setTo(this);
		n.p_subtract(i);
		return n;
	}
	default Type multiply(int i, Type n)
	{
		n.setTo(this);
		n.p_multiply(i);
		return n;
	}
	default Type divide(int i, Type n)
	{
		n.setTo(this);
		n.p_divide(i);
		return n;
	}
	default Type modulo(int i, Type n)
	{
		n.setTo(this);
		n.p_modulo(i);
		return n;
	}
	default Type exponent(int i, Type n)
	{
		n.setTo(this);
		n.p_exponent(i);
		return n;
	}

	void p_add(int i);
	void p_subtract(int i);
	void p_multiply(int i);
	void p_divide(int i);
	void p_modulo(int i);
	int  p_divmod(int i);
	
	default void p_exponent(long exponent)
	{
		if(exponent == 0) {
			this.setTo(1);
			return;
		} 
		if(exponent == 1)
			return;
		Type o = this.createDeepClone();
		o.setTo(1);
		while(exponent > 1)
		{
			if((exponent & 1) == 1) 
				o.p_multiply(this);
			this.p_square();
			exponent >>>= 1;
		}
		this.p_multiply(o);
	}
	
	boolean isGreaterThan(int i);
	boolean isLesserThan(int i);
	boolean isEqualTo(int i);
	boolean doesNotEqual(int i);
	boolean isGreaterOrEqualTo(int i);
	boolean isLesserOrEqualTo(int i);
	boolean isNonZero();
	
	boolean bitAt(int pos);
	void setBit(int pos, boolean bit);
	int getBits();
	int getIntLen();
	
	void setNegative(boolean b);
	void invertSign();
	void setTo(long l);
	void setTo(IInteger<?> other);
	
	void p_trim();
	
	default Type trim()
	{
		Type t = this.createDeepClone();
		t.p_trim();
		return t;
	}
	
	void setArr(int[] arr);
	
	IntegerFactory<Type> iFactory();
	
	default void truncate(int bits)
	{
		MathHelper.truncate(this.getArr(), bits);
	}
	
	default void setToAbs(IInteger<?> i)
	{
		this.setArr(i.getArr());
	}
	
	void zero();
	
	boolean isOdd();
	
	default void p_square()
	{
		Type self = this.createDeepClone();
		this.p_multiply(self);
	}
	
	default Type square()
	{
		Type n = this.createDeepClone();
		n.p_square();
		return n;
	}
	
	default byte[] toBytes()
	{
		return Bits.intsToBytes(this.getArr());
	}

	default char[] toChars()
	{
		Type n = this.createDeepClone();
		int[] arr = n.getArr();
		int len = arr.length - Bits.countEndingZeroes(arr);
		if(len == 0)
			return new char[] { '0' };
		boolean negative = this.isNegative();
		char[] characters = negative ? new char[Bits.APPROXIMATEB10DIDGITS(len) + 1] : new char[Bits.APPROXIMATEB10DIDGITS(len)];
		int pos = 0;
		for(; n.isNonZero(); pos++)
		{
			int remainder = n.p_divmod(10);
			characters[pos] = (char)(48 + remainder);
		}
		if(n.isNegative())
			characters[pos++] = '-';
		EncodingUtil.REVERSE(characters);
		return EncodingUtil.ELIMINATE_PREPENDING_CHARS(characters);
	}
	
	default CString toCString()
	{
		return new CString(this.toChars());
	}
	
	default void setTo(String s)
	{
		this.setTo(s.toCharArray());
	}
	
	default void setTo(CString s)
	{
		this.setTo(s.array());
	}
	
	default void setTo(PartialString s)
	{
		this.setTo(s.array(), s.getOffset(), s.getLength());
	}
	
	default void setTo(char[] chars)
	{
		this.zero();
		int i = 0;
		boolean negative = false;
		if(chars[0] == '-') {
			i++;
			negative = true;
		}
		this.p_add(chars[i] - 48);
		i++;
		for(; i < chars.length; i++)
		{
			this.p_multiply(10);
			this.p_add(chars[i] - 48);
		}
		this.setNegative(negative);
	}
	
	default void setTo(char[] chars, int start, int len)
	{
		this.zero();
		int i = start;
		boolean negative = false;
		if(chars[i] == '-') {
			i++;
			len--;
			negative = true;
		}
		this.p_add(chars[i] - 48);
		while(len-- > 0)
		{
			this.p_multiply(10);
			this.p_add(chars[i] - 48);
		}
		this.setNegative(negative);
	}
	
	static <Type extends IInteger<?>> void make(Type t, char[] chars, int start, int len)
	{
		boolean negative = false;
		if(chars[0] == '-') {
			start++;
			negative = true;
		}
		t.p_add(chars[start] - 48);
		start++;
		for(; start < len; start++)
		{
			t.p_multiply(10);
			t.p_add(chars[start] - 48);
		}
		if(negative)
			t.invertSign();
	}
	
	static <Type extends IInteger<?>> void make(Type t, char[] chars)
	{
		int i = 0;
		boolean negative = false;
		if(chars[0] == '-') {
			i++;
			negative = true;
		}
		t.p_add(chars[i] - 48);
		i++;
		for(; i < chars.length; i++)
		{
			t.p_multiply(10);
			t.p_add(chars[i] - 48);
		}
		if(negative)
			t.invertSign();
	}
	
	public static <Int extends IInteger<Int>> int verifyConstruction(IntegerFactory<Int> fac)
	{
		int er = 0;
		try {
			char[] chars = new char[100];
			Arrays.fill(chars, '0');
			for(int i = 0; i < 100; i++)
				chars[i] += RandUtils.dprng.nextIntFast(10);
			while(chars[0] == '0')
				chars[0] += RandUtils.dprng.nextIntFast(10);
			
			Int i = fac.construct(chars, 32);
			if(!i.toString().equals(new String(chars))) {
				System.out.println(i);
				System.out.println(chars);
				Log.err.println("String construction non consistent");
				er++;
			}
			return er;
		} catch(Exception e) {
			Log.err.println("Exception encountered while testing construction of integers from " + fac.getClass().getSimpleName() + ": " + e.getMessage());
			e.printStackTrace();
			return er + 1;
		}
	}
	
	public static <Int extends IInteger<Int>> int verifyAddition(IntegerFactory<Int> fac)
	{
		int er = 0;
		try {
			int[] ints1 = new int[32];
			RandUtils.fillArr(ints1, 0, 16);
			Int i1 = fac.construct(ints1);
			int[] ints2 = new int[32];
			RandUtils.fillArr(ints2, 0, 16);
			Int i2 = fac.construct(ints2);
			BigInteger b1 = new BigInteger(i1.toString());
			BigInteger b2 = new BigInteger(i2.toString());
			i1.p_add(i2);
			b1 = b1.add(b2);
			if(!i1.toString().equals(b1.toString())){
				Log.err.println("Addition failed");
				er++;
			}
			//Carrying
			Arrays.fill(ints1, 16, 32, 0);
			Arrays.fill(ints1, 0, 16, -1);
			b1 = new BigInteger(i1.toString());
			i1.p_add(i2);
			b1 = b1.add(b2);
			if(!i1.toString().equals(b1.toString())) {
				Log.err.println("Addition failed with carry");
				er++;
			}
			return er;
		} catch(Exception e) {
			Log.err.println("Exception encountered while testing addition of integers from " + fac.getClass().getSimpleName() + ": " + e.getMessage());
			e.printStackTrace();
			return er + 1;
		}
	}
	
	public static <Int extends IInteger<Int>> int verifySubtraction(IntegerFactory<Int> fac)
	{
		int er = 0;
		try {
			int[] ints1 = new int[32];
			RandUtils.fillArr(ints1, 0, 16);
			Int i1 = fac.construct(ints1);
			int[] ints2 = new int[32];
			RandUtils.fillArr(ints2, 0, 15);
			Int i2 = fac.construct(ints2);
			BigInteger b1 = new BigInteger(i1.toString());
			BigInteger b2 = new BigInteger(i2.toString());
			i1.p_subtract(i2);
			b1 = b1.subtract(b2);
			if(!i1.toString().equals(b1.toString())){
				Log.err.println("Subtraction failed");
				er++;
			}
			//Carrying
			RandUtils.fillArr(ints1, 0, 16);
			RandUtils.fillArr(ints2, 0, 15);
			Arrays.fill(ints2, 0, 15, -1);
			b1 = new BigInteger(i1.toString());
			b2 = new BigInteger(i2.toString());
			i1.p_subtract(i2);
			b1 = b1.subtract(b2);
			if(!i1.toString().equals(b1.toString())) {
				Log.err.println("Subtraction failed with carry");
				er++;
			}
			
			RandUtils.fillArr(ints1, 0, 16);
			RandUtils.fillArr(ints2, 0, 15);
			Arrays.fill(ints1, 15, 32, 0);
			Arrays.fill(ints1, 6, 15, -1);
			b1 = new BigInteger(i1.toString());
			b2 = new BigInteger(i2.toString());
			i1.p_subtract(i2);
			b1 = b1.subtract(b2);
			if(!i1.toString().equals(b1.toString())) {
				Log.err.println("Subtraction failed with carry");
				er++;
			}
			return er;
		} catch(Exception e) {
			Log.err.println("Exception encountered while testing subtraction of integers from " + fac.getClass().getSimpleName() + ": " + e.getMessage());
			e.printStackTrace();
			return er + 1;
		}
	}
	
	public static <Int extends IInteger<Int>> int verifyMultiplication(IntegerFactory<Int> fac)
	{
		int er = 0;
		try {
			int[] ints1 = new int[32];
			RandUtils.fillArr(ints1, 0, 16);
			Int i1 = fac.construct(ints1);
			int[] ints2 = new int[32];
			RandUtils.fillArr(ints2, 0, 16);
			Int i2 = fac.construct(ints2);
			BigInteger b1 = new BigInteger(i1.toString());
			BigInteger b2 = new BigInteger(i2.toString());
			i1.p_multiply(i2);
			b1 = b1.multiply(b2);
			if(!i1.toString().equals(b1.toString())){
				Log.err.println("Multiplication failed");
				er++;
			}
			return er;
		} catch(Exception e) {
			Log.err.println("Exception encountered while testing multiplication of integers from " + fac.getClass().getSimpleName() + ": " + e.getMessage());
			e.printStackTrace();
			return er + 1;
		}
	}
	
	public static <Int extends IInteger<Int>> int verifySquaring(IntegerFactory<Int> fac)
	{
		int er = 0;
		try {
			int[] ints1 = new int[32];
			RandUtils.fillArr(ints1, 0, 16);
			Int i1 = fac.construct(ints1);
			BigInteger b1 = new BigInteger(i1.toString());
			i1.p_square();
			b1 = b1.pow(2);
			if(!i1.toString().equals(b1.toString())){
				Log.err.println("Squaring failed");
				er++;
			}
			return er;
		} catch(Exception e) {
			Log.err.println("Exception encountered while testing squaring of integers from " + fac.getClass().getSimpleName() + ": " + e.getMessage());
			e.printStackTrace();
			return er + 1;
		}
	}

	public static <Int extends IInteger<Int>> int verifyDivision(IntegerFactory<Int> fac)
	{
		int er = 0;
		try {
			int[] ints1 = new int[32];
			RandUtils.fillArr(ints1, 0, 24);
			Int i1 = fac.construct(ints1);
			int[] ints2 = new int[32];
			RandUtils.fillArr(ints2, 0, 6);
			Int i2 = fac.construct(ints2);
			BigInteger b1 = new BigInteger(i1.toString());
			BigInteger b2 = new BigInteger(i2.toString());
			i1.p_divide(i2);
			b1 = b1.divide(b2);
			if(!i1.toString().equals(b1.toString())){
				Log.err.println("Division failed");
				er++;
			}
			return er;
		} catch(Exception e) {
			Log.err.println("Exception encountered while testing division of integers from " + fac.getClass().getSimpleName() + ": " + e.getMessage());
			e.printStackTrace();
			return er + 1;
		}
	}
	
	public static <Int extends IInteger<Int>> int verifyModulus(IntegerFactory<Int> fac)
	{
		int er = 0;
		try {
			int[] ints1 = new int[32];
			RandUtils.fillArr(ints1, 0, 24);
			Int i1 = fac.construct(ints1);
			int[] ints2 = new int[32];
			RandUtils.fillArr(ints2, 0, 6);
			Int i2 = fac.construct(ints2);
			BigInteger b1 = new BigInteger(i1.toString());
			BigInteger b2 = new BigInteger(i2.toString());
			i1.p_modulo(i2);
			b1 = b1.mod(b2);
			if(!i1.toString().equals(b1.toString())){
				Log.err.println("Modulus failed");
				er++;
			}
			return er;
		} catch(Exception e) {
			Log.err.println("Exception encountered while testing modulus of integers from " + fac.getClass().getSimpleName() + ": " + e.getMessage());
			e.printStackTrace();
			return er + 1;
		}
	}
	
	public static <Int extends IInteger<Int>> int verifyAddSub(IntegerFactory<Int> fac)
	{
		int er = 0;
		try {
			int[] ints1 = new int[32];
			RandUtils.fillArr(ints1, 0, 18);
			Int i1 = fac.construct(ints1);
			int[] ints2 = new int[32];
			RandUtils.fillArr(ints2, 0, 16);
			Int i2 = fac.construct(ints2);
			Int conf = i1.createDeepClone();
			i1.p_add(i2);
			i1.p_subtract(i2);
			if(!i1.equals(conf)) {
				Log.err.println("Addition followed by subtraction did not yield the same value");
				er++;
			}
			return er;
		} catch(Exception e) {
			Log.err.println("Exception encountered while testing addition/subtraction of integers from " + fac.getClass().getSimpleName() + ": " + e.getMessage());
			e.printStackTrace();
			return er + 1;
		}
	}
	
	public static <Int extends IInteger<Int>> int verifyMulDiv(IntegerFactory<Int> fac)
	{
		int er = 0;
		try {
			int[] ints1 = new int[32];
			RandUtils.fillArr(ints1, 0, 16);
			Int i1 = fac.construct(ints1);
			int[] ints2 = new int[32];
			RandUtils.fillArr(ints2, 0, 16);
			Int i2 = fac.construct(ints2);
			Int conf = i1.createDeepClone();
			i1.p_multiply(i2);
			i1.p_divide(i2);
			if(!i1.equals(conf)) {
				Log.err.println("Multiplication followed by Division did not yield the same value");
				er++;
			}
			return er;
		} catch(Exception e) {
			Log.err.println("Exception encountered while testing multiplication/division of integers from " + fac.getClass().getSimpleName() + ": " + e.getMessage());
			e.printStackTrace();
			return er + 1;
		}
	}
	
}

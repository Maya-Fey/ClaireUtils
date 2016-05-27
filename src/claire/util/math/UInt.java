package claire.util.math;

import java.io.IOException;
import java.util.Arrays;

import claire.util.encoding.CString;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IInteger;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class UInt 
	   extends StdUInt<UInt> {
	
	private int[] val;
	private int[] ref;
	private int length;
	
	public UInt(int i)
	{
		this.length = i;
		val = new int[this.length];
	}
	
	public UInt(int[] i)
	{
		val = i;
		this.length = i.length;
	}

	public UInt(String string, int len) 
	{
		this.length = len;
		this.val = new int[len];
		IInteger.make(this, string.toCharArray());
	}

	public UInt(CString string, int len) 
	{
		this.length = len;
		this.val = new int[len];
		IInteger.make(this, string.array());
	}
	
	public UInt(char[] chars, int start, int len, int len2)
	{
		this.length = len2;
		this.val = new int[len2];
		IInteger.make(this, chars, start, len);
	}
	
	public UInt createDeepClone()
	{
		int[] n = new int[this.length];
		System.arraycopy(val, 0, n, 0, this.length);
		return new UInt(n);
	}
	
	public UInt(UInt u, int nt)
	{
		int[] ints = new int[nt];
		System.arraycopy(u.getArr(), 0, ints, 0, u.getIntLen());
		val = ints;
		this.length = nt;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.UINT;
	}
	
	protected void create()
	{
		this.val = new int[this.length];
	}
	
	protected void p_add(int[] ints)
	{
		int len = MathHelper.getRealLength(ints);
		if(len > this.length) throw new java.lang.IllegalArgumentException();
		int j = 0;
		long carry = 0;
		while(j < len)
		{
			carry += (val[j] & 0xFFFFFFFFL) + (ints[j] & 0xFFFFFFFFL);
			val[j++] = (int) carry;
			carry >>>= 32;
		}
		val[j] += carry;
		if(carry != 0)
			while(val[j] == 0 && ++j < length)
				val[j]++;
	}
	
	protected void p_subtract(int[] ints)
	{
		int len = MathHelper.getRealLength(ints);
		if(len > this.length) throw new java.lang.IllegalArgumentException();
		int carry = 0;
		int j = 0;
		while(j < len)
		{
			int i2 = ints[j] + carry;
			if(i2 != 0)
			{
				int i1 = val[j];
				i2 = i1 - i2;
				carry = Bits.u_greaterThan(i2, i1) ? 1 : 0; 
				val[j] = i2;
			} else
				carry = 1;
			j++;
		}
		val[j] -= carry;
		if(carry > 0)
			while(val[j] == -1 && ++j < length)
				val[j]--;
	}
	
	protected void p_multiply(int[] ints)
	{
		int len = MathHelper.getRealLength(ints);
		int tlen = MathHelper.getRealLength(val);
		int max = length;
		if(len > this.length) throw new java.lang.IllegalArgumentException();
		if(ref == null)
			ref = new int[length];
		final int[] ref = this.ref;
		System.arraycopy(val, 0, ref, 0, tlen);
		if(MathHelper.mul1(val, tlen, ints[0]) > 0) {
			return;
		}
		for(int j = 1; j < len; j++)
		{
			long borrow = 0;
			long jw = ints[j] & 0xFFFFFFFFL;
			int k = 0;
			int t = j;
			for(; t < max; k++, t++)
			{
				if(k < tlen) {
					borrow += (jw * (ref[k] & 0xFFFFFFFFL)) + (val[t] & 0xFFFFFFFFL);
					val[t] = (int) borrow;
					borrow >>>= 32;
				} else {
					if(borrow != 0) {
						val[t] = (int) borrow;
						borrow = 0;
					} else 
						break;
				}
			}
			if(borrow != 0) {
				return;
			}
		}
	}
	
	protected  void p_divide(int[] ints)
	{
		int dlen = MathHelper.getRealLength(ints);
		int vlen = MathHelper.getRealLength(val);
		if(dlen > vlen) {
			this.zero();
		}
		if(dlen < 2) {
			divideOneWord(ints[0]);
			return;
		} 
		fastdiv(ints, dlen, vlen);
	}
	
	protected void p_modulo(int[] ints)
	{
		int dlen = MathHelper.getRealLength(ints);
		if(dlen < 2) {
			moduloOneWord(ints[0]);
			return;
		}
		final int[] orig = val;
		int len = MathHelper.getRealLength(orig);
		fastmod(ints, dlen, len);
		//Arrays.fill(orig, dlen, this.length, 0);		
	}
	
	private final void fastdiv(final int[] divisor, final int dlen, int vlen)
	{
		final int[] n;
		final int[] divd;
		
		int vit,
			q,
			r,
			prev;
		
		final int shift = 31 - Bits.getMSB(divisor[dlen - 1]);
		boolean narr = false;
		if((Bits.getMSB(val[vlen - 1]) + shift) > 31)
		{
			if(vlen == this.length) 
				narr ^= true;
			vlen++;
		}
		if(narr) {
			if(ref == null || ref.length < vlen + 1)
				ref = new int[length + 1];
			divd = ref;
			System.arraycopy(val, 0, divd, 0, vlen - 1);
			n = val;
		} else {
			divd = val;
			if(ref == null || ref.length < vlen + 1)
				ref = new int[length + 1];
			n = ref;
		}
		
		if(shift > 0) {
			MathHelper.shiftArrayRightBE(divisor, shift);
			MathHelper.shiftArrayRightBE(divd, shift);
		}
	
		final long divs = divisor[dlen - 1] & 0xFFFFFFFFL;
		final long divs2 = divisor[dlen - 2] & 0xFFFFFFFFL;

		vit = vlen - 1;
		prev = 0;
		for(int i = vlen - dlen; i >= 0; i--)
		{
			
			boolean verify = true;
			if(prev == divs) {
				q = 0xFFFFFFFF;
				r = divd[vit] + prev;
				verify ^= Bits.u_greaterThan(prev, r);
			} else {
				long cur = (((prev & 0xFFFFFFFFL) << 32) | (divd[vit] & 0xFFFFFFFFL));
				if(cur >= 0) {
        			q = (int) (cur / divs);
        			r = (int) (cur % divs);
        		} else {
        			long q2, r2;
                	q2 = (cur >>> 1) / (divs >>> 1);
                	r2 = cur - q2 * divs;
                	while (r2 < 0) {
                    	r2 += divs;
                    	q2--;
                	}
                	while (r2 >= divs) {
                    	r2 -= divs;
                    	q2++;
                	}            	
                	q = (int) q2;
                	r = (int) r2;
        		}
        	}
			
			
			if(q == 0) {
				prev = divd[vit--];
				continue;
			}
			
			if(verify) {
				long next;
				if(vit == 0)
					next = 0;
				else 
					next = divd[vit - 1] & 0xFFFFFFFFL;
				long actual = ((r & 0xFFFFFFFFL) << 32) | next;
				long ex = divs2 * (q & 0xFFFFFFFFL);
				if(Bits.u_greaterThan(ex, actual)) {
					q--;
					r = (int) ((r & 0xFFFFFFFFL) + divs);
					if((r & 0xFFFFFFFFL) >= divs) {
						ex -= divs2;
						actual = ((r & 0xFFFFFFFFL) << 32) | next;
						if(Bits.u_greaterThan(ex, actual))
							q--;
					}
				}
			} 
			int carry = 0;
			long mul = q & 0xFFFFFFFFL;
			int pos = i;
			for(int j = 0; j < dlen; j++) {	
				long ans = ((divisor[j] & 0xFFFFFFFFL) * mul);
				int low = ((int) ans) + carry;
				if(Bits.u_greaterThan(carry, low))
					carry = (int) ((ans >>> 32) + 1);
				else
					carry = (int) (ans >>> 32);
				low = divd[pos] - low;
				if(Bits.u_greaterThan(low, divd[pos]))
					carry++;
				divd[pos++] = low;
			}
			
			if(Bits.u_greaterThan(carry, prev)) {
				q--;
				long c2 = 0;
				pos = i - 1;
				for(int j = 0; j < dlen; j++) {
					if(pos >= 0) {
					c2 += (divd[pos] & 0xFFFFFFFFL) + (divisor[j] & 0xFFFFFFFFL);
					divd[pos++] = (int) c2;
					c2 >>>= 32;
					} else
						pos++;
				}
			}
			n[i] = q;
			prev = divd[vit--];
		}
		MathHelper.shiftArrayLeftBE(divisor, shift);
		val = n;
		ref = divd;
	}
	
	private final void fastmod(final int[] divisor, final int dlen, int vlen)
	{
		final int[] divd;
		
		int vit,
			q,
			r,
			prev;
		
		final int shift = 31 - Bits.getMSB(divisor[dlen - 1]);
		boolean narr = false;
		if((Bits.getMSB(val[vlen - 1]) + shift) > 31)
		{
			if(vlen == this.length) 
				narr ^= true;
			vlen++;
		}
		if(narr) {
			if(ref == null || ref.length < vlen + 1)
				ref = new int[length + 1];
			divd = ref;
			System.arraycopy(val, 0, divd, 0, vlen - 1);
		} else 
			divd = val;
		
		if(shift > 0) {
			MathHelper.shiftArrayRightBE(divisor, shift);
			MathHelper.shiftArrayRightBE(divd, shift);
		}
	
		final long divs = divisor[dlen - 1] & 0xFFFFFFFFL;
		final long divs2 = divisor[dlen - 2] & 0xFFFFFFFFL;

		vit = vlen - 1;
		prev = 0;
		for(int i = vlen - dlen; i >= 0; i--)
		{
			
			boolean verify = true;
			if(prev == divs) {
				q = 0xFFFFFFFF;
				r = divd[vit] + prev;
				verify ^= Bits.u_greaterThan(prev, r);
			} else {
				long cur = (((prev & 0xFFFFFFFFL) << 32) | (divd[vit] & 0xFFFFFFFFL));
				if(cur >= 0) {
        			q = (int) (cur / divs);
        			r = (int) (cur % divs);
        		} else {
        			long q2, r2;
                	q2 = (cur >>> 1) / (divs >>> 1);
                	r2 = cur - q2 * divs;
                	while (r2 < 0) {
                    	r2 += divs;
                    	q2--;
                	}
                	while (r2 >= divs) {
                    	r2 -= divs;
                    	q2++;
                	}            	
                	q = (int) q2;
                	r = (int) r2;
        		}
        	}
			
			
			if(q == 0) {
				prev = divd[vit--];
				continue;
			}
			
			if(verify) {
				long next;
				if(vit == 0)
					next = 0;
				else 
					next = divd[vit - 1] & 0xFFFFFFFFL;
				long actual = ((r & 0xFFFFFFFFL) << 32) | next;
				long ex = divs2 * (q & 0xFFFFFFFFL);
				if(Bits.u_greaterThan(ex, actual)) {
					q--;
					r = (int) ((r & 0xFFFFFFFFL) + divs);
					if((r & 0xFFFFFFFFL) >= divs) {
						ex -= divs2;
						actual = ((r & 0xFFFFFFFFL) << 32) | next;
						if(Bits.u_greaterThan(ex, actual))
							q--;
					}
				}
			} 
			int carry = 0;
			long mul = q & 0xFFFFFFFFL;
			int pos = i;
			for(int j = 0; j < dlen; j++) {	
				long ans = ((divisor[j] & 0xFFFFFFFFL) * mul);
				int low = ((int) ans) + carry;
				if(Bits.u_greaterThan(carry, low))
					carry = (int) ((ans >>> 32) + 1);
				else
					carry = (int) (ans >>> 32);
				low = divd[pos] - low;
				if(Bits.u_greaterThan(low, divd[pos]))
					carry++;
				divd[pos++] = low;
			}
			if(pos != vlen)
				divd[pos] = 0;
			
			if(Bits.u_greaterThan(carry, prev)) {
				long c2 = 0;
				pos = i;
				for(int j = 0; j < dlen; j++) {
					if(pos >= 0) {
						c2 += (divd[pos] & 0xFFFFFFFFL) + (divisor[j] & 0xFFFFFFFFL);
						divd[pos++] = (int) c2;
						c2 >>>= 32;
					} else
						pos++;
				}
				//divd[pos++] += (int) c2;
			}
			prev = divd[vit--];
		}
		MathHelper.shiftArrayLeftBE(divisor, shift);
		MathHelper.shiftArrayLeftBE(divd, shift);
		if(narr) 
			System.arraycopy(divd, 0, val, 0, vlen - 1);
	}
	
	private final void divideOneWord(int divs) 
	{
		if(divs == 1) {
			return;
		}
        final long divsLong = divs & 0xFFFFFFFFL;
        if(ref == null)
			ref = new int[length];
        final int[] n = ref;
        final int vlen = MathHelper.getRealLength(val);
        Arrays.fill(n, vlen, n.length, 0);
        
        if (vlen == 1) {
        	n[0] = (int) ((val[0] & 0xFFFFFFFFL) / divsLong);
        	val = n;
            return;
        }
        
        long cur = 0;
        int i = vlen;
        while(--i >= 0)
        {
        	cur <<= 32;
        	cur |= val[i] & 0xFFFFFFFFL;
        	if(cur >= 0) {
        		n[i] = (int) (cur / divsLong);
        		cur = (int) (cur % divsLong);
        	} else {
                long q = (cur >>> 1) / (divsLong >>> 1);
                long r = cur - q * divsLong;
                while (r < 0) {
                    r += divsLong;
                    q--;
                }
                while (r >= divsLong) {
                    r -= divsLong;
                    q++;
                }
                n[i] = (int) q;
                cur = (int) r;
        	}
        }
        ref = val;
        val = n;
    }
	
	private final int divmodOneWord(int divs) 
	{
		final int[] orig = val;
		if(divs == 1) 
			return 0;
        final long divsLong = divs & 0xFFFFFFFFL;
        if(ref == null)
			ref = new int[length];
        final int[] n = ref;
        final int vlen = MathHelper.getRealLength(orig);
        if (vlen == 1) {
        	n[0] = (int) ((orig[0] & 0xFFFFFFFFL) / divsLong);
        	orig[0] = (int) ((val[0] & 0xFFFFFFFFL) % divsLong);
        	val = n;
        	ref = orig;
            return orig[0];
        }
        
        long cur = 0;
        int i = vlen;
        while(--i >= 0)
        {
        	cur <<= 32;
        	cur |= orig[i] & 0xFFFFFFFFL;
        	if(cur >= 0) {
        		n[i] = (int) (cur / divsLong);
        		cur = (int) (cur % divsLong);
        	} else {
                long q = (cur >>> 1) / (divsLong >>> 1);
                long r = cur - q * divsLong;
                while (r < 0) {
                    r += divsLong;
                    q--;
                }
                while (r >= divsLong) {
                    r -= divsLong;
                    q++;
                }
                n[i] = (int) q;
                cur = (int) r;
        	}
        }
        orig[0] = (int) cur;
        Arrays.fill(orig, 1, vlen, 0);
        val = n;
        ref = orig;
        return orig[0];
    }
	
	private final void moduloOneWord(final int divs) 
	{
		if(divs == 1) {
			Arrays.fill(val, 0);
			return;
		}
        final long divsLong = divs & 0xFFFFFFFFL;
        final int vlen = MathHelper.getRealLength(val);
        
        if (vlen == 1) {
        	val[0] = (int) ((val[0] & 0xFFFFFFFFL) % divsLong);
            //int r = (int) (dividendValue - q * divsLong);
            return;
        }
        
        long cur = 0;
        int i = vlen;
        while(--i >= 0)
        {
        	cur <<= 32;
        	cur |= val[i] & 0xFFFFFFFFL;
        	if(cur >= 0) {
        		cur = (int) (cur % divsLong);
        	} else {
                long q = (cur >>> 1) / (divsLong >>> 1);
                long r = cur - q * divsLong;
                while (r < 0) 
                    r += divsLong;
                while (r >= divsLong) 
                    r -= divsLong;
                cur = (int) r;
        	}
        }
        val[0] = (int) cur;
        Arrays.fill(val, 1, vlen, 0);
    }

	public void p_square()
	{
		
		int tlen = MathHelper.getRealLength(val);
		int max = length;
		if(ref == null)
			ref = new int[length];
		final int[] ref = this.ref;
		System.arraycopy(val, 0, ref, 0, tlen);
		if(MathHelper.mul1(val, tlen, val[0]) > 0) {	
			return;
		}
		for(int j = 1; j < tlen; j++)
		{
			long borrow = 0;
			long jw = ref[j] & 0xFFFFFFFFL;
			int k = 0;
			int t = j;
			for(; t < max; k++, t++)
			{
				if(k < tlen) {
					borrow += (jw * (ref[k] & 0xFFFFFFFFL)) + (val[t] & 0xFFFFFFFFL);
					val[t] = (int) borrow;
					borrow >>>= 32;
				} else {
					if(borrow != 0) {
						val[t] = (int) borrow;
						borrow = 0;
					} else 
						break;
				}
			}
			if(borrow != 0) {
				return;
			}
		}
	}

	public UInt p_divmod(IInteger<?> i)
	{
		int[] ints = i.getArr();
		int dlen = MathHelper.getRealLength(ints);
		int vlen = MathHelper.getRealLength(val);
		if(dlen > vlen) {
			this.zero();
			return new UInt(ArrayUtil.copy(val));
		}
		if(dlen < 2) 
			return new UInt(divmodOneWord(ints[0]));
		int[] orig = val;
		fastdiv(ints, dlen, vlen);
		return new UInt(ArrayUtil.copy(orig));
	}

	public void increment()
	{
		val[0]++;
		int i = 0;
		while(val[i] == 0)
			val[++i]++;
	}

	public void decrement()
	{
		val[0]--;
		int i = 0;
		while(val[i] == 0xFFFFFFFF)
			val[++i]--;
	}

	public int[] getInts()
	{
		return this.val;
	}

	public byte toInt8()
	{
		throw new java.lang.UnsupportedOperationException();
	}

	public short toInt16()
	{
		throw new java.lang.UnsupportedOperationException();
	}

	public int toInt32()
	{
		return val[0];
	}

	public long toInt64()
	{
		return Bits.getLong(val[1], val[0]);
	}
	
	public void p_add(int i)
	{
		MathHelper.add1(val, i);
	}
	
	public void p_subtract(int i)
	{
		MathHelper.subtract1(val, i);
	}
	
	public void p_multiply(int i)
	{
		MathHelper.mul1(val, MathHelper.getRealLength(val), i);
	}
	
	public void p_divide(int i)
	{
		divideOneWord(i);
	}

	public void p_modulo(int i)
	{
		moduloOneWord(i);
	}
	
	public int p_divmod(int i)
	{
		return this.divmodOneWord(i);
	}
	
	public boolean isNonZero()
	{
		for(int i = 0; i < this.length; i++)
			if(this.val[i] != 0)
				return true;
		return false;
	}

	public int[] getArr()
	{
		return this.val;
	}
	
	public void p_trim()
	{
		length = MathHelper.getRealLength(val);
	}

	public boolean sameAs(UInt obj)
	{
		if(this.length != obj.length)
			return false;
		for(int i = 0; i < this.length; i++)
			if(obj.val[i] != this.val[i])
				return false;
		return true;
	}

	public void setTo(long l)
	{
		if(length > 1) {
			val[0] = (int) l;
			val[1] = (int) (l >>> 32);
			Arrays.fill(val, 2, length, 0);
		} else
			val[0] = (int) l;
	}
	
	public void setArr(int[] arr)
	{
		if(arr.length > this.length)
			if(MathHelper.getRealLength(arr) > this.length)
				throw new java.lang.ArrayIndexOutOfBoundsException("Cannot set " + arr.length + " array to a " + this.length +"-int integer");
		if(length > arr.length)
			Arrays.fill(val, arr.length, length, 0);
		System.arraycopy(arr, 0, val, 0, length > arr.length ? arr.length : length);
	}
	
	public boolean isOdd()
	{
		return (val[0] & 1) == 1;
	}
	
	public int getIntLen()
	{
		return this.length;
	}

	public void zero()
	{
		Arrays.fill(val, 0);
	}
	
	public boolean bitAt(int pos)
	{
		int ipos = pos >>> 5;
		if(ipos == 0) 
			return Bits.getBit(val[0], pos);
		return Bits.getBit(val[ipos], pos & 31);
	}

	public void upsize(int size)
	{
		int[] n = new int[size];
		if(ref != null && size > ref.length)
			ref = null;
		System.arraycopy(val, 0, n, 0, length);
		this.length = size;
		val = n;
	}

	public UInt getLarge(int size)
	{
		int[] n = new int[size];
		System.arraycopy(val, 0, n, 0, length);
		return new UInt(n);
	}


	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInt(this.length);
		stream.writeInts(val, 0, length);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.intToBytes(this.length, bytes, offset);
		Bits.intsToBytes(val, 0, bytes, offset + 4, length);
	}

	public int exportSize()
	{
		return (this.length << 2) + 4;
	}

	public Factory<UInt> factory()
	{
		return factory;
	}
	
	public IntegerFactory<UInt> iFactory()
	{
		return ifactory;
	}
	
	public static final Factory<UInt> factory = new UIntFactory();
	public static final IntegerFactory<UInt> ifactory = new UIntBuildFactory();
	
	private static final class UIntFactory extends Factory<UInt>
	{
		protected UIntFactory()
		{
			super(UInt.class);
		}

		public UInt resurrect(byte[] data, int start)
		{
			int[] val = new int[Bits.intFromBytes(data, start)];
			Bits.bytesToInts(data, start + 4, val, 0, val.length);
			return new UInt(val);
		}

		public UInt resurrect(IIncomingStream stream) throws IOException
		{
			int[] val = new int[stream.readInt()];
			stream.readInts(val);
			return new UInt(val);
		}
		
	}
	
	private static final class UIntBuildFactory extends IntegerFactory<UInt>
	{

		public UInt construct(int len)
		{
			return new UInt(len);
		}

		public UInt construct(int[] ints)
		{
			return new UInt(ints);
		}

		public UInt construct(char[] chars, int start, int len, int len2)
		{
			return new UInt(chars, start, len, len2);
		}

		public UInt[] array(int len)
		{
			return new UInt[len];
		}
		
	}
	
	public static int test()
	{
		int er = 0;
		er += IInteger.verifyConstruction(ifactory);
		er += IInteger.verifyAddition(ifactory);
		er += IInteger.verifySubtraction(ifactory);
		er += IInteger.verifyMultiplication(ifactory);
		er += IInteger.verifySquaring(ifactory);
		er += IInteger.verifyDivision(ifactory);
		er += IInteger.verifyModulus(ifactory);
		er += IInteger.verifyAddSub(ifactory);
		er += IInteger.verifyMulDiv(ifactory);
		er += IPersistable.test(new UInt("34123213123213123123123213213", 32));
		er += IDeepClonable.test(new UInt("34123213123213123123123213213", 32));
		return er;
	}
	
}

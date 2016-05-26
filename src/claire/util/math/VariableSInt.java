package claire.util.math;

import java.io.IOException;
import java.util.Arrays;

import claire.util.encoding.CString;
import claire.util.io.Factory;
import claire.util.io.IOUtils;
import claire.util.memory.Bits;
import claire.util.standards.IInteger;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class VariableSInt 
	   extends StdSInt<VariableSInt> {
	
	private int[] val;
	private int length;
	private boolean down = false;
	private boolean sign = false;
	
	public VariableSInt(int i)
	{
		this.length = i;
		val = new int[this.length];
	}
	
	public VariableSInt(int[] i)
	{
		val = i;
		this.length = i.length;
	}
	
	public VariableSInt(int[] i, boolean negative)
	{
		val = i;
		this.sign = negative;
		this.length = i.length;
	}
	
	public VariableSInt(char[] chars, int start, int len, int len2) 
	{
		this.length = len2;
		this.val = new int[len2];
		IInteger.make(this, chars, start, len);
	}

	public VariableSInt(String string, int len) 
	{
		this.length = len;
		this.val = new int[len];
		IInteger.make(this, string.toCharArray());
	}

	public VariableSInt(CString string, int len) 
	{
		this.length = len;
		this.val = new int[len];
		IInteger.make(this, string.array());
	}
	
	public VariableSInt(String string) 
	{
		this.length = string.length() / 9 + 1;
		this.val = new int[this.length];
		IInteger.make(this, string.toCharArray());
	}

	public VariableSInt(CString string) 
	{
		this.length = string.length() / 9 + 1;
		this.val = new int[this.length];
		IInteger.make(this, string.array());
	}

	public VariableSInt createDeepClone()
	{
		int[] n = new int[this.length];
		System.arraycopy(val, 0, n, 0, this.length);
		return new VariableSInt(n, this.sign);
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.VSINT;
	}
	
	protected void build(int len)
	{
		if(len == 0)
			len = 1;
		if(len != this.length) {
			this.length = len;
			int[] old = val;
			val = new int[len];
			System.arraycopy(old, 0, val, 0, (old.length > len) ? len : old.length);
		}
	}
	
	protected void p_add(int[] ints)
	{
		int len = MathHelper.getRealLength(ints);
		if(len >= MathHelper.getRealLength(val))
			this.build(len + 1);
		else if(val[this.length - 1] != 0)
			this.build(this.length + 1);
		long carry = 0;
		int j = 0;
		for(; j < len; j++)
		{
			carry += ((long) val[j] & 0xFFFFFFFFL) + ((long) ints[j] & 0xFFFFFFFFL);
			val[j] = (int) carry;
			carry >>>= 32;
		}
		while(j < this.length && carry != 0) {
			carry += ((long) val[j] & 0xFFFFFFFFL);
			val[j++] = (int) carry;
			carry >>>= 32;
		}
		if(carry != 0)
			throw new java.lang.ArithmeticException("Ran out of space during addition");
	}
	
	protected  void p_subtract(int[] ints)
	{
		int len = MathHelper.getRealLength(ints);
		if(len > this.length) {
			this.setMIN();
			this.build(1);
		}
		long carry = 0;
		int j;
		for(j = 0; j < len; j++)
		{
			int i1, i2;
			i1 = val[j];
			i2 = ints[j];
			i2 += carry;
			if(Bits.u_greaterThan((int) carry, i2)) 
				carry = 1;
			else
				carry = 0;
			i2 = i1 - i2;
			if(Bits.u_greaterThan(i2, i1)) 
				carry += 1;
			val[j] = i2;
		}
		if(carry > 0)
			if(j < this.length)
				while(true) {
					val[j]--;
					if(val[j++] != -1) {
						break;
					}
				}
			else
				this.setMIN();
		if(this.down)
			this.build(MathHelper.getRealLength(val));
	}
	
	protected void p_multiply(int[] ints)
	{
		int len = MathHelper.getRealLength(ints);
		int tlen = MathHelper.getRealLength(val);
		int[] ref = new int[tlen];
		System.arraycopy(val, 0, ref, 0, tlen);
		if((len + tlen) > this.length)
			this.build(tlen + len);
		int max = val.length;
		if(MathHelper.mul1(val, tlen + 1, ints[0]) > 0) 
			throw new java.lang.ArithmeticException("Ran out of space during multiplication");
		for(int j = 1; j < len; j++)
		{
			long borrow = 0;
			long jw = ((long) ints[j] & 0xFFFFFFFFL);
			int k = 0;
			int t = 0;
			while((t = k + j) < max)
			{
				if(k < tlen) {
					borrow += (jw * ((long) ref[k] & 0xFFFFFFFFL)) + ((long) val[t] & 0xFFFFFFFFL);
					val[t] = (int) borrow;
					borrow >>>= 32;
				} else {
					if(borrow != 0) {
						val[t] = (int) borrow;
						borrow >>>= 32;
					} else {
						break;
					}
				}
				k++;
			}
			//Shouldn't happen
			if(borrow != 0) 
				throw new java.lang.ArithmeticException("Ran out of space during multiplication");
		}
	}
	
	protected  void p_divide(int[] ints)
	{
		int dlen = MathHelper.getRealLength(ints);
		int vlen = MathHelper.getRealLength(val);
		if(dlen > vlen) {
			this.setMIN();
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
		if(down)
			this.build(dlen);	
	}
	
	private final void fastdiv(final int[] divisor, final int dlen, int vlen)
	{
		final int[] n = new int[down ? vlen - dlen + 1 : vlen];
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
			divd = new int[vlen + 1];
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
				verify ^= Bits.u_greaterThan((int) prev, r);
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
		this.length = val.length;
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
			divd = new int[vlen + 1];
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
				verify ^= Bits.u_greaterThan((int) prev, r);
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
			final int[] n = new int[this.length];
			System.arraycopy(val, 0, n, 0, this.length);
			val = n;
			return;
		}
        final long divsLong = divs & 0xFFFFFFFFL;
        final int[] n = new int[this.length];
        final int vlen = MathHelper.getRealLength(val);
        
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
        val = n;
    }
	
	private final int[] divmodOneWord(int divs) 
	{
		final int[] orig = val;
		if(divs == 1) {
			final int[] n = new int[this.length];
			System.arraycopy(orig, 0, n, 0, this.length);
			Arrays.fill(orig, 0);
			val = n;
			return orig;
		}
        final long divsLong = divs & 0xFFFFFFFFL;
        final int[] n = new int[this.length];
        final int vlen = MathHelper.getRealLength(orig);
        
        if (vlen == 1) {
        	n[0] = (int) ((orig[0] & 0xFFFFFFFFL) / divsLong);
        	orig[0] = (int) ((val[0] & 0xFFFFFFFFL) % divsLong);
        	val = n;
            return orig;
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
        return orig;
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
	
	private final void setMIN()
	{
		Arrays.fill(val, 0);
	}

	public void p_square()
	{
		int tlen = MathHelper.getRealLength(val);
		int[] ref = new int[tlen];
		System.arraycopy(val, 0, ref, 0, tlen);
		if((tlen * 2) > this.length)
			this.build(tlen * 2);
		int max = val.length;
		if(MathHelper.mul1(val, tlen + 1, ref[0]) > 0) 
			throw new java.lang.ArithmeticException("Ran out of space during multiplication");
		for(int j = 1; j < tlen; j++)
		{
			long borrow = 0;
			long jw = ((long) ref[j] & 0xFFFFFFFFL);
			int k = 0;
			int t = 0;
			while((t = k + j) < max)
			{
				if(k < tlen) {
					borrow += (jw * ((long) ref[k] & 0xFFFFFFFFL)) + ((long) val[t] & 0xFFFFFFFFL);
					val[t] = (int) borrow;
					borrow >>>= 32;
				} else {
					if(borrow != 0) {
						val[t] = (int) borrow;
						borrow >>>= 32;
					} else {
						break;
					}
				}
				k++;
			}
			//Shouldn't happen
			if(borrow != 0) 
				throw new java.lang.ArithmeticException("Ran out of space during multiplication");
		}
	}

	public VariableSInt p_divmod(IInteger<?> i)
	{
		int[] ints = i.getArr();
		int dlen = MathHelper.getRealLength(ints);
		int vlen = MathHelper.getRealLength(val);
		if(dlen > vlen) {
			this.setMIN();
		}
		if(dlen < 2) 
			return new VariableSInt(divmodOneWord(ints[0]));
		int[] orig = val;
		fastdiv(ints, dlen, vlen);
		return new VariableSInt(orig);
	}

	public boolean isNegative()
	{
		return this.sign;
	}

	public void increment()
	{
		if(this.isNegative()) {
			this.decrement();
			return;
		}
		val[0]++;
		int i = 0;
		while(val[i] == 0) {
			if(i == this.length - 1) {
				val = new int[this.length + 1];
				val[++i] = 1;
				return;
			}
			val[++i]++;
		}
	}

	public void decrement()
	{
		if(this.isNegative()) {
			this.increment();
			return;
		}
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
		throw new java.lang.UnsupportedOperationException();
	}
	
	public void ip_add(int i)
	{
		if(val[val.length - 1] == 0xFFFFFFFF)
			build(val.length + 1);
		MathHelper.add1(val, i);
	}
	
	public void ip_subtract(int i)
	{
		MathHelper.subtract1(val, i);
	}
	
	public void ip_multiply(int i)
	{
		if(val.length == MathHelper.getRealLength(val))
			build(val.length + 1);
		MathHelper.mul1(val, MathHelper.getRealLength(val), i);
	}
	
	public void ip_divide(int i)
	{
		divideOneWord(i);
	}

	public void ip_modulo(int i)
	{
		moduloOneWord(i);
	}

	public int ip_divmod(int i)
	{
		return this.divmodOneWord(i)[0];
	}
	
	public boolean isNonZero()
	{
		for(int i = 0; i < this.val.length; i++)
			if(this.val[i] != 0)
				return true;
		return false;
	}

	public void invertSign() 
	{
		this.sign ^= true;
	}
	
	public void setNegative(boolean neg)
	{
		this.sign = neg;
	}

	public int[] getArr()
	{
		return this.val;
	}

	public boolean sameAs(VariableSInt obj)
	{
		if(this.isNegative() ^ obj.isNegative())
			return false;
		if(this.length != obj.length)
			return false;
		for(int i = 0; i < this.length; i++)
			if(obj.val[i] != this.val[i])
				return false;
		return true;
	}

	public void setTo(long l)
	{
		if(l < 0) {
			this.sign = true;
			l = ~l + 1;
		}
		if(val.length == 1) 
			build(2);
		val[0] = (int) l;
		val[1] = (int) (l >>> 32);
		Arrays.fill(val, 2, val.length, 0);
	}
	
	public void setTo(char[] chars)
	{
		this.build(chars.length / 9 + 1);
		super.setTo(chars);
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
		this.sign = false;
	}

	public boolean bitAt(int pos)
	{
		int ipos = pos / 32;
		if(ipos == 0) 
			return Bits.getBit(val[0], pos);
		else
			return Bits.getBit(val[ipos], pos & 31);
	}
	
	public void upsize(int size)
	{
		int[] n = new int[size];
		System.arraycopy(val, 0, n, 0, val.length);
		this.length = size;
		val = n;
	}

	public VariableSInt getLarge(int size)
	{
		int[] n = new int[size];
		System.arraycopy(val, 0, n, 0, val.length);
		return new VariableSInt(n, this.sign);
	}
	
	public void setDownsizable(boolean down)
	{
		this.down = down;
	}

	public void setTo(IInteger<?> other)
	{
		this.sign = other.isNegative();
		this.setArr(other.getArr());
	}

	protected void create()
	{
		this.val = new int[this.length];
	}

	public void setArr(int[] arr)
	{
		if(arr.length > this.length)
			this.build(arr.length);
		if(val.length > arr.length)
			Arrays.fill(val, arr.length, val.length, 0);
		System.arraycopy(arr, 0, val, 0, val.length > arr.length ? arr.length : val.length);
	}
	
	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeIntArr(val);
		stream.writeBool(sign);
	}

	public void export(byte[] bytes, int offset)
	{
		offset = IOUtils.writeArr(val, bytes, offset);
		bytes[offset] = (byte) (sign ? 1 : 0);
	}

	public int exportSize()
	{
		return this.length * 4 + 5;
	}

	public Factory<VariableSInt> factory()
	{
		return factory;
	}
	
	public IntegerFactory<VariableSInt> iFactory()
	{
		return ifactory;
	}
	
	public static final IntegerFactory<VariableSInt> ifactory = new VariableSIntBuildFactory();
	public static final VariableSIntFactory factory = new VariableSIntFactory();
	
	private static final class VariableSIntBuildFactory extends IntegerFactory<VariableSInt>
	{

		public VariableSInt construct(int len)
		{
			return new VariableSInt(len);
		}

		public VariableSInt construct(int[] ints)
		{
			return new VariableSInt(ints);
		}

		public VariableSInt construct(char[] chars, int start, int len, int len2)
		{
			return new VariableSInt(chars, start, len, len2);
		}

		public VariableSInt[] array(int len)
		{
			return new VariableSInt[len];
		}
		
	}
	
	private static final class VariableSIntFactory extends Factory<VariableSInt>
	{

		protected VariableSIntFactory() 
		{
			super(VariableSInt.class);
		}

		public VariableSInt resurrect(byte[] data, int start) throws InstantiationException
		{
			int len = Bits.intFromBytes(data, start); start += 4;
			int[] val = new int[len];
			Bits.bytesToInts(data, start, val, 0, len); start += len * 4;
			return new VariableSInt(val, data[start] == 1);
		}

		public VariableSInt resurrect(IIncomingStream stream) throws InstantiationException, IOException
		{
			return new VariableSInt(stream.readIntArr(), stream.readBool());
		}
		
	}
	
}

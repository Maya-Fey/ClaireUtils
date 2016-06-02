package claire.util.math;

import java.io.IOException;
import java.util.Arrays;

import claire.util.encoding.CString;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.standards.IDeepClonable;
import claire.util.standards.IInteger;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class VariableUInt 
	   extends StdUInt<VariableUInt> {
	
	private int[] val;
	private int length;
	private boolean down = false;
	
	public VariableUInt(int i)
	{
		this.length = i;
		val = new int[this.length];
	}
	
	public VariableUInt(int[] i)
	{
		val = i;
		this.length = i.length;
	}

	public VariableUInt(char[] chars, int start, int len, int len2) 
	{
		this.length = len2;
		this.val = new int[len2];
		IInteger.make(this, chars, start, len);
	}
	
	public VariableUInt(String string, int len) 
	{
		this.length = len;
		this.val = new int[len];
		IInteger.make(this, string.toCharArray());
	}

	public VariableUInt(CString string, int len) 
	{
		this.length = len;
		this.val = new int[len];
		IInteger.make(this, string.array());
	}
	
	public VariableUInt(String string) 
	{
		this.length = string.length() / 9 + 1;
		this.val = new int[this.length];
		IInteger.make(this, string.toCharArray());
	}

	public VariableUInt(CString string) 
	{
		this.length = string.length() / 9 + 1;
		this.val = new int[this.length];
		IInteger.make(this, string.array());
	}
	
	public VariableUInt createDeepClone()
	{
		int[] n = new int[this.length];
		System.arraycopy(val, 0, n, 0, this.length);
		return new VariableUInt(n);
	}
	
	public VariableUInt(VariableUInt u, int nt)
	{
		int[] ints = new int[nt];
		System.arraycopy(u.getArr(), 0, ints, 0, u.getIntLen());
		val = ints;
		this.length = nt;
	}

	public int NAMESPACE()
	{
		return _NAMESPACE.VUINT;
	}
	
	protected void create()
	{
		this.val = new int[this.length];
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
	
	protected  void p_subtract(int[] ints)
	{
		int len = MathHelper.getRealLength(ints);
		if(len > this.length) {
			this.setMIN();
			this.build(1);
		}
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
			long jw = ints[j] & 0xFFFFFFFFL;
			int k = 0;
			int t = 0;
			while((t = k + j) < max)
			{
				if(k < tlen) {
					borrow += (jw * (ref[k] & 0xFFFFFFFFL)) + (val[t] & 0xFFFFFFFFL);
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
		this.build(tlen * 2 + 1);
		int max = val.length;
		if(MathHelper.mul1(val, tlen, val[0]) > 0) 
			throw new java.lang.ArithmeticException("Ran out of space during multiplication");
		for(int j = 1; j < tlen; j++)
		{
			long borrow = 0;
			long jw = ref[j] & 0xFFFFFFFFL;
			int k = 0;
			int t = 0;
			while((t = k + j) < max)
			{
				if(k < tlen) {
					borrow += (jw * (ref[k] & 0xFFFFFFFFL)) + (val[t] & 0xFFFFFFFFL);
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
			if(borrow != 0) 
				throw new java.lang.ArithmeticException("Ran out of space during multiplication");
		}
	}

	public VariableUInt p_divmod(IInteger<?> i)
	{
		int[] ints = i.getArr();
		int dlen = MathHelper.getRealLength(ints);
		int vlen = MathHelper.getRealLength(val);
		if(dlen > vlen) {
			this.setMIN();
		}
		if(dlen < 2) 
			return new VariableUInt(divmodOneWord(ints[0]));
		int[] orig = val;
		fastdiv(ints, dlen, vlen);
		return new VariableUInt(orig);
	}

	public void increment()
	{
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
		if(val[val.length - 1] == 0xFFFFFFFF)
			build(val.length + 1);
		MathHelper.add1(val, i);
	}
	
	public void p_subtract(int i)
	{
		MathHelper.subtract1(val, i);
	}
	
	public void p_multiply(int i)
	{
		if(MathHelper.getRealLength(val) == val.length)
			build(val.length + 1);
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
		return this.divmodOneWord(i)[0];
	}
	
	public boolean isNonZero()
	{
		for(int i = 0; i < this.val.length; i++)
			if(this.val[i] != 0)
				return true;
		return false;
	}

	public int[] getArr()
	{
		return this.val;
	}

	public boolean sameAs(VariableUInt obj)
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
	
	public void setArr(int[] arr)
	{
		if(arr.length > this.length)
			this.build(arr.length);
		if(val.length > arr.length)
			Arrays.fill(val, arr.length, val.length, 0);
		System.arraycopy(arr, 0, val, 0, val.length > arr.length ? arr.length : val.length);
	}
	
	public void p_trim()
	{
		length = MathHelper.getRealLength(val);
		int[] n = new int[length];
		System.arraycopy(val, 0, n, 0, length);
		val = n;
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

	public VariableUInt getLarge(int size)
	{
		int[] n = new int[size];
		System.arraycopy(val, 0, n, 0, val.length);
		return new VariableUInt(n);
	}
	
	public void setDownsizable(boolean down)
	{
		this.down = down;
	}

	public void export(IOutgoingStream stream) throws IOException
	{
		stream.writeInt(this.length);
		stream.writeInts(val);
	}

	public void export(byte[] bytes, int offset)
	{
		Bits.intToBytes(this.length, bytes, offset);
		Bits.intsToBytes(val, 0, bytes, offset + 4);
	}

	public int exportSize()
	{
		return (this.length << 2) + 4;
	}

	public Factory<VariableUInt> factory()
	{
		return factory;
	}
	
	public IntegerFactory<VariableUInt> iFactory()
	{
		return ifactory;
	}
	
	public static final IntegerFactory<VariableUInt> ifactory = new VariableUIntBuildFactory();
	public static final Factory<VariableUInt> factory = new UIntFactory();
	
	private static final class UIntFactory extends Factory<VariableUInt>
	{
		protected UIntFactory()
		{
			super(VariableUInt.class);
		}

		public VariableUInt resurrect(byte[] data, int start)
		{
			int[] val = new int[Bits.intFromBytes(data, start)];
			Bits.bytesToInts(data, start + 4, val, 0, val.length);
			return new VariableUInt(val);
		}

		public VariableUInt resurrect(IIncomingStream stream) throws IOException
		{
			int[] val = new int[stream.readInt()];
			stream.readInts(val);
			return new VariableUInt(val);
		}
		
	}
	
	private static final class VariableUIntBuildFactory extends IntegerFactory<VariableUInt>
	{

		public VariableUInt construct(int len)
		{
			return new VariableUInt(len);
		}

		public VariableUInt construct(int[] ints)
		{
			return new VariableUInt(ints);
		}

		public VariableUInt construct(char[] chars, int start, int len, int len2)
		{
			return new VariableUInt(chars, start, len, len2);
		}

		public VariableUInt[] array(int len)
		{
			return new VariableUInt[len];
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
		er += IInteger.verifyVariable(ifactory);
		er += IInteger.verifyComparator(ifactory);
		er += IInteger.verifySquareDiv(ifactory);
		er += IInteger.verifyIncDec(ifactory);
		er += IPersistable.test(new VariableUInt("34123213123213123123123213213", 32));
		er += IDeepClonable.test(new VariableUInt("34123213123213123123123213213", 32));
		return er;
	}
	
}

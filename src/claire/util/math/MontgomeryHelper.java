package claire.util.math;

import java.util.Arrays;

import claire.util.math.primitive.ImmutableUInt;
import claire.util.math.primitive.UInt;

public class MontgomeryHelper {

	private long[] moda;
	
	private ImmutableUInt mod;
	private ImmutableUInt rinv;
	
	private int len;
	private long ma;
	
	public void init(UInt modulus)
	{
		mod = new ImmutableUInt(modulus, false);
		len = modulus.realLen();
		moda = new long[len];
		for(int i = 0; i < len; i++)
			moda[i] = mod.getArr()[i] & 0xFFFFFFFFL;
		
		
		long t = 0;
		long r = 0x100000000L;
		long t2 = 1;
		long r2 = 0x100000000L - moda[0];
		long q;
		long T;
		while(r2 != 0)
		{
			q = r / r2;
			T = t;
			t = t2;
			t2 = T - q * t;
			T = r;
			r = r2;
			r2 = T - q * r;
		}
		if(t < 0)
			ma = (int) (t + 0x100000000L);
		else 
			ma = (int) t;
		
		UInt rinv = modulus.iFactory().construct(len + 1);
		rinv.getArr()[len] = 1;
		rinv.p_modulo(modulus);
		this.rinv = new ImmutableUInt(MathHelper.u_modular_inverse(rinv, modulus), true);
	}
	
	public void montgomerize(UInt i)
	{
		final int[] arr = i.getArr();
		System.arraycopy(arr, 0, arr, len, len);
		Arrays.fill(arr, 0, len, 0);
		i.p_modulo(mod);
	}
	
	public void demontgomerize(UInt i)
	{
		i.p_multiply(rinv);
		i.p_modulo(mod);
	}
	
	public void montReduce(UInt in) 
	{
		final int[] arr = in.getArr();
		
		for(int i = 0; i < len; i++) {
			final long u = 0xFFFFFFFFL & (ma * (arr[i] & 0xFFFFFFFFL));
			long c1, c2 = c1 = 0;
			int k = i;
			for(int j = 0; j < len; j++) {
				c1 += moda[j] * u;
				arr[k] = (int) (c2 += (arr[k++] & 0xFFFFFFFFL) + (c1 & 0xFFFFFFFFL));
				c2 >>>= 32;
				c1 >>>= 32;
			}
			
			arr[k] = (int) (c2 += (arr[k++] & 0xFFFFFFFFL) + (c1 & 0xFFFFFFFFL));
			c2 >>>= 32;
			
			if(c2 > 0)
				while(++arr[k++] == 0);
		}
		System.arraycopy(arr, len, arr, 0, len + 1);
		Arrays.fill(arr, len + 1, arr.length, 0);
		
		if(in.isGreaterOrEqualTo(mod)) {
			in.p_subtract(mod);
		}
	}

}

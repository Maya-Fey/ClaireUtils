package claire.util.crypto.hash.primitive.BEAR;

import java.util.Arrays;

import claire.util.crypto.rng.RandUtils;
import claire.util.memory.Bits;
import claire.util.memory.array.ComboArray;

/**
 * Note: This hash function is mostly a prototype. It is poor to its purpose 
 * and while being more difficult to implement in hardware, still consumes relatively
 * low memory as the N-Factor increases. Security *should* be fine, but for
 * the computational intensiveness of the hash I would not recommend for anything
 * else but password hashing.<br><br>
 * 
 * Flaws:
 * <ul>
 *  <li>Uses interesting RNG reseeding for collision resistance, but the RNG
 *      itself is overcomplicated and not cryptographically secure. To be frank
 *      I didn't have much of an idea of what I was doing.</li>
 *  <li>N-Factor increase does not increase memory consumption as nearly as much as
 *      it should</li>
 *  <li>Increasing N-Factor increases computational load too much</li>
 * </ul>
 * 
 * <hr>
 * 
 * <h1>BEAR-3</h1>
 * <br>
 * BEAR-3 is a hash function which combines cat-in-a-tree construction 
 * with fiestel networks. The result is called 'burning fiestel networks'
 * and is called such because BEAR-3 is a 'lossy' algorithm which gives
 * it extreme preimage resistance.
 * <br>
 * @author Claire
 */
public class $BEAR3 {
	
	private BEAR3_RNG brng;
	
	private byte[] INP;
	private byte[] MIR;
	
	protected final int N,
					  	R,
					  	S,
					  	I, Im,
					  	L,
					  	F, Ff,
					  	Sr, Er,
					  	D;

	protected int rem;
	
	protected byte[][] INET;
	protected byte[][] MNET;
	
	protected byte[][][] tree;
	
	/**
	 * @author Claire
	 * @param N Amount of leaves per branch
	 * @param R Rounds of leaf processing
	 * @param S Rounds of seasoning
	 * @param F Amount of bytes to process before function switch
	 * @param I Branch size in bytes
	 * @param L Output length in bytes
	 * @param Sr Seasoning rounds before burning
	 * @param Er Seasoning rounds after burning
	 * @param D Amount of leaves to destroy each round during seasoning
	 */
	public $BEAR3(int N, int R, int S, int F, int I, int L, int Sr, int Er, int D)
	{
		if(!Bits.powerOfTwo(L))
			throw new java.lang.IllegalArgumentException("Length must be a power of two");
		if(!Bits.powerOfTwo(I))
			throw new java.lang.IllegalArgumentException("Internal Length must be a power of two");
		if((L % I) > 0)
			throw new java.lang.IllegalArgumentException("Length must be divisible by internal leaf size.");
		if((I % F) > 0)
			throw new java.lang.IllegalArgumentException("Internal leaf size must be divisible by function.");
		if((N % D) > 0)
			throw new java.lang.IllegalArgumentException("N-Factor must be divisible by destruction factor.");
		this.N = N + 1;
		this.R = R;
		this.S = S;
		this.I = I; this.Im = L / I; 
		this.L = L;
		this.F = F;
		this.Ff = I / F;
		this.Sr = Sr;
		this.Er = Er;
		this.D = D;
	}
	
	public $BEAR3(int N, int R, int S, int F, int I, int L, int Sr, int Er)
	{
		this(N, R, S, F, I, L, Sr, Er, 1);
	}
	
	public $BEAR3(int N, int R, int S, int F, int I, int L)
	{
		this(N, R, S, F, I, L, 2, 2, 1);
	}
	
	public $BEAR3(int N, int R, int S, int I, int L)
	{
		this(N, R, S, 8, I, L, 2, 2, 1);
	}
	
	public final void init(byte[] input)
	{
		init(input, 0, input.length);
	}
	
	public final void init(byte[] input, int start, int len)
	{
		brng = new BEAR3_RNG(input, start, len);
		rem = (len + I - 1) / I;
		if(rem < Im)
			rem = Im;
		if(tree == null || rem > tree.length)
			tree = new byte[rem][N][I];
		INP = new byte[rem * I   ];
		MIR = new byte[INP.length];
		INET = new byte[rem][I];
		MNET = new byte[rem][I];
		System.arraycopy(input, start, INP, 0, len);
		for(int i = 0; i < len; i++)
			MIR[i] = (byte) (~INP[i] & 0xFF);
		Arrays.fill(MIR, len, MIR.length, (byte) 0xFF); 
		RandUtils.randomize(INP, brng);
		RandUtils.randomize(MIR, brng);
		ComboArray.Byte array = new ComboArray.Byte(INP, MIR);
		byte t1;
		int t2;
		for(int i = array.length() - 1; i > 0; i--)
		{
			t1 = array.get(i);
			t2 = brng.nextIntGood(i + 1);
			array.set(array.get(t2), i);
			array.set(t1, t2);
		}
		for(int i = 0; i < rem; i++)
		{
			System.arraycopy(INP, i * I, INET[i], 0, I);
			System.arraycopy(MIR, i * I, MNET[i], 0, I);
		}
	}
	
	public final void hash()
	{
		for(int i = 0; i < Sr; i++) {
			randomize();
			season();
		}
		while(rem > Im)
			burn();
		for(int i = 0; i < Er; i++) {
			randomize();
			season();
		}
	}
	
	public final void getOut(byte[] bytes, int start)
	{
		for(int i = 0; i < Im; i++)
			mix(INET[i], MNET[i], i);
		for(int i = 0; i < Im; i++) {
			System.arraycopy(INET[i], 0, bytes, start, I);
			start += I;
		}
	}
	
	public final byte[] getOut()
	{
		byte[] bytes = new byte[L];
		getOut(bytes, 0);
		return bytes;
	}
	
	protected final void burn()
	{
		for(int i = S; i > 0; i--) {
			randomize();
			season();
		}
		rem--;
		for(int i = 0; i < rem; i++) {
			mix(INET[i], MNET[rem], i);
			mix(MNET[i], INET[rem], i);
		}
	}

	private void randomize()
	{
		byte[] t1;
		int t2;
		for(int i = rem - 1; i > 0; i--)
		{
			t1 = INET[i];
			t2 = brng.nextIntGood(i + 1);
			INET[i ] = INET[t2];
			INET[t2] = t1;
			t1 = MNET[i];
			t2 = brng.nextIntGood(i + 1);
			MNET[i ] = MNET[t2];
			MNET[t2] = t1;
		}
		for(int i = 0; i < rem; i++) {
			RandUtils.randomize(INET[i], brng);
			RandUtils.randomize(MNET[i], brng);
		}
	}	
	
	private void season()
	{
		for(int i = 0; i < rem; i++) {
			final byte[][] branch = tree[i];
			for(int j = 0; j < N; j++) {
				final byte[] twig = branch[j];
				final byte[] MIR = MNET[i];
				final byte[] INP = INET[i];
				System.arraycopy(INET[i], 0, twig, 0, I);
				RandUtils.randomize(twig, brng);
				for(int k = 0; k < (I >> 1); k++)
					twig[k] ^= MIR[brng.nextIntGood(I)];
				for(int k = I >> 1; k < I; k++)
					twig[k] ^= INP[brng.nextIntGood(I)];
				brng.flash((byte) (twig[0] ^ twig[I - 1]));
			}
		}
		for(int i = N - 1; i > 0; i -= D)
			for(byte[][] branch : tree)
			{
				for(int j = 0; j <= i; j++)
					for(int k = R; k > 0; k--) {
						RandUtils.randomize(branch[j], brng);
						logic(branch[j], k);
					}
				for(int j = i; j > (i - D); j--)
					mix(branch[j - 1], branch[j], i);
			}
		for(int i = 0; i < rem; i++)
		{
			final byte[] carbon = tree[i][0];
			mix(INET[i], carbon, i);
			mix(MNET[i], INET[i], i);
		}
	}
	
	private void logic(byte[] leaf, int round)
	{
		byte prev = (byte) (brng.nextInt() ^ round);
		byte register = (byte) (brng.nextInt() ^ round);
		BearAccumulator acc = new BearAccumulator();
		LogicFunction func;
		for(int i = 0; i < F; i++)
		{
			func = nextFunction(prev);
			for(int j = (i * Ff); j < ((i + 1) * Ff); j++)
			{
				prev = func.exec(leaf[j], prev, register);
				register ^= prev;
				leaf[j] = prev;
			}
			acc.accumulate(register);
			brng.flash(prev);
		}
		brng.water(acc.out());
	}
	
	private void mix(byte[] leaf1, byte[] leaf2, int round)
	{
		RandUtils.randomize(leaf2, brng);
		byte prev = (byte) (brng.nextInt() ^ round);
		BearAccumulator acc = new BearAccumulator();
		LogicFunction func;
		for(int i = 0; i < F; i++)
		{
			func = nextFunction(prev);
			for(int j = (i * Ff); j < ((i + 1) * Ff); j++)
			{
				leaf1[j] ^= (byte) (leaf2[j] ^ prev);
				prev ^= func.exec(leaf1[j], leaf2[j], prev);
			}
			brng.flash(prev);
			acc.accumulate(prev);
		}
		brng.water(acc.out());
		RandUtils.randomize(leaf1, brng);
	}

	private static final LogicFunction nextFunction(byte in)
	{
		return FUNCTIONS[in & 15];
	}
	
	private static final LogicFunction[] FUNCTIONS = 
	{
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) ((a ^ b) ^ ((c ^ a) + (b ^ c)));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) (a ^ (b ^ c ^ (b + (c ^ a))));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) ((c | (a ^ b)) - (a & c) - (b & c) - (a & b));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) ((a & b) + ~(~a & c) + (b & ~c));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) ((a | b) ^ (a & b) + c);
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) ((a | b | c) & (a ^ b ^ c));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) (((a | b) ^ (a | c) ^ (b | c)) + ((a | ~b) ^ (a | ~c) ^ (b | ~c)));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) ((a | b ^ c) - (~c | b ^ a) + (c | a ^ b));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) ((a | b) + (a | c) + (b | c) ^ (~a | b));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) (((a | b) ^ c) + ((b | a) ^ c) ^ ((c | b) ^ a));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) ((a + ~(b & c) + (b | a)) + (b ^ ~a + (~c ^ a)));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) ((a & (b | c)) ^ (b | (c & a)) + (c ^ ~a));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) ((c ^ b & ~a) ^ (a ^ b | ~c) + (b ^ c + ~a));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) ((a ^ b ^ c) + (a & b & c) + (a | b | c));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) ((a | b + c) ^ (a & b - c));
			}
		},
		new LogicFunction()
		{
			public byte exec(byte a, byte b, byte c)
			{
				return (byte) ((a + b) ^ (b | c) ^ (a & c));
			}
		}
	};
	
	private static final class BearAccumulator
	{
		private int acc;
		private int pos = 0;
		
		private void accumulate(byte in)
		{
			acc ^= ((in & 0xFF) << (pos << 3));
			pos++;
			pos %= 4;
		}
		
		private int out()
		{
			return acc;
		}
	}
	
}
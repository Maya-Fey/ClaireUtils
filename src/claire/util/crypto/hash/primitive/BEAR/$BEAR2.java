package claire.util.crypto.hash.primitive.BEAR;

import claire.util.crypto.rng.primitive.MersenneTwister;
import claire.util.encoding.CString;
import claire.util.encoding.Hex;
import claire.util.memory.Bits;
import claire.util.memory.array.ComboArray;

/**
 * While being an improved version of a toy hash, this is still pretty poor. Do
 * not use for any serious application. Has known output bias.
 * 
 * <s><h1>BEAR-2</h1>
 * <br>
 * BEAR-2 is a hash function that uses cat-in-a-tree construction. It is built to resist
 * attempts at making a version which can be run in parallel. It randomizes and performs 
 * multiple logical functions on data in a sequential fashion alternating between 8 
 * distinct functions. 
 * <br><br>
 * To use BEAR-2, set N, R, and L values. N represents the amount of leaves created during
 * the seasoning stage, increased N-Factors will use more memory and computational power.
 * R represents the amount of randomize/logic rounds which are applied in each seasoning 
 * session. It is recommended that you use at least R = 2 to gain ample first and second
 * preimage resistance. The L represents the length of the hash in BYTES, must be a power
 * of two.
 * <br><br>
 * BEAR-2 Utilizes:
 * <ul>
 * <li>NOT</li>
 * <li>OR</li>
 * <li>AND</li>
 * <li>XOR</li>
 * <li>ROT</li>
 * <li>ADD</li>
 * <li>SUB</li>
 * </ul></s>
 * @author Claire
 */
public class $BEAR2 {
	
	private final int N;
	private final int R;
	private final int L;
	
	private byte[] IMP;
	private byte[] MIR;
	
	private byte[][] TREE;
	private byte[][] TMIR;
	private byte[][] leaves;
	
	private int branches;
	private State STATE;
	private MersenneTwister prng;

	public $BEAR2(int N, int R, int L)
	{
		if((L & (L - 1)) != 0)
			throw new java.lang.ExceptionInInitializerError("Length MUST be a power of two");
		if(L < 8)
			throw new java.lang.ExceptionInInitializerError("BEAR-2 requires over 8 bytes of state space to function");
		leaves = new byte[N << 1][L << 1];
		this.N = N;
		this.R = R;
		this.L = L;
	}
	
	public final void init(byte[] input, int off, int len)
	{
		int Lt = L;
		while(Lt < len)
			Lt <<= 1;
		IMP = new byte[Lt];
		MIR = new byte[Lt];
		System.arraycopy(input, off, IMP, 0, len);
		for(int i = 0; i < len; i++)
			MIR[i] = (byte) ~input[i + off];
		for(int i = len; i < MIR.length; i++)
			MIR[i] = (byte) 0xFF;
		this.prng = new MersenneTwister();
		this.STATE = new State(IMP, MIR, prng, (N * R) * 4);
		STATE.next();
		pisherYates(IMP);
		pisherYates(MIR);
		ComboArray.Byte array = new ComboArray.Byte(IMP, MIR);
		byte t1;
		int t2;
		for(int i = array.length() - 1; i > 1; i--)
		{
			t1 = array.get(i);
			t2 = prng.nextInt(i + 1);
			array.set(array.get(t2), i);
			array.set(t1, t2);
		}
		branches = IMP.length / L;
		TREE = new byte[branches][];
		TMIR = new byte[branches][];
		for(int i = 0; i < branches; i++)
		{
			byte[] branch = new byte[L];
			byte[] bramir = new byte[L];
			System.arraycopy(IMP, i * L, branch, 0, L);
			System.arraycopy(MIR, i * L, bramir, 0, L);
			TREE[i] = branch;
			TMIR[i] = bramir;
		}
	}
	
	public final void init(byte[] input)
	{
		int Lt = L;
		while(Lt < input.length)
			Lt <<= 1;
		IMP = new byte[Lt];
		MIR = new byte[Lt];
		System.arraycopy(input, 0, IMP, 0, input.length);
		for(int i = 0; i < input.length; i++)
			MIR[i] = (byte) ~input[i];
		for(int i = input.length; i < MIR.length; i++)
			MIR[i] = (byte) 0xFF;
		this.prng = new MersenneTwister();
		this.STATE = new State(IMP, MIR, prng, (N * R) << 2);
		STATE.next();
		pisherYates(IMP);
		pisherYates(MIR);
		ComboArray.Byte array = new ComboArray.Byte(IMP, MIR);
		byte t1;
		int t2;
		for(int i = array.length() - 1; i > 1; i--)
		{
			t1 = array.get(i);
			t2 = prng.nextInt(i + 1);
			array.set(array.get(t2), i);
			array.set(t1, t2);
		}
		branches = IMP.length / L;
		TREE = new byte[branches][];
		TMIR = new byte[branches][];
		for(int i = 0; i < branches; i++)
		{
			byte[] branch = new byte[L];
			byte[] bramir = new byte[L];
			System.arraycopy(IMP, i * L, branch, 0, L);
			System.arraycopy(MIR, i * L, bramir, 0, L);
			TREE[i] = branch;
			TMIR[i] = bramir;
		}
	}
	
	public final byte[] hash()
	{
		trunk();
		STATE.next();
		for(int i = 0; i < R; i++)
		{
			season(0);
			randomize();
		}
		logicMerge(TREE[0], TMIR[0], 0);
		return TREE[0];
	}
	
	public final int length()
	{
		return L;
	}	
	
	private final void pisherYates(byte[] bytes)
	{
		final MersenneTwister prng = this.prng;
		byte t1;
		int t2;
		for(int i = bytes.length - 1; i > 1; i--)
		{
			t1 = bytes[i];
			t2 = prng.nextInt(i + 1);
			bytes[i] = bytes[t2];
			bytes[t2] = t1;
		}
	}
	
	private final void randomize()
	{
		final MersenneTwister prng = this.prng;
		ComboArray.Byte array = new ComboArray.Byte(TREE[0], TMIR[0]);
		byte t1;
		int t2;
		for(int i = array.length() - 1; i > 1; i--)
		{
			t1 = array.get(i);
			t2 = prng.nextInt(i + 1);
			array.set(array.get(t2), i);
			array.set(t1, t2);
		}
	}
	
	private final void trunk()
	{
		while(branches > 1)
			degrow();
	}
	
	private final void degrow()
	{
		final int half = branches >> 1;
		for(int i = 0; i < half; i++)
		{
			season(i);
			season(i + half);
			reverseBranch(i, i + half);
		}
		branches >>= 1;
	}
	
	private final void reverseBranch(int b1, int b2)
	{
		logicMerge(TMIR[b1], TREE[b2], 0);
		logicMerge(TREE[b1], TMIR[b2], 0);
	}
	
	private final void nextLeaf(byte[] arr, byte[] mir, byte[] leaf)
	{
		System.arraycopy(arr, 0, leaf, 0, L);
		System.arraycopy(mir, 0, leaf, L, L);
		pisherYates(leaf);
	}
	
	private final void season(int branch)
	{
		STATE.next();
		for(int i = 0; i < (N << 1); i++)
			this.nextLeaf(TREE[branch], TMIR[branch], leaves[i]);
		int remaining = leaves.length;
		while(remaining > 1)
		{
			final int half = remaining >> 1;
			for(int i = 0; i < half; i++)
			{
				STATE.next();
				for(int j = 0; j < R; j++)
				{
					pisherYates(leaves[i]);
					pisherYates(leaves[i + half]);
					logic(leaves[i], j);
					logic(leaves[i + half], j);
				}
				logicMerge(leaves[i], leaves[i + half], i);
			}
			remaining >>= 1;
		}
		byte[] carbon = compact(leaves[0]);
		TREE[branch] = carbon;
		final byte[] mir = TMIR[branch];
		for(int i = 0; i < mir.length; i++)
			mir[i] = (byte) ~carbon[i];
	}
	
	private final byte[] compact(byte[] leaf)
	{
		int half = leaf.length >> 1;
		int lenper = half >> 3;
		byte prev = STATE.nextByte();
		LogicFunction func;
		for(int j = 0; j < 8; j++)
		{
			func = functions[(prev & 0xFF) % functions.length];
			for(int i = j * lenper; i < (j + 1) * lenper; i++)
			{
				prev = func.exec(leaf[i], leaf[i + half], prev);
				leaf[i] = prev;
				prev = Bits.rotateLeft(prev, prev & 7);
			}
		}
		STATE.update(prev);
		byte[] carbon = new byte[half];
		System.arraycopy(leaf, 0, carbon, 0, half);
		return carbon;
	}
	
	private final void logicMerge(byte[] b1, byte[] b2, int round)
	{
		int lenper = b1.length >> 3;
		byte prev = (byte) (STATE.nextByte() + round);
		LogicFunction func;
		for(int j = 0; j < 8; j++)
		{
			func = functions[(prev & 0xFF) % functions.length];
			for(int i = j * lenper; i < (j + 1) * lenper; i++)
			{
				prev = func.exec(b1[i], b2[i], prev);
				b1[i] = prev;
				prev = Bits.rotateLeft(prev, prev & 7);
			}
		}
		STATE.update(prev);
	}
	
	private final void logic(byte[] bytes, int round)
	{
		int lenper = bytes.length >> 3;
		byte register = 0;
		byte prev = (byte) (STATE.nextByte() + round);
		LogicFunction func;
		for(int j = 0; j < 8; j++)
		{
			func = functions[(prev & 0xFF) % functions.length];
			for(int i = j * lenper; i < (j + 1) * lenper; i++)
			{
				prev = func.exec(bytes[i], prev, register);
				bytes[i] = prev;
				register ^= Bits.rotateLeft(prev, prev & 7);
			}	
			register ^= (Bits.rotateLeft(prev, j) | register) + (Bits.rotateRight(register, register & 7) & ~prev);
			register = Bits.rotateRight(register, prev & 7);
		}
		STATE.update(prev);
	}
	
	@SuppressWarnings("unused")
	private static final void print(byte[] bytes)
	{
		System.out.println(new CString(Hex.toHex(bytes)));
	}
	
	private static final LogicFunction[] functions = new LogicFunction[] {
		
		new LogicFunction() {
			public byte exec(byte b1, byte b2, byte register)
			{
				return (byte) ((b1 | b2 + register) ^ (b1 & b2 - register));
			}
		},
		new LogicFunction() {
			public byte exec(byte b1, byte b2, byte register)
			{
				return (byte) ((byte) (b1 ^ b2) + (register - (~b1 ^ b2)));
			}
		},
		new LogicFunction() {
			public byte exec(byte b1, byte b2, byte register)
			{
				return (byte) ((byte) b1 + b2 + register);
			}
		},
		new LogicFunction() {
			public byte exec(byte b1, byte b2, byte register)
			{
				return (byte) ((byte) (b1 ^ b2) + ~register);
			}
		},
		new LogicFunction() {
			public byte exec(byte b1, byte b2, byte register)
			{
				return (byte) ((b1 ^ register) + (b2 ^ ~register));
			}
		},
		new LogicFunction() {
			public byte exec(byte b1, byte b2, byte register)
			{
				return (byte) ((b2 & register) ^ (b1 | register) + ~register);
			}
		},
		new LogicFunction() {
			public byte exec(byte b1, byte b2, byte register)
			{
				return (byte) ((b1 + register) ^ (b2 + register));
			}
		},
		new LogicFunction() {
			public byte exec(byte b1, byte b2, byte register)
			{
				return (byte) ((b2 + (b1 & register)) ^ (b1 | ~(b2 - register)));
			}
		},
		new LogicFunction() {
			public byte exec(byte b1, byte b2, byte register)
			{
				return (byte) ((~b2 - register) ^ (b1 & (b2 + ~register)));
			}
		}
		
	};
	
}
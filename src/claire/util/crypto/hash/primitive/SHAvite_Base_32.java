package claire.util.crypto.hash.primitive;

import java.util.Arrays;

import claire.util.math.counters.IntCounter;
import claire.util.memory.Bits;

abstract class SHAvite_Base_32 
	     extends SHAvite_Core {
	
	protected final int[] STATE = new int[8];
	
	private final int[] WORK = new int[144];
	private final IntCounter counter = new IntCounter(2);
	private final int[] counters = counter.getInts();

	public SHAvite_Base_32(int out) 
	{
		super(64, out);
		reset();
	}
	
	protected abstract int[] getIV();
	protected abstract void output(byte[] out, int start);
	
	private void reset()
	{
		counter.reset();
		System.arraycopy(this.getIV(), 0, STATE, 0, 8);
	}
	
	private static int subBytes(int A, int B, int C, int D)
	{
		return  SCUBE[0][ A 		& 0xFF]
			  ^ SCUBE[1][(B >>> 8 ) & 0xFF]
			  ^ SCUBE[2][(C >>> 16) & 0xFF]
			  ^ SCUBE[3][ D >>> 24		  ];
	}
	
	public void processNext(byte[] bytes, int offset)
	{
		this.processNext(bytes, offset, counters[0], counters[1], true);
	}
	
	public void processNext(byte[] bytes, int offset, int c1, int c2, boolean count)
	{
		if(count)
			counter.add(512);
		Bits.bytesToInts(bytes, offset, WORK, 0, 16);
		
		int A,
			B,
			C,
			D,
			E,
			F,
			G,
			H;
		
		int i1 = 0, i2 = 16, i3 = 12;
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];		
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++] ^  c1;
		WORK[i2++] = F ^ WORK[i3++] ^ ~c2;
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];	
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];		
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];	
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++];
		i3++;
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3  ];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];		
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];	
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];		
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++] ^ c2;
		WORK[i2++] = G ^ WORK[i3++] ^ ~c1;
		WORK[i2++] = H ^ WORK[i3++];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];	
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++];
		i3++;
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3  ];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];		
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];	
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++] ^  c2;
		WORK[i2++] = H ^ WORK[i3++] ^ ~c1;
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];		
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];	
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++];
		i3++;
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3  ];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];		
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];	
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];		
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++];
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++];
		D = WORK[i1++];
		A = WORK[i1++];
		B = WORK[i1++];
		C = WORK[i1++];	
		E = subBytes(A, B, C, D);
		F = subBytes(B, C, D, A);
		G = subBytes(C, D, A, B);
		H = subBytes(D, A, B, C);
		WORK[i2++] = E ^ WORK[i3++] ^  c1;
		WORK[i2++] = F ^ WORK[i3++];
		WORK[i2++] = G ^ WORK[i3++];
		WORK[i2++] = H ^ WORK[i3++] ^ ~c2;
		i3++;
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2++] = WORK[i1++] ^ WORK[i3++];
		WORK[i2  ] = WORK[i1  ] ^ WORK[i3  ];
		
		A = STATE[0];
		B = STATE[1];
		C = STATE[2];
		D = STATE[3];
		E = STATE[4];
		F = STATE[5];
		G = STATE[6];
		H = STATE[7];
		i2 = 0;
		int AA,
			BB,
			CC,
			DD,
			EE,
			FF,
			GG,
			HH;
		for(int i = 0; i < 6; i++)
		{
			AA = E ^ WORK[i2++];
			BB = F ^ WORK[i2++];
			CC = G ^ WORK[i2++];
			DD = H ^ WORK[i2++];
			EE = subBytes(AA, BB, CC, DD);
			FF = subBytes(BB, CC, DD, AA);
			GG = subBytes(CC, DD, AA, BB);
			HH = subBytes(DD, AA, BB, CC);
			AA = EE ^ WORK[i2++];
			BB = FF ^ WORK[i2++];
			CC = GG ^ WORK[i2++];
			DD = HH ^ WORK[i2++];
			EE = subBytes(AA, BB, CC, DD);
			FF = subBytes(BB, CC, DD, AA);
			GG = subBytes(CC, DD, AA, BB);
			HH = subBytes(DD, AA, BB, CC);
			AA = EE ^ WORK[i2++];
			BB = FF ^ WORK[i2++];
			CC = GG ^ WORK[i2++];
			DD = HH ^ WORK[i2++];
			EE = subBytes(AA, BB, CC, DD);
			FF = subBytes(BB, CC, DD, AA);
			GG = subBytes(CC, DD, AA, BB);
			HH = subBytes(DD, AA, BB, CC);
			A ^= EE;
			B ^= FF;
			C ^= GG;
			D ^= HH;
			
			AA = A ^ WORK[i2++];
			BB = B ^ WORK[i2++];
			CC = C ^ WORK[i2++];
			DD = D ^ WORK[i2++];
			EE = subBytes(AA, BB, CC, DD);
			FF = subBytes(BB, CC, DD, AA);
			GG = subBytes(CC, DD, AA, BB);
			HH = subBytes(DD, AA, BB, CC);
			AA = EE ^ WORK[i2++];
			BB = FF ^ WORK[i2++];
			CC = GG ^ WORK[i2++];
			DD = HH ^ WORK[i2++];
			EE = subBytes(AA, BB, CC, DD);
			FF = subBytes(BB, CC, DD, AA);
			GG = subBytes(CC, DD, AA, BB);
			HH = subBytes(DD, AA, BB, CC);
			AA = EE ^ WORK[i2++];
			BB = FF ^ WORK[i2++];
			CC = GG ^ WORK[i2++];
			DD = HH ^ WORK[i2++];
			EE = subBytes(AA, BB, CC, DD);
			FF = subBytes(BB, CC, DD, AA);
			GG = subBytes(CC, DD, AA, BB);
			HH = subBytes(DD, AA, BB, CC);
			E ^= EE;
			F ^= FF;
			G ^= GG;
			H ^= HH;
		}		
		STATE[0] ^= A;
		STATE[1] ^= B;
		STATE[2] ^= C;
		STATE[3] ^= D;
		STATE[4] ^= E;
		STATE[5] ^= F;
		STATE[6] ^= G;
		STATE[7] ^= H;
		//System.out.println(EncodingUtil.hexString(Bits.intsToBytes(STATE)));
	}
	
	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		counter.add(pos << 3);
		Arrays.fill(remaining, pos, 64, (byte) 0);
		remaining[pos] = (byte) 0x80;
		int c1 = counters[1],
			c2 = counters[0];
		if(pos == 0) 
			c1 = c2 = 0;
		
		if(pos >= 54)
		{
			processNext(remaining, 0, c1, c2, false);
			c1 = c2 = 0;
			Arrays.fill(remaining, (byte) 0);
		}
		System.out.println(c1 + " " + c2 + " " + pos);
		Bits.intToBytes(counters[1], remaining, 54);
		Bits.intToBytes(counters[0], remaining, 58);
		remaining[62] = (byte) (this.outputLength() << 3);
		remaining[63] = (byte) (this.outputLength() >>> 5);
		processNext(remaining, 0, c1, c2, false);
		output(out, start);
		reset();
	}

}

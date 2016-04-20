package claire.util.crypto.hash.primitive;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.hash.primitive.JHCore.JHState;
import claire.util.io.Factory;
import claire.util.math.counters.LongCounter;
import claire.util.memory.Bits;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

abstract class JHCore<Hash extends JHCore<Hash>>
		 extends MerkleHash<JHState, Hash> {
	
	private static final long[] K = 
	{
		0x72d5dea2df15f867L, 0x7b84150ab7231557L,
		0x81abd6904d5a87f6L, 0x4e9f4fc5c3d12b40L,
		0xea983ae05c45fa9cL, 0x03c5d29966b2999aL,
		0x660296b4f2bb538aL, 0xb556141a88dba231L,
		0x03a35a5c9a190edbL, 0x403fb20a87c14410L,
		0x1c051980849e951dL, 0x6f33ebad5ee7cddcL,
		0x10ba139202bf6b41L, 0xdc786515f7bb27d0L,
		0x0a2c813937aa7850L, 0x3f1abfd2410091d3L,
		0x422d5a0df6cc7e90L, 0xdd629f9c92c097ceL,
		0x185ca70bc72b44acL, 0xd1df65d663c6fc23L,
		0x976e6c039ee0b81aL, 0x2105457e446ceca8L,
		0xeef103bb5d8e61faL, 0xfd9697b294838197L,
		0x4a8e8537db03302fL, 0x2a678d2dfb9f6a95L,
		0x8afe7381f8b8696cL, 0x8ac77246c07f4214L,
		0xc5f4158fbdc75ec4L, 0x75446fa78f11bb80L,
		0x52de75b7aee488bcL, 0x82b8001e98a6a3f4L,
		0x8ef48f33a9a36315L, 0xaa5f5624d5b7f989L,
		0xb6f1ed207c5ae0fdL, 0x36cae95a06422c36L,
		0xce2935434efe983dL, 0x533af974739a4ba7L,
		0xd0f51f596f4e8186L, 0x0e9dad81afd85a9fL,
		0xa7050667ee34626aL, 0x8b0b28be6eb91727L,
		0x47740726c680103fL, 0xe0a07e6fc67e487bL,
		0x0d550aa54af8a4c0L, 0x91e3e79f978ef19eL,
		0x8676728150608dd4L, 0x7e9e5a41f3e5b062L,
		0xfc9f1fec4054207aL, 0xe3e41a00cef4c984L,
		0x4fd794f59dfa95d8L, 0x552e7e1124c354a5L,
		0x5bdf7228bdfe6e28L, 0x78f57fe20fa5c4b2L,
		0x05897cefee49d32eL, 0x447e9385eb28597fL,
		0x705f6937b324314aL, 0x5e8628f11dd6e465L,
		0xc71b770451b920e7L, 0x74fe43e823d4878aL,
		0x7d29e8a3927694f2L, 0xddcb7a099b30d9c1L,
		0x1d1b30fb5bdc1be0L, 0xda24494ff29c82bfL,
		0xa4e7ba31b470bfffL, 0x0d324405def8bc48L,
		0x3baefc3253bbd339L, 0x459fc3c1e0298ba0L,
		0xe5c905fdf7ae090fL, 0x947034124290f134L,
		0xa271b701e344ed95L, 0xe93b8e364f2f984aL,
		0x88401d63a06cf615L, 0x47c1444b8752afffL,
		0x7ebb4af1e20ac630L, 0x4670b6c5cc6e8ce6L,
		0xa4d5a456bd4fca00L, 0xda9d844bc83e18aeL,
		0x7357ce453064d1adL, 0xe8a6ce68145c2567L,
		0xa3da8cf2cb0ee116L, 0x33e906589a94999aL,
		0x1f60b220c26f847bL, 0xd1ceac7fa0d18518L,
		0x32595ba18ddd19d3L, 0x509a1cc0aaa5b446L,
		0x9f3d6367e4046bbaL, 0xf6ca19ab0b56ee7eL,
		0x1fb179eaa9282174L, 0xe9bdf7353b3651eeL,
		0x1d57ac5a7550d376L, 0x3a46c2fea37d7001L,
		0xf735c1af98a4d842L, 0x78edec209e6b6779L,
		0x41836315ea3adba8L, 0xfac33b4d32832c83L,
		0xa7403b1f1c2747f3L, 0x5940f034b72d769aL,
		0xe73e4e6cd2214ffdL, 0xb8fd8d39dc5759efL,
		0x8d9b0c492b49ebdaL, 0x5ba2d74968f3700dL,
		0x7d3baed07a8d5584L, 0xf5a5e9f0e4f88e65L,
		0xa0b8a2f436103b53L, 0x0ca8079e753eec5aL,
		0x9168949256e8884fL, 0x5bb05c55f8babc4cL,
		0xe3bb3b99f387947bL, 0x75daf4d6726b1c5dL,
		0x64aeac28dc34b36dL, 0x6c34a550b828db71L,
		0xf861e2f2108d512aL, 0xe3db643359dd75fcL,
		0x1cacbcf143ce3fa2L, 0x67bbd13c02e843b0L,
		0x330a5bca8829a175L, 0x7f34194db416535cL,
		0x923b94c30e794d1eL, 0x797475d7b6eeaf3fL,
		0xeaa8d4f7be1a3921L, 0x5cf47e094c232751L,
		0x26a32453ba323cd2L, 0x44a3174a6da6d5adL,
		0xb51d3ea6aff2c908L, 0x83593d98916b3c56L,
		0x4cf87ca17286604dL, 0x46e23ecc086ec7f6L,
		0x2f9833b3b1bc765eL, 0x2bd666a5efc4e62aL,
		0x06f4b6e8bec1d436L, 0x74ee8215bcef2163L,
		0xfdc14e0df453c969L, 0xa77d5ac406585826L,
		0x7ec1141606e0fa16L, 0x7e90af3d28639d3fL,
		0xd2c9f2e3009bd20cL, 0x5faace30b7d40c30L,
		0x742a5116f2e03298L, 0x0deb30d8e3cef89aL,
		0x4bc59e7bb5f17992L, 0xff51e66e048668d3L,
		0x9b234d57e6966731L, 0xcce6a6f3170a7505L,
		0xb17681d913326cceL, 0x3c175284f805a262L,
		0xf42bcbb378471547L, 0xff46548223936a48L,
		0x38df58074e5e6565L, 0xf2fc7c89fc86508eL,
		0x31702e44d00bca86L, 0xf04009a23078474eL,
		0x65a0ee39d1f73883L, 0xf75ee937e42c3abdL,
		0x2197b2260113f86fL, 0xa344edd1ef9fdee7L,
		0x8ba0df15762592d9L, 0x3c85f7f612dc42beL,
		0xd8a7ec7cab27b07eL, 0x538d7ddaaa3ea8deL,
		0xaa25ce93bd0269d8L, 0x5af643fd1a7308f9L,
		0xc05fefda174a19a5L, 0x974d66334cfd216aL,
		0x35b49831db411570L, 0xea1e0fbbedcd549bL,
		0x9ad063a151974072L, 0xf6759dbf91476fe2L
	};
	
	protected final long[] STATE = new long[16];
	
	private final LongCounter counter = new LongCounter(2);
	private final long[] counters = counter.getLongs();

	protected JHCore(int out) 
	{
		super(64, out);
		reset();
	}
	
	protected abstract long[] getIV();
	protected abstract void output(byte[] out, int start);
	
	public void reset()
	{
		super.reset();
		System.arraycopy(getIV(), 0, STATE, 0, 16);
		counter.reset();
	}
	
	private void F1(int round)
	{
		long A, 
			 B, 
			 C, 
			 D, 
			 RK, 
			 T;

		RK = K[(round << 2) + 0];
		A = STATE[ 0];
		B = STATE[ 4];
		C = STATE[ 8];
		D = STATE[12];	
		D = ~D;
		A ^= RK & ~C;
		T  = RK ^ (A & B);
		A ^= C & D;
		D ^= ~B & C;
		B ^= A & C;
		C ^= A & ~D;
		A ^= B | D;
		D ^= B & C;
		B ^= T & A;
		C ^= T;	
		STATE[ 0] = A;
		STATE[ 4] = B;
		STATE[ 8] = C;
		STATE[12] = D;

		RK = K[(round << 2) + 1];
		A = STATE[ 1];
		B = STATE[ 5];
		C = STATE[ 9];	
		D = STATE[13];
		D = ~D;
		A ^= RK & ~C;
		T  = RK ^ (A & B);
		A ^= C & D;
		D ^= ~B & C;
		B ^= A & C;
		C ^= A & ~D;
		A ^= B | D;
		D ^= B & C;
		B ^= T & A;
		C ^= T;	
		STATE[ 1] = A;
		STATE[ 5] = B;
		STATE[ 9] = C;
		STATE[13] = D;

		RK = K[(round << 2) + 2];
		A = STATE[ 2];
		B = STATE[ 6];
		C = STATE[10];
		D = STATE[14];
		D = ~D;
		A ^= RK & ~C;
		T  = RK ^ (A & B);
		A ^= C & D;
		D ^= ~B & C;
		B ^= A & C;
		C ^= A & ~D;
		A ^= B | D;
		D ^= B & C;
		B ^= T & A;
		C ^= T;
		STATE[ 2] = A;
		STATE[ 6] = B;
		STATE[10] = C;
		STATE[14] = D;

		RK = K[(round << 2) + 3];
		A = STATE[ 3];
		B = STATE[ 7];
		C = STATE[11];
		D = STATE[15];
		D = ~D;
		A ^= RK & ~C;
		T  = RK ^ (A & B);
		A ^= C & D;
		D ^= ~B & C;
		B ^= A & C;
		C ^= A & ~D;
		A ^= B | D;
		D ^= B & C;
		B ^= T & A;
		C ^= T;
		STATE[ 3] = A;
		STATE[ 7] = B;
		STATE[11] = C;
		STATE[15] = D;
	}
	
	private final void F2()
	{
		long A, 
			 B, 
			 C, 
			 D, 
			 E, 
			 F, 
			 G, 
			 H;
		
		A = STATE[ 0];
		B = STATE[ 4];
		C = STATE[ 8];
		D = STATE[12];
		E = STATE[ 2];
		F = STATE[ 6];
		G = STATE[10];
		H = STATE[14];
		E ^= B;
		F ^= C;
		G ^= D ^ A;
		H ^= A;
		A ^= F;
		B ^= G;
		C ^= H ^ E;
		D ^= E;
		STATE[ 0] = A;
		STATE[ 4] = B;
		STATE[ 8] = C;
		STATE[12] = D;
		STATE[ 2] = E;
		STATE[ 6] = F;
		STATE[10] = G;
		STATE[14] = H;

		A = STATE[ 1];
		B = STATE[ 5];
		C = STATE[ 9];
		D = STATE[13];
		E = STATE[ 3];
		F = STATE[ 7];
		G = STATE[11];
		H = STATE[15];
		E ^= B;
		F ^= C;
		G ^= D ^ A;
		H ^= A;
		A ^= F;
		B ^= G;
		C ^= H ^ E;
		D ^= E;
		STATE[ 1] = A;
		STATE[ 5] = B;
		STATE[ 9] = C;
		STATE[13] = D;
		STATE[ 3] = E;
		STATE[ 7] = F;
		STATE[11] = G;
		STATE[15] = H;
	}
	
	private final void F3_0(long mask, int rot)
	{
		STATE[ 2] = ((STATE[ 2] & mask) << rot) | ((STATE[ 2] >>> rot) & mask);
		STATE[ 3] = ((STATE[ 3] & mask) << rot) | ((STATE[ 3] >>> rot) & mask);
		STATE[ 6] = ((STATE[ 6] & mask) << rot) | ((STATE[ 6] >>> rot) & mask);
		STATE[ 7] = ((STATE[ 7] & mask) << rot) | ((STATE[ 7] >>> rot) & mask);
		STATE[10] = ((STATE[10] & mask) << rot) | ((STATE[10] >>> rot) & mask);
		STATE[11] = ((STATE[11] & mask) << rot) | ((STATE[11] >>> rot) & mask);
		STATE[14] = ((STATE[14] & mask) << rot) | ((STATE[14] >>> rot) & mask);
		STATE[15] = ((STATE[15] & mask) << rot) | ((STATE[15] >>> rot) & mask);
	}
	
	private final void F3_1()
	{
		long T;
		T = STATE[ 2]; STATE[ 2] = STATE[ 3]; STATE[ 3] = T;
		T = STATE[ 6]; STATE[ 6] = STATE[ 7]; STATE[ 7] = T;
		T = STATE[10]; STATE[10] = STATE[11]; STATE[11] = T;
		T = STATE[14]; STATE[14] = STATE[15]; STATE[15] = T;
	}
	
	public void processNext(byte[] bytes, int offset)
	{
		this.processNext(bytes, offset, true);
	}

	public void processNext(byte[] bytes, int offset, boolean count)
	{
		if(count)
			counter.add(512);
		long i1 = Bits.BigEndian.longFromBytes(bytes, offset     );
		long i2 = Bits.BigEndian.longFromBytes(bytes, offset +  8);
		long i3 = Bits.BigEndian.longFromBytes(bytes, offset + 16);
		long i4 = Bits.BigEndian.longFromBytes(bytes, offset + 24);
		long i5 = Bits.BigEndian.longFromBytes(bytes, offset + 32);
		long i6 = Bits.BigEndian.longFromBytes(bytes, offset + 40);
		long i7 = Bits.BigEndian.longFromBytes(bytes, offset + 48);
		long i8 = Bits.BigEndian.longFromBytes(bytes, offset + 56);
		STATE[0 ] ^= i1;
		STATE[1 ] ^= i2;
		STATE[2 ] ^= i3;
		STATE[3 ] ^= i4;
		STATE[4 ] ^= i5;
		STATE[5 ] ^= i6;
		STATE[6 ] ^= i7;
		STATE[7 ] ^= i8;
		for(int i = 0; i < 42;)
		{
			F1(i++);
			F2();
			F3_0(0x5555555555555555L,  1);
			F1(i++);
			F2();
			F3_0(0x3333333333333333L,  2);
			F1(i++);
			F2();
			F3_0(0x0F0F0F0F0F0F0F0FL,  4);
			F1(i++);
			F2();
			F3_0(0x00FF00FF00FF00FFL,  8);
			F1(i++);
			F2();
			F3_0(0x0000FFFF0000FFFFL, 16);
			F1(i++);
			F2();
			F3_0(0x00000000FFFFFFFFL, 32);
			F1(i++);
			F2();
			F3_1();
		}
		STATE[8 ] ^= i1;
		STATE[9 ] ^= i2;
		STATE[10] ^= i3;
		STATE[11] ^= i4;
		STATE[12] ^= i5;
		STATE[13] ^= i6;
		STATE[14] ^= i7;
		STATE[15] ^= i8;
	}

	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		counter.add(pos << 3);
		Arrays.fill(remaining, pos, 64, (byte) 0);
		remaining[pos] = (byte) 0x80;
		if(pos > 0)
		{
			processNext(remaining, 0, false);
			Arrays.fill(remaining, (byte) 0);
		}
		Bits.BigEndian.longToBytes(counters[0], remaining, 56);
		processNext(remaining, 0);
		output(out, start);
		reset();
	}
	
	public JHState getState()
	{
		return new JHState(this);
	}

	public void updateState(JHState state)
	{
		state.update(this);
	}

	public void loadCustom(JHState state)
	{
		System.arraycopy(state.counters, 0, this.counters, 0, 2);
		System.arraycopy(state.state, 0, this.STATE, 0, 16);
	}
	
	public static final JHStateFactory sfactory = new JHStateFactory();
	
	protected static final class JHState extends MerkleState<JHState, JHCore<? extends JHCore<?>>>
	{

		protected long[] state;
		protected long[] counters;
		
		public JHState(JHCore<? extends JHCore<?>> hash) 
		{
			super(hash);
		}
		
		public JHState(byte[] bytes, int pos)
		{
			super(bytes, pos);
		}

		public Factory<JHState> factory()
		{
			return sfactory;
		}
		
		public int stateID()
		{
			return IState.JH;
		}

		public int NAMESPACE()
		{
			return _NAMESPACE.JHSTATE;
		}

		protected void persistCustom(IOutgoingStream os) throws IOException
		{
			os.writeLongs(state);
			os.writeLongs(counters);
		}

		protected void persistCustom(byte[] bytes, int start)
		{
			Bits.longsToBytes(state, 0, bytes, start, 16); start += 128;
			Bits.longsToBytes(counters, 0, bytes, start, 2);
		}

		protected void addCustom(IIncomingStream is) throws IOException
		{
			state = is.readLongs(16);
			counters = is.readLongs(2);
		}
		
		protected void addCustom(byte[] bytes, int start)
		{
			state = new long[16];
			counters = new long[2];
			Bits.bytesToLongs(bytes, start, state, 0, 16); start += 128;
			Bits.bytesToLongs(bytes, start, counters, 0, 2);
		}

		protected void addCustom(JHCore<? extends JHCore<?>> hash)
		{
			state = ArrayUtil.copy(hash.STATE);
			counters = ArrayUtil.copy(hash.counters);
		}

		protected void updateCustom(JHCore<? extends JHCore<?>> hash)
		{
			System.arraycopy(this.state, 0, hash.STATE, 0, 16);
			System.arraycopy(this.counters, 0, hash.counters, 0, 2);
		}

		protected void eraseCustom()
		{
			Arrays.fill(state, 0);
			Arrays.fill(counters, 0);
			state = null;
			counters = null;
		}

		protected boolean compareCustom(JHState state)
		{	
			return ArrayUtil.equals(counters, state.counters) && ArrayUtil.equals(this.state, state.state);
		}

		protected int customSize()
		{
			return 144;
		}
		
	}
	
	protected static final class JHStateFactory extends MerkleStateFactory<JHState, JHCore<? extends JHCore<?>>>
	{

		protected JHStateFactory() 
		{
			super(JHState.class, 64);
		}

		protected JHState construct(byte[] bytes, int pos)
		{
			return new JHState(bytes, pos);
		}
		
	}

}

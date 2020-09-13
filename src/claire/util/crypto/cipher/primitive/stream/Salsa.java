package claire.util.crypto.cipher.primitive.stream;

import java.io.IOException;
import java.util.Arrays;

import claire.util.crypto.KeyFactory;
import claire.util.crypto.cipher.key.stream.KeySalsa;
import claire.util.crypto.cipher.primitive.stream.Salsa.SalsaState;
import claire.util.crypto.rng.RandUtils;
import claire.util.io.Factory;
import claire.util.memory.Bits;
import claire.util.memory.buffer.SimpleDeaggregator;
import claire.util.standards.IPersistable;
import claire.util.standards._NAMESPACE;
import claire.util.standards.crypto.IState;
import claire.util.standards.crypto.IStreamCipher;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class Salsa 
	   implements IStreamCipher<KeySalsa, SalsaState> {
	
	private static final int o1 = Bits.intFromBytes("expa".getBytes(), 0),
							 o2 = Bits.intFromBytes("nd 3".getBytes(), 0),
							 o3 = Bits.intFromBytes("2-by".getBytes(), 0),
							 o4 = Bits.intFromBytes("te k".getBytes(), 0),
							 t1 = Bits.intFromBytes("expa".getBytes(), 0),
							 t2 = Bits.intFromBytes("nd 1".getBytes(), 0),
							 t3 = Bits.intFromBytes("6-by".getBytes(), 0),
							 t4 = Bits.intFromBytes("te k".getBytes(), 0);
	
	private KeySalsa ref;
	
	private int non1, non2,
				pos1, pos2;
	
	private int[] hlf1, hlf2;
	private int[] buf = new int[16];
	
	private byte[] buf2 = new byte[4];
	private SimpleDeaggregator dea = new SimpleDeaggregator(buf2);
	int i, j;
	
	private int rounds;
	
	public Salsa() {}
	
	public Salsa(KeySalsa key)
	{
		this.setKey(key);
	}
	
	public KeySalsa getKey()
	{
		return ref;
	}

	public void setKey(KeySalsa t)
	{
		i = 16;
		j = 4;
		ref = t;
		rounds = t.getRounds() / 2;
		hlf1 = new int[6];
		hlf2 = new int[6];
		
		int[] raw = t.getInts();
		if(raw.length == 4) {
			hlf1[0] = t1;
			System.arraycopy(raw, 0, hlf1, 1, 4);
			hlf1[5] = t2;
			hlf2[0] = t3;
			System.arraycopy(raw, 0, hlf2, 1, 4);
			hlf2[5] = t4;
		} else {
			hlf1[0] = o1;
			System.arraycopy(raw, 0, hlf1, 1, 4);
			hlf1[5] = o2;
			hlf2[0] = o3;
			System.arraycopy(raw, 4, hlf2, 1, 4);
			hlf2[5] = o4;
		}

		doRound();
		
		non1 = buf[0];
		non2 = buf[1];
	}
	
	private void doRound()
	{
		/*
		 * 0, 1, 2, 3, 4, 5 <-- hlf1
		 * 6, 7 <-- non
		 * 8, 9 <-- pos
		 * 10, 11, 12, 13, 14, 15 <-- hlf2
		 */
		buf[ 4]  = Bits.rotateLeft(hlf1[0] + hlf2[2], 7 ) ^ hlf1[4]; buf[ 9]  = Bits.rotateLeft(hlf1[5] + hlf1[1], 7 ) ^ pos2;
		buf[14]  = Bits.rotateLeft(hlf2[0] +    non1, 7 ) ^ hlf2[4]; buf[ 3]  = Bits.rotateLeft(hlf2[5] + hlf2[2], 7 ) ^ hlf1[3];
		buf[ 8]  = Bits.rotateLeft(buf[ 4] + hlf1[0], 9 ) ^ pos1;    buf[13]  = Bits.rotateLeft(buf[ 9] + hlf1[5], 9 ) ^ hlf2[3];
		buf[ 2]  = Bits.rotateLeft(buf[14] + hlf2[0], 9 ) ^ hlf1[2]; buf[ 7]  = Bits.rotateLeft(buf[ 3] + hlf2[5], 9 ) ^ non2;
		buf[12]  = Bits.rotateLeft(buf[ 8] + buf[ 4], 13) ^ hlf2[2]; buf[ 1]  = Bits.rotateLeft(buf[13] + buf[ 9], 13) ^ hlf1[1];
		buf[ 6]  = Bits.rotateLeft(buf[ 2] + buf[14], 13) ^ non1;    buf[11]  = Bits.rotateLeft(buf[ 7] + buf[ 3], 13) ^ hlf2[1];
		buf[ 0]  = Bits.rotateLeft(buf[12] + buf[ 8], 18) ^ hlf1[0]; buf[ 5]  = Bits.rotateLeft(buf[ 1] + buf[13], 18) ^ hlf1[5];
		buf[10]  = Bits.rotateLeft(buf[ 6] + buf[ 2], 18) ^ hlf2[0]; buf[15]  = Bits.rotateLeft(buf[11] + buf[ 7], 18) ^ hlf2[5];

		buf[ 1] ^= Bits.rotateLeft(buf[ 0] + buf[ 3], 7 ); buf[ 6] ^= Bits.rotateLeft(buf[ 5] + buf[ 4], 7 );
		buf[11] ^= Bits.rotateLeft(buf[10] + buf[ 9], 7 ); buf[12] ^= Bits.rotateLeft(buf[15] + buf[14], 7 );
		buf[ 2] ^= Bits.rotateLeft(buf[ 1] + buf[ 0], 9 ); buf[ 7] ^= Bits.rotateLeft(buf[ 6] + buf[ 5], 9 );
		buf[ 8] ^= Bits.rotateLeft(buf[11] + buf[10], 9 ); buf[13] ^= Bits.rotateLeft(buf[12] + buf[15], 9 );
		buf[ 3] ^= Bits.rotateLeft(buf[ 2] + buf[ 1], 13); buf[ 4] ^= Bits.rotateLeft(buf[ 7] + buf[ 6], 13);
		buf[ 9] ^= Bits.rotateLeft(buf[ 8] + buf[11], 13); buf[14] ^= Bits.rotateLeft(buf[13] + buf[12], 13);
		buf[ 0] ^= Bits.rotateLeft(buf[ 3] + buf[ 2], 18); buf[ 5] ^= Bits.rotateLeft(buf[ 4] + buf[ 7], 18);
		buf[10] ^= Bits.rotateLeft(buf[ 9] + buf[ 8], 18); buf[15] ^= Bits.rotateLeft(buf[14] + buf[13], 18);
		
		for(int i = 1; i < rounds; i++) {
			buf[ 4] ^= Bits.rotateLeft(buf[ 0] + buf[12], 7 ); buf[ 9] ^= Bits.rotateLeft(buf[ 5] + buf[ 1], 7 );
			buf[14] ^= Bits.rotateLeft(buf[10] + buf[ 6], 7 ); buf[ 3] ^= Bits.rotateLeft(buf[15] + buf[11], 7 );
			buf[ 8] ^= Bits.rotateLeft(buf[ 4] + buf[ 0], 9 ); buf[13] ^= Bits.rotateLeft(buf[ 9] + buf[ 5], 9 );
			buf[ 2] ^= Bits.rotateLeft(buf[14] + buf[10], 9 ); buf[ 7] ^= Bits.rotateLeft(buf[ 3] + buf[15], 9 );
			buf[12] ^= Bits.rotateLeft(buf[ 8] + buf[ 4], 13); buf[ 1] ^= Bits.rotateLeft(buf[13] + buf[ 9], 13);
			buf[ 6] ^= Bits.rotateLeft(buf[ 2] + buf[14], 13); buf[11] ^= Bits.rotateLeft(buf[ 7] + buf[ 3], 13);
			buf[ 0] ^= Bits.rotateLeft(buf[12] + buf[ 8], 18); buf[ 5] ^= Bits.rotateLeft(buf[ 1] + buf[13], 18);
			buf[10] ^= Bits.rotateLeft(buf[ 6] + buf[ 2], 18); buf[15] ^= Bits.rotateLeft(buf[11] + buf[ 7], 18);
	
			buf[ 1] ^= Bits.rotateLeft(buf[ 0] + buf[ 3], 7 ); buf[ 6] ^= Bits.rotateLeft(buf[ 5] + buf[ 4], 7 );
			buf[11] ^= Bits.rotateLeft(buf[10] + buf[ 9], 7 ); buf[12] ^= Bits.rotateLeft(buf[15] + buf[14], 7 );
			buf[ 2] ^= Bits.rotateLeft(buf[ 1] + buf[ 0], 9 ); buf[ 7] ^= Bits.rotateLeft(buf[ 6] + buf[ 5], 9 );
			buf[ 8] ^= Bits.rotateLeft(buf[11] + buf[10], 9 ); buf[13] ^= Bits.rotateLeft(buf[12] + buf[15], 9 );
			buf[ 3] ^= Bits.rotateLeft(buf[ 2] + buf[ 1], 13); buf[ 4] ^= Bits.rotateLeft(buf[ 7] + buf[ 6], 13);
			buf[ 9] ^= Bits.rotateLeft(buf[ 8] + buf[11], 13); buf[14] ^= Bits.rotateLeft(buf[13] + buf[12], 13);
			buf[ 0] ^= Bits.rotateLeft(buf[ 3] + buf[ 2], 18); buf[ 5] ^= Bits.rotateLeft(buf[ 4] + buf[ 7], 18);
			buf[10] ^= Bits.rotateLeft(buf[ 9] + buf[ 8], 18); buf[15] ^= Bits.rotateLeft(buf[14] + buf[13], 18);
		}
	}

	public SalsaState getState()
	{
		SalsaState state = new SalsaState();
		state.i = (byte) i;
		state.j = (byte) j;
		state.non1 = non1;
		state.non2 = non2;
		state.pos1 = pos1;
		state.pos2 = pos2;
		return state;
	}

	public void loadState(SalsaState state)
	{
		i = state.i;
		j = state.j;
		non1 = state.non1;
		non2 = state.non2;
		pos1 = state.pos1;
		pos2 = state.pos2;
		doRound();
		dea.processInt(buf[i - 1]);
	}

	public void updateState(SalsaState state)
	{
		state.i = (byte) i;
		state.j = (byte) j;
		state.non1 = non1;
		state.non2 = non2;
		state.pos1 = pos1;
		state.pos2 = pos2;
	}

	public void reset()
	{
		pos1 = pos2 = 0;
		i = 16;
		j = 4;
	}

	public void wipe()
	{
		ref = null;
		Arrays.fill(hlf1, 0);
		Arrays.fill(hlf2, 0);
		Arrays.fill(buf, (byte) 0);
		Arrays.fill(buf2, (byte) 0);
		hlf1 = hlf2 = null;
		pos1 = pos2 = non1 = non2 = 0;
		i = 16;
		j = 4;
	}

	public byte nextByte()
	{
		if(j < 4)
			return buf2[j++];
		else if(i < 16) {
			dea.processInt(buf[i++]);
			j = 0;
			return buf2[j++];
		} else {
			doRound();
			i = 0;
			dea.processInt(buf[i++]);
			j = 0;
			return buf2[j++];
		}
	}

	public void fill(byte[] arr, int start, int len)
	{
		while(j < 4 && len-- > 0)
			arr[start++] = buf2[j++];
		
		if(len > 4) {
			if(i < 16) {
				int amt = len / 4;
				if(amt > (16 - i))
					amt = i;
				Bits.intsToBytes(buf, i, arr, start, amt); start += amt * 4; len -= amt * 4;
				i += amt;
			}
			if(len > 64) {
				int amt = len / 64;
				while(amt-- > 0) {
					doRound();
					Bits.intsToBytes(buf, 0, arr, start, 16); 
					start += 64;
				}
				len -= amt * 64;
			}
			if(len > 4) {
				int amt = len / 4;
				doRound(); i = 0;
				Bits.intsToBytes(buf, i, arr, start, amt); start += amt * 4; len -= amt * 4;
				i += amt;
			}
			dea.processInt(buf[i++]);
			j = 0;
			while(j < 4 && len-- > 0)
				arr[start++] = buf2[j++];
		} else {
			if(i < 16) {
				dea.processInt(buf[i++]);
				j = 0;
				while(j < 4 && len-- > 0)
					arr[start++] = buf2[j++];
			} else {
				doRound();
				i = 0;
				dea.processInt(buf[i++]);
				j = 0;
				while(j < 4 && len-- > 0)
					arr[start++] = buf2[j++];
			}
		}
	}
	
	public static final class SalsaState 
						implements IState<SalsaState>
	{
		private int non1, non2,
					pos1, pos2;
		
		private byte i, j;
		
		public SalsaState() {}
		
		public SalsaState(int non1, int non2, int pos1, int pos2, byte i, byte j)
		{
			this.non1 = non1;
			this.non2 = non2;
			this.pos1 = pos1;
			this.pos2 = pos2;
			this.i = i;
			this.j = j;
		}

		public void export(IOutgoingStream stream) throws IOException
		{
			stream.writeByte(i);
			stream.writeByte(j);
			stream.writeInt(non1);
			stream.writeInt(non2);
			stream.writeInt(pos1);
			stream.writeInt(pos2);
		}

		public void export(byte[] bytes, int offset)
		{
			bytes[offset++] = i;
			bytes[offset++] = j;
			Bits.intToBytes(non1, bytes, offset     );
			Bits.intToBytes(non2, bytes, offset + 4 );
			Bits.intToBytes(pos1, bytes, offset + 8 );
			Bits.intToBytes(pos2, bytes, offset + 12);
		}

		public int exportSize()
		{
			return 18;
		}
		
		public int NAMESPACE()
		{
			return _NAMESPACE.SALSASTATE;
		}

		public boolean sameAs(SalsaState obj)
		{
			return (obj.i == i && obj.j == j) && ((obj.pos1 == pos1 && obj.pos2 == pos2) && (obj.non1 == non1 && obj.non2 == non2));
		}

		public void erase()
		{
			non1 = non2 = pos1 = pos2 = i = j = 0;
		}

		public int stateID()
		{
			return IState.SALSA;
		}
		
		public static final SalsaStateFactory factory = new SalsaStateFactory();
		
		public Factory<SalsaState> factory()
		{
			return factory;
		}

		public static final class SalsaStateFactory
							extends Factory<SalsaState>
		{

			protected SalsaStateFactory()
			{
				super(SalsaState.class);
			}

			public SalsaState resurrect(byte[] data, int start) throws InstantiationException
			{
				SalsaState state = new SalsaState();
				state.i = data[start++];
				state.j = data[start++];
				state.non1 = Bits.intFromBytes(data, start     );
				state.non2 = Bits.intFromBytes(data, start + 4 );
				state.pos1 = Bits.intFromBytes(data, start + 8 );
				state.pos2 = Bits.intFromBytes(data, start + 12);
				return state;
			}

			public SalsaState resurrect(IIncomingStream stream) throws InstantiationException, IOException
			{
				SalsaState state = new SalsaState();
				state.i = stream.readByte();
				state.j = stream.readByte();
				state.non1 = stream.readInt();
				state.non2 = stream.readInt();
				state.pos1 = stream.readInt();
				state.pos2 = stream.readInt();
				return state;
			}
			
		}
		
	}
	
	public static final int test()
	{
		final int[] bytes = new int[32];
		RandUtils.fillArr(bytes);
		final Salsa rc4 = new Salsa(new KeySalsa(bytes, 20));
		int e = 0;
		e += IStreamCipher.testCipher(rc4);
		return e;
	}
	
	public static final int testState()
	{
		final int[] bytes = new int[256];
		RandUtils.fillArr(bytes);
		final SalsaState state = new SalsaState(RandUtils.dprng.readInt(), RandUtils.dprng.readInt(), RandUtils.dprng.readInt(), RandUtils.dprng.readInt(), RandUtils.dprng.readByte(), RandUtils.dprng.readByte());
		return IPersistable.test(state);
	}

	public KeyFactory<KeySalsa> keyFactory()
	{
		return KeySalsa.factory;
	}
	
}

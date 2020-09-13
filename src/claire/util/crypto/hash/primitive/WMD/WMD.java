package claire.util.crypto.hash.primitive.WMD;

import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.hash.primitive.MerkleHash;
import claire.util.crypto.hash.primitive.MerkleHash.MerkleState;
import claire.util.crypto.hash.primitive.WMD.WMD.WMDState;

public class WMD 
	   extends MerkleHash<WMDState, WMD> {
	
	private static short[] DSBOX = new short[65536];
	
	static {
		WMD_SBOX.init(DSBOX);
	}
	
	private static short[] SBOX = new short[65536];
	
	private static int[] state;
	
	private final int L;
	
	protected WMD(int N, int R, int L)
	{
		super(L / 8, L / 8);
		if((L & 31) != 0) 
			throw new java.lang.IllegalArgumentException("Length must be divisible by 32");
		this.L = L / 32;
		state = new int[L * R * N];
		this.reinit();
	}
	
	public int hashID()
	{
		return Hash.WMD;
	}

	private void reinit()
	{
		System.arraycopy(DSBOX, 0, SBOX, 0, 65536);
	}
	
	public void updateState(WMDState state)
	{
		// TODO Auto-generated method stub
		
	}

	public void processNext(byte[] bytes, int offset)
	{
		// TODO Auto-generated method stub
		
	}

	public void finalize(byte[] remaining, int pos, byte[] out, int start)
	{
		// TODO Auto-generated method stub
		
	}
	
	public HashFactory<WMD> factory()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public WMDState getState()
	{
		// TODO Auto-generated method stub
		return null;
	}


	public void loadCustom(WMDState state)
	{
		// TODO Auto-generated method stub
		
	}

	public static abstract class WMDState extends MerkleState<WMDState, WMD>
	{

		public WMDState(byte[] bytes, int pos) {
			super(bytes, pos);
		}
		
	}
	
	public abstract class WMDFunc 
	{
		
	}

	@Override
	public String genString(char sep)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}

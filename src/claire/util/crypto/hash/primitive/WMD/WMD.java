package claire.util.crypto.hash.primitive.WMD;

import claire.util.crypto.hash.Hash;
import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.hash.primitive.MerkleHash;
import claire.util.crypto.hash.primitive.WMD.WMD.WMDState;

public class WMD 
	   extends MerkleHash<WMDState, WMD> {

	private final int L,
					  R,
					  F;
	
	private final int[] STATE;
	
	/**
	 * L: The output size of the hash in 32-bit integers
	 * 
	 * N: Amount of L-sized chunks to make the state out of.
	 * 
	 * R: The amount of rounds per input
	 * 
	 * F: The amount of final rounds
	 */
	protected WMD(int L, int N, int R, int F)
	{
		super(L * 4, L * 4);
		STATE = new int[L * N];
		this.L = L;
		this.R = R;
		this.F = F;
	}
	
	public int hashID()
	{
		return Hash.WMD;
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
	
}

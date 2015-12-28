package claire.util.crypto.hash.primitive;

import claire.util.memory.Bits;

public class SHAvite224 
	   extends SHAvite_Base_32 {
	
	private static final int[] IV = 
	{
		0x6774F31C, 0x990AE210, 0xC87D4274, 0xC9546371,
		0x62B2AEA8, 0x4B5801D8, 0x1B702860, 0x842F3017
	};

	public SHAvite224()
	{
		super(28);
	}

	protected int[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.intsToBytes(STATE, 0, out, start, 7);
	}

}

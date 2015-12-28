package claire.util.crypto.hash.primitive;

import claire.util.memory.Bits;

public class BMW256 extends BMW_Base_32 {
	
	private static final int[] IV = 
	{
		0x40414243, 0x44454647, 0x48494a4b, 0x4c4d4e4f,
		0x50515253, 0x54555657, 0x58595a5b, 0x5c5d5e5f,
		0x60616263, 0x64656667, 0x68696a6b, 0x6c6d6e6f,
		0x70717273, 0x74757677, 0x78797a7b, 0x7c7d7e7f
	};

	public BMW256() 
	{
		super(32);
	}

	protected int[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		Bits.intsToBytes(STATE, 8, out, start, 8);
	}

}

package claire.util.crypto.hash.primitive;

import claire.util.memory.Bits;

public class JH256 
	   extends JHCore {
	
	private static final long[] IV = 
	{
		0xeb98a3412c20d3ebL, 0x92cdbe7b9cb245c1L,
		0x1c93519160d4c7faL, 0x260082d67e508a03L,
		0xa4239e267726b945L, 0xe0fb1a48d41a9477L,
		0xcdb5ab26026b177aL, 0x56f024420fff2fa8L,
		0x71a396897f2e4d75L, 0x1d144908f77de262L,
		0x277695f776248f94L, 0x87d5b6574780296cL,
		0x5c5e272dac8e0d6cL, 0x518450c657057a0fL,
		0x7be4d367702412eaL, 0x89e3ab13d31cd769L
	};

	public JH256()
	{
		super(32);
	}

	protected long[] getIV()
	{
		return IV;
	}

	protected void output(byte[] out, int start)
	{
		//System.out.println(EncodingUtil.hexString(Bits.BigEndian.longsToBytes(STATE)));
		Bits.BigEndian.longsToBytes(STATE, 12, out, start, 4);
	}

}

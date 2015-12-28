package claire.util.crypto.rng.primitive;

import java.util.Random;

import claire.util.standards.IRandom;

public class JRandom extends Random implements IRandom {
	
	private static final long serialVersionUID = 1L;

	public JRandom(int seed) {
		super(seed);
	}

	public JRandom() {
		super();
	}

	public byte nextByte()
	{
		return (byte) this.nextInt();
	}

	public short nextShort()
	{
		return (short) this.nextInt();
	}

}

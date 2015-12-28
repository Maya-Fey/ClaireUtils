package claire.util.crypto.hash.primitive;

final class CubeHashDouble 
   	  extends CubeHashBase {
	
	private final int[] IV;
	
	private final int rounds,
					  close;

	public CubeHashDouble(int block, int out, int rounds, int close, int[] IV) 
	{
		super(block, out);
		this.IV = IV;
		this.rounds = rounds >> 1;
		this.close = close >> 1;
		init();
	}

	protected void init()
	{
		System.arraycopy(IV, 0, STATE, 0, 32);
	}

	protected void process()
	{
		for(int i = 0; i < rounds; i++)
			doubleRound(STATE);
	}

	protected void last()
	{
		for(int i = 0; i < close; i++)
			doubleRound(STATE);
	}

}

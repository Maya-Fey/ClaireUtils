package claire.util.crypto.hash.primitive;

final class CubeHashSingle 
   	  extends CubeHashBase {
	
	private final int[] IV;
	private final int[] SCRATCH;
	
	private final int round,
				      close;
	
	private final boolean sRound,
						  sClose;

	public CubeHashSingle(int block, int out, int rounds, int close, int[] IV, int[] SCRATCH) 
	{
		super(block, out);
		this.IV = IV;
		this.SCRATCH = SCRATCH;
		this.round = rounds >> 1;
		this.close = close >> 1;
		this.sRound = (rounds & 1) == 1;
		this.sClose = (close & 1) == 1;
		init();
	}

	protected void init()
	{
		System.arraycopy(IV, 0, STATE, 0, 32);
	}

	protected void process()
	{
		for(int i = 0; i < round; i++)
			doubleRound(STATE);
		if(sRound)
			singleRound(STATE, SCRATCH);
	}
	
	protected void last()
	{
		for(int i = 0; i < close; i++)
			doubleRound(STATE);
		if(sClose)
			singleRound(STATE, SCRATCH);
	}

}

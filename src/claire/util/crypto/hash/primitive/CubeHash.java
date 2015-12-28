package claire.util.crypto.hash.primitive;

import claire.util.standards.crypto.IHash;

public class CubeHash implements IHash {
	
	private final CubeHashBase cube;
	
	public CubeHash(int init, int rounds, int block, int close, int out)
	{
		int[] IV = new int[32];
		if(((rounds & 1) + (close & 1)) != 0) {
			int[] SCRATCH = new int[8];
			CubeHashBase.genIV(IV, SCRATCH, out, block, rounds, init);
			this.cube = new CubeHashSingle(block, out, rounds, close, IV, SCRATCH);
		} else {
			int[] SCRATCH = null;
			if((init & 1) == 1)
				SCRATCH = new int[8];
			CubeHashBase.genIV(IV, SCRATCH, out, block, rounds, init);
			this.cube = new CubeHashDouble(block, out, rounds, close, IV);
		}
	}

	public void add(byte[] bytes, int start, int length)
	{
		cube.add(bytes, start, length);
	}

	public void finish(byte[] out, int start)
	{
		cube.finish(out, start);
	}

	public int outputLength()
	{
		return cube.outputLength();
	}

}

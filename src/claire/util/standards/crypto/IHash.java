package claire.util.standards.crypto;

import claire.util.crypto.hash.HashFactory;
import claire.util.crypto.rng.RandUtils;
import claire.util.logging.Log;
import claire.util.memory.util.ArrayUtil;
import claire.util.standards.IStateMachine;

public interface IHash<Hash extends IHash<Hash, State>, State extends IState<State>> 
	   extends IStateMachine<State> {
	
	void add(byte[] bytes, int start, int length);
	
	void finish(byte[] out, int start);
	
	int outputLength();
	int desiredInputLength();
	
	HashFactory<Hash> factory();
	
	default void add(byte[] bytes)
	{
		add(bytes, 0, bytes.length);
	}

	default void finish(byte[] out)
	{
		finish(out, 0);
	}
	
	default byte[] finish()
	{
		byte[] bytes = new byte[this.outputLength()];
		finish(bytes, 0);
		return bytes;
	}
	
	default void hash(byte[] in, byte[] out)
	{
		hash(in, 0, out, 0);
	}
	
	default void hash(byte[] in, int start0, byte[] out, int start1)
	{
		this.add(in, start0, in.length);
		this.finish(out, start1);
	}
	
	default void hash(byte[] in, int start0, int length, byte[] out, int start1)
	{
		this.add(in, start0, length);
		this.finish(out, start1);
	}
	
	default byte[] hash(byte[] in, int start)
	{
		byte[] out = new byte[this.outputLength()];
		hash(in, start, out, 0);
		return out;
	}
	
	default byte[] hash(byte[] in)
	{
		byte[] out = new byte[this.outputLength()];
		hash(in, 0, out, 0);
		return out;
	}
	
	public static <State extends IState<State>>int test(IHash<?, State> hash)
	{
		int e = 0;
		try {
			byte[] bytes = new byte[500];
			byte[] h1 = new byte[hash.outputLength()];
			byte[] h2 = new byte[hash.outputLength()];
			RandUtils.fillArr(bytes);
			hash.add(bytes);
			hash.finish(h1, 0);
			hash.add(bytes);
			hash.finish(h2, 0);
			if(!ArrayUtil.equals(h1, h2)) {
				Log.err.println("Finish did not properly reset the hash.");
				e++;
			}
			hash.add(bytes);
			hash.reset();
			hash.add(bytes);
			hash.finish(h2, 0);
			if(!ArrayUtil.equals(h1, h2)) {
				Log.err.println("Reset did not properly reset the hash.");
				e++;
			}
			hash.add(bytes);
			State state = hash.getState();
			hash.add(bytes);
			hash.finish(h1, 0);
			hash.loadState(state);
			hash.add(bytes);
			hash.finish(h2, 0);
			if(!ArrayUtil.equals(h1, h2)) {
				Log.err.println("State does not properly load");
				e++;
			}
			hash.add(bytes); hash.add(bytes);
			hash.updateState(state);
			hash.add(bytes);
			hash.finish(h1, 0);
			hash.loadState(state);
			hash.add(bytes);
			hash.finish(h2, 0);
			if(!ArrayUtil.equals(h1, h2)) {
				Log.err.println("State does not properly update");
				e++;
			}
		} catch(Exception ex) {
			Log.err.println("An unexpected " + ex.getClass().getSimpleName() + ": " + ex.getMessage() + " occured while testing  " + hash.getClass().getSimpleName());
			ex.printStackTrace();
			return e + 1;
		}
		return e;
	}
	
}

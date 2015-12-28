package claire.util.standards;

public interface IArray<T> {
	
	/**
	 * Simple array set, sets position at index <b>pos</b> to <b>obj</b>
	 * 
	 * @param pos
	 * @param obj
	 */
	public void set(int pos, T obj);
	
	/**
	 * Simple array get. Uses index.
	 * 
	 * @param pos
	 * @return
	 */
	public T get(int pos);

}

package claire.util.standards;

public interface ICache<T> {
	
	public T get(int i);
	public void add(T t, int i);	

}

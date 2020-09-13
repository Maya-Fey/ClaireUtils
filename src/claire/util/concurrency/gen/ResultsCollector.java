package claire.util.concurrency.gen;

public class ResultsCollector<Type> {
	
	private final Type[] arr;
	
	private volatile int pos;
	
	public ResultsCollector(Type[] arr, int pos)
	{
		this.pos = pos;
		this.arr = arr;
	}
	
	public synchronized void addResult(Type t)
	{
		if(pos == arr.length)
			return;
		arr[pos++] = t;
	}

}

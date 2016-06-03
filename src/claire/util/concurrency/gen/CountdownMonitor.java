package claire.util.concurrency.gen;

public class CountdownMonitor 
	   extends TaskMonitor {
	
	private int i;
	
	public CountdownMonitor(int from)
	{
		i = from;
	}

	public boolean isDone()
	{
		return i < 1;
	}
	
	public void onProgress()
	{
		i--;
	}
	
	public int remaining()
	{
		return i;
	}

}

package claire.util.concurrency;

public class TaskMonitor {
	
	boolean[] status;
	
	public TaskMonitor(int threads)
	{
		status = new boolean[threads];
	}
	
	public void setCompleted(int thread)
	{
		status[thread] = true;
	}
	
	public boolean isComplete()
	{
		for(int i = 0; i < status.length; i++)
			if(!status[i])
				return false;
		return true;
	}

}

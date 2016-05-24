package claire.util.concurrency;

public abstract class TaskMonitor {
	
	public abstract boolean isDone();
	public abstract void onProgress();
	
	public void notifyProgress()
	{
		this.onProgress();
		if(this.isDone())
			synchronized(this) {
				this.notify();
			}
	}

}

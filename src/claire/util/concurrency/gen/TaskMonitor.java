package claire.util.concurrency.gen;

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
	
	public void waitOn()
	{		
		synchronized(this) {
			while(!isDone())
				try {
					this.wait();
				} catch (InterruptedException e) {}
		}
	}

}

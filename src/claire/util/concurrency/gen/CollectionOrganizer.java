package claire.util.concurrency.gen;

import claire.util.concurrency.Basket;

public class CollectionOrganizer<Type> 
	   extends TaskMonitor {
	
	private final Basket<Type> basket;
	private final GeneratorCollectorThread<Type>[] gens;
	
	public CollectionOrganizer(Basket<Type> basket, GeneratorCollectorThread<Type>[] gens)
	{
		this.basket = basket;
		this.gens = gens;
	}
	
	public boolean isDone()
	{
		return !basket.hasSpace();
	}

	public void onProgress() {}
	
	public void notifyProgress()
	{
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
		for(GeneratorCollectorThread<Type> g : gens)
			g.interrupt();
	}

}

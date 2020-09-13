package claire.util.concurrency.gen;

import claire.util.concurrency.Basket;
import claire.util.standards.IGenerator;

public class GeneratorCollectorThread<Type> 
	   extends Thread {
	
	private final IGenerator<Type> gen;
	private final TaskMonitor mon;
	private final Basket<Type> bask;
	
	public GeneratorCollectorThread(IGenerator<Type> gen, TaskMonitor mon, Basket<Type> basket)
	{
		this.gen = gen;
		this.mon = mon;
		this.bask = basket;
	}
			
	public void run()
	{
		while(bask.hasSpace())
		{
			bask.drop(gen.generate(mon));
			mon.notifyProgress();
		}
	}

}

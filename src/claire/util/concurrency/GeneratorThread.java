package claire.util.concurrency;

import claire.util.standards.IGenerator;

public class GeneratorThread<Type> 
	   extends Thread {
	
	private IGenerator<Type> gen;
	private TaskMonitor mon;
	private Type fruit;
	
	public GeneratorThread(IGenerator<Type> gen, TaskMonitor mon)
	{
		this.gen = gen;
		this.mon = mon;
	}
			
	public void run()
	{
		fruit = gen.generate();
		mon.notifyProgress();
	}
	
	public Type harvest()
	{
		return fruit;
	}
	
	public boolean harvested()
	{
		return fruit != null;
	}

}

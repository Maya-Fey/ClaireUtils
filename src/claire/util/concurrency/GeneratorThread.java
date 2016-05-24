package claire.util.concurrency;

import claire.util.standards.IGenerator;

public class GeneratorThread<Type> 
	   extends Thread {
	
	private IGenerator<Type> gen;
	private Type fruit;
	
	public GeneratorThread(IGenerator<Type> gen)
	{
		this.gen = gen;
	}
			
	public void run()
	{
		fruit = gen.generate();
		synchronized(gen) {
			gen.notify();
		}
	}
	
	public Type harvest()
	{
		return fruit;
	}

}

package claire.util.concurrency.gen;

import claire.util.standards.IGenerator;

public class GeneratorThread<Type> 
	   extends Thread {
	
	private IGenerator<Type> gen;
	private ResultsCollector<Type> results;
	private TaskMonitor mon;
	
	public GeneratorThread(IGenerator<Type> gen, ResultsCollector<Type> collect, TaskMonitor mon)
	{
		this.gen = gen;
		this.mon = mon;
		this.results = collect;
	}
			
	public void run()
	{
		while(!mon.isDone()) {
			Type t = gen.generate(mon);
			if(mon.isDone()) 
				return;
			results.addResult(t);
			mon.notifyProgress();
		}
	}

}

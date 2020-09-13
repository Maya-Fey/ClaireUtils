package claire.util.standards;

import claire.util.concurrency.gen.TaskMonitor;

public interface IGenerator<Type> {

	Type generate(TaskMonitor mon);
	
}

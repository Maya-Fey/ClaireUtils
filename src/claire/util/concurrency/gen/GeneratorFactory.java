package claire.util.concurrency.gen;

import claire.util.standards.IGenerator;

public abstract class GeneratorFactory<Generator extends IGenerator<?>>
{
	public abstract Generator generate();
}

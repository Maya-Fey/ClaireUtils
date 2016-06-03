package claire.util.concurrency.gen;

import claire.util.standards.IGenerator;

public abstract class GeneratorFactory<Type>
{
	public abstract IGenerator<Type> generate();
}

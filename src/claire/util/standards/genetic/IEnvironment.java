package claire.util.standards.genetic;

public interface IEnvironment {
	
	public void cull();
	public void theNextGeneration();
	public void printPool();
	
	public static void simulate(IEnvironment startrek, int iterations)
	{
		for(int i = 0; i < iterations; i++) {
			startrek.cull();
			startrek.theNextGeneration();
		}
	}
	
}

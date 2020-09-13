package claire.util.standards.genetic;

import claire.util.memory.array.IJArray;
import claire.util.memory.sort.CSorter;
import claire.util.memory.sort.Getter;
import claire.util.memory.util.Pair;

public class Pool implements IEnvironment {
	
	IJArray<Pair<Long, Integer>> last;
	final IJArray<ICompetitor> competitors = new IJArray<ICompetitor>(ICompetitor.class, 100);
	static final Pair<Long, Integer> lol = new Pair<Long, Integer>(0L, 0);
	final CSorter<Pair<Long, Integer>> sorter = new CSorter<Pair<Long, Integer>>(new Getter<Long, Pair<Long, Integer>>() {

		public Long get(Pair<Long, Integer> e)
		{
			return e.getObject();
		}
		
	});
	
	public Pool(ICompetitor seed) {
		TestThread.init(this.competitors);
		this.begin(seed);
	}
	
	public void begin(ICompetitor seed)
	{
		competitors.set(0, seed);
		for(int i = 1; i < competitors.size(); i++)
			competitors.set(i, seed.getOffspring());
	}

	@SuppressWarnings("unchecked")
	public void cull()
	{
		IJArray<Pair<Long, Integer>> scores = new IJArray<Pair<Long, Integer>>((Class<Pair<Long, Integer>>) lol.getClass(), 100);
		TestThread.process();
		long[] raw = TestThread.getResults();
		for(int i = 0; i < 100; i++)
			scores.set(i, new Pair<Long, Integer>(raw[i], i));
		IJArray<Pair<Long, Integer>> sorted = sorter.sort(scores);
		last = sorted;
		for(int i = 0; i < 50; i++)
			competitors.get(sorted.get(i).getPair()).age();
		for(int i = 50; i < sorted.size(); i++)
			competitors.get(sorted.get(i).getPair()).setDead();
		//System.out.println("Round ended, round winner: ");
		//((Competitor) competitors.get(sorted.get(0).getPair())).print();
	}

	/**
	 * Data approves
	 */
	public void theNextGeneration()
	{
		int ii = 0;
		for(int i = 0; i < competitors.size(); i++)
			if(competitors.get(i).isDead()) {
				competitors.set(i, competitors.get(last.get(ii).getPair()).getOffspring());
				ii++;
			}
	}
	
	public void printPool()
	{
		Pair<Long, Integer>[] arr = last.getArray();
		for(int i = 0; i < 50; i++)
		{
			System.out.println("+============================================================+");
			System.out.println("Competitor " + arr[i].getPair() + " survived with a score of " + arr[i].getObject());
			competitors.get(arr[i].getPair()).print();
		}
		for(int i = 50; i < 100; i++)
		{
			System.out.println("+============================================================+");
			System.out.println("Competitor " + arr[i].getPair() + " died with a score of " + arr[i].getObject());
			competitors.get(arr[i].getPair()).print();
		}
	}
	
}

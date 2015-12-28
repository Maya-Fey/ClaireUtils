package claire.util.standards.genetic;

import java.util.concurrent.CountDownLatch;

import claire.util.memory.array.CArray;

public class TestThread extends Thread {
	
	private static final TestThread[] threads = new TestThread[2];
	private static CArray<ICompetitor> array;
	private static final long[] results = new long[100];
	private final int[] range;
	private boolean go = true;
	
	private static CountDownLatch monitor;
	
	public void exit()
	{
		this.go = false;
	}
	
	public TestThread(int min, int max)
	{
		range = new int[] { min, max };
	}
	
	public void run()
	{
		try {
			synchronized (this) {
				this.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while(go)
		{
			for(int i = range[0]; i < range[1]; i++) 
				results[i] = array.get(i).getScore();
			monitor.countDown();
			try {
				synchronized(this) {
					this.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void init(CArray<ICompetitor> array)
	{
		TestThread.array = array;
		monitor = new CountDownLatch(2);
		for(int i = 0; i < 2; i++)
		{
			threads[i] = new TestThread(i * 50, (i + 1) * 50);
			threads[i].start();
		}
	}
	
	public static void process()
	{
		for(Thread t : threads)
			synchronized(t) {
				t.notify();
			}
	}
	
	public static long[] getResults()
	{
		try {
			monitor.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	@SuppressWarnings("deprecation")
	public static void terminate()
	{
		for(TestThread t : threads) {
			synchronized(t) {
				t.exit();
				t.notify();
				t.stop();
			}
		}
	}
	
}

package claire.util.logging;

import claire.util.standards.io.ILogManager;
import claire.util.standards.io.ILogger;

public class StandardOut 
	   implements ILogManager {

	private final ILogger info = new INFO();
	private final ILogger warn = new WARN();
	private final ILogger error = new ERROR();
	private final ILogger crit = new CRITICAL();
	
	public ILogger getInfo()
	{
		return info;
	}

	public ILogger getWarning()
	{
		return warn;
	}

	public ILogger getError()
	{
		return error;
	}

	public ILogger getCritical()
	{
		return crit;
	}
	
	private static final class INFO
						 extends BasicLogger {
		
		private boolean fresh = true;
			
		public void println()
		{
			fresh = true;
			System.out.println();
		}
		
		protected void add(char ch)
		{
			if(fresh)
				System.out.print("[INFO] ");
			fresh ^= true;
			System.out.print(ch);
		}

		protected void add(char[] chars)
		{
			if(fresh)
				System.out.print("[INFO] ");
			fresh ^= true;
			System.out.print(chars);
		}
		
	}
	
	private static final class WARN
	 					 extends BasicLogger {

		private boolean fresh = true;

		public void println()
		{
			fresh = true;
			System.out.println();
		}

		protected void add(char ch)
		{
			if(fresh)
				System.out.print("[WARNING] ");
			fresh ^= true;
			System.out.print(ch);
		}

		protected void add(char[] chars)
		{
			if(fresh)
				System.out.print("[WARNING] ");
			fresh ^= true;
			System.out.print(chars);
		}

	}
	
	private static final class ERROR
	 					 extends BasicLogger {

		private boolean fresh = true;

		public void println()
		{
			fresh = true;
			System.out.println();
		}

		protected void add(char ch)
		{
			if(fresh)
				System.out.print("[ERROR] ");
			fresh ^= true;
			System.out.print(ch);
		}

		protected void add(char[] chars)
		{
			if(fresh)
				System.out.print("[ERROR] ");
			fresh ^= true;
			System.out.print(chars);
		}

	}
	
	private static final class CRITICAL
				         extends BasicLogger {

		private boolean fresh = true;
		
		public void println()
		{
			fresh = true;
			System.out.println();
		}

		protected void add(char ch)
		{
		if(fresh)
				System.out.print("[CRITICAL] ");
			fresh ^= true;
			System.out.print(ch);
		}

		protected void add(char[] chars)
		{
			if(fresh)
				System.out.print("[CRITICAL] ");
			fresh ^= true;
			System.out.print(chars);
		}
	
	}
	
}

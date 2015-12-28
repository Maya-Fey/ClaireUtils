package claire.util.logging;

import claire.util.standards.io.ILogManager;
import claire.util.standards.io.ILogger;

public class Log {
	
	public static ILogger info;
	public static ILogger warn;
	public static ILogger err;
	public static ILogger crit;
	
	private static ILogManager manager;
	
	static
	{
		setOut(new StandardOut());
	}
	
	public static final void setOut(ILogManager manager)
	{
		Log.manager = manager;
		info = manager.getInfo();
		warn = manager.getWarning();
		err = manager.getError();
		crit = manager.getCritical();
	}
	
	public static final ILogManager getLogManager()
	{
		return manager;
	}

}

package claire.util.logging;

import claire.util.standards.io.ILogManager;
import claire.util.standards.io.ILogger;

public class Log {
	
	public static ILogger debug;
	public static ILogger info;
	public static ILogger warn;
	public static ILogger err;
	public static ILogger crit;
	public static Version version;
	
	private static ILogManager manager;
	
	static
	{
		setOut(new StandardOut());
		manager.addLogListener(new StandardOutListener());
	}
	
	public static final void setOut(ILogManager manager)
	{
		Log.manager = manager;
		debug = manager.getDebug();
		info = manager.getInfo();
		warn = manager.getWarning();
		err = manager.getError();
		crit = manager.getCritical();
	}
	
	public static final ILogManager getLogManager()
	{
		return manager;
	}
	
	public static final void setLogLevel(int level)
	{
		manager.setLogLevel(level);
	}
	
	public static final void addLogListener(ILogListener listener)
	{
		manager.addLogListener(listener);
	}
	
	public static final void remLogListener(ILogListener listener)
	{
		manager.remLogListener(listener);
	}
	
	public static void setVersion(Version version)
	{
		Log.version = version;
	}
	
	public static Version getVersion()
	{
		return Log.version;
	}
	
	

}

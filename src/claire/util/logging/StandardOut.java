package claire.util.logging;

import claire.util.memory.util.ArrayUtil;
import claire.util.standards.io.ILogManager;
import claire.util.standards.io.ILogger;

public class StandardOut 
	   implements ILogManager {
	
	private boolean sD, sI, sW, sE, sC;

	private final ILogger debug = new DEBUG();
	private final ILogger info = new INFO();
	private final ILogger warn = new WARN();
	private final ILogger error = new ERROR();
	private final ILogger crit = new CRITICAL();
	
	private ILogListener[] listeners = new ILogListener[4];
	private int lpos;

	{
		sI = sW = sE = sC = true;
		sD = false;
	}
	
	public void addLogListener(ILogListener listener) 
	{
		if(lpos == listeners.length)
			listeners = ArrayUtil.upsize(listeners, 4);
		listeners[lpos++] = listener;
	}
	
	public void remLogListener(ILogListener listener)
	{
		for(int i = 0; i < lpos; i++) {
			if(listeners[i] == listener) {
				System.arraycopy(listeners, i + 1, listeners, i, --lpos - i);
				break;
			}
		}
	}
	
	public void setLogLevel(int level)
	{
		sD = (level & 1 ) != 0;
		sI = (level & 2 ) != 0;
		sW = (level & 4 ) != 0;
		sE = (level & 8 ) != 0;
		sC = (level & 16) != 0;
	}
	
	private void out(String s)
	{
		for(int i = 0; i < lpos; i++)
			listeners[i].println(s);
	}
	
	private void debug(String s)
	{
		if(sD)
			out(s);
	}
	
	private void info(String s)
	{
		if(sI)
			out(s);
	}
	
	private void warn(String s)
	{
		if(sW)
			out(s);
	}
	
	private void error(String s)
	{
		if(sE)
			out(s);
	}
	
	private void critical(String s)
	{
		if(sC)
			out(s);
	}
	
	public ILogger getDebug()
	{
		return debug;
	}
	
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
	
	private final class DEBUG
				  implements ILogger {

		public void println(String s)
		{
			StandardOut.this.debug("[DEBUG] " + s);
		}		
		
	}
	
	private final class INFO
				  implements ILogger {
		
		public void println(String s)
		{
			StandardOut.this.info("[INFO] " + s);
		}		
		
	}
	
	private final class WARN
		  		  implements ILogger {
	
		public void println(String s)
		{
			StandardOut.this.warn("[WARN] " + s);
		}		
	
	}
	
	private final class ERROR
		  		  implements ILogger {
	
		public void println(String s)
		{
			StandardOut.this.error("[ERROR] " + s);
		}		
	
	}
	
	private final class CRITICAL
				  implements ILogger {
		
		public void println(String s)
		{
			StandardOut.this.critical("[CRITICAL] " + s);
		}		
	
	}
	
}

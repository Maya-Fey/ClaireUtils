package claire.util.logging;

import claire.util.encoding.CString;
import claire.util.standards.io.IEncoder;
import claire.util.standards.io.ILogger;
import claire.util.standards.io.IOutgoingStream;
import claire.util.standards.io.ITextWriter;

public class LogWriter 
	   implements ITextWriter {
	
	private final ILogger log;
	
	public LogWriter(ILogger log)
	{
		this.log = log;
	}

	public void useEncoder(IEncoder enc)
	{
		throw new java.lang.UnsupportedOperationException();
	}

	public void setStream(IOutgoingStream os)
	{
		throw new java.lang.UnsupportedOperationException();
	}

	public void newline()
	{
		log.println();
	}

	public void print(char c) 
	{
		log.print(c);
	}

	public void print(char[] chars) 
	{
		log.print(chars);
	}

	public void print(String s) 
	{
		log.print(s);
	}

	public void print(CString s) 
	{
		log.print(s);
	}

	public void println(char c)
	{
		log.println(c);
	}

	public void println(char[] chars) 
	{
		log.println(chars);
	}

	public void println(String s)
	{
		log.println(s);
	}

	public void println(CString s) 
	{
		log.println(s);
	}

}

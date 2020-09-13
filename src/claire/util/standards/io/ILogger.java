package claire.util.standards.io;

public interface ILogger {
	
	void println(String s);
	
	default void println(char[] chars)
	{
		println(new String(chars));
	}
	
	default void printStackTrace(StackTraceElement[] ele)
	{
		for(int i = 0; i < ele.length; i++)
			println("at " + ele[i].toString());
	}
	
}

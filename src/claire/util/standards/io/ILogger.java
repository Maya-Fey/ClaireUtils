package claire.util.standards.io;

import claire.util.encoding.CString;
import claire.util.standards.IDebuggable;

public interface ILogger {
	
	void print(String s);
	void print(CString s);
	void print(char ... chars);
	void print(byte ... bytes);
	void print(boolean bool);
	void print(byte b);
	void print(short b);
	void print(char b);
	void print(int b);
	void print(long b);
	void print(Object o);
	void print(IDebuggable o);
	
	default void print(String ... s)
	{
		for(String str : s)
			this.print(str);
	}
	
	default void print(CString ... s)
	{
		for(CString str : s)
			this.print(str);
	}
	
	default void print(Object ... obj)
	{
		for(Object o : obj)
			this.print(o);
	}
	
	
	void println();
	void println(String s);
	void println(CString s);
	void println(char ... chars);
	void println(byte ... bytes);
	void println(boolean bool);
	void println(byte b);
	void println(short b);
	void println(char b);
	void println(int b);
	void println(long b);
	void println(Object o);
	void println(IDebuggable o);
	
	default void println(String ... s)
	{
		for(String str : s)
			this.println(str);
	}
	
	default void println(CString ... s)
	{
		for(CString str : s)
			this.println(str);
	}
	
	default void println(Object ... obj)
	{
		for(Object o : obj)
			this.println(o);
	}
}

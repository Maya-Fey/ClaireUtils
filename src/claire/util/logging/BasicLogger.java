package claire.util.logging;

import claire.util.encoding.Base10;
import claire.util.encoding.CString;
import claire.util.encoding.EncodingUtil;
import claire.util.encoding.Hex;
import claire.util.standards.IDebuggable;
import claire.util.standards.io.ILogger;

public abstract class BasicLogger 
				implements ILogger {
	
	protected abstract void add(char ch);
	protected abstract void add(char[] chars);

	public void print(String s)
	{
		add(s.toCharArray());
	}

	public void print(CString s)
	{
		add(s.array());
	}

	public void print(char ... chars)
	{
		add(chars);
	}

	public void print(byte ... bytes)
	{
		add(Hex.toHex(bytes));
	}

	public void print(boolean bool)
	{
		add(EncodingUtil.booleanText(bool));
	}

	public void print(byte b)
	{
		add(Base10.fromByte(b));
	}

	public void print(short b)
	{
		add(Base10.fromShort(b));
	}

	public void print(char b)
	{
		add(b);
	}

	public void print(int b)
	{
		add(Base10.fromInt(b));
	}

	public void print(long b)
	{
		add(Base10.fromLong(b));
	}

	public void print(Object o)
	{
		add(o.toString().toCharArray());
	}
	
	public void print(IDebuggable o)
	{
		add(o.toCString().array());
	}

	public void println(String s)
	{
		this.print(s);
		this.println();
	}

	public void println(CString s)
	{
		this.print(s);
		this.println();
	}

	public void println(char ... chars)
	{
		this.print(chars);
		this.println();
	}

	public void println(byte ... bytes)
	{
		this.print(bytes);
		this.println();
	}

	public void println(boolean bool)
	{
		this.print(bool);
		this.println();
	}

	public void println(byte b)
	{
		this.print(b);
		this.println();
	}

	public void println(short b)
	{
		this.print(b);
		this.println();
	}

	public void println(char b)
	{
		this.print(b);
		this.println();
	}

	public void println(int b)
	{
		this.print(b);
		this.println();
	}

	public void println(long b)
	{
		this.print(b);
		this.println();
	}

	public void println(Object o)
	{
		this.print(o);
		this.println();
	}
	
	public void println(IDebuggable o)
	{
		this.print(o);
		this.println();
	}

}

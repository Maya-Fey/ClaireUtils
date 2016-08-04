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

	public final void print(String s)
	{
		add(s.toCharArray());
	}

	public final void print(CString s)
	{
		add(s.array());
	}

	public final void print(char ... chars)
	{
		add(chars);
	}

	public final void print(byte ... bytes)
	{
		add(Hex.toHex(bytes));
	}

	public final void print(boolean bool)
	{
		add(EncodingUtil.booleanText(bool));
	}

	public final void print(byte b)
	{
		add(Base10.fromByte(b));
	}

	public final void print(short b)
	{
		add(Base10.fromShort(b));
	}

	public final void print(char b)
	{
		add(b);
	}

	public final void print(int b)
	{
		add(Base10.fromInt(b));
	}

	public final void print(long b)
	{
		add(Base10.fromLong(b));
	}

	public final void print(Object o)
	{
		add(o.toString().toCharArray());
	}
	
	public final void print(IDebuggable o)
	{
		add(o.toCString().array());
	}

	public final void println(String s)
	{
		this.print(s);
		this.println();
	}

	public final void println(CString s)
	{
		this.print(s);
		this.println();
	}

	public final void println(char ... chars)
	{
		this.print(chars);
		this.println();
	}

	public final void println(byte ... bytes)
	{
		this.print(bytes);
		this.println();
	}

	public final void println(boolean bool)
	{
		this.print(bool);
		this.println();
	}

	public final void println(byte b)
	{
		this.print(b);
		this.println();
	}

	public final void println(short b)
	{
		this.print(b);
		this.println();
	}

	public final void println(char b)
	{
		this.print(b);
		this.println();
	}

	public final void println(int b)
	{
		this.print(b);
		this.println();
	}

	public final void println(long b)
	{
		this.print(b);
		this.println();
	}

	public final void println(Object o)
	{
		this.print(o);
		this.println();
	}
	
	public final void println(IDebuggable o)
	{
		this.print(o);
		this.println();
	}

}

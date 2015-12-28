package claire.util.io.setting;

import claire.util.encoding.CString;

public class LongSetting extends Setting {
	
	public LongSetting(String name) 
	{
		super(name);
	}
	
	public LongSetting(String name, long def) 
	{
		super(name);
		this.setDefaultValue(def);
	}

	public void setDefaultValue(long value)
	{
		this.setDefault(new CString(value));
	}
	
	public void set(long i)
	{
		this.set(new CString(i));
	}
	
	public long get()
	{
		return this.value().toLong();
	}

}

package claire.util.io.setting;

import claire.util.encoding.CString;

public class IntSetting extends Setting {
	
	public IntSetting(String name) 
	{
		super(name);
	}
	
	public IntSetting(String name, int def) 
	{
		super(name);
		this.setDefaultValue(def);
	}

	public void setDefaultValue(int value)
	{
		this.setDefault(new CString(value));
	}
	
	public void set(int i)
	{
		this.set(new CString(i));
	}
	
	public int get()
	{
		return this.value().toInt();
	}

}

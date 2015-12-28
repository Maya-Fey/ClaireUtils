package claire.util.io.setting;

import claire.util.encoding.CString;

public class StringSetting extends Setting {

	public StringSetting(String name) 
	{
		super(name);
	}
	
	public StringSetting(String name, CString def) 
	{
		super(name);
		this.setDefaultValue(def);
	}
	
	public void setDefaultValue(CString s)
	{
		super.setDefault(s);
	}
	
	public void set(CString s)
	{
		super.set(s);
	}
	
	public CString get()
	{
		return this.value();
	}

}

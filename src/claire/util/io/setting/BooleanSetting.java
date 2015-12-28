package claire.util.io.setting;

import claire.util.encoding.CString;
import claire.util.encoding.EncodingUtil;

public class BooleanSetting extends Setting {

	public BooleanSetting(String name) 
	{
		super(name);
	}
	
	public BooleanSetting(String name, boolean def) 
	{
		super(name);
		this.setDefaultValue(def);
	}

	public void setDefaultValue(boolean b)
	{
		super.setDefault(new CString(b));
	}
	
	public void set(boolean b)
	{
		this.set(new CString(b));
	}
	
	public boolean get()
	{
		return EncodingUtil.parseBoolean(this.value());
	}

}

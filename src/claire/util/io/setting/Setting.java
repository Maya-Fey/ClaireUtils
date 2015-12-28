package claire.util.io.setting;

import claire.util.encoding.CString;

public abstract class Setting {
	
	private Settings owner;
	private boolean hasDefault = false;
	private CString defaultVal;
	
	private final String name;
	
	protected Setting(String name)
	{
		this.name = name;
	}
	
	protected final void setDefault(CString val)
	{
		this.defaultVal = val;
		this.hasDefault = true;
	}
	
	void init(Settings owner)
	{
		this.owner = owner;
		try {
			owner.getSetting(name);
		} catch (java.lang.NullPointerException e) {
			if(hasDefault)
				owner.add(name, defaultVal);
			else
				throw new java.lang.ExceptionInInitializerError("Setting " + name + " was not set a value.");
		}
	}

	CString value()
	{
		return this.owner.getSetting(name);
	}
	
	void set(CString s)
	{
		this.owner.edit(name, s);
	}

}

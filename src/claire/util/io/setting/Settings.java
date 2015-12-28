package claire.util.io.setting;

import java.io.File;

import claire.util.encoding.CString;
import claire.util.io.LinkedFile;

public class Settings extends LinkedFile {
	
	final Setting[] settings;

	public Settings(File file, CString separator, Setting ... settings) throws Exception {
		super(file, separator);
		this.settings = settings;
		for(Setting s : settings)
			s.init(this);
	}
	
}

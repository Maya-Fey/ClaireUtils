package claire.util.display.component;

import claire.util.display.message.StringCollectionPanel;
import claire.util.encoding.EncodingUtil;
import claire.util.memory.util.Pointer;

public class FilenameCollectionPanel 
	   extends StringCollectionPanel {

	private static final long serialVersionUID = 8128288197926784002L;
	private static final char[] fb = new char[] { '\\', '/' };
	
	public FilenameCollectionPanel(String text)
	{
		super(text);
	}
	
	public boolean error(Pointer<String> p)
	{
		if(EncodingUtil.contains(this.field.getText(), fb)) {
			p.set("File string contains '\\' or '/' characters. Please enter name only.");
			return true;
		}
		return false;
	}
	
}

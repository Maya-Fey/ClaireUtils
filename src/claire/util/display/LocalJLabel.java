package claire.util.display;

import javax.swing.JLabel;

import claire.util.standards.ITranslatable;
import claire.util.standards.ITranslator;

public class LocalJLabel 
	   extends JLabel 
	   implements ITranslatable {

	private static final long serialVersionUID = 2221451966294119661L;
	
	private final String ID;
	private final ITranslator parent;
	
	public LocalJLabel(ITranslator parent, String ID)
	{
		this.parent = parent;
		parent.addChild(this);
		this.ID = ID;
		this.setText(parent.localize(ID));
	}

	public void languageChanged()
	{
		this.setText(parent.localize(ID));
	}


}

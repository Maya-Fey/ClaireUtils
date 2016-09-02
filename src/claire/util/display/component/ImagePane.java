package claire.util.display.component;

import javax.swing.JComponent;

public abstract class ImagePane 
				extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	protected abstract void drawImage();
}

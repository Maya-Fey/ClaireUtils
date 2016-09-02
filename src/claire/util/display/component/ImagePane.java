package claire.util.display.component;

import java.awt.Image;

import javax.swing.JComponent;

public abstract class ImagePane 
				extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	protected Image img;
	
	protected abstract void drawImage();

	public void setImage(Image img)
	{
		this.img = img;
	}
	
}

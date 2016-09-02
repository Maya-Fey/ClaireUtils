package claire.util.display.component.image;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import claire.util.display.component.ImagePane;

public class StretchImagePane
	   extends ImagePane
{

	private static final long serialVersionUID = -2530653933232324940L;

	protected void drawImage(Graphics g)
	{
		Dimension d = this.getSize();
        g.drawImage(img.getScaledInstance((int) d.getWidth(), (int) d.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
	}
	
}

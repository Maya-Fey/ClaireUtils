package claire.util.display.component.image;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import claire.util.display.component.ImagePane;

public class CenterFitImagePane
	   extends ImagePane
{
	private static final long serialVersionUID = 8442392182514668627L;

	protected void drawImage(Graphics g)
	{
		Dimension d = this.getSize();
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int x = 0;
        int y = 0;
        int sh = (int) d.getHeight();
        int sw = (sh * w) / h;
        if(sw > d.getWidth()) {
        	sw = (int) d.getWidth();
        	sh = (sw * h) / w;
        	y = (int) ((d.getHeight() - sh) / 2);
        } else
        	x = (int) ((d.getWidth() - sw) / 2);
        g.drawImage(img.getScaledInstance(sw, sh, Image.SCALE_SMOOTH), x, y, null);
	}
	
}

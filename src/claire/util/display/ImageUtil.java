package claire.util.display;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class ImageUtil {
	
	public static BufferedImage imageFromBytes(byte[] bytes, int width, int height) throws IOException
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] raw = new int[bytes.length / 3];
		for (int i = 0; i < bytes.length;) {
		    raw[i / 3] = 0xFF000000 | 
		        ((bytes[i++] & 0xFF) << 16) |
		        ((bytes[i++] & 0xFF) << 8) |
		        ((bytes[i++] & 0xFF));
		}
		image.setRGB(0, 0, width, height, raw, 0, width);
		return image;
	}
	
	public static BufferedImage bwImageFromBytes(byte[] bytes, int width, int height) throws IOException
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] raw = new int[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
		    raw[i] = 0xFF000000 | 
		        ((bytes[i] & 0xFF) << 16) |
		        ((bytes[i] & 0xFF) << 8) |
		        ((bytes[i] & 0xFF));
		}
		image.setRGB(0, 0, width, height, raw, 0, width);
		return image;
	}
	
	public static void saveImage(Image image, File target) throws IOException
	{
		ImageIO.write(renderImage(image), "bmp", target);
	}
	
	public static void saveImage(BufferedImage image, File target) throws IOException
	{
		ImageIO.write(image, "bmp", target);
	}
	
	public static BufferedImage renderImage(Image in)
    {
		int h = in.getHeight(null);
		int w = in.getWidth(null);
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage out = new BufferedImage(w, h, type);
        Graphics2D g2 = out.createGraphics();
        g2.drawImage(in, 0, 0, null);
        g2.dispose();
        return out;
    }

}

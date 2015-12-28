package claire.util.display;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public final class DisplayHelper {

	public static JMenuItem getActionMenu(ActionListener listener, String s)
	{
		JMenuItem item = new JMenuItem(s);
		item.addActionListener(listener);
		return item;
	}
	
	public static JScrollPane getScrollPane(Component c, int vert, int horz)
	{
		return new JScrollPane(c, vert, horz);
	}
	
	public static Border uniformBorder(int size)
	{
		return new EmptyBorder(size, size, size, size);
	}
	
	public static JPanel nestBorder(Component c, Border b)
	{
		JPanel panel = new JPanel();
		panel.add(c);
		panel.setBorder(b);
		return panel;
	}
	
	public static void addBorder(JComponent c, Border b)
	{
		Border b1 = c.getBorder();
		c.setBorder(new CompoundBorder(b, b1));
	}
	
	public static void center(Window window)
	{
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		final int X = (int) ((screen.getWidth() - window.getWidth()) / 2);
		final int Y = (int) ((screen.getHeight() - window.getHeight()) / 2);
		window.setLocation(X, Y);
	}
	
	public static void showError(Window parent, String message)
	{
		ErrorMessage error = new ErrorMessage(parent, message);
		center(error);
		error.start();
	}
	
}

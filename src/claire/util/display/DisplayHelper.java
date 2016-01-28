package claire.util.display;

import java.awt.BorderLayout;
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

import claire.util.display.message.ConfirmMessage;
import claire.util.display.message.ErrorMessage;

public final class DisplayHelper {
	
	public static boolean confirm(final Window window, final String title, final String message)
	{
		final ConfirmMessage m = new ConfirmMessage(window, title, message);
		DisplayHelper.center(m);
		m.start();
		return m.isOk();
	}

	public static JMenuItem getActionMenu(final ActionListener listener, final String s)
	{
		final JMenuItem item = new JMenuItem(s);
		item.addActionListener(listener);
		return item;
	}
	
	public static JScrollPane getScrollPane(final Component c, final int vert, final int horz)
	{
		return new JScrollPane(c, vert, horz);
	}
	
	public static Border uniformBorder(final int size)
	{
		return new EmptyBorder(size, size, size, size);
	}
	
	public static JPanel nestBorder(final Component c, final Border b)
	{
		final JPanel panel = new JPanel();
		panel.add(c);
		panel.setBorder(b);
		return panel;
	}
	
	public static JPanel nestBorderWide(final Component c, final Border b)
	{
		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(c, BorderLayout.CENTER);
		panel.setBorder(b);
		return panel;
	}
	
	public static void addBorder(final JComponent c, final Border b)
	{
		final Border b1 = c.getBorder();
		c.setBorder(new CompoundBorder(b, b1));
	}
	
	public static JPanel widen(final Component c)
	{
		final JPanel p = new JPanel(new BorderLayout());
		p.add(c, BorderLayout.CENTER);
		return p;
	}
	
	public static void center(final Window window)
	{
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		final int X = (int) ((screen.getWidth() - window.getWidth()) / 2);
		final int Y = (int) ((screen.getHeight() - window.getHeight()) / 2);
		window.setLocation(X, Y);
	}
	
	public static void showError(final Window parent, final String message)
	{
		final ErrorMessage error = new ErrorMessage(parent, message);
		center(error);
		error.start();
	}
	
}

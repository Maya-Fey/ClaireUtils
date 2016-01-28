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
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import claire.util.display.message.ConfirmMessage;
import claire.util.display.message.ErrorMessage;

public final class DisplayHelper {
	
	/**
	 * Shows a confirmation dialog to collect a yes or no answer
	 * from the user. If yes it will return true.
	 * <br><br>
	 * This method will block until the user or closes the dialog.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A parent window</li>
	 * <li>A title string</li>
	 * <li>A question string</li>
	 * </ul>
	 * This method is safe and will work for any non-null inputs.
	 * <br><br>
	 * Returns: The users answer as a boolean
	 */
	public static boolean confirm(final Window window, final String title, final String message)
	{
		final ConfirmMessage m = new ConfirmMessage(window, title, message);
		DisplayHelper.center(m);
		m.start();
		return m.isOk();
	}

	/**
	 * Returns a JMenuItem to which the given ActionListener will respond too.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An ActionListener</li>
	 * <li>A String</li>
	 * </ul>
	 * This method is safe and will work for any non-null inputs.
	 * <br><br>
	 * Returns: A JMenuItem with the given name.
	 */
	public static JMenuItem getActionMenu(final ActionListener listener, final String s)
	{
		final JMenuItem item = new JMenuItem(s);
		item.addActionListener(listener);
		return item;
	}
	
	/**
	 * Returns a uniform border of the given size.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>An integer greater then zero</li>
	 * </ul>
	 * If the integer is negative the behavior of this method is undefined.
	 * <br><br>
	 * Returns: A uniform EmptyBorder of the given size.
	 */
	public static Border uniformBorder(final int size)
	{
		return new EmptyBorder(size, size, size, size);
	}
	
	/**
	 * Returns your component with the given border
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A component</li>
	 * <li>A border</li>
	 * </ul>
	 * This method is safe and should work with any non-null values.
	 * <br><br>
	 * Returns: A JPanel containing your component, surrounded by your border.
	 */
	public static JPanel nestBorder(final Component c, final Border b)
	{
		final JPanel panel = new JPanel();
		panel.add(c);
		panel.setBorder(b);
		return panel;
	}
	
	/**
	 * Returns your component with the given border, while making sure your 
	 * component can still expand to fill all available space.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A component</li>
	 * <li>A border</li>
	 * </ul>
	 * This method is safe and should work with any non-null values.
	 * <br><br>
	 * Returns: A JPanel containing your component, surrounded by your border.
	 */
	public static JPanel nestBorderWide(final Component c, final Border b)
	{
		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(c, BorderLayout.CENTER);
		panel.setBorder(b);
		return panel;
	}
	
	/**
	 * Returns your component with the given border
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A component</li>
	 * <li>A border</li>
	 * </ul>
	 * This method is safe and should work with any non-null values.
	 * <br><br>
	 * Returns: null
	 */
	public static void addBorder(final JComponent c, final Border b)
	{
		final Border b1 = c.getBorder();
		c.setBorder(new CompoundBorder(b, b1));
	}
	
	/**
	 * Centers a window on the screen
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A window</li>
	 * </ul>
	 * This method is safe and should work with any non-null values.
	 * <br><br>
	 * Returns: null
	 */
	public static void center(final Window window)
	{
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		final int X = (int) ((screen.getWidth() - window.getWidth()) / 2);
		final int Y = (int) ((screen.getHeight() - window.getHeight()) / 2);
		window.setLocation(X, Y);
	}
	
	/**
	 * Shows an error with the given message.
	 * <br><br>
	 * Note: This method will block until the users closes the dialog.
	 * <br><br>
	 * Expects:
	 * <ul>
	 * <li>A window</li>
	 * <li>A message</li>
	 * </ul>
	 * This method is safe and should work with any non-null values.
	 * <br><br>
	 * Returns: null
	 */
	public static void showError(final Window parent, final String message)
	{
		final ErrorMessage error = new ErrorMessage(parent, message);
		center(error);
		error.start();
	}
	
}

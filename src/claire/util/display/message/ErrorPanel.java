package claire.util.display.message;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import claire.util.display.DisplayHelper;
import claire.util.display.component.TablePane;
import claire.util.display.component.WrappedLabel;

public class ErrorPanel 
	   extends TablePane {
	
	private static final long serialVersionUID = -2811137992288778513L;
	private static final Border border = DisplayHelper.uniformBorder(5);
	
	private final JButton button;
	
	public ErrorPanel(String error)
	{
		super(GridBagConstraints.BOTH);
		WrappedLabel label = new WrappedLabel(error);
		DisplayHelper.addBorder(label, border);
		JButton button = new JButton("OK");
		this.newRow(1.0D);
		this.newCol(label, 2, 1.0D);
		this.newRow();
		this.newCol(new JPanel(), 1.0D);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(button, BorderLayout.CENTER);
		panel.setBorder(new EmptyBorder(4, 10, 4, 10));
		this.newCol(panel, 0.3D);
		this.button = button;
	}
	
	public JButton getButton()
	{
		return this.button;
	}

}

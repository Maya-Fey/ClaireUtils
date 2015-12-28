package claire.util.display;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ErrorPanel 
	   extends TableLayout {
	
	private static final long serialVersionUID = -2811137992288778513L;
	
	private final JButton button;
	
	public ErrorPanel(String error)
	{
		super(GridBagConstraints.BOTH);
		JLabel label = new JLabel(error, SwingConstants.CENTER);
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

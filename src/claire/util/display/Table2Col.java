package claire.util.display;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Table2Col {
	
	private JPanel panel = new JPanel(new GridBagLayout());
	private final GridBagConstraints con = new GridBagConstraints();
	
	public Table2Col(JPanel owner)
	{
		owner.add(panel);
		con.fill = GridBagConstraints.HORIZONTAL;
	}
	
	public void addRow(String name, Component comp)
	{
		JLabel label = new JLabel(name);
		this.addRow(label, comp);
	}
	
	public void addRow(Component c1, Component c2)
	{
		GridBagConstraints con = new GridBagConstraints();
		con.gridy++;
		con.weightx = 0.0D;
		panel.add(c1, con);
		con.weightx = 1.0D;
		panel.add(c2, con);
	}

}

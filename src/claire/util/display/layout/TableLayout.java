package claire.util.display.layout;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class TableLayout {
	
	private final GridBagConstraints con = new GridBagConstraints();
	private final JPanel panel;

	public TableLayout(JPanel panel) 
	{
		this.panel = panel;
		panel.setLayout(new GridBagLayout());
		con.fill = GridBagConstraints.HORIZONTAL;
		con.gridx = 0;
	}
	
	public TableLayout(JPanel panel, int fill) 
	{
		this.panel = panel;
		panel.setLayout(new GridBagLayout());
		con.fill = fill;
		con.gridx = 0;
	}
	
	public void newRow()
	{
		con.weighty = 0.0D;
		con.gridy++;
		con.gridx = 0;
	}
	
	public void newRow(double weight)
	{
		con.weighty = weight;
		con.gridy++;
		con.gridx = 0;
	}
	
	public void newCol(Component com)
	{
		con.weightx = 0.0000001D;
		panel.add(com, con);
		con.gridx++;
	}
	
	public void newCol(Component com, double weight)
	{
		con.weightx = weight;
		panel.add(com, con);
		con.gridx++;
	}
	
	public void newCol(Component com, int width)
	{
		con.weightx = 0.0000001D;
		con.gridwidth = width;
		panel.add(com, con);
		con.gridwidth = 1;
		con.gridx += width;
	}
	
	public void newCol(Component com, int width, double weight)
	{
		con.weightx = weight;
		con.gridwidth = width;
		panel.add(com, con);
		con.gridwidth = 1;
		con.gridx += width;
	}


}

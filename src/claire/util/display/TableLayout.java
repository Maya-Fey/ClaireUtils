package claire.util.display;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class TableLayout 
	   extends JPanel {

	private static final long serialVersionUID = -6912515493480346058L;
	
	private final GridBagConstraints con = new GridBagConstraints();

	public TableLayout() 
	{
		super(new GridBagLayout());
		con.fill = GridBagConstraints.HORIZONTAL;
		con.gridx = 0;
	}
	
	public TableLayout(int fill) 
	{
		super(new GridBagLayout());
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
		this.add(com, con);
		con.gridx++;
	}
	
	public void newCol(Component com, double weight)
	{
		con.weightx = weight;
		this.add(com, con);
		con.gridx++;
	}
	
	public void newCol(Component com, int width)
	{
		con.weightx = 0.0000001D;
		con.gridwidth = width;
		this.add(com, con);
		con.gridwidth = 1;
		con.gridx += width;
	}
	
	public void newCol(Component com, int width, double weight)
	{
		con.weightx = weight;
		con.gridwidth = width;
		this.add(com, con);
		con.gridwidth = 1;
		con.gridx += width;
	}


}

package claire.util.display.component;

import java.awt.GridBagConstraints;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

public class ListSelectionPane
	   extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private final JList<String> list;
	
	public ListSelectionPane(String[] elements)
	{
		TablePane table = new TablePane(GridBagConstraints.BOTH);
		list = new JList<String>(elements);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		table.newRow();
		table.newCol(list, 1.0D);
		
		this.add(table);
	}
	
	public boolean hasSelected()
	{
		return list.getSelectedIndex() > -1;
	}
	
	public int getSelection()
	{
		return list.getSelectedIndex();
	}
	
}

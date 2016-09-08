package claire.util.display.component;

import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

public class ListSelectionPane
	   extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private final JList<String> list;
	private final ActionListener onselect;
	
	public ListSelectionPane(ActionListener onselect, String[] elements, String title)
	{
		this.onselect = onselect;
		TablePane table = new TablePane(GridBagConstraints.BOTH);
		list = new JList<String>(elements);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JLabel label = new JLabel(title);
		
		table.newRow();
		table.newCol(label);
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

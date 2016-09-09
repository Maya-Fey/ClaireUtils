package claire.util.display.component;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import claire.util.display.DisplayHelper;

public class ListSelectionPane
	   extends JPanel
	   implements KeyListener
{
	private static final long serialVersionUID = 1L;
	private static final Border border = new EmptyBorder(4, 4, 4, 4);
	
	private final JList<String> list;
	private final ActionListener onselect;
	
	public ListSelectionPane(ActionListener onselect, String[] elements, String title)
	{
		this.onselect = onselect;
		TablePane table = new TablePane(GridBagConstraints.BOTH);
		list = new JList<String>(elements);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addKeyListener(this);
		JLabel label = new JLabel(title);
		
		table.newRow();
		table.newCol(DisplayHelper.nestBorderWide(label, border));
		table.newRow();
		table.newCol(DisplayHelper.nestBorderWide(list, border), 1.0D);
		
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

	public void keyPressed(KeyEvent arg0)
	{
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER && this.hasSelected())
			onselect.actionPerformed(new ActionEvent(this, 0, "sel"));
	}

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	
}

package claire.util.display.component;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import claire.util.display.DisplayHelper;

public class MultiListSelectionPane
	   extends JPanel
	   implements KeyListener
{

	private static final long serialVersionUID = 2315352448715873384L;
	private static final Border border = new EmptyBorder(4, 4, 4, 4);
	
	private final JList<String> list = new JList<String>();
	private final ActionListener onselect;
	
	public MultiListSelectionPane(ActionListener onselect, String[] stuff, String title)
	{
		this.onselect = onselect;
		list.setListData(stuff);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.addKeyListener(this);
		TablePane table = new TablePane(GridBagConstraints.BOTH);
		JLabel label = new JLabel(title);
		
		table.newRow();
		table.newCol(label);
		table.newRow(1.0D);
		table.newCol(DisplayHelper.nestBorderWide(new JScrollPane(list), border), 1.0D);
		
		this.setLayout(new BorderLayout());
		this.add(table, BorderLayout.CENTER);
	}
	
	public boolean hasSelected()
	{
		return list.getSelectedIndex() > -1;
	}
	
	public int[] getSelected()
	{
		return list.getSelectedIndices();
	}

	public void keyPressed(KeyEvent arg0)
	{
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER && this.hasSelected())
			onselect.actionPerformed(new ActionEvent(this, 0, "sel"));
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
}

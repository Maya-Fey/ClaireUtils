package claire.util.display.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class MultiListSelectionPane
	   extends JPanel
	   implements KeyListener
{

	private static final long serialVersionUID = 2315352448715873384L;
	private static final Border border = new EmptyBorder(4, 4, 4, 4);
	
	private final JList<String> list = new JList<String>();
	private final ActionListener onselect;
	
	public MultiListSelectionPane(ActionListener onselect, String[] stuff)
	{
		this.onselect = onselect;
		list.setListData(stuff);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.addKeyListener(this);
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

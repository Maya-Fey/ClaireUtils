package claire.util.display.component;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JList;
import javax.swing.JPanel;

public class MultiListSelectionPane
	   extends JPanel
	   implements KeyListener
{

	private static final long serialVersionUID = 2315352448715873384L;
	
	private final JList<String> list = new JList<String>();
	private final ActionListener onselect;
	
	public MultiListSelectionPane(ActionListener onselect, String[] stuff)
	{
		this.onselect = onselect;
		list.setListData(stuff);
	}
	
	public boolean hasSelected()
	{
		return list.getSelectedIndex() > -1;
	}
	
	public int[] getSelected()
	{
		return list.getSelectedIndices();
	}

	public void keyPressed(KeyEvent e)
	{
	
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
}

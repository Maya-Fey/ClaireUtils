package claire.util.display.component;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class MultiListSelectionPane
	   extends JPanel
{

	private static final long serialVersionUID = 2315352448715873384L;
	
	private final ActionListener onselect;
	
	public MultiListSelectionPane(ActionListener onselect)
	{
		this.onselect = onselect;
	}
	
}

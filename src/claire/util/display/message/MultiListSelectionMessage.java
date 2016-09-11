package claire.util.display.message;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import claire.util.display.component.MultiListSelectionPane;

public class MultiListSelectionMessage
	   extends BasicMessage
	   implements ActionListener
{

	private static final long serialVersionUID = 7603321529435719756L;
	
	private final MultiListSelectionPane pane;
	
	private boolean ok;
	
	public MultiListSelectionMessage(Window arg0, String title, String ok, String message, String[] elements)
	{
		super(arg0, title);
		this.pane = new MultiListSelectionPane(this, elements, message);
	}
	
	public boolean isOk()
	{
		return ok;
	}

	public void actionPerformed(ActionEvent arg0)
	{
		// TODO Fix yo shiet
		
	}
	
}

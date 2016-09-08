package claire.util.display.message;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListSelectionMessage
	   extends BasicMessage
	   implements ActionListener
{

	private static final long serialVersionUID = -8679876580683360350L;

	public ListSelectionMessage(Window arg0, String message)
	{
		super(arg0, message);
	}

	public void actionPerformed(ActionEvent e)
	{
		switch(e.getActionCommand())
		{
			
		}
	}
	
}

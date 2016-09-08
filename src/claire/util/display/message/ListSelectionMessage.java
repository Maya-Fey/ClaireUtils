package claire.util.display.message;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import claire.util.display.component.ListSelectionPane;

public class ListSelectionMessage
	   extends BasicMessage
	   implements ActionListener
{

	private static final long serialVersionUID = -8679876580683360350L;

	private final ListSelectionPane list;
	public ListSelectionMessage(Window arg0, String title, String message, String[] elements)
	{
		super(arg0, title);
		list = new ListSelectionPane(elements, message);
	}

	public void actionPerformed(ActionEvent e)
	{
		switch(e.getActionCommand())
		{
			
		}
	}
	
}

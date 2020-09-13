package claire.util.display.message;

import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import claire.util.display.DisplayHelper;
import claire.util.display.component.MultiListSelectionPane;
import claire.util.display.component.TablePane;

public class MultiListSelectionMessage
	   extends BasicMessage
	   implements ActionListener
{

	private static final long serialVersionUID = 7603321529435719756L;
	private static final Border border = new EmptyBorder(4, 4, 4, 4);
	
	private final MultiListSelectionPane pane;
	
	private boolean ok;
	
	public MultiListSelectionMessage(Window arg0, String title, String OK, String message, String[] elements, boolean cancel)
	{
		super(arg0, title);
		this.pane = new MultiListSelectionPane(this, elements, message);
		TablePane table = new TablePane(GridBagConstraints.BOTH);
		table.newRow(1.0D);
		if(cancel) {
			table.newCol(pane, 3);
			table.newRow();
			table.newCol(new JPanel(), 1.0D);
			JButton ok = new JButton(OK);
			JButton can = new JButton("Cancel");
			ok.setActionCommand("1");
			can.setActionCommand("2");
			ok.addActionListener(this);
			can.addActionListener(this);
			table.newCol(DisplayHelper.nestBorderWide(ok, border), 0.1D);
			table.newCol(DisplayHelper.nestBorderWide(can, border), 0.1D);
		} else {
			table.newCol(pane, 2);
			table.newRow();
			table.newCol(new JPanel(), 1.0D);
			JButton ok = new JButton(OK);
			ok.setActionCommand("1");
			ok.addActionListener(this);
			table.newCol(DisplayHelper.nestBorderWide(ok, border), 0.3D);
		}
		this.setSize(360, 640);
		this.add(table);	
	}
	
	public void start()
	{
		ok = false;
		super.start();
	}
	
	public boolean isOk()
	{
		return ok;
	}
	
	public int[] getSelection()
	{
		return pane.getSelected();
	}

	public void actionPerformed(ActionEvent arg0)
	{
		switch(arg0.getActionCommand())
		{
			case "1":
				if(!pane.hasSelected()) {
					ErrorMessage mes = new ErrorMessage(this, "You have not selected any elements yet");
					DisplayHelper.center(mes);
					mes.start();
					break;
				}
				ok = true;
				this.close();
				break;
			case "2":
				this.close();
				break;
			case "sel":
				ok = true;
				this.close();
				break;
		}
	}
	
}

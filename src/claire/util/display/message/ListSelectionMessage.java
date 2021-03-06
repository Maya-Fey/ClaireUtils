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
import claire.util.display.component.ListSelectionPane;
import claire.util.display.component.TablePane;

public class ListSelectionMessage
	   extends BasicMessage
	   implements ActionListener
{

	private static final long serialVersionUID = -8679876580683360350L;
	private static final Border border = new EmptyBorder(4, 4, 4, 4);

	private final ListSelectionPane list;
	
	private boolean ok;
	
	public ListSelectionMessage(Window arg0, String title, String OK, String message, String[] elements, boolean cancel)
	{
		super(arg0, title);
		list = new ListSelectionPane(this, elements, message);
		TablePane table = new TablePane(GridBagConstraints.BOTH);
		table.newRow(1.0D);
		if(cancel) {
			table.newCol(list, 3);
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
			table.newCol(list, 2);
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
	
	public boolean isOk()
	{
		return ok;
	}
	
	public int getSelected()
	{
		return list.getSelection();
	}

	public void actionPerformed(ActionEvent e)
	{
		switch(e.getActionCommand())
		{
			case "1":
				if(list.hasSelected()) {
					ok = true;
					this.close();
				} else {
					ErrorMessage mes = new ErrorMessage(this, "You have not selected an index");
					DisplayHelper.center(mes);
					mes.start();
				}
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

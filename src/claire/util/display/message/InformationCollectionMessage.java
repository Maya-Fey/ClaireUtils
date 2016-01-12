package claire.util.display.message;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import claire.util.display.DisplayHelper;
import claire.util.display.component.TablePane;
import claire.util.memory.util.Pointer;

public class InformationCollectionMessage 
	   extends BasicMessage 
	   implements ActionListener {

	private static final long serialVersionUID = 832694922303125237L;
	
	private final InformationCollectionPanel panel;
	private final Pointer<String> p = new Pointer<String>();

	public InformationCollectionMessage(Window arg0, InformationCollectionPanel panel, String message, boolean cancel) 
	{
		super(arg0, panel, message);
		this.panel = panel;
		TablePane table = new TablePane();
		table.newRow(1.0D);
		if(cancel) {
			table.newCol(panel, 3);
			table.newRow();
			table.newCol(new JPanel(), 1.0D);
			JButton ok = new JButton("Ok");
			JButton can = new JButton("Cancel");
			ok.setActionCommand("1");
			can.setActionCommand("2");
			ok.addActionListener(this);
			can.addActionListener(this);
			table.newCol(DisplayHelper.nestBorder(ok, new EmptyBorder(4, 10, 4, 10)), 0.3D);
			table.newCol(DisplayHelper.nestBorder(can, new EmptyBorder(4, 10, 4, 10)), 0.3D);
		} else {
			table.newCol(panel, 2);
			table.newRow();
			table.newCol(new JPanel(), 1.0D);
			JButton ok = new JButton("Ok");
			ok.setActionCommand("1");
			ok.addActionListener(this);
			table.newCol(DisplayHelper.nestBorder(ok, new EmptyBorder(4, 10, 4, 10)), 0.3D);
		}
		this.setSize(368, 167);
		this.add(table);
	}

	public void actionPerformed(ActionEvent arg0)
	{
		switch(arg0.getActionCommand())
		{
			case "1":
				if(panel.error(p)) {
					ErrorMessage mes = new ErrorMessage(this.getOwner(), p.get());
					DisplayHelper.center(mes);
					mes.start();
					break;
				} else {
					this.close();
					break;
				}
			case "2":
				if(panel.close(p)) {
					ErrorMessage mes = new ErrorMessage(this.getOwner(), p.get());
					DisplayHelper.center(mes);
					mes.start();
					break;
				} else {
					this.close();
					break;
				}
			default:
				throw new java.lang.AssertionError("Invalid event recieved");
		}
	}

}
package claire.util.display.message;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import claire.util.display.DisplayHelper;
import claire.util.display.component.TablePane;
import claire.util.display.component.WrappedLabel;

public class ConfirmMessage 
	   extends BasicMessage 
	   implements ActionListener {

	private static final long serialVersionUID = 832694922303125237L;
	private static final Border border = DisplayHelper.uniformBorder(5);
	private static final int MIN_WIDTH = 320;
	private static final int MIN_HEIGHT = 142;
	private static final double IDEAL = Math.sqrt(9D / 4D);
	
	private boolean ok = false;

	public ConfirmMessage(Window arg0, String message, String quest) 
	{
		super(arg0, message);
		TablePane table = new TablePane(GridBagConstraints.BOTH);
		WrappedLabel label = new WrappedLabel(quest);
		DisplayHelper.addBorder(label, border);
		table.newRow(1.0D);
		table.newCol(label, 3);
		table.newRow();
		table.newCol(new JPanel(), 1.0D);
		JButton ok = new JButton("Ok");
		JButton can = new JButton("Cancel");
		ok.setActionCommand("1");
		can.setActionCommand("2");
		ok.addActionListener(this);
		can.addActionListener(this);
		table.newCol(DisplayHelper.nestBorderWide(ok, new EmptyBorder(4, 4, 4, 4)), 0.1D);
		table.newCol(DisplayHelper.nestBorderWide(can, new EmptyBorder(4, 4, 4, 4)), 0.1D);
		this.add(table);
		this.pack();
		Dimension dim = table.getPreferredSize();
		double root = Math.sqrt(dim.width * dim.height) + 48;
		int width = (int) (root * IDEAL);
		int height = (int) (root / IDEAL);
		if(width > MIN_WIDTH)
			this.setSize(width, height);
		else
			this.setSize(MIN_WIDTH, MIN_HEIGHT);
	}

	public void actionPerformed(ActionEvent arg0)
	{
		switch(arg0.getActionCommand())
		{
			case "1":
				ok = true;
				this.close();
				break;
			case "2":
				this.close();
				break;
			default:
				throw new java.lang.AssertionError("Invalid event recieved");
		}
	}
	
	public boolean isOk()
	{
		return this.ok;
	}

}
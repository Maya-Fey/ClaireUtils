package claire.util.display.message;

import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import claire.util.display.DisplayHelper;
import claire.util.display.layout.TableLayout;
import claire.util.memory.util.Pointer;

public class StringCollectionPanel 
	   extends InformationCollectionPanel {

	private static final long serialVersionUID = -2920601639095981265L;
	private static final Border border = DisplayHelper.uniformBorder(4);

	protected final JLabel title;
	protected final JTextField field = new JTextField(8);
	
	protected final ActionListener onenter;
	
	public StringCollectionPanel()
	{
		this.title = new JLabel("Please enter string:");
		onenter = null;
		this.initialize();
	}
	
	public StringCollectionPanel(String text)
	{
		this.title = new JLabel(text);
		onenter = null;
		this.initialize();
	}
	
	public void initialize()
	{
		TableLayout layout = new TableLayout(this);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		DisplayHelper.addBorder(title, border);
		layout.newRow();
		layout.newCol(title);
		layout.newRow();
		layout.newCol(DisplayHelper.nestBorderWide(field, border));
	}
	
	public String getString()
	{
		return field.getText();
	}

	public boolean error(Pointer<String> msg)
	{
		return false;
	}

	public boolean close(Pointer<String> msg) 
	{ 
		return false; 
	}

}

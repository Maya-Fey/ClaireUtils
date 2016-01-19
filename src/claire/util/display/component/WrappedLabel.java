package claire.util.display.component;

import javax.swing.JTextArea;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class WrappedLabel 
	   extends JTextArea {

	public WrappedLabel()
	{
		super();
	    init();
	}
	
	public WrappedLabel(String text)
	{
		this();
		this.setText(text);
	}
	
	public WrappedLabel(int rows, int cols)
	{
		super(rows, cols);
		init();
	}
	
	public WrappedLabel(int rows, int cols, String text)
	{
		this(rows, cols);
		this.setText(text);
	}

	private void init()
	{
		this.setWrapStyleWord(true);
	    this.setLineWrap(true);
	    this.setOpaque(false);
	    this.setEditable(false);
	    this.setBackground(UIManager.getColor("Label.background"));
	    this.setFont(UIManager.getFont("Label.font"));
	    this.setBorder(UIManager.getBorder("Label.border"));
	}
}

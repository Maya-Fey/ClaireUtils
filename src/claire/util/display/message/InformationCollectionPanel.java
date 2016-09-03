package claire.util.display.message;

import javax.swing.JPanel;

import claire.util.memory.util.Pointer;

public abstract class InformationCollectionPanel 
	   			extends JPanel {

	private static final long serialVersionUID = -8592273310526993784L;
	
	public abstract void initialize(InformationCollectionMessage m);
	public abstract boolean error(Pointer<String> msg);
	public abstract boolean close(Pointer<String> msg);
	
	public int requestedHeight()
	{
		return 171;
	}
	
	public int requestedWidth()
	{
		return 304;
	}
	
}

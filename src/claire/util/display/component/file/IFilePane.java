package claire.util.display.component.file;

import java.io.File;

/**
 * TODO: Make Advanced*Pane classes extend their basic cousins
 * TODO: Possibly separate FilePane and FolderPane interface
 * @author Claire
 */
public interface IFilePane {
	
	boolean hasSelected();
	
	File getFile();

}

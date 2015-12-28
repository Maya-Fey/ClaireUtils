package claire.util.standards;

import java.util.Iterator;

public interface IIterator<T> extends Iterator<T> {
	
	void skip();
	void skip(int amt);

}

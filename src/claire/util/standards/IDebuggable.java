package claire.util.standards;

import claire.util.encoding.CString;

public interface IDebuggable {
	
	public CString toCString();
	public CString[] getDebugInfo();

}

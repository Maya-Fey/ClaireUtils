package claire.util.standards.io;

import claire.util.logging.ILogListener;

public interface ILogManager {
	
	ILogger getDebug();
	ILogger getInfo();
	ILogger getWarning();
	ILogger getError();
	ILogger getCritical();
	
	/**
	 * Sets the log level. 
	 * <br><br>
	 * Log level is a composite integer containing 5 flags. The flags, from least to 
	 * most significant are:
	 * <br><br>
	 * <ul>
	 * <li>Debug flag, can be set with the first (least significant) bit</li>
	 * <li>Info flag, can be set with the second bit</li>
	 * <li>Warn flag, can be set with the third bit</li>
	 * <li>Error flag, can be set with the fourth bit</li>
	 * <li>Critical flag, can be set with the fifth bit</li>
	 * </ul>
	 * 
	 * The remaining bits are RESERVED for implementations of this interface. Setting flags above 
	 * bit five may result in undefined behavior. To mask out these flags, use the mask 0x1F
	 * <br><br>
	 * Setting a flag to TRUE will cause that output to behave normally. Setting a flag to FALSE
	 * will supress that output.
	 * <br><br>
	 * Use this method to filter unnecessary information from the logging system.
	 */
	void setLogLevel(int level);
	
	void addLogListener(ILogListener listener);
	void remLogListener(ILogListener listener);
	
}

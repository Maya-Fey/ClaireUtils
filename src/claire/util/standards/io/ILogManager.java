package claire.util.standards.io;

public interface ILogManager {
	
	ILogger getInfo();
	ILogger getWarning();
	ILogger getError();
	ILogger getCritical();
	
}

package claire.util.standards;

import claire.util.encoding.CString;

public interface ITranslator {
	
	String localize(String ID);
	CString localizeC(String ID);
	
	void setLang(String language) throws Exception;
	void addChild(ITranslatable t);

}

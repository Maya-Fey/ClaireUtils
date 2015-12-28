package claire.util.encoding;

import claire.util.io.LinkedFile;
import claire.util.memory.array.IndexedRegistry;
import claire.util.memory.util.Pair;
import claire.util.standards.io.IDecoder;
import claire.util.standards.io.IEncoder;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;
import claire.util.standards.io.ITextReader;
import claire.util.standards.io.ITextWriter;

public class FindReplaceParser {
	
	private final IndexedRegistry<CString> search;
	
	public FindReplaceParser(String[] find, String[] replace)
	{
		this.search = new IndexedRegistry<CString>(CString.arrayFrom(replace), find);
	}
	
	public FindReplaceParser(LinkedFile file)
	{
		Pair<String[], CString[]> settings = file.getSettings();
		this.search = new IndexedRegistry<CString>(settings.getPair(), settings.getObject());
	}
	
	public FindReplaceParser(CString[] find, CString[] repl) 
	{
		this.search = new IndexedRegistry<CString>(repl, CString.arrayTo(find));
	}

	public void findAndReplace(IIncomingStream is, IOutgoingStream os, String encoding) throws Exception
	{
		IDecoder decoder = new Decoder(encoding);
		IEncoder encoder = new Encoder(encoding);
		ITextReader reader = new TextReader(decoder, is);
		ITextWriter writer = new TextWriter(encoder, os);
		while(reader.hasLines()) {
			CString line = reader.readLineCString();
			for(int i = 0; i < search.length(); i++)
				line.replaceWith(search.getName(i).toCharArray(), search.get(i).array());
			writer.println(line);
		}
	}

}

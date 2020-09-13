package claire.util.io;

import java.io.IOException;

import claire.util.encoding.UTF8;
import claire.util.memory.util.ArrayUtil;

public class SInTextReader {
	
	byte[] buffer = new byte[200];
	
	public char[] read() throws IOException
	{
		int pos = System.in.read(buffer);
		while((System.in.available()) > 0) {
			if((pos + 200) > buffer.length)
				ArrayUtil.upsize(buffer, 200);
			pos += System.in.read(buffer, pos, 200);
		}
		
		byte byt = buffer[pos - 1];
		if(byt == 0xA || byt == 0xD) {
			byt = buffer[--pos - 1];
			if(byt == 0xA || byt == 0xD)
				pos--;
		}
		
		return UTF8.toUTF16(buffer, 0, pos);
	}

}

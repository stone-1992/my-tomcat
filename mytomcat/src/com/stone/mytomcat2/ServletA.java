package com.stone.mytomcat2;
import java.io.InputStream;
import java.io.OutputStream;
public class ServletA implements Servlet {
	@Override
	public void init() {
		System.err.println(" a servlet init.....");
	}
	@Override
	public void service(InputStream is, OutputStream ops) throws Exception {
		System.err.println(" a servlet service....");
		ops.write("i am a servlet".getBytes());
		ops.flush();
	}
	@Override
	public void destroy() {
		System.err.println(" a servlet destroy ......");
	}
}



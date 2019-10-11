package com.stone.mytomcat2;
import java.io.InputStream;
import java.io.OutputStream;
public class ServletB  implements Servlet {
	@Override
	public void init() {
		System.err.println(" b servlet init.....");
	}
	@Override
	public void service(InputStream is, OutputStream ops) throws Exception {
		System.err.println(" b servlet service....");
		ops.write("i am b servlet".getBytes());
		ops.flush();
	}
	@Override
	public void destroy() {
		System.err.println(" b servlet destroy ......");
	}
}




package com.stone.mytomcat2;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 所以服务端，java小程序要实现的接口
 */
public interface Servlet {
	
	public void init();
	
	public void service (InputStream is,OutputStream ops) throws Exception;
	
	public void destroy();
}

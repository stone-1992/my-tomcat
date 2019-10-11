package com.stone.testBS;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 案例1：模拟Http请求的输出
 * 
 * @author 周泽星
 *
 */
public class TestClient {
	public static void main(String[] args) throws Exception {
		
		// 1.建立一个Socket对象，连接itcastcn域名的80端口
		Socket socket = null;
		
		InputStream is=null;
		
		OutputStream ops = null ;
		try {
			socket = new Socket("www.itcast.cn",80);
			
			// 2.获取输出流对象
			is = socket.getInputStream();
			// 3.获取输入流对象
			ops = socket.getOutputStream();
			// 4.将HTTP协议的请求部分发送到服务端 /subject/about/index.html
			ops.write("GET  /subject/about/index.html HTTP/1.1\n".getBytes());
			ops.write("HOST:www.itcast.cn\n".getBytes());
			ops.write("\n".getBytes());
			// 5.读取来自服务端的数据打印到控制台
			int i = is.read();
			while (i!=-1) {
				System.err.print((char)i);
				i = is.read();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			// 6.释放资源
			if(null != is) {
				is.close();
				is = null;
			}
			if(null != ops) {
				ops.close();
				ops = null;
			}
			
			if(null != socket) {
				socket.close();
				socket = null;
			}
		}
	}
}

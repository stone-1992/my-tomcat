package com.stone.testBS;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 案列2：从浏览器的地址栏输入localhost:8080，向本机的8080端口发起请求，服务端向客户端响应回去HTtp协议的响应部分
 * 
 * 
 */
public class TestServer {

	public static void main(String[] args) throws Exception {
		
		ServerSocket serverSocket = null;
		Socket socket = null;
		OutputStream ops = null;
		try {
			// 1.创建ServerSocket对象，监听本机的8080端口
			serverSocket = new ServerSocket(8080);
			
			while (true) {
				// 2.等待来自客户端的请求获取和客户端对应的Socket对象
				socket = serverSocket.accept();
				
				// 3.通过获取到的Socket对象获取到输出流对象
				ops = socket.getOutputStream();
				
				// 4.通过获取到的输出流对象将HTTP协议的响应部分发送到客户端
				ops.write("HTTP/1.1 200 OK\n".getBytes());
				ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
				ops.write("Server:Apache-Coyoto/1.1\n".getBytes());
				ops.write("\n\n".getBytes());
				
				StringBuffer buffer = new StringBuffer();
				buffer.append("<html>");
				buffer.append("<head><title>我是标题</title></head>");
				buffer.append("<body>");
				buffer.append("<h1> I am Header 1</h1>");
				buffer.append("<h2> I am Header 2</h2>");
				buffer.append("<h3 style='color:red'> I am Header 3</h3>");
				buffer.append("");
				buffer.append("");
				buffer.append("</body>");
				buffer.append("</html>");
				ops.write(buffer.toString().getBytes());
				ops.flush();
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			// 5. 释放资源
			if(null != ops) {
				ops.close();
				ops = null;
			}
			
			if(socket != null) {
				socket.close();
				socket = null;
			}
		}
		
		
	}
}

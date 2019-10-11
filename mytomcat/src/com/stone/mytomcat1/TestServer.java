package com.stone.mytomcat1;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 案例3：访问静态资源
 * 	1.在WebContent下发布静态资源demo.html
 *  2.启动Tomcat服务器
 *  3.当客户端对服务端发起不同的请求，localhost:8080/demo.html
 *  4.服务端可以将对应的HTML页面响应给客户端
 * 
 * 案例4：
 * 	当客户端发送请求到服务端的时候，可以运行服务端的一段java代码，而且可以向客户端响应数据
 * 
 */
public class TestServer {
	
	// 定义一个变量，存放服务端webcontext目录的绝对路径
	public static String WEB_ROOT = System.getProperty("user.dir") + "\\" +"webcontext";
	// 定义静态变量，用于存放本次请求的静态路径名称
	private static String uri = "";
	
	public static void main(String[] args) throws Exception {
		System.err.println(WEB_ROOT);
		
		ServerSocket serverSocket = null;
		Socket socket = null;
		OutputStream ops = null;
		InputStream is = null;
		try {
			// 1.创建ServerSocket对象，监听本机的8080端口
			serverSocket = new ServerSocket(8080);
			
			while (true) {
				// 2.等待来自客户端的请求获取和客户端对应的Socket对象
				socket = serverSocket.accept();
				
				// 3.通过获取到的Socket对象获取到输出流对象
				ops = socket.getOutputStream();
				
				// 3.通过获取到的Socket对象获取到输入流对象
				is = socket.getInputStream();
				
				// 4.读取HTTP协议请求部分的数据，截取客户端要访问的资源名称，将这个资源名称赋值给url
				parset(is);
				
				// 5.发送静态资源
				sendStaticSource(ops);
				
				// 4.通过获取到的输出流对象将HTTP协议的响应部分发送到客户端
				ops.flush();
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			// 5. 释放资源
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

	private static void sendStaticSource(OutputStream ops) throws Exception {
		byte[] bytes = new byte[2048];
		
		FileInputStream fis = null;
		
		try {
			File file = new File(WEB_ROOT,uri);
			if(file.exists()) {
				ops.write("HTTP/1.1 200 OK\n".getBytes());
				ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
				ops.write("Server:Apache-Coyoto/1.1\n".getBytes());
				ops.write("\n\n".getBytes());
				fis = new FileInputStream(file);
				int ch = fis.read(bytes);
				
				while (ch!=-1) {
					ops.write(bytes, 0, ch);
					
					ch = fis.read(bytes); 
				}
			}else {
				ops.write("HTTP/1.1 404 not found\n".getBytes());
				ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
				ops.write("Server:Apache-Coyoto/1.1\n".getBytes());
				ops.write("\n\n".getBytes());
			}
		} catch (Exception e) {
			
		}finally {
			if(null != fis) {
				fis.close();
				fis = null;
			}
		}
	}

	private static void parset(InputStream is) throws Exception {
		// 定义一个变量，存放HTTP协议请求部分数据
		StringBuffer context = new StringBuffer();
		// 定义一个数组，存放HTTP协议请求部分数据
		byte[] buffer = new byte[2048];
		int i = -1;
		i = is.read(buffer);
		for(int j = 0;j<i;j++) {
			context.append((char)buffer[j]);
			System.err.print((char)buffer[j]);
		}
		parseUrl(context.toString());
	}

	private static void parseUrl(String context) {
		int index1;
		int index2;
		index1 = context.indexOf(" ");
		if(index1 != -1) {
			index2 = context.indexOf(" ",index1 + 1);
			if(index2 > index1) {
				uri = context.substring(index1 + 2,index2);
			}
		}
	}
}

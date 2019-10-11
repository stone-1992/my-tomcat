package com.stone.mytomcat2;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 案例4：
 * 	当客户端发送请求到服务端的时候，可以运行服务端的一段java代码，而且可以向客户端响应数据
 */
public class TestServer {
	// conf.properties配置信息
	public static Map<String, String> map = new HashMap<String, String>();
	
	// 定义一个变量，存放服务端webcontext目录的绝对路径
	public static String WEB_ROOT = System.getProperty("user.dir") + "\\" +"webcontext";
	// 定义静态变量，用于存放本次请求的静态路径名称
	private static String uri = "";
	
	static {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(WEB_ROOT+"\\conf.properties"));
			Set keySet = properties.keySet();
			Iterator iterator = keySet.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = (String) properties.get(key);
				
				map.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		System.err.println(map);
		
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
				if(null != uri) {
					if(uri.indexOf(".") != -1) {
						// 5.发送静态资源
						sendStaticSource(ops);
					}else {
						// 5.动态资源
						sendDynamicResource(is,ops);
					}
				}
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

	/**
	 * 动态资源
	 * @param is
	 * @param ops
	 * @throws Exception
	 */
	private static void sendDynamicResource(InputStream is, OutputStream ops) throws Exception {
		// 发送响应头
		ops.write("HTTP/1.1 200 OK\n".getBytes());
		ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
		ops.write("Server:Apache-Coyoto/1.1\n".getBytes());
		ops.write("\n\n".getBytes());
		// 发送响应体
		if(map.containsKey(uri)) {
			String value = map.get(uri);
			Class class1 = Class.forName(value);
			Servlet instance = (Servlet) class1.newInstance();
			instance.init();
			instance.service(is, ops);
		}
	}

	/**
	 * 发送静态资源
	 * @param ops
	 * @throws Exception
	 */
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

	/**
	 * 读取HTTP协议请求部分的数据，截取客户端要访问的资源名称，将这个资源名称赋值给url
	 * @param is
	 * @throws Exception
	 */
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

	/**
	 * 获取请求头中的url
	 * @param context
	 */
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

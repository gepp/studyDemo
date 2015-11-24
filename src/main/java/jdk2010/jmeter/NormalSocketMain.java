package jdk2010.jmeter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class NormalSocketMain {
	public static void main(String[] args) throws IOException {
		ServerSocket server=new ServerSocket(8080);
		System.out.println("系统启动===");
		while(true){
			Socket client=null;
		    client=server.accept();
		    System.out.println("请求----"+client.getRemoteSocketAddress());
		    try {
 				InputStream in = client.getInputStream();
 				OutputStream out = client.getOutputStream();
 				byte[] b = new byte[1024];
 				int length = 0;
 				String s = "";
 				while ((length = in.read(b)) != -1) {
 					s = s + new String(b, 0, length);
 				}
 				 System.out.println("客户端请求：" + s);
 				 out.write((s+":"+UUID.randomUUID().toString()).getBytes());
 				 out.close();
 				 in.close();
 	 
 			} catch (Exception e) {
 				
 				System.out.println("服务器连接超时:" + e.getMessage());
 			} finally {
 				System.out.println("关闭");
 				if (!client.isClosed()) {
 					try {
 						client.close();
 					} catch (IOException e) {
 						// TODO Auto-generated catch block
 						e.printStackTrace();
 					}
 				}
 			}
 		}
	}
}

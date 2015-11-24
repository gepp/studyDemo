package jdk2010.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class ThreadServerClient extends Thread {

	private Socket client;

	public ThreadServerClient(Socket socket) {
		this.client = socket;
		System.out.println("请求----"+socket.getRemoteSocketAddress());
		start();
	}

	@Override
	public void run() {
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
//			if(s.equals("1")){
//				Thread.sleep(3000);
//			}else if(s.equals("2")){
//				Thread.sleep(1000);
//			}
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

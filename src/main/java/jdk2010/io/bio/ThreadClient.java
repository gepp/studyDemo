package jdk2010.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class ThreadClient extends Thread {

	private Socket client;
	private int current;
	public ThreadClient (Socket socket,int current) {
		this.client = socket;
		this.current=current;
		System.out.println("客户端发送请求----"+socket.getRemoteSocketAddress());
		start();
	}

	@Override
	public void run() {
		try {
			InputStream in=client.getInputStream();
			OutputStream out=client.getOutputStream();
			 
	 		out.write((current+"").getBytes());
	  		client.shutdownOutput();
	 		byte[] b=new byte[1024];
			int length=0;
			String s="";
			while((length=in.read(b))!=-1){
				s=s+new String(b,0,length);
			}
 			System.out.println("服务端返回："+s);
			out.close();
			in.close();
 
		} catch (Exception e) {

			System.out.println("服务器连接超时:" + e.getMessage());
		} finally {
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

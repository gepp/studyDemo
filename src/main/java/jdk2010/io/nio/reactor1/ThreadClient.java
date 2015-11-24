package jdk2010.io.nio.reactor1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.UUID;

public class ThreadClient extends Thread {

	private SocketChannel socketChannel;
	private int current;
	public ThreadClient (SocketChannel socket,int current) {
		this.socketChannel = socket;
		this.current=current;
		System.out.println("客户端发送请求----"+current);
		start();
	}
	private static String receive(SocketChannel socketChannel) throws Exception {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int length = 0;
		String username = "";
		while ((length = socketChannel.read(buffer)) != -1) {
			
			buffer.flip();
			username = username
					+ Charset.forName("UTF-8").decode(buffer).toString();
			buffer.clear();
		}

		return username;

	}

	// 发送数据
	private static void send(String username, SocketChannel socketChannel)
			throws Exception {
		ByteBuffer buffer = ByteBuffer.wrap(username.getBytes("utf-8"));
		socketChannel.write(buffer);
		socketChannel.socket().shutdownOutput();
	}

	@Override
	public void run() {
		try {
			 
			socketChannel.connect(new InetSocketAddress("localhost", 9098));
			ByteBuffer buffer = ByteBuffer.wrap((current+"hello world").getBytes("utf-8"));
			socketChannel.write(buffer);
			socketChannel.socket().shutdownOutput();
			String username = receive(socketChannel);
			System.out.println(username);
			
 
		} catch (Exception e) {

 		} finally {
		 
		}
	}
}

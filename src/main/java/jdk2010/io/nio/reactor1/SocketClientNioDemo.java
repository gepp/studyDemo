package jdk2010.io.nio.reactor1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class SocketClientNioDemo {
	public static void main(String[] args) throws Exception {
//		SocketChannel socketChannel = SocketChannel.open();
//		socketChannel.connect(new InetSocketAddress("localhost", 8080));
//		ByteBuffer buffer = ByteBuffer.wrap("ะกร๗".getBytes("utf-8"));
//		socketChannel.write(buffer);
//		socketChannel.socket().shutdownOutput();
//		String username = receive(socketChannel);
//		System.out.println(username);
		
		for(int i=0;i<1;i++){
			SocketChannel socketChannel = SocketChannel.open();
			new ThreadClient(socketChannel,i);
		}
		System.out.println(1 << 2);
		
	}

 
	 

}

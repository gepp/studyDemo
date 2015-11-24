package jdk2010.io.nio2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class SocketClientNioDemo {
	public static void main(String[] args) throws Exception {
//		SocketChannel socketChannel = SocketChannel.open();
//		socketChannel.connect(new InetSocketAddress("localhost", 8080));
//		ByteBuffer buffer = ByteBuffer.wrap("Ð¡Ã÷".getBytes("utf-8"));
//		socketChannel.write(buffer);
//		socketChannel.socket().shutdownOutput();
//		String username = receive(socketChannel);
//		System.out.println(username);
		
		for(int i=0;i<5000;i++){
			SocketChannel socketChannel = SocketChannel.open();
			new ThreadClient(socketChannel,i);
		}
		System.out.println(1 << 3);
		System.out.println("thread:"+Thread.activeCount());
//		
		//System.out.println("1&~1 £º"+(1&~1));
 		
	}

 
	 

}

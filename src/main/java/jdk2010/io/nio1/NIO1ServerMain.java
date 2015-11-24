package jdk2010.io.nio1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

public class NIO1ServerMain {
	public static void main(String[] args) throws IOException {
		ServerSocketChannel serverchannel=ServerSocketChannel.open();
		serverchannel.configureBlocking(false);
		SocketAddress address=new InetSocketAddress(8080);
		serverchannel.bind(address);
		ServerSocket serversocket=serverchannel.socket();
		
		serverchannel.accept();
		System.out.println("abc");	
	}
}

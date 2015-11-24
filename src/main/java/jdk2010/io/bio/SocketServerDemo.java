package jdk2010.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class SocketServerDemo {
	public static void main(String[] args) throws IOException {
		ServerSocket server=new ServerSocket(8080);
		System.out.println("ÏµÍ³Æô¶¯===");
		while(true){
			Socket client=null;
		    client=server.accept();
 		    new ThreadServerClient(client);
 		  
		}
	}
}

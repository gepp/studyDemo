package jdk2010.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClientDemo {
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		for(int i=0;i<5;i++){
		Socket client=new Socket("127.0.0.1",9999);
 		new ThreadClient(client,i);
		}
		
		
	}
}

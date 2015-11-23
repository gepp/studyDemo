package jdk2010.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class SocketServerDemo {
    public static void main(String[] args) throws IOException {
        ServerSocket server=new ServerSocket(9999);
        System.out.println("系统启动===");
        while(true){
            Socket client=null;
            
            client=server.accept();
           
            System.out.println("abc");
            new ThreadServerClient(client);
        }
    }
}
